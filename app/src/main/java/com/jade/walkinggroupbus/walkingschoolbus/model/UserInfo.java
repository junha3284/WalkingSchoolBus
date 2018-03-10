package com.jade.walkinggroupbus.walkingschoolbus.model;


import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private Long id;
    private String email;

    private String password;
    private String name;

    private List<UserInfo> monitoredByUsers = new ArrayList<>();
    private List<UserInfo> monitorsUsers = new ArrayList<>();
    private List<Group> memberOfGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED
    private List<Group> leadsGroups = new ArrayList<>();

    private String href;

    // Singleton Implementation
    private static UserInfo instance;

    private UserInfo() {
        // private to prevent public instantiation
    }

    // Checks to see if an instance of UserInfo already exists
    public static UserInfo userInfo(){
        if (instance == null){
            instance = new UserInfo();
            return instance;
        }
        else
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserInfo> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public void setMonitoredByUsers(List<UserInfo> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public List<UserInfo> getMonitorsUsers() {
        return monitorsUsers;
    }

    public void setMonitorsUsers(List<UserInfo> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

    public List<Group> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<Group> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public List<Group> getLeadsGroups() {
        return leadsGroups;
    }

    public void setLeadsGroups(List<Group> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    public void addWalkingGroup(String groupName) {
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    // UI for Logcat messages.
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", monitoredByUsers=" + monitoredByUsers +
                ", monitorsUsers=" + monitorsUsers +
                ", walkingGroups=" + memberOfGroups +
                '}';
    }


    public String toStringForList() {
        return "name='" + name + '\'' +
                ", email='" + email;
    }
}

