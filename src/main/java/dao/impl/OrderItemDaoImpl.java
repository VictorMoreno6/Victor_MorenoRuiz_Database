package dao.impl;

import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;
import model.hibernate.MenuItemsEntity;
import model.hibernate.OrderItemsEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrderItemDaoImpl implements OrderItemDAO {
    private final JPAUtil jpautil;

    private EntityManager entityManager;


    @Inject
    public OrderItemDaoImpl(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }
    @Override
    public List<OrderItem> getAll() {
//        List<OrderItem> orderItems = null;
//
//        try {
//            entityManager = jpautil.getEntityManager();
//
//            TypedQuery<OrderItemsEntity> query = entityManager.createQuery("SELECT oi FROM OrderItemsEntity oi", OrderItemsEntity.class);
//            List<OrderItemsEntity> orderItemsEntities = query.getResultList();
//
//            orderItems = orderItemsEntities.stream()
//                    .map(orderItemsEntity -> new OrderItem(
//                            orderItemsEntity.getMenuItemsByMenuItemId().getId(),
//                            orderItemsEntity.getQuantity()
//                    ))
//                    .collect(Collectors.toList());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (entityManager != null) {
//                entityManager.close();
//            }
//        }
//
//        return orderItems;
        return null;
    }

    @Override
    public Either<OrderError, Integer> save(List<OrderItem> orderItems, Order order) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> delete(int id) {
        return null;
    }
}
