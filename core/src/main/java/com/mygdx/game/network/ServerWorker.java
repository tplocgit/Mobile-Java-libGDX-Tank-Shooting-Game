//package com.mygdx.game.network;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class ServerWorker extends Thread{
//    private InputStream svIn;
//    private OutputStream svOut;
//    private DataInputStream dataIn;
//    private DataOutputStream dataOut;
//    private GameServer server;
//    private Socket clientSocket;
//
//    private String username;
//
//
//    public ServerWorker(GameServer server, Socket clientSocket) throws IOException {
//        this.server = server;
//        this.clientSocket = clientSocket;
//    }
//
//    public void run(){
//        try {
//            HandleClientSocket();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void HandleClientSocket() throws IOException {
//
//        this.svIn = clientSocket.getInputStream();
//        this.svOut = clientSocket.getOutputStream();
//        this.dataIn = new DataInputStream(svIn);
//        this.dataOut= new DataOutputStream(svOut);
//
//        String line = null;
//
//        boolean loginStatus = true;
//        do {
//            line = dataIn.readUTF();
//            String[] token = line.split(" ", 3);
//            if(token[0].equalsIgnoreCase("login")){
//                loginStatus = HandleLogin(token);
//            }
//        }while(loginStatus == false && line != null);
//
//        System.out.println(username);
//
//        //print out cmd from client
//        while (loginStatus && !line.equalsIgnoreCase("@quit")){ //handle after login
//            line = dataIn.readUTF();
//            System.out.println(line);
//            String[] token = line.split(" ", 3);
//
////            if(token[0].equalsIgnoreCase("msg")){
////                HandleMessenger(token);
////            }
//        }
//    }
//
//    private boolean HandleLogin(String[] token) {
//        if(!server.usernameList.contains(token[1])){
//            Send("LOGIN SUCCESS");
//            this.username = token[1];
//            return true;
//        }
//        return false;
//    }
//
//    private void Send(String cmd) {
//        try {
//            dataOut.writeUTF(cmd);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("line 64 (Send function) false");
//        }
//    }
//
//}
