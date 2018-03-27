package com.jade.walkinggroupbus.walkingschoolbus.model;

public class SharedData {
    private static SharedData instance;

    private String token;

    private SharedData(){}

    public static SharedData getSharedData(){
        if(instance == null)
            instance = new SharedData();
        return instance;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }
}

