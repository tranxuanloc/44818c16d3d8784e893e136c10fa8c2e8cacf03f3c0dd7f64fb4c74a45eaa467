package com.scsvn.whc_2016.main.kiemcontainer;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 3/9/2016.
 */
public class HistoryCheckingAdapter extends ArrayAdapter<HistoryCheckingInfo> {
    private LayoutInflater inflater;

    public HistoryCheckingAdapter(Context context, List<HistoryCheckingInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_history_container_checking, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        HistoryCheckingInfo info = getItem(position);
        holder.tvCreateBy.setText(info.getCreatedBy());
        holder.tvCreateTime.setText(info.getCreatedTime());
        holder.tvRemark.setText(info.getRemark());
        holder.tvTC.setText(info.getT_Show());
        holder.tvDockNumber.setText(info.getDockNumber());
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_history_create_by)
        TextView tvCreateBy;
        @Bind(R.id.tv_history_create_time)
        TextView tvCreateTime;
        @Bind(R.id.tv_history_remark)
        TextView tvRemark;
        @Bind(R.id.tv_history_tc)
        TextView tvTC;
        @Bind(R.id.tv_history_dock_number)
        TextView tvDockNumber;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
