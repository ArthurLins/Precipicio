package online.precipicio.websocket.messages.client;

import online.precipicio.game.room.RoomManager;
import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;

@UserEvent(id=UserEvents.JOIN_ROOM)
public class JoinRoomEvent implements Event {

    @Override
    public void handle(ClientMessage message) {
        //String uuid = message.readString();
        //String color = message.readString();
        RoomManager.getInstance().joinRoom(message.readString("r"), message.getSession(), message.readString("c"));
    }
}
