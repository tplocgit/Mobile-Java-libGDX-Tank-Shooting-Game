package com.mygdx.game;


import com.badlogic.gdx.math.Rectangle;

public class GameObject extends Rectangle {
    public GameObject(float xPos, float yPos, float width, float height) {
        super(xPos, yPos, width, height);
    }

    public void rotateHitBox90Deg() {
        float prevWidth = this.getWidth(), prevHeight = this.getHeight();
        this.setWidth(prevHeight);
        this.setHeight(prevWidth);
    }
}
