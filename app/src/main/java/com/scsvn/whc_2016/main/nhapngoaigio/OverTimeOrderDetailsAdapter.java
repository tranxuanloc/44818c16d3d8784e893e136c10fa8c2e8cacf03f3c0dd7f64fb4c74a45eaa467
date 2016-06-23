package com.scsvn.whc_2016.main.nhapngoaigio;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class OverTimeOrderDetailsAdapter extends ArrayAdapter<OverTimeOrderDetailsInfo> {
    private LayoutInflater inflater;

    public OverTimeOrderDetailsAdapter(Context context, List<OverTimeOrderDetailsInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_over_time_oder_details, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        OverTimeOrderDetailsInfo info = getItem(position);
        holder.tvEndTime.setText(info.getEndTime());
        holder.tvOrderNumber.setText(info.getOrderNumber());
        holder.tvStartTime.setText(info.getStartTime());
        holder.tvTotalWeight.setText(NumberFormat.getInstance().format(info.getTotalWeight()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_over_time_detail_total_weight)
        TextView tvTotalWeight;
        @Bind(R.id.item_tv_over_time_detail_start_time)
        TextView tvStartTime;
        @Bind(R.id.item_tv_over_time_detail_order_number)
        TextView tvOrderNumber;
        @Bind(R.id.item_tv_over_time_detail_end_time)
        TextView tvEndTime;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}