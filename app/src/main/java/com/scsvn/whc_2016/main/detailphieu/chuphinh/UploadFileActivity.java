package com.scsvn.whc_2016.main.detailphieu.chuphinh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.postiamge.GridImage;
import com.scsvn.whc_2016.main.postiamge.PostImage;
import com.scsvn.whc_2016.main.postiamge.Thumb;
import com.scsvn.whc_2016.main.postiamge.ThumbImageAdapter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadFileActivity extends BaseActivity {
    private final String TAG = UploadFileActivity.class.getSimpleName();
    @Bind(R.id.et_description_file_upload)
    EditText etDesc;
    @Bind(R.id.grid_image)
    GridView gridImage;
    private String orderNumber;
    private ThumbImageAdapter gridImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        snackBarView = gridImage;

        Intent intent = getIntent();
        orderNumber = intent.getStringExtra(ORDER_NUMBER);
        int type = intent.getIntExtra(ChupHinhActivity.TYPE, 0);
        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);

        if (type == ChupHinhActivity.PICK_IMAGE)
            checkPickImage();
        else
            checkCaptureImage();

        ChupHinhActivity.isUpdate = false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CODE_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentPickImage();
            }
        } else if (requestCode == CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentCaptureImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            files = GridImage.updateGridImage(imageCapturedUri.getPath(), gridImageAdapter);
        } else if (requestCode == CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            files = GridImage.updateGridImage(this, data, gridImageAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attachment, menu);
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

    public UploadFileActivity getInstance() {
        return UploadFileActivity.this;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_post) {
            uploadingImage();
        }

        return true;
    }

    private void uploadingImage() {
        ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.upload));
        PostImage postImage = new PostImage(this, dialog, snackBarView, etDesc.getText().toString(), orderNumber);
        postImage.uploadImage(files, files.size() - 1);
    }

    @Override
    public void onBackPressed() {
        ChupHinhActivity.isUpdate = true;
        super.onBackPressed();
    }
}
