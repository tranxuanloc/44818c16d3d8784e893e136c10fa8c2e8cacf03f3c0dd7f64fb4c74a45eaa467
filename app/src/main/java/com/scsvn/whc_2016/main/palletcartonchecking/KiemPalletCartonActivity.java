package com.scsvn.whc_2016.main.palletcartonchecking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.PalletFindParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KiemPalletCartonActivity extends BaseActivity {
    public static final String TAG = KiemPalletCartonActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.lv_pallet_find)
    ListView palletFindLV;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.etScanResult)
    EditText etScanResult;
    @Bind(R.id.etTakeScannerResult)
    EditText etTakeScannerResult;
    @Bind(R.id.title)
    TextView titleTV;
    @Bind(R.id.tv_pallet_carton_header)
    TextView headerTV;
    private ProgressDialog dialog;
    private View.OnClickListener tryAgain;
    private String scanResult = "";
    private int eventKeycode;
    private MovementHistoryAdapter adapter;
    private PalletFindAdapter palletFindAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pallet_carton_checking);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initialUI();
    }

    private void initialUI() {

        snackBarView = listView;
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPalletFind();
            }
        };
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (scanResult.length() > 0)
                    getPalletFind();
                else
                    refreshLayout.setRefreshing(false);
            }
        });
        etScanResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    scanResult = etScanResult.getText().toString();
                    getPalletFind();
                }
                return false;
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
                    new UpdateScanResultAsync().execute(contents.replace("\n", ""));
            }
        });
        palletFindAdapter = new PalletFindAdapter(this, new ArrayList<PalletFind>());
        palletFindLV.setAdapter(palletFindAdapter);
    }


    private void setUnderLine(TextView view, String content) {
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        view.setText(spannableString);
    }

    private void getPalletFind(final int palletId) {
        Utilities.hideKeyboard(this);
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getPalletFind(new PalletFindParameter(palletId)).enqueue(new Callback<List<PalletFind>>() {
            @Override
            public void onResponse(Response<List<PalletFind>> response, Retrofit retrofit) {
                List<PalletFind> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    palletFindAdapter.clear();
                    palletFindAdapter.addAll(body);
                    PalletFind palletFind = body.get(0);
                    titleTV.setText(String.format(Locale.getDefault(), "%s~%s", palletFind.getCustomerNumber(), palletFind.getCustomerName()));
                    String header = String.format("Product: %s - %s", palletFind.getProductNumber(), palletFind.getProductName());
                    headerTV.setText(header);
                }
                getMovementHistory();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(KiemPalletCartonActivity.this, t, TAG, snackBarView, tryAgain);
            }
        });
    }

    private void getMovementHistory() {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getMovementHistory(scanResult).enqueue(new Callback<List<MovementHistoryInfo>>() {
            @Override
            public void onResponse(Response<List<MovementHistoryInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dialog.dismiss();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(KiemPalletCartonActivity.this, t, TAG, snackBarView, tryAgain);
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
                new UpdateScanResultAsync().execute(contents);
            }
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
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    private void getPalletFind() {
        if (!scanResult.contains("PI"))
            scanResult = "PI" + scanResult;
        try {
            String idString = scanResult.replaceAll("\\D*", "");
            Log.d(TAG, "onPostExecute() returned: " + idString);
            getPalletFind(Integer.parseInt(idString));
        } catch (NumberFormatException ignored) {

        }
    }

    public class UpdateScanResultAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String s) {
            etTakeScannerResult.setText("");

            scanResult = s;
            etScanResult.setText(scanResult);
            getPalletFind();
        }
    }
}
