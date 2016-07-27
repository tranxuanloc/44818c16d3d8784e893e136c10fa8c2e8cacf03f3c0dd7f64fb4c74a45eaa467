package com.scsvn.whc_2016.main.detailphieu;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Trần Xuân Lộc on 1/3/2016.
 */
public class DetailPhieuAdapter extends BaseExpandableListAdapter {
    private List<String> groupLevel1;
    private LinkedHashMap<String, List<Item>> groupLevel2;

    public DetailPhieuAdapter(List<String> groupLevel1
            , LinkedHashMap<String, List<Item>> groupLevel2) {
        this.groupLevel1 = groupLevel1;
        this.groupLevel2 = groupLevel2;
    }

    public void removeChildAt(int groupPosition, int childPosition) {
        groupLevel2.get(groupLevel1.get(groupPosition)).remove(childPosition);
        notifyDataSetChanged();
    }

    @Override
    public int getChildTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return groupLevel2.get(groupLevel1.get(groupPosition)).get(childPosition).getViewType();
    }

    @Override
    public int getGroupCount() {
        return groupLevel1.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupLevel2.get(groupLevel1.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return groupLevel1.get(groupPosition);
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {
        return groupLevel2.get(groupLevel1.get(groupPosition)).get(childPosition);
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
        TextView headerLevel1 = new TextView(parent.getContext());
        headerLevel1.setGravity(Gravity.CENTER_HORIZONTAL);
        headerLevel1.setTextColor(Color.WHITE);
        headerLevel1.setBackgroundColor(Color.argb(255, 127, 0, 127));
        headerLevel1.setText(getGroup(groupPosition));
        return headerLevel1;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Item info = getChild(groupPosition, childPosition);
        return info.getItem(inflater, convertView);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public enum RowType {TYPE_ITEM, TYPE_HEADER}

    class ViewHolder {

        public View view;
    }


}
