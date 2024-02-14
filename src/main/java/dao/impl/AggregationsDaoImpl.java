package dao.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import dao.AggregationsDao;
import io.vavr.control.Either;
import model.errors.OrderError;
import model.errors.OrderError;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;

public class AggregationsDaoImpl implements AggregationsDao {
    @Override
    public Either<OrderError, String> ExA() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("menuitems");


            String result = col.aggregate(Arrays.asList(
                    sort(Sorts.descending("price")),
                    limit(1),
                    project(Projections.fields(Projections.excludeId(), Projections.include("description")))
            )).first().toJson();

            return Either.right(result);

        } catch (Exception e){
            return Either.left(new OrderError("Error"));
        }
    }

    @Override
    public Either<OrderError, String> ExB(ObjectId id) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    match(eq("_id", id)),
                    project(Projections.fields(Projections.excludeId(), Projections.include("first_name", "orders.table_id")))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExC() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    group("$orders.order_date", sum("total_items", "$orders.order_items.quantity"))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExD() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    match(eq("orders.order_items.menu_item_id", 4)),
                    project(Projections.fields(Projections.excludeId(), Projections.include("first_name")))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExE() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    group("$orders.order_date", sum("total", "$orders.order_items.quantity")),
                    group(null, avg("average", "$total")),
                    project(Projections.fields(Projections.excludeId(), Projections.include("average")))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExF() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    group("$orders.order_items.menu_item_id", sum("total", "$orders.order_items.quantity")),
                    sort(Sorts.descending("total")),
                    limit(1)
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    //TODO: Fix this
    @Override
    public Either<OrderError, String> ExG(ObjectId id) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    match(eq("_id", id)),
                    group(and(eq("customer_id", "$_id"), eq("menu_item_id", "$orders.order_items.menu_items_id")),
                            sum("totalItems", "$orders.order_items.quantity")),
                    project(fields(excludeId(),  computed("item_id", "$orders.order_items.menu_item_id"),
                            include("totalItems", "$totalItems")))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExH() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    group("$orders.table_id", sum("total", 1)),
                    sort(Sorts.descending("total")),
                    limit(1)
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExI() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    group(and(eq("customer_name", "$first_name"), eq("table_id", "$orders.table_id")),
                            sum("total", 1)),
                    sort(Sorts.descending("total")),
                    group(eq("_id", "$_id.customer_name"), first("table_id", "$_id.table_id"), max("total", "$total"))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExJ() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    group(and(eq("item_id", "$orders.order_items.menu_item_id")),
                            addToSet("order_quantities", "$orders.order_items.quantity")),
                    match(eq("order_quantities", 1)),
                    addFields(new Field("item_id", "$_id.item_id")),
                    project(fields(excludeId(),  include("item_id")))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExK() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("customers");

            List<Document> results = col.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    lookup("menuitems", "orders.order_items.menu_item_id", "_id", "menuitems"),
                    unwind("$menuitems"),
                    addFields(new Field("total", new Document("$multiply", Arrays.asList("$orders.order_items.quantity", "$menuitems.price")))),
                    group("$orders.order_date", sum("total", "$total"))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n" ));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExL() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("customers");

            List<Document> results = customersCollection.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    lookup("menuitems", "orders.order_items.menu_item_id", "_id", "menuitems"),
                    unwind("$menuitems"),
                    addFields(new Field("total", new Document("$multiply", Arrays.asList("$orders.order_items.quantity", "$menuitems.price")))),
                    group("$first_name", sum("total", "$total")),
                    sort(Sorts.descending("first_name","total")),
                    limit(1)

            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n"));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> ExM() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("customers");

            List<Document> results = customersCollection.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.order_items"),
                    lookup("menuitems", "orders.order_items.menu_item_id", "_id", "menuitems"),
                    unwind("$menuitems"),
                    addFields(new Field("total", new Document("$multiply", Arrays.asList("$orders.order_items.quantity", "$menuitems.price")))),
                    group(null, sum("total_amount", "$total")),
                    project(fields(
                            excludeId(),
                            computed("total_amount", "$total_amount")
                    ))
            )).into(new ArrayList<>());

            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n"));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    //Get the names of a specie
    @Override
    public Either<OrderError, String> Ex1part2(String species) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_rym");
            MongoCollection<Document> customersCollection = db.getCollection("characters");

            List<Document> results = customersCollection.aggregate(Arrays.asList(
                    group(and(eq("species", "$species"),eq("name", "$name"))),
                    project(fields(excludeId(), eq("name", "$_id.name"), eq("species", "$_id.species"))),
                    match(eq("species", species)),
                    project(fields(excludeId(), include("name")))
            )).into(new ArrayList<>());


            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n"));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    //
    @Override
    public Either<OrderError, String> Ex2part2() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_rym");
            MongoCollection<Document> customersCollection = db.getCollection("characters");

            List<Document> results = customersCollection.aggregate(Arrays.asList(
                    unwind("$origin"),
                    match(eq("origin.name", "Abadango")),
                    project(fields(excludeId(), include("name", "status")))
            )).into(new ArrayList<>());


            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n"));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> Ex3part2() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_rym");
            MongoCollection<Document> customersCollection = db.getCollection("characters");

            List<Document> results = customersCollection.aggregate(Arrays.asList(
                    unwind("$episode"),
                    group("$name", sum("count",1)),
                    sort(Sorts.descending("count")),
                    limit(1)
            )).into(new ArrayList<>());


            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n"));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> Ex4part2(int id) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_rym");
            MongoCollection<Document> customersCollection = db.getCollection("characters");

            List<Document> results = customersCollection.aggregate(Arrays.asList(
                    unwind("$episode"),
                    match(eq("episode", "https://rickandmortyapi.com/api/episode/"+id)),
                    project(fields(excludeId(), include("name")))
            )).into(new ArrayList<>());


            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n"));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> Ex5part2(int id, String species) {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("victormoreno_rym");
            MongoCollection<Document> customersCollection = db.getCollection("characters");

            List<Document> results = customersCollection.aggregate(Arrays.asList(
                    unwind("$episode"),
                    match(and(eq("episode", "https://rickandmortyapi.com/api/episode/"+id),
                            eq("species", species))),
                    unwind("$location"),
                    project(fields(excludeId(), include("location.name", "location.url", "image")))
            )).into(new ArrayList<>());


            if (!results.isEmpty()) {
                String resultString = results.stream()
                        .map(Document::toJson)
                        .collect(Collectors.joining(", \n"));

                return Either.right(resultString);
            } else {
                return Either.left(new OrderError("No se encontraron resultados"));
            }

        } catch (Exception e) {
            return Either.left(new OrderError("Error: " + e.getMessage()));
        }
    }
}
