package online.precipicio.game.room;

import online.precipicio.game.arena.Arena;
import online.precipicio.game.arena.ArenaPlayer;
import online.precipicio.game.util.StatsUtil;
import online.precipicio.threading.ThreadPool;
import online.precipicio.websocket.messages.server.*;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.types.ServerMessage;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {

    private String uuid;
    private List<Session> sessions;
    private List<Session> aliveSessions;
    private Arena arena;
    private RoomState state;

    private int timeout; //seconds
    private int maxRounds;
    private int roundCount;

    private volatile Session playingSession;

    public Room(String uuid){

        timeout = 15;
        maxRounds = 5;
        roundCount = 0;

        this.state = RoomState.WAITING;
        this.uuid = uuid;

        this.arena = new Arena(this,5,6);
        this.sessions = new CopyOnWriteArrayList<>();
        this.aliveSessions = new CopyOnWriteArrayList<>();
        //));
    }


    public void addUser(Session session, String color){
        if (this.state == RoomState.STARTED){
            return;
        }
        if (sessions.size() == 6) {
            return;
        }
        if (sessions.contains(session)){
            return;
        }
        sessions.add(session);
        ArenaPlayer arenaPlayer = new ArenaPlayer(session, color);

        session.setArenaPlayer(arenaPlayer);
        session.setRoom(this);

        //Transmit to users
        session.send(new SelfJoin(session.getId(), session.getName(), color, arenaPlayer.getX(), arenaPlayer.getY(), sessions));
        session.send(new RoomInfos(this.uuid));
        broadcast(new PlayerJoin(session.getId(), session.getName(), color, arenaPlayer.getX(), arenaPlayer.getY()), session);

        //time
        if((sessions.size()) == 6){
            StatsUtil.getInstance().addStartedRoom();
            this.state = RoomState.STARTED;
            nextRound();
        }
    }

    public void removeUser(Session session){
        long id = session.getId();
        arena.removePlayer(session.getArenaPlayer());
        sessions.remove(session);

        broadcast(new PlayerLeave(id));
    }


    public void broadcast(ServerMessage message){
        sessions.forEach((s)-> s.send(message));
    }

    public void broadcast(ServerMessage message, Session exclude){
        sessions.forEach((s)->{
            if (s.equals(exclude)){
                return;
            }
            s.send(message);
        });
    }



    public void nextRound(){
        if (this.state != RoomState.STARTED){
            return;
        }
        if (this.roundCount+1 > this.maxRounds){
            gameOver();
            return;
        }
        this.roundCount++;
        arena.generateSpawns();
        aliveSessions.addAll(sessions);
        assignPositions();
        broadcast(new NextRound(this.roundCount, aliveSessions));
        requestSessionMove();
    }

    private void assignPositions() {
        for (Session session : sessions){
            arena.spawnPlayer(session.getArenaPlayer());
        }
    }

    public Session getNextMoveSession(){
        if (aliveSessions.isEmpty()){
            gameOver();
            return null;
        }
        Random rand = new Random();
        int val = rand.nextInt(aliveSessions.size());
        return aliveSessions.get(val);
    }

    public void requestSessionMove(){
        Session session = getNextMoveSession();
        if (session == null){
            gameOver();
            return;
        }

        playingSession = session;
        broadcast(new PlayerTurn(session.getId()));
        session.getArenaPlayer().setTimeoutSchedule(ThreadPool.getInstance().shedule(this::requestSessionMove, timeout));

    }

    public void gameOver(){
        if (sessions.size() == 1 || roundCount+1 > maxRounds){
            StatsUtil.getInstance().removeStartedRoom();
            this.roundCount = 0;
            this.state = RoomState.WAITING;
            return;
        }
        nextRound();
    }



    public RoomState getState() {
        return state;
    }

    public void setState(RoomState state) {
        this.state = state;
    }

    public Session getPlayingSession() {
        return playingSession;
    }

    public void setPlayingSession(Session playingSession) {
        this.playingSession = playingSession;
    }

    public Arena getArena() {
        return arena;
    }

    public List<Session> getAliveSessions() {
        return aliveSessions;
    }
}
