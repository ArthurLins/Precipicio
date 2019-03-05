package online.precipicio.websocket.messages.structs;

public final class ArenaPlayerJson {
    private final long i;
    private final int x;
    private final int y;

    public ArenaPlayerJson(long i, int x, int y) {
        this.i = i;
        this.x = x;
        this.y = y;
    }
}
