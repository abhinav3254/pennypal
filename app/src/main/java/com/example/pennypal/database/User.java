package com.example.pennypal.database;

import java.util.Date;

public class User {

    private Integer id;
    private String name;
    private String gender;
    private Date dob;

    private String userOccupation;

    public User() {
    }

    public User(Integer id, String name, String gender, Date dob, String userOccupation) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.userOccupation = userOccupation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public void setUserOccupation(String userOccupation) {
        this.userOccupation = userOccupation;
    }
}
