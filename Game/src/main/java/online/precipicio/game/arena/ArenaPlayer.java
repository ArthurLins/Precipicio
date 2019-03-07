package online.precipicio.game.arena;

import online.precipicio.websocket.sessions.Session;

import java.util.concurrent.Future;

public class ArenaPlayer {

    private int x;
    private int y;

    private boolean alive;

    private String skin;

    private Session session;
    private Future timeoutSchedule;

    public ArenaPlayer(Session session){
        this.session = session;
        this.skin = "";
        alive = true;
    }


    public boolean isValidMove(int x, int y){
        return (x == this.x || x == (this.x + 1) || x == (this.x - 1)) && ((y == this.y) || y == (this.y + 1) || y == (this.y - 1));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Session getSession() {
        return session;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
