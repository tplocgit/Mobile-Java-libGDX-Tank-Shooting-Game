package com.mygdx.game;


import com.badlogic.gdx.math.Rectangle;

public class GameObject extends Rectangle {
    protected Rectangle hitBox;

    public GameObject(float xPos, float yPos, float width, float height) {
        super(xPos, yPos, width, height);
        this.hitBox = new Rectangle(this);
    }

    public GameObject(Rectangle hitBox) {
        super(hitBox);
        this.hitBox = new Rectangle(this);

    }

    public static Rectangle calculateHitBox(Rectangle texture, float hbWidth, float hbHeight) {
        float hbX, hbY;
        hbX = texture.getX() + (texture.getWidth() - hbWidth) / 2f;
        hbY = texture.getY() + (texture.getHeight() - hbHeight) / 2f;
        return new Rectangle(hbX, hbY, hbWidth, hbHeight);
    }

    public void rotateHitBox90Deg() {
        float prevWidth = this.hitBox.getWidth(), prevHeight = this.hitBox.getHeight();
        this.hitBox.setWidth(prevHeight);
        this.hitBox.setHeight(prevWidth);
    }
}
