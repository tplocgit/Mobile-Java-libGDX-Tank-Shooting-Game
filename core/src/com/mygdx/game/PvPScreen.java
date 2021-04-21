package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

public class PvPScreen implements Screen {
    private int roomID = 0;
    //screen
    //private Camera  camera;
    private OrthographicCamera  camera;
    private Viewport viewport;

    public static final Random GENERATOR = new Random();

    //graphic
    static public SpriteBatch batch;
    private TextureRegion background;

    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas("images.atlas");;
    private TextureRegion enemyTankTextureRegion, enemyBigTankTextureRegion,
            barrelRedTextureRegion, barrelGreenTextureRegion, enemyBulletTextureRegion;

    private TextureRegion explosionTextureRegion, deadStateTextureRegion, shieldTextureRegion;


    public static final TextureRegion[] PLAYER1_TANK_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("tank_blue_left"),
            TEXTURE_ATLAS.findRegion("tank_blue_right"),
            TEXTURE_ATLAS.findRegion("tank_blue_up"),
            TEXTURE_ATLAS.findRegion("tank_blue_down"),
    };

    public static final TextureRegion[] PLAYER1_BULLET_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("bulletBlue2_left"),
            TEXTURE_ATLAS.findRegion("bulletBlue2_right"),
            TEXTURE_ATLAS.findRegion("bulletBlue2_up"),
            TEXTURE_ATLAS.findRegion("bulletBlue2_down"),
    };

    //timing
    private float spawnTimers = 0;
    private float spawnerDownTime = 4;
    private int normalEnemyCounter = 0;
    private int bigEnemyThreshold = 6;

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
    public static final int PLAYER_INITIAL_MOVEMENT_SPEED = TILE_SIZE * 6;
    public static final float PLAYER_INITIAL_POSITION_X = 64;
    public static final float PLAYER_INITIAL_POSITION_Y = 64;
    public static final int PLAYER_INITIAL_SHIELD = 50;
    public static final float PLAYER_HIT_BOX_WIDTH = 60;
    public static final float PLAYER_HIT_BOX_HEIGHT = 60;
    public static final int PLAYER_INITIAL_DIRECTION = Direction.RIGHT;
    public static final Bullet PLAYER_BULLET_SAMPLE = new Bullet(
            0, 0, PLAYER_BULLET_WIDTH, PLAYER_BULLET_HEIGHT,
            PLAYER_BULLET_SPEED, Direction.UP , PLAYER1_BULLET_TEXTURE_REGIONS);

    public static final int ENEMY_QUANTITY = 10;
    public static final int ENEMY_FIREPOWER = 1000;
    public static final int   ENEMY_BULLET_SPEED = TILE_SIZE * 6;
    public static final Bullet ENEMY_BULLET_SAMPLE = new Bullet(
            0, 0, PLAYER_BULLET_WIDTH, PLAYER_BULLET_HEIGHT,
            ENEMY_BULLET_SPEED, Direction.UP, Bullet.DEFAULT_TEXTURE_REGIONS
    );
    public static final float ENEMY_TIME_BETWEEN_SHOT = 0.8f;
    public static final TextureRegion[] BIG_TANK_TEXTURE_REGIONS = {
            TEXTURE_ATLAS.findRegion("tank_huge_left"),
            TEXTURE_ATLAS.findRegion("tank_huge_right"),
            TEXTURE_ATLAS.findRegion("tank_huge_up"),
            TEXTURE_ATLAS.findRegion("tank_huge_down")
    };

    //game objects
    private Tank playerTank;
    private Tank enemyTank;


    // other stuff
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private VirtualController controller;


    private MapLayer layer;
    private MapObjects mapObjects;

    private BitmapFont font;
    private HUD my_hud;
    private int score = 0, enemyCount = 0;

    private ArrayList<Vector2> spawnPos;
    boolean deadState = false;
    boolean shieldState = false;

    //firebase stuff
    FirebaseInterface myFb;

    PvPScreen(FirebaseInterface fb) {

        camera = new OrthographicCamera();
        // make sure the camera always shows us an area of our game world that is
        // WORLD_HEIGHTxWORLD_WIDTH units wide
        //camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        //viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport = new FitViewport(20 * TILE_SIZE, 10 * TILE_SIZE, camera);
        map = new TmxMapLoader().load("beta_01.tmx");
        renderer = new OrthogonalTiledMapRenderer(this.map);


        //background = TEXTURE_ATLAS.findRegion("bg_prison");


        enemyTankTextureRegion = Tank.DEFAULT_TANK_TEXTURE_REGIONS[Direction.UP];
        enemyBigTankTextureRegion = TEXTURE_ATLAS.findRegion("tank_huge_up");
        explosionTextureRegion = TEXTURE_ATLAS.findRegion("explosion4");
        deadStateTextureRegion = new TextureRegion(new Texture("dead_state.png"));
        shieldTextureRegion = new TextureRegion(new Texture("Shield/shieldBlue.png"));

        //backgroundOffset = 0;
        font = new BitmapFont();
        font.setColor(Color.GRAY);
        font.getData().setScale(5);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //set up game objects

        playerTank = new Tank(
                PLAYER_INITIAL_POSITION_X, PLAYER_INITIAL_POSITION_Y, PLAYER_WIDTH, PLAYER_HEIGHT,
                PLAYER_HIT_BOX_WIDTH, PLAYER_HIT_BOX_HEIGHT,
                PLAYER_INITIAL_BULLET_MAG, 1000 ,PLAYER_INITIAL_MOVEMENT_SPEED, PLAYER_FIREPOWER, PLAYER_INITIAL_SHIELD,
                PLAYER_TIME_BETWEEN_SHOT, PLAYER_INITIAL_DIRECTION,
                PLAYER_BULLET_SAMPLE, PLAYER1_TANK_TEXTURE_REGIONS);
        playerTank.life = 100;

        enemyTank = new Tank(
                PLAYER_INITIAL_POSITION_X * 6, PLAYER_INITIAL_POSITION_Y * 2, PLAYER_WIDTH, PLAYER_HEIGHT,
                PLAYER_HIT_BOX_WIDTH, PLAYER_HIT_BOX_HEIGHT,
                PLAYER_INITIAL_BULLET_MAG, 1000 ,PLAYER_INITIAL_MOVEMENT_SPEED, PLAYER_FIREPOWER, PLAYER_INITIAL_SHIELD,
                PLAYER_TIME_BETWEEN_SHOT, PLAYER_INITIAL_DIRECTION,
                PLAYER_BULLET_SAMPLE, PLAYER1_TANK_TEXTURE_REGIONS);
        enemyTank.life = 100;

        /*enemyTank = new EnemyTank(TILE_SIZE * (WORLD_TILE_WIDTH - 2),
                TILE_SIZE * (WORLD_TILE_HEIGHT - 2),
                PLAYER_WIDTH, PLAYER_HEIGHT, TILE_SIZE * 5,
                PLAYER_BULLET_WIDTH, PLAYER_BULLET_HEIGHT,
                PLAYER_BULLET_SPEED, PLAYER_TIME_BETWEEN_SHOT,
                enemyTankTextureRegion, enemyBulletTextureRegion);*/



//        player = new Rectangle();
//        player.x = WORLD_WIDTH / 2 - WORLD_HEIGHT / 4;
//        player.y = 50;
//        player.width = PLAYER_WIDTH;
//        player.height = PLAYER_HEIGHT;

        batch = new SpriteBatch();
        controller = new VirtualController();

        layer = map.getLayers().get(OBJECTS_LAYER_INDEX);
        mapObjects = layer.getObjects();
        /*for (MapObject object : my_objects_map) {
            System.out.println(object.getProperties().get("type"));
            System.out.println(object.getProperties().get("x"));
            System.out.println(object.getProperties().get("y"));
            System.out.println(object.getProperties().get("width"));
            System.out.println(object.getProperties().get("height"));
        }*/
        controller = new VirtualController();

        my_hud = new HUD(score, playerTank.life, playerTank.firepower,
                playerTank.shield, playerTank.movementSpeed / 64,
                enemyCount, new Vector2(playerTank.getX(), playerTank.getY()), true);
        my_hud.enemyCount = 1;
        my_hud.isPvp = true;
        //position to spawn enemies
        spawnPos = new ArrayList<>();
        spawnPos.add(new Vector2(700, 450));
        spawnPos.add(new Vector2(2240, 820));
        spawnPos.add(new Vector2(1400, 2048));
        spawnPos.add(new Vector2(624, 2122));
        spawnPos.add(new Vector2(225, 1520));

        //firebase stuff
        myFb = fb;

        myFb.writePlayerTankVal("room0", "P1", playerTank);
        myFb.writePlayerTankVal("room0", "P2", enemyTank);
        myFb.setValEventListener("TankGame/room0/Player/P1", enemyTank);

        /*
        myFb.writePlayerTankVal("P2", playerTank);
        myFb.setValEventListener("room0/P1", enemyTank);
        */

        //fb.readPlayerTankVal("P1");
        //myFb.readPlayerTankVal("Player/P1");
    }

    public void readData(Tank tank) {
        System.out.println("Hit box: "  + tank.hitBox.x + " " + tank.hitBox.y + " " +
                tank.hitBox.width + " " + tank.hitBox.height);
        System.out.println("Firepower: " + tank.firepower);
        System.out.println("Direction: " + tank.direction);
        System.out.println("BulletMag: " + tank.bulletMag);
        System.out.println("Shield: " + tank.shield);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0 ,0, 0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        renderer.setView(camera);

        batch.begin();
        renderer.render();
//        font.draw(batch, "Hi there", 100, 100);
        detectInput(delta);
        //System.out.println(playerTank.getX() + " " + playerTank.getY());

        playerTank.update(delta);
        enemyTank.update(delta);

        

        //game objects
        playerTank.draw(batch, delta);
        enemyTank.draw(batch, delta);

        //bullet stuff
        renderBullets(delta);
        detectBulletCollisions();

        //in render method

        batch.end();

        controller.draw();
        my_hud.draw(batch);

        myFb.writePlayerTankVal("room0", "P2", playerTank);

        /*
        myFb.writePlayerTankVal("P2", playerTank);
        */

        readData(enemyTank);
    }

    private void renderBullets(float deltaTime) {
        if (playerTank.canFire() && controller.crossHairPressed && !deadState) {
            playerTank.fireBullet();
        }
    }
    

    private void detectInput(float deltaTime) {
        if (!deadState) {
            boolean[] collisionDetected = detectCollisions(playerTank, deltaTime);

            //keyboard input
            if (controller.leftPressed) {
                playerTank.direction = Direction.LEFT;
                if (!collisionDetected[playerTank.direction])
                    playerTank.move(deltaTime);
            }

            if (controller.rightPressed) {
                playerTank.direction = Direction.RIGHT;
                if (!collisionDetected[playerTank.direction])
                    playerTank.move(deltaTime);
            }

            if (controller.upPressed) {
                playerTank.direction = Direction.UP;
                if (!collisionDetected[playerTank.direction])
                    playerTank.move(deltaTime);
            }

            if (controller.downPressed) {
                playerTank.direction = Direction.DOWN;
                if (!collisionDetected[playerTank.direction])
                    playerTank.move(deltaTime);
            }
        }

        camera.position.set(playerTank.getX() + playerTank.getWidth() / 2f, playerTank.getY() + playerTank.getHeight() / 2f, 0);
    }

    private boolean[] detectCollisions(Tank tank, float dt) {
        boolean[] collisionMap = new boolean[4];
        for(RectangleMapObject objectRectangle : mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle objectBounds = objectRectangle.getRectangle();
            for(int i = 0; i < 4; ++i)
                if(tank.getNextMoveHitBox(i, dt).overlaps(objectBounds)) {
                    collisionMap[i] = true;
                    break;
                }
        }
        return collisionMap;
    }

    // check if bullet hit anything
    private void detectBulletCollisions() {
        //check player's bullet collision
        for (int i = 0; i < playerTank.getBullets().size(); i++) {
            if (enemyTank != null) {
                if (enemyTank.isColliding(playerTank.getBullets().get(i).getHitBox())) {
                    playerTank.bulletCollision(i);
                    if (enemyTank.life > PLAYER_FIREPOWER) {
                        enemyTank.life -= PLAYER_FIREPOWER;
                    } else {
                        Explosion explosion = new Explosion(enemyTank.getX(),
                                enemyTank.getY(),
                                enemyTank.getWidth(), enemyTank.getHeight(),
                                explosionTextureRegion);
                        explosion.draw(batch);
                        TextureRegion[] deadStateTextureRegionArr = {deadStateTextureRegion, deadStateTextureRegion, deadStateTextureRegion, deadStateTextureRegion};

                        my_hud.score += enemyTank.getScore();
                        my_hud.enemyCount -= 1;
                        enemyTank.tankTextureRegions = deadStateTextureRegionArr;
                        enemyTank.setX(TILE_SIZE * -50);
                        enemyTank.setY(TILE_SIZE * -50);

                        deadState = true;
                        playerTank.tankTextureRegions = deadStateTextureRegionArr;
                        deadState = true;
                        playerTank.shield = 0;
                        playerTank.setX(TILE_SIZE * -55);
                        playerTank.setY(TILE_SIZE * -55);
                        System.out.println("You win");
                    }
                    --i; //safeguard
                    break;
                }
            }
        }


        //check enemy's bullet collision
            for(int j = 0; j < enemyTank.getBullets().size(); ++j) {
                if (playerTank.isColliding(enemyTank.getBullets().get(j).getHitBox())) {
                    enemyTank.bulletCollision(j);
                    --j;
                    shieldState = playerTank.shield > 0;
                    if (shieldState) {
                        playerTank.shield -= ENEMY_FIREPOWER;
                        my_hud.shield -= ENEMY_FIREPOWER;
                        if (my_hud.shield < 0)
                            my_hud.shield = 0;
                    }
                    else {
                        if (playerTank.life > ENEMY_FIREPOWER) {
                            playerTank.life -= ENEMY_FIREPOWER;
                            my_hud.life -= ENEMY_FIREPOWER;
                        } else {
                            Explosion explosion = new Explosion(playerTank.getX(), playerTank.getY(),
                                    playerTank.getWidth(), playerTank.getHeight(), explosionTextureRegion);
                            explosion.draw(batch);
                            TextureRegion[] deadStateTextureRegionArr = {deadStateTextureRegion, deadStateTextureRegion, deadStateTextureRegion, deadStateTextureRegion};
                            playerTank.tankTextureRegions = deadStateTextureRegionArr;
                            deadState = true;
                            playerTank.setX(TILE_SIZE * -50);
                            playerTank.setY(TILE_SIZE * -50);
                            my_hud.life -= ENEMY_FIREPOWER;
                            if (my_hud.life < 0)
                                my_hud.life = 0;
                            System.out.println("Game over");
                        }
                    }
                }
            }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(playerTank.getX(), playerTank.getY(), 0);
        batch.setProjectionMatrix(camera.combined);
        controller.resize(width, height);
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
}
