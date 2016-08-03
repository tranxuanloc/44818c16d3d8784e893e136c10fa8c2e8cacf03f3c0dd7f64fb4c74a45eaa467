package com.scsvn.whc_2016.main.vitritrong;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FreeLocationDetailsActivity extends AppCompatActivity {

    public static final String TAG = "FreeLocationDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_location_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        String roomId = getIntent().getStringExtra("ROOM_ID");
        ListView listView = (ListView) findViewById(R.id.lvOrderDetail);
        getFreeLocationDetails(listView, roomId);
    }

    private void getFreeLocationDetails(final ListView listView, String roomID) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, listView);
            return;
        }
        MyRetrofit.initRequest(this).getFreeLocationDetails(roomID).enqueue(new Callback<List<FreeLocationDetailsInfo>>() {
            @Override
            public void onResponse(Response<List<FreeLocationDetailsInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    listView.setAdapter(new ArrayAdapter<>(FreeLocationDetailsActivity.this, android.R.layout.simple_list_item_1, response.body().toArray()));
                    dialog.show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(FreeLocationDetailsActivity.this, t, TAG, listView);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}
