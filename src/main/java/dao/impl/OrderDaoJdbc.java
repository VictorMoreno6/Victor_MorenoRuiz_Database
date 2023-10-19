package dao.impl;

import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJdbc implements OrderDAO {

    private DBConnection db;

    @Inject
    public OrderDaoJdbc(DBConnection db) {
        this.db = db;
    }

    @Override
    public Either<OrderError, List<Order>> getAll() {
        Either<OrderError, List<Order>> either = null;
        try(Connection myConnection = db.getConnection();
            Statement stmt = myConnection.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");
            List<Order> list = readRS(rs);
            either = Either.right(list);
        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new OrderError("Error connecting to database"));
        }
        return either;
    }

    @Override
    public Either<OrderError, Order> get(int id) {
        Either<OrderError, Order> either = null;
        try(Connection myConnection = db.getConnection();
            Statement stmt = myConnection.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE order_id = " + id);
            List<Order> list = readRS(rs);
            if(list.size() == 1)
                either = Either.right(list.get(0));
            else
                either = Either.left(new OrderError("Order not found"));
        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new OrderError("Error connecting to database"));
        }
        return either;
    }

    private List<Order> readRS(ResultSet rs) {
        try {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                int Id = rs.getInt("order_id");
                LocalDateTime date = rs.getTimestamp("order_date").toLocalDateTime();
                int customer_id = rs.getInt("customer_id");
                int table_id = rs.getInt("table_id");
                orders.add(new Order(Id, date, customer_id, table_id));
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Either<OrderError, Integer> save(Order c) {
        return null;
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
