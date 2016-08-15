package com.scsvn.whc_2016.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by tranxuanloc on 3/18/2016.
 */
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    public View snackBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Float parserFloat(String target) {
        try {
            return Float.parseFloat(target);
        } catch (NumberFormatException ex) {
            return 0f;
        }
    }

    public Integer parserInt(String target) {
        try {
            return Integer.parseInt(target);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
