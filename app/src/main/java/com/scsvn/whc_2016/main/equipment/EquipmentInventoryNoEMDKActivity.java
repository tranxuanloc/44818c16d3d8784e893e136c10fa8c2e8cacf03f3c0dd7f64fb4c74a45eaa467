package com.scsvn.whc_2016.main.equipment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EquipmentInventoryParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class EquipmentInventoryNoEMDKActivity extends AppCompatActivity {
    private final String TAG = EquipmentInventoryNoEMDKActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.ivCameraScan)
    ImageView ivCamera;
    @Bind(R.id.tv_equipment_Quantity)
    TextView tvQuantity;
    @Bind(R.id.tv_equipment_qty_scanned)
    TextView tvQtyScanned;
    @Bind(R.id.etTakeScannerResult)
    EditText etTakeScannerResult;

    private EquipmentInventoryAdapter adapter;
    private String userName, locationCheck;
    private int eventKeycode;
    private boolean isClickedFromCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_inventory);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        etScanResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Utilities.hideKeyboard(EquipmentInventoryNoEMDKActivity.this);
                    new AsyncUiControlUpdate().execute(etScanResult.getText().toString());
                    return true;
                }
                return false;
            }
        });
        adapter = new EquipmentInventoryAdapter(this, new ArrayList<EquipmentInventoryInfo>());
        listView.setAdapter(adapter);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        etTakeScannerResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contents = s.toString();
                if (contents.contains("\n"))
                    new AsyncUiControlUpdate().execute(contents.replace("\n", ""));
            }
        });
    }

    public void getEquipmentInventory(final View view, EquipmentInventoryParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).getEquipmentInventory(parameter).enqueue(new Callback<List<EquipmentInventoryInfo>>() {

            @Override
            public void onResponse(Response<List<EquipmentInventoryInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());

                    int quantityNormal = 0, qtyScanned = 0;
                    for (EquipmentInventoryInfo info : response.body()) {
                        if (info.getResult().equals(""))
                            quantityNormal++;
                        if (info.getResult().equalsIgnoreCase("OK"))
                            qtyScanned++;
                    }
                    if (isClickedFromCamera && quantityNormal > 0)
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivCamera.performClick();
                            }
                        }, 1000);
                    tvQtyScanned.setText(String.format(Locale.US, "%d", qtyScanned));
                } else {
                    tvQuantity.setText("");
                    tvQtyScanned.setText("");
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(EquipmentInventoryNoEMDKActivity.this, t, TAG, view);
            }
        });
    }

    @OnClick(R.id.ivCameraScan)
    public void cameraScan() {
        isClickedFromCamera = true;
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
        Log.d(TAG, "onResume: ");


    }


    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String contents = result.getContents();
                new AsyncUiControlUpdate().execute(contents);
            } else isClickedFromCamera = false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Utilities.hideKeyboard(this);
        if (eventKeycode == 0 && (keyCode == 245 || keyCode == 242)) {
            eventKeycode++;
            etTakeScannerResult.requestFocus();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 245 || keyCode == 242)
            eventKeycode = 0;

        return super.onKeyUp(keyCode, event);
    }


    private class AsyncUiControlUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onData: " + result);
            etTakeScannerResult.setText("");
            etScanResult.setText(result);
            locationCheck = result;
            EquipmentInventoryParameter parameter = new EquipmentInventoryParameter(
                    userName,
                    result,
                    Utilities.getAndroidID(EquipmentInventoryNoEMDKActivity.this)

            );
            getEquipmentInventory(listView, parameter);
        }

    }
}
