package com.scsvn.whc_2016.main.technical.schedulejobplan;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class ScheduleJobAdapter extends ArrayAdapter<ScheduleJobPlanInfo> {
    private static final String TAG = ScheduleJobAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    public ScheduleJobAdapter(Context context, ArrayList<ScheduleJobPlanInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_schedule_job, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        ScheduleJobPlanInfo info = getItem(position);
        if (info.getEquipmentID().length() + info.getEquipmentName().length() > 40) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.tvEquipmentID);
            holder.tvEquipmentName.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.tvEquipmentID);
            holder.tvEquipmentName.setLayoutParams(params);
        }
        holder.tvEquipmentID.setText(info.getEquipmentID());
        holder.tvEquipmentName.setText(info.getEquipmentName());
        holder.tvFrequence.setText(info.getFrequence());
        holder.tvHours.setText(String.format("Hour: %s", NumberFormat.getInstance().format(info.getPlanHour())));
        holder.tvDate.setText(info.getDueDate());

        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.tvEquipmentID)
        TextView tvEquipmentID;
        @Bind(R.id.tvEquipmentName)
        TextView tvEquipmentName;
        @Bind(R.id.tvFrequence)
        TextView tvFrequence;
        @Bind(R.id.tvHours)
        TextView tvHours;
        @Bind(R.id.tvDate)
        TextView tvDate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
