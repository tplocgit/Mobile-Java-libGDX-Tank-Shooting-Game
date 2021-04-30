package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.GameObject;
import com.mygdx.game.GameScreen;

import java.util.ArrayList;

public class Tank extends GameObject {
    private int bulletMag;
    private float timeBetweenShots;
    private float timeSinceLastShot = 0;
    private float life;
    private int firepower;
    private int shield;
    private boolean isFire = false;
    private boolean canFire = true;
    private int direction;
    private int score = 100;
    private TextureRegion[] tankTextureRegions;



    public Tank(TextureRegion[] textureRegions, Vector2 position){
        super();
        setTankTextureRegions(textureRegions);
        setImage(textureRegions[direction]);
        setPosition(position);
        setCollidable(true);
        setBlockable(true);
    }

    public int getBulletMag() {
        return bulletMag;
    }

    public void setBulletMag(int bulletMag) {
        this.bulletMag = bulletMag;
    }

    public float getTimeBetweenShots() {
        return timeBetweenShots;
    }

    public void setTimeBetweenShots(float timeBetweenShots) {
        this.timeBetweenShots = timeBetweenShots;
    }

    public float getTimeSinceLastShot() {
        return timeSinceLastShot;
    }

    public void setTimeSinceLastShot(float timeSinceLastShot) {
        this.timeSinceLastShot = timeSinceLastShot;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public int getFirepower() {
        return firepower;
    }

    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public boolean isFire() {
        return isFire;
    }

    public void setFire(boolean fire) {
        isFire = fire;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
        setImage(tankTextureRegions[direction]);
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    //-----------------------------------

    public TextureRegion[] getTankTextureRegions() {
        return tankTextureRegions;
    }

    public void setTankTextureRegions(TextureRegion[] tankTextureRegions) {
        this.tankTextureRegions = tankTextureRegions;
    }

    @Override
    public void update(){
        if(GameScreen.time_line - timeSinceLastShot >= timeBetweenShots){
            canFire = true;
        }

        super.update();
    }

    public void fireBullet() {
        if (canFire) {
            Bullet bullet = new Bullet(Bullet.DEFAULT_TEXTURE_REGIONS[direction], this, new Vector2(getPosition()), new Vector2(0,0));
            bullet.setSpeed(10);
            if(direction == Direction.UP){
                bullet.setVelocity(new Vector2(0, 1));
            }else if(direction == Direction.DOWN){
                bullet.setVelocity(new Vector2(0, -1));
            }else if(direction == Direction.LEFT){
                bullet.setVelocity(new Vector2(-1, 0));
            }else if(direction == Direction.RIGHT){
                bullet.setVelocity(new Vector2(1, 0));
            }

            canFire = false;
            timeSinceLastShot = GameScreen.time_line;
        }
    }



}
