package com.jade.walkinggroupbus.walkingschoolbus.model;

import java.util.List;

public class Permission {
    private Long id;
    private String action;
    private PermissionStatus status;

    private UserInfo userA;
    private UserInfo userB;
    private Group groupG;
    private UserInfo requestingUser;

    private List<Authorizor> authorizors;
    private String message;

    private String href;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public PermissionStatus getStatus() {
        return status;
    }

    public void setStatus(PermissionStatus status) {
        this.status = status;
    }

    public UserInfo getUserA() {
        return userA;
    }

    public void setUserA(UserInfo userA) {
        this.userA = userA;
    }

    public UserInfo getUserB() {
        return userB;
    }

    public void setUserB(UserInfo userB) {
        this.userB = userB;
    }

    public Group getGroupG() {
        return groupG;
    }

    public void setGroupG(Group groupG) {
        this.groupG = groupG;
    }

    public UserInfo getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(UserInfo requestingUser) {
        this.requestingUser = requestingUser;
    }

    public List<Authorizor> getAuthorizors() {
        return authorizors;
    }

    public void setAuthorizors(List<Authorizor> authorizors) {
        this.authorizors = authorizors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
