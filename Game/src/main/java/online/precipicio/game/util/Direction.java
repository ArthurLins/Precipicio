package online.precipicio.game.util;

public enum Direction {

    NOTHING(0),
    UP(1),
    LEFT(3),
    RIGHT(4),
    DOWN(2);

    int val;

    Direction(int val){
        this.val = val;
    }

    public int getValue(){
        return val;
    }

    public static Direction getDirectionByValue(int val){
        switch (val){
            case 1:
                return UP;
            case 2:
                return DOWN;
            case 3:
                return LEFT;
            case 4:
                return RIGHT;
        }
        return NOTHING;
    }

}
