package com.scsvn.whc_2016.main.postiamge;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AttachmentParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by xuanloc on 11/7/2016.
 */
public class PostImage {
    private static final String TAG = PostImage.class.getSimpleName();
    private ProgressDialog dialog;
    private AppCompatActivity context;
    private View view;
    private String username;
    private Date dateCreate;
    private String originalFileName;
    private String md5FileName;
    private int fileSize;
    private String description;
    private String orderNumber;
    private boolean result = true;

    public PostImage(AppCompatActivity context, ProgressDialog dialog, View view, String description, String orderNumber) {
        this.dialog = dialog;
        this.context = context;
        this.view = view;
        this.description = description;
        this.orderNumber = orderNumber;
        username = LoginPref.getUsername(context);
    }

    public boolean uploadImage(final ArrayList<File> files, final int n) {
        int size = files.size();
        dialog.setMessage(String.format(Locale.getDefault(), "Đang tải lên %d/%d ...", size - n, size));
        if (n >= 0 && size > 0 && n < size) {
            File fileUpload = files.get(n);
            dateCreate = new Date(fileUpload.lastModified());
            originalFileName = fileUpload.getName();
            md5FileName = Utilities.md5(originalFileName) + ".jpg";
            fileSize = (int) fileUpload.length() / 1024;

            RequestBody requestBodyFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), fileUpload);
            RequestBody requestBodyFileName =
                    RequestBody.create(MediaType.parse("multipart/form-data"), md5FileName);
            RequestBody requestBodyDescription =
                    RequestBody.create(MediaType.parse("multipart/form-data"), description);

            Call<String> call = MyRetrofit.initRequest(context)
                    .uploadFile(requestBodyFile, requestBodyFileName, requestBodyDescription);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    updateAttachment(files, n);
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(context, "Upload thất bại", Toast.LENGTH_LONG).show();
                    result = false;
                    dialog.dismiss();
                }
            });
        }
        return result;
    }

    private void updateAttachment(final ArrayList<File> files, final int n) {
        result = false;
        if (!WifiHelper.isConnected(context)) {
            dialog.dismiss();
            return;
        }
        AttachmentParameter parameter = new AttachmentParameter(
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(dateCreate.getTime()),
                description,
                md5FileName,
                fileSize,
                0,
                username,
                0,
                3,
                orderNumber,
                originalFileName);
        MyRetrofit.initRequest(context)
                .setAttachment(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    if (n > 0) {
                        uploadImage(files, n - 1);
                    } else {
                        Toast.makeText(context, "Thành công", Toast.LENGTH_LONG).show();
                        result = true;
                        dialog.dismiss();

                        context.onBackPressed();
                    }
                } else {
                    Toast.makeText(context, "Upload thất bại", Toast.LENGTH_LONG).show();
                    result = false;
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                result = false;
                dialog.dismiss();
                RetrofitError.errorNoAction(context, t, TAG, view);
            }
        });
    }

}
