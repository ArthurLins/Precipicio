package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class NewGameStarting extends ServerMessage {

    private final int time;

    public NewGameStarting(int time) {
        super(Messages.NEW_GAME_STARTING);
        this.time = time;
    }

    @Override
    public void compose() {
        writeInt("t",this.time);
    }
}
