package services;

import dao.MenuItemDAO;
import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import model.*;
import model.errors.OrderError;

import java.util.ArrayList;
import java.util.List;

public class OrderItemService {

    @Inject
    private OrderItemDAO orderItemDAO;

    @Inject
    private MenuItemDAO menuItemDAO;

    public List<OrderItem> getAll() {
        return orderItemDAO.getAll();
    }

    public Either<OrderError, Integer> save(List<OrderItem> orderItems, Order order) {
        return orderItemDAO.save(orderItems, order);
    }


    public SimpleStringProperty printMenuItemName(OrderItem value) {
        return new SimpleStringProperty(value.getMenuItem().getName());
    }
}
