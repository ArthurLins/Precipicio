package online.precipicio.websocket.messages.structs;

public final class UserJson{
    private final long i;
    private final String n;
    private final String c;
    private final int x;
    private final int y;

    public UserJson(long i, String n, String c, int x, int y) {
        this.i = i;
        this.n = n;
        this.c = c;
        this.x = x;
        this.y = y;
    }
}
