package com.scsvn.whc_2016.main.lichlamviec;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class WorkingSchedulesEmployeePlanAdapter extends BaseExpandableListAdapter {

    private LinkedHashMap<String, List<WorkingSchedulesEmployeePlanInfo>> employeePlan = new LinkedHashMap<>();
    private List<String> employeePlanGroup = new LinkedList<>();

    public WorkingSchedulesEmployeePlanAdapter(LinkedHashMap<String, List<WorkingSchedulesEmployeePlanInfo>> employeePlan, List<String> employeePlanGroup) {
        this.employeePlan = employeePlan;
        this.employeePlanGroup = employeePlanGroup;
    }

    @Override
    public int getGroupCount() {
        return employeePlanGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return employeePlan.get(employeePlanGroup.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return employeePlanGroup.get(groupPosition);
    }

    @Override
    public WorkingSchedulesEmployeePlanInfo getChild(int groupPosition, int childPosition) {
        return employeePlan.get(employeePlanGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView tvPosition = new TextView(parent.getContext());
        tvPosition.setText(String.format(Locale.US, "%s (%d)", getGroup(groupPosition), getChildrenCount(groupPosition)));
        tvPosition.setGravity(Gravity.CENTER_HORIZONTAL);
        tvPosition.setTextColor(Color.argb(255, 0x3f, 0x51, 0xb5));
        return tvPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PlanViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_employee_plan, parent, false);
            holder = new PlanViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (PlanViewHolder) convertView.getTag();
        WorkingSchedulesEmployeePlanInfo item = getChild(groupPosition, childPosition);
        holder.tvID.setText(String.format(Locale.US, "%d", item.EmployeeID));
        holder.tvName.setText(item.VietnamName);
        holder.tvDateStatus.setText(item.DayStatus.trim());
        holder.tvTimeIn.setText(Utilities.formatDate_HHmm(item.TimeIn));
        holder.tvNight.setText(String.format(Locale.US, "%dh", item.Shift));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class PlanViewHolder {
        @Bind(R.id.itemEmployeePlanID)
        TextView tvID;
        @Bind(R.id.itemEmployeePlanName)
        TextView tvName;
        @Bind(R.id.itemEmployeePlanTimeIn)
        TextView tvTimeIn;
        @Bind(R.id.itemEmployeePlanDateStatus)
        TextView tvDateStatus;
        @Bind(R.id.itemEmployeePlanNight)
        TextView tvNight;

        public PlanViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}