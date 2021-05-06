package com.mygdx.game;

public abstract class Direction {
    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int UP = 2;
    public final static int DOWN = 3;

    public static boolean validateDirection(int direction) {
        return direction >= 0 && direction <= 3;
    }

    public static boolean isHorizontalDirection(int direction) {
        return direction == Direction.LEFT || direction == Direction.RIGHT;
    }

    public static boolean isVerticalDirection(int direction) {
        return direction == Direction.UP || direction == Direction.DOWN;
    }


    public static boolean isParallelDirections(int dir1, int dir2) {
        return isHorizontalDirection(dir1) == isHorizontalDirection(dir2);
    }
}
