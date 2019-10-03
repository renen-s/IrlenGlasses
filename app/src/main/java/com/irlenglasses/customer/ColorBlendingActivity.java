package com.irlenglasses.customer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import com.irlenglasses.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ColorBlendingActivity extends AppCompatActivity {

    private EditText hue1TextInput, hue2TextInupt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_blending);

        hue1TextInput = findViewById(R.id.first_hue_input);
        hue2TextInupt = findViewById(R.id.second_hue_input);

        Button colorBlendButton = findViewById(R.id.button_activate_colorblend);
        colorBlendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHueBlendButtonClicked();
            }
        });
    }


    private void onHueBlendButtonClicked() {
        float blendRatio = (float) 0.5;
        String hue1 = hue1TextInput.getText().toString();
        if (hue1.isEmpty()) {
            Toast.makeText(this, "לא נבחר גוון ראשון", Toast.LENGTH_SHORT).show();
        }

        String hue2 = hue2TextInupt.getText().toString();
        if (hue2.isEmpty()) {
            Toast.makeText(this, "לא נבחר גוון שני", Toast.LENGTH_SHORT).show();
        }

        if (isColorValidHexadecimalValue(hue1) && isColorValidHexadecimalValue(hue2)) {

            int intColorForHue1 = Color.parseColor(hue1);
            int intColorForHue2 = Color.parseColor(hue2);
            int BlendedColorRes = ColorUtils.blendARGB(intColorForHue1, intColorForHue2, blendRatio);
            TextView colorBox = findViewById(R.id.res_blended_color_box);
            colorBox.setBackgroundColor(BlendedColorRes);

        } else {
            Toast.makeText(this, "מספר הקסדצימלי לא תקין", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isColorValidHexadecimalValue(String str) {
        boolean res;
        Pattern colorPattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
        Matcher m = colorPattern.matcher(str);
        res = m.matches();
        return (res);
    }
}
