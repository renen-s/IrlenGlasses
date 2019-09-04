package com.irlenglasses.database.models;

public class DatabaseCameraColor {
    private String userID;
    private String colorName;
    private String colorID;

    public DatabaseCameraColor() {
        userID = "";
        colorID = "";
        colorName = "";
    }

    public DatabaseCameraColor(String userID, String colorID, String colorName) {
        this.userID = userID;
        this.colorID = colorID;
        this.colorName = colorName;
    }

    public String getColorID() {
        return colorID;
    }

    public String getColorName() {
        return colorName;
    }

    public String getUserID() {
        return userID;
    }

    public void setColorID(String colorID) {
        this.colorID = colorID;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
