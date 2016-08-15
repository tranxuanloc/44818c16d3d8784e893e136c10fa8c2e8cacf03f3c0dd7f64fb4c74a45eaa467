package com.scsvn.whc_2016.main.crm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;

/**
 * Created by tranxuanloc on 8/6/2016.
 */
public class LabelAdapter extends ArrayAdapter<Label> {
    public LabelAdapter(Context context, ArrayList<Label> objects) {
        super(context, R.layout.item_crm_label, objects);
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_crm_label, parent, false);
            holder.color = convertView.findViewById(R.id.item_label_color);
            holder.title = (TextView) convertView.findViewById(R.id.item_label_title);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Label item = getItem(position);
        holder.color.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.title.setText(item.getLabel());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_crm_label, parent, false);
            holder.color = convertView.findViewById(R.id.item_label_color);
            holder.title = (TextView) convertView.findViewById(R.id.item_label_title);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Label item = getItem(position);
        holder.color.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.title.setText(item.getLabel());
        return convertView;

    }

    private class ViewHolder {
        TextView title;
        View color;
    }
}
