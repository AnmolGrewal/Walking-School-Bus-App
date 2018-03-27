package com.cmpt276.project.walkinggroupapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * This class model a user.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Integer birthYear;
    private Integer birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;
    private GpsLocation lastGpsLocation;

    private List<User> monitoredByUsers = new ArrayList<>();
    private List<User> monitorsUsers = new ArrayList<>();

    private List<WalkingGroup> memberOfGroups = new ArrayList<>();
    private List<WalkingGroup> leadsGroups = new ArrayList<>();

    private String href;

    @JsonIgnore
    private boolean isParent = false;

    @JsonIgnore
    private boolean isViewingChild = false;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    //Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //E-mail
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Birth Year
    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }

    //isParent
    public boolean isParent() {return isParent;}

    public void setIsParent(boolean isParent) {this.isParent = isParent;}

    //isViewingChild
    public boolean isViewingChild() {return isViewingChild;}

    public void setIsViewingChild(boolean isViewingChild) {this.isViewingChild = isViewingChild;}

    //Birth Month
    public Integer getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Integer birthMonth) {
        this.birthMonth = birthMonth;
    }

    //Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //CellPhone
    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) { this.cellPhone = cellPhone; }

    //HomePhone
    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    //Grade
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    //Teacher Name
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    //Emergency Contact Info
    public String getEmergencyContactInfo() {
        return emergencyContactInfo;
    }

    public void setEmergencyContactInfo(String emergencyContactInfo) { this.emergencyContactInfo = emergencyContactInfo; }

    //lastGpsLocation
    public GpsLocation getLastGpsLocation() {return lastGpsLocation;}

    public void setLastGpsLocation(GpsLocation lastGpsLocation) {this.lastGpsLocation = lastGpsLocation;}


    public List<User> getMonitoredByUsers() {
        return monitoredByUsers;
    }

    public void setMonitoredByUsers(List<User> monitoredByUsers) {
        this.monitoredByUsers = monitoredByUsers;
    }

    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }

    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

    public List<WalkingGroup> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<WalkingGroup> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public List<WalkingGroup> getLeadsGroups() {
        return leadsGroups;
    }

    public void setLeadsGroups(List<WalkingGroup> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthYear=" + birthYear +
                ", birthMonth=" + birthMonth +
                ", address='" + address + '\'' +
                ", cellPhone='" + cellPhone + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", grade='" + grade + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", emergencyContactInfo='" + emergencyContactInfo + '\'' +
                ", monitoredByUsers=" + monitoredByUsers +
                ", monitorsUsers=" + monitorsUsers +
                ", memberOfGroups=" + memberOfGroups +
                ", leadsGroups=" + leadsGroups +
                '}';
    }
}
