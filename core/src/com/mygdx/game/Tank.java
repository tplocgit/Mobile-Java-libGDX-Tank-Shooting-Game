package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tank extends GameObject implements Movable {
    public static final TextureRegion[] DEFAULT_TANK_TEXTURE_REGIONS = {
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_left"),
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_right"),
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_up"),
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_down"),
    };


    // Graphic
    public static final TextureRegion SHIELD_TEXTURE_REGION = new TextureRegion(new Texture("Shield/shieldBlue.png"));
    protected TextureRegion currentTankTextureRegion, currentBulletTextureRegion;
    protected TextureRegion[] tankTextureRegions;
    protected Bullet sampleBullet;

    // Game Play
    protected int bulletMag;
    protected float movementSpeed;
    protected float timeBetweenShots;
    protected float timeSinceLastShot = 0;
    protected int direction;
    protected float life;
    protected int firepower;
    protected int shield;


    public Tank(float xPos, float yPos, float textureWidth, float textureHeight,
                float hbWidth, float hbHeight, int bulletMag,
                float movementSpeed, int firepower, int shield, float timeBetweenShots,
                int direction, Bullet sampleBullet,
                TextureRegion[] tankTextureRegions) {

        super(xPos, yPos, textureWidth, textureHeight);

        // Graphic
        this.tankTextureRegions = tankTextureRegions;
        this.direction = direction;
        this.currentTankTextureRegion = this.tankTextureRegions[this.direction];
        this.sampleBullet = sampleBullet;

        // Game Play
        this.bulletMag = bulletMag;
        this.hitBox = GameObject.calculateHitBox(this, hbWidth, hbHeight);
        this.movementSpeed = movementSpeed;
        this.firepower = firepower;
        this.shield = shield;
        this.timeBetweenShots = timeBetweenShots;
    }

    public void update(float deltaTime) {

        timeSinceLastShot += deltaTime;

        this.updateHitBox();

        if(Direction.validateDirection(this.direction)) {

            if (this.tankTextureRegions[this.direction] != null)
                this.currentTankTextureRegion = this.tankTextureRegions[this.direction];
        }
    }

    public boolean canFire() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    public LinkedList<Bullet> fireBullet() {
        if(this.bulletMag < 1 || this.bulletMag > 3) return null;

        // calc bullet position later
        LinkedList<Bullet> bullets = new LinkedList<>();

        Vector2[] bulletPositions = getNextGeneratedBulletPosition();

        timeSinceLastShot = 0;

        for(Vector2 position : bulletPositions)
            bullets.add(new Bullet(position, this.direction, this.sampleBullet));

        switch (this.bulletMag) {
            case 1:
                bullets.remove(bullets.get(0));
                bullets.remove(bullets.get(2));
                return bullets;
            case 2:
                bullets.remove(bullets.get(1));
                return bullets;
            default:
                return bullets;
        }
    }

    public Vector2[] getNextGeneratedBulletPosition() {
        Vector2[] bulletPositions = new Vector2[3];
        Vector2 bulletLeftPosition = new Vector2();
        Vector2 bulletRightPosition = new Vector2();
        Vector2 bulletCenterPosition = new Vector2();
        float bulletDistance = 5 + this.sampleBullet.getWidth();

        switch (this.direction) {
            case Direction.UP:
                bulletCenterPosition.y = this.getY() + this.getHeight();
                bulletCenterPosition.x = this.getX() + (this.getWidth() - this.sampleBullet.getWidth()) / 2f;
                bulletLeftPosition.y = bulletRightPosition.y = bulletCenterPosition.y - this.sampleBullet.getHeight() / 3f;
                bulletLeftPosition.x = bulletCenterPosition.x - bulletDistance;
                bulletRightPosition.x = bulletCenterPosition.x + bulletDistance;
                break;
            case Direction.DOWN:
                bulletCenterPosition.y = this.getY();
                bulletCenterPosition.x = this.getX() + (this.getWidth() - this.sampleBullet.getWidth()) / 2f;
                bulletLeftPosition.y = bulletRightPosition.y = bulletCenterPosition.y + this.sampleBullet.getHeight() / 3f;
                bulletLeftPosition.x = bulletCenterPosition.x - bulletDistance;
                bulletLeftPosition.x = bulletCenterPosition.x - bulletDistance;
                bulletRightPosition.x = bulletCenterPosition.x + bulletDistance;
                break;
            case Direction.LEFT:
                bulletCenterPosition.x = this.getX();
                bulletCenterPosition.y = this.getY() + (this. getHeight() - this.sampleBullet.getWidth()) / 2f;
                bulletLeftPosition.x = bulletRightPosition.x = bulletCenterPosition.x + this.sampleBullet.getWidth() / 3f;
                bulletLeftPosition.y = bulletCenterPosition.y - bulletDistance;
                bulletRightPosition.y = bulletCenterPosition.y + bulletDistance;
                break;
            case Direction.RIGHT:
                bulletCenterPosition.x = this.getX() + this.getWidth();
                bulletCenterPosition.y = this.getY() + (this. getHeight() - this.sampleBullet.getWidth()) / 2f;
                bulletLeftPosition.x = bulletRightPosition.x = bulletCenterPosition.x - this.sampleBullet.getWidth() / 3f;
                bulletLeftPosition.y = bulletCenterPosition.y + bulletDistance;
                bulletRightPosition.y = bulletCenterPosition.y + bulletDistance;
                break;
        }

        bulletPositions[0] = bulletLeftPosition;
        bulletPositions[1] = bulletCenterPosition;
        bulletPositions[2] = bulletRightPosition;

        return bulletPositions;
    }

    public boolean isColliding(Rectangle rectangle) {
        return hitBox.overlaps(rectangle);
    }

    public void draw(Batch batch) {
        batch.draw(currentTankTextureRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if (shield > 0)
            batch.draw(Tank.SHIELD_TEXTURE_REGION, this.getX() - 12, this.getY() - 12, this.getWidth() + 25, this.getHeight() + 25);
    }
    
    public void moveLeft(float deltaTime) {
        this.direction = Direction.LEFT;
        this.setX(this.getX() - this.movementSpeed * deltaTime);
    }

    public void moveRight(float deltaTime) {
        this.direction = Direction.RIGHT;
        this.setX(this.getX() + this.movementSpeed * deltaTime);
    }

    public void moveUp(float deltaTime) {
        this.direction = Direction.UP;
        this.setY(this.getY() + this.movementSpeed * deltaTime);
    }

    public void moveDown(float deltaTime) {
        this.direction = Direction.DOWN;
        this.setY(this.getY() - this.movementSpeed * deltaTime);
    }

    @Override
    public void move(float deltaTime) {
        switch (this.direction) {
            case Direction.LEFT: this.moveLeft(deltaTime); break;
            case Direction.RIGHT: this.moveRight(deltaTime); break;
            case Direction.UP: this.moveUp(deltaTime); break;
            case Direction.DOWN: this.moveDown(deltaTime); break;
            default: break;
        }        
    }

    protected void updateHitBox() {
        this.hitBox = GameObject.calculateHitBox(this, this.hitBox.getWidth(), this.hitBox.getHeight());
    }

    public Rectangle getHitbox() {
        return new Rectangle(this.hitBox);
    }

    public Bullet getSampleBullet() {
        return new Bullet(sampleBullet);
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public float getTimeBetweenShots() {
        return timeBetweenShots;
    }

    public int getDirection() {
        return direction;
    }

    public float getLife() {
        return life;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getShield() {
        return shield;
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
            case Direction.LEFT: return this.getNextLeftHitBox(dt);
            case Direction.RIGHT: return this.getNextRightHitBox(dt);
            case Direction.UP: return this.getNextUpHitBox(dt);
            case Direction.DOWN: return this.getNextDownHitBox(dt);
            default: return null;
        }
    }
}
