package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.PvEScreen;
import com.mygdx.game.VirtualController;

public class PlayerTank extends Tank {

    private boolean deadState = false;

    public PlayerTank(TextureRegion[] textureRegions, Vector2 position) {
        super(textureRegions, position);
    }

    @Override
    public void update(){
        super.update();
        detectInput();
    }

    private void detectInput() {
        if (!deadState) {

            //if nothing press then velocity is 0 --> cant move
            this.setVelocity(new Vector2(0,0));

            if (this.isCanFire() && VirtualController.getInstance().isCrossHairPressed() && !deadState) {
                this.fireBullet();
            }
            //keyboard input -- if move button press then  velocity != 0 --> move
            if (VirtualController.getInstance().isLeftPressed()) {
                this.setDirection(Direction.LEFT);
            }

            if (VirtualController.getInstance().isRightPressed()) {
                this.setDirection(Direction.RIGHT);
            }

            if (VirtualController.getInstance().isUpPressed()) {
                this.setDirection(Direction.UP);
            }

            if (VirtualController.getInstance().isDownPressed()) {
                this.setDirection(Direction.DOWN);
            }
        }

        PvEScreen.getInstance().getCamera().position.set(this.getPosition().x , this.getPosition().y , 0);
    }



}
