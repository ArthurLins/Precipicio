package online.precipicio.websocket.messages.server.lobby;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class RoomNotFound extends ServerMessage {

    public RoomNotFound() {
        super(Messages.ROOM_NOT_FOUND);
    }

    @Override
    public void compose() {

    }
}
