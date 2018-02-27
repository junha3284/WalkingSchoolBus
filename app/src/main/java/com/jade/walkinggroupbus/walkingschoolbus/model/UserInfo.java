package com.jade.walkinggroupbus.walkingschoolbus.model;

/**
 * Created by Richard Wong on 2018-02-27.
 */

public class UserInfo {
    private String email;
    private String password;
    private String name;

    private static UserInfo instance;

    private UserInfo() {
        // private to prevent public instantiation
    }

    public static UserInfo userInfo(){
        if (instance == null){
            instance = new UserInfo();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
