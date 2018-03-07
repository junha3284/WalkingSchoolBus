package com.jade.walkinggroupbus.walkingschoolbus.model;

/**
 * Created by c on 06/03/18.
 */

public class GroupsInfo {



    public static GroupsInfo instance;

    private GroupsInfo() {

    }

    public static GroupsInfo getInstance() {
        if (instance == null) {
            instance = new GroupsInfo();
        }
        return instance;
    }


    public String[] getNames() {
        String[] groupNames = {};
        return groupNames;
    }

    public float[] getCoordinates(String groupName) {
        float[] coordinates = {0,0};
        return coordinates;
    }

    public String getDestination(String groupName) {
        String destination = "Destination";
        return destination;
    }

    public String getMeetingPlace(String groupName) {
        String meetingPlace = "Meeting Place";
        return meetingPlace;
    }

    public String[] getMembers(String groupName) {
        String[] members = {};
        return members;
    }
}
