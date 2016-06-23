package com.scsvn.whc_2016.main.kiemqa.metroqacheckingcarton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MetroQACheckingCartonDelUpdateParameter;
import com.scsvn.whc_2016.retrofit.MetroQACheckingCartonInsertParameter;
import com.scsvn.whc_2016.retrofit.MetroQACheckingCartonParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MetroCartonActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private final String TAG = MetroCartonActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_metro_carton_product_name)
    TextView tvProductName;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private View.OnClickListener tryAgain;
    private MetroCartonAdapter adapter;
    private int roID;
    private ArrayList<MetroCartonInfo> arrayList = new ArrayList<>();
    private int switchNewInsert;
    private float damageWeight;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_carton);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        roID = getIntent().getIntExtra("RO_ID", 0);
        tvProductName.setText(productName);
        username = LoginPref.getUsername(this);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMetroQACheckingCarton(listView);
            }
        };
        adapter = new MetroCartonAdapter(this, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        getMetroQACheckingCarton(listView);
    }

    private void getMetroQACheckingCarton(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).getMetroQACheckingCarton(new MetroQACheckingCartonParameter(roID)).enqueue(new Callback<List<MetroCartonInfo>>() {
            @Override
            public void onResponse(Response<List<MetroCartonInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    arrayList.clear();
                    arrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(MetroCartonActivity.this, t, TAG, view, tryAgain);
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.fab)
    public void cartonAdd() {
        if (switchNewInsert % 2 == 0) {
            fab.setImageResource(R.drawable.ic_action_luu);
            MetroCartonInfo info = new MetroCartonInfo();
            info.setNew(true);
            int index = arrayList.size() > 0 ? arrayList.get(arrayList.size() - 1).getCheckingCartonIndex() + 1 : 1;
            info.setCheckingCartonIndex(index);
            arrayList.add(info);
            adapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(arrayList.size() - 1);
            switchNewInsert++;
            if (switchNewInsert > 1) {
                int lastChildIndex = listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();
                EditText etWeight = (EditText) listView.getChildAt(lastChildIndex).findViewById(R.id.item_et_metro_carton_weight);
                Utilities.showKeyboard(this, etWeight);
            }
        } else {
            int lastChildIndex = listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();
            EditText etWeight = (EditText) listView.getChildAt(lastChildIndex).findViewById(R.id.item_et_metro_carton_weight);
            EditText etDamageWeight = (EditText) listView.getChildAt(lastChildIndex).findViewById(R.id.item_et_metro_carton_damage_weight);
            TextView tvDamageWeightPercent = (TextView) listView.getChildAt(lastChildIndex).findViewById(R.id.item_tv_metro_carton_damage_percent);
            if (etWeight.getText().length() == 0 || Float.parseFloat(etWeight.getText().toString()) == 0)
                return;
            try {
                damageWeight = Float.parseFloat(etDamageWeight.getText().toString());
            } catch (NumberFormatException ex) {
                Log.d(TAG, "cartonAdd() returned: " + ex.getMessage());
            }
            float weight = Float.parseFloat(etWeight.getText().toString());
            tvDamageWeightPercent.setText(String.format(Locale.US, "%.2f%%", (damageWeight / weight) * 100));
            etDamageWeight.setText(NumberFormat.getInstance().format(damageWeight));
            MetroQACheckingCartonInsertParameter parameter = new MetroQACheckingCartonInsertParameter(
                    weight,
                    damageWeight,
                    roID,
                    username
            );
            Log.d(TAG, "cartonAdd() returned: " + new Gson().toJson(parameter));
            executeMetroQACheckingCartonInsert(listView, parameter);
            listView.requestFocus();
        }
    }

    private void executeMetroQACheckingCartonInsert(final View view, final MetroQACheckingCartonInsertParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang lưu dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).executeMetroQACheckingCartonInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                dialog.dismiss();
                if (response.isSuccess() && response.body() != null && response.body().length() > 0) {
                    MetroCartonInfo cartonInfo = arrayList.get(arrayList.size() - 1);
                    cartonInfo.setReceivingCartonCheckingID(Integer.parseInt(response.body()));
                    cartonInfo.setCheckingWeighPerCarton(parameter.getCheckingWeighPerCarton());
                    cartonInfo.setDamageWeighPerCarton(parameter.getDamageWeighPerCarton());
                    cartonInfo.setNew(false);
                    adapter.notifyDataSetChanged();
                    fab.setImageResource(R.drawable.ic_action_them);
                    switchNewInsert++;
                    Toast.makeText(MetroCartonActivity.this, "Đã lưu", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(MetroCartonActivity.this, "Có lỗi xảy ra, dữ liệu chưa được lưu", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MetroCartonActivity.this, "Có lỗi xảy ra, dữ liệu chưa được lưu", Toast.LENGTH_LONG).show();
                RetrofitError.errorNoAction(MetroCartonActivity.this, t, TAG, view);
                dialog.dismiss();
            }
        });
    }

    private void executeMetroQACheckingCartonDelUpdate(final View view, final MetroQACheckingCartonDelUpdateParameter parameter, final int position) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang xóa");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).executeMetroQACheckingCartonDelUpdate(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                dialog.dismiss();
                if (response.isSuccess() && response.body() != null) {
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(MetroCartonActivity.this, "Có lỗi xảy ra, dữ liệu chưa được xóa", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MetroCartonActivity.this, "Có lỗi xảy ra, dữ liệu chưa được xóa", Toast.LENGTH_LONG).show();
                RetrofitError.errorNoAction(MetroCartonActivity.this, t, TAG, view);
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.d(TAG, "onItemLongClick() returned: " + position);
        final MetroCartonInfo item = adapter.getItem(position);
        if (!item.isNew()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MetroCartonActivity.this)
                    .setMessage(String.format(Locale.US, "Bạn có chắc muốn xóa thùng số %d này?", item.getCheckingCartonIndex()))
                    .setNegativeButton("Không", null)
                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MetroQACheckingCartonDelUpdateParameter parameter = new MetroQACheckingCartonDelUpdateParameter(
                                    0, 0, 1, item.getReceivingCartonCheckingID(), username
                            );
                            executeMetroQACheckingCartonDelUpdate(listView, parameter, position);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return true;
        }
        return false;

    }
}
