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

    private UserInfo leader;
    private List<UserInfo> memberUsers = new ArrayList<>();

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
        return routeLngArray;
    }

    public UserInfo getLeader(){
        return leader;
    }

    public List<UserInfo> getMemberUsers(){
        return memberUsers;
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

    public void setLeader(UserInfo leader) {
        this.leader = leader;
    }

    public void setMemberUsers(List<UserInfo> members) {
        this.memberUsers = members;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
