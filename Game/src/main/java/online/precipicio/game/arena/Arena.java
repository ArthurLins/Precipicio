package online.precipicio.game.arena;

import online.precipicio.game.room.Room;
import online.precipicio.game.util.ArenaSpawnUtil;
import online.precipicio.game.util.Direction;
import online.precipicio.game.util.Position;
import online.precipicio.websocket.messages.server.ScoreboardPoint;
import online.precipicio.websocket.sessions.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Arena {

    private int width;
    private int length;
    private Square[][] squares;
    private Room room;

    private final List<ArenaPlayer> players;

    private List<Position> spawnPositions;



    public Arena(Room room, int width, int length){
        this.room = room;
        this.squares = new Square[width][length];
        this.width = width;
        this.length = length;
        this.players = Collections.synchronizedList(new ArrayList<>());

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


    private void spawnPlayer(ArenaPlayer arenaPlayer){
        Position pos = getRandomSpawnPoint();
        if (pos == null){
            return;
        }
        setPlayerPos(arenaPlayer, pos.getX(), pos.getY());

    }

    public void addPlayer(Session session){
        session.setArenaPlayer(new ArenaPlayer(session));
    }


    private void setPlayerPos(ArenaPlayer arenaPlayer, int x, int y){
        Square square = this.getSquare(x,y);

        if (square == null){
            return;
        }
        arenaPlayer.setCurrentSquare(square);
        square.setArenaPlayer(arenaPlayer);
    }

    private Square getSquare(int x, int y){
        try {
            return squares[x][y];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    private Square getSquareFront(Square square, Direction direction){
        int X = square.getX();
        int Y = square.getY();

        switch(direction) {
            case UP:
                Y--;
                break;
            case DOWN:
                Y++;
                break;
            case LEFT:
                X--;
                break;
            case RIGHT:
                X++;
                break;
        }
        return this.getSquare(X, Y);
    }

    public void movePlayer(ArenaPlayer player, Direction direction) {
        Square s = getSquareFront(player.getCurrentSquare(), direction);
        if (s != null){
            if (!s.hasPlayer()){
                player.getCurrentSquare().setArenaPlayer(null);
                s.setArenaPlayer(player);
                player.setCurrentSquare(s);
            } else{
                movePlayer(s.getArenaPlayer(), direction);
                player.getCurrentSquare().setArenaPlayer(null);
                s.setArenaPlayer(player);
                player.setCurrentSquare(s);
            }
        } else {
            killPlayer(player);
        }
    }

    private void killPlayer(ArenaPlayer player) {
        removePlayer(player);
        if (players.size() == 1){
            room.gameOver();
        }
    }


    public void removePlayer(ArenaPlayer arenaPlayer){
        players.remove(arenaPlayer);
        if (arenaPlayer.getCurrentSquare() != null){
            Square square = getSquare(arenaPlayer.getCurrentSquare().getX(), arenaPlayer.getCurrentSquare().getY());
            arenaPlayer.setCurrentSquare(null);
            if (square == null){
                return;
            }
            square.removePlayer();
        }
    }

    public void reset(){
        players.clear();
        generateSpawns();
        for (Session session : room.getSessions()){
            ArenaPlayer arenaPlayer = new ArenaPlayer(session);
            players.add(arenaPlayer);
            spawnPlayer(arenaPlayer);
        }
    }

    private void generateSpawns(){
        spawnPositions = ArenaSpawnUtil.getSpawnList();
    }


    public List<ArenaPlayer> getPlayers(){
        return players;
    }

    public long getWinnerId(){
        if (players.size() > 1){
            return 0;
        } else {
            return players.stream().findFirst().map((arenaPlayer -> (arenaPlayer.getSession().getId()))).orElse(0L);
        }
    }


    public void dispose(){
        players.clear();
        spawnPositions.clear();
        for(int y = 0; y < this.length; y++) {
            for(int x = 0; x < this.width; x++) {
                this.squares[x][y].removePlayer();
                this.squares[x][y] =  null;
            }
        }
        room = null;
    }


    @Override
    public String toString() {
        StringBuilder map = new StringBuilder();
        for (int y = 0; y < length; y++){
            for (int x = 0; x < width; x++){
                map.append(squares[x][y]);
            }
            map.append("\n");
        }
        return map.toString();
    }


}
