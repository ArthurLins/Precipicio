package online.precipicio.websocket.messages.structs;

public final class UserJson{
    private final long i;
    private final String n;
    private final String av;
    private final int l;
    private final String s;

    public UserJson(long i, String n, String av, int l, String s) {
        this.i = i;
        this.n = n;
        this.av = av;
        this.l = l;
        this.s = s;
    }

}
