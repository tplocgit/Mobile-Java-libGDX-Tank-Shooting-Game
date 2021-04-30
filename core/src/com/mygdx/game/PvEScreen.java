package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.game.items.Item;

import com.mygdx.game.items.Star;
import com.mygdx.game.objects.PlayerTank;
import com.mygdx.game.objects.Tank;
import com.mygdx.game.objects.TankAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PvEScreen extends GameScreen {

    public static PvEScreen instance = null;

    public static final Random GENERATOR = new Random();

    //graphic
    private SpriteBatch batch;

    public static final TextureRegion[] PLAYER1_TANK_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("tank_blue_left"),
            TEXTURE_ATLAS.findRegion("tank_blue_right"),
            TEXTURE_ATLAS.findRegion("tank_blue_up"),
            TEXTURE_ATLAS.findRegion("tank_blue_down"),
    };

    //timing
    private float spawnTimers = 0;
    private float spawnerDownTime = 4;
    private int normalEnemyCounter = 0;
    private int bigEnemyThreshold = 6;





    public static final int ENEMY_QUANTITY = 10;
    public static final int ENEMY_FIREPOWER = 10;
    public static final int   ENEMY_BULLET_SPEED = 8;
    public static final float ENEMY_TIME_BETWEEN_SHOT = 0.8f;
    public static final TextureRegion[] BIG_TANK_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("tank_huge_left"),
            TEXTURE_ATLAS.findRegion("tank_huge_right"),
            TEXTURE_ATLAS.findRegion("tank_huge_up"),
            TEXTURE_ATLAS.findRegion("tank_huge_down")
    };
    public static final TextureRegion[] DEFAULT_TANK_TEXTURE_REGIONS = {
            Graphic.TEXTURE_ATLAS.findRegion("tank_bigRed_left"),
            Graphic.TEXTURE_ATLAS.findRegion("tank_bigRed_right"),
            Graphic.TEXTURE_ATLAS.findRegion("tank_bigRed_up"),
            Graphic.TEXTURE_ATLAS.findRegion("tank_bigRed_down"),
    };




    // other stuff
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private MapLayer layer;
    public MapObjects mapObjects;

    private BitmapFont font;
    private HUD my_hud;
    private int score = 0, enemyCount = 0;

    private ArrayList<Vector2> spawnPos;

    PvEScreen(FirebaseInterface fb) {
        instance = this;
        camera = new OrthographicCamera();
        // make sure the chViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport = new FitViewport(20 * TILE_SIZE, 10 * TILE_SIZE, camera);
        map = new TmxMapLoader().load("beta_01.tmx");
        renderer = new OrthogonalTiledMapRenderer(this.map);


        //background = TEXTURE_ATLAS.findRegion("bg_prison");


//        enemyTankTextureRegion = Tank.DEFAULT_TANK_TEXTURE_REGIONS[Direction.UP];
//        enemyBigTankTextureRegion = TEXTURE_ATLAS.findRegion("tank_huge_up");
//        explosionTextureRegion = TEXTURE_ATLAS.findRegion("explosion4");
//        deadStateTextureRegion = new TextureRegion(new Texture("dead_state.png"));
//        shieldTextureRegion = new TextureRegion(new Texture("Shield/shieldBlue.png"));

        //backgroundOffset = 0;
        font = new BitmapFont();
        font.setColor(Color.GRAY);
        font.getData().setScale(5);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //set up game objects

        playerTank = new com.mygdx.game.objects.PlayerTank(PLAYER1_TANK_TEXTURE_REGIONS,
                new Vector2(PLAYER_INITIAL_POSITION_X, PLAYER_INITIAL_POSITION_Y));

        playerTank.setHitBox(new Rectangle(0, 0, PLAYER_HIT_BOX_WIDTH, PLAYER_HIT_BOX_HEIGHT));
        playerTank.setBulletMag(PLAYER_INITIAL_BULLET_MAG);
        playerTank.setScore(1000);
        playerTank.setSpeed(PLAYER_INITIAL_MOVEMENT_SPEED);
        playerTank.setBaseSpeed(PLAYER_INITIAL_MOVEMENT_SPEED);
        playerTank.setFirepower(PLAYER_FIREPOWER);
        playerTank.setShield(PLAYER_INITIAL_SHIELD);
        playerTank.setTimeBetweenShots(PLAYER_TIME_BETWEEN_SHOT);
        playerTank.setDirection(PLAYER_INITIAL_DIRECTION);
        playerTank.setLife(100);
        playerTank.setTankTextureRegions(PLAYER1_TANK_TEXTURE_REGIONS);

        batch = new SpriteBatch();

        layer = map.getLayers().get(OBJECTS_LAYER_INDEX);
        mapObjects = layer.getObjects();

        my_hud = new HUD();

        //position to spawn enemies
        spawnPos = new ArrayList<>();
        spawnPos.add(new Vector2(700, 450));
        spawnPos.add(new Vector2(2240, 820));
        spawnPos.add(new Vector2(1400, 2048));
        spawnPos.add(new Vector2(624, 2122));
        spawnPos.add(new Vector2(225, 1520));

        SpawnItem();
        SpawnItem();
        SpawnItem();
        SpawnItem();
        SpawnItem();
        SpawnItem();
        SpawnItem();
        SpawnItem();

    }

    @Override
    public void show() {
    }

    public static PvEScreen getInstance(){
        return instance;
    }


    private void spawnEnemies(float x, float y, int direction, float deltaTime) {
        spawnTimers += deltaTime;

        if (spawnTimers > spawnerDownTime) {
            if (normalEnemyCounter > bigEnemyThreshold) {
                TankAI enemyBigTank = new TankAI(BIG_TANK_TEXTURE_REGIONS,
                        new Vector2(x, y));

                enemyBigTank.setHitBox(new Rectangle(x - (PLAYER_HIT_BOX_WIDTH * 1.5f) / 2f,
                        y -(PLAYER_HIT_BOX_WIDTH * 1.5f) / 2f, PLAYER_HIT_BOX_WIDTH * 1.5f,  PLAYER_HIT_BOX_HEIGHT * 1.5f));
                enemyBigTank.setBulletMag(3);
                enemyBigTank.setScore(200);
                enemyBigTank.setSpeed(3);
                enemyBigTank.setFirepower(ENEMY_FIREPOWER * 2);
                enemyBigTank.setShield(0);
                enemyBigTank.setTimeBetweenShots(ENEMY_TIME_BETWEEN_SHOT * 0.7f);
                enemyBigTank.setDirection(Direction.UP);
                enemyBigTank.setLife(80);
                enemyBigTank.setTankTextureRegions(BIG_TANK_TEXTURE_REGIONS);
                
                my_hud.enemyCount += 1;
                normalEnemyCounter = 0;
            }

            TankAI enemyTank = new TankAI(DEFAULT_TANK_TEXTURE_REGIONS,
                    new Vector2(x, y));

            enemyTank.setHitBox(new Rectangle(x, y, PLAYER_HIT_BOX_WIDTH ,  PLAYER_HIT_BOX_HEIGHT ));
            enemyTank.setBulletMag(2);
            enemyTank.setScore(100);
            enemyTank.setSpeed(3);
            enemyTank.setFirepower(ENEMY_FIREPOWER);
            enemyTank.setShield(0);
            enemyTank.setTimeBetweenShots(ENEMY_TIME_BETWEEN_SHOT);
            enemyTank.setDirection(direction);
            enemyTank.setLife(30);
            enemyTank.setTankTextureRegions(DEFAULT_TANK_TEXTURE_REGIONS);

            my_hud.enemyCount += 1;
            spawnTimers -= spawnerDownTime;
            normalEnemyCounter += 1;
        }



    }

    private void moveEnemy(Tank enemyTank, float deltaTime) {
//        if (enemyTank != null) {
//            boolean[] collisionDetected = detectCollisions(enemyTank, deltaTime);
//            int trigger = 0;
//            while(collisionDetected[trigger]) ++trigger;
//
//            if(collisionDetected[trigger]) return; //??
//
//            enemyTank.timeSinceLastDirChange += deltaTime;
//            if (enemyTank.timeSinceLastDirChange > enemyTank.dirChangeFreq){
//                int i = GENERATOR.nextInt(4);
//                //while(collisionDetected[i]) GENERATOR.nextInt(4);
//
//                enemyTank.direction = i;
//                enemyTank.timeSinceLastDirChange -= enemyTank.dirChangeFreq;
//            }
//
//            if (!collisionDetected[enemyTank.direction]) {
//                enemyTank.move(deltaTime);
//            }
//        }
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
    }

    //loop game here


    public MapObjects getMapObjects() {
        return mapObjects;
    }


    private List<GameObject> getTankAIList(){
        List<GameObject> gameObjects = new ArrayList<>();
        for( GameObject gameObject : getGameObjectList()){
            if(gameObject instanceof TankAI){
                gameObjects.add(gameObject);
            }
        }
        return gameObjects;
    }

    private boolean isCollideMap(Rectangle rec){
        for(RectangleMapObject objectRectangle : PvEScreen.getInstance().mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle objectBounds = objectRectangle.getRectangle();
            if(rec.overlaps(objectBounds)) {
                return true;
            }
        }
        return false;
    }

    private void SpawnItem(){
        Star star = new Star(Star.STAR_TEXTURE_REGION);
        star.setScale(new Vector2(0.1f, 0.1f));
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
//        out.println(playerTank.getX() + " " + playerTank.getY());

        // change direction later maybe
        int direction = GENERATOR.nextInt(4);
        int pos = GENERATOR.nextInt(5);
        if(getTankAIList().size() < ENEMY_QUANTITY){
            spawnEnemies(spawnPos.get(pos).x, spawnPos.get(pos).y, direction, delta);
        }

        for(GameObject ob : getGameObjectList()){
            ob.update();
            ob.draw(batch);
        }

        batch.end();

        VirtualController.getInstance().draw();
        my_hud.update();
        my_hud.draw();

    }
}
