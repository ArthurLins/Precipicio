package online.precipicio.game.room;

import online.precipicio.game.util.StatsUtil;
import online.precipicio.websocket.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {

    private Logger logger = LoggerFactory.getLogger(RoomManager.class.getSimpleName());

    private static RoomManager instance = new RoomManager();

    public static RoomManager getInstance(){
        return instance;
    }

    private ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public void createRoom(Session session){
        String uuid = "dbg";//UUID.randomUUID().toString();
        rooms.put(uuid, new Room(uuid));
        logger.debug("Room created with id: "+uuid);
        //joinRoom(uuid, session, "red");
        StatsUtil.getInstance().addRoom();
    }

    public void joinRoom(String uuid, Session session){
        if (rooms.containsKey(uuid)){
            rooms.get(uuid).addUser(session);
            logger.debug("USER JOIN JOIN: " + session.getId());
        }
    }

    public void leaveRoom(String uuid, Session session){
        if (rooms.containsKey(uuid)){
            rooms.get(uuid).removeUser(session);
        }
    }
}
