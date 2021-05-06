package com.mygdx.game.network;

public class ServerInfo {

    private String serverName = "Unknown";
    private String serverIp = "";

    public ServerInfo(String serverName, String serverIp) {
        this.serverName = serverName;
        this.serverIp = serverIp;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
