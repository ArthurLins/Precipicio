package online.precipicio.websocket.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import online.precipicio.websocket.sessions.Session;

public class ClientMessage {

    private final int header;
    private final JsonObject body;
    private final Session session;

    public ClientMessage(Session session, int header, JsonObject body) {
        this.session = session;
        this.header = header;
        this.body = body;

    }

    private JsonElement get(String name){
        return body.get(name);
    }

    public String readString(String name){
        return get(name).getAsString();
    }

    public double readDouble(String name){
        return get(name).getAsDouble();
    }

    public int readInt(String name){
        return get(name).getAsInt();
    }

    public boolean readBoolean(String name){
        return get(name).getAsBoolean();
    }

    public short readShort(String name){
        return get(name).getAsShort();
    }

    public JsonObject readJsonObject(String name){
        return get(name).getAsJsonObject();
    }

    public JsonArray readJsonArray(String name){
        return get(name).getAsJsonArray();
    }


    public int getHeader() {
        return this.header;
    }


    public Session getSession() {
        return session;
    }
}

