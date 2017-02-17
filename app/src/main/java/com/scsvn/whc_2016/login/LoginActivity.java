package com.scsvn.whc_2016.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.MainActivity;
import com.scsvn.whc_2016.main.services.CheckActive;
import com.scsvn.whc_2016.main.services.NotificationServices;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.preferences.SettingPref;
import com.scsvn.whc_2016.preferences.VersionPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class LoginActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback {
    public static final String TAG = "LoginActivity";
    @Bind(R.id.username)
    EditText etUsername;
    @Bind(R.id.password)
    EditText etPassword;
    @Bind(R.id.acb_auto_sign_out)
    AppCompatCheckBox acbAutoSignOut;
    @Bind(R.id.acb_remember_sign_in_info)
    AppCompatCheckBox acbRememberSignInInfo;

    private int navigate;
    private int posRadioButton;
    private String ip = "192.168.104.29:810";
    private ActionBar actionBar;
    private String[] infoNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LoginPref.getInfoUser(this, LoginPref.USERNAME).equalsIgnoreCase("-1")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            navigate = 1;
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if ((actionBar = getSupportActionBar()) != null)
            actionBar.setSubtitle(String.format("%s - %s", VersionPref.getVersion(LoginActivity.this), VersionPref.getVersionDate(LoginActivity.this)));
        infoNetwork = SettingPref.getInfoNetwork(this);
        ip = infoNetwork[0];
        posRadioButton = Integer.parseInt(infoNetwork[1]);
        boolean autoSignOut = LoginPref.isAutoSignOut(this);
        acbAutoSignOut.setChecked(autoSignOut);
    }

    @OnClick(R.id.sign_in)
    public void signIn(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang đăng nhập...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        com.scsvn.whc_2016.retrofit.LoginInfo info = new com.scsvn.whc_2016.retrofit.LoginInfo(etUsername.getText().toString(), etPassword.getText().toString());
        MyRetrofit.initRequest(this).signIn(info).enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Response<LoginInfo> response, Retrofit retrofit) {
                LoginInfo object = response.body();
                Log.e(TAG, "onResponse: " + new Gson().toJson(object));
                if (response.isSuccess() && object != null) {
                    if (object.getStatus().equalsIgnoreCase("OK")) {
                        String username = object.getUsername();
                        String positionGroup = object.getPositionGroup();
                        String realName = object.getRealName();
                        int warehouseID = object.getWarehouseID();
                        boolean isOutSide = object.isAllowOutside();
                        if (isOutSide || posRadioButton == 0) {
                            LoginPref.putInfoUser(LoginActivity.this, username, positionGroup, realName, warehouseID, acbAutoSignOut.isChecked());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            navigate = 1;
                            startService(new Intent(LoginActivity.this, NotificationServices.class));
                            if (acbAutoSignOut.isChecked())
                                startService(new Intent(LoginActivity.this, CheckActive.class));
                        } else
                            Snackbar.make(view, "Bạn không thể đăng nhập bên ngoài mạng nội bộ", Snackbar.LENGTH_LONG).show();
                    } else
                        Snackbar.make(view, "Tên tài khoản hoặc mật khẩu không chính xác", Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(LoginActivity.this, t, TAG, view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting)
            setNetwork();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if (navigate == 1)
            finish();
        super.onStop();
    }

    public void setNetwork() {
        infoNetwork = SettingPref.getInfoNetwork(this);
        ip = infoNetwork[0];
        posRadioButton = Integer.parseInt(infoNetwork[1]);

        final View view = View.inflate(this, R.layout.setting_network, null);
        final EditText ipManual = (EditText) view.findViewById(R.id.et_manual_ip);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rb_group);
        if (infoNetwork[1].equalsIgnoreCase("1"))
            ((RadioButton) radioGroup.findViewById(R.id.rb_global)).setChecked(true);
        else if (infoNetwork[1].equalsIgnoreCase("2")) {
            ((RadioButton) radioGroup.findViewById(R.id.rb_manual)).setChecked(true);
            ipManual.setVisibility(View.VISIBLE);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_manual)
                    ipManual.setVisibility(View.VISIBLE);
                else
                    ipManual.setVisibility(View.GONE);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utilities.hideKeyboard(LoginActivity.this);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utilities.hideKeyboard(LoginActivity.this);
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_local) {
                    posRadioButton = 0;
                    ip = "192.168.104.29:810";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_global) {
                    posRadioButton = 1;
                    ip = "115.78.13.10:810";
                } else {
                    String ipM = ipManual.getText().toString().trim();
                    if (ipM.length() > 0) {
                        posRadioButton = 2;
                        ip = ipM;
                    } else {
                        Snackbar.make(view, "Bạn phải điền một địa chỉ IP", Snackbar.LENGTH_LONG).show();
                    }
                }
                SettingPref.setInfoNetwork(LoginActivity.this, ip, posRadioButton);
            }
        }).setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}
