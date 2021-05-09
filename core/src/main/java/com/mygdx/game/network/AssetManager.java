package com.mygdx.game.network;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Graphic;
import gameservice.GameService;

public class AssetManager {

    public static AssetManager instance;
    private final TextureAtlas TEXTURE_ATLAS = new TextureAtlas("images.atlas");
    public final String SKIN_PATH = "skin\\commodore64\\skin\\uiskin.json";
//    public final TextureRegion[] PLAYER1_BULLET_TEXTURE_REGIONS = {
//            TEXTURE_ATLAS.findRegion("bulletBlue2_left"),
//            TEXTURE_ATLAS.findRegion("bulletBlue2_right"),
//            TEXTURE_ATLAS.findRegion("bulletBlue2_up"),
//            TEXTURE_ATLAS.findRegion("bulletBlue2_down"),
//    };
    public final TextureRegion[] DEFAULT_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("bulletRed2_left"),
            TEXTURE_ATLAS.findRegion("bulletRed2_right"),
            TEXTURE_ATLAS.findRegion("bulletRed2_up"),
            TEXTURE_ATLAS.findRegion("bulletRed2_down"),
    };
    public final TextureRegion STAR_TEXTURE_REGION = new TextureRegion(new Texture("item/star.png"));

    public final Image UP_IMAGE = new Image(new Texture("Controller/up-arrow.png"));
    public final Image DOWN_IMAGE = new Image(new Texture("Controller/down-arrow.png"));
    public final Image LEFT__IMAGE = new Image(new Texture("Controller/left-arrow.png"));
    public final Image RIGHT_IMAGE = new Image(new Texture("Controller/right-arrow.png"));
    public final Image CROSSHAIR__IMAGE = new Image(new Texture("Controller/focus.png"));
    public final TextureRegion[] PLAYER1_TANK_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("tank_blue_left"),
            TEXTURE_ATLAS.findRegion("tank_blue_right"),
            TEXTURE_ATLAS.findRegion("tank_blue_up"),
            TEXTURE_ATLAS.findRegion("tank_blue_down"),
    };

    public final TextureRegion[] BIG_TANK_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("tank_huge_left"),
            TEXTURE_ATLAS.findRegion("tank_huge_right"),
            TEXTURE_ATLAS.findRegion("tank_huge_up"),
            TEXTURE_ATLAS.findRegion("tank_huge_down")
    };
    public final TextureRegion[] DEFAULT_TANK_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("tank_bigRed_left"),
            TEXTURE_ATLAS.findRegion("tank_bigRed_right"),
            TEXTURE_ATLAS.findRegion("tank_bigRed_up"),
            TEXTURE_ATLAS.findRegion("tank_bigRed_down"),
    };

    public AssetManager(){
        instance = this;
    }

    public static AssetManager getInstance(){
        if(instance == null){
            instance = new AssetManager();
        }
        return instance;
    }

    public TextureRegion getTextureFromID(GameService.Texture textureID){
        switch (textureID){
            case DEFAULT_TEXTURE_REGIONS_L: return DEFAULT_TEXTURE_REGIONS[0];
            case DEFAULT_TEXTURE_REGIONS_R: return DEFAULT_TEXTURE_REGIONS[1];
            case DEFAULT_TEXTURE_REGIONS_U: return DEFAULT_TEXTURE_REGIONS[2];
            case DEFAULT_TEXTURE_REGIONS_D: return DEFAULT_TEXTURE_REGIONS[3];
            case STAR_TEXTURE_REGION: return STAR_TEXTURE_REGION;
            case PLAYER1_TANK_TEXTURE_REGIONS_L: return PLAYER1_TANK_TEXTURE_REGIONS[0];
            case PLAYER1_TANK_TEXTURE_REGIONS_R: return PLAYER1_TANK_TEXTURE_REGIONS[1];
            case PLAYER1_TANK_TEXTURE_REGIONS_U: return PLAYER1_TANK_TEXTURE_REGIONS[2];
            case PLAYER1_TANK_TEXTURE_REGIONS_D: return PLAYER1_TANK_TEXTURE_REGIONS[3];
            case BIG_TANK_TEXTURE_REGIONS_L: return BIG_TANK_TEXTURE_REGIONS[0];
            case BIG_TANK_TEXTURE_REGIONS_R: return BIG_TANK_TEXTURE_REGIONS[1];
            case BIG_TANK_TEXTURE_REGIONS_U: return BIG_TANK_TEXTURE_REGIONS[2];
            case BIG_TANK_TEXTURE_REGIONS_D: return BIG_TANK_TEXTURE_REGIONS[3];
            case DEFAULT_TANK_TEXTURE_REGIONS_L: return DEFAULT_TEXTURE_REGIONS[0];
            case DEFAULT_TANK_TEXTURE_REGIONS_R: return DEFAULT_TEXTURE_REGIONS[1];
            case DEFAULT_TANK_TEXTURE_REGIONS_U: return DEFAULT_TEXTURE_REGIONS[2];
            case DEFAULT_TANK_TEXTURE_REGIONS_D: return DEFAULT_TEXTURE_REGIONS[3];
            default: return null;
        }
    }

    public GameService.Texture getIDFromTexture(TextureRegion textureRegion){
        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[0])) return GameService.Texture.DEFAULT_TEXTURE_REGIONS_L;
        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[1])) return GameService.Texture.DEFAULT_TEXTURE_REGIONS_R;
        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[2])) return GameService.Texture.DEFAULT_TEXTURE_REGIONS_U;
        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[3])) return GameService.Texture.DEFAULT_TEXTURE_REGIONS_D;

        if(textureRegion.equals(STAR_TEXTURE_REGION)) return GameService.Texture.STAR_TEXTURE_REGION;

        if(textureRegion.equals(PLAYER1_TANK_TEXTURE_REGIONS[0])) return GameService.Texture.PLAYER1_TANK_TEXTURE_REGIONS_L;
        if(textureRegion.equals(PLAYER1_TANK_TEXTURE_REGIONS[1])) return GameService.Texture.PLAYER1_TANK_TEXTURE_REGIONS_R;
        if(textureRegion.equals(PLAYER1_TANK_TEXTURE_REGIONS[2])) return GameService.Texture.PLAYER1_TANK_TEXTURE_REGIONS_U;
        if(textureRegion.equals(PLAYER1_TANK_TEXTURE_REGIONS[3])) return GameService.Texture.PLAYER1_TANK_TEXTURE_REGIONS_D;

        if(textureRegion.equals(BIG_TANK_TEXTURE_REGIONS[0])) return GameService.Texture.BIG_TANK_TEXTURE_REGIONS_L;
        if(textureRegion.equals(BIG_TANK_TEXTURE_REGIONS[1])) return GameService.Texture.BIG_TANK_TEXTURE_REGIONS_R;
        if(textureRegion.equals(BIG_TANK_TEXTURE_REGIONS[2])) return GameService.Texture.BIG_TANK_TEXTURE_REGIONS_U;
        if(textureRegion.equals(BIG_TANK_TEXTURE_REGIONS[3])) return GameService.Texture.BIG_TANK_TEXTURE_REGIONS_D;

        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[0])) return GameService.Texture.DEFAULT_TANK_TEXTURE_REGIONS_L;
        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[1])) return GameService.Texture.DEFAULT_TANK_TEXTURE_REGIONS_R;
        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[2])) return GameService.Texture.DEFAULT_TANK_TEXTURE_REGIONS_U;
        if(textureRegion.equals(DEFAULT_TEXTURE_REGIONS[3])) return GameService.Texture.DEFAULT_TANK_TEXTURE_REGIONS_D;

        return null;
    }
}
