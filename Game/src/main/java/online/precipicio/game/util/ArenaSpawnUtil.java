package online.precipicio.game.util;

import java.util.ArrayList;
import java.util.List;

public class ArenaSpawnUtil {

    public static List<Position> getSpawnList(){
        List<Position> spawnMap = new ArrayList<>();

//        spawnMap.add(new Position(0, 0));
//        spawnMap.add(new Position(0, 1));

        spawnMap.add(new Position(1, 0));
        spawnMap.add(new Position(4, 0));
        spawnMap.add(new Position(0, 2));
        spawnMap.add(new Position(5, 2));
        spawnMap.add(new Position(1, 4));
        spawnMap.add(new Position(4, 4));
        return spawnMap;
    }
}
