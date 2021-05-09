package com.mygdx.game.network;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.GameObject;
import com.mygdx.game.PvEScreen;
import com.mygdx.game.TankShootingGame;
import com.mygdx.game.objects.PlayerTank;
import com.mygdx.game.objects.Tank;
import gameservice.GameService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerWorker extends Thread{

    private InputStream svIn;
    private OutputStream svOut;
    private GameServer server;
    private Socket clientSocket;
    private int TankID;
    private Tank playerTank;
    private boolean isCanceled = false;

    public ServerWorker(GameServer server, Socket clientSocket) throws IOException {
        this.server = server;
        this.clientSocket = clientSocket;
        this.TankID = (new Timestamp(System.currentTimeMillis())).getNanos();
        this.svIn = clientSocket.getInputStream();
        this.svOut = clientSocket.getOutputStream();
        this.server.getWorkerList().add(this);
        start();
        createClientTank();
        System.out.println(TankID);
    }

    public void run(){

        while (!isCanceled) {
            try {
                HandleClientSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        GameObject.Destroy(playerTank);
        server.getWorkerList().remove(this);
    }

    public void safeKill() {
        isCanceled = true;
    }

    private void HandleClientSocket() throws IOException {
        byte[] data = new byte[1024];
        int dataLength = svIn.read(data);
        GameService.MainMessage receiveData = server.getMessageFromData(data, dataLength);

        switch (receiveData.getCommand()) {
            case FIRE_BULLET:
                playerTank.fireBullet();
                break;

            case MOVE_LEFT:
                playerTank.setDirection(Direction.LEFT);
                break;

            case MOVE_RIGHT:
                playerTank.setDirection(Direction.RIGHT);
                break;

            case MOVE_UP:
                playerTank.setDirection(Direction.UP);
                break;

            case MOVE_DOWN:
                playerTank.setDirection(Direction.DOWN);
                break;

            case MOVE_NONE:
                playerTank.setVelocity(new Vector2(0, 0));
                break;
        }
    }

    public void Send(byte[] data) {
        try {
            svOut.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ServerWorker.java (Send function) false");
        }
    }
    private void createClientTank() {
        playerTank = new PlayerTank(AssetManager.getInstance().PLAYER1_TANK_TEXTURE_REGIONS,
                new Vector2(PvPScreen.PLAYER_INITIAL_POSITION_X, PvPScreen.PLAYER_INITIAL_POSITION_Y));

        playerTank.setHitBox(new Rectangle(0, 0, PvPScreen.PLAYER_HIT_BOX_WIDTH, PvPScreen.PLAYER_HIT_BOX_HEIGHT));
        playerTank.setBulletMag(PvPScreen.PLAYER_INITIAL_BULLET_MAG);
        playerTank.setScore(1000);
        playerTank.setSpeed(PvPScreen.PLAYER_INITIAL_MOVEMENT_SPEED);
        playerTank.setBaseSpeed(PvPScreen.PLAYER_INITIAL_MOVEMENT_SPEED);
        playerTank.setFirepower(PvPScreen.PLAYER_FIREPOWER);
        playerTank.setShield(PvPScreen.PLAYER_INITIAL_SHIELD);
        playerTank.setTimeBetweenShots(PvPScreen.PLAYER_TIME_BETWEEN_SHOT);
        playerTank.setDirection(PvPScreen.PLAYER_INITIAL_DIRECTION);
        playerTank.setLife(100);
        playerTank.setTankTextureRegions(AssetManager.getInstance().PLAYER1_TANK_TEXTURE_REGIONS);

    }

}
