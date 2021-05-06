package com.mygdx.game.network;

//import javax.swing.*;
//import java.io.*;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Scanner;
//import java.util.concurrent.TimeUnit;
//
//public class GameClient extends Thread{
//    private final String svName;
//    private final int svPort;
//    private Socket clientSocket;
//    private OutputStream clientOut;
//    private InputStream clientIn;
//    private DataInputStream dataIn;
//    private DataOutputStream dataOut;
////    public ArrayList<String> onlineUser = new ArrayList<>();
////    public DefaultListModel<String> onlineUserList = new DefaultListModel<>();
//
//    public GameClient(String svName, int svPort) throws IOException {
//        this.svName = svName;
//        this.svPort = svPort;
//        if(!connect()){
//            System.out.println("connect false");
//        }
//        else {
//            if (Login()) {
//                System.out.println("login successful");
//            } else {
//                System.out.println("login false");
//            }
//        }
//    }
//
//    private boolean Login() throws IOException {
//        dataOut.writeUTF("LOGIN jake");
//
//        String respone = dataIn.readUTF();
//        if (respone == null || respone.equals("")) System.out.println("cant respone");
//                System.out.println(respone);
//
//        String[] token;
//        token = respone.split(" ");
//        if (respone.equals("LOGIN SUCCESS") && token[0].equalsIgnoreCase("LOGIN")) {
////            startMessageReader();
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public boolean connect() {
//        try {
////            System.out.println(svName + svPort);
//            this.clientSocket = new Socket(svName, svPort);
////            clientSocket.connect(new InetSocketAddress(svName, svPort));
////            System.out.println("client port is: " + clientSocket.getLocalPort());
//            this.clientIn = clientSocket.getInputStream();
//            this.clientOut = clientSocket.getOutputStream();
//            dataOut = new DataOutputStream(this.clientOut);
//            dataIn = new DataInputStream(this.clientIn);
//            return true;
//        }catch (IOException e){ System.out.println(e); }
//
//        return false;
//    }
//
//    public void Send(String cmd) {
//        try {
//            dataOut.writeUTF(cmd);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("something wrong in Send");
//        }
//    }
//
//}

import com.mygdx.game.ConnectServerScreen;
import gameservice.GameService;
import sun.applet.Main;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class GameClient {

    public static final int UDP_PORT = 1235;
    public static GameClient instance;
    private Thread udpThread;
    private DatagramSocket UDPSocket;
    private String clientName;
    private boolean isCanceled = false;
    private List<ServerInfo> serverList = new ArrayList<>();

    public GameClient(String clientName) {
        this.clientName = clientName;
        instance = this;
        createUDPClient();
    }

    public static GameClient getInstance() {
        return instance;
    }

    private void createUDPClient() {
        udpThread = new Thread(() -> {
            try {
                UDPSocket = new DatagramSocket(UDP_PORT);
                byte[] buffer = new byte[1024];

                while (!isCanceled) {
                    DatagramPacket respondPackage = new DatagramPacket(buffer, buffer.length);
                    UDPSocket.receive(respondPackage);

                    byte[] receiveBuffer = Arrays.copyOf(respondPackage.getData(), respondPackage.getLength());
                    System.out.println("begin " + receiveBuffer.length);

                    GameService.MainMessage responseMessage = GameService.MainMessage.parseFrom(receiveBuffer);

                    if(responseMessage.getCommand() == GameService.Command.FIND_SERVER) {
                        serverList.add(new ServerInfo(
                                responseMessage.getData().getServerInfo().getServerName(),
                                respondPackage.getAddress().getHostAddress()
                        ));

                        System.out.println("Found server at "
                                + respondPackage.getAddress().getHostAddress()
                                + "/"
                                + respondPackage.getPort()
                                + " with name: "
                                + responseMessage.getData().getServerInfo().getServerName()
                        );

                        if(ConnectServerScreen.getInstance() != null) {
                            ConnectServerScreen.getInstance().addServerToList(
                                    responseMessage.getData().getServerInfo().getServerName(),
                                    respondPackage.getAddress().getHostAddress(),
                                    respondPackage.getPort()
                            );
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        udpThread.start();

    }

    public void searchingForServers() {
        serverList.clear();
        new Thread(() -> {
            try {
                GameService.MainMessage mainMessage = GameService.MainMessage.newBuilder()
                        .setCommand(GameService.Command.FIND_SERVER)
                        .build();

                String broadcast = getBroadcast();
                System.out.println("broadcast: " + broadcast);

                DatagramPacket requestPackage = new DatagramPacket(
                        mainMessage.toByteArray(),
                        mainMessage.toByteArray().length,
                        InetAddress.getByName(broadcast),
                        GameServer.UDP_PORT
                );

                UDPSocket.send(requestPackage);
                System.out.println("Client sent request");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static String getBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements();) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    if(interfaceAddress.getBroadcast() != null) {
                        return interfaceAddress.getBroadcast().toString().substring(1);
                    }
                }
            }
        }
        return null;
    }
}
