package online.precipicio.websocket.messages.server.room;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class VoteStartButton extends ServerMessage {

    private boolean available;

    public VoteStartButton(boolean available) {
        super(Messages.VOTE_START_BUTTON);
        this.available = available;
    }

    @Override
    public void compose() {
        writeBoolean("a", available);
    }
}
