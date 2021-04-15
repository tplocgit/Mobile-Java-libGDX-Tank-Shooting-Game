package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class EnemyTank extends Tank {
    float timeSinceLastDirChange = 0;
    float dirChangeFreq = 1.0f;

    public EnemyTank(float xPos, float yPos, float width, float height, float movementSpeed,
                      float bulletWidth, float bulletHeight,
                      float bulletMovementSpeed, float timeBetweenShots, int direction,
                      TextureRegion tankTexture, TextureRegion bulletTextureRegion) {
        super(xPos, yPos, width, height, movementSpeed, bulletWidth, bulletHeight,
                bulletMovementSpeed, timeBetweenShots, direction, tankTexture, bulletTextureRegion);
    }

    @Override
    public void moveLeft(float deltaTime) {
        super.moveLeft(deltaTime);
        this.tankTexture = GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_left");

    }

    @Override
    public void moveRight(float deltaTime) {
        super.moveRight(deltaTime);
        this.tankTexture = GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_right");
    }

    @Override
    public void moveUp(float deltaTime) {
        super.moveUp(deltaTime);
        this.tankTexture = GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_up");
    }

    @Override
    public void moveDown(float deltaTime) {
        super.moveDown(deltaTime);
        this.tankTexture = GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_down");
    }

    @Override
    public Bullet[] fireBullet(TextureRegion bulletTextureRegion) {
        // fix these later
        Bullet[] bullets = new Bullet[2];
        bullets[0] = new Bullet(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.26f,
                this.getY() + this.getHeight() * 0.8f, bulletWidth, bulletHeight,
                bulletMovementSpeed, -1, bulletTextureRegion);
        bullets[1] = new Bullet(this.getX() + (this.getWidth() / 2) + this.getWidth() * 0.12f,
                this.getY() + this.getHeight() * 0.8f, bulletWidth, bulletHeight,
                bulletMovementSpeed, -1, bulletTextureRegion);

        switch (this.direction) {
            case Direction.UP:
                bullets[0].setX(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.26f);
                bullets[0].setY(this.getY() + this.getHeight() * 0.8f);
                bullets[0].direction = Direction.UP;

                bullets[1].setX(this.getX() + (this.getWidth() / 2) + this.getWidth() * 0.12f);
                bullets[1].setY(this.getY() + this.getHeight() * 0.8f);
                bullets[1].direction = Direction.UP;
                break;
            case Direction.DOWN:
                bullets[0].setX(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.26f);
                bullets[0].setY(this.getY() + this.getHeight() * 0.05f);
                bullets[0].direction = Direction.DOWN;

                bullets[1].setX(this.getX() + (this.getWidth() / 2) + this.getWidth() * 0.12f);
                bullets[1].setY(this.getY() + this.getHeight() * 0.05f);
                bullets[1].direction = Direction.DOWN;
                break;
            case Direction.LEFT:
                bullets[0].setX(this.getX() + this.getWidth() * 0.02f);
                bullets[0].setY(this.getY() + this.getHeight() / 2 + this.getHeight() * 0.16f);
                bullets[0].direction = Direction.LEFT;
                bullets[0].rotate90Deg();

                bullets[1].setX(this.getX() + this.getWidth() * 0.02f);
                bullets[1].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.22f);
                bullets[1].direction = Direction.LEFT;
                bullets[1].rotate90Deg();
                break;
            case Direction.RIGHT:
                bullets[0].setX(this.getX() + this.getWidth() - this.getWidth() * 0.2f);
                bullets[0].setY(this.getY() + this.getHeight() / 2 + this.getHeight() * 0.16f);
                bullets[0].direction = Direction.RIGHT;
                bullets[0].rotate90Deg();

                bullets[1].setX(this.getX() + this.getWidth() - this.getWidth() * 0.22f);
                bullets[1].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.22f);
                bullets[1].direction = Direction.RIGHT;
                bullets[1].rotate90Deg();
                break;
        }

        timeSinceLastShot = 0;
        return bullets;
    }

    @Override
    public void update (float deltaTime) {
        super.update(deltaTime);
        timeSinceLastDirChange += deltaTime;
        if (timeSinceLastDirChange > dirChangeFreq){
            direction = new Random().nextInt(4);
            timeSinceLastDirChange -= dirChangeFreq;
        }
    }
}
