package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.PvEScreen;
import com.mygdx.game.AssetManager;


public class TankAI extends  Tank {

    private float lastTimeDirChange = 0;

    public TankAI(TextureRegion[] textureRegions, Vector2 position) {
        super(textureRegions, position);
    }

    @Override
    public void update(){
        super.update();

        if(GameScreen.time_line - lastTimeDirChange >= 4 + PvEScreen.GENERATOR.nextInt(2)){
            int random_dir = PvEScreen.GENERATOR.nextInt(4);
            setDirection(random_dir);
            lastTimeDirChange = GameScreen.time_line;
        }

        if(GameScreen.time_line - getTimeSinceLastShot() >= getTimeBetweenShots()){
            fireBullet(AssetManager.getInstance().ENEMY_BULLET_TEXTURE_REGIONS, true);
            setTimeSinceLastShot(GameScreen.time_line);
        }

    }

}
