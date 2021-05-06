package com.mygdx.game.objects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Direction;
import com.mygdx.game.PvEScreen;
import com.mygdx.game.VirtualController;
import com.mygdx.game.network.GameClient;

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

//    public void SendInputToServer(GameClient gameClient){
////        Integer value;
//        String cmd ;
//        if (this.isCanFire() && VirtualController.getInstance().isCrossHairPressed() && !deadState) {
////            value = FIRE;
//            cmd = "msg FIRE";
//            gameClient.Send(cmd);
//        }
//        //keyboard input -- if move button press then  velocity != 0 --> move
//        if (VirtualController.getInstance().isLeftPressed()) {
//            cmd = "msg LEFT";
//            gameClient.Send(cmd);
//        }
//
//        if (VirtualController.getInstance().isRightPressed()) {
//            cmd = "msg RIGHT";
//            gameClient.Send(cmd);
//        }
//
//        if (VirtualController.getInstance().isUpPressed()) {
//            cmd = "msg UP";
//            gameClient.Send(cmd);
//        }
//
//        if (VirtualController.getInstance().isDownPressed()) {
//            cmd = "msg DOWN";
//            gameClient.Send(cmd);
//        }
//    }

}
