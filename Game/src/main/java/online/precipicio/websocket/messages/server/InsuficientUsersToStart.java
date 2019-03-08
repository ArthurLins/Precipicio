package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class InsuficientUsersToStart extends ServerMessage {

    public InsuficientUsersToStart() {
        super(Messages.PONG);
    }

    @Override
    public void compose() {

    }
}
