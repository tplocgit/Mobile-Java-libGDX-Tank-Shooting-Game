package com.mygdx.game.network;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.GameObject;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Graphic;
import com.mygdx.game.objects.Tank;
import gameservice.GameService;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;

public class ServerWorker extends Thread{

    private InputStream svIn;
    private OutputStream svOut;
    private GameServer server;
    private Socket clientSocket;
    private long TankID;
    private Tank tank;
    private boolean isCanceled = false;

    public ServerWorker(GameServer server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
        this.TankID = (new Timestamp(System.currentTimeMillis())).getTime();
        this.svIn = this.clientSocket.getInputStream();
        this.svOut = this.clientSocket.getOutputStream();
        System.out.println(TankID);
        start();
        createClientTank();
        this.server.getWorkerList().add(this);
    }

    public void run(){

        while (!isCanceled) {
            try {
                HandleClientSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        GameObject.Destroy(tank);
        server.getWorkerList().remove(this);
    }

    public void safeKill() {
        isCanceled = true;
    }

    private void HandleClientSocket() throws IOException {

        GameService.MainMessage receiveData = Network.getIntance().readMessage(svIn);

        switch (receiveData.getCommand()) {
            case FIRE_BULLET:
                tank.fireBullet(AssetManager.getInstance().PLAYER1_BULLET_TEXTURE_REGIONS, false);
                break;

            case MOVE_LEFT:
                tank.setDirection(Direction.LEFT);
                break;

            case MOVE_RIGHT:
                tank.setDirection(Direction.RIGHT);
                break;

            case MOVE_UP:
                tank.setDirection(Direction.UP);
                break;

            case MOVE_DOWN:
                tank.setDirection(Direction.DOWN);
                break;

            case MOVE_NONE:
                tank.setVelocity(new Vector2(0, 0));
                break;
        }
    }

    private void createClientTank() throws IOException {
        tank = new Tank(AssetManager.getInstance().PLAYER1_TANK_TEXTURE_REGIONS,
                new Vector2(PvPScreen.PLAYER_INITIAL_POSITION_X, PvPScreen.PLAYER_INITIAL_POSITION_Y));

        tank.setTankId(TankID);
        tank.setHitBox(new Rectangle(0, 0, PvPScreen.PLAYER_HIT_BOX_WIDTH, PvPScreen.PLAYER_HIT_BOX_HEIGHT));
        tank.setBulletMag(PvPScreen.PLAYER_INITIAL_BULLET_MAG);
        tank.setScore(1000);
        tank.setSpeed(PvPScreen.PLAYER_INITIAL_MOVEMENT_SPEED);
        tank.setBaseSpeed(PvPScreen.PLAYER_INITIAL_MOVEMENT_SPEED);
        tank.setFirepower(PvPScreen.PLAYER_FIREPOWER);
        tank.setShield(PvPScreen.PLAYER_INITIAL_SHIELD);
        tank.setTimeBetweenShots(PvPScreen.PLAYER_TIME_BETWEEN_SHOT);
        tank.setDirection(PvPScreen.PLAYER_INITIAL_DIRECTION);
        tank.setLife(100);
        tank.setTankTextureRegions(AssetManager.getInstance().PLAYER1_TANK_TEXTURE_REGIONS);

        boolean isOverLapOtherTank = true;
        while (GameServer.getInstance().isCollideMap(tank.getHitBox()) || isOverLapOtherTank){
            isOverLapOtherTank = false;
            tank.setPosition(new Vector2(GameServer.GENERATOR.nextInt(Graphic.NUMBER_OF_WIDTH_TILE*64),
                    GameServer.GENERATOR.nextInt(Graphic.NUMBER_OF_HEIGHT_TILE*64)));
            for (GameObject ob : GameObject.gameObjectList){
                if (ob instanceof Tank){
                    if (ob.getHitBox().overlaps(tank.getHitBox()) && ob != tank){
                        isOverLapOtherTank = true;
                    }
                }
            }
        }

        GameService.MainMessage mainMessage = GameService.MainMessage.newBuilder()
                .setCommand(GameService.Command.PLAYER_DATA)
                .setData(GameService.Data.newBuilder()
                        .setPlayerData(GameService.PlayerData.newBuilder()
                                .setPlayerId(TankID)
                                .build())
                )
                .build();

        Network.getIntance().sendMessage(svOut, mainMessage);

    }

    public OutputStream getSvOut() {
        return svOut;
    }

    public void shutdown() {
        isCanceled = true;
        try{
            if(clientSocket != null) clientSocket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
