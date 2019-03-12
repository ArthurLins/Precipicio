package online.precipicio.websocket.messages.server.room;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class GameFinished extends ServerMessage {

    public GameFinished() {
        super(Messages.GAME_FINISHED);
    }

    @Override
    public void compose() {

    }
}
