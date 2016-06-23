package com.scsvn.whc_2016.main.palletcartonchecking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
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
import com.scsvn.whc_2016.login.LoginActivity;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.PalletCartonParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KiemPalletCartonNoEMDKActivity extends BaseActivity {
    @Bind(R.id.listView)
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
    @Bind(R.id.etTakeScannerResult)
    EditText etTakeScannerResult;

    public static final String TAG = KiemPalletCartonNoEMDKActivity.class.getSimpleName();
    private ProgressDialog dialog;
    private View.OnClickListener tryAgain;
    private String scanResult = "";
    private int eventKeycode;
    private MovementHistoryAdapter adapter;

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

    private void getPalletCarton(final View view) {
        Utilities.hideKeyboard(this);
        dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
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
                RetrofitError.errorWithAction(KiemPalletCartonNoEMDKActivity.this, t, TAG, view, tryAgain);
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
        tvCustomerRef.setText(info.getCustomerRef());
        if (info.getCustomerRef().length() > 0)
            tvCustomerRef.setVisibility(View.VISIBLE);
        else
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
                RetrofitError.errorWithAction(KiemPalletCartonNoEMDKActivity.this, t, TAG, view, tryAgain);
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
                String contents = result.getContents();
                updateUIWithScanResult(contents);
            }
        }
    }

    private void updateUIWithScanResult(String contents) {
        etTakeScannerResult.setText("");
        scanResult = contents;
        etScanResult.setText(scanResult);
        getPalletCarton(listView);
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
