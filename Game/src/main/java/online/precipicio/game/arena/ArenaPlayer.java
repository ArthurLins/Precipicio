package online.precipicio.game.arena;

import online.precipicio.websocket.sessions.Session;

import java.util.concurrent.Future;

public class ArenaPlayer {


    private String skin;

    private Session session;
    private Future timeoutSchedule;

    private Square currentSquare;


    public ArenaPlayer(Session session){
        this.session = session;
        session.setArenaPlayer(this);
        this.skin = "";
    }


    public Square getCurrentSquare() {
        return currentSquare;
    }

    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public Session getSession() {
        return session;
    }


    public String getSkin() {
        return skin;
    }

    public Future getTimeoutSchedule() {
        return timeoutSchedule;
    }

    public void setTimeoutSchedule(Future timeoutSchedule) {
        this.timeoutSchedule = timeoutSchedule;
    }

    public void setSkin(String color) {
        this.skin = color;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
