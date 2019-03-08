package online.precipicio.websocket.messages.client;

import online.precipicio.game.room.Room;
import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.messages.server.ChatMessage;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;


@UserEvent(id=UserEvents.CHAT_MESSAGE)
public class ChatMessageEvent implements Event {
    @Override
    public void handle(ClientMessage message) {
        Room room = message.getSession().getRoom();

        if (room == null){
            return;
        }

        String textMessage = message.readString("m");

        if (textMessage.length() > 100){
            return;
        }

        room.broadcast(new ChatMessage(message.getSession().getId(),escapeHtml4(textMessage)));
    }
}
