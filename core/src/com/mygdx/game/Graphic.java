package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class Graphic {
    public static final int TILE_SIZE = 64;
    public static final int NUMBER_OF_WIDTH_TILE = 40;
    public static final int NUMBER_OF_HEIGHT_TILE = 40;
    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas("images.atlas");
    public static final String SKIN_PATH = "skin\\commodore64\\skin\\uiskin.json";
    public static final int BUTTON_DEFAULT_PAD_VERTICAL = 20;
    public static final int BUTTON_DEFAULT_PAD_HORIZONTAL = 110;
    public static final int TABLE_ROW_DEFAULT_PAD_VERTICAL = 30;
    public static final float LABEL_TITLE_FONT_SCALE = 8;
    public static final int LABEL_TITLE_PAD_BOTTOM = 50;
    public static final float BUTTON_LABEL_FONT_SCALE = 4;
    public static final float TEXT_FIELD_LABEL_FONT_SCALE = 2.2f;
    public static final float TEXT_FIELD_WIDTH = 1000;
    public static final float TEXT_FIELD_HEIGHT = 100;
    public static final float TEXT_FIELD_PAD = 50;
}
