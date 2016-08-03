package com.scsvn.whc_2016.main.detailphieu.worker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.scsvn.whc_2016.R;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 7/29/2016.
 */
public class WorkerAdditionalFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {
    public static final String ORDER_NUMBER = "ORDER_NUMBER";
    @Bind(R.id.et_worker_boc_xep_percent_3)
    EditText vPercentBocXep3;
    @Bind(R.id.et_worker_boc_xep_percent_4)
    EditText vPercentBocXep4;
    @Bind(R.id.et_worker_boc_xep_percent_5)
    EditText vPercentBocXep5;
    @Bind(R.id.actv_worker_boc_xep_3)
    AppCompatAutoCompleteTextView vBocXepID3;
    @Bind(R.id.actv_worker_boc_xep_4)
    AppCompatAutoCompleteTextView vBocXepID4;
    @Bind(R.id.actv_worker_boc_xep_5)
    AppCompatAutoCompleteTextView vBocXepID5;
    @Bind(R.id.actv_worker_walkie_3)
    AppCompatAutoCompleteTextView vWalkieID3;
    @Bind(R.id.actv_worker_walkie_4)
    AppCompatAutoCompleteTextView vWalkieID4;
    @Bind(R.id.et_worker_walkie_percent_3)
    EditText vPercentWalkie3;
    @Bind(R.id.et_worker_walkie_percent_4)
    EditText vPercentWalkie4;
    @Bind(R.id.actv_worker_tai_xe_2)
    AppCompatAutoCompleteTextView actvTaiXeID2;
    @Bind(R.id.et_worker_tai_xe_percent_2)
    EditText etPercentTaiXe2;

    private String orderNumber;
    private WorkerInfo info;

    public static WorkerAdditionalFragment newInstance() {
        WorkerAdditionalFragment workerFragment = new WorkerAdditionalFragment();
        Bundle bundle = new Bundle();
        workerFragment.setArguments(bundle);
        return workerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
//            orderNumber = arguments.getString(ORDER_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_additional, container, false);
        ButterKnife.bind(this, view);
        vBocXepID3.setOnFocusChangeListener(this);
        vBocXepID4.setOnFocusChangeListener(this);
        vBocXepID5.setOnFocusChangeListener(this);
        vWalkieID3.setOnFocusChangeListener(this);
        vWalkieID4.setOnFocusChangeListener(this);
        actvTaiXeID2.setOnFocusChangeListener(this);
        vBocXepID3.setOnClickListener(this);
        vBocXepID4.setOnClickListener(this);
        vBocXepID5.setOnClickListener(this);
        vWalkieID3.setOnClickListener(this);
        vWalkieID4.setOnClickListener(this);
        actvTaiXeID2.setOnClickListener(this);
        return view;
    }

    public void setWorkerInfo(WorkerInfo info) {
        this.info = info;
        vBocXepID3.setText(String.format(Locale.US, "%d", info.getGeneralHandID3()));
        vBocXepID4.setText(String.format(Locale.US, "%d", info.getGeneralHandID4()));
        vBocXepID5.setText(String.format(Locale.US, "%d", info.getGeneralHandID5()));
        vPercentBocXep3.setText(String.format(Locale.US, "%d", info.getPercentGH3()));
        vPercentBocXep4.setText(String.format(Locale.US, "%d", info.getPercentGH4()));
        vPercentBocXep5.setText(String.format(Locale.US, "%d", info.getPercentGH5()));
        vWalkieID3.setText(String.format(Locale.US, "%d", info.getWalkieID3()));
        vWalkieID4.setText(String.format(Locale.US, "%d", info.getWalkieID4()));
        vPercentWalkie3.setText(String.format(Locale.US, "%d", info.getPercentWalkieID3() == 0 ? 100 : info.getPercentWalkieID3()));
        vPercentWalkie4.setText(String.format(Locale.US, "%d", info.getPercentWalkieID4() == 0 ? 100 : info.getPercentWalkieID4()));
        actvTaiXeID2.setText(String.format(Locale.US, "%d", info.getForkliftDriverID2()));
        etPercentTaiXe2.setText(String.format(Locale.US, "%d", info.getPercentFD2() == 0 ? 100 : info.getPercentFD2()));
    }

    public void setEmployeePresentAdapter(EmployeePresentAdapter adapter) {
        vBocXepID3.setAdapter(adapter);
        vBocXepID4.setAdapter(adapter);
        vBocXepID5.setAdapter(adapter);
        vWalkieID3.setAdapter(adapter);
        vWalkieID4.setAdapter(adapter);
        actvTaiXeID2.setAdapter(adapter);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            ((AppCompatAutoCompleteTextView) v).showDropDown();
        }
    }

    public EditText getvPercentBocXep3() {
        return vPercentBocXep3;
    }

    public EditText getvPercentBocXep4() {
        return vPercentBocXep4;
    }

    public EditText getvPercentBocXep5() {
        return vPercentBocXep5;
    }

    public AppCompatAutoCompleteTextView getvBocXepID3() {
        return vBocXepID3;
    }

    public AppCompatAutoCompleteTextView getvBocXepID4() {
        return vBocXepID4;
    }

    public AppCompatAutoCompleteTextView getvBocXepID5() {
        return vBocXepID5;
    }

    public AppCompatAutoCompleteTextView getvWalkieID3() {
        return vWalkieID3;
    }

    public AppCompatAutoCompleteTextView getvWalkieID4() {
        return vWalkieID4;
    }

    public EditText getvPercentWalkie3() {
        return vPercentWalkie3;
    }

    public EditText getvPercentWalkie4() {
        return vPercentWalkie4;
    }

    public AppCompatAutoCompleteTextView getActvTaiXeID2() {
        return actvTaiXeID2;
    }

    public EditText getEtPercentTaiXe2() {
        return etPercentTaiXe2;
    }

    @Override
    public void onClick(View v) {
        ((AppCompatAutoCompleteTextView) v).showDropDown();
    }
}
