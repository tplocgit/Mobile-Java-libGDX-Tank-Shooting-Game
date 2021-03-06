package com.mygdx.game.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.*;
import com.mygdx.game.AssetManager;
import gameservice.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PvPScreen extends GameScreen {
    private static PvPScreen instance;

    private CopyOnWriteArrayList<GameService.GameObject> netObjectList = new CopyOnWriteArrayList<>();

    // other stuff
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private BitmapFont font;
    private HUD my_hud;
    private int score = 0, enemyCount = 0;
    private PlayerNet playerNet;

    private ArrayList<Vector2> spawnPos;


    public PvPScreen(){

        GameObject.ClearObjectList();
        if(GameServer.getInstance() != null) {
            GameServer.getInstance().startServer();
        }

        instance = this;

        playerNet = new PlayerNet();

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

        // play music and stuff
        AssetManager.getInstance().backgroundMusic.setVolume(0.5f);
        AssetManager.getInstance().backgroundMusic.setLooping(true);
        AssetManager.getInstance().backgroundMusic.play();
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
        if(playerNet.getPlayerObject() != null) {
            camera.position.set(playerNet.getPlayerObject().getPosition().getX(), playerNet.getPlayerObject().getPosition().getY(), 0);
        }
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

        if(GameServer.getInstance() != null) {
            GameServer.getInstance().shutdownServer();
        }
    }

    private void drawNetObject(GameService.GameObject gameObject) {

        Sprite sprite = null;

        if(gameObject.getTexture() != GameService.Texture.TEXTURE_ATLAS) {
            sprite = new Sprite(AssetManager.getInstance().getTextureFromID(gameObject.getTexture()));
            if(gameObject.getSize().getX() != 0 && gameObject.getSize().getY() != 0){
                sprite.setSize(gameObject.getSize().getX(), gameObject.getSize().getY());
            }
        }

        if(sprite != null){
            sprite.setBounds(gameObject.getPosition().getX() - sprite.getWidth() / 2f, gameObject.getPosition().getY() - sprite.getHeight() / 2f,
                    sprite.getWidth(), sprite.getHeight());
            sprite.setScale(gameObject.getScale().getX(), gameObject.getScale().getY());
            sprite.draw(batch);
        }
    }

    public PlayerNet getPlayerNet() {
        return playerNet;
    }

    public CopyOnWriteArrayList<GameService.GameObject> getNetObjectList() {
        return netObjectList;
    }

    public void setNetObjectList(List<GameService.GameObject> netObjectList) {
        this.netObjectList.clear();
        this.netObjectList.addAll(netObjectList);
    }

    private int getEnemyCount(){
        int count = 0;
        for(GameService.GameObject go : netObjectList){
            if(go.getTankData() != null){
                count++;
            }
        }
        return count - 1;
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

        playerNet.detectInput();
//        System.out.println("got data: " + playerNet.getPlayerID());

        for(GameService.GameObject ob : netObjectList){
            if(ob.getTankData().getTankID() == playerNet.getPlayerID() && playerNet.getPlayerID() != 0) {
                playerNet.setPlayerObject(ob);
                camera.position.set(ob.getPosition().getX(), ob.getPosition().getY(), 0);
                if (ob != null) {
                    my_hud.setLife(ob.getTankData().getLife());
                    my_hud.setShield(ob.getTankData().getShield());
                    my_hud.setScore(ob.getTankData().getScore());
                    my_hud.setPosition(new Vector2(ob.getPosition().getX(), ob.getPosition().getY()));
                    my_hud.setEnemyCount(ob.getTankData().getEnemyCount());
                    my_hud.setFirepower(ob.getTankData().getFirePower());
                    my_hud.setMovementSpeed(ob.getSpeed());
                }
            }
            drawNetObject(ob);
        }

        batch.end();

        VirtualController.getInstance().draw();

        my_hud.draw();
    }
}
