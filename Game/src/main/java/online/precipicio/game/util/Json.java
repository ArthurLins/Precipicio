package online.precipicio.game.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Json {
    private static Json ourInstance = new Json();

    public static Json getInstance() {
        return ourInstance;
    }

    private JsonParser parser;
    private Gson gson;

    private Json() {
        this.gson = new Gson();
        this.parser = new JsonParser();
    }

    public JsonElement parse(String string){
        return this.parser.parse(string);
    }

    public String stringify(JsonElement element){
        return gson.toJson(element);
    }

    public JsonElement toJsonTree(Object obj){
        return gson.toJsonTree(obj);
    }
}
