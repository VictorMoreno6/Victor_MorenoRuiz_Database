package dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import model.Customer;
import model.Order;
import model.errors.OrderError;
import model.gsonAdapters.LocalDateAdapter;
import model.gsonAdapters.LocalDateTimeAdapter;
import model.gsonAdapters.ObjectIdAdapter;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Log4j2
@Named("orderDAO")
public class OrderDaoMongo implements OrderDAO {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();

    @Override
    public Either<OrderError, List<Order>> getAll() {
        List<Order> orders = new ArrayList<>();

        try {
            MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323/");
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            FindIterable<Document> customerDocuments = col.find();

            for (Document document : customerDocuments) {
                Customer customer = gson.fromJson(document.toJson(), Customer.class);
                if (customer.getOrders() != null)
                    orders.addAll(customer.getOrders());
            }

            return Either.right(orders);
        } catch (Exception e) {
            return Either.left(new OrderError( "An error occurred: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError,  List<Order>> get(ObjectId id) {
        try {
            MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323/");
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            Document document = col.find(eq("_id", id)).first();

            if (document != null) {
                Customer customer = gson.fromJson(document.toJson(), Customer.class);
                List<Order> orders = customer.getOrders();

                if (orders.isEmpty()) {
                    return Either.left(new OrderError("Customer has no orders"));
                } else {
                    // For simplicity, returning the first order. Modify as needed.
                    return Either.right(orders);
                }
            } else {
                return Either.left(new OrderError("Customer not found"));
            }
        } catch (Exception e) {
            return Either.left(new OrderError("An error occurred: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, List<Order>> getAll(ObjectId idCustomer) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> save(ObjectId customerId, List<Order> orders) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> save(ObjectId customerId, Order order) {
        try {
            MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323");
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            BasicDBObject query = new BasicDBObject("_id", customerId);
            BasicDBObject update = new BasicDBObject("$push", new BasicDBObject("orders", Document.parse(gson.toJson(order))));

            UpdateResult result = col.updateOne(query, update);

            if (result.getModifiedCount() > 0) {
                return Either.right((int) result.getModifiedCount());
            } else {
                return Either.left(new OrderError("Error saving order to customer"));
            }
        } catch (Exception e) {
            return Either.left(new OrderError("An error occurred: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, Integer> update(Order newOrder, Order oldOrder) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            // Build the filter to find the document with the matching old order
            Bson filter = eq("orders", Document.parse(gson.toJson(oldOrder)));

            // Build the update using $set and positional operator $ to update the matching element
            Bson update = set("orders.$", Document.parse(gson.toJson(newOrder)));

            UpdateResult result = col.updateOne(filter, update);

            // Get the number of modified documents
            long modifiedCount = result.getModifiedCount();

            if (modifiedCount > 0) {
                return Either.right((int) modifiedCount);
            } else {
                return Either.left(new OrderError("Error updating order"));
            }
        } catch (Exception e) {
            return Either.left(new OrderError("Error updating order: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, Integer> delete(Order order) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            BasicDBObject pullQuery = new BasicDBObject("orders", new BasicDBObject("order_date", order.getDate()));

            UpdateResult result = col.updateOne(new BasicDBObject(), new BasicDBObject("$pull", pullQuery));

            if (result.getModifiedCount() > 0) {
                return Either.right((int) result.getModifiedCount());
            } else {
                return Either.left(new OrderError("Error deleting order"));
            }
        } catch (Exception e) {
            return Either.left(new OrderError("An error occurred: " + e.getMessage()));
        }
    }
}
