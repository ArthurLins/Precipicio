package online.precipicio.game.arena;

public class Square {

    private int x;
    private int y;
    private ArenaPlayer arenaPlayer;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        arenaPlayer = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ArenaPlayer getArenaPlayer() {
        return arenaPlayer;
    }

    public void setArenaPlayer(ArenaPlayer arenaPlayer) {
        this.arenaPlayer = arenaPlayer;
    }

    public void removePlayer(){
        arenaPlayer = null;
    }
}
