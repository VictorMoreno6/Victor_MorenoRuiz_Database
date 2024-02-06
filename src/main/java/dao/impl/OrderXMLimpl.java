package dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import model.Customer;
import model.Order;
import model.errors.OrderError;
import model.gsonAdapters.LocalDateAdapter;
import model.gsonAdapters.LocalDateTimeAdapter;
import model.gsonAdapters.ObjectIdAdapter;
import model.xml.OrderItemXml;
import model.xml.Ordersxml;
import model.xml.Orderxml;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Named("orderXML")
public class OrderXMLimpl implements OrderDAO {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();

    private DBConnection db;

    @Inject
    public OrderXMLimpl(DBConnection db) {
        this.db = db;
    }
    @Override
    public Either<OrderError, List<Order>> getAll() {
        return null;
    }

    @Override
    public Either<OrderError,  List<Order>> get(ObjectId id) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> save(ObjectId customerId, Order order) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> update( Order newOrder, Order oldOrder) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> delete(Order order) {
        return null;
    }

    //TODO
    // hacer aqui el getAll
    // Cambiar tambn el save
    @Override
    public Either<OrderError, List<Order>> getAll(ObjectId idCustomer) {
        try {
            MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323/");
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            Document document = col.find(eq("_id", idCustomer)).first();

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
    public Either<OrderError, Integer> save(ObjectId customerId, List<Order> orders) {
        Path ordersXMLPath = Paths.get("data/Customer" + customerId + "orders.xml");

        try {
            JAXBContext context = JAXBContext.newInstance(Ordersxml.class, Orderxml.class, OrderItemXml.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Ordersxml ordersList = new Ordersxml();
            ordersList.setOrders(new ArrayList<>());

            int i = 0;
            for (Order order : orders) {
                i++;
                Orderxml newOrderXML = new Orderxml();
                newOrderXML.setId(i);
                newOrderXML.setOrderItems(new ArrayList<>());

                if (order.getOrderItems() != null) {
                    order.getOrderItems().forEach(orderItem -> {
                        OrderItemXml newOrderItemXML = new OrderItemXml();
                        newOrderItemXML.setMenuItem(orderItem.getMenuItemId());
                        newOrderItemXML.setQuantity(orderItem.getQuantity());
                        newOrderXML.getOrderItems().add(newOrderItemXML);
                    });
                }

                ordersList.getOrders().add(newOrderXML);
            }

            try (Writer writer = Files.newBufferedWriter(ordersXMLPath)) {
                marshaller.marshal(ordersList, writer);
            }

            return Either.right(0);

        } catch (Exception e) {
            return Either.left(new OrderError("Error saving orders"));
        }
    }

}
