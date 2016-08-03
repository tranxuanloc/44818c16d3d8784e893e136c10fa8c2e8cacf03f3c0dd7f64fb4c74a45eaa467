package com.scsvn.whc_2016.main.detailphieu.worker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

public class WorkerActivity extends BaseActivity implements View.OnClickListener, WorkerFragment.OnDataListener {

    private String orderNumber;
    private WorkerFragment workerFragment;
    private WorkerAdditionalFragment workerAdditionalFragment;
    private Button btApprove, btSave, btDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Utilities.showBackIcon(actionBar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.workerViewPager);
        assert viewPager != null;
        WorkerFragmentAdapter adapter = new WorkerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        btSave = (Button) findViewById(R.id.bt_worker_save);
        btDone = (Button) findViewById(R.id.bt_worker_done);
        btApprove = (Button) findViewById(R.id.bt_worker_approve);
        btSave.setOnClickListener(this);
        btDone.setOnClickListener(this);
        btApprove.setOnClickListener(this);
        orderNumber = getIntent().getStringExtra("order_number");
        workerFragment = WorkerFragment.newInstance(orderNumber);
        workerAdditionalFragment = WorkerAdditionalFragment.newInstance();
    }

    public WorkerFragment getWorkerFragment() {
        return workerFragment;
    }

    public WorkerAdditionalFragment getWorkerAdditionalFragment() {
        return workerAdditionalFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
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
    public void onClick(View v) {
        if (v == btSave)
            getWorkerFragment().save(v);
        else if (v == btDone)
            getWorkerFragment().done(v);
        else if (v == btApprove)
            getWorkerFragment().approve(v);
    }

    @Override
    public void onUpdateButton(int orderStatus) {
        if (orderStatus == 2) {
            btSave.setEnabled(false);
            btDone.setEnabled(false);
        }
    }


    class WorkerFragmentAdapter extends FragmentPagerAdapter {

        public WorkerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return position == 0 ? workerFragment : workerAdditionalFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
