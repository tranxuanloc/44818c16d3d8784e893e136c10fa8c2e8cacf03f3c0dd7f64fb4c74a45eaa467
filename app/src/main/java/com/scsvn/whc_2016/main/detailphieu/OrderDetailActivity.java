package com.scsvn.whc_2016.main.detailphieu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderDetailActivity extends BaseActivity
        implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    public static final String ORDER_NUMBER = "order_number";
    public static final String TAG = OrderDetailActivity.class.getSimpleName();
    public static final String DO_ID = "DO_ID";
    public ListView listView;
    private EditText etBarcode;
    private TextView totalQuantity;
    private TextView totalScanned;
    private EditText etTakeScannerResult;
    private Button ivCamera;
    private int eventKeycode;
    private View.OnClickListener action;
    private String orderNumber;
    private String scanResult = "xx123456789", userName;
    private OrderDetailAdapter adapter;
    private MenuItem item_sort, itemFilter;
    private int filterResult = 1;
    private boolean isClickedFromCamera;
    private String deviceNumber;
    private List<Item> completedList = new LinkedList<>();
    private HashMap<String, List<OrderDetail>> groupDO = new LinkedHashMap<>();
    private List<String> keySetGroupDO = new LinkedList<>();
    private int total = 0;
    private int scanned = 0;
    private int quantityNormal = 0;
    private int barcodeNumber;
    private int positionJustScan = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mapView();
        setListenerView();
        initial();
    }

    private void mapView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.lv_order_detail);
        etBarcode = (EditText) findViewById(R.id.barcode);
        totalQuantity = (TextView) findViewById(R.id.tv_total);
        totalScanned = (TextView) findViewById(R.id.tv_scanned);
        etTakeScannerResult = (EditText) findViewById(R.id.etTakeScannerResult);
        ivCamera = (Button) findViewById(R.id.scan_camera);
    }

    private void setListenerView() {
        findViewById(R.id.so_do_day).setOnClickListener(this);
        findViewById(R.id.take_picture).setOnClickListener(this);
        findViewById(R.id.worker).setOnClickListener(this);
        findViewById(R.id.scan_camera).setOnClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    private void initial() {
        deviceNumber = Utilities.getAndroidID(getApplicationContext());
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        orderNumber = getIntent().getStringExtra(ORDER_NUMBER);

        Utilities.showBackIcon(getSupportActionBar());

        getRequest();
        adapter = new OrderDetailAdapter(this, completedList);
        listView.setAdapter(adapter);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderDetail(listView);
            }
        };
        getOrderDetail(listView);
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

    public void getOrderDetail(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getDetailPhieu(new OrdersInfo(scanResult, orderNumber, userName, deviceNumber)).enqueue(new Callback<List<OrderDetail>>() {
            @Override
            public void onResponse(Response<List<OrderDetail>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {

                    resetUI();

                    groupDO(response.body());

                    merge();

                    updateUI();

                    checkAutoScan();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(OrderDetailActivity.this, t, TAG, view, action);
            }
        });
    }

    private void checkAutoScan() {
        if (isClickedFromCamera && quantityNormal > 0)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivCamera.performClick();
                }
            }, 1000);
    }

    private void updateUI() {
        totalScanned.setText(String.format(Locale.US, "%d", scanned));
        totalQuantity.setText(String.format(Locale.US, "%d", total));
        adapter.notifyDataSetChanged();
        if (positionJustScan != -1)
            listView.smoothScrollToPosition(positionJustScan);
    }

    private void resetUI() {
        completedList.clear();
        positionJustScan = -1;
        total = 0;
        scanned = 0;
        quantityNormal = 0;
    }

    public HashMap<String, List<OrderDetail>> groupDO(List<OrderDetail> listOrderDetail) {

        for (OrderDetail orderDetail : listOrderDetail) {

            String result = orderDetail.getResult();

            if (isResultScanValidWithCaseFilter(result)) {

                if (result.equals(""))
                    quantityNormal++;
                int quantity = Integer.parseInt(orderDetail.getQuantityOfPackages());
                total += quantity;
                if (result.equalsIgnoreCase("OK") || result.equalsIgnoreCase("XX"))
                    scanned += quantity;

                String labelDO = orderDetail.getDO();
                if (groupDO.containsKey(labelDO)) {
                    groupDO.get(labelDO).add(orderDetail);
                } else {
                    keySetGroupDO.add(labelDO);
                    List<OrderDetail> items = new LinkedList<>();
                    items.add(orderDetail);
                    groupDO.put(labelDO, items);
                }
            }
        }
        return groupDO;
    }

    private boolean isResultScanValidWithCaseFilter(String orderResult) {
        return filterResult == 0 || (filterResult == 1 && orderResult.equals(" ") || (filterResult == 2 && (orderResult.equals("OK") || orderResult.equals("XX"))));
    }

    public List<Item> merge() {
        for (String labelDO : keySetGroupDO) {
            List<OrderDetail> items = groupDO.get(labelDO);

            DO DO = new DO(items.get(0).getDO() + " ~ " + items.get(0).getSpecialRequirement());
            completedList.add(DO);

            int sizeGroupDO = items.size();
            String tmpProductNumber = "";
            Product product = null;

            for (int i = 0; i < sizeGroupDO; i++) {
                OrderDetail orderDetail = items.get(i);

                String productNumber = orderDetail.getProductNumber();
                int quantity = Integer.parseInt(orderDetail.getQuantityOfPackages());

                if (notSameProductNumberInGroupDO(tmpProductNumber, productNumber)) {

                    product = new Product(productNumber + " ~ " + orderDetail.getProductName(), quantity);
                    completedList.add(product);
                    tmpProductNumber = productNumber;

                } else {
                    assert product != null;
                    product.setTotal(product.getTotal() + quantity);
                }
                if (barcodeNumber != 0 && orderDetail.getPalletID().contains(barcodeNumber + ""))
                    positionJustScan = completedList.size();

                completedList.add(orderDetail);
            }
        }
        return completedList;
    }

    private boolean notSameProductNumberInGroupDO(String tmpProductNumber, String productNumber) {
        return !tmpProductNumber.equals(productNumber);
    }


    public void getRequest() {
        MyRetrofit.initRequest(this).getRequestPhieu(orderNumber).enqueue(new Callback<List<RequirementInfo>>() {
            @Override
            public void onResponse(Response<List<RequirementInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    StringBuilder message = new StringBuilder();
                    for (RequirementInfo info : response.body())
                        message.append(info.getRequirement()).append("\n");
                    if (message.toString().length() > 0)
                        Utilities.basicDialog(OrderDetailActivity.this, message.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
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
                    item_sort.setTitle(getResources().getString(R.string.product_no));
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
                getOrderDetail(listView);
                break;
            case R.id.action_filter_yet_scan:
                filterResult = 1;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.not_yet_scan));
                }
                getOrderDetail(listView);
                break;
            case R.id.action_filter_scanned:
                filterResult = 2;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.scanned));
                }
                getOrderDetail(listView);
                break;
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
            intent.putExtra(DO_ID, Integer.parseInt(orderNumber.split("-")[1]));
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Item child = adapter.getItem(position);

        if (child instanceof OrderDetail) {
            final OrderDetail item = (OrderDetail) child;

            View content = View.inflate(this, R.layout.item_dialog_detail_phieu, null);
            final CheckBox cbChecked = (CheckBox) content.findViewById(R.id.cb_detail_phieu_checked);
            if (item.getResult().equalsIgnoreCase(getString(R.string.ok)))
                cbChecked.setChecked(true);
            final EditText etRemark = (EditText) content.findViewById(R.id.et_detail_phieu_remark);
            etRemark.setText(item.getRemark());
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                    .setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.hideKeyboard(OrderDetailActivity.this);
                        }
                    })
                    .setNeutralButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.hideKeyboard(OrderDetailActivity.this);
                            boolean checked = cbChecked.isChecked();
                            String remark = etRemark.getText().toString();
                            updatePalletID(listView, checked, item.getDispatchingOrderDetailID(), remark);
                        }
                    })
                    .setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) {
                            AlertDialog dialog = new AlertDialog.Builder(OrderDetailActivity.this)
                                    .setMessage("Bạn có chắc muốn xóa phiếu này?")
                                    .setPositiveButton(getString(R.string.no), null).
                                            setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DispatchingOrderScannedDeleteParameter parameter = new DispatchingOrderScannedDeleteParameter(item.BarcodeScanDetailID, item.getDispatchingOrderDetailID(), userName);
                                                    executeDispatchingOrderScannedDelete(listView, parameter);
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

        return false;
    }

    private void updatePalletID(final View view, boolean checked, int orderId, String remark) {
        UpdateDispatchingOrderDetailParameter parameter = new UpdateDispatchingOrderDetailParameter(checked, orderId, remark, userName, orderNumber, deviceNumber);
        MyRetrofit.initRequest(this).updateDispatchingOrderDetail(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null) {
                    getOrderDetail(listView);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(OrderDetailActivity.this, t, TAG, view);
            }
        });
    }

    private void executeDispatchingOrderScannedDelete(final View view, DispatchingOrderScannedDeleteParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
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
                    dialog.dismiss();
                    getOrderDetail(view);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(OrderDetailActivity.this, t, TAG, view);
            }
        });
    }

    public class AsyncUiControlUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            etTakeScannerResult.setText("");
            etBarcode.setText(result);
            String type;
            try {
                type = result.substring(0, 2);
                barcodeNumber = Integer.parseInt(result.substring(2, result.length()));
            } catch (NumberFormatException ex) {
                type = result.substring(0, 3);
                barcodeNumber = Integer.parseInt(result.substring(3, result.length()));
            }

            if (type.equalsIgnoreCase("DO") || type.equalsIgnoreCase("DP")) {
                orderNumber = String.format(Locale.getDefault(), "%s-%d", type, barcodeNumber);
                getRequest();
            } else
                scanResult = result;
            getOrderDetail(listView);
        }

    }
}
