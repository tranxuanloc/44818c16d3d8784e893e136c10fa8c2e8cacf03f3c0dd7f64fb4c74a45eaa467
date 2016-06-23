package com.scsvn.whc_2016.main.giaonhanhoso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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

public class GiaoHoSoDetailNoEMDKActivity extends BaseActivity implements AdapterView.OnItemLongClickListener {
    public static final String TAG = GiaoHoSoDetailNoEMDKActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 1411;

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.ivThumbSignature)
    ImageView ivThumbSignature;
    @Bind(R.id.btGhsSign)
    Button btGhsSign;
    @Bind(R.id.etTakeScannerResult)
    EditText etTakeScannerResult;
    @Bind(R.id.tvQuantityScanned)
    TextView tvQuantityScanned;
    private int eventKeycode;
    private View.OnClickListener tryAgain;
    private String userName;
    private GiaoHoSoDetailAdapter adapter;
    private String orderNumber;
    private int orderID, showImage;
    private int SIGN_ACTIVITY_CODE = 100;
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
            getSupportActionBar().setTitle(String.format(getString(R.string.ho_so_x), orderNumber));
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
        getDSDispatchingOrdersDetail(listView);
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
                    updateUIWithScanResult(contents.replace("\n", ""));
            }
        });
    }

    private void getDSDispatchingOrdersDetail(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
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
                                ivThumbSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(getApplicationContext()) / 2));
                                Utilities.getPicasso(getApplicationContext()).load(Utilities.generateUrlImage(getApplicationContext(), attachmentFile)).into(ivThumbSignature);
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
                RetrofitError.errorWithAction(GiaoHoSoDetailNoEMDKActivity.this, t, TAG, view, tryAgain);
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
            updateUIWithScanResult(contents);
        } else if (requestCode == SIGN_ACTIVITY_CODE && resultCode == RESULT_OK) {
            String filePath = intent.getStringExtra("filePath");
            if (filePath != null) {
//                Uri uriImage = Uri.fromFile(new File(filePath));
                SendMailParameter parameter = new SendMailParameter(orderNumber, userName);
                sendMail(listView, parameter);
                int sampleSize = 0;
                try {
                    sampleSize = ResizeImage.resizeImageFromFile(filePath, Const.IMAGE_UPLOAD_WIDTH);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = sampleSize;
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
                RetrofitError.errorNoAction(GiaoHoSoDetailNoEMDKActivity.this, t, TAG, view);
            }
        });
    }

    private void executeDSROCartonDelete(final View view, DSROCartonDeleteParameter parameter, final int position) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang xóa...");
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
                    Snackbar.make(listView, "Đã xóa", Snackbar.LENGTH_LONG).show();
                    if ((item.getScannedType() == 3 || item.getScannedType() == 6) && (item.getResult().equalsIgnoreCase("OK") || item.getResult().equalsIgnoreCase("XX")))
                        --totalOK;
                    --totalOrder;
                    tvQuantityScanned.setText(String.format(Locale.US, "%d / %d", totalOK, totalOrder));
                    if (totalOK == totalOrder)
                        btGhsSign.setEnabled(true);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(GiaoHoSoDetailNoEMDKActivity.this, t, TAG, view);
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
                    updateUIWithScanResult("");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(GiaoHoSoDetailNoEMDKActivity.this, t, TAG, view);
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
                RetrofitError.errorNoAction(GiaoHoSoDetailNoEMDKActivity.this, t, TAG, view);
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
                RetrofitError.errorNoAction(GiaoHoSoDetailNoEMDKActivity.this, t, TAG, view);
            }
        });
    }

    private void updateUIWithScanResult(String contents) {
        etTakeScannerResult.setText("");
        etScanResult.setText(contents);
        scanResult = contents;
        getDSDispatchingOrdersDetail(listView);
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
                Utilities.showKeyboard(GiaoHoSoDetailNoEMDKActivity.this, etDescription);
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
                    Utilities.showKeyboard(GiaoHoSoDetailNoEMDKActivity.this, etDescription);
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
                Utilities.hideKeyboard(GiaoHoSoDetailNoEMDKActivity.this);
                getDSDispatchingOrdersDetail(listView);
            }
        });
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
                            Utilities.hideKeyboard(GiaoHoSoDetailNoEMDKActivity.this);
                        }
                    })
                    .setNeutralButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.hideKeyboard(GiaoHoSoDetailNoEMDKActivity.this);
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
                            AlertDialog dialog = new AlertDialog.Builder(GiaoHoSoDetailNoEMDKActivity.this)
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
}
