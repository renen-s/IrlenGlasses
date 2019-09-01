package com.irlenglasses.database;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irlenglasses.database.models.DatabaseCameraColor;
import com.irlenglasses.database.models.DatabaseUser;

public class DatabaseAPI {
    private static DatabaseAPI instance = null;

    public static DatabaseAPI getInstance() {
        if(instance == null) {
            instance = new DatabaseAPI();
        }
        return instance;
    }

    public void SaveUser(DatabaseUser user) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(user.getUserID())
                .setValue(user);
    }

    public void SaveCameraColor(DatabaseCameraColor cameraColor) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("colors")
                .child(cameraColor.getUserID())
                .setValue(cameraColor);
    }

    public void SetUserListener(String userID, ValueEventListener valueEventListener) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userID)
                .addListenerForSingleValueEvent(valueEventListener);
    }

    public void SetCameraColorListener(String userID, ValueEventListener valueEventListener) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("colors")
                .child(userID)
                .addListenerForSingleValueEvent(valueEventListener);
    }
}
