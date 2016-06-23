package com.scsvn.whc_2016.main.detailphieu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailPhieuActivityNoEMDK extends BaseActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {
    public static final String ORDER_NUMBER = "order_number";
    public static final String TAG = "DetailPhieuNoEMDK";
    ExpandableListView listView;
    EditText etBarcode;
    TextView totalQuantity;
    TextView totalScanned;
    EditText etTakeScannerResult;
    Button ivCamera;
    private int eventKeycode;
    private View.OnClickListener action;
    private String orderNumber = "";
    private String scanResult = "xx123456789", userName;


    private ActionBar actionBar;
    private DetailPhieuAdapter adapter;
    private List<String> groupLevel1 = new LinkedList<>();
    private LinkedHashMap<String, List<DetailPhieuInfo>> groupLevel2 = new LinkedHashMap<>();
    private LinkedHashMap<String, List<DetailPhieuInfo>> groupLevel3 = new LinkedHashMap<>();
    private LinkedHashMap<String, List<Item>> groupLevel2Bind = new LinkedHashMap<>();
    private MenuItem item_sort;
    private int scanType;
    private boolean isClickedFromCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_phieu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ExpandableListView) findViewById(R.id.listView);
        etBarcode = (EditText) findViewById(R.id.barcode);
        totalQuantity = (TextView) findViewById(R.id.tv_total);
        totalScanned = (TextView) findViewById(R.id.tv_scanned);
        etTakeScannerResult = (EditText) findViewById(R.id.etTakeScannerResult);
        ivCamera = (Button) findViewById(R.id.scan_camera);
        findViewById(R.id.so_do_day).setOnClickListener(this);
        findViewById(R.id.take_picture).setOnClickListener(this);
        findViewById(R.id.worker).setOnClickListener(this);
        findViewById(R.id.scan_camera).setOnClickListener(this);
        initUI();
    }

    private void initUI() {
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        orderNumber = getIntent().getStringExtra(ORDER_NUMBER);
        scanType = getIntent().getByteExtra("SCAN_TYPE", (byte) 0);
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setTitle(String.format("Chi tiết %s", orderNumber));
            Utilities.showBackIcon(actionBar);
        }
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
        getDetailPhieu(listView);
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
                int quantityNormal = 0;
                if (response.isSuccess() && response.body() != null) {
                    List<DetailPhieuInfo> body = response.body();
                    for (DetailPhieuInfo info : body) {
                        String tempDO = String.format("%s - %s", info.DO, info.SpecialRequirement);
                        if (!groupLevel2.containsKey(tempDO)) {
                            List<DetailPhieuInfo> oneItemGroup2 = new LinkedList<>();
                            oneItemGroup2.add(info);
                            groupLevel2.put(tempDO, oneItemGroup2);
                        } else
                            groupLevel2.get(tempDO).add(info);
                        if (info.getResult().equals(""))
                            quantityNormal++;
                        int quantity = Integer.parseInt(info.getQuantityOfPackages());
                        total += quantity;
                        if (info.getResult().equalsIgnoreCase("OK"))
                            scanned += quantity;
                    }
                    int totalOneProduct = 0;
                    DetailPhieuGroupInfo tProductName = null;
                    groupLevel1.addAll(groupLevel2.keySet());
                    for (String keyDO : groupLevel1) {
                        List<DetailPhieuInfo> detailPhieuInfo = groupLevel2.get(keyDO);
                        List<Item> item = new LinkedList<>();
                        for (DetailPhieuInfo info : detailPhieuInfo) {
                            String productName = new StringBuilder().append(info.getProductName()).append(keyDO).toString();
                            DetailPhieuGroupInfo tempGroup2 = new DetailPhieuGroupInfo(productName, info.getProductNumber());
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

                    if (isClickedFromCamera && quantityNormal > 0)
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ivCamera.performClick();
                            }
                        }, 1000);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(DetailPhieuActivityNoEMDK.this, t, TAG, view, action);
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
                        Utilities.basicDialog(DetailPhieuActivityNoEMDK.this, message.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //RetrofitError.errorWithAction(DetailPhieuActivityNoEMDK.this, t, TAG, view, action);
            }
        });
    }


    @Override
    protected void onStart() {

        super.onStart();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
                                Utilities.hideKeyboard(DetailPhieuActivityNoEMDK.this);
                            }
                        })
                        .setNeutralButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilities.hideKeyboard(DetailPhieuActivityNoEMDK.this);
                                boolean checked = cbChecked.isChecked();
                                String remark = etRemark.getText().toString();
                                updatePalletID(listView, checked, item.getDispatchingOrderDetailID(), remark);
                            }
                        })
                        .setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                AlertDialog dialog = new AlertDialog.Builder(DetailPhieuActivityNoEMDK.this)
                                        .setMessage("Bạn có chắc muốn xóa phiếu này?")
                                        .setPositiveButton("Không", null).
                                                setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
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
                RetrofitError.errorNoAction(DetailPhieuActivityNoEMDK.this, t, TAG, view);
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
                RetrofitError.errorNoAction(DetailPhieuActivityNoEMDK.this, t, TAG, view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_phieu, menu);
        item_sort = menu.findItem(R.id.action_menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_detail_no) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_sort.setTitle(getResources().getString(R.string.no));
            }
        } else if (itemId == R.id.action_detail_name) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_sort.setTitle(getResources().getString(R.string.product_name));
            }
        } else if (itemId == R.id.action_detail_remark) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_sort.setTitle(getResources().getString(R.string.remark));
            }
        } else if (itemId == R.id.action_detail_nxs) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_sort.setTitle(getResources().getString(R.string.nxs));
            }
        } else if (itemId == R.id.action_detail_hsd) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_sort.setTitle(getResources().getString(R.string.hsd));
            }
        } else if (itemId == R.id.action_detail_vi_tri) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_sort.setTitle(getResources().getString(R.string.vi_tri));
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                isClickedFromCamera = true;
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String contents = result.getContents();
                new AsyncUiControlUpdate().execute(contents);
            } else isClickedFromCamera = false;
        }
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.so_do_day) {
            Intent intent = new Intent(getApplicationContext(), SoDoDayActivity.class);
            intent.putExtra("DO_ID", Integer.parseInt(orderNumber.split("-")[1]));
            startActivity(intent);
        } else if (id == R.id.take_picture) {
            if (orderNumber.length() > 0) {
                Intent intent = new Intent(this,
                        ChupHinhActivity.class);
                intent.putExtra(ORDER_NUMBER, orderNumber);
                startActivity(intent);
            } else
                Snackbar.make(v, "Vui lòng Scan đơn hàng trước khi chụp hình", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.worker) {
            Intent intent = new Intent(this,
                    WorkerActivity.class);
            intent.putExtra(ORDER_NUMBER, orderNumber);
            startActivity(intent);
        } else if (id == R.id.scan_camera) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.setCaptureActivity(ScanCameraPortrait.class);
            integrator.initiateScan();
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
