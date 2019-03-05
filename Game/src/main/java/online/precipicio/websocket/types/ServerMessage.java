package online.precipicio.websocket.types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import online.precipicio.game.util.Json;
import online.precipicio.websocket.headers.Messages;

import java.util.ArrayList;
import java.util.List;

public abstract class ServerMessage {

    private Messages head;
    private JsonObject body;

    public ServerMessage(Messages head){
        this.head = head;
        this.body = new JsonObject();
    }

    protected void writeString(String key, String value) {
        body.addProperty(key, value);
    }

    protected void writeDouble(String key, double d) {
        body.addProperty(key, d);
    }

    protected void writeInt(String key, int i) {
        body.addProperty(key, i);
    }

    protected void writeLong(String key, long i) {
        body.addProperty(key, i);
    }

    protected void writeBoolean(String key, boolean b) {
        body.addProperty(key, b);
    }

    protected void writeShort(String key, short s) {
        body.addProperty(key, s);
    }

    protected void writeObject(String key, Object object){
        body.add(key, new Gson().toJsonTree(object));
    }

    public abstract void compose();

    public String serialize(){
        List<Object> objects = new ArrayList<>();
        objects.add(0, this.head.getVal());
        objects.add(1, body);
        return Json.getInstance().stringify(Json.getInstance().toJsonTree(objects));
    }
}
