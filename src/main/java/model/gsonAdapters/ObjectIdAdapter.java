package model.gsonAdapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.lang.reflect.Type;

public class ObjectIdAdapter extends TypeAdapter<ObjectId> implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {

    @Override
    public void write(JsonWriter out, ObjectId value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            // Crea un objeto JSON con el campo "$oid" y su valor
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("$oid", new JsonPrimitive(value.toHexString()));

            // Escribe el objeto JSON en el flujo de salida
            out.jsonValue(jsonObject.toString());
        }
    }

    @Override
    public ObjectId read(JsonReader in) throws IOException {
        JsonElement jsonElement = JsonParser.parseReader(in);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Lee el valor del campo "$oid" y crea un ObjectId
        String objectIdString = jsonObject.getAsJsonPrimitive("$oid").getAsString();
        return new ObjectId(objectIdString);
    }

    @Override
    public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
        // Crea un objeto JSON con el campo "$oid" y su valor
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("$oid", new JsonPrimitive(src.toHexString()));
        return jsonObject;
    }

    @Override
    public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

        // Lee el valor del campo "$oid" y crea un ObjectId
        String objectIdString = jsonObject.getAsJsonPrimitive("$oid").getAsString();
        return new ObjectId(objectIdString);
    }
}