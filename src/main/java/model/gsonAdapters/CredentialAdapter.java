package model.gsonAdapters;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Credential;
import org.bson.types.ObjectId;

import java.io.IOException;

public class CredentialAdapter extends TypeAdapter<Credential> {

    @Override
    public void write(JsonWriter out, Credential value) throws IOException {
        // Implementa la lógica de escritura si es necesario
    }

    @Override
    public Credential read(JsonReader in) throws IOException {
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
        ObjectId id = new ObjectId(jsonObject.getAsJsonObject("_id").get("$oid").getAsString());
        String username = jsonObject.get("user_name").getAsString();
        String password = jsonObject.get("password").getAsString();

        return new Credential(id, username, password);
    }
}