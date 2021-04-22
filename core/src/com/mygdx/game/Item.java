package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Item extends GameObject{
    public static final TextureRegion STAR_TEXTURE_REGION = new TextureRegion(
            new Texture("item/star.png"));

    public Item() {
        super();
    }

    public void draw(Batch batch){
        batch.draw(STAR_TEXTURE_REGION, 5, 5, Graphic.TILE_SIZE, Graphic.TILE_SIZE);
    }
}
