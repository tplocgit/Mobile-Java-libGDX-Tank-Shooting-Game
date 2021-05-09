package com.mygdx.game.network;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.*;
import com.mygdx.game.objects.PlayerTank;

import java.util.ArrayList;

public class PvPScreen extends GameScreen {
    private static PvPScreen instance;
    //graphic

    // other stuff
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private BitmapFont font;
    private HUD my_hud;
    private int score = 0, enemyCount = 0;

    private ArrayList<Vector2> spawnPos;

    public PvPScreen(){

        GameObject.ClearObjectList();
        if(GameServer.getInstance() != null) {
            GameServer.getInstance().startServer();
        }

        instance = this;

        playerTank = new PlayerTank(AssetManager.getInstance().PLAYER1_TANK_TEXTURE_REGIONS,
                new Vector2(PLAYER_INITIAL_POSITION_X, PLAYER_INITIAL_POSITION_Y));

        camera = new OrthographicCamera();
        viewport = new FitViewport(20 * TILE_SIZE, 10 * TILE_SIZE, camera);
        map = new TmxMapLoader().load("beta_01.tmx");
        renderer = new OrthogonalTiledMapRenderer(this.map);

        //backgroundOffset = 0;
        font = new BitmapFont();
        font.setColor(Color.GRAY);
        font.getData().setScale(5);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //set up game objects

        batch = new SpriteBatch();
        my_hud = new HUD(this);

        //position to spawn enemies
        spawnPos = new ArrayList<>();
        spawnPos.add(new Vector2(700, 450));
        spawnPos.add(new Vector2(2240, 820));
        spawnPos.add(new Vector2(1400, 2048));
        spawnPos.add(new Vector2(624, 2122));
        spawnPos.add(new Vector2(225, 1520));
    }

    public static PvPScreen getInstance(){
        return instance;
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(playerTank.getPosition().x, playerTank.getPosition().y, 0);
        batch.setProjectionMatrix(camera.combined);
        VirtualController.getInstance().resize(width, height);
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
        renderer.dispose();
        map.dispose();
        font.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(GameServer.getInstance() != null && GameServer.getInstance().isStarted()){
            GameServer.getInstance().update(this);
        }

        Gdx.gl.glClearColor(0 ,0, 0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.render();

        detectInput();

        for(GameObject ob : GameObject.gameObjectList){
            ob.draw(batch);
        }

        batch.end();

        VirtualController.getInstance().draw();
        my_hud.update();
        my_hud.draw();
    }

    public void detectInput() {

        if (VirtualController.getInstance().isCrossHairPressed()) {
            GameClient.getInstance().sendFireCommand();
        }
        //keyboard input -- if move button press then  velocity != 0 --> move
        if (VirtualController.getInstance().isLeftPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.LEFT);
        }

        if (VirtualController.getInstance().isRightPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.RIGHT);
        }

        if (VirtualController.getInstance().isUpPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.UP);
        }

        if (VirtualController.getInstance().isDownPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.DOWN);
        }

        if (!VirtualController.getInstance().isLeftPressed()
                && !VirtualController.getInstance().isRightPressed()
                && !VirtualController.getInstance().isDownPressed()
                && !VirtualController.getInstance().isUpPressed()
        ) {
            GameClient.getInstance().sendDirectionCommands(Direction.NONE);
        }
    }
}
