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
    //PLAYER_TURN_TIMEOUT(1),

    //GAME_START(4),
    //GAME_STOP(5),
    GAME_NEXT_ROUND(4),
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
