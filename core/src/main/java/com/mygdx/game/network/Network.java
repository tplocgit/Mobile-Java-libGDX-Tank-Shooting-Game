package com.mygdx.game.network;

import gameservice.GameService;
import sun.applet.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Network {

    private static Network instance;

    public static Network getIntance() {
        if(instance == null) {
            instance = new Network();
        }
        return instance;
    }

    public void sendMessage(OutputStream outputStream, GameService.MainMessage mainMessage) throws IOException {
        mainMessage.writeDelimitedTo(outputStream);
    }

    public final GameService.MainMessage readMessage(InputStream inputStream) throws IOException {
        return GameService.MainMessage.parseDelimitedFrom(inputStream);
    }
}
