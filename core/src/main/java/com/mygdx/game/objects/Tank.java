package com.mygdx.game.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.*;

import gameservice.GameService;


public class Tank extends GameObject {
    private int bulletMag;
    private long tankId = 0;
    private float timeBetweenShots;
    private float timeSinceLastShot = 0;
    private float life;
    private int firepower;
    private float shield;
    private boolean isFire = false;
    private boolean canFire = true;
    private int direction;
    private int score = 100;
    private int gainedScore = 0;
    private TextureRegion[] tankTextureRegions;
    private float baseSpeed = 0;

    private float boostedSpeed = 0;
    private float lastBoostedSpeedTime = 0;

    private Sound shootingSound;
    private Sound destroySound;

    public Tank(TextureRegion[] textureRegions, Vector2 position){
        super();
        setTankTextureRegions(textureRegions);
        setImage(textureRegions[direction]);
        setPosition(position);
        setCollidable(true);
        setBlockable(true);
        shootingSound = Gdx.audio.newSound(Gdx.files.internal("explosionCrunch_000.ogg"));
        destroySound = Gdx.audio.newSound(Gdx.files.internal("explosionCrunch_001.ogg")); //put this here since it take a while to create this
    }

    public long getTankId() {
        return tankId;
    }

    public void setTankId(long tankId) {
        this.tankId = tankId;
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

    public float getShield() {
        return shield;
    }

    public void setShield(float shield) {
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

    public int getGainedScore() {
        return gainedScore;
    }

    public void setGainedScore(int gainedScore) {
        this.gainedScore = gainedScore;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
        switch (direction){
            case Direction.UP :
                this.setVelocity(new Vector2(0, 1));
                break;
            case Direction.DOWN :
                this.setVelocity(new Vector2(0, -1));
                break;
            case Direction.LEFT :
                this.setVelocity(new Vector2(-1, 0));
                break;
            case Direction.RIGHT :
                this.setVelocity(new Vector2(1, 0));
                break;
        }
        setImage(tankTextureRegions[direction]);
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
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
        super.update();
        if(GameScreen.time_line - timeSinceLastShot >= timeBetweenShots){
            canFire = true;
        }
        if(boostedSpeed != 0 && GameScreen.time_line - lastBoostedSpeedTime >= 4){
            setSpeed(baseSpeed);
        }
    }

    public void fireBullet(TextureRegion[] textureRegions, boolean isAI) {
        if (canFire) {
            Bullet bullet = new Bullet(textureRegions[direction],
                    this, new Vector2(getPosition()), new Vector2(0,0),
                    10, isAI, destroySound);
            if(direction == Direction.UP){
                bullet.setVelocity(new Vector2(0, 1));
            }else if(direction == Direction.DOWN){
                bullet.setVelocity(new Vector2(0, -1));
            }else if(direction == Direction.LEFT){
                bullet.setVelocity(new Vector2(-1, 0));
            }else if(direction == Direction.RIGHT){
                bullet.setVelocity(new Vector2(1, 0));
            }

            long shootingSoundId = shootingSound.play(0.2f);
            shootingSound.setPitch(shootingSoundId, 1.5f);
            shootingSound.setLooping(shootingSoundId, false);
            //shootingSound.dispose();

            canFire = false;
            timeSinceLastShot = GameScreen.time_line;
        }
    }

    public void setBoostedSpeed(float boostedSpeed){
        this.boostedSpeed = boostedSpeed;
        setSpeed(baseSpeed + boostedSpeed);
        lastBoostedSpeedTime = GameScreen.time_line;
    }

    @Override
    public GameService.GameObject getServiceObject(){
        GameService.GameObject.Builder gameObjectBuilder = GameService.GameObject.newBuilder()
                .setPosition(GameService.Vector2.newBuilder()
                        .setX(getPosition().x)
                        .setY(getPosition().y))
                .setScale(GameService.Vector2.newBuilder()
                        .setX(getScale().x)
                        .setY(getScale().y))
                .setOrigin(GameService.Vector2.newBuilder()
                        .setX(getOrigin().x)
                        .setY(getOrigin().y))
                .setVelocity(GameService.Vector2.newBuilder()
                        .setX(getVelocity().x)
                        .setY(getVelocity().y))
                .setSpeed(getSpeed())
                .setTankData(GameService.TankData.newBuilder()
                        .setTankID(tankId)
                        .setLife(getLife())
                        .setShield(getShield())
                        .setDirection(getDirection())
                        .setScore(gainedScore)
                        .setEnemyCount(HUD.getInstance().getEnemyCount())
                );

        if(getTextureID() != null) {
            gameObjectBuilder.setTexture(getTextureID());
        }

        return gameObjectBuilder.build();
    }

}
