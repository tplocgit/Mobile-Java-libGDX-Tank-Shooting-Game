package com.mygdx.game;

public class Direction {
    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int UP = 2;
    public final static int DOWN = 3;

    public static boolean isValidDirection(int direction) {
        return direction >= 0 && direction <= 3;
    }
}
