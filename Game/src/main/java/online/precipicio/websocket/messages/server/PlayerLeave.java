package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class PlayerLeave extends ServerMessage {

    private long id;

    public PlayerLeave(long id) {
        super(Messages.PLAYER_LEAVE);
        this.id = id;
    }

    @Override
    public void compose() {
        writeLong("i", this.id);
    }
}
