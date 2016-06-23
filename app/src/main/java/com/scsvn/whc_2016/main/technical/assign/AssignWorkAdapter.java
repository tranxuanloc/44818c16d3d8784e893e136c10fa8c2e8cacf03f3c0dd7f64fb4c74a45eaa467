package com.scsvn.whc_2016.main.technical.assign;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.viewImage.ViewImageActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.CommentParameter;
import com.scsvn.whc_2016.retrofit.InsertAssignWorkParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by buu on 28/01/2016.
 */
public class AssignWorkAdapter extends ArrayAdapter<AssignWorkInfo> implements View.OnClickListener, Filterable {
    private static final String TAG = AssignWorkAdapter.class.getSimpleName();
    private final Picasso picasso;
    private LayoutInflater inflater;
    private String userName, positionGroup;
    private ArrayList<AssignWorkInfo> dataOrigin;
    private ArrayList<AssignWorkInfo> dataRelease;
    private Runnable r;

    public AssignWorkAdapter(Context context, ArrayList<AssignWorkInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userName = LoginPref.getInfoUser(context, LoginPref.USERNAME);
        positionGroup = LoginPref.getInfoUser(context, LoginPref.POSITION_GROUP);
        picasso = Utilities.getPicasso(context);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public AssignWorkInfo getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Handler handler = new Handler();
        final AssignWorkInfo info = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_assign_work, parent, false);
            holder = new ViewHolder(convertView);
            holder.ivEditor.setOnClickListener(this);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        String createBy = info.getCreatedBy().split(" - ")[0];
        holder.pbProgress.setProgress(info.TaskProgress);
        holder.tvProgress.setText(String.format(Locale.US, "%s%%", info.TaskProgress));
        holder.ratingBar.setTag(info);
        holder.ratingBar.setRating(info.Evaluation);
        holder.ratingBar.setIsIndicator(!createBy.equals(userName));
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                if (fromUser) {
                    if (r != null)
                        handler.removeCallbacks(r);
                    r = new Runnable() {
                        @Override
                        public void run() {
                            InsertAssignWorkParameter parameter = new InsertAssignWorkParameter(5, rating, info.getQHSEID());
                            updateRating(holder.ratingBar, parameter);
                        }
                    };
                    handler.postDelayed(r, 2000);
                }
            }
        });
        holder.tvSubject.setText(info.getSubject());
        holder.tvComment.setText(info.getComment());
        holder.tvCreateTime.setText(String.format("%s bởi %s", info.getCreatedTime(), info.getCreatedBy()));
        holder.tvArea.setText(String.format("Khu: %s", info.getLocation()));
        holder.tvCategory.setText(String.format("Loại: %s", info.getCategory()));
        holder.tvCommentQty.setText(String.format(Locale.US, "%d comment", info.getCommentQty()));
        holder.tvLastCommentBy.setText(info.getLatestCommentBy());
        holder.tvLastComment.setText(info.getLatestComment());
        if (info.getLatestCommentBy().length() > 0) {
            holder.tvLastCommentBy.setVisibility(View.VISIBLE);
            holder.tvLastComment.setVisibility(View.VISIBLE);
        } else {
            holder.tvLastCommentBy.setVisibility(View.GONE);
            holder.tvLastComment.setVisibility(View.GONE);
        }
        holder.tvAssignTo.setText(new StringBuilder().append("Thực hiện: ").append(info.getAssignedTo()).append("\nHạn: ").append(Utilities.formatDate_ddMMyy(info.getDeadline())));
        holder.cbConfirm.setChecked(info.isConfirmed());
        holder.cbConfirm.setEnabled(!info.isConfirmed());
        holder.cbConfirm.setTag(info);
        holder.tvOrderNumber.setText(String.format("OrderNo: %s", info.OrderNumber));
        holder.cbConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbConfirm.isChecked())
                    alertConfirm(v);
            }
        });
        holder.reject.setChecked(info.AssigmentReject);
        holder.reject.setTag(info);
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertReject(v);
            }
        });

        if (!userName.equalsIgnoreCase(createBy) && !positionGroup.equalsIgnoreCase(Const.MANAGER) && !positionGroup.equalsIgnoreCase(Const.SUPERVISOR)) {
            holder.ivEditor.setVisibility(View.GONE);
        } else {
            holder.ivEditor.setVisibility(View.VISIBLE);
            holder.ivEditor.setTag(info);
        }

        RelativeLayout.LayoutParams params = getLayoutParamsImage();
        holder.ivImage.setLayoutParams(params);
        RelativeLayout.LayoutParams paramsCommentQty = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (info.getPhotoAttachment().equalsIgnoreCase("")) {
            holder.ivImage.setVisibility(View.GONE);
            paramsCommentQty.addRule(RelativeLayout.BELOW, R.id.tv_qhse_assign_to);
        } else {
            String path = Utilities.generateUrlImage(getContext(), info.getPhotoAttachment());
            picasso.load(path).into(holder.ivImage);
            holder.ivImage.setVisibility(View.VISIBLE);
            paramsCommentQty.addRule(RelativeLayout.BELOW, R.id.iv_qhse_image);
        }
        holder.tvCommentQty.setLayoutParams(paramsCommentQty);
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewImageActivity.class);
                intent.putExtra("src", info.getPhotoAttachment());
                getContext().startActivity(intent);
            }
        });
        holder.tvCommentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AssignWorkCommentActivity.class);
                intent.putExtra(AssignWorkCommentActivity.QHSE_ID, info.getQHSEID());
                intent.putExtra(AssignWorkCommentActivity.QHSE_SUBJECT, info.getSubject());
                intent.putExtra(AssignWorkCommentActivity.CONFIRMED, info.isConfirmed());
                getContext().startActivity(intent);

            }
        });
        holder.tvCommentQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvCommentClick.performClick();
            }
        });
        holder.tvLastComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvCommentClick.performClick();
            }
        });
        holder.tvLastCommentBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvCommentClick.performClick();
            }
        });

        return convertView;
    }

    @NonNull
    private RelativeLayout.LayoutParams getLayoutParamsImage() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width / 2);
        params.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics));
        return params;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<AssignWorkInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        AssignWorkInfo info = dataOrigin.get(i);
                        String type = info.getCategory();
                        if (type.equalsIgnoreCase(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = dataOrigin.size();
                    results.values = dataOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataRelease = (ArrayList<AssignWorkInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onClick(final View v) {
        final AssignWorkInfo info = (AssignWorkInfo) v.getTag();
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.qhse_options, popupMenu.getMenu());
        MenuItem itemEdit = popupMenu.getMenu().findItem(R.id.action_qhse_edit);
        itemEdit.setVisible(!info.isConfirmed());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_qhse_edit) {
                    Intent intent = new Intent(getContext(), UpdateAssignWorkActivity.class);
                    intent.putExtra(UpdateAssignWorkActivity.QHSEID, info.getQHSEID());
                    intent.putExtra(UpdateAssignWorkActivity.QHSENUMBER, info.getQHSENumber());
                    intent.putExtra(UpdateAssignWorkActivity.CreatedTime, info.getCreatedTime());
                    intent.putExtra(UpdateAssignWorkActivity.Subject, info.getSubject());
                    intent.putExtra(UpdateAssignWorkActivity.Category, info.getCategory());
                    intent.putExtra(UpdateAssignWorkActivity.Comment, info.getComment());
                    intent.putExtra(UpdateAssignWorkActivity.Location, info.getLocation());
                    intent.putExtra(UpdateAssignWorkActivity.PhotoAttachment, info.getPhotoAttachment());
                    intent.putExtra(UpdateAssignWorkActivity.DEAD_LINE, info.getDeadline());
                    intent.putExtra(UpdateAssignWorkActivity.ASSIGN_TO, info.getAssignedTo());
                    intent.putExtra(UpdateAssignWorkActivity.PROGRESS, info.TaskProgress);
                    intent.putExtra(UpdateAssignWorkActivity.ORDER_NUMBER, info.OrderNumber);
                    getContext().startActivity(intent);
                } else if (item.getItemId() == R.id.action_qhse_delete) {
                    alerDelete(v);
                }
                return true;
            }
        });
        popupMenu.show();

    }

    private void alerDelete(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn xóa bài viết này?").setPositiveButton("Không", null).setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InsertAssignWorkParameter parameter = new InsertAssignWorkParameter(userName, ((AssignWorkInfo) v.getTag()).getQHSEID(), 3);
                deleteQHSE(v, parameter);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void alertConfirm(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn đóng bài viết này?").setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((CompoundButton) v).setChecked(false);
            }
        }).setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InsertAssignWorkParameter parameter = new InsertAssignWorkParameter(2, ((AssignWorkInfo) v.getTag()).getQHSEID());
                updateAssign(v, parameter);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void alertReject(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn cập nhật trạng thái từ chối này?").setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((CompoundButton) v).setChecked(((AssignWorkInfo) v.getTag()).AssigmentReject);
            }
        }).setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CompoundButton cb = (CompoundButton) v;
                InsertAssignWorkParameter parameter = new InsertAssignWorkParameter(6, cb.isChecked(), ((AssignWorkInfo) v.getTag()).getQHSEID());
                rejectAssign(v, parameter, cb.isChecked());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deleteQHSE(final View view, InsertAssignWorkParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(getContext(), "Đang xóa...");
        dialog.show();

        if (!Utilities.isConnected(getContext())) {
            dialog.dismiss();
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).insertAssignWork(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    remove((AssignWorkInfo) view.getTag());
                    Log.e(TAG, "onResponse: xóa success");
                    Snackbar.make(view, "Đã xóa", Snackbar.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "onResponse: xóa failed");
                    Snackbar.make(view, getContext().getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
                dialog.dismiss();
            }
        });
    }

    private void rejectAssign(final View view, InsertAssignWorkParameter parameter, final boolean checked) {
        final ProgressDialog dialog = Utilities.getProgressDialog(getContext(), "Đang cập nhật...");
        dialog.show();

        if (!Utilities.isConnected(getContext())) {
            dialog.dismiss();
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).insertAssignWork(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    AssignWorkInfo tag = (AssignWorkInfo) view.getTag();
                    tag.AssigmentReject = checked;
                    notifyDataSetChanged();
                    String state = checked ? "Checked Reject" : "Unchecked reject";
                    getComment(view, state, tag.getQHSEID());
                } else {
                    Snackbar.make(view, getContext().getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
                dialog.dismiss();
            }
        });
    }

    public void getComment(final View view, final String state, int id) {
        CommentParameter parameter = new CommentParameter(String.format("%s - %s", userName, state), userName, 0, "", 1, id);
        MyRetrofit.initRequest(getContext()).getAssignWorkComment(parameter).enqueue(new Callback<List<CommentInfo>>() {
            @Override
            public void onResponse(Response<List<CommentInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
            }
        });
    }

    private void updateRating(final View view, final InsertAssignWorkParameter parameter) {

        if (!Utilities.isConnected(getContext())) {
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).insertAssignWork(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    ((AssignWorkInfo) view.getTag()).Evaluation = parameter.Evaluation;
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
            }
        });
    }

    private void updateAssign(final View view, InsertAssignWorkParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(getContext(), "Đang xác nhận...");
        dialog.show();

        if (!Utilities.isConnected(getContext())) {
            dialog.dismiss();
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).insertAssignWork(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    ((AssignWorkInfo) view.getTag()).setConfirmed(true);
                    notifyDataSetChanged();
                } else {
                    Snackbar.make(view, getContext().getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
                dialog.dismiss();
            }
        });
    }


    static class ViewHolder {
        @Bind(R.id.tv_qhse_subject)
        TextView tvSubject;
        @Bind(R.id.tv_qhse_comment)
        TextView tvComment;
        @Bind(R.id.tv_qhse_create_time)
        TextView tvCreateTime;
        @Bind(R.id.iv_qhse_editor)
        ImageView ivEditor;
        @Bind(R.id.iv_qhse_image)
        ImageView ivImage;
        @Bind(R.id.tv_qhse_comment_click)
        TextView tvCommentClick;
        @Bind(R.id.tv_qhse_area)
        TextView tvArea;
        @Bind(R.id.tv_qhse_category)
        TextView tvCategory;
        @Bind(R.id.tv_qhse_comment_qty)
        TextView tvCommentQty;
        @Bind(R.id.item_tv_qhse_LastCommentBy)
        TextView tvLastCommentBy;
        @Bind(R.id.item_tv_qhse_LastComment)
        TextView tvLastComment;
        @Bind(R.id.tv_qhse_assign_to)
        TextView tvAssignTo;
        @Bind(R.id.item_cb_confirm)
        CheckBox cbConfirm;
        @Bind(R.id.ratingBar)
        AppCompatRatingBar ratingBar;
        @Bind(R.id.cb_qhse_reject)
        CheckBox reject;
        @Bind(R.id.tv_qhse_order_number)
        TextView tvOrderNumber;
        @Bind(R.id.tvProgress)
        TextView tvProgress;
        @Bind(R.id.progressBar)
        ProgressBar pbProgress;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}