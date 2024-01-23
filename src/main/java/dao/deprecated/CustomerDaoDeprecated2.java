//package dao.deprecated;
//
//import common.Constants;
//import dao.impl.CustomerDaoHibernate;
//import dao.impl.CustomerMapper;
//import io.vavr.control.Either;
//import model.Customer;
//import model.errors.CustomerError;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.DefaultTransactionDefinition;
//
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.SQLIntegrityConstraintViolationException;
//import java.sql.Statement;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class CustomerDaoDeprecated2 {
//    //    @Override
////    public Either<CustomerError, List<Customer>> getAll() {
////        JdbcTemplate jtm = new JdbcTemplate(db.getDataSource());
////        List<Customer> l = jtm.query("SELECT * FROM customers", new CustomerMapper());
////        if (l.isEmpty()) {
////            return Either.left(new CustomerError(0, Constants.ERROR_CONNECTING_TO_DATABASE));
////        } else {
////            return Either.right(l);
////        }
////    }
//
//    @Override
//    public Either<CustomerError, Customer> get(int id) {
//        JdbcTemplate jtm = new JdbcTemplate(db.getDataSource());
//
//        List<Customer> customers = jtm.query("SELECT * FROM customers WHERE id = ? ", new CustomerMapper(), id);
//
//        if (customers.isEmpty()) {
//            return Either.left(new CustomerError(0, Constants.ERROR_CONNECTING_TO_DATABASE));
//        } else {
//            return Either.right(customers.get(0));
//        }
//    }
//    @Override
//    public Either<CustomerError, Integer> save(Customer c) {
//        Either<CustomerError, Integer> either;
//
//        TransactionDefinition txDef = new DefaultTransactionDefinition();
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(db.getDataSource());
//        TransactionStatus txStatus = transactionManager.getTransaction(txDef);
//
//        try {
//            JdbcTemplate jtm = new JdbcTemplate(transactionManager.getDataSource());
//
//            KeyHolder keyHolder = new GeneratedKeyHolder();
//
//            int rowsAffected = jtm.update(connection -> {
//
//                PreparedStatement preparedStatement1 = connection.prepareStatement(
//                        "INSERT INTO credentials (user_name, password) VALUES (?, ?)",
//                        Statement.RETURN_GENERATED_KEYS
//                );
//
//                preparedStatement1.setString(1, c.getCredential().getUsername());
//                preparedStatement1.setString(2, c.getCredential().getPassword());
//
//                return preparedStatement1;
//            }, keyHolder);
//
//            if (rowsAffected == 0) {
//                transactionManager.rollback(txStatus);
//                return Either.left(new CustomerError(0, "Error inserting credential"));
//            }
//
//            int credentialId = keyHolder.getKey().intValue();
//
//            rowsAffected = jtm.update(
//                    "INSERT INTO customers (id, first_name, last_name, email, phone, date_of_birth) VALUES (?, ?, ?, ?, ?, ?)",
//                    credentialId,
//                    c.getFirst_name(),
//                    c.getLast_name(),
//                    c.getEmail(),
//                    c.getPhone(),
//                    Date.valueOf(c.getDob())
//            );
//
//            if (rowsAffected == 0) {
//                transactionManager.rollback(txStatus);
//                return Either.left(new CustomerError(0, "Error inserting customer"));
//            }
//
//            transactionManager.commit(txStatus);
//            either = Either.right(0);
//        } catch (Exception ex) {
//            transactionManager.rollback(txStatus);
//            Logger.getLogger(CustomerDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
//            either = Either.left(new CustomerError(0, "Error connecting database"));
//        }
//
//        return either;
//    }
//
//    @Override
//    public Either<CustomerError, Integer> update(Customer customer) {
//        Either<CustomerError, Integer> either;
//
//        try {
//            JdbcTemplate jtm = new JdbcTemplate(db.getDataSource());
//
//            int rowsAffected = jtm.update(
//                    "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone = ?, date_of_birth = ? WHERE id = ?",
//                    customer.getFirst_name(),
//                    customer.getLast_name(),
//                    customer.getEmail(),
//                    customer.getPhone(),
//                    Date.valueOf(customer.getDob()),
//                    customer.getId()
//            );
//
//            if (rowsAffected == 0) {
//                either = Either.left(new CustomerError(0, "Error connecting database"));
//            } else {
//                either = Either.right(rowsAffected);
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(CustomerDaoHibernate.class.getName()).log(Level.SEVERE, null, ex);
//            either = Either.left(new CustomerError(0, "Error connecting database"));
//        }
//
//        return either;
//    }
//
//    @Override
//    public Either<CustomerError, Integer> delete(Customer c, boolean deleteOrders) {
//        Either<CustomerError, Integer> result;
//
//        TransactionDefinition txDef = new DefaultTransactionDefinition();
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(db.getDataSource());
//        TransactionStatus txStatus = transactionManager.getTransaction(txDef);
//
//        try {
//            JdbcTemplate jtm = new JdbcTemplate(transactionManager.getDataSource());
//
//            if (deleteOrders) {
//                int rowsAffected1 = jtm.update(
//                        "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id = ?)",
//                        c.getId()
//                );
//
//                int rowsAffected2 = jtm.update(
//                        "DELETE FROM orders WHERE customer_id = ?",
//                        c.getId()
//                );
//
//                int rowsAffected3 = jtm.update(
//                        "DELETE FROM customers WHERE id = ?",
//                        c.getId()
//                );
//
//                int rowsAffected4 = jtm.update(
//                        "DELETE FROM credentials WHERE id = ?",
//                        c.getId()
//                );
//
//                if (rowsAffected1 == 0 || rowsAffected2 == 0 || rowsAffected3 == 0 || rowsAffected4 == 0) {
//                    transactionManager.rollback(txStatus);
//                    result = Either.left(new CustomerError(0, "Error deleting customer with orders"));
//                } else {
//                    transactionManager.commit(txStatus);
//                    result = Either.right(0);
//                }
//            } else {
//                int rowsAffected1 = jtm.update(
//                        "DELETE FROM customers WHERE id = ?",
//                        c.getId()
//                );
//
//                int rowsAffected2 = jtm.update(
//                        "DELETE FROM credentials WHERE id = ?",
//                        c.getId()
//                );
//
//                if (rowsAffected1 == 0 || rowsAffected2 == 0) {
//                    transactionManager.rollback(txStatus);
//                    result = Either.left(new CustomerError(0, "Error deleting customer"));
//                } else {
//                    transactionManager.commit(txStatus);
//                    result = Either.right(0);
//                }
//            }
//        } catch (DataAccessException ex) {
//            transactionManager.rollback(txStatus);
//            if (ex.getCause() instanceof SQLIntegrityConstraintViolationException){
//                result = Either.left(new CustomerError(2, "The customer has orders"));
//            } else {
//                result = Either.left(new CustomerError(1, "Error connecting database"));
//            }
//        }
//
//        return result;
//    }
//
//
//}
//
//}