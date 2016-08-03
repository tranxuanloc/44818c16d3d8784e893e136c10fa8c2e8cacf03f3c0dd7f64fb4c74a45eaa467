package com.scsvn.whc_2016.main.chuyenhang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.chuyenhang.lichsu.LichSuChuyenHangActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.ListLocationParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.StockMovementInsertParameter;
import com.scsvn.whc_2016.retrofit.StockMovementParameter;
import com.scsvn.whc_2016.retrofit.StockMovementReversedParameter;
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

public class ChuyenHangActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnFocusChangeListener {
    @Bind(R.id.listViewFrom)
    ListView listViewFrom;
    @Bind(R.id.listViewTo)
    ListView listViewTo;
    @Bind(R.id.acs_chuyen_hang_reason)
    AppCompatSpinner acsReason;
    @Bind(R.id.acactv_chuyen_hang_from)
    AppCompatAutoCompleteTextView acactvFrom;
    @Bind(R.id.acactv_chuyen_hang_to)
    AppCompatAutoCompleteTextView acactvTo;
    @Bind(R.id.acactv_chuyen_hang_pallet_id)
    AppCompatAutoCompleteTextView acactvPalletId;
    @Bind(R.id.tv_chuyen_hang_original_from)
    TextView tvOriginalFrom;
    @Bind(R.id.tv_chuyen_hang_original_to)
    TextView tvOriginalTo;
    @Bind(R.id.et_chuyen_hang_location_code)
    EditText etLocationCode;

