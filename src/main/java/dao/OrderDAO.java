package dao;

import io.vavr.control.Either;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderDAO {
    Either<OrderError, List<Order>> getAll();

//    Either<OrderError, List<Order>> getAll(int idCustomer);
    Either<OrderError, List<Order>> getAll(ObjectId idCustomer);

//    Either<OrderError, Order> get(int id);
    Either<OrderError,  List<Order>> get(ObjectId id);

//    Either<OrderError, Integer> save(Order c);

//    Either<OrderError, Integer> save(List<Order> orders);
    Either<OrderError, Integer> save(ObjectId customerId, Order order);

    Either<OrderError, Integer> save(ObjectId customerId,List<Order> orders);

//    Either<OrderError, Integer> update(Customer c);

    Either<OrderError, Integer> update(Order newOrder, Order oldOrder);
//    Either<OrderError, Integer> update(Customer c);

    Either<OrderError, Integer> delete(Order order);
//    Either<OrderError, Integer> delete(Customer c);
}
