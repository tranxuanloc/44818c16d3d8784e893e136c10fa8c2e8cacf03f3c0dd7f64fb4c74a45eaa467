package com.scsvn.whc_2016.main.detailphieu.chuphinh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AttachmentParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UploadFileActivity extends BaseActivity {
    private final String TAG = "UploadFileActivity";
    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.vv)
    VideoView vv;
    @Bind(R.id.progressBar)
    ProgressBar bar;
    @Bind(R.id.tv_percent_upload)
    TextView tvPercentage;
    @Bind(R.id.et_description_file_upload)
    EditText etDesc;
    private String path, orderNumber, originalName, fileName;
    private long fileCreate, fileSize;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(new File(path)));
        this.sendBroadcast(mediaScanIntent);
        orderNumber = intent.getStringExtra("order_number");
        originalName = intent.getStringExtra("original_file_name");
        fileName = intent.getStringExtra("file_name");
        fileCreate = intent.getLongExtra("file_date", -1);
        fileSize = intent.getLongExtra("file_size", -1);
        if (path != null)
            previewMedia(intent.getBooleanExtra("isImage", false));
        ChupHinhActivity.isUpdate = false;
    }

    public void upload(final View view) {
        Utilities.hideKeyboard(this);
        dialog = Utilities.getProgressDialog(this, "Đang upload file...");
        dialog.show();
        String description = etDesc.getText().toString();
        File file = new File(path);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody fileNameDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), file.getName());
        RequestBody requestBodyDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), description);

        Call<String> call = MyRetrofit.initRequest(this).uploadFile(requestBody, fileNameDescription, requestBodyDescription);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                updateData(view);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onFailure", t.getMessage());
                dialog.dismiss();
            }
        });
    }

    public void updateData(final View view) {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            //RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        AttachmentParameter parameter = new AttachmentParameter(
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(fileCreate),
                etDesc.getText().toString(),
                Utilities.md5(fileName) + ".jpg",
                (int) fileSize,
                0,
                LoginPref.getInfoUser(this, LoginPref.USERNAME),
                0,
                3,
                orderNumber, originalName);
        MyRetrofit.initRequest(this).setAttachment(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    Snackbar.make(view, "Upload thành công", Snackbar.LENGTH_LONG).show();
                    ChupHinhActivity.isUpdate = true;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                //RetrofitError.errorWithAction(UploadFileActivity.this,  t, TAG, view, action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attachment, menu);
        return true;
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = new File(path);
        file.delete();

    }

    public UploadFileActivity getInstance() {
        return UploadFileActivity.this;
    }
/*class AsyncTaskUpload extends AsyncTask<Void, Integer, String> {
        private long totalSize;

        @Override
        protected void onPostExecute(String result) {
            Snackbar.make(bar, result, Snackbar.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            bar.setProgress(values[0]);
            tvPercentage.setText(String.format("%d%", values[0]));
        }

        @Override
        protected String doInBackground(Void... params) {

            return uploadFile();
        }

        private String uploadFile() {
            String sResponse = "FAIL";
            MultipartUploadListener entity = new MultipartUploadListener(new MultipartUploadListener.ProgressListener() {
                @Override
                public void transferred(long num) {
                    publishProgress((int) (num / (float) totalSize) * 100);
                }
            });
            File sourceFile = new File(path);
            FileBody fileBody = new FileBody(sourceFile);
            entity.addPart("fileBody", fileBody);
            totalSize = entity.getContentLength();
            try {
                URL url = new URL("");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.addRequestProperty("Content-length", entity.getContentLength() + "");
                conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());
                OutputStream os = conn.getOutputStream();
                entity.writeTo(conn.getOutputStream());
                os.close();
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                    sResponse = "OK";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sResponse;
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_post)
            upload(iv);
        return true;
    }

    private void previewMedia(boolean isImage) {
        if (isImage) {
            iv.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            iv.setImageBitmap(bitmap);
        } else {
            vv.setVisibility(View.VISIBLE);
            vv.setVideoPath(path);
            vv.start();
        }
    }

}
