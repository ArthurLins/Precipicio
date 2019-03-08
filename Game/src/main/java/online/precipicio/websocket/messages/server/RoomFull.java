package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class RoomFull extends ServerMessage {

    public RoomFull() {
        super(Messages.ROOM_FULL);
    }

    @Override
    public void compose() {

    }
}
