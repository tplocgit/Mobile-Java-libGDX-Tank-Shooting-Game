package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.items.Item;

import  com.mygdx.game.objects.PlayerTank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameScreen  implements Screen {

    //texture map
    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas("images.atlas");
    public static final TextureRegion[] PLAYER1_BULLET_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("bulletBlue2_left"),
            TEXTURE_ATLAS.findRegion("bulletBlue2_right"),
            TEXTURE_ATLAS.findRegion("bulletBlue2_up"),
            TEXTURE_ATLAS.findRegion("bulletBlue2_down"),
    };

    private CopyOnWriteArrayList<GameObject> gameObjectList = new CopyOnWriteArrayList<>();

    protected OrthographicCamera camera;
    protected Viewport viewport;
    private SpriteBatch batch;
    static public boolean in_power_up_time = false;

    //world parameters
    public static final int OBJECTS_LAYER_INDEX = 2;

    public static final int TILE_SIZE = 64;
    public static final int NUMBER_OF_WIDTH_TILE = 40;
    public static final int NUMBER_OF_HEIGHT_TILE = 40;

    //player parameters
    public static final int PLAYER_HEIGHT = 64;
    public static final int PLAYER_WIDTH = 64;
    public static final int PLAYER_BULLET_WIDTH = 10;
    public static final int PLAYER_BULLET_HEIGHT = 15;
    public static final int PLAYER_BULLET_SPEED = TILE_SIZE * 7;
    public static final int PLAYER_INITIAL_BULLET_MAG = 3;
    public static final float PLAYER_TIME_BETWEEN_SHOT = 0.5f;
    public static final int PLAYER_FIREPOWER = 10;
    public static final float PLAYER_INITIAL_MOVEMENT_SPEED = 6.5f;
    public static final float PLAYER_INITIAL_POSITION_X = 128;
    public static final float PLAYER_INITIAL_POSITION_Y = 128;
    public static final int PLAYER_INITIAL_SHIELD = 50;
    public static final float PLAYER_HIT_BOX_WIDTH = 60;
    public static final float PLAYER_HIT_BOX_HEIGHT = 60;
    public static final int PLAYER_INITIAL_DIRECTION = Direction.RIGHT;
//    public static final Bullet PLAYER_BULLET_SAMPLE = new Bullet(
//            0, 0, PLAYER_BULLET_WIDTH, PLAYER_BULLET_HEIGHT,
//            PLAYER_BULLET_SPEED, Direction.UP , PLAYER1_BULLET_TEXTURE_REGIONS);


    //power up item
    public static boolean powerUP = false;
    protected ArrayList<Item> itemList = new ArrayList<>();


    //player object
    protected PlayerTank playerTank;

    public static float time_line = 0;

    public GameScreen(){
        batch = new SpriteBatch();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        time_line += delta;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(playerTank.getPosition().x, playerTank.getPosition().y, 0);
        batch.setProjectionMatrix(camera.combined);
        VirtualController.getInstance().resize(width, height);
    }

    public List<GameObject> getGameObjectList() {
        return gameObjectList;
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }


}
