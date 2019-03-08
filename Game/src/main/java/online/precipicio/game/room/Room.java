package online.precipicio.game.room;

import online.precipicio.game.arena.Arena;
import online.precipicio.game.arena.ArenaPlayer;
import online.precipicio.game.util.StatsUtil;
import online.precipicio.threading.ThreadPool;
import online.precipicio.websocket.messages.server.*;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.types.ServerMessage;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Room {

    private String uuid;
    private List<Session> sessions;
    private Arena arena;
    private RoomState state;

    private AtomicBoolean roundFinished = new AtomicBoolean(false);

    private int timeout; //seconds
    private int maxRounds;
    private int roundCount;
    private int maxPlayers;
    private int newGameTime;
    private int width;
    private int height;

    private volatile Session playingSession;

    public Room(String uuid){

        newGameTime = 25;
        timeout = 15;
        maxRounds = 5;
        roundCount = 0;
        maxPlayers = 6;
        width = 6;
        height = 5;

        this.state = RoomState.WAITING;
        this.uuid = uuid;

        this.arena = new Arena(this,width,height);
        this.sessions = new CopyOnWriteArrayList<>();
        //));
    }


    public void addUser(Session session){
        if (sessions.size() == this.maxPlayers) {
            session.send(new RoomFull());
            return;
        }
        if (sessions.contains(session)){
            return;
        }
        sessions.add(session);
        arena.addPlayer(session);
        session.setRoom(this);


        //Transmit to users
        session.send(new RoomInfos(this.uuid, timeout, maxRounds, width, height));
        //
        String randomImg = "https://api.adorable.io/avatars/285/"+session.getId()+session.getName().replace(" ", "");

        session.send(new SelfJoin(session.getId(), session.getName(), randomImg,10,randomImg, sessions));
        broadcast(new PlayerJoin(session.getId(), session.getName(), randomImg, 10, randomImg), session);

        //time
        if((sessions.size()) == this.maxPlayers && this.state == RoomState.WAITING){
            StatsUtil.getInstance().addStartedRoom();
            this.state = RoomState.STARTED;
            nextRound();
        }

        if (this.state == RoomState.WAITING){
            ///Todo::
            //session.send(new);
        }
    }

    public void removeUser(Session session){
        long id = session.getId();
        arena.removePlayer(session.getArenaPlayer());
        sessions.remove(session);
        if (this.state == RoomState.STARTED){
            if (sessions.size() <= 1){
               stopGame();
            } else {
                if (playingSession == session){
                    requestSessionMove();
                }
            }
        }
        broadcast(new PlayerLeave(id));
    }

    private void stopGame() {

        if (sessions.isEmpty()){
            RoomManager.getInstance().unloadRoom(this);
        }
        this.roundCount = 0;
        this.state = RoomState.WAITING;
        broadcast(new GameStoped());
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
        System.out.println("NOVO ROUND");
        if (this.state != RoomState.STARTED){
            return;
        }
        if (this.roundCount+1 > this.maxRounds){
            gameOver();
            return;
        }
        this.roundCount++;

        arena.reset();
        final long winnerId = arena.getWinnerId();
        broadcast(new NextRound(this.roundCount, arena.getPlayers(), winnerId));
        requestSessionMove();
    }


    private Session getNextMoveSession(){
        if (arena.getPlayers().isEmpty()){
            gameOver();
            return null;
        }
        Random rand = new Random();
        int val = rand.nextInt(arena.getPlayers().size());
        ArenaPlayer player = arena.getPlayers().get(val);
        return player.getSession();
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
        if (roundCount+1 > maxRounds || sessions.size() == 1){
            StatsUtil.getInstance().removeStartedRoom();
            this.roundCount = 0;
            if (sessions.size() == maxPlayers){
                broadcast(new NewGameStarting(newGameTime));
                ThreadPool.getInstance().shedule(()->{
                    if (sessions.size() == maxPlayers){
                        this.nextRound();
                    } else {
                        broadcast(new InsuficientUsersToStart());
                    }
                }, newGameTime);
                return;
            }
            this.state = RoomState.WAITING;
            broadcast(new GameFinished());
            return;
        }
        ThreadPool.getInstance().shedule(this::nextRound, 2);
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

    public List<Session> getSessions() {
        return sessions;
    }

    public Arena getArena() {
        return arena;
    }

    public String getUuid() {
        return uuid;
    }

    public void dispose(){
        playingSession = null;
        sessions.clear();
        arena.dispose();

    }

}
