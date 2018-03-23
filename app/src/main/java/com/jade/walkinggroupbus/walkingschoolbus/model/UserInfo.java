package com.jade.walkinggroupbus.walkingschoolbus.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    private Long id;
    private String email;

    private String password;
    private String name;
    private String birthYear;
    private String birthMonth;
    private String cellPhone;
    private String homePhone;
    private String address;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;

    private List<UserInfo> monitoredByUsers = new ArrayList<>();
    private List<UserInfo> monitorsUsers = new ArrayList<>();
    private List<Group> memberOfGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED
    private List<Group> leadsGroups = new ArrayList<>();

    private String href;

    // for child
    private boolean manageChild;

    // Singleton Implementation
    private static UserInfo instance;

    private UserInfo() {
        // private to prevent public instantiation
        manageChild = false;
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



    public boolean managingChild() {
        return manageChild;
    }

    public void startManagingChild() {
        manageChild = true;
    }

    public void stopManagingChild() {
        manageChild = false;
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setUserInfo(UserInfo user) {
        setEmail(user.getEmail());
        setHref(user.getHref());
        setId(user.getId());
        setLeadsGroups(user.getLeadsGroups());
        setMemberOfGroups(user.getMemberOfGroups());
        setMonitoredByUsers(user.getMonitoredByUsers());
        setMonitorsUsers(user.getMonitorsUsers());
        setName(user.getName());
        setPassword(user.getPassword());
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

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getEmergencyContactInfo() {
        return emergencyContactInfo;
    }

    public void setEmergencyContactInfo(String emergencyContactInfo) {
        this.emergencyContactInfo = emergencyContactInfo;
    }
}

