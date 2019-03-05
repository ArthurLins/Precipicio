package online.precipicio.game.arena;

import online.precipicio.game.room.Room;
import online.precipicio.game.room.RoomState;
import online.precipicio.game.util.ArenaSpawnUtil;
import online.precipicio.game.util.Direction;
import online.precipicio.game.util.Position;

import java.util.List;
import java.util.Random;

public class Arena {



    private int width;
    private int length;
    private Square[][] squares;
    private Room room;


    private List<Position> spawnPositions;



    public Arena(Room room, int width, int length){
        this.room = room;
        this.squares = new Square[width][length];
        this.width = width;
        this.length = length;
        generateSpawns();
        generate();
    }

    //Todo: Refactor
    private void generate(){
        for(int y = 0; y < this.length; y++) {
            for(int x = 0; x < this.width; x++) {
                this.squares[x][y] = new Square(x,y);
            }
        }
    }

    public Position getRandomSpawnPoint(){
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
        setPlayerPos(arenaPlayer, pos.getX(), pos.getY());
        //arenaPlayer.getSession().send(new SpawnPlayerMessage(arenaPlayer));
    }


    public void setPlayerPos(ArenaPlayer arenaPlayer, int x, int y){
        Square square = this.getSquare(x,y);

        if (square == null){
            return;
        }
        arenaPlayer.setX(x);
        arenaPlayer.setY(y);
        square.setArenaPlayer(arenaPlayer);
    }

    public Square getSquare(int x, int y){
        try {
            return squares[x][y];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }


    public void killPlayer(ArenaPlayer arenaPlayer){
        room.getAliveSessions().remove(arenaPlayer.getSession());
        System.out.println("["+arenaPlayer.getSession().getName()+"] Morreu");
        if (room.getAliveSessions().size() == 1){
            room.gameOver();
        }
    }

    public void moveUserToDirection(ArenaPlayer arenaPlayer, Direction direction) {
        switch(direction) {
            case UP:
                this.moveUserToCoords(arenaPlayer, 0, -1, direction);
                break;
            case DOWN:
                this.moveUserToCoords(arenaPlayer, 0, +1, direction);
                break;
            case LEFT:
                this.moveUserToCoords(arenaPlayer, -1, 0, direction);
                break;
            case RIGHT:
                this.moveUserToCoords(arenaPlayer, +1, 0, direction);
                break;
        }
    }


    public void moveUserToCoords(ArenaPlayer arenaPlayer, int x, int y, Direction direction) {
        if (arenaPlayer == null)
            return;

        int oldX = arenaPlayer.getX();
        int oldY = arenaPlayer.getY();

        Square oldSlot = this.getSquare(oldX, oldY);
        Square newSlot = this.getSquare(oldX+x, oldY+y);

        if (newSlot == null)
        {
            this.killPlayer(arenaPlayer);
            return;
        }

        if(!arenaPlayer.isValidMove(newSlot.getX(), newSlot.getY()))
        {
            return;
        }

        if (newSlot.getArenaPlayer() != null)
        {
            this.moveUserToDirection(newSlot.getArenaPlayer(), direction);
        }

        oldSlot.removePlayer();
        newSlot.setArenaPlayer(arenaPlayer);

        arenaPlayer.setX(newSlot.getX());
        arenaPlayer.setY(newSlot.getY());
    }


    public void removePlayer(ArenaPlayer arenaPlayer){
        Square square = getSquare(arenaPlayer.getX(), arenaPlayer.getY());
        if (square.getArenaPlayer() != null){
            if (room.getState() == RoomState.WAITING){
                //Add spawn slot;
                spawnPositions.add(new Position(arenaPlayer.getX(), arenaPlayer.getY()));
            }
            square.removePlayer();
        }
    }


    public void generateSpawns(){
        spawnPositions = ArenaSpawnUtil.getSpawnList();
    }

}
