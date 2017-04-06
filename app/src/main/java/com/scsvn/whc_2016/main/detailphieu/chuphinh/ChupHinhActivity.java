package com.scsvn.whc_2016.main.detailphieu.chuphinh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.viewImage.ViewImageActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChupHinhActivity extends BaseActivity {
    public static final String TYPE = "type";
    public static boolean isUpdate;
    public static int PICK_IMAGE = 0;
    public static int CAPTURE_IMAGE = 1;
    private final String TAG = ChupHinhActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.tv_order_id)
    TextView tvOrderID;
    @Bind(R.id.tv_order_dock)
    TextView tvOrderDock;
    @Bind(R.id.tv_order_number)
    TextView tvOrderNumber;
    @Bind(R.id.tv_order_date)
    TextView tvOrderDate;
    @Bind(R.id.tv_order_customer_number)
    TextView tvCusNumber;
    @Bind(R.id.tv_order_customer_name)
    TextView tvCusName;
    @Bind(R.id.tv_order_special_requirement)
    TextView tvSpecialRequirement;
    private String orderNumber;
    private View.OnClickListener action;
    private AttachmentInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chup_hinh);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        if (getIntent() != null)
            orderNumber = getIntent().getStringExtra(ORDER_NUMBER);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderInfo(listView);
            }
        };
        adapter = new AttachmentInfoAdapter(ChupHinhActivity.this, new ArrayList<AttachmentInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                intent.putExtra("src", adapter.getItem(position).getAttachmentFile());
                startActivity(intent);
            }
        });
        getAttachmentInfo(listView);
        getOrderInfo(listView);
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        if (isUpdate)
            getAttachmentInfo(listView);
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    public void getOrderInfo(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getOrderInfo(orderNumber).enqueue(new Callback<List<OrderInfo>>() {
            @Override
            public void onResponse(Response<List<OrderInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    OrderInfo orderInfo = response.body().get(0);
                    tvOrderID.setText(String.format(Locale.getDefault(), "%d", orderInfo.getOrderID()));
                    tvOrderNumber.setText(orderInfo.getOrderNumber());
                    tvOrderDate.setText(orderInfo.getOrderDate());
                    tvCusNumber.setText(orderInfo.getCustomerNumber());
                    tvCusName.setText(orderInfo.getCustomerName());
                    tvOrderDock.setText(orderInfo.getDockNumber());
                    tvSpecialRequirement.setText(orderInfo.getSpecialRequirement());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(ChupHinhActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getAttachmentInfo(final View view) {
        MyRetrofit.initRequest(this).getAttachmentInfo(orderNumber).enqueue(new Callback<List<AttachmentInfo>>() {
            @Override
            public void onResponse(Response<List<AttachmentInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @OnClick(R.id.bt_take_picture)
    public void takePicture() {
        intentUpload(CAPTURE_IMAGE);
    }

    @OnClick(R.id.bt_browser_gallery)
    public void browserGallery() {
        intentUpload(PICK_IMAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void intentUpload(int type) {
        Intent intent = new Intent(this, UploadFileActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(ORDER_NUMBER, orderNumber);
        startActivity(intent);
    }


}
