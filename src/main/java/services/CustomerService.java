package services;

import dao.CustomersDAO;
import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Customer;
import model.Order;
import model.errors.CustomerError;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private final CustomersDAO dao;
    private final OrderDAO daoOrders;
    private final OrderDAO daoOrderXml;
    @Inject
    public CustomerService(CustomersDAO dao, @Named("orderDAO") OrderDAO daoOrders, @Named("orderXML") OrderDAO daoOrderXml) {
        this.dao = dao;
        this.daoOrders = daoOrders;
        this.daoOrderXml = daoOrderXml;
    }

    public Either<CustomerError, List<Customer>> getAll() {
        return dao.getAll();
    }

    public Either<CustomerError, Customer> get(int id) {
        return dao.get(id);
    }

    public Either<CustomerError, Integer> save(Customer c) {
        return dao.save(c);
    }

    public Either<CustomerError, Integer> update(Customer customer) {
        return dao.update(customer);
    }

    public Either<CustomerError, Boolean> delete(Customer c, Boolean deleteOrders) {
        if (Boolean.TRUE.equals(deleteOrders))
            daoOrderXml.save(daoOrderXml.getAll(c.getId()).get());
        Either<CustomerError, Integer> resultDao = dao.delete(c, deleteOrders);
        Either<CustomerError, Boolean> result;
        if (resultDao.isLeft()) {
            if (resultDao.getLeft().getNumError() == 2){
                result = Either.right(true);
            }else {
                result = Either.left(resultDao.getLeft());
            }
        } else {
            result = Either.right(false);
        }
        return result;
    }

    public List<Order> getOrdersOfCustomer(int customer_Id) {
        if (daoOrders.getAll().isRight()){
            return daoOrders.getAll().get().stream()
                    .filter(order -> order.getCustomer_id() == customer_Id)
                    .toList();
        } else {
            return null;
        }
    }

    public Integer autoId(){
        List<Integer> aux = new ArrayList<>();
        for(Integer i : dao.getAll().get().stream().map(Customer::getId).toList()){
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
}
