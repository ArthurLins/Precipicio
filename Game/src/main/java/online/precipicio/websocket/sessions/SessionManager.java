package online.precipicio.websocket.sessions;

import io.netty.channel.Channel;
import online.precipicio.game.realtime.RealTimeInfosService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static online.precipicio.websocket.WebSocketServerHandler.WS_SESSION_ID;

public class SessionManager {
    private static SessionManager ourInstance = new SessionManager();

    public static SessionManager getInstance() {
        return ourInstance;
    }

    private AtomicLong ids = new AtomicLong(1);
    private ConcurrentHashMap<Long, Session> sessions = new ConcurrentHashMap<>();



    private SessionManager() {
    }

    public Session getSession(long id){
        return sessions.get(id);
    }

    public void addSession(Channel channel){
        long id = ids.getAndIncrement();
        channel.attr(WS_SESSION_ID).set(id);
        Session session = new Session(id, channel);
        sessions.put(id, session);
        RealTimeInfosService.getInstance().subscribe(session);
    }

    public void removeSession(long id){
        Session session = sessions.get(id);
        RealTimeInfosService.getInstance().unsubscribe(session);
        if (session != null){
            session.dispose();
            sessions.remove(id);
        }
    }

    public int activeSessions(){
        return sessions.size();
    }



}
