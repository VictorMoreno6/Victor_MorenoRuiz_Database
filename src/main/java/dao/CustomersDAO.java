package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.vavr.control.Either;
import model.Credential;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public interface CustomersDAO {

    Either<CustomerError, List<Customer>> getAll();

    Either<CustomerError, Customer> get(ObjectId id);

    Either<CustomerError, Integer> save(Customer c);
    Either<CustomerError, Integer> save(Customer c, Credential credential);

    Either<CustomerError, Integer> update(Customer customer);

    Either<CustomerError, Integer> delete(Customer c, boolean deleteOrders);

     Customer getCustomerByOrder(Order order);
}
