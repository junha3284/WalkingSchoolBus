package com.jade.walkinggroupbus.walkingschoolbus.model;

import java.util.ArrayList;
import java.util.List;

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


    public List<String> getNames() {
        List<String> groupNames = new ArrayList<>();
        return groupNames;
    }

    public List<Float> getCoordinates(String groupName) {
        List<Float> coordinates = new ArrayList<>();
        coordinates.add(new Float(0));
        coordinates.add(new Float(0));
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

    public List<String> getMembers(String groupName) {
        List<String> members = new ArrayList<>();
        return members;
    }
}
