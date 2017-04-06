package com.scsvn.whc_2016.main.nhaphoso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.ReceivingOrderDetailParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.UpdateLocationReceivingOrder;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NhapHoSoNoEMDKActivity extends BaseActivity {
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.tvNhsType)
    TextView tvType;
    @Bind(R.id.tvNhsCartonIDSelect)
    TextView tvCartonIDSelect;
    @Bind(R.id.tvNhsOrderNumber)
    TextView tvOrderNumber;
    @Bind(R.id.tvNhsRD)
    TextView tvRD;
    @Bind(R.id.tvNhsTotalSelect)
    TextView tvTotalSelect;
    @Bind(R.id.etTakeScannerResult)
    EditText etTakeScannerResult;

    private int eventKeycode;
    private final String TAG = NhapHoSoNoEMDKActivity.class.getSimpleName();
    //ScanBarcode
    private ReceivingOrderDetailParameter parameter;
    private ReceivingOrderDetailsAdapter adapter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_ho_so);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initial();
    }

    private void initial() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
        etScanResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    new AsyncUiControlUpdate().execute(etScanResult.getText().toString());
                    Utilities.hideKeyboard(NhapHoSoNoEMDKActivity.this);
                    return true;
                }
                return false;
            }
        });
        adapter = new ReceivingOrderDetailsAdapter(this, new ArrayList<ReceivingOrderDetailsInfo>());
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

    @OnClick(R.id.ivCameraScan)
    public void cameraScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String contents = result.getContents();
                new AsyncUiControlUpdate().execute(contents);
            }
        }
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
            String type = result.substring(0, 2);
            int number = Integer.parseInt(result.substring(2));
            tvOrderNumber.setText(String.format("%d", number));
            if (type.equalsIgnoreCase("LO")) {
                if (tvType.getText().toString().equalsIgnoreCase("CT")
                        || tvType.getText().toString().equalsIgnoreCase("PI")) {
                    showSettingsAlert(number, tvCartonIDSelect.getText().toString()
                            , tvType.getText().toString());
                } else
                    tvOrderNumber.setText(number);

            } else if (type.equalsIgnoreCase("RD")) {
                parameter = new ReceivingOrderDetailParameter(result);
                getReceivingOrderDetails(listView, parameter);
            } else if (type.equalsIgnoreCase("CT")) {
                if (tvRD.getText().toString().trim().length() > 0) {
                    byte TotalCarton = Byte.parseByte(tvTotalSelect.getText().toString());
                    if (TotalCarton < 12) {
                        //    parameter = new ReceivingOrderDetailParameter(result, tvRD.getText().toString().trim());
                        getReceivingOrderDetails(listView, parameter);
                    } else
                        Snackbar.make(listView, "Bạn đã chọn 5 thùng, vui lòng chọn vị trí", Snackbar.LENGTH_LONG).show();
                } else {
                    parameter = new ReceivingOrderDetailParameter(result);
                    getReceivingOrderDetails(listView, parameter);
                }
            }
            tvType.setText(type);
        }

    }

    public void getReceivingOrderDetails(final View view, ReceivingOrderDetailParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).getReceivingOrderDetails(parameter).enqueue(new Callback<List<ReceivingOrderDetailsInfo>>() {

            @Override
            public void onResponse(Response<List<ReceivingOrderDetailsInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(NhapHoSoNoEMDKActivity.this, t, TAG, view);
            }
        });
    }

    public void showSettingsAlert(final int locationNumber,
                                  final String cartonID, final String codeType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (codeType.equals("CT")) {
            builder.setMessage("Bạn có muốn cập nhật carton " + cartonID
                    + " này vào vị trí " + locationNumber + " hay không?");
        } else if (codeType.equals("PI")) {
            builder.setMessage("Bạn có muốn cập nhật pallet " + cartonID
                    + " này vào vị trí " + locationNumber + " hay không?");
        }
        builder.setPositiveButton("Cập nhật",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateLocationReceivingOrder parameterUpdate = new UpdateLocationReceivingOrder(
                                Integer.parseInt(cartonID), Integer.toString(locationNumber), userName
                        );
                        updateLocation(listView, parameterUpdate);

                        switch (tvType.getText().toString()) {
                            case "CT":
                                parameter = new ReceivingOrderDetailParameter(String.format("RD0%s", tvRD.getText().toString()));
                                getReceivingOrderDetails(listView, parameter);
                                break;
                            case "PI":
                                parameter = new ReceivingOrderDetailParameter(String.format("PI0%s", tvRD.getText().toString()));
                                getReceivingOrderDetails(listView, parameter);
                                break;
                            default:
                                parameter = new ReceivingOrderDetailParameter(String.format("%s%s", codeType, cartonID));
                                getReceivingOrderDetails(listView, parameter);
                                break;
                        }

                    }
                });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void updateLocation(final View view, UpdateLocationReceivingOrder parameter) {
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).updateLocationReceivingOrderDetails(parameter).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccess() && response.body() != null) {
                    Snackbar.make(view, response.body(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(NhapHoSoNoEMDKActivity.this, t, TAG, view);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
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
