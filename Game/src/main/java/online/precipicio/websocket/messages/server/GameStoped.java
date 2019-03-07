package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class GameStoped extends ServerMessage {

    public GameStoped() {
        super(Messages.GAME_STOPPED);
    }

    @Override
    public void compose() {
        writeString("c", "iu");
    }
}
