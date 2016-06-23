package com.scsvn.whc_2016.main.nangsuat;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 3/17/2016.
 */
public class NangSuatAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<GroupInfo> arrayGroup;
    private HashMap<Integer, List<ChildInfo>> arrayChild;

    public NangSuatAdapter(Context context, ArrayList<GroupInfo> arrayGroup, HashMap<Integer, List<ChildInfo>> arrayChild) {
        this.context = context;
        this.arrayGroup = arrayGroup;
        this.arrayChild = arrayChild;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return arrayGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arrayChild.get(groupPosition).size();
    }

    @Override
    public GroupInfo getGroup(int groupPosition) {
        return arrayGroup.get(groupPosition);
    }

    @Override
    public ChildInfo getChild(int groupPosition, int childPosition) {
        return arrayChild.get(groupPosition).get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_group_nang_suat, parent, false);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (GroupViewHolder) convertView.getTag();
        GroupInfo groupInfo = getGroup(groupPosition);
        holder.tvPayrollDate.setText(groupInfo.getPayrollDate());
        holder.tvTimeWork.setText(groupInfo.getTimeWork());
        holder.tvStatus.setText(String.format("Công: %s", groupInfo.getTimeKeepingStatus()));
        if (groupInfo.isNightShift())
            holder.tvNight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_toggle_check_box, 0);
        else
            holder.tvNight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_toggle_check_box_outline_blank, 0);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_child_nang_suat, parent, false);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ChildViewHolder) convertView.getTag();
        ChildInfo childInfo = getChild(groupPosition, childPosition);
        holder.tvOrderNumber.setText(childInfo.getOrderNumber());
        holder.tvStartTime.setText(childInfo.getStartTime());
        holder.tvEndTime.setText(childInfo.getEndTime());
        holder.tvTotal.setText(Utilities.formatFloat(childInfo.getTOTAL()));
        if (childPosition % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        @Bind(R.id.item_tv_performance_payrollDate)
        TextView tvPayrollDate;
        @Bind(R.id.item_tv_performance_timeWork)
        TextView tvTimeWork;
        @Bind(R.id.item_tv_performance_status)
        TextView tvStatus;
        @Bind(R.id.item_tv_performance_night)
        TextView tvNight;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @Bind(R.id.item_tv_performance_order_number)
        TextView tvOrderNumber;
        @Bind(R.id.item_tv_performance_start_time)
        TextView tvStartTime;
        @Bind(R.id.item_tv_performance_end_time)
        TextView tvEndTime;
        @Bind(R.id.item_tv_performance_total)
        TextView tvTotal;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
