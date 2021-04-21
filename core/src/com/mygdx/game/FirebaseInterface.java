package com.mygdx.game;

public interface FirebaseInterface {

    void writePlayerTankVal(String userId, Tank playerTank);

    void readPlayerTankVal(final String target);

}
