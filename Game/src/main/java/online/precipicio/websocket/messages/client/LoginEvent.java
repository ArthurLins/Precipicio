package online.precipicio.websocket.messages.client;

import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.messages.server.auth.InvalidUsername;
import online.precipicio.websocket.messages.server.auth.LoginOK;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

@UserEvent(id = UserEvents.LOGIN)
public class LoginEvent implements Event {

    @Override
    public void handle(ClientMessage message) {

        String username = message.readString("n");

        if (username.length() > 25){
            message.getSession().send(new InvalidUsername());
            return;
        }

        message.getSession().setName(escapeHtml4(username));

        message.getSession().setAvatar("https://api.adorable.io/avatars/285/"+message.getSession().getId()+message.getSession().getName().replace(" ", ""));

        message.getSession().send(new LoginOK());
    }

}
