package com.scsvn.whc_2016.main.palletcartonchecking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.PalletCartonParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
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

public class KiemPalletCartonActivity extends BaseActivity implements Scanner.DataListener, Scanner.StatusListener {
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.tvPalletCheckingPalletID)
    TextView tvPalletID;
    @Bind(R.id.tvPalletCheckingCurrentQuantity)
    TextView tvCurrentQuantity;
    @Bind(R.id.tvPalletCheckingCustomerNumber)
    TextView tvCustomerNumber;
    @Bind(R.id.tvPalletCheckingCustomerRef)
    TextView tvCustomerRef;
    @Bind(R.id.tvPalletCheckingLocationNumber)
    TextView tvLocationNumber;
    @Bind(R.id.tvPalletCheckingProductName)
    TextView tvProductName;
    @Bind(R.id.tvPalletCheckingProductNumber)
    TextView tvProductNumber;
    @Bind(R.id.tvPalletCheckingReceivingOrderNumber)
    TextView tvReceivingOrderNumber;
    @Bind(R.id.flPalletCheckingPalletInfo)
    FrameLayout flPalletInfo;

    public static final String TAG = KiemPalletCartonActivity.class.getSimpleName();
    private ProgressDialog dialog;
    private View.OnClickListener tryAgain;
    private String scanResult = "";
    private MovementHistoryAdapter adapter;
    //ScanBarcode
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pallet_carton_checking);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initialUI();
    }

    private void initialUI() {
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPalletCarton(listView);
            }
        };
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (scanResult.length() > 0)
                    getPalletCarton(listView);
                else
                    refreshLayout.setRefreshing(false);
            }
        });
        etScanResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    scanResult = etScanResult.getText().toString();
                    getPalletCarton(listView);
                }
                return true;
            }
        });
        adapter = new MovementHistoryAdapter(this, new ArrayList<MovementHistoryInfo>());
        listView.setAdapter(adapter);
        new AsyncEMDK().execute();
    }

    private void getPalletCarton(final View view) {
        Utilities.hideKeyboard(this);
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getPalletCarton(new PalletCartonParameter(0, scanResult)).enqueue(new Callback<List<PalletCartonInfo>>() {
            @Override
            public void onResponse(Response<List<PalletCartonInfo>> response, Retrofit retrofit) {
                List<PalletCartonInfo> body = response.body();
                Log.e(TAG, "onResponse: " + new Gson().toJson(body));
                if (response.isSuccess() && body != null) {
                    if (body.size() > 0) {
                        updatePalletInfo(body);
                        getMovementHistory(view);
                    } else {
                        flPalletInfo.setVisibility(View.GONE);
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        Snackbar.make(listView, R.string.no_data, Snackbar.LENGTH_LONG).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(KiemPalletCartonActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    private void updatePalletInfo(List<PalletCartonInfo> body) {
        PalletCartonInfo info = body.get(0);
        tvCurrentQuantity.setText(String.format("%.2f", info.getCurrentQuantity()));
        tvPalletID.setText(String.format("%d", info.getPalletID()));
        tvCustomerNumber.setText(info.getCustomerNumber());
        tvProductName.setText(info.getProductName());
        tvProductNumber.setText(info.getProductNumber());
        int length = info.getCustomerRef().length();
        Log.e(TAG, "updatePalletInfo: " + length);
        if (length > 0) {
            tvCustomerRef.setText(info.getCustomerRef());
            tvCustomerRef.setVisibility(View.VISIBLE);
        } else
            tvCustomerRef.setVisibility(View.GONE);

        setUnderLine(tvLocationNumber, info.getLocationNumber());
        setUnderLine(tvReceivingOrderNumber, info.getReceivingOrderNumber());
    }

    private void setUnderLine(TextView view, String content) {
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        view.setText(spannableString);
    }

    private void getMovementHistory(final View view) {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getMovementHistory(scanResult).enqueue(new Callback<List<MovementHistoryInfo>>() {
            @Override
            public void onResponse(Response<List<MovementHistoryInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    flPalletInfo.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(KiemPalletCartonActivity.this, t, TAG, view, tryAgain);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                scanResult = result.getContents();
                etScanResult.setText(scanResult);
                getPalletCarton(listView);
            }
        }
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
            scanResult = scanData.get(0).getData();
            Log.e(TAG, "onData: " + scanResult);
            new UpdateScanResultAsync().execute(scanResult);
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
            scanner.triggerType = Scanner.TriggerType.HARD;
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
            EMDKResults results = EMDKManager.getEMDKManager(KiemPalletCartonActivity.this, new EMDKManager.EMDKListener() {
                @Override
                public void onOpened(EMDKManager emdkManager) {
                    Log.d(TAG, "onOpened: ");
                    KiemPalletCartonActivity.this.emdkManager = emdkManager;
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

    class UpdateScanResultAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String s) {
            etScanResult.setText(scanResult);
            getPalletCarton(listView);
        }
    }
}
