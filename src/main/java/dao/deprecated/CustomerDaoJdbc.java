//package dao.deprecated;
//
//import common.Constants;
//import dao.CustomersDAO;
//import dao.impl.DBConnection;
//import io.vavr.control.Either;
//import jakarta.inject.Inject;
//import model.Customer;
//import model.errors.CustomerError;
//
//import java.sql.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class CustomerDaoJdbc implements CustomersDAO {
//
//    private DBConnection db;
//
//    @Inject
//    public CustomerDaoJdbc(DBConnection db) {
//        this.db = db;
//    }
//
//    @Override
//    public Either<CustomerError, List<Customer>> getAll() {
//        Either<CustomerError, List<Customer>> either;
//
//        try ( Connection myConnection= db.getConnection();
//              Statement stmt= myConnection.createStatement()){
//              ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
//
//              List<Customer> list = readRS(rs);
//
//              either = Either.right(list);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            either = Either.left(new CustomerError(0, Constants.ERROR_CONNECTING_TO_DATABASE));
//        }
//        return either;
//    }
//
//    private List<Customer> readRS(ResultSet rs) {
//        try {
//            List<Customer> customers = new ArrayList<>();
//            while (rs.next()) {
//                int Id = rs.getInt("id");
//                String first_name = rs.getString("first_name");
//                String last_name = rs.getString("last_name");
//                String email = rs.getString("email");
//                String phone = rs.getString("phone");
//                LocalDate dote = rs.getDate("date_of_birth").toLocalDate();
//
//                customers.add(new Customer(Id, first_name, last_name, email, phone, dote));
//            }
//            return customers;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public Either<CustomerError, Customer> get(int id) {
//        Either<CustomerError, Customer> either;
//
//        try (Connection con = db.getConnection();
//             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM customers WHERE id = ?")) {
//            preparedStatement.setInt(1, id);
//
//            ResultSet rs = preparedStatement.executeQuery();
//
//            List<Customer> customers = readRS(rs);
//
//            if (customers.isEmpty()) {
//                either = Either.left(new CustomerError(0, Constants.ERROR_CONNECTING_TO_DATABASE));
//            } else {
//                either = Either.right(customers.get(0));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerDaoJdbc.class.getName()).log(Level.SEVERE, null, ex);
//            either = Either.left(new CustomerError(0, Constants.ERROR_CONNECTING_TO_DATABASE));
//        }
//        return either;
//    }
//
//    @Override
//    public Either<CustomerError, Integer> save(Customer c) {
//        Either<CustomerError, Integer> either;
//        try (Connection con = db.getConnection();
//             PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO customers (id, first_name, last_name, email, phone, date_of_birth) VALUES (?, ?, ?, ?, ?, ?)")) {
//            preparedStatement.setInt(1, c.getId());
//            preparedStatement.setString(2, c.getFirst_name());
//            preparedStatement.setString(3, c.getLast_name());
//            preparedStatement.setString(4, c.getEmail());
//            preparedStatement.setString(5, c.getPhone());
//            preparedStatement.setDate(6, Date.valueOf(c.getDob()));
//
//            int rs = preparedStatement.executeUpdate();
//
//            if (rs == 0) {
//                either = Either.left(new CustomerError(0, "Error connecting database"));
//            } else {
//                either = Either.right(0);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerDaoJdbc.class.getName()).log(Level.SEVERE, null, ex);
//            either = Either.left(new CustomerError(0, "Error connecting database"));
//        }
//        return either;
//    }
//
//    @Override
//    public Either<CustomerError, Integer> update(Customer customer) {
//        Either<CustomerError, Integer> either;
//        try (Connection con = db.getConnection();
//             PreparedStatement preparedStatement = con.prepareStatement("UPDATE customers SET id = ?, first_name = ?, last_name = ?, email = ?, phone = ?, date_of_birth = ? WHERE id = ?")) {
//            preparedStatement.setInt(1, customer.getId());
//            preparedStatement.setString(2, customer.getFirst_name());
//            preparedStatement.setString(3, customer.getLast_name());
//            preparedStatement.setString(4, customer.getEmail());
//            preparedStatement.setString(5, customer.getPhone());
//            preparedStatement.setDate(6, Date.valueOf(customer.getDob()));
//            preparedStatement.setInt(7, customer.getId());
//
//            int rs = preparedStatement.executeUpdate();
//
//            if (rs == 0) {
//                either = Either.left(new CustomerError(0, "Error connecting database"));
//            } else {
//                either = Either.right(0);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerDaoJdbc.class.getName()).log(Level.SEVERE, null, ex);
//            either = Either.left(new CustomerError(0, "Error connecting database"));
//        }
//        return either;
//    }
//
//    @Override
//    public Either<CustomerError, Integer> delete(Customer c, boolean deleteOrders) {
//        Either<CustomerError, Integer> result;
//        Connection con = null;
//        if (deleteOrders) {
//            try {
//                con = db.getConnection();
//                con.setAutoCommit(false);
//
//                PreparedStatement statement1 = con.prepareStatement("DELETE FROM order_items WHERE order_id IN (SELECT id FROM orders WHERE customer_id = ?)");
//                statement1.setInt(1, c.getId());
//                statement1.executeUpdate();
//
//                PreparedStatement statement2 = con.prepareStatement("DELETE FROM orders WHERE customer_id = ?");
//                statement2.setInt(1, c.getId());
//                statement2.executeUpdate();
//
//                PreparedStatement statement3 = con.prepareStatement("DELETE FROM credentials WHERE customer_id = ?");
//                statement3.setInt(1, c.getId());
//                statement3.executeUpdate();
//
//                PreparedStatement statement4 = con.prepareStatement("DELETE FROM customers WHERE id = ?");
//                statement4.setInt(1, c.getId());
//                statement4.executeUpdate();
//
//                con.commit();
//
//                result = Either.right(0);
//            } catch (Exception e) {
//                result = Either.left(new CustomerError(0, "There was an error"));
//            }
//        } else {
//            try {
//                con = db.getConnection();
//                PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM customers WHERE id = ?");
//                PreparedStatement preparedStatement2 = con.prepareStatement("DELETE FROM credentials WHERE customer_id = ?");
//                preparedStatement.setInt(1, c.getId());
//                preparedStatement2.setInt(1, c.getId());
//
//                preparedStatement.executeUpdate();
//                preparedStatement2.executeUpdate();
//
//                result = Either.right(0);
//            } catch (SQLException ex) {
//                if (ex.getErrorCode() == 1451) {
//                    result = Either.left(new CustomerError(ex.getErrorCode(), "The customer has orders"));
//                } else {
//                    result = Either.left(new CustomerError(0, "There was an error"));
//                }
//            }
//        }
//        return result;
//    }
//
//
//}
