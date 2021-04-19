package com.mygdx.game;

public interface FirebaseInterface {

    public void writePlayerTankVal(String userId, Tank playerTank);

    public void readValFromDb(final String target);

}
