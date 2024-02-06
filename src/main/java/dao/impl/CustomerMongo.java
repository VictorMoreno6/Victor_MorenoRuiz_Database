package dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.*;
import common.Constants;
import dao.CustomersDAO;
import io.vavr.control.Either;
import jakarta.inject.Named;
import model.Credential;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.gsonAdapters.LocalDateAdapter;
import model.gsonAdapters.LocalDateTimeAdapter;
import model.gsonAdapters.ObjectIdAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Named("customerMongo")
public class CustomerMongo implements CustomersDAO {
    //    mongodb://informatica.iesquevedo.es:2323/

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        List<Customer> customers = new ArrayList<>();

        try ( MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);
            MongoCollection<Document> col = db.getCollection(Constants.CUSTOMERS);

            FindIterable<Document> customerDocuments = col.find();

            for (Document document : customerDocuments) {
                Customer customer = gson.fromJson(document.toJson(), Customer.class);
                customers.add(customer);
            }

            if (customers.isEmpty()) {
                return Either.left(new CustomerError(1, "Error connecting to database"));
            } else {
                return Either.right(customers);
            }
        } catch (Exception e) {
            return Either.left(new CustomerError(1, "An error occurred: " + e.getMessage()));
        }
    }

    @Override
    public Either<CustomerError, Customer> get(ObjectId id) {
        try ( MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);
            MongoCollection<Document> col = db.getCollection(Constants.CUSTOMERS);

            Document document = col.find(eq("_id", id)).first();

            if (document != null) {
                Customer customer = gson.fromJson(document.toJson(), Customer.class);
                return Either.right(customer);
            } else {
                return Either.left(new CustomerError(1, "Customer not found"));
            }
        } catch (Exception e) {
            return Either.left(new CustomerError(1, "An error occurred: " + e.getMessage()));
        }
    }

    //TODO
    //Que al salvar Order salve Customer
    @Override
    public Either<CustomerError, Integer> save(Customer c) {
        try ( MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);
            MongoCollection<Document> est = db.getCollection(Constants.CUSTOMERS);

            Document customerDocument = Document.parse(gson.toJson(c));
            est.insertOne(customerDocument);

            int insertedCount = customerDocument.size();

            if (insertedCount > 0) {
                return Either.right(insertedCount);
            } else {
                return Either.left(new CustomerError(1, "Error saving customer"));
            }
        } catch (Exception e) {
            return Either.left(new CustomerError(1, "An error occurred: " + e.getMessage()));
        }
    }

    @Override
    public Either<CustomerError, Integer> save(Customer c, Credential credential) {
        try (MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);

            // Save the Credential
            MongoCollection<Document> credentialCollection = db.getCollection(Constants.CREDENTIALS);
            Document credentialDocument = Document.parse(gson.toJson(credential));
            credentialCollection.insertOne(credentialDocument);

            // Retrieve the generated Credential ID
            ObjectId credentialId = credentialDocument.getObjectId("_id");

            // Associate the Credential ID with the Customer
            c.set_id(credentialId);

            // Save the Customer with the associated Credential ID
            MongoCollection<Document> customerCollection = db.getCollection(Constants.CUSTOMERS);
            Document customerDocument = Document.parse(gson.toJson(c));
            customerCollection.insertOne(customerDocument);

            int insertedCount = customerDocument.size();

            if (insertedCount > 0) {
                return Either.right(insertedCount);
            } else {
                return Either.left(new CustomerError(1, "Error saving customer"));
            }
        } catch (Exception e) {
            return Either.left(new CustomerError(1, "An error occurred: " + e.getMessage()));
        }
    }


    @Override
    public Either<CustomerError, Integer> update(Customer customer) {
        try ( MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);
            MongoCollection<Document> est = db.getCollection(Constants.CUSTOMERS);

            Document updateFields = new Document();
            updateFields.append("first_name", customer.getFirst_name())
                    .append("last_name", customer.getLast_name())
                    .append("email", customer.getEmail())
                    .append("phone", customer.getPhone())
                    .append("dob", customer.getDob());

            // Construir la actualización
            Document update = new Document("$set", updateFields);

            est.updateOne(eq("_id", customer.get_id()), update);

            long modifiedCount = est.countDocuments(eq("_id", customer.get_id()));

            if (modifiedCount > 0) {
                return Either.right((int) modifiedCount);
            } else {
                return Either.left(new CustomerError(1, "Error updating customer"));
            }
        } catch (Exception e) {
            return Either.left(new CustomerError(1, "Error updating customer: " + e.getMessage()));
        }

    }

    @Override
    public Either<CustomerError, Integer> delete(Customer c, boolean deleteOrders) {
        try ( MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);
            MongoCollection<Document> est = db.getCollection(Constants.CREDENTIALS);
            MongoCollection<Document> est2 = db.getCollection(Constants.CUSTOMERS);

            est.deleteOne(eq("_id", c.get_id()));
            est2.deleteOne(eq("_id", c.get_id()));

            long deletedCount = est.countDocuments(eq("_id", c.get_id()));
            long deletedCount2 = est2.countDocuments(eq("_id", c.get_id()));

            if (deletedCount > 0 && deletedCount2 > 0) {
                return Either.right(1);
            } else {
                return Either.left(new CustomerError(1, "Error deleting customer"));
            }
        } catch (Exception e) {
            return Either.left(new CustomerError(1, "Error deleting customer: " + e.getMessage()));
        }
    }

    @Override
    public Customer getCustomerByOrder(Order order) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> customerCollection = db.getCollection("customers");

            // Build the filter for the nested fields
            Bson filter = and(
                    eq("orders.date", order.getDate()),
                    eq("orders.table_id", order.getTable_id()),
                    eq("orders.orderItems", order.getOrderItems())
            );

            Document customerDocument = customerCollection.find(filter).first();

            if (customerDocument != null) {
                return gson.fromJson(customerDocument.toJson(), Customer.class);
            } else {
                // Handle the case when no matching customer is found
                return null;
            }

        } catch (Exception e) {
            // Handle exceptions (log or throw as needed)
            e.printStackTrace();
            return null;
        }
    }

//    public Customer getCustomerByOrder(Order order) {
//        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
//            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
//            MongoCollection<Document> customerCollection = db.getCollection("customers");
//
//            // Build the filter for the nested fields
//            Bson filter = and(
//                    eq("orders.date", order.getDate()),
//                    eq("orders.table_id", order.getTable_id()),
//                    eq("orders.orderItems", order.getOrderItems())
//            );
//
//            Document customerDocument = customerCollection.find(filter).first();
//
//            if (customerDocument != null) {
//                return gson.fromJson(customerDocument.toJson(), Customer.class);
//            } else {
//                // Handle the case when no matching customer is found
//                return null;
//            }
//
//        } catch (Exception e) {
//            // Handle exceptions (log or throw as needed)
//            e.printStackTrace();
//            return null;
//        }
//    }


}
