package online.precipicio.websocket.sessions;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import online.precipicio.game.arena.ArenaPlayer;
import online.precipicio.game.room.Room;
import online.precipicio.websocket.types.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Session {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final long id;
    private final String name;
    private String avatar;
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
        final String txtMsg = message.serialize();
        logger.info(" ["+this.name+"] - "+txtMsg);
        channel.writeAndFlush(new TextWebSocketFrame(txtMsg));
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

    public void dispose() {
        if (room != null) {
            room.removeUser(this);
        }
        arenaPlayer = null;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
