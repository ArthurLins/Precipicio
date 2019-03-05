package online.precipicio.websocket.messages.client;

import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.messages.server.SelfJoin;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;

@UserEvent(id= UserEvents.ROOM_OK)
public class RoomOKEvent implements Event {
    @Override
    public void handle(ClientMessage message) {
        //message.getSession().send(new SelfJoin());
    }
}
