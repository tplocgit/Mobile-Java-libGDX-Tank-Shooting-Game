package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameObject;
import com.mygdx.game.Graphic;
import com.mygdx.game.PvEScreen;

public class Bullet extends GameObject {

    private float lifeTime = 3;
    private float timeSinceSpawn;

    public static final TextureRegion[] DEFAULT_TEXTURE_REGIONS = {
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_left"),
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_right"),
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_up"),
            Graphic.TEXTURE_ATLAS.findRegion("bulletRed2_down"),
    };

    private Tank ownerTank;


    public Bullet(TextureRegion textureRegion, Tank ownerTank, Vector2 position, Vector2 movement){
        super();
        setImage(textureRegion);
        setPosition(position);
        setSpeed(movement.len());
        setVelocity(movement);
        setCollidable(true);
        setBlockable(false);
        this.ownerTank = ownerTank;
        timeSinceSpawn = PvEScreen.time_line;
    }

    //bullet meet map (wall)
    @Override
    protected void onMapCollided(Rectangle mapRec) {
        GameObject.Destroy(this);
    }

    //bullet meet tank
    @Override
    protected void onCollided(GameObject gameObject){
        if(gameObject instanceof Tank ){
            Tank targetTank = (Tank)gameObject;
            if (!targetTank.equals(ownerTank)){
                GameObject.Destroy(this);
                if (targetTank.getShield() >= ownerTank.getFirepower()){
                    targetTank.setShield(targetTank.getShield() - ownerTank.getFirepower());
                }
                else {
                    targetTank.setLife(targetTank.getLife() + targetTank.getShield() - ownerTank.getFirepower());
                }

                if(targetTank.getLife() <= 0){
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
    public void update(){
        super.update();
        if(PvEScreen.time_line - timeSinceSpawn >= lifeTime){
            GameObject.Destroy(this);
        }
    }


}
