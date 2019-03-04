package online.precipicio.game.util;

import java.util.ArrayList;
import java.util.List;

public class ArenaSpawnUtil {

    public static List<Position> getSpawnList(){
        List<Position> spawnMap = new ArrayList<>();
        spawnMap.add(new Position(0, 1));
        spawnMap.add(new Position(2, 0));
        spawnMap.add(new Position(2, 5));
        spawnMap.add(new Position(4, 1));
        spawnMap.add(new Position(4, 4));
        spawnMap.add(new Position(0, 4));
        return spawnMap;
    }
}
