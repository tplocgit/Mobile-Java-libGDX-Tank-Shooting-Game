package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface FirebaseInterface {
    public void writePlayerTankVal(String userId, PlayerTank playerTank);

    public void readValFromDb(String target);
}
