package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Tank extends GameObject {

    //characteristics
    float movementSpeed;
    Rectangle hitBox;
    //projectile information
    float bulletWidth, bulletHeight;
    float bulletMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;
    int direction;
    int life;
    int HIT_BOX_WIDTH = 60;
    int HIT_BOX_HEIGHT = 60;
    public final static int DIR_LEFT = 0;
    public final static int DIR_RIGHT = 1;
    public final static int DIR_UP = 2;
    public final static int DIR_DOWN = 3;
    //graphic
    TextureRegion tankTexture, bulletTextureRegion;

    public Tank(float xPos, float yPos, float width, float height, float movementSpeed,
                float bulletWidth, float bulletHeight,
                float bulletMovementSpeed, float timeBetweenShots, int direction,
                TextureRegion tankTexture, TextureRegion bulletTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.setX(xPos);
        this.setY(yPos);
        this.setWidth(width);
        this.setHeight(height);
        this.hitBox = calcHitBox(HIT_BOX_WIDTH, HIT_BOX_HEIGHT);
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;
        this.bulletMovementSpeed = bulletMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.direction = direction;
        this.tankTexture = tankTexture;
        this.bulletTextureRegion = bulletTextureRegion;
    }

    public void update(float deltaTime) {
        hitBox = calcHitBox(HIT_BOX_WIDTH, HIT_BOX_HEIGHT);
        timeSinceLastShot += deltaTime;
    }

    public boolean canFire() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    public abstract Bullet[] fireBullet(TextureRegion bulletTextureRegion);

    public boolean collides(Rectangle rectangle) {
        return hitBox.overlaps(rectangle);
    }

    public void draw(Batch batch) {
        batch.draw(tankTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
    
    public void moveLeft(float deltaTime) {
        this.setX(this.getX() - this.movementSpeed * deltaTime);
    }

    public void moveRight(float deltaTime) {
        this.setX(this.getX() + this.movementSpeed * deltaTime);
    }

    public void moveUp(float deltaTime) {
        this.setY(this.getY() + this.movementSpeed * deltaTime);
    }

    public void moveDown(float deltaTime) {
        this.setY(this.getY() - this.movementSpeed * deltaTime);
    }

    public void move(float deltaTime) {
        switch (this.direction) {
            case DIR_LEFT: this.moveLeft(deltaTime); break;
            case DIR_RIGHT: this.moveRight(deltaTime); break;
            case DIR_UP: this.moveUp(deltaTime); break;
            case DIR_DOWN: this.moveDown(deltaTime); break;
            default: break;
        }        
    }

    public Rectangle calcHitBox(float hbWidth, float hbHeight) {
        return new Rectangle(this.getX() + (this.getWidth() - hbWidth) / 2f, this.getY() + (this.getHeight() - hbHeight) / 2f,
                hbWidth, hbHeight);
    }


    public Rectangle getNextLeftHitBox(float dt) {
        return new Rectangle(this.hitBox.x - this.movementSpeed * dt, this.hitBox.y, this.hitBox.width, this.hitBox.height);
    }

    public Rectangle getNextRightHitBox(float dt) {
        return new Rectangle(this.hitBox.x + this.movementSpeed * dt, this.hitBox.y, this.hitBox.width, this.hitBox.height);
    }

    public Rectangle getNextUpHitBox(float dt) {
        return new Rectangle(this.hitBox.x, this.hitBox.y + this.movementSpeed * dt, this.hitBox.width, this.hitBox.height);
    }

    public Rectangle getNextDownHitBox(float dt) {
        return new Rectangle(this.hitBox.x, this.hitBox.y - this.movementSpeed * dt, this.hitBox.width, this.hitBox.height);
    }

    public Rectangle getNextMoveHitBox(int nextMoveDirection, float dt) {
        switch (nextMoveDirection) {
            case DIR_LEFT: return this.getNextLeftHitBox(dt);
            case DIR_RIGHT: return this.getNextRightHitBox(dt);
            case DIR_UP: return this.getNextUpHitBox(dt);
            case DIR_DOWN: return this.getNextDownHitBox(dt);
            default: return null;
        }
    }
}
