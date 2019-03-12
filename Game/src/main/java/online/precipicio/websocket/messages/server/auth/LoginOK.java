package online.precipicio.websocket.messages.server.auth;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class LoginOK extends ServerMessage {

    public LoginOK() {
        super(Messages.LOGIN_OK);
    }

    @Override
    public void compose() {

    }
}
