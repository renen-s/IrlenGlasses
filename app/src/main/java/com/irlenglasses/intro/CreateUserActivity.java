package com.irlenglasses.intro;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.annotations.NotNull;
import com.irlenglasses.R;
import com.irlenglasses.database.DatabaseAPI;
import com.irlenglasses.database.models.DatabaseCameraColor;
import com.irlenglasses.database.models.DatabaseUser;

public class CreateUserActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    private EditText firstNameEditText, lastNameEditText, idEditText;
    RadioGroup radioGroup;
    private Spinner colorSpinner;
    private String currentColorValue = "None";
    private TextView greetingTextView;
    private EditText colorRGBEditText;

    enum UserType {diagnostic, customer}
    private UserType userType = UserType.customer;
    String[] colors = {"None", "Red", "Green", "Blue", "Yellow", "Purple", "Gray", "Pink"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        firstNameEditText = findViewById(R.id.first_name_input);
        lastNameEditText = findViewById(R.id.last_name_input);
        idEditText = findViewById(R.id.password_id_number_input);
        colorSpinner = findViewById(R.id.spinner);
        greetingTextView = findViewById(R.id.greeting_text_input);
        radioGroup = findViewById(R.id.radioGroup);
        colorRGBEditText = findViewById(R.id.colorRGBEditText);

        String name = getIntent().getStringExtra("user_name");
        greetingTextView.setText(getString(R.string.greeting_text_prefix) + name);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.radioButton) {
                    userType = UserType.diagnostic;
                    colorRGBEditText.setEnabled(false);
                    colorRGBEditText.setFocusable(false);
                    colorSpinner.setEnabled(false);
                    colorSpinner.setClickable(false);
                } else {
                    //customer
                    userType = UserType.customer;
                    colorSpinner.setEnabled(true);
                    colorSpinner.setClickable(true);
                    colorRGBEditText.setEnabled(true);
                    colorRGBEditText.setFocusable(true);


                }

            }
        });


        colorSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currentColorValue = colors[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void onCreateUserButtonClicked() {
        String firstName = firstNameEditText.getText().toString();
        if (firstName.isEmpty()) {
            Toast.makeText(this, "לא הוזן שם פרטי", Toast.LENGTH_SHORT).show();
            return;
        }
        String lastName = lastNameEditText.getText().toString();
        if (lastName.isEmpty()) {
            Toast.makeText(this, "לא הוזן שם משפחה", Toast.LENGTH_SHORT).show();
            return;
        }
        String userID = idEditText.getText().toString();
        if (userID.isEmpty()) {
            Toast.makeText(this, "לא הוזנה תעודת זהות", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseCameraColor cameraColor = new DatabaseCameraColor();
        cameraColor.setUserID(userID);
        if (currentColorValue.equalsIgnoreCase("None") && userType.equals(UserType.customer)) {
            if (colorRGBEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "לא נבחר צבע", Toast.LENGTH_SHORT).show();
                return;
            }
            cameraColor.setColorID(colorRGBEditText.getText().toString());
        } else {
            if (userType.equals(UserType.customer))
                cameraColor.setColorName(currentColorValue);
            else
                currentColorValue = "None";
        }

        DatabaseAPI.getInstance().SaveCameraColor(cameraColor);

        String type = userType == UserType.customer ? "customer" : "diagnostic";
        DatabaseUser databaseUser = new DatabaseUser(type, firstName, lastName, userID);
        DatabaseAPI.getInstance().SaveUser(databaseUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_user:
                onCreateUserButtonClicked();
                finish();
                break;
        }

        return true;
    }
}