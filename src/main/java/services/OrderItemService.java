package services;

import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import model.*;
import model.errors.OrderError;

import java.util.ArrayList;
import java.util.List;

public class OrderItemService {

    @Inject
    private OrderItemDAO orderItemDAO;

    public List<OrderItem> getAll(int id) {
        return orderItemDAO.getAll(id);
    }

    public Either<OrderError, Integer> save(List<OrderItem> orderItems, Order order) {
        return orderItemDAO.save(orderItems, order);
    }


    public SimpleStringProperty printMenuItemName(OrderItem value) {
        return new SimpleStringProperty(value.getMenuItem().getName());
    }

    public List<String> getMenuItemsName(List<Integer> ids) {
        List<String> result = new ArrayList<>();
        for (Integer i : ids) {
            List<OrderItem> aux =  orderItemDAO.getAll(i);
            for (OrderItem orderItem : aux) {
                result.add(orderItem.getMenuItem().getName());
            }
        }

        return result;
    }
}
