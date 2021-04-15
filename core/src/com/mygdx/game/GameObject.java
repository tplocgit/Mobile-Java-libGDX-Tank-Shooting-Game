package com.mygdx.game;


import com.badlogic.gdx.math.Rectangle;

public class GameObject extends Rectangle {
    public void rotate90Deg() {
        this.setWidth(this.getHeight());
        this.setHeight(this.getWidth());
    }
}
