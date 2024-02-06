package dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Configuration;
import common.Constants;
import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.java.Log;
import model.Credential;
import model.Customer;
import model.User;
import model.errors.CustomerError;
import model.gsonAdapters.CredentialAdapter;
import model.gsonAdapters.LocalDateAdapter;
import model.gsonAdapters.LocalDateTimeAdapter;
import model.gsonAdapters.ObjectIdAdapter;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mongodb.client.model.Filters.eq;

public class LoginDaoImpl implements LoginDAO {

    private final Gson gson = new GsonBuilder()
//            .registerTypeAdapter(Credential.class, new CredentialAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();

    @Override
    public Credential getCredential(String username) {
        Credential credential = null;
        try {
            MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323/");
            MongoDatabase db = mongo.getDatabase("victormoreno_restaurant");
            MongoCollection<Document> col = db.getCollection("credentials");

            Document document = col.find(eq("user_name", username)).first();

            if (document != null) {
                credential = gson.fromJson(document.toJson(), Credential.class);
            }
        } catch (Exception e) {
            credential = null;
        }
        return credential;
    }

    @Override
    public boolean doLogin(User user, Credential credential) {
        if (credential == null)
            return false;
        else
            return user.getPassword().equals(credential.getPassword());
    }
}
