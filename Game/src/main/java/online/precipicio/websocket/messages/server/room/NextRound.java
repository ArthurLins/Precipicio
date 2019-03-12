package online.precipicio.websocket.messages.server.room;

import online.precipicio.game.arena.ArenaPlayer;
import online.precipicio.websocket.headers.Messages;
import online.precipicio.websocket.messages.structs.ArenaPlayerJson;
import online.precipicio.websocket.types.ServerMessage;

import java.util.ArrayList;
import java.util.List;

public class NextRound extends ServerMessage {

    private final int roundCount;
    private final List<ArenaPlayer> players;
    private final long winnerId;

    public NextRound(int roundCount, List<ArenaPlayer> sessions, long winnerId) {
        super(Messages.GAME_NEXT_ROUND);
        this.roundCount = roundCount;
        this.players = sessions;
        this.winnerId = winnerId;
    }

    @Override
    public void compose() {
        writeInt("r",this.roundCount);
        writeLong("wi",this.winnerId);
        List<ArenaPlayerJson> activeUsers = new ArrayList<>();
        for (ArenaPlayer player : players){
            activeUsers.add(new ArenaPlayerJson(player.getSession().getId(), player.getCurrentSquare().getX(), player.getCurrentSquare().getY()));
        }
        writeObject("a", activeUsers);
    }
}
