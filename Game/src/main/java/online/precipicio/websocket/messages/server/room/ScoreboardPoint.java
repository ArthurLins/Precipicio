package online.precipicio.websocket.messages.server.room;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class ScoreboardPoint extends ServerMessage {

    private final int qty;

    public ScoreboardPoint(int qty) {
        super(Messages.SCOREBOARD_POINT);
        this.qty = qty;
    }

    @Override
    public void compose() {
        writeInt("q", this.qty);
    }
}
