package com.mygdx.game.network;

import gameservice.GameService;
import io.grpc.Server;

import java.io.IOException;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

//public class GameServer {
//    public ServerSocket svSocket;
//    private int svPort;
//    public GameServer instance = this;
//    public ArrayList<ServerWorker> workerList = new ArrayList<>();
//    public List<String> usernameList = new ArrayList<>();
////    private static GameServer instance;
////    private String serverName = "defaultname";
////    private Server server;//    public GameServer(String serverName) throws IOException {
////
////        this.serverName = serverName;
////
////    }
////
////    public static GameServer getInstance() throws IOException {
////        if(instance == null){
////            instance = new GameServer("default_server_name");
////        }
////        return instance;
////    }
////
////    public void Start(){
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    server = NettyServerBuilder.forPort(9995)
////                            .addService(getInstance())
////                            .build();
////
////                    server.start();
////                    System.out.println("server started at port: " + server.getPort());
////                    server.awaitTermination();
////                } catch (IOException | InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        }).start();
////    }
////
////    @Override
////    public void findServer(GameService.CommandData request, StreamObserver<GameService.ServerInfo> responseObserver) {
////
////        GameService.ServerInfo serverInfo = GameService.ServerInfo.newBuilder()
////                .setServerName(serverName)
////                .build();
////
////
////        responseObserver.onNext(serverInfo);
////        responseObserver.onCompleted();
////    }
//    public GameServer() throws IOException {
//        int svPort = 8888;
//        Start();
//    }
//
//    private void Start() throws IOException {
//        this.svSocket = new ServerSocket(svPort);
//        System.out.println(svSocket.toString());
//        System.out.println("waiting for client to connect");
//        Socket clientSocket = svSocket.accept();
//        System.out.println("connect to: " + clientSocket);
//        ServerWorker worker = new ServerWorker(this, clientSocket);
//        workerList.add(worker);
//    }
//
//    public void RemoveWorker(ServerWorker worker){
//        workerList.remove(worker);
//    }
//}
public class GameServer{

    public static final int UDP_PORT = 1234;
    public static GameServer instance;
    private Thread udpThread;
    private DatagramSocket UDPSocket;
    private String serverName;
    private boolean isCanceled = false;

    public GameServer(String serverName, GameServerListener listener) {
        this.serverName = serverName;
        createUDPServer(listener);
    }

    public static GameServer getInstance() {

        return instance;
    }

    private void createUDPServer(GameServerListener listener) {
        udpThread = new Thread(() -> {
            try {
                UDPSocket = new DatagramSocket(UDP_PORT);
                byte[] buffer = new byte[1024];
                listener.onReady(UDP_PORT);
                while (!isCanceled) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    UDPSocket.receive(packet);
//                    ByteBuffer byteBuffer = new ByteBuffer()
                    byte[] receiveBuffer = Arrays.copyOf(packet.getData(), packet.getLength());
                    System.out.println("begin " + receiveBuffer.length);
                    GameService.MainMessage mainMessage = GameService.MainMessage.parseFrom(receiveBuffer);

                    System.out.println("end");

                    if(mainMessage.getCommand() == GameService.Command.FIND_SERVER) {
                        GameService.MainMessage response = GameService.MainMessage.newBuilder()
                                .setCommand(GameService.Command.FIND_SERVER)
                                .setData(GameService.Data.newBuilder()
                                        .setServerInfo(GameService.ServerInfo.newBuilder()
                                                .setServerName(serverName)
                                        )
                                )
                                .build();

                        DatagramPacket responsePackage = new DatagramPacket(response.toByteArray(), response.toByteArray().length, packet.getAddress(), packet.getPort());
                        UDPSocket.send(responsePackage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        udpThread.start();
    }
}
