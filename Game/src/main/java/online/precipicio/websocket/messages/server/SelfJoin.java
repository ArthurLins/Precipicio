package online.precipicio.websocket.messages.server;

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
    private final String color;
    private final int x;
    private final int y;
    private final List<Session> sessions;

    public SelfJoin(long id, String name, String color, int x, int y, List<Session> sessions) {
        super(Messages.SELF_JOIN);
        this.id = id;
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.sessions = sessions.stream().filter((session -> session.getId() != id)).collect(Collectors.toList());
    }

    @Override
    public void compose() {
        writeLong("i", this.id);
        writeString("n", this.name);
        writeString("c", this.color);
        writeInt("x", this.x);
        writeInt("y", this.y);
        writeInt("t", 15);
        writeInt("rc", 5);
        List<UserJson> activeUsers = new ArrayList<>();
        for (Session session : sessions){
            System.out.println();
            activeUsers.add(new UserJson(session.getId(), session.getName(), session.getArenaPlayer().getColor(),
                    session.getArenaPlayer().getX(), session.getArenaPlayer().getY()));
        }
        writeObject("a", activeUsers);
    }


}
