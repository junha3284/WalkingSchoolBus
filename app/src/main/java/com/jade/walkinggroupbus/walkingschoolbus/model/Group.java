package com.jade.walkinggroupbus.walkingschoolbus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {
    private Long id;
    private String groupDescription;
    private String customJson;

    private Double[] routeLatArray;
    private Double[] routeLngArray;

    private UserInfo leader;
    private List<UserInfo> memberUsers = new ArrayList<>();

    private List<Message> messages = new ArrayList<>();

    private String href;

    public Long getId(){
        return id;
    }

    public String getGroupDescription(){
        return groupDescription;
    }

    public String getCustomJson() {
        return customJson;
    }

    public Double[] getRouteLatArray(){
        return routeLatArray;
    }

    public Double[] getRouteLngArray(){
        return routeLngArray;
    }

    public UserInfo getLeader(){
        return leader;
    }

    public List<UserInfo> getMemberUsers(){
        return memberUsers;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getHref(){
        return href;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupDescription(String description) {
        groupDescription = description;
    }

    public void setCustomJson(String customJson) {
        this.customJson = customJson;
    }

    public void setRouteLatArray(Double[] routeLatArray) {
        this.routeLatArray = routeLatArray;
    }

    public void setRouteLngArray(Double[] routeLngArray) {
        this.routeLngArray = routeLngArray;
    }

    public void setLeader(UserInfo leader) {
        this.leader = leader;
    }

    public void setMemberUsers(List<UserInfo> members) {
        this.memberUsers = members;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
