package online.precipicio.game.arena;

public class Square {

    private ArenaPlayer arenaPlayer;

    private final int x;
    private final int y;



    public Square(int x, int y) {
        arenaPlayer = null;
        this.x = x;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasPlayer(){
        return arenaPlayer != null;
    }


    @Override
    public String toString() {
        return "["+((this.arenaPlayer == null) ? 0 : arenaPlayer.getSession().getId())+"]";
    }
}
