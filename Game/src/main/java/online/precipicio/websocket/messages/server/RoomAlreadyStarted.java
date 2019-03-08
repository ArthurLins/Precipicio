package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class RoomAlreadyStarted extends ServerMessage {

    public RoomAlreadyStarted() {
        super(Messages.ROOM_ALREADY_STARTED);
    }

    @Override
    public void compose() {

    }
}
