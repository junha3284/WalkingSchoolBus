package com.jade.walkinggroupbus.walkingschoolbus.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * class for storing + accessing + modifying all user related info
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    private Long id;

    private String email;
    private String password;
    private String name;

    private String birthYear;
    private String birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;

    // fields for Gamification
    private int currentPoints;
    private int totalPointsEarned;
    private String customJson;

    private List<UserInfo> monitoredByUsers = new ArrayList<>();
    private List<UserInfo> monitorsUsers = new ArrayList<>();
    private List<Group> memberOfGroups = new ArrayList<>();   // <-- TO BE IMPLEMENTED
    private List<Group> leadsGroups = new ArrayList<>();

    private List<Message> unreadMessages = new ArrayList<>();
    private List<Message> readMessages = new ArrayList<>();

    private GPSLocation lastGpsLocation;

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
        } else {
            return instance;
        }
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    // Manage User functions
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

    public GPSLocation getLastGpsLocation() {
        return lastGpsLocation;
    }

    public void setLastGpsLocation(GPSLocation lastGpsLocation) {
        this.lastGpsLocation = lastGpsLocation;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    public List<Message> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(List<Message> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public List<Message> getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(List<Message> readMessages) {
        this.readMessages = readMessages;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }

    public int getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(int totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }

    public String getCustomJson() {
        return customJson;
    }

    public void setCustomJson(String customJson) {
        this.customJson = customJson;
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
        setBirthYear(user.getBirthYear());
        setBirthMonth(user.getBirthMonth());
        setAddress(user.getAddress());
        setCellPhone(user.getCellPhone());
        setHomePhone(user.getHomePhone());
        setGrade(user.getGrade());
        setTeacherName(user.getTeacherName());
        setEmergencyContactInfo(user.getEmergencyContactInfo());
        setUnreadMessages(user.getUnreadMessages());
        setReadMessages(user.getReadMessages());
        setLastGpsLocation(user.getLastGpsLocation());
        setCurrentPoints(user.getCurrentPoints());
        setTotalPointsEarned(user.getTotalPointsEarned());
        setCustomJson(user.getCustomJson());
    }

    // UI for Logcat messages.
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthYear=" + birthYear + '\'' +
                ", birthMonth=" + birthMonth + '\'' +
                ", address=" + address + '\'' +
                ", cellPhone=" + cellPhone + '\'' +
                ", homePhone=" + homePhone + '\'' +
                ", grade=" + grade + '\'' +
                ", teacherName=" + teacherName + '\'' +
                ", emergencyContactInfo=" + emergencyContactInfo + '\'' +
                ", monitoredByUsers=" + monitoredByUsers + '\'' +
                ", monitorsUsers=" + monitorsUsers + '\'' +
                ", membersOfGroups" + memberOfGroups + '\'' +
                ", leadsGroups=" + leadsGroups + '\'' +
                ", currentPoints=" + currentPoints + '\'' +
                ", totalPointsEarned=" + totalPointsEarned + '\'' +
                ", customJson=" + customJson + '\'' +
                '}';
    }


    public String toStringForList() {
        return "Name: '" + name + '\'' + "\n" +
                "Email: '" + email;
    }

    public String toStringContactInfo() {
        String contactInfo = "Name: " + name;
        if (email != null) {
            contactInfo = contactInfo + "\nEmail: " + email;
        }
        if (cellPhone != null) {
            contactInfo = contactInfo + "\nCell Phone: " + cellPhone;
        }
        if (homePhone != null) {
            contactInfo = contactInfo + "\nHome Phone: " + homePhone;
        }
        return contactInfo;
    }
}

