package com.mygdx.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameObject;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Graphic;
import com.mygdx.game.objects.Tank;
import com.mygdx.game.objects.TankAI;

public class Star extends GameObject implements Item{
    public static final TextureRegion STAR_TEXTURE_REGION = new TextureRegion(
            new Texture("item/star.png"));

    public Star(TextureRegion textureRegion){
        super();
        setImage(textureRegion);
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
}
