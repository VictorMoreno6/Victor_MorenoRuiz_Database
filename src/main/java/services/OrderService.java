package services;

import common.Constants;
import dao.*;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.errors.CustomerError;
import model.errors.OrderError;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderDAO dao;

    private final CustomersDAO dao2;

    private final TableDAO tabledao;

    private final MenuItemDAO menuItemDAO;

    @Inject
    public OrderService(@Named("orderDAO") OrderDAO dao, @Named("customerMongo") CustomersDAO dao2, TableDAO tabledao, MenuItemDAO menuItemDAO) {
        this.dao = dao;
        this.dao2 = dao2;
        this.tabledao = tabledao;
        this.menuItemDAO = menuItemDAO;
    }


    public Either<OrderError, List<Order>> getAll() {
        return dao.getAll();
    }

    public Either<OrderError,  List<Order>> get(ObjectId id) {
        return dao.get(id);
    }

    public Either<OrderError, Integer> save(ObjectId id, Order o) {
        return dao.save(id, o);
    }

    public List<Order> getOrdersById(ObjectId customerId) {
        if (dao.get(customerId).isRight())
            return dao.get(customerId).get();
        else
            return null;
    }

    public List<ObjectId> getIds() {
        List<ObjectId> aux = new ArrayList<>();
        if (dao2.getAll().isRight())
            dao2.getAll().get().stream().forEach(customer -> aux.add(customer.get_id()));

        return aux;
    }

    public Either<OrderError, Integer> update(Order newOrder, Order oldOrder) {
        return dao.update(newOrder, oldOrder);
    }

    public Either<OrderError, Integer> delete( Order o) {
        return dao.delete(o);
    }

    public List<Order> getOrdersByDate(LocalDate value) {
        if (dao.getAll().isRight())
            return dao.getAll().get().stream()
                    .filter(order -> order.getDate().toLocalDate().equals(value))
                    .collect(Collectors.toList());
        else
            return null;
    }

    public List<Integer> getTableIds() {
        List<Integer> aux = new ArrayList<>();
        if (!tabledao.getAll().isEmpty())
            tabledao.getAll().stream().forEach(table -> aux.add(table.getId()));
        return aux;
    }

    public double getTotalPrice(List<OrderItem> orderItems){
        double total = 0;
        for (OrderItem o : orderItems){
            MenuItem menuItem = menuItemDAO.get(o.getMenuItemId()).get();
            total += o.getQuantity() * menuItem.getPrice();
        }
        return (double) Math.round(total * 100.0) / 100.0;
    }
}
