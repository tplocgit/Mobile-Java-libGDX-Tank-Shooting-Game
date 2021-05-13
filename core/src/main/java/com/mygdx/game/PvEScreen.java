package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.items.Star;
import com.mygdx.game.network.AssetManager;
import com.mygdx.game.network.GameServer;
import com.mygdx.game.objects.PlayerTank;
import com.mygdx.game.objects.TankAI;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PvEScreen extends GameScreen {

    public static final Random GENERATOR = new Random();
    private String roomName = "default_room_name";
    //graphic
    //timing
    private float spawnTimers = 0;
    private float spawnerDownTime = 4;
    private int normalEnemyCounter = 0;
    private int bigEnemyThreshold = 6;
    private float timeToNextItemSpawn = 0;

    public static final int ENEMY_QUANTITY = 10;
    public static final int ENEMY_FIREPOWER = 10;
    public static final int   ENEMY_BULLET_SPEED = 8;
    public static final float ENEMY_TIME_BETWEEN_SHOT = 0.8f;

    // other stuff
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private MapLayer layer;
    private BitmapFont font;
    private HUD my_hud;
    private int score = 0, enemyCount = 0;

    private ArrayList<Vector2> spawnPos;

    // music stuff
    private Music backgroundMusic;

    PvEScreen() {
        instance = this;
        GameObject.ClearObjectList();
        camera = new OrthographicCamera();
        // make sure the chViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport = new FitViewport(20 * TILE_SIZE, 10 * TILE_SIZE, camera);
        map = new TmxMapLoader().load("beta_01.tmx");
        renderer = new OrthogonalTiledMapRenderer(this.map);

        //backgroundOffset = 0;
        font = new BitmapFont();
        font.setColor(Color.GRAY);
        font.getData().setScale(5);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //set up game objects
        playerTank = new PlayerTank(AssetManager.getInstance().PLAYER1_TANK_TEXTURE_REGIONS,
                new Vector2(PLAYER_INITIAL_POSITION_X, PLAYER_INITIAL_POSITION_Y));

        playerTank.setHitBox(new Rectangle(0, 0, PLAYER_HIT_BOX_WIDTH, PLAYER_HIT_BOX_HEIGHT));
        playerTank.setBulletMag(PLAYER_INITIAL_BULLET_MAG);
        playerTank.setScore(0);
        playerTank.setSpeed(PLAYER_INITIAL_MOVEMENT_SPEED);
        playerTank.setBaseSpeed(PLAYER_INITIAL_MOVEMENT_SPEED);
        playerTank.setFirepower(PLAYER_FIREPOWER);
        playerTank.setShield(PLAYER_INITIAL_SHIELD);
        playerTank.setTimeBetweenShots(PLAYER_TIME_BETWEEN_SHOT);
        playerTank.setDirection(PLAYER_INITIAL_DIRECTION);
        playerTank.setLife(100);
        playerTank.setTankTextureRegions(AssetManager.getInstance().PLAYER1_TANK_TEXTURE_REGIONS);

        batch = new SpriteBatch();

        layer = map.getLayers().get(OBJECTS_LAYER_INDEX);
        mapObjects = layer.getObjects();

        my_hud = new HUD(this);

        //position to spawn enemies
        spawnPos = new ArrayList<>();
        spawnPos.add(new Vector2(700, 450));
        spawnPos.add(new Vector2(2240, 820));
        spawnPos.add(new Vector2(1400, 2048));
        spawnPos.add(new Vector2(624, 2122));
        spawnPos.add(new Vector2(225, 1520));

        // play music and stuff
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Ending Theme - Super Mario- World.mp3"));
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {
    }

    private void spawnEnemies(float x, float y, int direction) {

        if (GameScreen.time_line - spawnTimers > spawnerDownTime) {
            if (normalEnemyCounter > bigEnemyThreshold) {
                TankAI enemyBigTank = new TankAI(AssetManager.getInstance().BIG_TANK_TEXTURE_REGIONS,
                        new Vector2(x, y));

                enemyBigTank.setHitBox(new Rectangle(x - (GameScreen.PLAYER_HIT_BOX_WIDTH * 1.5f) / 2f,
                        y - (GameScreen.PLAYER_HIT_BOX_WIDTH * 1.5f) / 2f, GameScreen.PLAYER_HIT_BOX_WIDTH * 1.5f,
                        GameScreen.PLAYER_HIT_BOX_HEIGHT * 1.5f));
                enemyBigTank.setBulletMag(3);
                enemyBigTank.setScore(200);
                enemyBigTank.setSpeed(3);
                enemyBigTank.setFirepower(ENEMY_FIREPOWER * 2);
                enemyBigTank.setShield(0);
                enemyBigTank.setTimeBetweenShots(ENEMY_TIME_BETWEEN_SHOT * 0.7f);
                enemyBigTank.setDirection(Direction.UP);
                enemyBigTank.setLife(80);
                enemyBigTank.setTankTextureRegions(AssetManager.getInstance().BIG_TANK_TEXTURE_REGIONS);
                normalEnemyCounter = 0;
            }else {
                TankAI enemyTank = new TankAI(AssetManager.getInstance().DEFAULT_TANK_TEXTURE_REGIONS,
                        new Vector2(x, y));

                enemyTank.setHitBox(new Rectangle(x, y, GameScreen.PLAYER_HIT_BOX_WIDTH, GameScreen.PLAYER_HIT_BOX_HEIGHT));
                enemyTank.setBulletMag(2);
                enemyTank.setScore(100);
                enemyTank.setSpeed(3);
                enemyTank.setFirepower(ENEMY_FIREPOWER);
                enemyTank.setShield(0);
                enemyTank.setTimeBetweenShots(ENEMY_TIME_BETWEEN_SHOT);
                enemyTank.setDirection(direction);
                enemyTank.setLife(30);
                enemyTank.setTankTextureRegions(AssetManager.getInstance().DEFAULT_TANK_TEXTURE_REGIONS);
                normalEnemyCounter += 1;
            }
            spawnTimers = GameScreen.time_line;
            HUD.getInstance().setEnemyCount(HUD.getInstance().getEnemyCount() + 1);
        }
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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
        // Dispose your own mess
        renderer.dispose();
        map.dispose();
        font.dispose();
        batch.dispose();
        backgroundMusic.dispose();
    }

    //loop game here


    public MapObjects getMapObjects() {
        return mapObjects;
    }


    private List<GameObject> getTankAIList(){
        List<GameObject> gameObjects = new ArrayList<>();
        for( GameObject gameObject : GameObject.gameObjectList){
            if(gameObject instanceof TankAI){
                gameObjects.add(gameObject);
            }
        }
        return gameObjects;
    }

    private boolean isCollideMap(Rectangle rec){
        for(RectangleMapObject objectRectangle : GameScreen.getInstance().mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle objectBounds = objectRectangle.getRectangle();
            if(rec.overlaps(objectBounds)) {
                return true;
            }
        }
        return false;
    }

    private void SpawnItem(){
        Star star = new Star(AssetManager.getInstance().STAR_TEXTURE_REGION);
        star.setSize(64, 64);
        star.setHitBox(new Rectangle(0, 0, 64, 64));
        star.setCollidable(true);
        while (isCollideMap(star.getHitBox())){
            star.setPosition(new Vector2(GENERATOR.nextInt(Graphic.NUMBER_OF_WIDTH_TILE*64),
                    GENERATOR.nextInt(Graphic.NUMBER_OF_HEIGHT_TILE*64)));
        }
    }

    @Override
    public void render(float delta) {

        //set super
        super.render(delta);

        Gdx.gl.glClearColor(0 ,0, 0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.render();

        if(getTankAIList().size() < ENEMY_QUANTITY){
            int direction = GENERATOR.nextInt(4);
            int pos = GENERATOR.nextInt(5);
            spawnEnemies(spawnPos.get(pos).x, spawnPos.get(pos).y, direction);
        }

        if(GameScreen.time_line >= timeToNextItemSpawn){
            timeToNextItemSpawn = 1 + GENERATOR.nextInt(3) + GameScreen.time_line;
            SpawnItem();
        }

        playerTank.detectInput();
        for(GameObject ob : GameObject.gameObjectList){
            ob.update();
            ob.draw(batch);
        }

        batch.end();

        VirtualController.getInstance().draw();
//        my_hud.update();
        my_hud.setLife(playerTank.getLife());
        my_hud.setShield(playerTank.getShield());
        my_hud.setPosition(playerTank.getPosition());
        my_hud.setScore(playerTank.getGainedScore());
        my_hud.draw();

    }
}
