package com.scsvn.whc_2016.main.viewImage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.login.LoginActivity;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewImageActivity extends BaseActivity {
    @Bind(R.id.iv_view_image_image)
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        String src = getIntent().getStringExtra("src");
        Utilities.getPicasso(this).load(Utilities.generateUrlImage(this, src)).into(ivImage);
    }

    @OnClick(R.id.iv_home)
    public void home() {
        onBackPressed();
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
