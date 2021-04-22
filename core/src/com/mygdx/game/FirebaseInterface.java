package com.mygdx.game;

public interface FirebaseInterface {

    void writePlayerTankVal(String room, String userId, Tank playerTank);

    void setValEventListener(final String target, final Tank tank);

    void deletePlayerNode(String targetKey);

    void deleteBullet(String targetKey);

    void deleteNode(String parentPath, String targetKey);

    void createRoom(Room room);
}
