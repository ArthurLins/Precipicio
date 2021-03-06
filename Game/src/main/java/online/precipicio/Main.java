package online.precipicio;

import online.precipicio.game.room.RoomManager;
import online.precipicio.game.util.StatsUtil;
import online.precipicio.websocket.WebSocketEventHandler;
import online.precipicio.websocket.WebSocketServer;
import org.reflections.Reflections;

import java.util.Scanner;


public class Main {

    public static String[] args;
    public static Reflections reflections = new Reflections(Main.class.getPackage().getName());

    public static void main(String... args){

        Main.args = args;
        //Vm args to debug: -Dorg.slf4j.simpleLogger.defaultLogLevel=debug

        //RoomManager.getInstance().createRoom(null);
        WebSocketEventHandler.getInstance();
        WebSocketServer.getInstance().initialize();
        //StatsUtil.getInstance().start();

        Thread thread = new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (true){
                String command = scanner.nextLine();

                switch (command){



                }

            }
        });
        thread.setName("Console-Commands-Thread");
        thread.start();
        //Table table = new Table(6,5);

       // ArenaPlayer player = new ArenaPlayer();

       // table.addPlayer(player, new Position(2,0));
        //table.move(player, Direction.RIGHT);
        //System.out.println(table.toString());
    }


}