    private final String TAG = ChuyenHangActivity.class.getSimpleName();
    private String userName, location, reason = "", stringPalletID;
    private LocationAdapter adapterFrom, adapterTo;
    private ListLocationInfo locationInfoFrom, locationInfoTo;
    private int object, adapterFromSize, adapterToSize, locationCode = -1, funcion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_hang);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        initUI();
    }

    private void initUI() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.reason));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acsReason.setAdapter(adapter);
        acactvFrom.setOnEditorActionListener(this);
        acactvTo.setOnEditorActionListener(this);
        acactvFrom.setOnFocusChangeListener(this);
        acactvTo.setOnFocusChangeListener(this);

        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        adapterFrom = new LocationAdapter(this, new ArrayList<LocationInfo>());
        listViewFrom.setAdapter(adapterFrom);
        adapterTo = new LocationAdapter(this, new ArrayList<LocationInfo>());
        listViewTo.setAdapter(adapterTo);
    }

    public void getLocation() {
        MyRetrofit.initRequest(this).getLocation(new ListLocationParameter(location)).enqueue(new Callback<List<ListLocationInfo>>() {
            @Override
            public void onResponse(Response<List<ListLocationInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    if (response.body().size() > 0) {
                        if (object == 1) {
                            locationInfoFrom = response.body().get(0);
                            tvOriginalFrom.setText(locationInfoFrom.getLocationNumber());
                        } else if (object == 2) {
                            locationInfoTo = response.body().get(0);
                            tvOriginalTo.setText(locationInfoTo.getLocationNumber());
                            locationCode = locationInfoTo.getLocationCode();
                            if (funcion == 1) {
                                int amountFrom = 0;
                                StringBuilder builderFrom = new StringBuilder("(");
                                for (int i = 0; i < adapterFromSize; i++) {
                                    LocationInfo info = adapterFrom.getItem(i);
                                    if (info.isChecked()) {
                                        builderFrom.append(info.getPalletID());
                                        builderFrom.append(",");
                                        amountFrom++;
                                    }
                                }
                                builderFrom.deleteCharAt(builderFrom.length() - 1);
                                builderFrom.append(")");
                                stringPalletID = builderFrom.toString();
                                reason = "Reversed";
                                if (amountFrom == 0) {
                                    Snackbar.make(listViewFrom, "Không có Pallet nào được chọn", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    executeStockMovementReversed(listViewFrom);

                                }
                            }
                        }
                    } else {
                        if (object == 1) {
                            tvOriginalFrom.setText("");
                        } else if (object == 2) {
                            tvOriginalTo.setText("");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void getPalletID() {
        MyRetrofit.initRequest(this).getPalletID().enqueue(new Callback<List<ListPalletIDInfo>>() {
            @Override
            public void onResponse(Response<List<ListPalletIDInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    ArrayList<String> palletArray = new ArrayList<String>();
                /*    for (ListLocationInfo info : response.body())
                        employeeIDArray.add(Integer.toString(info.getEmployeeID()));*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChuyenHangActivity.this, android.R.layout.simple_list_item_1, palletArray);
                    acactvPalletId.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    private void getStockMovement(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
        }
        StockMovementParameter parameter = new StockMovementParameter(location, userName);
        MyRetrofit.initRequest(this).getStockMovement(parameter).enqueue(new Callback<List<LocationInfo>>() {
            @Override
            public void onResponse(Response<List<LocationInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    if (object == 1) {
                        adapterFrom.clear();
                        adapterFrom.addAll(response.body());
                        adapterFromSize = response.body().size();
                        if (funcion == 2) {
                            object = 2;
                            location = acactvTo.getText().toString();
                            getStockMovement(listViewFrom);
                        }
                    } else if (object == 2) {
                        adapterTo.clear();
                        adapterTo.addAll(response.body());
                        adapterToSize = response.body().size();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(ChuyenHangActivity.this, t, TAG, view);
                dialog.dismiss();

            }
        });
    }

    private void executeStockMovementInsert(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang chuyển...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
        }
        StockMovementInsertParameter parameter = new StockMovementInsertParameter(
                locationInfoTo.getLocationID(),
                locationInfoTo.getLocationNumber(),
                reason,
                locationInfoFrom.getLocationID(),
                userName,
                stringPalletID
        );
        MyRetrofit.initRequest(this).executeStockMovementInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    funcion = 2;
                    object = 1;
                    location = acactvFrom.getText().toString();
                    getStockMovement(listViewFrom);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(ChuyenHangActivity.this, t, TAG, view);
                dialog.dismiss();

            }
        });
    }

    private void executeStockMovementReversed(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang đảo...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
        }
        StockMovementReversedParameter parameter = new StockMovementReversedParameter(
                locationInfoTo.getLocationID(),
                locationInfoTo.getLocationNumber(),
                0,
                reason,
                locationInfoFrom.getLocationID(),
                locationInfoFrom.getLocationNumber(),
                userName
        );
        MyRetrofit.initRequest(this).executeStockMovementReversed(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    funcion = 2;
                    object = 1;
                    location = acactvFrom.getText().toString();
                    getStockMovement(listViewFrom);

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(ChuyenHangActivity.this, t, TAG, view);
                dialog.dismiss();

            }
        });
    }

    @OnClick(R.id.bt_chuyen_hang_history)
    public void history(View view) {
        funcion = 0;
        startActivity(new Intent(this, LichSuChuyenHangActivity.class));
    }

    @OnClick(R.id.bt_chuyen_hang_move)
    public void move(View view) {
        funcion = 0;
        String code = etLocationCode.getText().toString();
        if (tvOriginalFrom.getText().toString().equals("")) {
            Snackbar.make(view, "Bạn chưa chọn vị trí ban đầu", Snackbar.LENGTH_SHORT).show();
            acactvFrom.requestFocus();
            return;
        }
        if (tvOriginalTo.getText().toString().equals("")) {
            Snackbar.make(view, "Bạn chưa chọn vị trí đích", Snackbar.LENGTH_SHORT).show();
            acactvTo.requestFocus();
            return;
        }
        if (code.length() == 0) {
            Snackbar.make(view, "Bạn phải nhập Location code", Snackbar.LENGTH_SHORT).show();
            etLocationCode.requestFocus();
            return;
        }
        if (Integer.parseInt(code) != locationCode) {
            Snackbar.make(view, "Location code không chính xác", Snackbar.LENGTH_SHORT).show();
            etLocationCode.requestFocus();
            return;
        }
        int amountFrom = 0;
        StringBuilder builderFrom = new StringBuilder("(");
        for (int i = 0; i < adapterFromSize; i++) {
            LocationInfo info = adapterFrom.getItem(i);
            if (info.isChecked()) {
                builderFrom.append(info.getPalletID());
                builderFrom.append(",");
                amountFrom++;
            }
        }
        builderFrom.deleteCharAt(builderFrom.length() - 1);
        builderFrom.append(")");
        stringPalletID = builderFrom.toString();
        reason = ((TextView) acsReason.getSelectedView()).getText().toString();

        if (amountFrom == 0) {
            Snackbar.make(view, "Không có Pallet nào được chọn", Snackbar.LENGTH_SHORT).show();
        } else
            executeStockMovementInsert(listViewFrom);
        etLocationCode.setText("");
    }

    @OnClick(R.id.bt_chuyen_hang_reverse)
    public void reverse(View view) {
        funcion = 0;
        String code = etLocationCode.getText().toString();
        if (tvOriginalFrom.getText().toString().equals("")) {
            Snackbar.make(view, "Bạn chưa chọn vị trí ban đầu", Snackbar.LENGTH_SHORT).show();
            acactvFrom.requestFocus();
            return;
        }
        if (tvOriginalTo.getText().toString().equals("")) {
            Snackbar.make(view, "Bạn chưa chọn vị trí đích", Snackbar.LENGTH_SHORT).show();
            acactvTo.requestFocus();
            return;
        }
        if (code.length() == 0) {
            Snackbar.make(view, "Bạn phải nhập Location code", Snackbar.LENGTH_SHORT).show();
            etLocationCode.requestFocus();
            return;
        }
        if (Integer.parseInt(code) != locationCode) {
            Snackbar.make(view, "Location code không chính xác", Snackbar.LENGTH_SHORT).show();
            etLocationCode.requestFocus();
            return;
        }
        executeStockMovementReversed(listViewFrom);
        etLocationCode.setText("");
    }

    @OnClick(R.id.bt_chuyen_hang_one_to_two)
    public void oneToTwo(View view) {
        funcion = 1;
        if (tvOriginalFrom.getText().toString().equals("")) {
            Snackbar.make(view, "Bạn chưa chọn vị trí ban đầu", Snackbar.LENGTH_SHORT).show();
            acactvFrom.requestFocus();
            return;
        }
        String location = acactvFrom.getText().toString();
        if (location.length() == 6) {
            if (location.substring(5).equals("1")) {
                acactvTo.requestFocus();
                acactvTo.setText(String.format("%s2", location.substring(0, 5)));
                getStockMovement();
            } else if (location.substring(5).equals("2")) {
                acactvTo.requestFocus();
                acactvTo.setText(String.format("%s1", location.substring(0, 5)));
                getStockMovement();
            } else {
                Snackbar.make(view, "Chỉ đảo vị trí 1 <-> 2", Snackbar.LENGTH_LONG).show();
                return;
            }
        }


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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            funcion = 0;
            getStockMovement();
            return true;
        }
        return false;
    }

    private void getStockMovement() {
        if (object == 1) {
            location = acactvFrom.getText().toString();
        } else if (object == 2) {
            location = acactvTo.getText().toString();
        }
        getLocation();
        getStockMovement(listViewFrom);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v == acactvFrom)
                object = 1;
            else if (v == acactvTo)
                object = 2;
            Log.e(TAG, "onFocusChange: " + object);
        }

    }
}
