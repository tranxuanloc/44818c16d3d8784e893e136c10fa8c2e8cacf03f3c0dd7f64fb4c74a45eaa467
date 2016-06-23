package com.scsvn.whc_2016.main.nhapngoaigio.detail;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class ListOverTimeAdapter extends ArrayAdapter<OverTimeViewInfo> {
    private LayoutInflater inflater;

    public ListOverTimeAdapter(Context context, List<OverTimeViewInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_over_time_entry, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        OverTimeViewInfo info = getItem(position);
        if (info.isEmployeeOTSupervisorConfirm())
            holder.ivConfirmed.setImageResource(R.drawable.ic_toggle_check_box);
        else holder.ivConfirmed.setImageResource(R.drawable.ic_toggle_check_box_outline_blank);

        holder.tvAuthor.setText(info.getAuthorisedBy());
        holder.tvDate.setText(Utilities.formatDate_ddMMyy(info.getEmployeeOTSupervisorDate()));
        holder.tvDayStatus.setText(info.getDayStatus());
        holder.tvEmID.setText(String.format(Locale.US, "%d", info.getEmployeeID()));
        holder.tvEmName.setText(info.getVietnamName());
        holder.tvHours.setText(String.format("%s", info.getHourQuantity()));
        holder.tvRemark.setText(info.getRemarks());
        holder.tvTimeWork.setText(info.getTimeWork());
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_list_over_time_author)
        android.widget.TextView tvAuthor;
        @Bind(R.id.item_iv_list_over_time_confirm)
        ImageView ivConfirmed;
        @Bind(R.id.item_tv_list_over_time_date)
        TextView tvDate;
        @Bind(R.id.item_tv_list_over_time_dayStatus)
        TextView tvDayStatus;
        @Bind(R.id.item_tv_list_over_time_emID)
        TextView tvEmID;
        @Bind(R.id.item_tv_list_over_time_emName)
        TextView tvEmName;
        @Bind(R.id.item_tv_list_over_time_qtyHours)
        TextView tvHours;
        @Bind(R.id.item_tv_list_over_time_remark)
        TextView tvRemark;
        @Bind(R.id.item_tv_list_over_time_timeWork)
        TextView tvTimeWork;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}