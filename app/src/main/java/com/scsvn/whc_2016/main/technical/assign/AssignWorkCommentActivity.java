package com.scsvn.whc_2016.main.technical.assign;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.CommentParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tranxuanloc on 3/22/2016.
 */
public class AssignWorkCommentActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static final String QHSE_ID = "qhse_id";
    public static final String QHSE_SUBJECT = "qhse_subject";
    public static final String CONFIRMED = "CONFIRMED";
    private final String TAG = AssignWorkCommentActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_qhse_subject)
    TextView tvSubject;
    @Bind(R.id.et_qhse_comment)
    EditText etComment;
    @Bind(R.id.iv_qhse_send_comment)
    ImageView ivSendComment;
    private View.OnClickListener tryAgain;
    private String qhseSubject, comment = "", userName;
    private int qhseID, flag, commentID;
    private CommentAdapter adapter;
    private boolean isConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_qhse_comment);
        ButterKnife.bind(this);


        initUI();

    }

    private void initUI() {
        if (getIntent() != null) {
            qhseID = getIntent().getIntExtra(QHSE_ID, 0);
            qhseSubject = getIntent().getStringExtra(QHSE_SUBJECT);
            isConfirmed = getIntent().getBooleanExtra(CONFIRMED, false);
            tvSubject.setText(qhseSubject);
        }
        ivSendComment.setEnabled(!isConfirmed);

        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getComment(listView);
            }
        };
        adapter = new CommentAdapter(this, new ArrayList<CommentInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        getComment(listView);
    }

    public void getComment(final View view) {
        CommentParameter parameter = new CommentParameter(comment, userName, commentID, "", flag, qhseID);
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getAssignWorkComment(parameter).enqueue(new Callback<List<CommentInfo>>() {
            @Override
            public void onResponse(Response<List<CommentInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(AssignWorkCommentActivity.this, t, TAG, view, tryAgain);
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.iv_qhse_send_comment)
    public void sendComment() {
        if (Utilities.isEmpty(etComment))
            return;
        Utilities.hideKeyboard(this);
        comment = etComment.getText().toString();
        flag = 1;
        getComment(listView);
        etComment.setText("");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
        if (!isConfirmed) {
            final CommentInfo info = adapter.getItem(position);
            String commentBy = info.getCommentBy().split(" - ")[0];
            PopupMenu menu = new PopupMenu(this, view);
            int menuRes = commentBy.equalsIgnoreCase(userName) ? R.menu.qhse_item_comment_admin : R.menu.qhse_item_comment_guest;
            menu.getMenuInflater().inflate(menuRes, menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_comment_edit)
                        updateComment(info);
                    else if (item.getItemId() == R.id.action_comment_delete)
                        deleteComment(info);

                    return true;
                }
            });
            menu.show();
            return true;
        }
        return false;
    }

    private void updateComment(final CommentInfo info) {
        final EditText editText = new EditText(AssignWorkCommentActivity.this);
        editText.setText(info.getComment());
        AlertDialog dialog = new AlertDialog.Builder(AssignWorkCommentActivity.this)
                .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utilities.hideKeyboard(AssignWorkCommentActivity.this);
                        flag = 2;
                        commentID = info.getCommentID();
                        comment = editText.getText().toString();
                        getComment(listView);
                    }
                })
                .setNegativeButton("Hủy", null)
                .setView(editText)
                .create();
        dialog.show();
    }

    private void deleteComment(final CommentInfo info) {
        AlertDialog dialog = new AlertDialog.Builder(AssignWorkCommentActivity.this)
                .setMessage("Bạn muốn xóa bình luận này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utilities.hideKeyboard(AssignWorkCommentActivity.this);
                        flag = 3;
                        commentID = info.getCommentID();
                        getComment(listView);
                    }
                }).setNegativeButton("Không", null)
                .create();
        dialog.show();
    }
}
