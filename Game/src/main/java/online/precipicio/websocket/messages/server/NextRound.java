package online.precipicio.websocket.messages.server;

import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.messages.structs.ArenaPlayerJson;
import online.precipicio.websocket.messages.structs.UserJson;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.types.ServerMessage;

import java.util.ArrayList;
import java.util.List;

public class NextRound extends ServerMessage {

    private final int roundCount;
    private final List<Session> sessions;

    public NextRound(int roundCount, List<Session> sessions) {
        super(Messages.GAME_NEXT_ROUND);
        this.roundCount = roundCount;
        this.sessions = sessions;
    }

    @Override
    public void compose() {
        writeInt("r",this.roundCount);
        List<ArenaPlayerJson> activeUsers = new ArrayList<>();
        for (Session session : sessions){
            activeUsers.add(new ArenaPlayerJson(session.getId(), session.getArenaPlayer().getX(), session.getArenaPlayer().getY()));
        }
        writeObject("a", activeUsers);
    }
}
