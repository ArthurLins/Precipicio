package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class ChatMessage extends ServerMessage {

    private final long id;
    private final String message;

    public ChatMessage(long id, String message) {
        super(Messages.CHAT_MESSAGE);
        this.id = id;
        this.message = message;
    }

    @Override
    public void compose() {
        writeLong("i", id);
        writeString("m",message);
    }
}
