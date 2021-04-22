package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends GameObject implements Movable {
    //characteristics
    private float movementSpeed;
    private int direction;

    public static final int BULLET_WIDTH = 10;
    public static final int BULLET_HEIGHT = 15;

    public static final TextureRegion[] DEFAULT_TEXTURE_REGIONS = {
            /*GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_left"),
            GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_right"),
            GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_up"),
            GameScreen.TEXTURE_ATLAS.findRegion("bulletRed2_down"),*/
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_left"),
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_right"),
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_up"),
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_down"),
    };

    //graphic
    protected TextureRegion[] bulletTextureRegions;
    private TextureRegion currentTextureRegion;


    public Bullet(float xPos, float yPos, float width, float height, float movementSpeed,
                  int direction, TextureRegion[] textureRegions) {
        super(xPos, yPos, width, height);
        this.movementSpeed = movementSpeed;
        this.direction = direction;
        this.bulletTextureRegions = textureRegions;
        this.currentTextureRegion = this.bulletTextureRegions[this.direction];
    }

    public Bullet(Bullet bullet) {
        super(bullet);
        this.movementSpeed = bullet.movementSpeed;
        this.direction = bullet.direction;
        this.currentTextureRegion = bullet.currentTextureRegion;
        this.bulletTextureRegions = bullet.bulletTextureRegions;
    }

    public Bullet(Vector2 position, int direction, Bullet bullet) {
        super(bullet);
        this.movementSpeed = bullet.movementSpeed;
        this.bulletTextureRegions = bullet.bulletTextureRegions;
        this.directionChangeUpdate(direction);
        this.setPosition(position);
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public int getDirection() {
        return direction;
    }

    public TextureRegion getCurrentTextureRegion() {
        return new TextureRegion(currentTextureRegion);
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setDirection(int direction) {
        this.directionChangeUpdate(direction);
    }

    public void setCurrentTextureRegion(TextureRegion currentTextureRegion) {
        this.currentTextureRegion = new TextureRegion(currentTextureRegion);
    }

        public void directionChangeUpdate(int changeDirection) {
            if(!Direction.validateDirection(changeDirection)) return;

            this.currentTextureRegion = this.bulletTextureRegions[changeDirection];

            if (!Direction.isParallelDirections(this.direction, changeDirection))
                this.rotateHitBox90Deg();

        this.direction = changeDirection;
    }

    public void draw(Batch batch) {
        float drawX = this.getX() - BULLET_WIDTH / 2f;
        float drawY = this.getY() - BULLET_HEIGHT / 2f;
        batch.draw(currentTextureRegion, drawX, drawY, this.getWidth(), this.getHeight());
    }

    public Rectangle getHitBox() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void move(float deltaTime) {
        switch (this.direction) {
            case Direction.UP:
                this.setY(this.getY() + this.movementSpeed * deltaTime);
                break;
            case Direction.DOWN:
                this.setY(this.getY() - this.movementSpeed * deltaTime);
                break;
            case Direction.LEFT:
                this.setX(this.getX() - this.movementSpeed * deltaTime);
                break;
            case Direction.RIGHT:
                this.setX(this.getX() + this.movementSpeed * deltaTime);
                break;
        }
    }
}
