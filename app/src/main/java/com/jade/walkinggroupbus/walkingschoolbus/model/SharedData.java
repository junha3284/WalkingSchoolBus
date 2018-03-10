package com.jade.walkinggroupbus.walkingschoolbus.model;

/**
 * Created by choijun-ha on 2018-03-09.
 */

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

