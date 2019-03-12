package online.precipicio.game.room;

import online.precipicio.game.arena.Arena;
import online.precipicio.game.arena.ArenaPlayer;
import online.precipicio.game.util.StatsUtil;
import online.precipicio.threading.ThreadPool;
import online.precipicio.websocket.messages.server.*;
import online.precipicio.websocket.messages.server.auth.InvalidUsername;
import online.precipicio.websocket.messages.server.lobby.RoomFull;
import online.precipicio.websocket.messages.server.room.*;
import online.precipicio.websocket.messages.server.room.player.PlayerJoin;
import online.precipicio.websocket.messages.server.room.player.PlayerLeave;
import online.precipicio.websocket.messages.server.room.player.SelfJoin;
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

    private int timeout; //seconds
    private int maxRounds;
    private int roundCount;
    private int maxPlayers;
    private int minPlayers;
    private int newGameTime;
    private int width;
    private int height;

    private List<Session> fastStart = new CopyOnWriteArrayList<>();

    private volatile Session playingSession;

    public Room(String uuid){

        newGameTime = 25;
        timeout = 15;
        maxRounds = 5;
        roundCount = 0;
        maxPlayers = 6;
        minPlayers = 2;
        width = 6;
        height = 5;

        this.state = RoomState.WAITING;
        this.uuid = uuid;

        this.arena = new Arena(this,width,height);
        this.sessions = new CopyOnWriteArrayList<>();
        //));
    }


    public void addUser(Session session){

        if (session.getName() == null){
            session.send(new InvalidUsername());
            return;
        }

        if (sessions.size() == this.maxPlayers) {
            session.send(new RoomFull());
            return;
        }
        if (sessions.contains(session)){
            return;
        }
        //Add player in room
        sessions.add(session);
        //add player in arena
        arena.addPlayer(session);
        session.setRoom(this);

        //Transmit to users
        session.send(new RoomInfos(this.uuid, timeout, maxRounds, width, height));
        //
        String randomImg = "https://api.adorable.io/avatars/285/"+session.getId()+session.getName().replace(" ", "");

        session.send(new SelfJoin(session.getId(), session.getName(), randomImg,10,randomImg, sessions));
        broadcast(new PlayerJoin(session.getId(), session.getName(), randomImg, 10, randomImg), session);


        //Add vote button to user screen
        if (sessions.size() == minPlayers){
            broadcast(new VoteStartButton(true));
        } else if (sessions.size() > minPlayers) {
            session.send(new VoteStartButton(true));
        }


        //time
        if((sessions.size()) == this.maxPlayers && this.state == RoomState.WAITING){
            startGame();
        }

        if (this.state != RoomState.WAITING){
            ///Todo::
            broadcast(new NextRound(this.roundCount, arena.getPlayers(), 0));
            //session.send(new);
        }
    }

    public void removeUser(Session session){
        long id = session.getId();
        arena.removePlayer(session.getArenaPlayer());
        sessions.remove(session);
       //Stop game if only have one player
        if (this.state == RoomState.STARTED){
            if (sessions.size() <= 1){
               stopGame();
            }
        }
        //Unload room
        if (sessions.isEmpty()){
            RoomManager.getInstance().unloadRoom(this);
            return;
        }
        //Clear fast start
        if (sessions.size() == 1){
            fastStart.clear();
            broadcast(new VoteStartButton(false));
        }
        broadcast(new PlayerLeave(id));
    }

    private void startGame(){
        StatsUtil.getInstance().addStartedRoom();
        this.state = RoomState.STARTED;
        broadcast(new VoteStartButton(false));
        ThreadPool.getInstance().shedule(this::nextRound, 2);
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



    private void nextRound(){
        if (this.state != RoomState.STARTED){
            return;
        }
        if (this.roundCount+1 > this.maxRounds){
            gameOver();
            return;
        }

        playingSession = null;

        for (Session session: sessions){
            if (session.getArenaPlayer() != null){
                if (session.getArenaPlayer().getTimeoutSchedule() != null){
                    session.getArenaPlayer().getTimeoutSchedule().cancel(true);
                    session.getArenaPlayer().setTimeoutSchedule(null);
                }
            }
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
            broadcast(new GameFinished());
            StatsUtil.getInstance().removeStartedRoom();
            this.roundCount = 0;
            if (sessions.size() == maxPlayers){
                broadcast(new NewGameStarting(newGameTime));
                ThreadPool.getInstance().shedule(()->{
                    if (sessions.size() == maxPlayers){
                        this.nextRound();
                    } else {
                        this.state = RoomState.WAITING;
                        broadcast(new InsuficientUsersToStart());
                        broadcast(new VoteStartButton(true));
                    }
                }, newGameTime);
                return;
            } else {
                broadcast(new VoteStartButton(true));
            }
            this.state = RoomState.WAITING;
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

    public boolean isFull(){
        return sessions.size() == maxPlayers;
    }

    public void voteStart(Session session){
        if (state != RoomState.WAITING){
            return;
        }
        fastStart.add(session);
        if (sessions.size() >= minPlayers && fastStart.size() == sessions.size()){
            startGame();
            fastStart.clear();
        }
    }

    public void dispose(){
        playingSession = null;
        sessions.clear();
        arena.dispose();

    }

}
