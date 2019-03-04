package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class PlayerTurn extends ServerMessage {

    private final long id;

    public PlayerTurn(long id) {
        super(Messages.PLAYER_TURN);
        this.id = id;
    }

    @Override
    public void compose() {
        writeLong("i", this.id);
    }
}
