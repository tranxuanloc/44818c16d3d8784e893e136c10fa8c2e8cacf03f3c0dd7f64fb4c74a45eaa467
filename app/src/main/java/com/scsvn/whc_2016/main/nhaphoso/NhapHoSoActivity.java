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
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NhapHoSoActivity extends BaseActivity implements Scanner.DataListener, Scanner.StatusListener {
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
    private final String TAG = NhapHoSoActivity.class.getSimpleName();
    //ScanBarcode
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;
    private int triggerIndex;
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
                    return true;
                }
                return false;
            }
        });
        adapter = new ReceivingOrderDetailsAdapter(this, new ArrayList<ReceivingOrderDetailsInfo>());
        listView.setAdapter(adapter);
        new AsyncEMDK().execute();
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
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
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
        Log.d(TAG, "onResume: ");
        if (emdkManager != null) {
            barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
            initScanner();
            setTrigger();
            setDecoders();
        }

    }


    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deInitScanner();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        deInitScanner();
        if (emdkManager != null) {
            emdkManager.release(EMDKManager.FEATURE_TYPE.BARCODE);
        }
    }


    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
            String barcode = scanData.get(0).getData();
            new AsyncUiControlUpdate().execute(barcode);
        }
        if (triggerIndex == 1) {
            triggerIndex = 0;
        }
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

    @Override
    public void onStatus(StatusData statusData) {
        Log.d(TAG, "onStatus: " + statusData.getState());
        String statusString = "";
        StatusData.ScannerStates state = statusData.getState();
        switch (state) {
            case IDLE:
                try {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    scanner.read();
                } catch (ScannerException e) {
                    statusString = e.getMessage();
                    // new AsyncStatusUpdate().execute(statusString);
                }
                break;
            case WAITING:
                statusString = "Có thể Scan...";
                //new AsyncStatusUpdate().execute(statusString);
                break;
            case SCANNING:
                statusString = "Đang Scan...";
                //new AsyncStatusUpdate().execute(statusString);
                break;
            case DISABLED:
                statusString = statusData.getFriendlyName() + " bị vô hiệu hóa";
                //new AsyncStatusUpdate().execute(statusString);
                break;
            case ERROR:
                statusString = "Có lỗi xảy ra";
                // new AsyncStatusUpdate().execute(statusString);
                break;
            default:
                break;
        }
        Log.e(TAG, "onStatus: " + statusString);
    }


    private void initScanner() {

        if (scanner == null) {
            List<ScannerInfo> deviceList = barcodeManager.getSupportedDevicesInfo();
            int numberDevices = deviceList.size();
            if ((deviceList != null) && (numberDevices != 0)) {
                scanner = barcodeManager.getDevice(deviceList.get(numberDevices > 0 ? 1 : 0));
            } else {
                Snackbar.make(listView, "initScanner: " + "Failed to get the specified scanner device! Please close and restart the application.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    Snackbar.make(listView, "initScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(listView, "initScanner: " + "Failed to initialize the scanner device.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void setTrigger() {
        if (scanner == null) {
            initScanner();
        }
        if (scanner != null) {
            switch (triggerIndex) {
                case 0: // Selected "HARD"
                    scanner.triggerType = Scanner.TriggerType.HARD;
                    break;
                case 1: // Selected "SOFT"
                    scanner.triggerType = Scanner.TriggerType.SOFT_ALWAYS;
                    break;
            }
        }
    }

    private void setDecoders() {
        if (scanner == null)
            initScanner();
        if (scanner != null)
            try {
                ScannerConfig config = scanner.getConfig();
                config.decoderParams.ean8.enabled = true;
                config.decoderParams.ean13.enabled = true;
                config.decoderParams.code39.enabled = true;
                config.decoderParams.code128.enabled = true;
                scanner.setConfig(config);
            } catch (ScannerException e) {
                //  Snackbar.make(listView, "setDecoders: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                scanner.disable();

            } catch (ScannerException e) {
                Snackbar.make(listView, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner.removeDataListener(this);
            scanner.removeStatusListener(this);
            try {
                scanner.release();
            } catch (ScannerException e) {
                Snackbar.make(listView, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner = null;
        }
    }

    private class AsyncEMDK extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EMDKResults results = EMDKManager.getEMDKManager(NhapHoSoActivity.this, new EMDKManager.EMDKListener() {
                @Override
                public void onOpened(EMDKManager emdkManager) {
                    Log.d(TAG, "onOpened: ");
                    NhapHoSoActivity.this.emdkManager = emdkManager;
                    barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
                    initScanner();
                    setTrigger();
                    setDecoders();
                }

                @Override
                public void onClosed() {
                    Log.d(TAG, "onClosed: ");
                    if (emdkManager != null) {
                        // Release all the resources
                        emdkManager.release();
                        emdkManager = null;
                    }
                    Snackbar.make(listView, "onClosed: " + "EMDK closed unexpectedly! Please close and restart the application.", Snackbar.LENGTH_LONG).show();
                }
            });
            if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS)
                Snackbar.make(listView, "Có lỗi xảy ra. Không thể thực hiện Scan", Snackbar.LENGTH_SHORT).show();
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
            etScanResult.setText(result);
            String type = result.substring(0, 2);
            int number = Integer.parseInt(result.substring(2));
            tvOrderNumber.setText(String.format("%d", number));
            if (type.equalsIgnoreCase("LO")) {
                if (tvType.getText().toString().equalsIgnoreCase("CT")
                        || tvType.getText().toString().equalsIgnoreCase("PI")) {
                    showSettingsAlert(number, tvType.getText().toString());
                } else {
                    tvOrderNumber.setText(String.format("%d", number));
                    tvType.setText(type);
                }

            } else if (type.equalsIgnoreCase("RD")) {
                tvType.setText(type);
                parameter = new ReceivingOrderDetailParameter(result);
                getReceivingOrderDetails(listView, parameter);
            } else if (type.equalsIgnoreCase("CT")) {
                tvType.setText(type);
                if (tvRD.getText().toString().trim().length() > 0) {
                    byte TotalCarton = Byte.parseByte(tvTotalSelect.getText().toString());
                    if (TotalCarton < 12) {
                        parameter = new ReceivingOrderDetailParameter(result, Integer.parseInt(tvRD.getText().toString()));
                        getReceivingOrderDetails(listView, parameter);
                    } else
                        Snackbar.make(listView, "Bạn đã chọn 12 thùng, vui lòng chọn vị trí", Snackbar.LENGTH_LONG).show();
                } else {
                    parameter = new ReceivingOrderDetailParameter(result);
                    getReceivingOrderDetails(listView, parameter);
                }
            }
        }

    }

    private ArrayList<Integer> cartonSelectIDs = new ArrayList<>();

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
                cartonSelectIDs.clear();
                if (response.isSuccess() && response.body() != null) {
                    if (response.body().size() > 0)
                        tvRD.setText(String.format("%s", response.body().get(response.body().size() - 1).getDSROID()));
                    int totalSelect = 0;
                    for (ReceivingOrderDetailsInfo info : response.body()) {
                        if (info.isRecordFirst()) {
                            totalSelect++;
                            cartonSelectIDs.add(info.getPalletID());
                        }
                    }
                    tvTotalSelect.setText(String.format("%d", totalSelect));
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(NhapHoSoActivity.this, t, TAG, view);
            }
        });
    }

    public void showSettingsAlert(final int locationNumber,
                                  final String codeType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (codeType.equals("CT")) {
            builder.setMessage("Bạn có muốn cập nhật carton " + cartonSelectIDs.toString()
                    + " này vào vị trí " + locationNumber + " hay không?");
        } else if (codeType.equals("PI")) {
            builder.setMessage("Bạn có muốn cập nhật pallet " + cartonSelectIDs.toString()
                    + " này vào vị trí " + locationNumber + " hay không?");
        }
        builder.setPositiveButton("Cập nhật",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (int cartonID : cartonSelectIDs) {
                            UpdateLocationReceivingOrder parameterUpdate = new UpdateLocationReceivingOrder(
                                    cartonID, Integer.toString(locationNumber), userName
                            );
                            updateLocation(listView, parameterUpdate);
                        }

                        switch (tvType.getText().toString()) {
                            case "CT":
                                parameter = new ReceivingOrderDetailParameter(String.format("RD0%s", tvRD.getText().toString()));
                                getReceivingOrderDetails(listView, parameter);
                                break;
                            case "PI":
                                parameter = new ReceivingOrderDetailParameter(String.format("PI0%s", tvRD.getText().toString()));
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
                RetrofitError.errorNoAction(NhapHoSoActivity.this, t, TAG, view);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
