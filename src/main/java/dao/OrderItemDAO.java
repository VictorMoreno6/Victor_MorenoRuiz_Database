package dao;

import io.vavr.control.Either;
import model.*;
import model.errors.OrderError;

import java.util.List;

public interface OrderItemDAO {

     List<OrderItem> getAll();

     Either<OrderError, Integer> save(List<OrderItem> orderItems, Order order);

     Either<OrderError, Integer> delete(int id);




}
