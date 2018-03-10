package com.jade.walkinggroupbus.walkingschoolbus.model;


import java.util.ArrayList;
import java.util.List;

public class ChildInfo {
    private Long id;
    private String email;
    private String password;
    private String name;

    private List<UserInfo> monitoredByUsers = new ArrayList<>();
    private List<UserInfo> monitorsUsers = new ArrayList<>();
    private List<Void> walkingGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED

    private String href;
    
}
