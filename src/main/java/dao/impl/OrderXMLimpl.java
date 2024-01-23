package dao.impl;

import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import model.Order;
import model.errors.OrderError;
import model.xml.OrderItemXml;
import model.xml.Ordersxml;
import model.xml.Orderxml;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Named("orderXML")
public class OrderXMLimpl implements OrderDAO {

    private DBConnection db;

    @Inject
    public OrderXMLimpl(DBConnection db) {
        this.db = db;
    }
    @Override
    public Either<OrderError, List<Order>> getAll() {
        return null;
    }

    @Override
    public Either<OrderError, List<Order>> getAll(int idCustomer) {
        JdbcTemplate jtm = new JdbcTemplate(db.getDataSource());
        Either<OrderError, List<Order>> result;


        try {
            List<Order> orders = jtm.query("SELECT * FROM orders WHERE customer_id = ?", new OrderMapper(), idCustomer);

            if (!orders.isEmpty()) {
                result = Either.right(orders);
            } else {
                result = Either.left(new OrderError("No orders found"));
            }
        } catch (Exception e) {
            result = Either.left(new OrderError("Error connecting to database"));
        }
        return result;
    }

    @Override
    public Either<OrderError, Order> get(int id) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> save(Order c) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> save(List<Order> orders) {
        int customerId = orders.get(0).getCustomer_id();

        Path ordersXMLPath = Paths.get("data/Customer" + customerId + "orders.xml");

        try {
            JAXBContext context = JAXBContext.newInstance(Ordersxml.class, Orderxml.class, OrderItemXml.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Ordersxml ordersList = new Ordersxml();
            ordersList.setOrders(new ArrayList<>());

            for (Order order : orders) {
                Orderxml newOrderXML = new Orderxml();
                newOrderXML.setId(order.getId());
                newOrderXML.setOrderItems(new ArrayList<>());

                if (order.getOrderItems() != null) {
                    order.getOrderItems().forEach(orderItem -> {
                        OrderItemXml newOrderItemXML = new OrderItemXml();
                        newOrderItemXML.setMenuItem(orderItem.getMenuItem().getName());
                        newOrderItemXML.setQuantity(orderItem.getQuantity());
                        newOrderXML.getOrderItems().add(newOrderItemXML);
                    });
                }

                ordersList.getOrders().add(newOrderXML);
            }

            try (Writer writer = Files.newBufferedWriter(ordersXMLPath)) {
                marshaller.marshal(ordersList, writer);
            }

            return Either.right(0);

        } catch (Exception e) {
            return Either.left(new OrderError("Error saving orders"));
        }
    }

    @Override
    public Either<OrderError, Integer> update(Order c) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> delete(Order c) {
        return null;
    }
}
