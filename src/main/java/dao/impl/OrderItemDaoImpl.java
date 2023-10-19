package dao.impl;

import dao.OrderItemDAO;
import io.vavr.control.Either;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;

import java.util.List;

public class OrderItemDaoImpl implements OrderItemDAO {
    @Override
    public List<OrderItem> getAll(int id) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> save(List<OrderItem> orderItems, Order order) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> delete(int id) {
        return null;
    }
}
