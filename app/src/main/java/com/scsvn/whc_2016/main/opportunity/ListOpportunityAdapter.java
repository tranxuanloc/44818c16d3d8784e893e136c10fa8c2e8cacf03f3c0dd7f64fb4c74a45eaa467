package com.scsvn.whc_2016.main.opportunity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;

/**
 * Created by tranxuanloc on 8/3/2016.
 */
public class ListOpportunityAdapter extends ArrayAdapter<Opportunity> {

    public ListOpportunityAdapter(Context context, ArrayList<Opportunity> objects) {
        super(context, R.layout.opportunity_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.opportunity_item, parent, false);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.opportunity_name);
            holder.timeAndUser = (TextView) convertView.findViewById(R.id.opportunity_time_user_create);
            holder.description = (TextView) convertView.findViewById(R.id.opportunity_description);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        Opportunity item = getItem(position);
        holder.name.setText(item.getOpportunityName());
        holder.timeAndUser.setText(String.format("Create: %s  by %s  Close: %s", item.getCreatedTime(), item.getCreatedBy(), item.getClosedDate()));
        holder.description.setText(item.getDescription());
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView timeAndUser;
        TextView description;
    }

}
