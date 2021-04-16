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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class GameScreen implements Screen {

    //screen
    //private Camera  camera;
    private OrthographicCamera  camera;
    private Viewport viewport;

    public static final Random GENERATOR = new Random();
    
    //graphic
    static public SpriteBatch batch;
    private TextureRegion background;

    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas("images.atlas");;
    private TextureRegion playerTankTextureRegion, enemyTankTextureRegion, barrelRedTextureRegion,
            barrelGreenTextureRegion, playerBulletTextureRegion, enemyBulletTextureRegion;
    private TextureRegion explosionTextureRegion, deadStateTexture;

    //timing
    private int backgroundOffset;
    private float spawnTimers = 0;
    private float spawnerDownTime = 4;

    //world parameters

    private final int OBJECTS_LAYER_INDEX = 2;
    /*public static final int WORLD_HEIGHT = 40;
    public static final int WORLD_WIDTH = 40;*/
    private static final int TILE_SIZE = 64;
    private static final int NUMBER_OF_WIDTH_TILE = 40;
    private static final int NUMBER_OF_HEIGHT_TILE = 40;


    //player parameters
    private final int PLAYER_HEIGHT = 64;
    private final int PLAYER_WIDTH = 64;
    private final int PLAYER_BULLET_WIDTH = 2 * 5;
    private final int PLAYER_BULLET_HEIGHT = 3 * 5;
    private final int PLAYER_BULLET_SPEED = TILE_SIZE * 7;
    private final float PLAYER_TIME_BETWEEN_SHOT = 0.5f;
    private final int   ENEMY_BULLET_SPEED = TILE_SIZE * 5;
    private final float ENEMY_TIME_BETWEEN_SHOT = 0.8f;
    private final int ENEMY_BULLET_WIDTH = 10;
    private final int ENEMY_BULLET_HEIGHT = 12;
    private final int ENEMY_QUANTITY = 5;
    private final int PLAYER_FIREPOWER = 10;
    private final int ENEMY_FIREPOWER = 5;

    //game objects
    private PlayerTank playerTank;
    private LinkedList<EnemyTank> enemyTankList;
    private LinkedList<Bullet> playerBulletList;
    private LinkedList<Bullet> enemyBulletList;

    // other stuff
    public static final float SCALE = 2;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private VirtualController controller;

    private Vector3 touchPoint;

    private MapLayer layer;
    private MapObjects mapObjects;

    private BitmapFont font;
    private HUD my_hud;
    private int score = 0, enemyCount = 0;

    private ArrayList<Vector2> spawnPos;
    boolean deadState = false;

    GameScreen() {

        camera = new OrthographicCamera();
        // make sure the camera always shows us an area of our game world that is
        // WORLD_HEIGHTxWORLD_WIDTH units wide
        //camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        //viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport = new FitViewport(20 * TILE_SIZE, 10 * TILE_SIZE, camera);
        map = new TmxMapLoader().load("beta_01.tmx");
        renderer = new OrthogonalTiledMapRenderer(this.map);
        

        //background = TEXTURE_ATLAS.findRegion("bg_prison");
        playerTankTextureRegion = TEXTURE_ATLAS.findRegion("tank_blue_up");
        enemyTankTextureRegion = TEXTURE_ATLAS.findRegion("tank_bigRed_up");
        playerBulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletBlue2_up");
        enemyBulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletRed2_up");
        explosionTextureRegion = TEXTURE_ATLAS.findRegion("explosion4");
        deadStateTexture = new TextureRegion(new Texture("dead_state.png"));

        //backgroundOffset = 0;
        font = new BitmapFont();
        font.setColor(Color.GRAY);
        font.getData().setScale(5);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //set up game objects
        playerTank = new PlayerTank(TILE_SIZE, TILE_SIZE,
                PLAYER_WIDTH, PLAYER_HEIGHT,
                TILE_SIZE * 5, PLAYER_FIREPOWER, 0,
                PLAYER_BULLET_WIDTH, PLAYER_BULLET_HEIGHT,
                PLAYER_BULLET_SPEED, PLAYER_TIME_BETWEEN_SHOT, Direction.UP,
                playerTankTextureRegion, playerBulletTextureRegion);
        playerTank.life = 100;

        /*enemyTank = new EnemyTank(TILE_SIZE * (WORLD_TILE_WIDTH - 2),
                TILE_SIZE * (WORLD_TILE_HEIGHT - 2),
                PLAYER_WIDTH, PLAYER_HEIGHT, TILE_SIZE * 5,
                PLAYER_BULLET_WIDTH, PLAYER_BULLET_HEIGHT,
                PLAYER_BULLET_SPEED, PLAYER_TIME_BETWEEN_SHOT,
                enemyTankTextureRegion, enemyBulletTextureRegion);*/
        enemyTankList = new LinkedList<EnemyTank>();

        playerBulletList = new LinkedList<>();
        enemyBulletList = new LinkedList<>();

//        player = new Rectangle();
//        player.x = WORLD_WIDTH / 2 - WORLD_HEIGHT / 4;
//        player.y = 50;
//        player.width = PLAYER_WIDTH;
//        player.height = PLAYER_HEIGHT;

        batch = new SpriteBatch();
        controller = new VirtualController();
        touchPoint = new Vector3();

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

        my_hud = new HUD(score, playerTank.life, enemyCount, new Vector2(playerTank.getX(), playerTank.getY()), true);

        //position to spawn enemies
        spawnPos = new ArrayList<>();
        spawnPos.add(new Vector2(700, 450));
        spawnPos.add(new Vector2(2240, 820));
        spawnPos.add(new Vector2(1400, 2048));
        spawnPos.add(new Vector2(624, 2122));
        spawnPos.add(new Vector2(225, 1520));
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

        int x = GENERATOR.nextInt(10);
        int y = GENERATOR.nextInt(10);
        // change direction later maybe
        int direction = GENERATOR.nextInt(4);
        int pos = GENERATOR.nextInt(5);
        if (enemyTankList.size() < ENEMY_QUANTITY && !deadState)
            spawnEnemies(spawnPos.get(pos).x, spawnPos.get(pos).y, direction, delta);

        for (EnemyTank tank : enemyTankList) {
            moveEnemy(tank, delta);
            tank.update(delta);
            tank.draw(batch);
        }

        //background
        //batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        //game objects
        playerTank.draw(batch);

        //bullet stuff
        renderBullets(delta);
        detectBulletCollisions();

        //in render method

        batch.end();

        controller.draw();
        my_hud.draw(batch);
    }

    private void renderBullets(float deltaTime) {
        if (playerTank.canFire() && controller.crossHairPressed && !deadState) {
            Bullet[] bullets = playerTank.fireBullet(playerBulletTextureRegion);
            Collections.addAll(playerBulletList, bullets);
        }

        for (int i = 0; i < enemyTankList.size(); i++) {
            if (enemyTankList.get(i).canFire()) {
                Bullet[] bullets = enemyTankList.get(i).fireBullet(enemyBulletTextureRegion);
                Collections.addAll(enemyBulletList, bullets);
            }
        }
        //draw those bullet
        for (int i = 0; i < playerBulletList.size(); i++) {
            Bullet bullet = playerBulletList.get(i);
            bullet.draw(batch);
            switch (bullet.direction) {
                case Direction.UP:
                    bullet.setY(bullet.getY() + bullet.movementSpeed * deltaTime);
                    break;
                case Direction.DOWN:
                    bullet.setY(bullet.getY() - bullet.movementSpeed * deltaTime);
                    break;
                case Direction.LEFT:
                    bullet.setX(bullet.getX() - bullet.movementSpeed * deltaTime);
                    break;
                case Direction.RIGHT:
                    bullet.setX(bullet.getX() + bullet.movementSpeed * deltaTime);
                    break;
            }
            //check if bullet still inside our world, if not remove them
            if (bullet.getX() > TILE_SIZE * (NUMBER_OF_HEIGHT_TILE - 1) || bullet.getX() < 0 ||
                    bullet.getY() > TILE_SIZE * (NUMBER_OF_WIDTH_TILE - 1) || bullet.getY() < 0)
                playerBulletList.remove(i);
        }

        for (int i = 0; i < enemyBulletList.size(); i++) {
            Bullet bullet = enemyBulletList.get(i);
            bullet.draw(batch);
            switch (bullet.direction) {
                case Direction.UP:
                    bullet.setY(bullet.getY() + bullet.movementSpeed * deltaTime);
                    break;
                case Direction.DOWN:
                    bullet.setY(bullet.getY() - bullet.movementSpeed * deltaTime);
                    break;
                case Direction.LEFT:
                    bullet.setX(bullet.getX() - bullet.movementSpeed * deltaTime);
                    break;
                case Direction.RIGHT:
                    bullet.setX(bullet.getX() + bullet.movementSpeed * deltaTime);
                    break;
            }
            //check if bullet still inside our world, if not remove them
            if (bullet.getX() > TILE_SIZE * (NUMBER_OF_HEIGHT_TILE - 1) || bullet.getX() < 0 ||
                    bullet.getY() > TILE_SIZE * (NUMBER_OF_WIDTH_TILE - 1) || bullet.getY() < 0)
                enemyBulletList.remove(i);
        }
    }

    private void spawnEnemies(float x, float y, int direction, float deltaTime) {
        spawnTimers += deltaTime;
        EnemyTank enemyTank = new EnemyTank(x, y, PLAYER_WIDTH, PLAYER_HEIGHT,
                TILE_SIZE * 3, ENEMY_FIREPOWER, 0,
                PLAYER_BULLET_WIDTH, PLAYER_BULLET_HEIGHT,
                ENEMY_BULLET_SPEED, ENEMY_TIME_BETWEEN_SHOT, direction,
                enemyTankTextureRegion, enemyBulletTextureRegion);
        if (spawnTimers > spawnerDownTime){
            enemyTank.life = 30;
            enemyTankList.add(enemyTank);
            my_hud.enemyCount += 1;
            spawnTimers -= spawnerDownTime;
        }
    }

    private void moveEnemy(EnemyTank enemyTank, float deltaTime) {
        boolean[] collisionDetected = detectCollisions(enemyTank, deltaTime);

        if (!collisionDetected[enemyTank.direction]) {
            if(enemyTank.direction == Direction.LEFT)
                enemyTank.bulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletRed2_left");

            else if(enemyTank.direction == Direction.RIGHT)
                enemyTank.bulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletRed2_right");

            else if(enemyTank.direction == Direction.UP)
                enemyTank.bulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletRed2_up");

            else if(enemyTank.direction == Direction.DOWN)
                enemyTank.bulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletRed2_down");

            enemyTank.move(deltaTime);
        }
    }

    private void detectInput(float deltaTime) {
        if (!deadState) {
            boolean[] collisionDetected = detectCollisions(playerTank, deltaTime);

            //keyboard input
            if (controller.leftPressed) {
                playerBulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletBlue2_left");
                playerTank.direction = Direction.LEFT;
                if (!collisionDetected[playerTank.direction])
                    playerTank.move(deltaTime);
            }

            if (controller.rightPressed) {
                playerBulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletBlue2_right");
                playerTank.direction = Direction.RIGHT;
                if (!collisionDetected[playerTank.direction])
                    playerTank.move(deltaTime);
            }

            if (controller.upPressed) {
                playerBulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletBlue2_up");
                playerTank.direction = Direction.UP;
                if (!collisionDetected[playerTank.direction])
                    playerTank.move(deltaTime);
            }

            if (controller.downPressed) {
                playerBulletTextureRegion = TEXTURE_ATLAS.findRegion("bulletBlue2_down");
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

    private void detectBulletCollisions() {

        //check player's bullet collision
        for (int i = 0; i < playerBulletList.size(); i++) {
            if (enemyTankList != null) {
                for (int j = 0; j < enemyTankList.size(); j++) {
                    if (enemyTankList.get(j).collides(playerBulletList.get(i).getHitBox())) {
                        playerBulletList.remove(i);
                        if (enemyTankList.get(j).life > PLAYER_FIREPOWER) {
                            enemyTankList.get(j).life -= PLAYER_FIREPOWER;
                        }
                        else {
                            Explosion explosion = new Explosion(enemyTankList.get(j).getX(),
                                    enemyTankList.get(j).getY(),
                                    enemyTankList.get(j).getWidth(), enemyTankList.get(j).getHeight(),
                                    explosionTextureRegion);
                            explosion.draw(batch);
                            enemyTankList.remove(j);
                            my_hud.score += 100;
                            my_hud.enemyCount -= 1;
                            --j;
                        }
                        --i; //safeguard
                        break;
                    }
                }
            }
        }

        //check enemy's bullet collision
        for (int i = 0; i < enemyBulletList.size(); i++) {
            if (playerTank.collides(enemyBulletList.get(i).getHitBox())) {
                enemyBulletList.remove(i);
                if (playerTank.life > ENEMY_FIREPOWER) {
                    playerTank.life -= ENEMY_FIREPOWER;
                    my_hud.life -= ENEMY_FIREPOWER;
                }
                else {
                    Explosion explosion = new Explosion(playerTank.getX(), playerTank.getY(),
                            playerTank.getWidth(), playerTank.getHeight(), explosionTextureRegion);
                    explosion.draw(batch);
                    playerTank.tankTexture = deadStateTexture;
                    deadState = true;
                    playerTank.setX(TILE_SIZE * - 50);
                    playerTank.setY(TILE_SIZE * - 50);
                    my_hud.life -= ENEMY_FIREPOWER;
                    System.out.println("Game over");
                }
                --i; //safeguard
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
