package com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilitiesFiles {
    public interface ImageCompletionBlock {
        public void onComplete(Bitmap image);
    }

    public static void loadImageInBackground(final Context context, final Uri uri, final UtilitiesFiles.ImageCompletionBlock onImageFetchedFormStorage) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap selectedImage = UtilitiesFiles.getImageBitmap(context, uri);
                    if (onImageFetchedFormStorage != null) {
                        onImageFetchedFormStorage.onComplete(selectedImage);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static Bitmap getImageBitmap(Context context, Uri uri) throws FileNotFoundException {
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream, null, options);
        if (selectedImage != null) {
            selectedImage = UtilitiesFiles.checkRotation(context, uri, selectedImage);
        }

        return selectedImage;
    }

    public static Bitmap rotateBitmap(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap checkRotation(Context context, Uri imageUri, Bitmap bitmap) {
        ExifInterface ei = null;
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (imageStream != null) {
                    ei = new ExifInterface(imageStream);
                }
            } else {
                ei = new ExifInterface(imageUri.getPath());
            }
            if (ei != null) {
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bitmap = rotateBitmap(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bitmap = rotateBitmap(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bitmap = rotateBitmap(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
