package online.precipicio.websocket.messages.client;

import online.precipicio.game.room.Room;
import online.precipicio.game.util.Direction;
import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.messages.server.PlayerMovement;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;

@UserEvent(id= UserEvents.PLAYER_MOVE)
public class PlayerMoveEvent implements Event {

    @Override
    public void handle(ClientMessage message) {
        final Session session = message.getSession();
        if (session == null){
            return;
        }
        Direction direction = Direction.getDirectionByValue(message.readInt("d"));
        if (direction == Direction.NOTHING){
            return;
        }
        if (session.getArenaPlayer() == null){
            return;
        }
        final Room room = session.getRoom();
        if (room == null){
            return;
        }
        if (room.getPlayingSession() != session){
            return;
        }
        room.setPlayingSession(null);

        session.getArenaPlayer().getTimeoutSchedule().cancel(false);
        session.getArenaPlayer().setTimeoutSchedule(null);

        room.getArena().movePlayer(session.getArenaPlayer(), direction);

        room.broadcast(new PlayerMovement(session.getId(),direction), session);

        if (room.getArena().getPlayers().size() > 1){
            room.requestSessionMove();
        }
    }

}
