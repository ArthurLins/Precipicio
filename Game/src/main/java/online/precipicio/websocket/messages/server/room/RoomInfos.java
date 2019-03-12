package online.precipicio.websocket.messages.server.room;

import online.precipicio.game.room.RoomState;
import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class RoomInfos extends ServerMessage {

    private String uuid;
    private final int timeout;
    private final int maxRounds;
    private final int w;
    private final int h;

    public RoomInfos(String uuid, int timeout, int maxRounds, int w, int h) {
        super(Messages.ROOM_INFOS);
        this.uuid = uuid;
        this.timeout = timeout;
        this.maxRounds = maxRounds;
        this.w = w;
        this.h = h;
    }

    @Override
    public void compose() {
        writeString("i", uuid);
        writeInt("tt", timeout);
        writeInt("mr", maxRounds);
        writeInt("w", w);
        writeInt("h", h);
    }
}
