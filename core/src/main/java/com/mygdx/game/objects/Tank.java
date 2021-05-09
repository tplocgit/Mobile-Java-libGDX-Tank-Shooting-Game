package com.mygdx.game.objects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.GameObject;
import com.mygdx.game.GameScreen;
import com.mygdx.game.PvEScreen;
import com.mygdx.game.network.AssetManager;
import gameservice.GameService;


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
    private float baseSpeed = 0;

    private float boostedSpeed = 0;
    private float lastBoostedSpeedTime = 0;

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

    public void fireBullet() {
        if (canFire) {
            Bullet bullet = new Bullet(AssetManager.getInstance().DEFAULT_TEXTURE_REGIONS[direction],
                    this, new Vector2(getPosition()), new Vector2(0,0), 10);
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
                        .setLife(getLife())
                        .setShield(getShield())
                        .setDirection(getDirection())
                );

        if(getTextureID() != null) {
            gameObjectBuilder.setTexture(getTextureID());
        }

        return gameObjectBuilder.build();
    }

}
