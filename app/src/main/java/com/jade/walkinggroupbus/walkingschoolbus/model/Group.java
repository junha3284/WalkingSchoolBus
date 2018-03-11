package com.jade.walkinggroupbus.walkingschoolbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choijun-ha on 2018-03-10.
 */

public class Group {
    private Long id;
    private String GroupDescription;

    private Double[] routeLatArray;
    private Double[] routeLngArray;

    private List<UserInfo> leaders = new ArrayList<>();
    private List<UserInfo> members = new ArrayList<>();

    private String href;

    public Long getId(){
        return id;
    }

    public String getGroupDescription(){
        return GroupDescription;
    }

    public Double[] getRouteLatArray(){
        return routeLatArray;
    }

    public Double[] getRouteLngArray(){
        return routeLatArray;
    }

    public List<UserInfo> getLeaders(){
        return leaders;
    }

    public List<UserInfo> getMembers(){
        return members;
    }

    public String getHref(){
        return href;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupDescription(String groupDescription) {
        GroupDescription = groupDescription;
    }

    public void setRouteLatArray(Double[] routeLatArray) {
        this.routeLatArray = routeLatArray;
    }

    public void setRouteLngArray(Double[] routeLngArray) {
        this.routeLngArray = routeLngArray;
    }

    public void setLeaders(List<UserInfo> leaders) {
        this.leaders = leaders;
    }

    public void setMembers(List<UserInfo> members) {
        this.members = members;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
