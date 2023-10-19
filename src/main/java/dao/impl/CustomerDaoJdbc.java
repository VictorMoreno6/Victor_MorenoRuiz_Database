package dao.impl;

import dao.CustomersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.errors.CustomerError;
import java.sql.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDaoJdbc implements CustomersDAO {

    private DBConnection db;

    @Inject
    public CustomerDaoJdbc(DBConnection db) {
        this.db = db;
    }

    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        Either<CustomerError, List<Customer>> either;

        try ( Connection myConnection= db.getConnection();
              Statement stmt= myConnection.createStatement()){
              ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

              List<Customer> list = readRS(rs);

              either = Either.right(list);

        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(new CustomerError(0, "Error al conectar con la base de datos"));
        }
        return either;
    }

    private List<Customer> readRS(ResultSet rs) {
        try {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                int Id = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                LocalDate dote = rs.getDate("date_of_birth").toLocalDate();

                customers.add(new Customer(Id, first_name, last_name, email, phone, dote));
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Either<CustomerError, Customer> get(int id) {
        Either<CustomerError, Customer> either;

        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM customers WHERE id = ?")){
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            readRS(rs);
            Customer customer = readRS(rs).get(0);

            if (customer == null){
                either = Either.left(new CustomerError(0, "Error al conectar con la base de datos"));
            } else {
                either = Either.right(customer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoJdbc.class.getName()).log(Level.SEVERE, null, ex);
            either = Either.left(new CustomerError(0, "Error al conectar con la base de datos"));
        }
        return either;
    }

    @Override
    public Either<CustomerError, Integer> save(Customer c) {
        Either<CustomerError, Integer> either;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO customers (id, first_name, last_name, email, phone, date_of_birth) VALUES (?, ?, ?, ?, ?, ?)")){
            preparedStatement.setInt(1, c.getId());
            preparedStatement.setString(2, c.getFirst_name());
            preparedStatement.setString(3, c.getLast_name());
            preparedStatement.setString(4, c.getEmail());
            preparedStatement.setString(5, c.getPhone());
            preparedStatement.setDate(6, Date.valueOf(c.getDob()));

            int rs = preparedStatement.executeUpdate();

            if (rs == 0){
                either = Either.left(new CustomerError(0, "Error connecting database"));
            } else {
                either = Either.right(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoJdbc.class.getName()).log(Level.SEVERE, null, ex);
            either = Either.left(new CustomerError(0, "Error connecting database"));
        }
        return either;
    }

    @Override
    public Either<CustomerError, Integer> update(Customer old, Customer neew) {
        Either<CustomerError, Integer> either;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE customers SET id = ?, first_name = ?, last_name = ?, email = ?, phone = ?, date_of_birth = ? WHERE id = ?")){
            preparedStatement.setInt(1, neew.getId());
            preparedStatement.setString(2, neew.getFirst_name());
            preparedStatement.setString(3, neew.getLast_name());
            preparedStatement.setString(4, neew.getEmail());
            preparedStatement.setString(5, neew.getPhone());
            preparedStatement.setDate(6, Date.valueOf(neew.getDob()));
            preparedStatement.setInt(7, old.getId());

            int rs = preparedStatement.executeUpdate();

            if (rs == 0){
                either = Either.left(new CustomerError(0, "Error connecting database"));
            } else {
                either = Either.right(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoJdbc.class.getName()).log(Level.SEVERE, null, ex);
            either = Either.left(new CustomerError(0, "Error connecting database"));
        }
        return either;
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer c) {
        Either<CustomerError, Integer> either;
        try(Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM customers WHERE id = ?")){
            preparedStatement.setInt(1, c.getId());

            int rs = preparedStatement.executeUpdate();

            if (rs == 0){
                either = Either.left(new CustomerError(0, "Error connecting database"));
            } else {
                either = Either.right(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDaoJdbc.class.getName()).log(Level.SEVERE, null, ex);
            either = Either.left(new CustomerError(0, "Error connecting database"));
        }
        return either;
    }
}
