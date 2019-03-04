package online.precipicio.websocket.messages.client;


import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.messages.server.Pong;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;


@UserEvent(id= UserEvents.PING)
public class PingEvent implements Event {

    @Override
    public void handle(ClientMessage message) {
        if (message.getSession() == null){
            return;
        }
        message.getSession().send(new Pong());
    }


}
