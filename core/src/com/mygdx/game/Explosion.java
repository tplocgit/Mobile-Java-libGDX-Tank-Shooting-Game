package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion extends GameObject {
    TextureRegion textureRegion;

    public Explosion(float xPos, float yPos, float width, float height,
                     TextureRegion textureRegion) {
        this.setX(xPos);
        this.setY(yPos);
        this.setWidth(width);
        this.setHeight(height);
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion, this.getX(), this.getY(),
                this.getWidth(), this.getHeight());
    }
}
