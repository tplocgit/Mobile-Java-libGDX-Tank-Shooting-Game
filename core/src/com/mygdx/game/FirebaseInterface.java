package com.mygdx.game;

import com.mygdx.game.objects.Tank;

public interface FirebaseInterface {

    void writePlayerTankVal(String room, String userId, com.mygdx.game.objects.Tank playerTank);

    void setValEventListener(final String target, final Tank tank);

    void deletePlayerNode(String targetKey);

    void deleteBullet(String targetKey);

    void deleteNode(String parentPath, String targetKey);

    void createRoom(Room room);
}
