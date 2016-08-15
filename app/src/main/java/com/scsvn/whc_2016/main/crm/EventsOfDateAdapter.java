package com.scsvn.whc_2016.main.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;

/**
 * Created by tranxuanloc on 8/10/2016.
 */
public class EventsOfDateAdapter extends ArrayAdapter<Event> {
    public EventsOfDateAdapter(Context context, ArrayList<Event> objects) {
        super(context, R.layout.item_event_of_date, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event_of_date, parent, false);
            holder.duration = (TextView) convertView.findViewById(R.id.item_event_duration);
            holder.title = (TextView) convertView.findViewById(R.id.item_event_title);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Event item = getItem(position);
        holder.duration.setText(item.getDescription());
        holder.title.setText(item.getSubject());
        return convertView;

    }

    private class ViewHolder {
        TextView title;
        TextView duration;
    }
}
