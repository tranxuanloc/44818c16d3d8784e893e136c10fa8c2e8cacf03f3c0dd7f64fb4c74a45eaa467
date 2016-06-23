package com.scsvn.whc_2016.main.vitritrong;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FreeLocationActivity extends BaseActivity {
    public static final String TAG = FreeLocationActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tvFreeLocationFreeQtyBot)
    TextView tvFreeQty;
    @Bind(R.id.tvFreeLocationHBot)
    TextView tvH;
    @Bind(R.id.tvFreeLocationLBot)
    TextView tvL;
    @Bind(R.id.tvFreeLocationVHBot)
    TextView tvVH;
    @Bind(R.id.tvFreeLocationVLBot)
    TextView tvVL;
    @Bind(R.id.tvFreeLocationWFBot)
    TextView tvWF;
    @Bind(R.id.tvFreeLocationPalletQtyBot)
    TextView tvPalletQty;
    @Bind(R.id.tvFreeLocationTotalBot)
    TextView tvTotal;
    private View.OnClickListener tryAgain;
    private FreeLocationAdapter adapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_location);
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
                getFreeLocation(listView);
            }
        };
        adapter = new FreeLocationAdapter(this, new ArrayList<FreeLocationInfo>());
        listView.setAdapter(adapter);
        executeFreeLocationUpdate(listView);
    }

    private void getFreeLocation(final View view) {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getFreeLocation().enqueue(new Callback<List<FreeLocationInfo>>() {
            @Override
            public void onResponse(Response<List<FreeLocationInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    if (response.body().size() > 0) {
                        int qtyOfFree = 0;
                        int qtyOfPallets_onHand = 0;
                        int qtyFree_high = 0;
                        int qtyFree_low = 0;
                        int qtyFree_veryHigh = 0;
                        int qtyFree_veryLow = 0;
                        int qtyFreeAfterDP = 0;
                        int qtyTotal = 0;
                        for (FreeLocationInfo info : response.body()) {
                            qtyOfFree += info.getQtyOfFree();
                            qtyOfPallets_onHand += info.getQtyOfPallets_OnHand();
                            qtyFree_high += info.getQtyFree_High();
                            qtyFree_low += info.getQtyFree_Low();
                            qtyFree_veryHigh += info.getQtyFree_VeryHigh();
                            qtyFree_veryLow += info.getQtyFree_VeryLow();
                            qtyFreeAfterDP += info.getQtyFreeAfterDP();
                            qtyTotal += info.getQtyLocation();
                        }
                        tvFreeQty.setText(NumberFormat.getInstance().format(qtyOfFree));
                        tvH.setText(NumberFormat.getInstance().format(qtyFree_high));
                        tvL.setText(NumberFormat.getInstance().format(qtyFree_low));
                        tvVH.setText(NumberFormat.getInstance().format(qtyFree_veryHigh));
                        tvVL.setText(NumberFormat.getInstance().format(qtyFree_veryLow));
                        tvWF.setText(NumberFormat.getInstance().format(qtyFreeAfterDP));
                        tvPalletQty.setText(NumberFormat.getInstance().format(qtyOfPallets_onHand));
                        tvTotal.setText(NumberFormat.getInstance().format(qtyTotal));

                    } else {
                        tvFreeQty.setText("0");
                        tvH.setText("0");
                        tvL.setText("0");
                        tvVH.setText("0");
                        tvVL.setText("0");
                        tvWF.setText("0");
                        tvPalletQty.setText("0");
                    }

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(FreeLocationActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    private void executeFreeLocationUpdate(final View view) {
        dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).executeFreeLocationUpdate().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    getFreeLocation(view);
                } else
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(FreeLocationActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return true;
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
