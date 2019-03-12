package online.precipicio.websocket.messages.server.lobby;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.types.ServerMessage;


public class RealTimeInfos extends ServerMessage {

    private int roomQty;
    private int usersQty;

    public RealTimeInfos(int roomQty, int usersQty) {
        super(Messages.REAL_TIME_INFOS);
        this.roomQty = roomQty;
        this.usersQty = usersQty;
    }

    @Override
    public void compose() {
        writeInt("u", usersQty);
        writeInt("r", roomQty);
    }
}
