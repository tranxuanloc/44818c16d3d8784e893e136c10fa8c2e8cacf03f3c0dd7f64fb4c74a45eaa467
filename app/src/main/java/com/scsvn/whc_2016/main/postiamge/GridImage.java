package com.scsvn.whc_2016.main.postiamge;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.ResizeImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by xuanloc on 11/9/2016.
 */
public class GridImage {

    public static final String TAG = GridImage.class.getSimpleName();

    public static ArrayList<File> updateGridImage(Context context, Intent data, ThumbImageAdapter adapter) {
        ArrayList<File> files = new ArrayList<>();
        ArrayList<Thumb> valuesThumb = new ArrayList<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Const.SAMPLE_SIZE;

        Uri uriImage = data.getData();
        ClipData clipData = data.getClipData();

        if (uriImage != null) {
            String path = ImageUtils.getPath(context, uriImage);

            if (path == null || path.length() == 0) {
                Toast.makeText(context, "Không thể tìm đúng đường dẫn của hình ảnh đã chọn", Toast.LENGTH_LONG).show();
            } else {
                try {
                    path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                files.add(new File(path));
                Bitmap thumb = BitmapFactory.decodeFile(path, options);
                valuesThumb.add(new Thumb(thumb));
            }
            adapter.clear();
            adapter.addAll(valuesThumb);

        } else if (clipData != null) {
            for (int i = 0, n = clipData.getItemCount(); i < n; i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                String path = ImageUtils.getPath(context, uri);
                if (path == null || path.length() == 0) {
                    Toast.makeText(context, "Không thể tìm đúng đường dẫn của hình ảnh đã chọn", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    files.add(new File(path));

                    Bitmap thumb = BitmapFactory.decodeFile(path, options);
                    valuesThumb.add(new Thumb(thumb));
                }
            }
            adapter.clear();
            adapter.addAll(valuesThumb);
        } else {
            Toast.makeText(context, "Không thể nhận được hình ảnh đã chọn.", Toast.LENGTH_LONG).show();
        }
        return files;
    }

    public static ArrayList<File> updateGridImage(String path, ThumbImageAdapter adapter) {
        ArrayList<File> files = new ArrayList<>();
        ArrayList<Thumb> valuesThumb = new ArrayList<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Const.SAMPLE_SIZE;

        try {
            path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        files.add(new File(path));

        Bitmap thumb = BitmapFactory.decodeFile(path, options);
        valuesThumb.add(new Thumb(thumb));

        adapter.clear();
        adapter.addAll(valuesThumb);

        return files;
    }
}
