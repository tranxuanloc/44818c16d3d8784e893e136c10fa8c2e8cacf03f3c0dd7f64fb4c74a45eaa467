package com.scsvn.whc_2016.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tranxuanloc on 2/25/2016.
 */
public class ResizeImage {
    private static final String TAG = ResizeImage.class.getSimpleName();

    public static int resizeImageFromFile(String filePath, int newWidth) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;
        Log.e(TAG, "resizeImageFromFile: " + originalWidth + " " + originalHeight);
        int inSampleSize = 1;
        if (originalWidth > newWidth)
            inSampleSize = originalWidth / newWidth;
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = inSampleSize;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap src = BitmapFactory.decodeFile(filePath, options);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei != null ? ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED) : 0;
        Log.e(TAG, "resizeImageFromFile: " + orientation);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                src = rotateImage(src, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                src = rotateImage(src, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                src = rotateImage(src, 270);
                break;
        }
        FileOutputStream out = new FileOutputStream(filePath);
        boolean compressResult = src.compress(Bitmap.CompressFormat.JPEG, 96, out);
        Log.e("ResizeImage", "resizeImageFromFile: " + compressResult + inSampleSize);

        src.recycle();


        return inSampleSize;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
