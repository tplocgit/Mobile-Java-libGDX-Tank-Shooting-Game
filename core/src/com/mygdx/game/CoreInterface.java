package com.mygdx.game;

import com.mygdx.game.objects.Tank;

public class CoreInterface implements FirebaseInterface{

    @Override
    public void writePlayerTankVal(String room, String userId, com.mygdx.game.objects.Tank playerTank) {

    }

    @Override
    public void setValEventListener(String target, final Tank tank) {

    }

    @Override
    public void deletePlayerNode(String targetKey) {


    }

    @Override
    public void deleteBullet(String targetKey) {

    }

    @Override
    public void deleteNode(String parentPath, String targetKey) {

    }

    @Override
    public void createRoom(Room room) {

    }

}
