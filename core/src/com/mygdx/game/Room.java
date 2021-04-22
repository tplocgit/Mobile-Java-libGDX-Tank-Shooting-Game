package com.mygdx.game;


import java.util.ArrayList;

public class Room {
    private String name;
    private boolean isPrivate;
    private String password;

    public Room() {

    }

    public Room(String name, boolean isPrivate, String password) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getPassword() {
        return password;
    }


    public boolean comparePassword(String inputPassword) {
        return password.equals(inputPassword);
    }

}
