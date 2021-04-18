package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends GameObject {
    //characteristics
    float movementSpeed;
    int direction;

    public static final int BULLET_WIDTH = 10;
    public static final int BULLET_HEIGHT = 15;

    //graphic
    TextureRegion textureRegion;

    public Bullet(float xPos, float yPos, float width, float height, float movementSpeed,
                  int direction, TextureRegion textureRegion) {
        super(xPos, yPos, width, height);
        this.movementSpeed = movementSpeed;
        this.direction = direction;
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion,
                this.getX() - BULLET_WIDTH / 2f + 4, this.getY() - BULLET_HEIGHT / 2f + 4,
                this.getWidth(), this.getHeight());
    }

    public Rectangle getHitBox() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
