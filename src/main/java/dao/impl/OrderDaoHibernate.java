package dao.impl;

import common.Constants;
import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.log4j.Log4j2;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;
import model.hibernate.CustomersEntity;
import model.hibernate.MenuItemsEntity;
import model.hibernate.OrderItemsEntity;
import model.hibernate.OrdersEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static common.Constants.ERROR_CONNECTING_TO_DATABASE;
@Log4j2
@Named("orderDAO")
public class OrderDaoHibernate implements OrderDAO {

    private final JPAUtil jpautil;

    private EntityManager entityManager;

    @Inject
    public OrderDaoHibernate(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }

    @Override
    public Either<OrderError, List<Order>> getAll() {
        List<OrdersEntity> list;
        List<Order> orderList = new ArrayList<>();

        try {
            entityManager = jpautil.getEntityManager();
            list = entityManager.createQuery("from OrdersEntity", OrdersEntity.class).getResultList();

            if (list.isEmpty()) {
                return Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
            } else {
                for (OrdersEntity entity : list) {
                    Order order = new Order();
                    order.setId(entity.getId());
                    order.setDate(entity.getDate().toLocalDateTime());
                    order.setCustomer_id(entity.getCustomer_id());
                    order.setTable_id(entity.getTable_id());

                    orderList.add(order);
                }
                return Either.right(orderList);
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public Either<OrderError, List<Order>> getAll(int idCustomer) {
        return null;
    }

    @Override
    public Either<OrderError, Order> get(int id) {
        try {
            entityManager = jpautil.getEntityManager();
            OrdersEntity entity = entityManager.find(OrdersEntity.class, id);

            if (entity == null) {
                return Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
            } else {
                Order order = new Order();
                order.setId(entity.getId());
                order.setDate(entity.getDate().toLocalDateTime());
                order.setCustomer_id(entity.getCustomer_id());
                order.setTable_id(entity.getTable_id());

                return Either.right(order);
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public Either<OrderError, Integer> save(Order order) {
        Either<OrderError, Integer> either;

        EntityTransaction tx = null;

        try {
            entityManager = jpautil.getEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();

            OrdersEntity ordersEntity = new OrdersEntity();
            ordersEntity.setDate(Timestamp.valueOf(order.getDate()));
            ordersEntity.setCustomer_id(order.getCustomer_id());
            ordersEntity.setTable_id(order.getTable_id());

            CustomersEntity customersEntity = entityManager.find(CustomersEntity.class, order.getCustomer_id());

            ordersEntity.setCustomersByCustomerId(customersEntity);

            entityManager.persist(ordersEntity);

            int orderId = ordersEntity.getId();

            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
                orderItemsEntity.setIdOrder(orderId);
                orderItemsEntity.setMenuItem(orderItem.getMenuItem().getId());
                MenuItemsEntity menuItemsEntity = entityManager.find(MenuItemsEntity.class, orderItem.getMenuItem().getId());
                orderItemsEntity.setMenuItemsByMenuItemId(menuItemsEntity);
                orderItemsEntity.setQuantity(orderItem.getQuantity());
                orderItemsEntity.setOrdersByOrderId(ordersEntity);

                entityManager.persist(orderItemsEntity);
            }

            tx.commit();
            either = Either.right(0);
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
//            Logger.getLogger(OrderDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return either;
    }


    @Override
    public Either<OrderError, Integer> save(List<Order> orders) {
        return null;
    }

//    @Override
//    public Either<OrderError, Integer> update(Order updatedOrder) {
//        Either<OrderError, Integer> either = null;
//
//        try (Connection con = db.getConnection()) {
//            con.setAutoCommit(false);
//
//            try (PreparedStatement updateOrderStatement = con.prepareStatement(
//                    "UPDATE orders SET order_date = ?, customer_id = ?, table_id = ? WHERE order_id = ?")) {
//
//                updateOrderStatement.setTimestamp(1, Timestamp.valueOf(updatedOrder.getDate()));
//                updateOrderStatement.setInt(2, updatedOrder.getCustomer_id());
//                updateOrderStatement.setInt(3, updatedOrder.getTable_id());
//                updateOrderStatement.setInt(4, updatedOrder.getId());
//
//                int rs = updateOrderStatement.executeUpdate();
//
//                if (rs == 0) {
//                    con.rollback();
//                    return Either.left(new OrderError("Error updating order"));
//                }
//            }
//
//            try (PreparedStatement deleteOrderItemsStatement = con.prepareStatement(
//                    "DELETE FROM order_items WHERE order_id = ?")) {
//                deleteOrderItemsStatement.setInt(1, updatedOrder.getId());
//
//                int rs = deleteOrderItemsStatement.executeUpdate();
//
//                if (rs == 0) {
//                    con.rollback();
//                    return Either.left(new OrderError("Error deleting existing orderItems"));
//                }
//            }
//
//            try (PreparedStatement insertOrderItemsStatement = con.prepareStatement(
//                    "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)")) {
//
//                for (OrderItem orderItem : updatedOrder.getOrderItems()) {
//                    insertOrderItemsStatement.setInt(1, updatedOrder.getId());
//                    insertOrderItemsStatement.setInt(2, orderItem.getMenuItem().getId());
//                    insertOrderItemsStatement.setInt(3, orderItem.getQuantity());
//
//                    int rs = insertOrderItemsStatement.executeUpdate();
//
//                    if (rs == 0) {
//                        con.rollback();
//                        return Either.left(new OrderError("Error inserting new orderItems"));
//                    }
//                }
//            }
//
//            con.commit();
//            either = Either.right(0);
//
//        } catch (SQLException e) {
//            log.error(e.getMessage());
//            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
//        }
//
//        return either;
//    }
    @Override
    public Either<OrderError, Integer> update(Order updatedOrder) {
        Either<OrderError, Integer> either;

        EntityTransaction tx = null;

        try {
            entityManager = jpautil.getEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();

            OrdersEntity ordersEntity = entityManager.find(OrdersEntity.class, updatedOrder.getId());

            if (ordersEntity == null) {
                tx.rollback();
                return Either.left(new OrderError("Order not found"));
            }

            ordersEntity.setDate(Timestamp.valueOf(updatedOrder.getDate()));
            ordersEntity.setCustomer_id(updatedOrder.getCustomer_id());
            ordersEntity.setTable_id(updatedOrder.getTable_id());

            CustomersEntity customersEntity = entityManager.find(CustomersEntity.class, updatedOrder.getCustomer_id());
            ordersEntity.setCustomersByCustomerId(customersEntity);

            entityManager.createQuery("DELETE FROM OrderItemsEntity o WHERE o.ordersByOrderId.id = :orderId")
                    .setParameter("orderId", updatedOrder.getId())
                    .executeUpdate();

            for (OrderItem orderItem : updatedOrder.getOrderItems()) {
                OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
                orderItemsEntity.setIdOrder(updatedOrder.getId());
                orderItemsEntity.setMenuItem(orderItem.getMenuItem().getId());

                MenuItemsEntity menuItemsEntity = entityManager.find(MenuItemsEntity.class, orderItem.getMenuItem().getId());
                orderItemsEntity.setMenuItemsByMenuItemId(menuItemsEntity);

                orderItemsEntity.setQuantity(orderItem.getQuantity());
                orderItemsEntity.setOrdersByOrderId(ordersEntity);

                entityManager.persist(orderItemsEntity);
            }

            tx.commit();
            either = Either.right(0);

        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return either;
    }


    @Override
    public Either<OrderError, Integer> delete(Order c) {
        Either<OrderError, Integer> either = null;

        EntityTransaction tx = null;

        try {
            entityManager = jpautil.getEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();

            OrdersEntity ordersEntity = entityManager.find(OrdersEntity.class, c.getId());

            if (ordersEntity == null) {
                tx.rollback();
                return Either.left(new OrderError("Order not found"));
            }

            entityManager.createQuery("DELETE FROM OrderItemsEntity o WHERE o.ordersByOrderId.id = :orderId")
                    .setParameter("orderId", c.getId())
                    .executeUpdate();

            entityManager.remove(ordersEntity);

            tx.commit();
            either = Either.right(0);

        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }


        return either;
    }
}
