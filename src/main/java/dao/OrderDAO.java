package dao;

import io.vavr.control.Either;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;

import java.util.List;

public interface OrderDAO {
    Either<OrderError, List<Order>> getAll();

    Either<OrderError, List<Order>> getAll(int idCustomer);

    Either<OrderError, Order> get(int id);

    Either<OrderError, Integer> save(Order c);

    Either<OrderError, Integer> save(List<Order> orders);

    Either<OrderError, Integer> update(Order c);

    Either<OrderError, Integer> delete(Order c);
}
