package com.scsvn.whc_2016.main.kiemqa.metroqacheckingcarton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MetroQACheckingCartonDelUpdateParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class MetroCartonAdapter extends ArrayAdapter<MetroCartonInfo> {
    private final String TAG = MetroCartonAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    public MetroCartonAdapter(Context context, List<MetroCartonInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_metro_carton, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final MetroCartonInfo info = getItem(position);
        holder.tvIndex.setText(String.format(Locale.US, "%d", info.getCheckingCartonIndex()));
        if (info.isNew()) {
            holder.etWeight.requestFocus();
            holder.tvDamagePercent.setText("");
            holder.etDamageWeight.setText("");
            holder.etWeight.setText("");
            holder.llRoot.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        } else {
            holder.llRoot.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            holder.tvDamagePercent.setText(String.format(Locale.US, "%.2f%%", (info.getDamageWeighPerCarton() / info.getCheckingWeighPerCarton()) * 100));
            holder.etWeight.setText(NumberFormat.getInstance().format(info.getCheckingWeighPerCarton()));
            holder.etDamageWeight.setText(NumberFormat.getInstance().format(info.getDamageWeighPerCarton()));
        }
        if (info.getReceivingCartonCheckingID() != 0) {
            holder.ivEditor.setImageResource(R.drawable.ic_editor_mode_edit_14dip);
            holder.ivEditor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_metro_carton, null);
                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.item_ll_metro_carton);
                    ll.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    ll.setBackgroundResource(R.drawable.outlines);
                    TextView tvIndex = (TextView) view.findViewById(R.id.item_tv_metro_carton_index);
                    final TextView tvPercent = (TextView) view.findViewById(R.id.item_tv_metro_carton_damage_percent);
                    final EditText etWeight = (EditText) view.findViewById(R.id.item_et_metro_carton_weight);
                    final EditText etDamageWeight = (EditText) view.findViewById(R.id.item_et_metro_carton_damage_weight);
                    tvIndex.setText(String.format(Locale.US, "%d", info.getCheckingCartonIndex()));
                    tvPercent.setText(holder.tvDamagePercent.getText());
                    etWeight.setText(NumberFormat.getInstance().format(info.getCheckingWeighPerCarton()));
                    etDamageWeight.setText(NumberFormat.getInstance().format(info.getDamageWeighPerCarton()));

                    final AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(String.format(Locale.US, "Sửa thông tin thùng số %d", info.getCheckingCartonIndex()))
                            .setView(view)
                            .setNegativeButton("Hủy", null)
                            .setPositiveButton("Sửa", null)
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float damageWeight = 0;
                            if (etWeight.getText().length() == 0 || Float.parseFloat(etWeight.getText().toString()) == 0)
                                return;
                            try {
                                damageWeight = Float.parseFloat(etDamageWeight.getText().toString());
                            } catch (NumberFormatException ex) {
                                Log.d(TAG, "cartonAdd() returned: " + ex.getMessage());
                            }
                            float weight = Float.parseFloat(etWeight.getText().toString());
                            tvPercent.setText(String.format(Locale.US, "%.2f%%", (damageWeight / weight) * 100));
                            MetroQACheckingCartonDelUpdateParameter parameter = new MetroQACheckingCartonDelUpdateParameter(
                                    weight, damageWeight, 2, info.getReceivingCartonCheckingID(), LoginPref.getUsername(getContext())
                            );
                            executeMetroQACheckingCartonDelUpdate(view, parameter, position, dialog);
                        }
                    });
                }
            });
        } else {
            holder.ivEditor.setImageDrawable(null);
            holder.ivEditor.setOnClickListener(null);
        }
        holder.ivEditor.setClickable(true);
        TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    if (holder.etWeight.length() > 0 && holder.etDamageWeight.length() > 0) {
                        float weight = Float.parseFloat(holder.etWeight.getText().toString());
                        float damageWeight = Float.parseFloat(holder.etDamageWeight.getText().toString());
                        holder.tvDamagePercent.setText(String.format(Locale.US, "%.2f%%", (damageWeight / weight) * 100));
                        return true;
                    }
                return false;
            }
        };
        holder.etDamageWeight.setOnEditorActionListener(editorListener);
        holder.etWeight.setOnEditorActionListener(editorListener);
        return convertView;
    }

    private void executeMetroQACheckingCartonDelUpdate(final View view, final MetroQACheckingCartonDelUpdateParameter parameter, final int position, final AlertDialog alertDialog) {
        final ProgressDialog dialog = Utilities.getProgressDialog(getContext(), "Đang sửa");
        dialog.show();
        if (!Utilities.isConnected(getContext())) {
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(getContext()).executeMetroQACheckingCartonDelUpdate(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                dialog.dismiss();
                if (response.isSuccess() && response.body() != null) {
                    getItem(position).setCheckingWeighPerCarton(parameter.getCheckingWeighPerCarton());
                    getItem(position).setDamageWeighPerCarton(parameter.getDamageWeighPerCarton());
                    notifyDataSetChanged();
                    alertDialog.dismiss();
                } else
                    Toast.makeText(getContext(), "Có lỗi xảy ra, dữ liệu chưa được sửa", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Có lỗi xảy ra, dữ liệu chưa được sửa", Toast.LENGTH_LONG).show();
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
                dialog.dismiss();
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.item_et_metro_carton_damage_weight)
        EditText etDamageWeight;
        @Bind(R.id.item_tv_metro_carton_damage_percent)
        TextView tvDamagePercent;
        @Bind(R.id.item_tv_metro_carton_index)
        TextView tvIndex;
        @Bind(R.id.item_et_metro_carton_weight)
        EditText etWeight;
        @Bind(R.id.item_iv_metro_carton_edit)
        ImageView ivEditor;
        @Bind(R.id.item_ll_metro_carton)
        LinearLayout llRoot;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}