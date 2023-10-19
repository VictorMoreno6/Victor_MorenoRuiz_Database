package dao;

import io.vavr.control.Either;
import model.Customer;
import model.errors.CustomerError;

import java.util.List;

public interface CustomersDAO {

    Either<CustomerError, List<Customer>> getAll();

    Either<CustomerError, Customer> get(int id);

    Either<CustomerError, Integer> save(Customer c);

    //TODO: only one customer
    Either<CustomerError, Integer> update(Customer old, Customer neew);

    Either<CustomerError, Integer> delete(Customer c);
}
