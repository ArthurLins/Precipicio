package online.precipicio.game.realtime;

import online.precipicio.game.room.RoomManager;
import online.precipicio.websocket.messages.server.lobby.RealTimeInfos;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.sessions.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RealTimeInfosService implements IInscribable {

    private static RealTimeInfosService instance = new RealTimeInfosService();

    public static RealTimeInfosService getInstance(){
        return instance;
    }


    private List<Session> sessions = Collections.synchronizedList(new ArrayList<>());

    private RealTimeInfosService(){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    private int users;
    private int rooms;

    @Override
    public void tick() {
        if (sessions.isEmpty()){
            return;
        }

        rooms = RoomManager.getInstance().currentRoomsQty();
        users = SessionManager.getInstance().activeSessions();

        for (Session session: sessions){
            session.send(new RealTimeInfos(rooms, users));
        }
    }

    @Override
    public void subscribe(Session session) {
        sessions.add(session);
        session.send(new RealTimeInfos(rooms, users));
    }

    @Override
    public void unsubscribe(Session session) {
        sessions.remove(session);
    }
}
