package com.scsvn.whc_2016.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

public class DeviceInfoActivity extends BaseActivity {

    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        Utilities.showBackIcon(getSupportActionBar());

        androidId = Utilities.getAndroidID(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        StringBuilder builder = new StringBuilder();
        builder.append("Resolution: ").append(width).append(" x ").append(height).append(" px").append("\n")
                .append("Version: ").append(Build.VERSION.RELEASE);
        TextView tvInfo = (TextView) findViewById(R.id.tv_device_info);
        assert tvInfo != null;
        tvInfo.setText(builder.toString());
        TextView tvAndroidID = (TextView) findViewById(R.id.tv_android_id);
        assert tvAndroidID != null;
        tvAndroidID.setText(androidId);
    }

    public void textSelect(final View view) {
        view.setSelected(true);
        PopupMenu menu = new PopupMenu(this, view);
        MenuItem itemCopy = menu.getMenu().add("Copy");
        itemCopy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Android Id", androidId);
                clipboard.setPrimaryClip(clipData);
                return true;
            }
        });

        menu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                view.setSelected(false);
            }
        });
        menu.show();
    }
}
