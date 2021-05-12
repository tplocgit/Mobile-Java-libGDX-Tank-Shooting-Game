package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.*;

import java.security.acl.Owner;


public class Bullet extends GameObject {

    private float lifeTime = 3;
    private float timeSinceSpawn;

    private Tank ownerTank;
    boolean isAI = false;

    public Bullet(TextureRegion textureRegion, Tank ownerTank, Vector2 position, Vector2 movement, float speed, boolean isAI) {
        super();
        setImage(textureRegion);
        setPosition(position);
        setSpeed(speed);
        setVelocity(movement);
        setCollidable(true);
        setBlockable(false);
        this.ownerTank = ownerTank;
        timeSinceSpawn = GameScreen.time_line;
        this.isAI = isAI;
    }

    //bullet meet map (wall)
    @Override
    protected void onMapCollided(Rectangle mapRec) {
        GameObject.Destroy(this);
    }

    //bullet meet tank
    @Override
    protected void onCollided(GameObject gameObject) {
        if (gameObject instanceof Tank) {
            Tank targetTank = (Tank) gameObject;
            if (isAI && targetTank instanceof TankAI){
                return;
            }
            if (!targetTank.equals(ownerTank)) {
                GameObject.Destroy(this);
                if (targetTank.getShield() >= ownerTank.getFirepower()) {
                    targetTank.setShield(targetTank.getShield() - ownerTank.getFirepower());
                } else {
                    targetTank.setLife(targetTank.getLife() + targetTank.getShield() - ownerTank.getFirepower());
                }

                if (targetTank.getLife() <= 0) {
                    HUD.getInstance().setEnemyCount(HUD.getInstance().getEnemyCount() - 1);
                    ownerTank.setGainedScore(ownerTank.getGainedScore() + targetTank.getScore());
                    GameObject.Destroy(targetTank);
                }
            }
        }
    }

    public Tank getOwnerTank() {
        return ownerTank;
    }

    public void setOwnerTank(Tank ownerTank) {
        this.ownerTank = ownerTank;
    }

    @Override
    public void update() {
        super.update();
        if (GameScreen.time_line - timeSinceSpawn >= lifeTime) {
            GameObject.Destroy(this);
        }
    }
}