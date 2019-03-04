package online.precipicio.websocket.messages.client;

import online.precipicio.game.room.RoomManager;
import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;


@UserEvent(id=UserEvents.CREATE_ROOM)
public class CreateRoomEvent implements Event {

    @Override
    public void handle(ClientMessage message) {
        RoomManager.getInstance().createRoom(message.getSession());
    }
}
