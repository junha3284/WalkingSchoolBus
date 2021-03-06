package com.jade.walkinggroupbus.walkingschoolbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * class for storing + accessing all groups
 */

public class GroupsInfo {

    private List<Group> walkingGroups = new ArrayList<>();

    private static GroupsInfo instance;

    private GroupsInfo() {

    }

    public static GroupsInfo getInstance() {
        if (instance == null) {
            instance = new GroupsInfo();
        }
        return instance;
    }


    public List<String> getNames() {
        List<String> groupNames = new ArrayList<>();
        for (Group group : walkingGroups) {
            groupNames.add(group.getGroupDescription());
        }
        return groupNames;
    }

    // pre-condition: groupName in walkingGroups
    public List<Double> getMeetingPlaceCoordinates(String groupName) {
        List<Double> coordinates = new ArrayList<>();
        for (Group group : walkingGroups) {
            if (group.getGroupDescription().equals(groupName)) {
                Double[] latCoordinates = group.getRouteLatArray();
                Double[] lngCoordinates = group.getRouteLngArray();
                coordinates.add(latCoordinates[0]);
                coordinates.add(lngCoordinates[0]);
            }
        }
        return coordinates;
    }

    // pre-condition: groupName in walkingGroups
    public List<Double> getDestinationCoordinates(String groupName) {
        List<Double> coordinates = new ArrayList<>();
        for (Group group : walkingGroups) {
            if (group.getGroupDescription().equals(groupName)) {
                Double[] latCoordinates = group.getRouteLatArray();
                Double[] lngCoordinates = group.getRouteLngArray();
                coordinates.add(latCoordinates[1]);
                coordinates.add(lngCoordinates[1]);
            }
        }
        return coordinates;
    }

    // pre-condition: groupName in walkingGroups
    public List<UserInfo> getMembers(String groupName) {
        List<UserInfo> members = new ArrayList<>();
        for (Group group : walkingGroups) {
            if (group.getGroupDescription().equals(groupName)) {
                members = group.getMemberUsers();
            }
        }
        return members;
    }

    // pre-condition: groupName in walkingGroups
    public Long getGroupID(String groupName) {
        Long groupID = null;
        for (Group group : walkingGroups) {
            if (group.getGroupDescription().equals(groupName)) {
                groupID = group.getId();
            }
        }
        return groupID;
    }

    // pre-condition: groupName in walkingGroups
    public void setMembers(String groupName, List<UserInfo> users) {
        for (Group group : walkingGroups) {
            if (group.getGroupDescription().equals(groupName)) {
                group.setMemberUsers(users);
            }
        }
    }

    public void setGroups(List<Group> groups) {
        walkingGroups = groups;
    }

    // pre-condition: id in walkingGroups
    public String getNameByID(Long id) {
        String name = "";
        for (Group group : walkingGroups) {
            if (group.getId().equals(id)) {
                name = group.getGroupDescription();
            }
        }
        return name;
    }

    // only to be used in MGD therefore, the group will always exist when function is called
    public Group getGroupByID(Long id) {
        Group retGroup = new Group();
        for (Group group : walkingGroups) {
            if (group.getId().equals(id)) {
                retGroup = group;
            }
        }
        return retGroup;
    }

    // pre-condition: id in walkingGroups
    public Long getLeaderByID(Long id) {
        Long leader = null;
        for (Group group : walkingGroups) {
            if (group.getId().equals(id)) {
                leader = group.getLeader().getId();
            }
        }
        return leader;
    }
}
