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

    private EditText Hue1TextView;
    private TextView Hue2TextView;
    private Button colorBlendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_blending);

        Hue1TextView = findViewById(R.id.textView6);
        Hue2TextView = findViewById(R.id.textView7);

        colorBlendButton = findViewById(R.id.buttonColorBlend);
        colorBlendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHueBlendButtonClicked();
            }
        });
    }


    private void onHueBlendButtonClicked() {
        float blendRatio = (float) 0.5;
        String hue1 = Hue1TextView.getText().toString();
        if (hue1.isEmpty()) {
            Toast.makeText(this, "לא נבחר גוון ראשון", Toast.LENGTH_SHORT).show();
        }

        String hue2 = Hue2TextView.getText().toString();
        if (hue2.isEmpty()) {
            Toast.makeText(this, "לא נבחר גוון שני", Toast.LENGTH_SHORT).show();
        }

        if (isColorHexValue(hue1) && isColorHexValue(hue2)) {
            if (!(hue2.isEmpty() && hue1.isEmpty())) {
                int intColorForHue1 = Color.parseColor(hue1);
                int intColorForHue2 = Color.parseColor(hue2);
                int BlendedColorRes = ColorUtils.blendARGB(intColorForHue1, intColorForHue2, blendRatio);
                TextView colorBox = findViewById(R.id.blendedHuesRes);
                colorBox.setBackgroundColor(BlendedColorRes);
            }

        } else {
            Toast.makeText(this, "Invalid Hex Numbers", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isColorHexValue(String str) {
        boolean res;
        Pattern colorPattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
        Matcher m = colorPattern.matcher(str);
        res = m.matches();
        return (res);
    }
}
