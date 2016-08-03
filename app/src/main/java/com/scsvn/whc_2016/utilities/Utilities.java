package com.scsvn.whc_2016.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.SettingPref;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Trần Xuân Lộc on 12/26/2015.
 */
public class Utilities {
    private static Snackbar snackbar;

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        return dialog;
    }

    public static void showBackIcon(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public static void hideKeyboard(AppCompatActivity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSnackbar(View view, String message) {
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private static Snackbar getSnackbar() {
        return snackbar;
    }

    public static String formatDate(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }


    public static String formatDate_ddMMyyyy(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_HHmm(String sDate) {
        if (sDate.substring(0, 4).equals("1900"))
            return "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static void showKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String formatDate_ddMMyy(String sDate) {
        SimpleDateFormat format;
        if (sDate.length() > 10)
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        else
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
            return newFormat.format(date).equalsIgnoreCase("01/01/00") ? "" : newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyyHHmm(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US);
            return newFormat.format(date).equalsIgnoreCase("01/01/00 00:00") ? "" : newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMHHmm(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.US);
            return newFormat.format(date).equalsIgnoreCase("01/01 00:00") ? "" : newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String formatDate_ddMMyyyyHHmm(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static long getMillisecondFromDate(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            Date date = format.parse(sDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String formatDateTime_yyyyMMddHHmmssFromMili(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String formatDateTime_yyyyMMddFromMili(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String format_ddMMHHmm(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM HH:mm", Locale.US);
        return format.format(calendar.getTime());
    }


    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.US);
        return format.format(calendar.getTime());
    }

    public static long getMinute(String date) {
        long timeWork;
        Calendar calendar = Calendar.getInstance();
        long timeCurrent = calendar.getTimeInMillis() > 0 ? calendar.getTimeInMillis() : 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            timeWork = format.parse(date).getTime();
            return (timeCurrent - timeWork) / 1000 / 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean notToday(String date) {
        Calendar ca = Calendar.getInstance();
        int dayToday = ca.get(Calendar.DAY_OF_MONTH);
        ca.setTimeInMillis(getMillisecondFromDate(date));
        return ca.get(Calendar.DAY_OF_MONTH) != dayToday;
    }

    public static String convertMinute(long minute) {
        String result = String.format(Locale.US, "%d phút", minute);
        if (minute > (23 * 60 + 59))
            result = String.format(Locale.US, "%d ngày %d giờ %d phút", minute / 60 / 24, (minute % (60 * 24)) / 60, (minute % (60 * 24)) % 60);
        else if (minute > 59)
            result = String.format(Locale.US, "%d giờ %d phút", minute / 60, minute % 60);
        return result;
    }

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void basicDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static String md5(final String s) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isEmpty(EditText view) {
        if (view.getText().toString().trim().length() == 0) {
            Snackbar.make(view, R.string.this_field_not_empty, Snackbar.LENGTH_LONG).show();
            view.requestFocus();
            return true;
        }
        return false;
    }

    public static String generateUrlImage(Context context, String imageName) {
        String BASE_URL = String.format("http://%s", SettingPref.getInfoNetwork(context)[0]);
        return String.format("%s/File_Attachments/%s", BASE_URL, imageName);
    }

    public static Picasso getPicasso(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        return new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(okHttpClient))
                .build();
    }

    public static String formatFloat(float number) {
        if (number == (int) number)
            return String.format(Locale.US, "%d", (int) number);
        return String.format("%s", number);
    }

    public static void setUnderLine(TextView view, String content) {
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        view.setText(spannableString);
    }
}
