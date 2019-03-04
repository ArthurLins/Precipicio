package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class PlayerJoin extends ServerMessage {

    private final long id;
    private final String name;
    private final String color;
    private final int x;
    private final int y;

    public PlayerJoin(long id, String name, String color, int x, int y) {
        super(Messages.PLAYER_JOIN);
        this.id = id;
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    @Override
    public void compose() {
        writeLong("i", this.id);
        writeString("n", this.name);
        writeString("c", this.color);
        writeInt("x", this.x);
        writeInt("y", this.y);
    }
}
