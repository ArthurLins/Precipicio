package online.precipicio.websocket.messages.server.auth;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class InvalidUsername extends ServerMessage {

    public InvalidUsername() {
        super(Messages.INVALID_USERNAME);
    }

    @Override
    public void compose() {



    }
}
