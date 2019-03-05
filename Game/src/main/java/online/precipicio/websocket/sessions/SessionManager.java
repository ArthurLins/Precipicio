package online.precipicio.websocket.sessions;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static online.precipicio.websocket.WebSocketServerHandler.WS_SESSION_ID;

public class SessionManager {
    private static SessionManager ourInstance = new SessionManager();

    public static SessionManager getInstance() {
        return ourInstance;
    }

    private AtomicLong ids = new AtomicLong(0);
    private ConcurrentHashMap<Long, Session> sessions = new ConcurrentHashMap<>();



    private SessionManager() {
    }

    public Session getSession(long id){
        return sessions.get(id);
    }

    public void addSession(Channel channel, String name){
        long id = ids.getAndIncrement();
        channel.attr(WS_SESSION_ID).set(id);
        Session session = new Session(id, channel, name);
        //Temp
        session.setAvatar("https://api.adorable.io/avatars/285/"+session.getId()+session.getName());
        sessions.put(id, session);

    }

    public void removeSession(long id){
        Session session = sessions.get(id);
        if (session != null){
            System.out.println("kdkd");
            session.dispose();
            sessions.remove(id);
        }
    }

    public int activeSessions(){
        return sessions.size();
    }



}
