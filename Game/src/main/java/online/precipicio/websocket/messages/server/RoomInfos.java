package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;

public class RoomInfos extends ServerMessage {

    private String uuid;

    public RoomInfos(String uuid) {
        super(Messages.ROOM_INFOS);
        this.uuid = uuid;
    }

    @Override
    public void compose() {
        writeString("i", uuid);
    }
}
