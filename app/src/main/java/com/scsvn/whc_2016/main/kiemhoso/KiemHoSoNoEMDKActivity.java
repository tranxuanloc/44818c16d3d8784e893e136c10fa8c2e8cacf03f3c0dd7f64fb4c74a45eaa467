package com.scsvn.whc_2016.main.kiemhoso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
import com.scsvn.whc_2016.retrofit.DSInventoryCheckingParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KiemHoSoNoEMDKActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private final String TAG = KiemHoSoNoEMDKActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.etTakeScannerResult)
    EditText etTakeScannerResult;
    @Bind(R.id.ivCameraScan)
    ImageView ivCamera;
    @Bind(R.id.tv_kiem_ho_so_Quantity)
    TextView tvQuantity;
    @Bind(R.id.tv_kiem_ho_so_qty_scanned)
    TextView tvQtyScanned;
    private KiemHoSoAdapter adapter;
    private String userName, locationCheck, type;
    private int eventKeycode;
    private boolean isClickedFromCamera, isCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiem_ho_so);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        etScanResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Utilities.hideKeyboard(KiemHoSoNoEMDKActivity.this);
                    new AsyncUiControlUpdate().execute(etScanResult.getText().toString());
                    return true;
                }
                return false;
            }
        });
        adapter = new KiemHoSoAdapter(this, new ArrayList<KiemHoSoInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
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
            } else
                isClickedFromCamera = false;
            Log.e(TAG, "onActivityResult: " + resultCode);
        }
    }

    public void getDSInventoryChecking(final View view, DSInventoryCheckingParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).getDSInventoryChecking(parameter).enqueue(new Callback<List<KiemHoSoInfo>>() {

            @Override
            public void onResponse(Response<List<KiemHoSoInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());

                    if (response.body().size() > 0) {
                        tvQuantity.setText(String.format(Locale.US, "%d", response.body().get(0).getQtyAtLocation()));
                      /*  if (type.equals("LO")) {
                            long scannedTime = response.body().get(0).getScannedTime();
                            showDialog(scannedTime);
                        }*/
                    }
                    int quantityNormal = 0, qtyScanned = 0;
                    for (KiemHoSoInfo info : response.body()) {
                        if (info.getResult().equals(""))
                            quantityNormal++;
                        if (info.getResult().equalsIgnoreCase("OK"))
                            qtyScanned++;
                    }
                    tvQtyScanned.setText(String.format(Locale.US, "%d", qtyScanned));

                    if (isCT && isClickedFromCamera && quantityNormal > 0)
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivCamera.performClick();
                            }
                        }, 1000);

                } else {
                    tvQuantity.setText("");
                    tvQtyScanned.setText("");
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(KiemHoSoNoEMDKActivity.this, t, TAG, view);
            }
        });
    }

    private void showDialog(long time) {
        String timeDisplay = Utilities.convertMinute(time);
        View view = View.inflate(this, R.layout.dialog_container_checking_verify, null);
        ((TextView) view.findViewById(R.id.tv_cont_time_check)).setText(timeDisplay);

        AlertDialog dialog = new AlertDialog.Builder(this).setNegativeButton(getString(R.string.khong), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.clear();
                adapter.addAll(new ArrayList<KiemHoSoInfo>());
                locationCheck = null;
                tvQuantity.setText("");
            }
        }).setPositiveButton(getString(R.string.kiem_tra), null).setView(view).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return true;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final KiemHoSoInfo item = adapter.getItem(position);
        if (item.getResult().equalsIgnoreCase("NO")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn muốn xóa Carton " + item.getCartonNewID() + "?");
            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    executeInventoryCheckingDelete(listView, item.getInventoryCheckingID(), position);
                }
            });
            builder.setNegativeButton("Không", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            return true;
        }
        return false;
    }

    public void executeInventoryCheckingDelete(final View view, int parameter, final int position) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).executeInventoryCheckingDelete(parameter).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.remove(adapter.getItem(position));
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(KiemHoSoNoEMDKActivity.this, t, TAG, view);
            }
        });
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
            type = result.substring(0, 2);
            int number = Integer.parseInt(result.substring(2));
            if (type.equalsIgnoreCase("LO"))
                locationCheck = result;
            DSInventoryCheckingParameter parameter = new DSInventoryCheckingParameter(
                    locationCheck,
                    result,
                    userName,
                    Utilities.getAndroidID(getApplicationContext())
            );
            isCT = type.equalsIgnoreCase("CT");
            getDSInventoryChecking(listView, parameter);
        }
    }
}
