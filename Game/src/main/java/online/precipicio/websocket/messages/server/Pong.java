package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class Pong extends ServerMessage {

    public Pong() {
        super(Messages.PONG);
    }

    @Override
    public void compose() {
        writeBoolean("pong",true);
    }
}
