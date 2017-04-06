package com.scsvn.whc_2016.main;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.scsvn.whc_2016.BuildConfig;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.login.LoginActivity;
import com.scsvn.whc_2016.main.services.CheckActive;
import com.scsvn.whc_2016.main.services.NotificationInfo;
import com.scsvn.whc_2016.main.services.NotificationServices;
import com.scsvn.whc_2016.main.services.UpdateLocationServices;
import com.scsvn.whc_2016.main.unitech.UnitechConst;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.preferences.SettingPref;
import com.scsvn.whc_2016.preferences.VersionPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NotificationParameter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 123, RESOLUTION_RESULT = 124;
    public static AppCompatActivity activity;
    @Bind(R.id.gridview)
    GridView gridView;
    private int posRadioButton;
    private String ip;
    private String userName;
    private MenuAdapter adapter;
    private int indexQHSE = -1, indexUpdateVersion = -1, indexQH = -1, indexPhieuCuaToi = -1, indexTruckContainer = -1;
    private SwitchCompat switchCompat;
    private int navigate;
    private int amountQHSE, amountQH, amountPhieuCuaToi, amountTruckContainer;
    private int sizeData;
    private boolean isAcceptChangeDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        String permission = LoginPref.getInfoUser(this, LoginPref.POSITION_GROUP);
        String realName = LoginPref.getInfoUser(this, LoginPref.REAL_NAME);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(realName);

        getDataByPermission(permission);
        sizeData = DataUser.data.size();
        adapter = new MenuAdapter(this, DataUser.data);
        gridView.setAdapter(adapter);
        if (!BuildConfig.VERSION_NAME.equalsIgnoreCase(VersionPref.getVersion(MainActivity.this)))
            VersionPref.setVersion(MainActivity.this, BuildConfig.VERSION_NAME);
    }

    private void getDataByPermission(String permission) {
        DataUser dataUser = new DataUser(this);
        String sCase = permission.toLowerCase();
        if (sCase.equalsIgnoreCase(Const.MANAGER)) {
            indexPhieuCuaToi = 1;
            indexQHSE = 5;
            indexTruckContainer = 8;
            indexQH = 19;
            indexUpdateVersion = 31;
            dataUser.manager();
        } else if (sCase.equalsIgnoreCase(Const.SUPERVISOR)) {
            indexPhieuCuaToi = 1;
            indexQHSE = 5;
            indexTruckContainer = 8;
            indexQH = 19;
            indexUpdateVersion = 26;
            dataUser.supervisor();
        } else if (sCase.equalsIgnoreCase(Const.PRODUCT_CHECKER)) {
            indexPhieuCuaToi = 1;
            indexQHSE = 3;
            indexUpdateVersion = 12;
            dataUser.productChecker();
        } else if (sCase.equalsIgnoreCase(Const.FORKLIFT_DRIVER)) {
            indexPhieuCuaToi = 1;
            indexQHSE = 5;
            indexTruckContainer = 8;
            indexUpdateVersion = 20;
            dataUser.forkliftDriver();
        } else if (sCase.equalsIgnoreCase(Const.NO_POSITION)) {
            indexUpdateVersion = 3;
            dataUser.user();
        } else if (sCase.equalsIgnoreCase(Const.LOWER_USER)) {
            indexUpdateVersion = 0;
            dataUser.lowerUser();
        } else if (sCase.equalsIgnoreCase(Const.TECHNICAL)) {
            indexQH = 1;
            indexQHSE = 0;
            indexUpdateVersion = 8;
            dataUser.technical();
        } else if (sCase.equalsIgnoreCase(Const.GROUP_DOCUMENTS)) {
            indexPhieuCuaToi = 1;
            indexQH = 14;
            indexQHSE = 5;
            indexUpdateVersion = 18;
            dataUser.documents();
        } else {
            indexUpdateVersion = 0;
            dataUser.lowerUser();
        }
    }

    private void getNumberQHSENewAssign() {
        MyRetrofit.initRequest(this).getNotification(new NotificationParameter(userName)).enqueue(new Callback<List<NotificationInfo>>() {
            @Override
            public void onResponse(Response<List<NotificationInfo>> response, Retrofit retrofit) {
                amountPhieuCuaToi = amountQH = amountQHSE = amountTruckContainer = 0;
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    for (NotificationInfo info : response.body()) {
                        if (info.getType().equalsIgnoreCase("QH") && indexQH != -1)
                            amountQH += info.getNotiQty();
                        else if (info.getType().equalsIgnoreCase("QHSE") && indexQHSE != -1)
                            amountQHSE += info.getNotiQty();
                        else if ((info.getType().equalsIgnoreCase("CO") || info.getType().equalsIgnoreCase("TR")) && indexTruckContainer != -1)
                            amountTruckContainer += info.getNotiQty();
                        else if (info.getType().equalsIgnoreCase("MT")) {

                        } else if (indexPhieuCuaToi != -1)
                            amountPhieuCuaToi += info.getNotiQty();
                    }
                    if (amountPhieuCuaToi != 0 && indexPhieuCuaToi < sizeData)
                        DataUser.data.get(indexPhieuCuaToi).setNumber((short) amountPhieuCuaToi);
                    if (amountQH != 0 && indexQH < sizeData)
                        DataUser.data.get(indexQH).setNumber((short) amountQH);
                    if (amountQHSE != 0 && indexQHSE < sizeData)
                        DataUser.data.get(indexQHSE).setNumber((short) amountQHSE);
                    if (amountTruckContainer != 0 && indexTruckContainer < sizeData)
                        DataUser.data.get(indexTruckContainer).setNumber((short) amountTruckContainer);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void getVersionWHC() {
        MyRetrofit.initRequest(this).getWHCVersion().enqueue(new Callback<List<VersionInfo>>() {
            @Override
            public void onResponse(Response<List<VersionInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    VersionInfo info = response.body().get(0);
                    if (!BuildConfig.VERSION_NAME.equalsIgnoreCase(info.getVersionNo()) && indexUpdateVersion < sizeData) {
                        DataUser.data.get(indexUpdateVersion).setNumber((short) 1);
                        adapter.notifyDataSetChanged();
                    } else {
                        VersionPref.setVersionDate(MainActivity.this, info.getVersionDate());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void getTimeServer() {
        isAcceptChangeDateTime = false;

        MyRetrofit.initRequest(this).getTimeServer().enqueue(new Callback<List<TimeServer>>() {
            @Override
            public void onResponse(Response<List<TimeServer>> response, Retrofit retrofit) {
                List<TimeServer> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    Date date = Calendar.getInstance().getTime();
                    long l = date.getTime() - body.get(0).getTime();
                    if (Math.abs(l) >= 3 * 60 * 1000) {
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Thời gian trên thiết bị không khớp với thời gian trên hệ thống. Vui lòng cập nhật lại thời gian trên thiết bị.")
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isAcceptChangeDateTime = true;
                                        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .create();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (!isAcceptChangeDateTime)
                                    getTimeServer();
                            }
                        });
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sign_out)
            signOut();
        else if (itemId == R.id.action_setting)
            setNetwork();
        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        updateSignOut();
        LoginPref.resetInfoUserByUser(this);
        startActivity(new Intent(this, LoginActivity.class));
        navigate = 1;
        stopServiceAndRemoveNotify();
        stopCheckActive();
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)
            stopService(new Intent(this, UpdateLocationServices.class));

    }

    private void updateSignOut() {
        if (!WifiHelper.isConnected(this))
            return;
        MyRetrofit.initRequest(this).signOut(userName).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void stopCheckActive() {
        stopService(new Intent(this, CheckActive.class));
        Const.isActivating = false;
        Const.timePauseActive = 0;
    }

    private void stopServiceAndRemoveNotify() {
        stopService(new Intent(this, NotificationServices.class));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (int id : Const.arrayListIDNotify) {
            notificationManager.cancel(id);
        }
        Const.arrayListIDNotify.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (manager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                SettingPref.setAccessLocation(this, false);
                stopService(new Intent(MainActivity.this, UpdateLocationServices.class));
            }
        }

        Const.isActivating = true;
        getNumberQHSENewAssign();
        getVersionWHC();
        boolean unitech = UnitechConst.isUnitech();
        if (unitech)
            startScanUnitech();

        getTimeServer();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        if (navigate == 1)
            finish();
        super.onStop();
    }

    private void startScanUnitech() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("close", true);
        Intent mIntent = new Intent().setAction(UnitechConst.START_SCAN_SERVICE).putExtras(bundle);
        sendBroadcast(mIntent);
    }

    public void setNetwork() {
        final View view = View.inflate(this, R.layout.setting_network_location, null);
        boolean accessLocation = SettingPref.getAccessLocation(this);
        switchCompat = (SwitchCompat) view.findViewById(R.id.sc_access_location);
        switchCompat.setChecked(accessLocation);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
                if (isChecked) {
                    if (code == ConnectionResult.SUCCESS)
                        requestLocation();
                    else {
                        Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, code, 125);
                        if (dialog != null) {
                            dialog.show();
                        }
                    }
                } else {
                    if (code == ConnectionResult.SUCCESS)
                        stopService(new Intent(MainActivity.this, UpdateLocationServices.class));
                }
            }
        });
        final EditText ipManual = (EditText) view.findViewById(R.id.et_manual_ip);
        String[] infoNetwork = SettingPref.getInfoNetwork(this);
        ip = infoNetwork[0];
        posRadioButton = Integer.parseInt(infoNetwork[1]);
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
                Utilities.hideKeyboard(MainActivity.this);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utilities.hideKeyboard(MainActivity.this);
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
                SettingPref.setInfoNetwork(MainActivity.this, ip, posRadioButton);
            }
        }).setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else
            startService(new Intent(this, UpdateLocationServices.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE)
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(this, UpdateLocationServices.class));
            } else {
                switchCompat.setChecked(false);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESOLUTION_RESULT) {
            stopService(new Intent(this, UpdateLocationServices.class));
            if (resultCode == RESULT_OK) {
                startService(new Intent(this, UpdateLocationServices.class));
            } else
                switchCompat.setChecked(false);
        }

    }
}
