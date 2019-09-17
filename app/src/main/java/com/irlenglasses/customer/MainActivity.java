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

    Button btn;
    String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mUserID = getIntent().getStringExtra("user_id");

            btn = findViewById(R.id.filterCameraView);
            btn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CameraViewer.class);
                    intent.putExtra("user_id", mUserID);
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
}
