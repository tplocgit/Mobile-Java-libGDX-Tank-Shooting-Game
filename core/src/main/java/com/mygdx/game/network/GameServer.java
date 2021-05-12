package com.mygdx.game.network;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.GameObject;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Graphic;
import com.mygdx.game.items.Star;
import com.mygdx.game.objects.Tank;
import com.mygdx.game.objects.TankAI;
import gameservice.GameService;

import java.io.IOException;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameServer {

    public static final int UDP_PORT = 1234;
    public static final int TCP_PORT = 34567;
    public static GameServer instance;
    private Thread udpThread;
    private Thread tcpThread;
    private DatagramSocket UDPSocket;
    public ServerSocket svSocket;
    private String serverName;
    private boolean isCanceled = false;
    private boolean isStarted = false;
    public static final Random GENERATOR = new Random();

    public static final int OBJECTS_LAYER_INDEX = 2;
    private ArrayList<Vector2> spawnPos;

    //timing
    private float spawnTimers = 0;
    private float spawnerDownTime = 4;
    private int normalEnemyCounter = 0;
    private int bigEnemyThreshold = 6;
    private float timeToNextItemSpawn = 0;

    public static final int ENEMY_QUANTITY = 10;
    public static final int ENEMY_FIREPOWER = 10;
    public static final int ENEMY_BULLET_SPEED = 8;
    public static final float ENEMY_TIME_BETWEEN_SHOT = 0.8f;


    //servermap
    private TiledMap map;
    private MapLayer layer;
    private MapObjects mapObjects;


    //game object list

    private ArrayList<ServerWorker> workerList = new ArrayList<>();

    public GameServer(String serverName) {
        this.serverName = serverName;
        instance = this;
        createServer();
    }

    public static GameServer getInstance() {

        return instance;
    }

    private void createServer() {
        udpThread = new Thread(() -> {
            try {
                UDPSocket = new DatagramSocket(UDP_PORT);
                byte[] buffer = new byte[1024];
                while (!isCanceled) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    UDPSocket.receive(packet);

                    GameService.MainMessage mainMessage = GameService.MainMessage.parser().parseFrom(packet.getData(), 0, packet.getLength());

                    if (mainMessage.getCommand() == GameService.Command.FIND_SERVER) {
                        GameService.MainMessage response = GameService.MainMessage.newBuilder()
                                .setCommand(GameService.Command.FIND_SERVER)
                                .setData(GameService.Data.newBuilder()
                                        .setServerInfo(GameService.ServerInfo.newBuilder()
                                                .setServerName(serverName)
                                        )
                                )
                                .build();
                        DatagramPacket responsePackage = new DatagramPacket(response.toByteArray(), response.toByteArray().length, packet.getAddress(), packet.getPort());
                        UDPSocket.send(responsePackage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        udpThread.start();

        tcpThread = new Thread(() -> {
            try {
                this.svSocket = new ServerSocket(TCP_PORT);

                while (!isCanceled) {
                    Socket clientSocket = svSocket.accept();
                    System.out.println("Client connected from: " + clientSocket);
                    ServerWorker worker = new ServerWorker(this, clientSocket);
                    workerList.add(worker);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tcpThread.start();
    }

    public void startServer() {
        map = new TmxMapLoader().load("beta_01.tmx");
        layer = map.getLayers().get(OBJECTS_LAYER_INDEX);
        mapObjects = layer.getObjects();

        isStarted = true;
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

                boolean isOverLapOtherTank = true;
                while (isCollideMap(enemyBigTank.getHitBox()) || isOverLapOtherTank){
                    isOverLapOtherTank = false;
                    enemyBigTank.setPosition(new Vector2(GENERATOR.nextInt(Graphic.NUMBER_OF_WIDTH_TILE*64),
                            GENERATOR.nextInt(Graphic.NUMBER_OF_HEIGHT_TILE*64)));
                    for (GameObject ob : GameObject.gameObjectList){
                        if (ob instanceof Tank){
                            if (ob.getHitBox().overlaps(enemyBigTank.getHitBox()) && ob != enemyBigTank){
                                isOverLapOtherTank = true;
                            }
                        }
                    }
                }

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

                boolean isOverLapOtherTank = true;
                while (isCollideMap(enemyTank.getHitBox()) || isOverLapOtherTank){
                    isOverLapOtherTank = false;
                    enemyTank.setPosition(new Vector2(GENERATOR.nextInt(Graphic.NUMBER_OF_WIDTH_TILE*64),
                            GENERATOR.nextInt(Graphic.NUMBER_OF_HEIGHT_TILE*64)));
                    for (GameObject ob : GameObject.gameObjectList){
                        if (ob instanceof Tank){
                            if (ob.getHitBox().overlaps(enemyTank.getHitBox()) && ob != enemyTank){
                                isOverLapOtherTank = true;
                            }
                        }
                    }
                }

                normalEnemyCounter += 1;
            }
            spawnTimers = GameScreen.time_line;
        }
    }

    private List<GameObject> getTankAIList(PvPScreen pvPScreen){
        List<GameObject> gameObjects = new ArrayList<>();
        for( GameObject gameObject : GameObject.gameObjectList){
            if(gameObject instanceof TankAI){
                gameObjects.add(gameObject);
            }
        }
        return gameObjects;
    }

    public boolean isCollideMap(Rectangle rec){
        for(RectangleMapObject objectRectangle : mapObjects.getByType(RectangleMapObject.class)) {
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



    private void broadcastToClients(PvPScreen pvPScreen){
        GameService.MainMessage mainMessage = getBroadcastMessage(pvPScreen);
        for (ServerWorker worker : workerList){
            try {
                Network.getIntance().sendMessage(worker.getSvOut(), mainMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private GameService.MainMessage getBroadcastMessage(PvPScreen pvPScreen) {
        List<GameService.GameObject> gameServiceObjectList = new ArrayList<>();

        for (GameObject go : GameObject.gameObjectList) {
            gameServiceObjectList.add(go.getServiceObject());
        }

        return GameService.MainMessage.newBuilder()
                .setCommand(GameService.Command.UPDATE)
                .setData(GameService.Data.newBuilder()
                        .setObjectList(GameService.GameObjectList.newBuilder()
                                .addAllGameObjectList(gameServiceObjectList)
                        )
                )
                .build();
    }

    public ArrayList<ServerWorker> getWorkerList() {
        return workerList;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public MapObjects getMapObjects() {
        return mapObjects;
    }

    public void shutdownServer() {
        isCanceled = true;
        try {
            if(svSocket != null) svSocket.close();
            if(UDPSocket != null) UDPSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(ServerWorker worker: workerList) {
            worker.shutdown();
        }
    }

    public void update(PvPScreen pvPScreen) {
        if(getTankAIList(pvPScreen).size() < ENEMY_QUANTITY){
            int direction = GENERATOR.nextInt(4);
            spawnEnemies(0, 0, direction);
        }

        if(GameScreen.time_line >= timeToNextItemSpawn){
            timeToNextItemSpawn = 1 + GENERATOR.nextInt(3) + GameScreen.time_line;
            SpawnItem();
        }

        for(GameObject ob : GameObject.gameObjectList){
            ob.update();
        }

        broadcastToClients(pvPScreen);
    }

}
