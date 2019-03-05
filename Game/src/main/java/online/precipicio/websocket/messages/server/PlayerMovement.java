package online.precipicio.websocket.messages.server;

import online.precipicio.game.util.Direction;
import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.types.ServerMessage;

public class PlayerMovement extends ServerMessage {

    private final long id;
    private final Direction direction;

    public PlayerMovement(long id, Direction direction) {
        super(Messages.PLAYER_MOVEMENT);
        this.id = id;
        this.direction = direction;
    }

    @Override
    public void compose() {
        writeLong("i", id);
        writeInt("d", direction.getValue());
    }
}
