package online.precipicio.websocket.headers;

public enum Messages {

    PONG(0),
    SELF_JOIN(1),
    PLAYER_JOIN(2),
    PLAYER_LEAVE(3),
    PLAYER_TURN(5),
    PLAYER_MOVEMENT(6),
    ROOM_INFOS(7),
    CHAT_MESSAGE(8),
    SCOREBOARD_POINT(10),
    NEW_GAME_STARTING(11),
    GAME_FINISHED(12),
    INSUFICIENT_USERS_TO_START(13),
    INVALID_USERNAME(18),
    LOGIN_OK(19),
    REAL_TIME_INFOS(20),
    //PLAYER_TURN_TIMEOUT(1),

    //GAME_START(4),
    //GAME_STOP(5),
    GAME_NEXT_ROUND(4),
    ROOM_NOT_FOUND(14),
    ROOM_FULL(15),
    ROOM_ALREADY_STARTED(16),
    CURRENT_MAP(17),
    VOTE_START_BUTTON(21),
    GAME_STOPPED(9);
    //GAME_WINNER(6),
    //GAME_TIMEOUT(7);


    private int val;

    Messages(int val){
        this.val = val;
    }

    public int getVal(){
        return this.val;
    }

}
