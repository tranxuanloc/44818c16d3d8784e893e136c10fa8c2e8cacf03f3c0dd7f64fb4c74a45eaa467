package com.scsvn.whc_2016.main.kiemhoso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KiemHoSoActivity extends AppCompatActivity implements Scanner.DataListener, Scanner.StatusListener, AdapterView.OnItemLongClickListener {
    private final String TAG = KiemHoSoActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.ivCameraScan)
    ImageView ivCamera;
    @Bind(R.id.tv_kiem_ho_so_Quantity)
    TextView tvQuantity;
    @Bind(R.id.tv_kiem_ho_so_qty_scanned)
    TextView tvQtyScanned;
    //ScanBarcode
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;
    private int triggerIndex;
    private KiemHoSoAdapter adapter;
    private String userName, locationCheck;
    private boolean isClickedFromCamera, isCT;
    private String type;

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
                    Utilities.hideKeyboard(KiemHoSoActivity.this);
                    new AsyncUiControlUpdate().execute(etScanResult.getText().toString());
                    return true;
                }
                return false;
            }
        });
        adapter = new KiemHoSoAdapter(this, new ArrayList<KiemHoSoInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        new AsyncEMDK().execute();
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
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
            } else isClickedFromCamera = false;
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
            if ((deviceList != null) && (deviceList.size() != 0)) {
                scanner = barcodeManager.getDevice(deviceList.get(0));
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

    public void getDSInventoryChecking(final View view, DSInventoryCheckingParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
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
                        /*if (type.equals("LO")) {
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
                        ivCamera.performClick();
                } else {
                    tvQuantity.setText("");
                    tvQtyScanned.setText("");
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(KiemHoSoActivity.this, t, TAG, view);
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
            builder.setMessage("Bạn muốn xóa Carton" + item.getCartonNewID() + "?");
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
        if (!Utilities.isConnected(this)) {
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
                RetrofitError.errorNoAction(KiemHoSoActivity.this, t, TAG, view);
            }
        });
    }

    private class AsyncEMDK extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EMDKResults results = EMDKManager.getEMDKManager(KiemHoSoActivity.this, new EMDKManager.EMDKListener() {
                @Override
                public void onOpened(EMDKManager emdkManager) {
                    Log.d(TAG, "onOpened: ");
                    KiemHoSoActivity.this.emdkManager = emdkManager;
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
