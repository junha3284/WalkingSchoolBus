package com.jade.walkinggroupbus.walkingschoolbus.model;


import java.util.ArrayList;
import java.util.List;

public class ChildInfo {

    private UserInfo child;
    private boolean manageChild = false;


    // Singleton Implementation
    private static ChildInfo instance;

    private ChildInfo() {

    }

    public static ChildInfo childInfo(){
        if (instance == null){
            instance = new ChildInfo();
            return instance;
        }
        return instance;
    }

    public void setChildInfo(UserInfo user) {
        child = user;
    }

    public boolean isActive() {
        return manageChild;
    }

    public void activateUser() {
        manageChild = true;
    }

    public void deactivateUser() {
        manageChild = false;
    }

    public Long getId() {
        Long id = child.getId();
        return id;
    }

    public UserInfo getChild(){
        return child;
    }
}
