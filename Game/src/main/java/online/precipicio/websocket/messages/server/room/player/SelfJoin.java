package online.precipicio.websocket.messages.server.room.player;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.messages.structs.UserJson;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.types.ServerMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelfJoin extends ServerMessage {

    private final long id;
    private final String name;
    private final String avatar;
    private final int level;
    private final String skin;

    private final List<Session> sessions;

    public SelfJoin(long id, String name, String avatar, int level, String skin, List<Session> sessions) {
        super(Messages.SELF_JOIN);
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.level = level;
        this.skin = skin;
        this.sessions = sessions;
    }

    @Override
    public void compose() {
        writeObject("me", new UserJson(id,name,avatar,level,skin));
        List<UserJson> activeUsers = new ArrayList<>();
        for (Session session : sessions){
            if (session.getId() == this.id) continue;
            activeUsers.add(new UserJson(session.getId(),session.getName(), session.getAvatar(), 10, session.getAvatar()));
        }
        writeObject("a", activeUsers);
    }


}
