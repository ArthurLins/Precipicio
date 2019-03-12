package online.precipicio.websocket.messages.client;

import online.precipicio.game.room.Room;
import online.precipicio.websocket.headers.UserEvents;
import online.precipicio.websocket.types.ClientMessage;
import online.precipicio.websocket.types.Event;
import online.precipicio.websocket.types.UserEvent;

@UserEvent(id = UserEvents.VOTE_START)
public class VoteStartEvent implements Event {

    @Override
    public void handle(ClientMessage message) {
        Room room = message.getSession().getRoom();

        if (room == null){
            return;
        }

        room.voteStart(message.getSession());
    }

}
