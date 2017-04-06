package com.scsvn.whc_2016.main.changepassword;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.ChangePasswordParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChangePasswordActivity extends BaseActivity {
    private final String TAG = ChangePasswordActivity.class.getSimpleName();
    @Bind(R.id.tv_changePassword_userName)
    TextView tvUserName;
    @Bind(R.id.tv_changePassword_realName)
    TextView tvRealName;
    @Bind(R.id.et_changePassword_currentPassword)
    EditText etCurrentPass;
    @Bind(R.id.et_changePassword_newPassword)
    EditText etNewPass;
    @Bind(R.id.et_changePassword_confirmPassword)
    EditText etConfirmNewPass;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initial();

    }

    private void initial() {
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        tvUserName.setText(String.format("User Name: %s", userName));
        String realName = LoginPref.getInfoUser(this, LoginPref.REAL_NAME);
        tvRealName.setText(realName);
    }


    @OnClick(R.id.bt_changePassword_send)
    public void send(View view) {
        if (isEmpty(etCurrentPass))
            return;
        if (isEmpty(etNewPass))
            return;
        if (isEmpty(etConfirmNewPass))
            return;
        if (!matchPass())
            return;
        ChangePasswordParameter parameter = new ChangePasswordParameter(userName, etCurrentPass.getText().toString(), etNewPass.getText().toString());
        changePassword(view, parameter);
    }

    public boolean isEmpty(EditText view) {
        if (view.getText().toString().length() == 0) {
            Snackbar.make(view, "Trường này không được để trống", Snackbar.LENGTH_LONG).show();
            view.requestFocus();
            return true;
        }
        return false;
    }

    public boolean matchPass() {
        if (etNewPass.getText().toString().equals(etConfirmNewPass.getText().toString()))
            return true;
        Snackbar.make(tvUserName, "Mật khẩu mới và mật khẩu xác nhận không trùng nhau", Snackbar.LENGTH_LONG).show();
        return false;

    }

    public void changePassword(final View view, ChangePasswordParameter parameter) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang gửi ...");
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).changePassword(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    Toast.makeText(ChangePasswordActivity.this, response.body(), Toast.LENGTH_LONG).show();
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(ChangePasswordActivity.this, t, TAG, view);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
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
}
