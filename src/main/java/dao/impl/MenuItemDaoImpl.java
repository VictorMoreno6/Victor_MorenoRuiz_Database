package dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.*;
import common.Constants;
import dao.MenuItemDAO;
import io.vavr.control.Either;
import javafx.scene.control.Menu;
import model.Customer;
import model.MenuItem;
import model.errors.CustomerError;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MenuItemDaoImpl implements MenuItemDAO {
    private final Gson gson = new GsonBuilder().create();

    @Override
    public Either<String, List<MenuItem>> getAll() {
        List<MenuItem> menuItems = new ArrayList<>();

        try ( MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);
            MongoCollection<Document> col = db.getCollection(Constants.MENUITEMS);

            FindIterable<Document> menuItemDocuments = col.find();

            for (Document document : menuItemDocuments) {
                MenuItem menuitem = gson.fromJson(document.toJson(), MenuItem.class);
                menuItems.add(menuitem);
            }

            if (menuItems.isEmpty()) {
                return Either.left("Error connecting to database");
            } else {
                return Either.right(menuItems);
            }
        } catch (Exception e) {
            return Either.left("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public Either<String, MenuItem> get(int id) {
        try ( MongoClient mongo = MongoClients.create(Constants.MONGODB_INFORMATICA_IESQUEVEDO_ES_2323)) {
            MongoDatabase db = mongo.getDatabase(Constants.VICTORMORENO_RESTAURANT);
            MongoCollection<Document> col = db.getCollection(Constants.MENUITEMS);

            Document document = col.find(eq("_id", id)).first();

            if (document != null) {
                MenuItem menuItem = gson.fromJson(document.toJson(), MenuItem.class);
                return Either.right(menuItem);
            } else {
                return Either.left("Customer not found");
            }
        } catch (Exception e) {
            return Either.left("An error occurred: " + e.getMessage());
        }
    }


    @Override
    public Either<String, Integer> save(MenuItem c) {
        return null;
    }

    @Override
    public Either<String, Integer> update(MenuItem c) {
        return null;
    }

    @Override
    public Either<String, Integer> delete(MenuItem c) {
        return null;
    }
}
