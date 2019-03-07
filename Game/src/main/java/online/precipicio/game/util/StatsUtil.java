package online.precipicio.game.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StatsUtil {
    private static StatsUtil ourInstance = null;

    public static StatsUtil getInstance() {
        if (ourInstance == null){
            ourInstance = new StatsUtil();
        }
        return ourInstance;
    }

    private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private AtomicInteger currentRooms;
    private AtomicInteger currentStartedRooms;
    private AtomicInteger currentSessions;

    private StatsUtil() {

        currentRooms = new AtomicInteger(0);
        currentStartedRooms = new AtomicInteger(0);
        currentSessions = new AtomicInteger(0);

    }

    public void start(){
        scheduledExecutor.scheduleAtFixedRate(()->{
            System.out.println("-------------------------------------------------------");
            System.out.println("Currentt rooms:          "+getValue(currentRooms));
            System.out.println("Currentt started rooms:  "+getValue(currentStartedRooms));
            System.out.println("Currentt  sessions:      "+getValue(currentSessions));
            System.out.println("-------------------------------------------------------");
        }, 0,1, TimeUnit.SECONDS);
    }


    public void addRoom(){
        System.out.println("HERE");
        currentRooms.incrementAndGet();
    }
    public void removeRoom(){
        currentRooms.decrementAndGet();
    }
    public void addStartedRoom(){
        currentStartedRooms.incrementAndGet();
    }
    public void removeStartedRoom(){
        currentStartedRooms.decrementAndGet();
    }
    public void addSession(){
        currentSessions.incrementAndGet();
    }
    public void removeSession(){
        currentSessions.decrementAndGet();
    }

    public synchronized int getValue(AtomicInteger integer){
        //synchronized (integer){
            return integer.get();
        //}
    }


}
