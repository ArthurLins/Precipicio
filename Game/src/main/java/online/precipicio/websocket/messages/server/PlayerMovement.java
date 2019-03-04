package online.precipicio.websocket.messages.server;

import online.precipicio.game.util.Direction;
import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class PlayerMovement extends ServerMessage {

    private final Direction direction;

    public PlayerMovement(Direction direction) {
        super(Messages.PLAYER_MOVEMENT);
        this.direction = direction;
    }

    @Override
    public void compose() {
        writeInt("d", direction.getValue());
    }
}
