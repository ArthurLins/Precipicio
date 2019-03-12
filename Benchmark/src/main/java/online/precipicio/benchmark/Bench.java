package online.precipicio.benchmark;

import java.util.concurrent.*;

public class Bench {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1,100,1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static void main(String... args) throws InterruptedException {
        //Create Rooms
        int rooms = 500;
        Globals.getInstance().bool.set(true);
        for (int i = 0; i < rooms; i++){
            try {

                WebSocketClient client = new WebSocketClient("ws://127.0.0.1:8081?");
                client.open();
                client.eval("[9,{\"n\":\"atu\"}]");
                client.eval("[6,{}]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        while (Globals.getInstance().roomIds.size() < rooms){
            Thread.sleep(50);
        }
        Globals.getInstance().bool.set(false);
        for (String roomId : Globals.getInstance().roomIds) {
            executor.execute(()->{
                for (int j = 0; j < 5; j++) {
                        try {
                            WebSocketClient clientPlayer = new WebSocketClient("ws://127.0.0.1:8081?username=ddjdjd");
                            clientPlayer.open();
                            clientPlayer.eval("[9,{\"n\":\"atu\"}]");
                            clientPlayer.eval("[4,{\"r\":\"" + roomId + "\", \"c\":\"red\"}]");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
        //Join to rooms
    }
}
