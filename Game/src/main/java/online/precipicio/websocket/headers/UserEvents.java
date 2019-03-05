package online.precipicio.websocket.headers;

public enum UserEvents {

    PING(0),
    SELF_JOIN(1),
    USER_JOIN(2),
    JOIN_ROOM(4),
    PLAYER_MOVE(5),
    CREATE_ROOM(6),
    CHAT_MESSAGE(7),
    ROOM_OK(8);

    private int val;

    UserEvents(int val){
        this.val = val;
    }

    public int getVal(){
        return this.val;
    }

}
