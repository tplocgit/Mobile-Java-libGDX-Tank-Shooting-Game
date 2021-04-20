package com.mygdx.game;

public interface FirebaseInterface {

    public void writePlayerTankVal(String userId, Tank playerTank);

    public void readPlayerTankVal(final String target);

}
