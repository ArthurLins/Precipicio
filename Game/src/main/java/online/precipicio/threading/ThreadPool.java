package online.precipicio.threading;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    private static ThreadPool instance = new ThreadPool();

    public static ThreadPool getInstance(){
        return instance;
    }

    private ThreadPool(){

    }

    private ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(8);


    public Future shedule(Runnable job, int timeSeconds){
        return scheduledExecutor.schedule(job, timeSeconds, TimeUnit.SECONDS);
    }

}
