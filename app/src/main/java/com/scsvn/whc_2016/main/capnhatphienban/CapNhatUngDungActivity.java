package com.scsvn.whc_2016.main.capnhatphienban;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.SettingPref;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CapNhatUngDungActivity extends BaseActivity {
    public static final int REQUEST_CODE = 124;
    private static final String TAG = CapNhatUngDungActivity.class.getSimpleName();
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.ll_view)
    LinearLayout llView;
    private String apkPath;
    private String urlApk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_ung_dung);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        urlApk = String.format("http://%s/File_Download/WHC-2016.apk", SettingPref.getInfoNetwork(this)[0]);
        String path = Environment.getExternalStorageDirectory().getPath();
        apkPath = path + "/WHC-2016.apk"/*/storage/sdcard1/WHC-2016.apk*/;
    }

    @OnClick(R.id.bt_test_mail)
    public void sendMail() {
        //sendMail(progressBar);
    }


    @OnClick(R.id.bt_tai_pbm)
    public void taiPbm() {
        // String deviceModel = Build.MODEL.toString().trim().substring(0, 6);
        //  Log.e(TAG, "taiPbm: " + deviceModel);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            new File(apkPath).delete();
            new DownloadFileAsync().execute(urlApk);
        }
//        if (deviceModel.equalsIgnoreCase("TC700H")) {
//            boolean delete = new File(apkPath).delete();
//            if (delete)
//                new DownloadFileAsync().execute(urlApk);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE)
            if (grantResults.length != 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new File(apkPath).delete();
                new DownloadFileAsync().execute(urlApk);
                Log.e(TAG, "onRequestPermissionsResult: allow");
            } else
                Log.e(TAG, "onRequestPermissionsResult: denied");
    }

    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.e(TAG, "onResume: " + displayMetrics.widthPixels + " " + displayMetrics.heightPixels + " " + displayMetrics.density + " " + displayMetrics.scaledDensity + " ");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Const.isActivating = false;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    class DownloadFileAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            llView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            int count;
            String result = "";
            if (WifiHelper.isConnected(CapNhatUngDungActivity.this)) {
                try {
                    URL url = new URL(urls[0]);
                    URLConnection connect = url.openConnection();
                    connect.connect();

                    int lengthOfFile = connect.getContentLength();

                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(apkPath);

                    byte data[] = new byte[lengthOfFile];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress((int) ((total * 100) / lengthOfFile));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                result = getString(R.string.no_internet);
            }

            return result;

        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            llView.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("")) {
                File file = new File(apkPath);
                if (file.exists())
                    installApk();
            } else {
                Snackbar.make(llView, result, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
