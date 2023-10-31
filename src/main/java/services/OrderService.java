package services;

import common.Constants;
import dao.CustomersDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    @Inject
    private OrderDAO dao;

    @Inject
    private CustomersDAO dao2;

    @Inject
    private OrderItemDAO dao3;


    public Either<OrderError, List<Order>> getAll() {
        return dao.getAll();
    }

    public Either<OrderError, Order> get(int id) {
        return dao.get(id);
    }

    public Either<OrderError, Integer> save(Order o) {
        if (dao2.get(o.getCustomer_id()) == null)
            return Either.left(new OrderError(Constants.CUSTOMER_DOES_NOT_EXIST));
        else
            return dao.save(o);
    }

    public List<Order> getOrdersById(int customer_Id) {
        if (dao.getAll().isRight())
            return dao.getAll().get().stream()
                    .filter(order -> order.getCustomer_id() == customer_Id)
                    .toList();
        else
            return null;
    }

    public List<OrderItem> getOrderItems(int id){
        List<OrderItem> aux = new ArrayList<>();
        List<OrderItem> list = dao3.getAll();

        for(OrderItem o : list){
            if(o.getIdOrder() == id)
                aux.add(o);
        }
        return aux;
    }

    public String getCustomerName(int id){
        Customer aux = dao2.get(id).get();
        return aux.getFirst_name() + " " + aux.getLast_name();
    }

    public Integer autoId(){
        List<Integer> aux = new ArrayList<>();
        for(Integer i : dao.getAll().get().stream().map(Order::getId).collect(Collectors.toList())){
            aux.add(i);
        }
        Integer n = 1;
        boolean repeat = true;
        while (repeat){
            if(aux.contains(n))
                n++;
            else
                repeat = false;
        }
        return n;
    }

    public List<Integer> getIds() {
        List<Integer> aux = new ArrayList<>();
        if (dao2.getAll().isRight())
            dao2.getAll().get().stream().forEach(customer -> aux.add(customer.getId()));

        return aux;
    }

    public Either<OrderError, Integer> update(Order o) {
        return dao.update(o);
    }

    public Either<OrderError, Integer> delete(Order o) {
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
}
