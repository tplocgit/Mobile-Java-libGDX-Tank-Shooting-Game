package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerTank extends Tank {
    public PlayerTank(float xPos, float yPos, float width, float height, float movementSpeed,
                      int firepower, int shield, float bulletWidth, float bulletHeight,
                      float bulletMovementSpeed, float timeBetweenShots, int direction,
                      TextureRegion tankTexture, TextureRegion bulletTextureRegion,
                      TextureRegion shieldTextureRegion) {
        super(xPos, yPos, width, height, movementSpeed, firepower, shield, bulletWidth, bulletHeight,
                bulletMovementSpeed, timeBetweenShots, direction, tankTexture, bulletTextureRegion,
                shieldTextureRegion);
    }

    @Override
    public void moveLeft(float deltaTime) {
        super.moveLeft(deltaTime);
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_blue_left");
    }

    @Override
    public void moveRight(float deltaTime) {
        super.moveRight(deltaTime);
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_blue_right");
    }

    @Override
    public void moveUp(float deltaTime) {
        super.moveUp(deltaTime);
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_blue_up");
    }

    @Override
    public void moveDown(float deltaTime) {
        super.moveDown(deltaTime);
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_blue_down");
    }

    @Override
    public Bullet[] fireBullet(TextureRegion bulletTextureRegion) {
        // calc bullet position later
        Bullet[] bullets = new Bullet[1];
        bullets[0] = new Bullet(this.getX() + this.getWidth() * 0.43f,
                this.getY() + this.getHeight() + this.getHeight() * 0.04f,
                bulletWidth, bulletHeight,
                bulletMovementSpeed, -1, bulletTextureRegion);

        switch (this.direction) {
            case Direction.UP:
                bullets[0].setX(this.getX() + this.getWidth() * 0.43f);
                bullets[0].setY(this.getY() + this.getHeight() + this.getHeight() * 0.04f);
                bullets[0].direction = Direction.UP;
                bullets[0].setWidth(bulletWidth);
                bullets[0].setHeight(bulletHeight);
                break;
            case Direction.DOWN:
                bullets[0].setX(this.getX() + this.getWidth() * 0.43f);
                bullets[0].setY(this.getY() - this.getHeight() * 0.2f);
                bullets[0].direction = Direction.DOWN;
                bullets[0].setWidth(bulletWidth);
                bullets[0].setHeight(bulletHeight);
                break;
            case Direction.LEFT:
                bullets[0].setX(this.getX() - this.getWidth() * 0.2f);
                bullets[0].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.02f);
                bullets[0].direction = Direction.LEFT;
                bullets[0].setWidth(bulletHeight);
                bullets[0].setHeight(bulletWidth);
                break;
            case Direction.RIGHT:
                bullets[0].setX(this.getX() + this.getWidth() + this.getWidth() * 0.02f);
                bullets[0].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.02f);
                bullets[0].direction = Direction.RIGHT;
                bullets[0].setWidth(bulletHeight);
                bullets[0].setHeight(bulletWidth);
                break;
        }

        timeSinceLastShot = 0;
        return bullets;
    }
}
