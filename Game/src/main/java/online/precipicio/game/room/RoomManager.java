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
        String uuid = UUID.randomUUID().toString();
        rooms.put(uuid, new Room(uuid));
        logger.info("Room created with id: "+uuid);

        if (session != null) {
            joinRoom(uuid, session);
        }

        StatsUtil.getInstance().addRoom();
    }

    public void joinRoom(String uuid, Session session){
        if (rooms.containsKey(uuid)){
            rooms.get(uuid).addUser(session);
            logger.info("USER JOIN JOIN: " + session.getId());
        } else  {
            //Random join
            for (Room room: rooms.values()){
                if (!room.isFull()){
                    room.addUser(session);
                    return;
                }
            }
            createRoom(session);
            //session.send(new RoomNotFound());
        }
    }

    public void leaveRoom(String uuid, Session session){
        if (rooms.containsKey(uuid)){
            rooms.get(uuid).removeUser(session);
        }
    }

    public void unloadRoom(Room room){
        if(rooms.containsKey(room.getUuid())){
            rooms.remove(room.getUuid());
            room.dispose();
        }
    }

    public int currentRoomsQty(){
        return rooms.size();
    }
}
