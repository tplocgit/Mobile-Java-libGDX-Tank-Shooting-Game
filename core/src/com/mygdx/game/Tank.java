package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Tank extends GameObject {

    //characteristics
    protected float movementSpeed;
    Rectangle hitBox;
    //projectile information
    protected float bulletWidth, bulletHeight;
    protected float bulletMovementSpeed;
    protected float timeBetweenShots;
    protected float timeSinceLastShot = 0;
    protected int direction;
    protected float life;
    protected int firepower;
    protected int shield;
    protected int HIT_BOX_WIDTH = 60;
    protected int HIT_BOX_HEIGHT = 60;


    //graphic
    public static final TextureRegion[] DEFAULT_TANK_TEXTURE_REGIONS = {
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_left"),
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_right"),
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_up"),
            GameScreen.TEXTURE_ATLAS.findRegion("tank_bigRed_down"),
    };

    public static final TextureRegion[] DEFAULT_BULLET_TEXTURE_REGIONS = {
            GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_left"),
            GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_right"),
            GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_up"),
            GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_down"),
    };

    protected TextureRegion currentTankTextureRegion, currentBulletTextureRegion, shieldTextureRegion;
    protected TextureRegion[] tankTextureRegions;
    protected TextureRegion[] bulletTextureRegions;

    public Tank(float xPos, float yPos, float width, float height, float movementSpeed, int firepower,
                int shield, float bulletWidth, float bulletHeight,
                float bulletMovementSpeed, float timeBetweenShots, int direction,
                TextureRegion[] tankTextureRegions, TextureRegion initTankTextureRegion,
                TextureRegion[] bulletTextureRegions, TextureRegion initBulletTextureRegion,
                TextureRegion shieldTextureRegion) {
        super(xPos, yPos, width, height);
        this.tankTextureRegions = tankTextureRegions;
        this.bulletTextureRegions = bulletTextureRegions;
        this.movementSpeed = movementSpeed;
        this.firepower = firepower;
        this.shield = shield;
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;
        this.bulletMovementSpeed = bulletMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.direction = direction;
        this.currentTankTextureRegion = initTankTextureRegion;
        this.currentBulletTextureRegion = initBulletTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        updateHitBox();
    }

    public void update(float deltaTime) {

        timeSinceLastShot += deltaTime;

        this.updateHitBox();

        if(Direction.isValidDirection(this.direction)) {

            if (this.tankTextureRegions[this.direction] != null)
                this.currentTankTextureRegion = this.tankTextureRegions[this.direction];

            if (this.bulletTextureRegions[this.direction] != null)
                this.currentBulletTextureRegion = this.bulletTextureRegions[this.direction];

        }
    }

    public boolean canFire() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    public abstract Bullet[] fireBullet(TextureRegion bulletTextureRegion);

    public boolean collides(Rectangle rectangle) {
        return hitBox.overlaps(rectangle);
    }

    public void draw(Batch batch) {
        batch.draw(currentTankTextureRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if (shield > 0)
            batch.draw(shieldTextureRegion, this.getX() - 12, this.getY() - 12, this.getWidth() + 25, this.getHeight() + 25);
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
        this.hitBox = new Rectangle(this.getX() + (this.getWidth() - this.HIT_BOX_WIDTH) / 2f, this.getY() + (this.getHeight() - this.HIT_BOX_HEIGHT) / 2f,
                this.HIT_BOX_WIDTH, this.HIT_BOX_HEIGHT);
    }

    public Rectangle getHitbox() {
        return new Rectangle(this.hitBox);
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
