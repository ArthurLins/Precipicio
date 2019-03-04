package online.precipicio.websocket;


import com.google.gson.JsonObject;
import online.precipicio.Main;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;
import online.precipicio.websocket.sessions.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;



public class WebSocketEventHandler {

    private static Logger log = Logger.getLogger(WebSocketEventHandler.class.getName());

    private static WebSocketEventHandler instance;

    private Map<Integer, Class<?>> events = new HashMap<>();

    private WebSocketEventHandler() {
        load();
    }

    public static WebSocketEventHandler getInstance() {
        if (instance == null) {
            instance = new WebSocketEventHandler();
        }
        return instance;
    }

    private void load() {
        Set<Class<?>> md = Main.reflections.getTypesAnnotatedWith(UserEvent.class);
        md.forEach((c)->{
            if(!Event.class.isAssignableFrom(c)){
                return;
            }
            try {
                events.put(c.getAnnotation(UserEvent.class).id().getVal(), c);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void handle(Session session, int header, JsonObject object) {
        ClientMessage message = new ClientMessage(session, header, object);
        Class<?> clazz = events.get(message.getHeader());
        if (clazz != null){
            try {
                ((Event) clazz.newInstance()).handle(message);
            } catch (ClassCastException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
