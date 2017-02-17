package com.scsvn.whc_2016.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tranxuanloc on 2/25/2016.
 */
public class ResizeImage {
    private static final String TAG = ResizeImage.class.getSimpleName();

    public static String resizeImageFromFile(String filePath, int newWidth) throws FileNotFoundException {
        File file = new File(Environment.getExternalStorageDirectory(), Const.WHC_DIRECTORY);
        if (!file.exists())
            file.mkdir();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int originalWidth = options.outWidth;
        int inSampleSize = 8;
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
        String[] split = filePath.split(File.separator);
        filePath = String.format("%s%s%s%s%s",
                Environment.getExternalStorageDirectory(), File.separator,
                Const.WHC_DIRECTORY, File.separator,
                split[split.length - 1]);
        FileOutputStream out = new FileOutputStream(filePath);
        src.compress(Bitmap.CompressFormat.JPEG, 96, out);

        src.recycle();


        return filePath;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
