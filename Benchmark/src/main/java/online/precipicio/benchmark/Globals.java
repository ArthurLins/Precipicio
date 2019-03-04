package online.precipicio.benchmark;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Globals {
    private static Globals ourInstance = new Globals();

    public AtomicBoolean bool = new AtomicBoolean(true);
    public CopyOnWriteArrayList<String> roomIds = new CopyOnWriteArrayList<>();

    public static Globals getInstance() {
        return ourInstance;
    }

    private Globals() {
    }

}
