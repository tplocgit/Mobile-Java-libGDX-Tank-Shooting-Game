package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Character {

    public Character(float movementSpeed, int hp, float xPosition, float yPosition, float width, float height, TextureRegion charTexture, TextureRegion hpTexture) {
        this.movementSpeed = movementSpeed;
        this.hp = hp;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.charTexture = charTexture;
        this.hpTexture = hpTexture;
    }

    //characteristics
    float movementSpeed; // unit/s
    int hp;

    //position, dimension
    float xPosition, yPosition;
    float width, height;

    //graphic
    TextureRegion charTexture, hpTexture;


}
