package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameObject;
import com.mygdx.game.PvEScreen;
import com.mygdx.game.objects.Tank;
import com.mygdx.game.objects.TankAI;


public class Star extends GameObject implements Item {
    private float lifeTime = 10;
    private float timeSinceSpawn;

    public static final TextureRegion STAR_TEXTURE_REGION = new TextureRegion(
            new Texture("item/star.png"));

    public Star(TextureRegion textureRegion){
        super();
        setImage(textureRegion);
        timeSinceSpawn = PvEScreen.time_line;
    }

    public Star(TextureRegion textureRegion, Vector2 position){
        setImage(textureRegion);
        setPosition(position);
    }

    @Override
    protected void onCollided(GameObject gameObject){
        if((gameObject instanceof Tank) && !(gameObject instanceof TankAI)){
            Tank targetTank = (Tank)gameObject;
            targetTank.setBoostedSpeed(5);
            GameObject.Destroy(this);
        }
    }

    @Override
    public void update() {
        super.update();
        if (PvEScreen.time_line - timeSinceSpawn >= lifeTime){
            GameObject.Destroy(this);
        }
    }
}
