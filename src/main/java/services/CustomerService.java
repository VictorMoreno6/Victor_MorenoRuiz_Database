package services;

import dao.CustomersDAO;
import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.Order;
import model.errors.CustomerError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    @Inject
    private CustomersDAO dao;

    @Inject
    private OrderDAO daoOrders;

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
        Either<CustomerError, Integer> resultDao = dao.delete(c, deleteOrders);
        Either<CustomerError, Boolean> result;
        if (resultDao.isLeft()) {
            if (resultDao.getLeft().getNumError() == 1451){
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
