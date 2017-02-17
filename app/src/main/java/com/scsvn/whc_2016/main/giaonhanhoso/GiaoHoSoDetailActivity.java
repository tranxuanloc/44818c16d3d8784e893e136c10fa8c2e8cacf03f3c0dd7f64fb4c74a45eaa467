package com.scsvn.whc_2016.main.giaonhanhoso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.scsvn.whc_2016.main.giaonhanhoso.cartonreturn.DispatchingOrderReturnedActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.DSCartonCategoriesParameter;
import com.scsvn.whc_2016.retrofit.DSCreateNewCartonParameter;
import com.scsvn.whc_2016.retrofit.DSOrderDetailParameter;
import com.scsvn.whc_2016.retrofit.DSROCartonDeleteParameter;
import com.scsvn.whc_2016.retrofit.DSRODOCartonUpdateParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.SendMailParameter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.ResizeImage;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GiaoHoSoDetailActivity extends BaseActivity implements Scanner.DataListener, Scanner.StatusListener, AdapterView.OnItemLongClickListener {
    public static final String TAG = GiaoHoSoDetailActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 1411;

    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.ivThumbSignature)
    ImageView ivThumbSignature;
    @Bind(R.id.btGhsSign)
    Button btGhsSign;
    @Bind(R.id.tvQuantityScanned)
    TextView tvQuantityScanned;

    private View.OnClickListener tryAgain;
    private String userName;
    private GiaoHoSoDetailAdapter adapter;
    private String orderNumber;
    private int orderID, showImage;
    private int SIGN_ACTIVITY_CODE = 100;
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;
    private String scanResult = "";
    private int customerID;
    private List<DSCartonCategoriesInfo> listCategory = new ArrayList<>();
    private ArrayList<String> listCategoryDes = new ArrayList<>();
    private String orderType;
    private boolean isRD;
    private int totalOK;
    private int totalOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_ho_so_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        orderNumber = getIntent().getStringExtra("orderID");
        orderType = getIntent().getStringExtra("ORDER_TYPE");
        customerID = Integer.parseInt(getIntent().getStringExtra("CUSTOMER_ID"));
        isRD = orderNumber.substring(0, 2).equals("RD");
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(String.format("Hồ sơ %s", orderNumber));
        orderID = Integer.parseInt(orderNumber.substring(3));
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);

        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDSDispatchingOrdersDetail(listView);
            }
        };
        adapter = new GiaoHoSoDetailAdapter(this, new ArrayList<DSDispatchingOrderDetailsInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        new AsyncEMDK().execute();
        getDSDispatchingOrdersDetail(listView);
    }

    private void getDSDispatchingOrdersDetail(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).getDSDispatchingOrderDetails(new DSOrderDetailParameter(orderNumber, scanResult, userName)).enqueue(new Callback<List<DSDispatchingOrderDetailsInfo>>() {
            @Override
            public void onResponse(Response<List<DSDispatchingOrderDetailsInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    totalOK = 0;
                    getDSCartonCategories(listView);
                    adapter.clear();
                    adapter.addAll(response.body());
                    totalOrder = response.body().size();
                    if (totalOrder > 0) {
                        for (DSDispatchingOrderDetailsInfo info : response.body())
                            if ((info.getScannedType() == 3 || info.getScannedType() == 6) && (info.getResult().equalsIgnoreCase("OK") || info.getResult().equalsIgnoreCase("XX")))
                                totalOK++;
                        tvQuantityScanned.setText(String.format(Locale.US, "%d / %d", totalOK, totalOrder));
                        String attachmentFile = response.body().get(0).getAttachmentFile();
                        if (attachmentFile.length() > 0) {
                            if (showImage == 0) {
                                showImage++;
                                btGhsSign.setEnabled(false);
                                ivThumbSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(GiaoHoSoDetailActivity.this) / 2));
                                Utilities.getPicasso(GiaoHoSoDetailActivity.this).load(Utilities.generateUrlImage(GiaoHoSoDetailActivity.this, attachmentFile)).into(ivThumbSignature);
                            }
                        } else if (totalOK == totalOrder)
                            btGhsSign.setEnabled(true);
                        else
                            btGhsSign.setEnabled(false);

                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(GiaoHoSoDetailActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    @OnClick(R.id.btGhsScanCamera)
    public void scanCamera() {
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
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String contents = result.getContents();
            etScanResult.setText(contents);
            scanResult = contents;
            getDSDispatchingOrdersDetail(listView);

        } else if (requestCode == SIGN_ACTIVITY_CODE && resultCode == RESULT_OK) {
            String filePath = intent.getStringExtra("filePath");
            if (filePath != null) {
//                Uri uriImage = Uri.fromFile(new File(filePath));
                SendMailParameter parameter = new SendMailParameter(orderNumber, userName);
                sendMail(listView, parameter);
                try {
                    ResizeImage.resizeImageFromFile(filePath, Const.IMAGE_UPLOAD_WIDTH);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = Const.SAMPLE_SIZE;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                ivThumbSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
                ivThumbSignature.setImageBitmap(bitmap);
                btGhsSign.setEnabled(false);

            }
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getDSDispatchingOrdersDetail(listView);
        }
    }

    private void sendMail(final View view, SendMailParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang gửi email...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).sendMail(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null)
                    Snackbar.make(listView, response.body(), Snackbar.LENGTH_LONG).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(GiaoHoSoDetailActivity.this, t, TAG, view);
            }
        });
    }

    private void executeDSRODOCartonUpdate(final View view, DSRODOCartonUpdateParameter parameter, final int position) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang cập nhật...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).executeDSRODOCartonUpdate(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    scanResult = "";
                    getDSDispatchingOrdersDetail(listView);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(GiaoHoSoDetailActivity.this, t, TAG, view);
            }
        });
    }

    private void getDSCartonCategories(final View view) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).getDSCartonCategories(new DSCartonCategoriesParameter(customerID)).enqueue(new Callback<List<DSCartonCategoriesInfo>>() {
            @Override
            public void onResponse(Response<List<DSCartonCategoriesInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    listCategory = response.body();
                    listCategoryDes.clear();
                    for (DSCartonCategoriesInfo info : listCategory)
                        listCategoryDes.add(info.getCategoryDescriptionVietnam());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(GiaoHoSoDetailActivity.this, t, TAG, view);
            }
        });
    }

    private void executeDSCreateNewCarton(final View view, DSCreateNewCartonParameter parameter, final EditText etDescription, final EditText etReference) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang thêm carton...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).executeDSCreateNewCarton(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    etDescription.setText("");
                    etReference.setText("");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(GiaoHoSoDetailActivity.this, t, TAG, view);
            }
        });
    }

    private void executeDSROCartonDelete(final View view, DSROCartonDeleteParameter parameter, final int position) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).executeDSROCartonDelete(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    DSDispatchingOrderDetailsInfo item = adapter.getItem(position);
                    adapter.remove(item);
                    adapter.notifyDataSetChanged();
                    if ((item.getScannedType() == 3 || item.getScannedType() == 6) && (item.getResult().equalsIgnoreCase("OK") || item.getResult().equalsIgnoreCase("XX")))
                        --totalOK;
                    --totalOrder;
                    tvQuantityScanned.setText(String.format(Locale.US, "%d / %d", totalOK, totalOrder));
                    Snackbar.make(listView, "Đã xóa", Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(GiaoHoSoDetailActivity.this, t, TAG, view);
            }
        });
    }

    @OnClick(R.id.btGhsSign)
    public void signClick() {
        Intent intent = new Intent(this, SignActivity.class);
        intent.putExtra("orderID", orderNumber);
        startActivityForResult(intent, SIGN_ACTIVITY_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (orderNumber.substring(0, 2).equals("RD"))
            getMenuInflater().inflate(R.menu.giao_ho_so_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        else if (id == R.id.action_receive_document) {
            if (orderType.equalsIgnoreCase("Carton New")) addCarton();
            else {
                Intent intent = new Intent(this, DispatchingOrderReturnedActivity.class);
                intent.putExtra("CUSTOMER_ID", customerID);
                intent.putExtra("RO_ID", orderID);
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCarton() {
        View view = LayoutInflater.from(this).inflate(R.layout.add_dispatching_order, null);
        final AppCompatSpinner spCategory = (AppCompatSpinner) view.findViewById(R.id.sp_add_dispatching_order_category);
        final EditText etDescription = (EditText) view.findViewById(R.id.et_add_dispatching_order_description);
        final EditText etReference = (EditText) view.findViewById(R.id.et_add_dispatching_order_reference);
        final EditText etSize = (EditText) view.findViewById(R.id.et_add_dispatching_order_size);

        spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listCategoryDes));
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Utilities.showKeyboard(GiaoHoSoDetailActivity.this, etDescription);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isEmpty(etDescription)) {
                    Utilities.showKeyboard(GiaoHoSoDetailActivity.this, etDescription);
                    return;
                }
                DSCreateNewCartonParameter parameter = new DSCreateNewCartonParameter(
                        customerID,
                        userName,
                        etDescription.getText().toString(),
                        etReference.getText().toString(),
                        Float.parseFloat(etSize.getText().toString()),
                        listCategory.get(spCategory.getSelectedItemPosition()).getDSCartonCategoryID(),
                        orderID
                );
                executeDSCreateNewCarton(listView, parameter, etDescription, etReference);

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utilities.hideKeyboard(GiaoHoSoDetailActivity.this);
                getDSDispatchingOrdersDetail(listView);
            }
        });
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
            for (ScanDataCollection.ScanData data : scanData) {
                String barcode = data.getData();
                new AsyncUiControlUpdate().execute(barcode);
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

    private void initScanner() {

        if (scanner == null) {
            List<ScannerInfo> deviceList = barcodeManager.getSupportedDevicesInfo();
            int numberDevices = deviceList.size();
            if ((deviceList != null) && (numberDevices != 0)) {
                scanner = barcodeManager.getDevice(deviceList.get(numberDevices > 0 ? 1 : 0));
            } else {
                Snackbar.make(etScanResult, "initScanner: " + "Failed to get the specified scanner device! Please close and restart the application.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    Snackbar.make(etScanResult, "initScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(etScanResult, "initScanner: " + "Failed to initialize the scanner device.", Snackbar.LENGTH_SHORT).show();
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
                //  Snackbar.make(etScanResult, "setDecoders: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                scanner.disable();

            } catch (ScannerException e) {
                Snackbar.make(etScanResult, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner.removeDataListener(this);
            scanner.removeStatusListener(this);
            try {
                scanner.release();
            } catch (ScannerException e) {
                Snackbar.make(etScanResult, "deInitScanner: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            scanner = null;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (isRD) {
            View content = View.inflate(this, R.layout.item_dialog_detail_phieu, null);
            final CheckBox cbChecked = (CheckBox) content.findViewById(R.id.cb_detail_phieu_checked);
            final DSDispatchingOrderDetailsInfo item = adapter.getItem(position);
            final EditText etRemark = (EditText) content.findViewById(R.id.et_detail_phieu_remark);
            etRemark.setText(item.getRemark());
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                    .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.hideKeyboard(GiaoHoSoDetailActivity.this);
                        }
                    })
                    .setNeutralButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.hideKeyboard(GiaoHoSoDetailActivity.this);
                            boolean checked = cbChecked.isChecked();
                            String remark = etRemark.getText().toString();
                            DSRODOCartonUpdateParameter parameter = new DSRODOCartonUpdateParameter(
                                    item.getDSROCartonID(),
                                    remark,
                                    checked,
                                    userName,
                                    orderNumber
                            );
                            executeDSRODOCartonUpdate(listView, parameter, position);

                        }
                    })
                    .setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) {
                            AlertDialog dialog = new AlertDialog.Builder(GiaoHoSoDetailActivity.this)
                                    .setMessage("Bạn có chắc muốn xóa thông tin về hồ sơ này?")
                                    .setNegativeButton("Không", null)
                                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DSROCartonDeleteParameter parameter = new DSROCartonDeleteParameter(adapter.getItem(position).getDSROCartonID(), userName);
                                            executeDSROCartonDelete(listView, parameter, position);
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

            return true;
        }

        return false;
    }

    private class AsyncEMDK extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EMDKResults results = EMDKManager.getEMDKManager(GiaoHoSoDetailActivity.this, new EMDKManager.EMDKListener() {
                @Override
                public void onOpened(EMDKManager emdkManager) {
                    Log.d(TAG, "onOpened: ");
                    GiaoHoSoDetailActivity.this.emdkManager = emdkManager;
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
                    Snackbar.make(etScanResult, "onClosed: " + "EMDK closed unexpectedly! Please close and restart the application.", Snackbar.LENGTH_LONG).show();
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
            scanResult = result;
            getDSDispatchingOrdersDetail(listView);
        }

    }

    private class AsyncStatusUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            etScanResult.setText(result);
        }
    }
}
