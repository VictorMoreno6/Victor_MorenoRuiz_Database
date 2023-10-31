package dao.impl;

import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static common.Constants.ERROR_CONNECTING_TO_DATABASE;

public class OrderDaoJdbc implements OrderDAO {

    private DBConnection db;

    @Inject
    public OrderDaoJdbc(DBConnection db) {
        this.db = db;
    }

    @Override
    public Either<OrderError, List<Order>> getAll() {
        Either<OrderError, List<Order>> either = null;

        try (Connection myConnection = db.getConnection();
             Statement stmt = myConnection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM orders");
                List<Order> list = readRS(rs);
                either = Either.right(list);
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
    public Either<OrderError, Order> get(int id) {
        Either<OrderError, Order> either = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM orders WHERE order_id = ?")){
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            readRS(rs);
            Order order = readRS(rs).get(0);

            if (order == null){
                either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
            } else {
                either = Either.right(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
        }
        return either;
    }

    @Override
    public Either<OrderError, Integer> save(Order c) {
        Either<OrderError, Integer> either = null;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO orders (order_id, order_date, customer_id, table_id) VALUES (?, ?, ?, ?)")){
            preparedStatement.setInt(1, c.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(c.getDate()));
            preparedStatement.setInt(3, c.getCustomer_id());
            preparedStatement.setInt(4, c.getTable_id());

            int rs = preparedStatement.executeUpdate();

            if (rs == 0){
                either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
            } else {
                either = Either.right(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
        }
        return either;
    }

    @Override
    public Either<OrderError, Integer> update(Order c) {
        Either<OrderError, Integer> either = null;

        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE orders SET order_date = ?, customer_id = ?, table_id = ? WHERE order_id = ?")){
            preparedStatement.setTimestamp(1, Timestamp.valueOf(c.getDate()));
            preparedStatement.setInt(2, c.getCustomer_id());
            preparedStatement.setInt(3, c.getTable_id());
            preparedStatement.setInt(4, c.getId());

            int rs = preparedStatement.executeUpdate();

            if (rs == 0){
                either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
            } else {
                either = Either.right(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
        }
        return either;
    }

    @Override
    public Either<OrderError, Integer> delete(Order c) {
        Either<OrderError, Integer> either = null;

        try(Connection con = db.getConnection()){

            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM order_item WHERE order_id = ?");
            preparedStatement.setInt(1, c.getId());

            PreparedStatement preparedStatement2 = con.prepareStatement("DELETE FROM orders WHERE order_id = ?");
            preparedStatement2.setInt(1, c.getId());

            int rs = preparedStatement.executeUpdate();
            int rs2 = preparedStatement2.executeUpdate();

            if (rs == 0 && rs2 == 0){
                either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
            } else {
                either = Either.right(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new OrderError(ERROR_CONNECTING_TO_DATABASE));
        }
        return either;
    }
}
