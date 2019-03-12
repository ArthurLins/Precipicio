package online.precipicio.websocket.messages.server.room.player;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.messages.structs.UserJson;
import online.precipicio.websocket.types.ServerMessage;

public class PlayerJoin extends ServerMessage {

    private final long id;
    private final String name;
    private final String avatar;
    private final int level;
    private final String skin;

    public PlayerJoin(long id, String name, String avatar, int level, String skin) {
        super(Messages.PLAYER_JOIN);
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.level = level;
        this.skin = skin;
    }

    @Override
    public void compose() {
        writeObject("u", new UserJson(this.id, this.name, this.avatar, this.level, this.skin));
    }
}
