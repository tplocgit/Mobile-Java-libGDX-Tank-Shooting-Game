package com.mygdx.game;

public interface FirebaseInterface {

    void writePlayerTankVal(String userId, Tank playerTank);

    void setValEventListener(final String target, final Tank tank);

}
