package online.precipicio.game.realtime;

import online.precipicio.websocket.sessions.Session;

import java.util.concurrent.TimeUnit;

public interface IInscribable {
    void tick();
    void subscribe(Session session);
    void unsubscribe(Session session);
}
