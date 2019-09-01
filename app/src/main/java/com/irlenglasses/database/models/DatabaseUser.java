package com.irlenglasses.database.models;

public class DatabaseUser {
    private String type;
    private String firstName;
    private String lastName;
    private String userID;

    public DatabaseUser() {
        type = "";
        firstName = "";
        lastName = "";
        userID = "";
    }

    public DatabaseUser(String type, String firstName, String lastName, String userID) {
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getType() {
        return type;
    }

    public String getUserID() {
        return userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
