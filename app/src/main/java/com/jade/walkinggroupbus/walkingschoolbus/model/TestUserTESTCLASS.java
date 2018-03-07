package com.jade.walkinggroupbus.walkingschoolbus.model;

/**
 * Created by brand on 2018-03-06.
 */

public class TestUserTESTCLASS {
    private String email;
    private String password;
    private String name;

    private static UserInfo instance;

    public TestUserTESTCLASS(){
        this.email = "testEmail@email.email";
        this.password = "bigMeatyClaws";
        this.name = "krabs";
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
