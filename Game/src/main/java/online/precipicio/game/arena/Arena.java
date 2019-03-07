package online.precipicio.game.arena;

import online.precipicio.game.room.Room;
import online.precipicio.game.room.RoomState;
import online.precipicio.game.util.ArenaSpawnUtil;
import online.precipicio.game.util.Direction;
import online.precipicio.game.util.Position;
import online.precipicio.websocket.sessions.Session;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Arena {

    private int width;
    private int length;
    private Square[][] squares;
    private Room room;


    private List<Position> spawnPositions;
    private List<ArenaPlayer> alivePlayers;



    public Arena(Room room, int width, int length){
        this.room = room;
        this.squares = new Square[width][length];
        this.width = width;
        this.length = length;
        this.alivePlayers = new CopyOnWriteArrayList<>();
        generateSpawns();
        generate();
    }

    private void generate(){
        for(int y = 0; y < this.length; y++) {
            for(int x = 0; x < this.width; x++) {
                this.squares[x][y] = new Square(x,y);
            }
        }
    }

    private Position getRandomSpawnPoint(){
        if (spawnPositions.isEmpty()){
            return null;
        }
        Random rand = new Random();
        int val = rand.nextInt(spawnPositions.size());
        Position pos = spawnPositions.get(val);
        if (pos == null){
            return null;

        }
        spawnPositions.remove(val) ;
        return pos;
    }


    public void spawnPlayer(ArenaPlayer arenaPlayer){
        Position pos = getRandomSpawnPoint();
        if (pos == null){
            return;
        }
        setPlayerPos(arenaPlayer, pos.getX(), pos.getY());
        alivePlayers.add(arenaPlayer);
    }


    private void setPlayerPos(ArenaPlayer arenaPlayer, int x, int y){
        Square square = this.getSquare(x,y);

        if (square == null){
            return;
        }
        arenaPlayer.setX(x);
        arenaPlayer.setY(y);
        square.setArenaPlayer(arenaPlayer);
    }

    private Square getSquare(int x, int y){
        try {
            return squares[x][y];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }


    private void killPlayer(ArenaPlayer arenaPlayer){
        alivePlayers.remove(arenaPlayer);
        System.out.println("["+arenaPlayer.getSession().getName()+"] Morreu " + alivePlayers.size());
        if (alivePlayers.size() == 1){
            room.gameOver();
        }
    }

    public void movePlayer(ArenaPlayer player, Direction direction) {
        if (player == null){
            return;
        }

        int x = player.getX();
        int y = player.getY();

        Square oldSquere = this.getSquare(x, y);

        if (oldSquere == null){
            return;
        }
        oldSquere.removePlayer();

        switch(direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }

        Square newSquare = this.getSquare(x, y);

        if (newSquare == null) {
            killPlayer(player);
        } else {
            if (newSquare.getArenaPlayer() != null){
                this.movePlayer(newSquare.getArenaPlayer(), direction);
            }
            newSquare.setArenaPlayer(player);
            player.setX(x);
            player.setY(y);
        }
    }

    public void removePlayer(ArenaPlayer arenaPlayer){
        alivePlayers.remove(arenaPlayer);
        Square square = getSquare(arenaPlayer.getX(), arenaPlayer.getY());
        if (square == null){
            return;
        }
        if (square.getArenaPlayer() != null){
            if (room.getState() == RoomState.WAITING){
                spawnPositions.add(new Position(arenaPlayer.getX(), arenaPlayer.getY()));
            }
            square.removePlayer();
        }
    }

    public void reset(){
        generateSpawns();
        alivePlayers.clear();
        for (Session session : room.getSessions()){
            spawnPlayer(session.getArenaPlayer());
        }
    }

    public ArenaPlayer getNextMove(){
        if (this.alivePlayers.isEmpty()){
            this.room.gameOver();
            return null;
        }

        Random rand = new Random();
        int val = rand.nextInt(alivePlayers.size());
        return alivePlayers.get(val);
    }


    public List<ArenaPlayer> getAlivePlayers() {
        return alivePlayers;
    }

    private void generateSpawns(){
        spawnPositions = ArenaSpawnUtil.getSpawnList();
    }

    public long getWinnerId(){
        if (alivePlayers.size() > 1){
            return 0;
        } else {
            return alivePlayers.stream().findFirst().map((arenaPlayer -> (arenaPlayer.getSession().getId()))).orElse(0L);
        }
    }

}
