package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class EnemyBigTank extends Tank {
    float timeSinceLastDirChange = 0;
    float dirChangeFreq = 1.0f;

    public EnemyBigTank(float xPos, float yPos, float width, float height, float movementSpeed,
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
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_huge_left");

    }

    @Override
    public void moveRight(float deltaTime) {
        super.moveRight(deltaTime);
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_huge_right");
    }

    @Override
    public void moveUp(float deltaTime) {
        super.moveUp(deltaTime);
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_huge_up");
    }

    @Override
    public void moveDown(float deltaTime) {
        super.moveDown(deltaTime);
        this.tankTextureRegion = GameScreen.TEXTURE_ATLAS.findRegion("tank_huge_down");
    }

    @Override
    public Bullet[] fireBullet(TextureRegion bulletTextureRegion) {
        // fix these later
        Bullet[] bullets = new Bullet[3];
        bullets[0] = new Bullet(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.18f,
                this.getY() + this.getHeight(), bulletWidth, bulletHeight,
                bulletMovementSpeed, -1, bulletTextureRegion);
        bullets[1] = new Bullet(this.getX() + (this.getWidth() / 2) + this.getWidth() * 0.08f,
                this.getY() + this.getHeight(), bulletWidth, bulletHeight,
                bulletMovementSpeed, -1, bulletTextureRegion);
        bullets[2] = new Bullet(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.04f,
                this.getY() + this.getHeight() / 2, bulletWidth, bulletHeight,
                bulletMovementSpeed, -1, bulletTextureRegion);

        switch (this.direction) {
            case Direction.UP:
                bullets[0].setX(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.18f);
                bullets[0].setY(this.getY() + this.getHeight());
                bullets[0].direction = Direction.UP;
                bullets[0].setWidth(bulletWidth);
                bullets[0].setHeight(bulletHeight);

                bullets[1].setX(this.getX() + (this.getWidth() / 2) + this.getWidth() * 0.08f);
                bullets[1].setY(this.getY() + this.getHeight());
                bullets[1].direction = Direction.UP;
                bullets[1].setWidth(bulletWidth);
                bullets[1].setHeight(bulletHeight);

                bullets[2].setX(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.04f);
                bullets[2].setY(this.getY() + this.getHeight() / 2);
                bullets[2].direction = Direction.UP;
                bullets[2].setWidth(bulletWidth);
                bullets[2].setHeight(bulletHeight);
                break;
            case Direction.DOWN:
                bullets[0].setX(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.18f);
                bullets[0].setY(this.getY() - this.getHeight() * 0.08f);
                bullets[0].direction = Direction.DOWN;
                bullets[0].setWidth(bulletWidth);
                bullets[0].setHeight(bulletHeight);

                bullets[1].setX(this.getX() + (this.getWidth() / 2) + this.getWidth() * 0.08f);
                bullets[1].setY(this.getY() - this.getHeight() * 0.08f);
                bullets[1].direction = Direction.DOWN;
                bullets[1].setWidth(bulletWidth);
                bullets[1].setHeight(bulletHeight);

                bullets[2].setX(this.getX() + (this.getWidth() / 2) - this.getWidth() * 0.04f);
                bullets[2].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.08f);
                bullets[2].direction = Direction.DOWN;
                bullets[2].setWidth(bulletWidth);
                bullets[2].setHeight(bulletHeight);
                break;
            case Direction.LEFT:
                bullets[0].setX(this.getX() - this.getWidth() * 0.05f);
                bullets[0].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.15f);
                bullets[0].direction = Direction.LEFT;
                bullets[0].setWidth(bulletHeight);
                bullets[0].setHeight(bulletWidth);

                bullets[1].setX(this.getX() - this.getWidth() * 0.05f);
                bullets[1].setY(this.getY() + this.getHeight() / 2 + this.getHeight() * 0.12f);
                bullets[1].direction = Direction.LEFT;
                bullets[1].setWidth(bulletHeight);
                bullets[1].setHeight(bulletWidth);

                bullets[2].setX(this.getX() + this.getWidth() / 2 - this.getWidth() * 0.1f);
                bullets[2].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.02f);
                bullets[2].direction = Direction.LEFT;
                bullets[2].setWidth(bulletHeight);
                bullets[2].setHeight(bulletWidth);
                break;
            case Direction.RIGHT:
                bullets[0].setX(this.getX() + this.getWidth());
                bullets[0].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.15f);
                bullets[0].direction = Direction.RIGHT;
                bullets[0].setWidth(bulletHeight);
                bullets[0].setHeight(bulletWidth);

                bullets[1].setX(this.getX() + this.getWidth());
                bullets[1].setY(this.getY() + this.getHeight() / 2 + this.getHeight() * 0.12f);
                bullets[1].direction = Direction.RIGHT;
                bullets[1].setWidth(bulletHeight);
                bullets[1].setHeight(bulletWidth);

                bullets[2].setX(this.getX() + this.getWidth() / 2);
                bullets[2].setY(this.getY() + this.getHeight() / 2 + this.getHeight() * 0.02f);
                bullets[2].direction = Direction.RIGHT;
                bullets[2].setWidth(bulletHeight);
                bullets[2].setHeight(bulletWidth);
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
            this.direction = new Random().nextInt(4);
            timeSinceLastDirChange -= dirChangeFreq;
        }
    }
}
