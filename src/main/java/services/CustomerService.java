package services;

import dao.CustomersDAO;
import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Credential;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private final CustomersDAO dao;
    private final OrderDAO daoOrders;
    private final OrderDAO daoOrderXml;
    @Inject
    public CustomerService(@Named("customerMongo") CustomersDAO dao, @Named("orderDAO") OrderDAO daoOrders, @Named("orderXML") OrderDAO daoOrderXml) {
        this.dao = dao;
        this.daoOrders = daoOrders;
        this.daoOrderXml = daoOrderXml;
    }

    public Either<CustomerError, List<Customer>> getAll() {
        return dao.getAll();
    }

    public Either<CustomerError, Customer> get(ObjectId id) {
        return dao.get(id);
    }

    public Customer getCustomerByOrder(Order order){
        return dao.getCustomerByOrder(order);
    }

    public Either<CustomerError, Integer> save(Customer c) {
        return dao.save(c);
    }

    public Either<CustomerError, Integer> save(Customer c, Credential credential) {
        return dao.save(c, credential);
    }

    public Either<CustomerError, Integer> update(Customer customer) {
        return dao.update(customer);
    }

    public Either<CustomerError, Boolean> delete(Customer c, Boolean deleteOrders) {
        if (Boolean.TRUE.equals(deleteOrders))
            daoOrderXml.save(c.get_id(), daoOrderXml.getAll(c.get_id()).get());
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

}
