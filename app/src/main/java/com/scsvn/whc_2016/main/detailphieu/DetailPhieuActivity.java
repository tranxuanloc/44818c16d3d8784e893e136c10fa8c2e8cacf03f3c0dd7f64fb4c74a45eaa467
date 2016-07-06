package com.scsvn.whc_2016.main.detailphieu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ChupHinhActivity;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.scsvn.whc_2016.main.detailphieu.so_do_day.SoDoDayActivity;
import com.scsvn.whc_2016.main.detailphieu.worker.WorkerActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.DispatchingOrderScannedDeleteParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OrdersInfo;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.UpdateDispatchingOrderDetailParameter;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailPhieuActivity extends BaseActivity implements Scanner.DataListener, Scanner.StatusListener, AdapterView.OnItemLongClickListener {
    public static final String ORDER_NUMBER = "order_number";
    public static final String TAG = "DetailPhieuActivity";
    @Bind(R.id.listView)
    ExpandableListView listView;
    @Bind(R.id.barcode)
    EditText etBarcode;
    @Bind(R.id.tv_total)
    TextView totalQuantity;
    @Bind(R.id.tv_scanned)
    TextView totalScanned;
    private View.OnClickListener action;
    private String orderNumber = "";
    private String scanResult = "xx123456789", userName;

    //ScanBarcode
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;
    private int triggerIndex;
    private ActionBar actionBar;
    private DetailPhieuAdapter adapter;
    private List<String> groupLevel1 = new LinkedList<>();
    private LinkedHashMap<String, List<DetailPhieuInfo>> groupLevel2 = new LinkedHashMap<>();
    private LinkedHashMap<String, List<DetailPhieuInfo>> groupLevel3 = new LinkedHashMap<>();
    private LinkedHashMap<String, List<Item>> groupLevel2Bind = new LinkedHashMap<>();

    private MenuItem item_sort, itemFilter;
    private int scanType, filterResult = 1;
    private int underScores = 0b1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_phieu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        orderNumber = getIntent().getStringExtra(ORDER_NUMBER);
        scanType = getIntent().getByteExtra("SCAN_TYPE", (byte) 0);
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setTitle(String.format("Chi tiết %s", orderNumber));
            Utilities.showBackIcon(actionBar);
        }
        ButterKnife.bind(this);
        getRequest();
        adapter = new DetailPhieuAdapter(groupLevel1, groupLevel2Bind);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailPhieu(listView);
            }
        };
        new AsyncEMDK().execute();
        getDetailPhieu(listView);
    }

    public void getDetailPhieu(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getDetailPhieu(new OrdersInfo(scanResult, orderNumber, userName)).enqueue(new Callback<List<DetailPhieuInfo>>() {

            @Override
            public void onResponse(Response<List<DetailPhieuInfo>> response, Retrofit retrofit) {
                groupLevel1.clear();
                groupLevel2.clear();
                groupLevel3.clear();
                groupLevel2Bind.clear();
                int total = 0;
                int scanned = 0;
                if (response.isSuccess() && response.body() != null) {
                    List<DetailPhieuInfo> body = response.body();
                    for (DetailPhieuInfo info : body) {
                        String result = info.getResult();
                        if (filterResult == 0 || (filterResult == 1 && result.equals(" ") || (filterResult == 2 && !result.equals(" ")))) {
                            String tempDO = String.format("%s - %s", info.DO, info.SpecialRequirement);
                            if (!groupLevel2.containsKey(tempDO)) {
                                List<DetailPhieuInfo> oneItemGroup2 = new LinkedList<>();
                                oneItemGroup2.add(info);
                                groupLevel2.put(tempDO, oneItemGroup2);
                            } else
                                groupLevel2.get(tempDO).add(info);

                            int quantity = Integer.parseInt(info.getQuantityOfPackages());
                            total += quantity;
                            if (info.getResult().equalsIgnoreCase("OK"))
                                scanned += quantity;
                        }
                    }
                    int totalOneProduct = 0;
                    DetailPhieuGroupInfo tProductName = null;
                    groupLevel1.addAll(groupLevel2.keySet());
                    for (String keyDO : groupLevel1) {
                        List<DetailPhieuInfo> detailPhieuInfo = groupLevel2.get(keyDO);
                        List<Item> item = new LinkedList<>();
                        for (DetailPhieuInfo info : detailPhieuInfo) {
                            String productNameBind = info.getProductName();
                            String productName = new StringBuilder().append(productNameBind).append(keyDO).toString();
                            DetailPhieuGroupInfo tempGroup2 = new DetailPhieuGroupInfo(productNameBind, info.getProductNumber());
                            if (!groupLevel3.containsKey(productName)) {
                                if (tProductName != null)
                                    tProductName.total = totalOneProduct;
                                totalOneProduct = 0;
                                List<DetailPhieuInfo> oneItemGroup3 = new LinkedList<>();
                                oneItemGroup3.add(info);
                                item.add(tempGroup2);
                                item.add(info);
                                totalOneProduct += Integer.parseInt(info.getQuantityOfPackages());
                                groupLevel3.put(productName, oneItemGroup3);
                                groupLevel2Bind.put(keyDO, item);
                                tProductName = tempGroup2;
                            } else {
                                totalOneProduct += Integer.parseInt(info.getQuantityOfPackages());
                                groupLevel3.get(productName).add(info);
                                groupLevel2Bind.get(keyDO).add(info);
                            }
                        }
                    }
                    if (tProductName != null)
                        tProductName.total = totalOneProduct;
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        listView.expandGroup(i);
                    }
                    totalScanned.setText(String.format(Locale.US, "%d", scanned));
                    totalQuantity.setText(String.format(Locale.US, "%d", total));

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(DetailPhieuActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getRequest() {
        MyRetrofit.initRequest(this).getRequestPhieu(orderNumber).enqueue(new Callback<List<RequirementInfo>>() {
            @Override
            public void onResponse(Response<List<RequirementInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    StringBuilder message = new StringBuilder();
                    for (RequirementInfo info : response.body())
                        message.append(info.getRequirement()).append("\n");
                    if (message.toString().length() > 0)
                        Utilities.basicDialog(DetailPhieuActivity.this, message.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //RetrofitError.errorWithAction(DetailPhieuActivity.this, t, TAG, view, action);
            }
        });
    }


    @Override
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (emdkManager != null) {
            barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
            initScanner();
            setTrigger();
            setDecoders();
        }

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
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
            for (ScanDataCollection.ScanData data : scanData) {

                String barcode = data.getData();
                new AsyncUiControlUpdate().execute(barcode);
            }
        }
        if (triggerIndex == 1) {
            triggerIndex = 0;
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
                    new AsyncStatusUpdate().execute(statusString);
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

    private void startScan() {

        if (scanner == null) {
            initScanner();
        }
        if (scanner != null) {
            try {
                scanner.read();
            } catch (ScannerException e) {
                Snackbar.make(etBarcode, "startScan: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void stopScan() {

        if (scanner != null) {

            try {
                scanner.cancelRead();

            } catch (ScannerException e) {
                Snackbar.make(etBarcode, "Status: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void initScanner() {

        if (scanner == null) {
            List<ScannerInfo> deviceList = barcodeManager.getSupportedDevicesInfo();
            if ((deviceList != null) && (deviceList.size() != 0)) {
                scanner = barcodeManager.getDevice(deviceList.get(0));
            } else {
                Snackbar.make(etBarcode, "initScanner: " + "Failed to get the specified scanner device! Please close and restart the application.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    Snackbar.make(etBarcode, "initScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(etBarcode, "initScanner: " + "Failed to initialize the scanner device.", Snackbar.LENGTH_SHORT).show();
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
                Snackbar.make(etBarcode, "setDecoders: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                scanner.disable();

            } catch (ScannerException e) {
                Snackbar.make(etBarcode, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner.removeDataListener(this);
            scanner.removeStatusListener(this);
            try {
                scanner.release();
            } catch (ScannerException e) {
                Snackbar.make(etBarcode, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner = null;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
            int childPosition = ExpandableListView.getPackedPositionChild(id);
            Item child = adapter.getChild(groupPosition, childPosition);
            if (child instanceof DetailPhieuInfo) {
                final DetailPhieuInfo item = (DetailPhieuInfo) child;

                View content = View.inflate(this, R.layout.item_dialog_detail_phieu, null);
                final CheckBox cbChecked = (CheckBox) content.findViewById(R.id.cb_detail_phieu_checked);
                if (item.getResult().equalsIgnoreCase("OK"))
                    cbChecked.setChecked(true);
                final EditText etRemark = (EditText) content.findViewById(R.id.et_detail_phieu_remark);
                etRemark.setText(item.getRemark());
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                        .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilities.hideKeyboard(DetailPhieuActivity.this);
                            }
                        })
                        .setNeutralButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilities.hideKeyboard(DetailPhieuActivity.this);
                                boolean checked = cbChecked.isChecked();
                                String remark = etRemark.getText().toString();
                                updatePalletID(listView, checked, item.getDispatchingOrderDetailID(), remark);
                            }
                        })
                        .setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                AlertDialog dialog = new AlertDialog.Builder(DetailPhieuActivity.this)
                                        .setMessage("Bạn có chắc muốn xóa phiếu này?")
                                        .setPositiveButton("Không", null)
                                        .setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DispatchingOrderScannedDeleteParameter parameter = new DispatchingOrderScannedDeleteParameter(item.BarcodeScanDetailID, item.getDispatchingOrderDetailID(), userName);
                                                executeDispatchingOrderScannedDelete(listView, parameter, groupPosition, position);

                                            }
                                        })
                                        .create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            }
                        });
                builder.setView(content);
                android.app.AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                if (item.BarcodeScanDetailID == 0)
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);
                return true;
            }
        }
        return false;
    }

    private void updatePalletID(final View view, boolean checked, int orderId, String remark) {
        UpdateDispatchingOrderDetailParameter parameter = new UpdateDispatchingOrderDetailParameter(checked, orderId, remark, userName, orderNumber);
        Log.e(TAG, "updatePalletID: " + parameter.getRemark() + " " + parameter.getUserName() + " " + parameter.getDispatchingOrderDetailID() + " " + parameter.isChecked());
        MyRetrofit.initRequest(this).updateDispatchingOrderDetail(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccess() && response.body() != null) {
                    getDetailPhieu(listView);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(DetailPhieuActivity.this, t, TAG, view);
            }
        });
    }

    private void executeDispatchingOrderScannedDelete(final View view, DispatchingOrderScannedDeleteParameter parameter, final int groupPosition, final int position) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang xóa...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).executeDispatchingOrderScannedDelete(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.removeChildAt(groupPosition, position);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(DetailPhieuActivity.this, t, TAG, view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_phieu, menu);
        item_sort = menu.findItem(R.id.action_menu);
        itemFilter = menu.findItem(R.id.action_filter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_detail_no:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.no));
                }
                break;
            case R.id.action_detail_name:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.product_name));
                }
                break;
            case R.id.action_detail_remark:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.remark));
                }
                break;
            case R.id.action_detail_nxs:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.nxs));
                }
                break;
            case R.id.action_detail_hsd:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.hsd));
                }
                break;
            case R.id.action_detail_vi_tri:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.vi_tri));
                }
                break;
            case R.id.action_filter_all:
                filterResult = 0;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.all));
                }
                getDetailPhieu(listView);
                break;
            case R.id.action_filter_yet_scan:
                filterResult = 1;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.not_yet_scan));
                }
                getDetailPhieu(listView);
                break;
            case R.id.action_filter_scanned:
                filterResult = 2;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.scanned));
                }
                getDetailPhieu(listView);
                break;
        }
        return true;
    }

    @OnClick(R.id.take_picture)
    public void chupHinh(View view) {
        if (orderNumber.length() > 0) {
            Intent intent = new Intent(this,
                    ChupHinhActivity.class);
            intent.putExtra(ORDER_NUMBER, orderNumber);
            startActivity(intent);
        } else
            Snackbar.make(view, "Vui lòng Scan đơn hàng trước khi chụp hình", Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.worker)
    public void worker() {
        Intent intent = new Intent(this,
                WorkerActivity.class);
        intent.putExtra(ORDER_NUMBER, orderNumber);
        startActivity(intent);
    }

    @OnClick(R.id.scan_camera)
    public void scanCamera() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();

    }

    @OnClick(R.id.so_do_day)
    public void intentSDD() {
        Intent intent = new Intent(this, SoDoDayActivity.class);
        intent.putExtra("DO_ID", Integer.parseInt(orderNumber.split("-")[1]));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String contents = result.getContents();
                new AsyncUiControlUpdate().execute(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    private class AsyncEMDK extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EMDKResults results = EMDKManager.getEMDKManager(DetailPhieuActivity.this, new EMDKManager.EMDKListener() {
                @Override
                public void onOpened(EMDKManager emdkManager) {
                    Log.d(TAG, "onOpened: ");
                    DetailPhieuActivity.this.emdkManager = emdkManager;
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
                    Snackbar.make(etBarcode, "onClosed: " + "EMDK closed unexpectedly! Please close and restart the application.", Snackbar.LENGTH_LONG).show();
                }
            });
            if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS)
                Snackbar.make(listView, "Có lỗi xảy ra. Không thể thực hiện Scan", Snackbar.LENGTH_SHORT).show();
        }
    }

    private class AsyncStatusUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            etBarcode.setText(result);
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
            etBarcode.setText(result);
            String type;
            int number;
            try {
                type = result.substring(0, 2);
                number = Integer.parseInt(result.substring(2, result.length()));
            } catch (NumberFormatException ex) {
                type = result.substring(0, 3);
                number = Integer.parseInt(result.substring(3, result.length()));
            }

            if (type.equalsIgnoreCase("DO") || type.equalsIgnoreCase("DP")) {
                actionBar.setTitle(String.format("Chi tiết %s", String.format("%s-%d", type, number)));
                orderNumber = String.format("%s-%d", type, number);
                getRequest();
            } else
                scanResult = result;
            Log.d(TAG, "onPostExecute: " + scanResult + " " + orderNumber);
            getDetailPhieu(listView);
        }

    }
}
