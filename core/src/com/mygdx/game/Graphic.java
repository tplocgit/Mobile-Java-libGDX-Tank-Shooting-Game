package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class Graphic {
    public static final int TILE_SIZE = 64;
    public static final int NUMBER_OF_WIDTH_TILE = 40;
    public static final int NUMBER_OF_HEIGHT_TILE = 40;
    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas("images.atlas");
    public static final String SKIN_PATH = "skin/glassy/skin/glassy-ui.json";
}
