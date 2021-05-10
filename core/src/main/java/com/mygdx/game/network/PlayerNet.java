package com.mygdx.game.network;

import com.mygdx.game.Direction;
import com.mygdx.game.VirtualController;
import gameservice.GameService;

public class PlayerNet {

    private GameService.GameObject playerObject;
    private long playerID = 0;

    public void detectInput() {

        if (VirtualController.getInstance().isCrossHairPressed()) {
            GameClient.getInstance().sendFireCommand();
        }
        //keyboard input -- if move button press then  velocity != 0 --> move
        if (VirtualController.getInstance().isLeftPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.LEFT);
        }

        if (VirtualController.getInstance().isRightPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.RIGHT);
        }

        if (VirtualController.getInstance().isUpPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.UP);
        }

        if (VirtualController.getInstance().isDownPressed()) {
            GameClient.getInstance().sendDirectionCommands(Direction.DOWN);
        }

        if (!VirtualController.getInstance().isLeftPressed()
                && !VirtualController.getInstance().isRightPressed()
                && !VirtualController.getInstance().isDownPressed()
                && !VirtualController.getInstance().isUpPressed()
        ) {
            GameClient.getInstance().sendDirectionCommands(Direction.NONE);
        }
    }

    public GameService.GameObject getPlayerObject() {
        return playerObject;
    }

    public void setPlayerObject(GameService.GameObject playerObject) {
        this.playerObject = playerObject;
    }

    public long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(long playerID) {
        this.playerID = playerID;
    }
}
