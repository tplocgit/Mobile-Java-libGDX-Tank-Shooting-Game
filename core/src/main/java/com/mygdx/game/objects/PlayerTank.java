package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.GameScreen;
import com.mygdx.game.PvEScreen;
import com.mygdx.game.VirtualController;
import com.mygdx.game.AssetManager;
import com.mygdx.game.network.GameClient;
import com.mygdx.game.network.PvPScreen;

public class PlayerTank extends Tank {

    public final int NOTHING_PRESS = 10;
    public final int FIRE = 4;

    private boolean deadState = false;

    public PlayerTank(TextureRegion[] textureRegions, Vector2 position) {
        super(textureRegions, position);
    }

    @Override
    public void update(){
        super.update();
    }

    public void detectInput() {
        if (!deadState) {

            //if nothing press then velocity is 0 --> cant move
            this.setVelocity(new Vector2(0,0));

            if (this.isCanFire() && VirtualController.getInstance().isCrossHairPressed() && !deadState) {
                if(GameScreen.getInstance() instanceof PvPScreen) {
                    GameClient.getInstance().sendFireCommand();
                }else {
                    this.fireBullet(AssetManager.getInstance().PLAYER1_BULLET_TEXTURE_REGIONS, false);
                }
            }
            //keyboard input -- if move button press then  velocity != 0 --> move
            if (VirtualController.getInstance().isLeftPressed()) {
                if(GameScreen.getInstance() instanceof PvPScreen) {
                    GameClient.getInstance().sendDirectionCommands(Direction.LEFT);
                }else {
                    this.setDirection(Direction.LEFT);
                }
            }

            if (VirtualController.getInstance().isRightPressed()) {
                if(GameScreen.getInstance() instanceof PvPScreen) {
                    GameClient.getInstance().sendDirectionCommands(Direction.RIGHT);
                }else {
                    this.setDirection(Direction.RIGHT);
                }
            }

            if (VirtualController.getInstance().isUpPressed()) {
                if(GameScreen.getInstance() instanceof PvPScreen) {
                    GameClient.getInstance().sendDirectionCommands(Direction.UP);
                }else {
                    this.setDirection(Direction.UP);
                }
            }

            if (VirtualController.getInstance().isDownPressed()) {
                if(GameScreen.getInstance() instanceof PvPScreen) {
                    GameClient.getInstance().sendDirectionCommands(Direction.DOWN);
                }else {
                    this.setDirection(Direction.DOWN);
                }
            }
        }
        PvEScreen.getInstance().getCamera().position.set(this.getPosition().x , this.getPosition().y , 0);
    }
}
