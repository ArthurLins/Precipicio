package online.precipicio.websocket.sessions;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import online.precipicio.game.arena.ArenaPlayer;
import online.precipicio.game.room.Room;
import online.precipicio.websocket.types.ServerMessage;


public class Session {


    private final long id;
    private final String name;
    private final Channel channel;
    private Room room;
    private ArenaPlayer arenaPlayer;


    public Session(long id, Channel channel, String name) {
        this.id = id;
        this.name = name;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void send(ServerMessage message){
        message.compose();
        channel.writeAndFlush(new TextWebSocketFrame(message.serialize()));
    }


    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ArenaPlayer getArenaPlayer() {
        return arenaPlayer;
    }

    public void setArenaPlayer(ArenaPlayer arenaPlayer) {
        this.arenaPlayer = arenaPlayer;
    }

    public void dispose(){
        if (room != null){
            room.removeUser(this);
        }
        arenaPlayer = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Session){
            return ((Session) obj).id == id;
        }
        return super.equals(obj);
    }
}
