package com.scsvn.whc_2016.main.booking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.retrofit.CustomerBookingByTimeSlotParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BookingActivity extends BaseActivity {

    private byte warehouseId = 0;
    private Calendar calendar;
    private String bookingDate;
    private Button btChooseDate;
    private Button btToday;
    private ListView lvBooking;
    private BookingAdapter adapter;
    private GridLayout glSumBooking;
    private TextView tvWeight;
    private TextView tvWeightR;
    private TextView tvWeightD;
    private TextView tvPalletR;
    private TextView tvPalletD;
    private TextView tvPallet;
    private List<Booking> bookings;
    private float weightMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Utilities.showBackIcon(getSupportActionBar());

        initial();
    }

    private void initial() {
        mapView();
        snackBarView = btChooseDate;
        btToday.performClick();
        adapter = new BookingAdapter(this, new ArrayList<Booking>());
        lvBooking.setAdapter(adapter);

    }

    private void mapView() {
        btChooseDate = (Button) findViewById(R.id.bt_choose_date);
        btToday = (Button) findViewById(R.id.bt_today);
        lvBooking = (ListView) findViewById(R.id.lv_booking);
        glSumBooking = (GridLayout) findViewById(R.id.booking_footer);
        tvWeight = (TextView) glSumBooking.findViewById(R.id.tv_weight);
        tvWeightR = (TextView) glSumBooking.findViewById(R.id.tv_weight_r);
        tvWeightD = (TextView) glSumBooking.findViewById(R.id.tv_weight_d);
        tvPallet = (TextView) glSumBooking.findViewById(R.id.tv_pallet);
        tvPalletR = (TextView) glSumBooking.findViewById(R.id.tv_pallet_r);
        tvPalletD = (TextView) glSumBooking.findViewById(R.id.tv_pallet_d);
    }

    public void getBooking() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).getBookings(new CustomerBookingByTimeSlotParameter(bookingDate.split("T")[0], warehouseId)).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Response<List<Booking>> response, Retrofit retrofit) {
                List<Booking> body = response.body();
                if (response.isSuccess() && body != null) {
                    bookings = new ArrayList<>(body);
                    adapter.clear();
                    adapter.addAll(body);

                    float weightR, weightD, weight;
                    int pallet, palletR, palletD;
                    weight = weightR = weightD = 0;
                    pallet = palletR = palletD = 0;
                    weightMax = 0;
                    for (Booking item : body) {
                        float weightAll = item.getWeightAll();
                        if (weightAll > weightMax)
                            weightMax = weightAll;
                        weight += weightAll;
                        weightR += item.getWeightRO();
                        weightD += item.getWeightDO();
                        pallet += item.getPalletAll();
                        palletR += item.getPalletRO();
                        palletD += item.getPalletDO();
                    }

                    tvWeight.setText(NumberFormat.getInstance().format(weight));
                    tvWeightR.setText(NumberFormat.getInstance().format(weightR));
                    tvWeightD.setText(NumberFormat.getInstance().format(weightD));
                    tvPallet.setText(String.format(Locale.getDefault(), "%d", pallet));
                    tvPalletR.setText(String.format(Locale.getDefault(), "%d", palletR));
                    tvPalletD.setText(String.format(Locale.getDefault(), "%d", palletD));
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(BookingActivity.this, t, TAG, snackBarView);
            }
        });
    }

    public void warehouseIdClick(final View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.warehouse_id);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_warehouse_all:
                        warehouseId = 0;
                        ((Button) view).setText(getString(R.string.all));
                        break;
                    case R.id.action_warehouse_123:
                        warehouseId = 1;
                        ((Button) view).setText(getString(R.string.warehouse_123));
                        break;
                    case R.id.action_warehouse_45:
                        warehouseId = 2;
                        ((Button) view).setText(getString(R.string.warehouse_45));
                        break;
                    default:
                        warehouseId = 0;
                }
                getBooking();
                return true;
            }
        });
        popupMenu.show();
        Menu menu = popupMenu.getMenu();
        switch (warehouseId) {
            case 0:
                menu.getItem(0).setChecked(true);
                break;
            case 1:
                menu.getItem(1).setChecked(true);
                break;
            case 2:
                menu.getItem(2).setChecked(true);
                break;
        }
    }

    public void weekChartClick(View view) {
        BarGraph barGraph = new BarGraph(this, bookings, weightMax + 10);
        barGraph.show();
    }

    public void todayClick(View view) {
        calendar = Calendar.getInstance();
        bookingDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(bookingDate));
        getBooking();
    }

    public void chooseDayClick(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                bookingDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(bookingDate));
                getBooking();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void previousDateClick(View view) {
        calendar.add(Calendar.DATE, -1);
        bookingDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(bookingDate));
        getBooking();
    }

    public void nextDateClick(View view) {
        calendar.add(Calendar.DATE, 1);
        bookingDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(bookingDate));
        getBooking();
    }
}
