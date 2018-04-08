package com.jade.walkinggroupbus.walkingschoolbus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 *  class for storing group members in which at least one member need to approve
 *        and status of decision of the group
 *        and who made the decision in the group
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authorizor {

    List<UserInfo> users;
    PermissionStatus status;
    UserInfo whoApprovedOrDenied;

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    public PermissionStatus getStatus() {
        return status;
    }

    public void setStatus(PermissionStatus status) {
        this.status = status;
    }

    public UserInfo getWhoApprovedOrDenied() {
        return whoApprovedOrDenied;
    }

    public void setWhoApprovedOrDenied(UserInfo whoApprovedOrDenied) {
        this.whoApprovedOrDenied = whoApprovedOrDenied;
    }

    public String toStringForList() {
        String detail = "\n" + status;
        if (whoApprovedOrDenied != null) {
            detail = "Name: " + whoApprovedOrDenied.getName() + "\n" +
                     "Status: " + status;
        }
        return detail;
    }
}
