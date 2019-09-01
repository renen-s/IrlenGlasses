package com.irlenglasses.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.irlenglasses.R;
import com.irlenglasses.customer.MainActivity;
import com.irlenglasses.database.DatabaseAPI;
import com.irlenglasses.database.models.DatabaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText userNameEditTxt, passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEditTxt = findViewById(R.id.editText8);
        passwordEditText = findViewById(R.id.editText9);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });
    }

    private void onLoginButtonClicked() {
        final String userName = userNameEditTxt.getText().toString();
        if(userName.isEmpty()) {
            Toast.makeText(this, "לא הוזן שם משתמש", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = passwordEditText.getText().toString();
        if(password.isEmpty()) {
            Toast.makeText(this, "לא הוזנה סיסמא", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseAPI.getInstance().SetUserListener(password, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    DatabaseUser databaseUser = dataSnapshot.getValue(DatabaseUser.class);
                    if(databaseUser != null) {
                        if(userName.equalsIgnoreCase(databaseUser.getFirstName()+databaseUser.getLastName())) {
                            if(databaseUser.getType().equalsIgnoreCase("customer")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user_id", databaseUser.getUserID());
                                startActivity(intent);
                            }
                            else {
                                //diagnostic
                                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                                intent.putExtra("user_name", databaseUser.getFirstName() + " " + databaseUser.getLastName());
                                startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "שם משתמש לא נכון", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "סיסמא לא נכונה", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
