package online.precipicio.game.arena;

import online.precipicio.websocket.sessions.Session;

import java.util.concurrent.Future;

public class ArenaPlayer {

    private int x;
    private int y;

    private String color;

    private Session session;
    private Future timeoutSchedule;

    public ArenaPlayer(Session session, String color){
        this.session = session;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public Future getTimeoutSchedule() {
        return timeoutSchedule;
    }

    public void setTimeoutSchedule(Future timeoutSchedule) {
        this.timeoutSchedule = timeoutSchedule;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
