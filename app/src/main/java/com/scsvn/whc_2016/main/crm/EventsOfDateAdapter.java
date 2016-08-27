package com.scsvn.whc_2016.main.crm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
            holder.location = (TextView) convertView.findViewById(R.id.item_event_location);
            holder.color = convertView.findViewById(R.id.item_event_of_date_color);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Event item = getItem(position);
        holder.duration.setText(String.format("%s - %s", eventTime(item.getStartTime()), eventTime(item.getEndTime())));
        holder.title.setText(item.getSubject());
        if (item.getLocation().length() > 0)
            holder.location.setVisibility(View.VISIBLE);
        else
            holder.location.setVisibility(View.GONE);
        holder.location.setText(item.getLocation());
        holder.color.setBackgroundColor(Color.parseColor(new ListLabel().getColor(item.getLabel())));

        return convertView;

    }

    private String eventTime(String date) {
        SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, HH:mm", Locale.US);
        try {
            return formatter.format(original.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private class ViewHolder {
        TextView title;
        View color;
        TextView duration;
        TextView location;
    }
}
