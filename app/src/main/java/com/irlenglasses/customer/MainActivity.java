package com.irlenglasses.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.irlenglasses.R;
import com.irlenglasses.diagnostic.CameraViewer;

public class MainActivity extends AppCompatActivity {

    Button navigateToCameraFilterButton, navigateToColorBlendButton;
    String loggedInUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            navigateToColorBlendButton = findViewById(R.id.navigate_to_color_blender_button);
            navigateToColorBlendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivityColorBlendingActivity();
                }
            });


            loggedInUserID = getIntent().getStringExtra("user_id");

            navigateToCameraFilterButton = findViewById(R.id.navigate_to_camera_filter_button);
            navigateToCameraFilterButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CameraViewer.class);
                    intent.putExtra("user_id", loggedInUserID);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void openActivityColorBlendingActivity() {
        Intent intent1 = new Intent(MainActivity.this, ColorBlendingActivity.class);
        startActivity(intent1);

    }
}