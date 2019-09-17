package com.irlenglasses.diagnostic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.irlenglasses.BuildConfig;
import com.irlenglasses.R;
import com.irlenglasses.database.DatabaseAPI;
import com.irlenglasses.database.models.DatabaseCameraColor;
import com.utils.UtilitiesFiles;

import java.io.File;
import java.io.IOException;


public class CameraViewer extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final String redRGB = "F08080";
    private static final String greenRGB = "90EE90";
    private static final String blueRGB = "87CEFA";
    private static final String yellowRGB = "FFFF00";
    private static final String purpleRGB = "A5A1FF";
    private static final String grayRGB = "E6E6E6";
    private static final String pinkRGB = "FFDCF1";

    private ImageView imageView;
    private String mUserID = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        imageView = findViewById(R.id.imageView);

        mUserID = getIntent().getStringExtra("user_id");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                try {
                    currImageUriTakenFromCamera = createImageFile(this);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currImageUriTakenFromCamera);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Uri currImageUriTakenFromCamera = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    currImageUriTakenFromCamera = createImageFile(this);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currImageUriTakenFromCamera);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri imageUri = null;
            if (currImageUriTakenFromCamera != null) {
                imageUri = currImageUriTakenFromCamera;
                currImageUriTakenFromCamera = null;
            }
            if (imageUri != null) {
                loadImageFromExternalStorage(imageUri);
            }
        }
    }

    private static Uri createImageFile(Context context) {
        Uri uri = null;
        File currImageFileTakenFromCamera = null;
        try {
            currImageFileTakenFromCamera = UtilitiesFiles.createImageFile(context);
            if (currImageFileTakenFromCamera != null) {
                uri = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        currImageFileTakenFromCamera);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uri;
    }

    private void loadImageFromExternalStorage(final Uri uri) {
        UtilitiesFiles.loadImageInBackground(CameraViewer.this, uri, new UtilitiesFiles.ImageCompletionBlock() {
            @Override
            public void onComplete(final Bitmap image) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(image);
                    }
                });

                DatabaseAPI.getInstance().SetCameraColorListener(mUserID, new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DatabaseCameraColor cameraColor = dataSnapshot.getValue(DatabaseCameraColor.class);
                            if (cameraColor != null) {
                                String colorName = cameraColor.getColorName();
                                String alpha = "99";
                                String currColor = alpha + cameraColor.getColorID();

                                if (colorName != null && !colorName.isEmpty()) {
                                    switch (colorName) {
                                        case "None":
                                            break;
                                        case "Red":
                                            currColor = alpha + redRGB;
                                            break;
                                        case "Green":
                                            currColor = alpha + greenRGB;
                                            break;
                                        case "Blue":
                                            currColor = alpha + blueRGB;
                                            break;
                                        case "Yellow":
                                            currColor = alpha + yellowRGB;
                                            break;
                                        case "Purple":
                                            currColor = alpha + purpleRGB;
                                            break;
                                        case "Gray":
                                            currColor = alpha + grayRGB;
                                            break;
                                        case "Pink":
                                            currColor = alpha + pinkRGB;
                                            break;
                                    }
                                }
                                final int intColor = Color.parseColor("#" + currColor);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.getDrawable().setColorFilter(intColor, PorterDuff.Mode.MULTIPLY);
                                    }
                                });
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
