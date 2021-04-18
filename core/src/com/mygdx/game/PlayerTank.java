package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerTank extends Tank {

    public PlayerTank(float xPos, float yPos, float width, float height, float movementSpeed, int firepower,
                      int shield, float bulletWidth, float bulletHeight,
                      float bulletMovementSpeed, float timeBetweenShots, int direction,
                      TextureRegion[] tankTextureRegions, TextureRegion initTankTextureRegion,
                      TextureRegion[] bulletTextureRegions, TextureRegion initBulletTextureRegion,
                      TextureRegion shieldTextureRegion) {

        super(xPos, yPos, width, height,
                movementSpeed, firepower, shield,
                bulletWidth, bulletHeight, bulletMovementSpeed, timeBetweenShots,
                direction,
                tankTextureRegions , initTankTextureRegion,
                bulletTextureRegions, initBulletTextureRegion,
                shieldTextureRegion);
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
                bullets[0].rotateHitBox90Deg();
                break;
            case Direction.DOWN:
                bullets[0].setX(this.getX() + this.getWidth() * 0.43f);
                bullets[0].setY(this.getY() - this.getHeight() * 0.2f);
                bullets[0].direction = Direction.DOWN;
                bullets[0].rotateHitBox90Deg();
                break;
            case Direction.LEFT:
                bullets[0].setX(this.getX() - this.getWidth() * 0.2f);
                bullets[0].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.02f);
                bullets[0].direction = Direction.LEFT;
                bullets[0].rotateHitBox90Deg();
                break;
            case Direction.RIGHT:
                bullets[0].setX(this.getX() + this.getWidth() + this.getWidth() * 0.02f);
                bullets[0].setY(this.getY() + this.getHeight() / 2 - this.getHeight() * 0.02f);
                bullets[0].direction = Direction.RIGHT;
                bullets[0].rotateHitBox90Deg();
                break;
        }

        timeSinceLastShot = 0;
        return bullets;
    }
}
