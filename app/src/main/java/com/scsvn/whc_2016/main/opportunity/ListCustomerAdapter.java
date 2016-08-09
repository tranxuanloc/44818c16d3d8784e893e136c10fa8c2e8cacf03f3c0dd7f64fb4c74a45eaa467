package com.scsvn.whc_2016.main.opportunity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by tranxuanloc on 8/8/2016.
 */
public class ListCustomerAdapter extends ArrayAdapter<Customer> implements Filterable {
    private ArrayList<Customer> origin;
    private ArrayList<Customer> release;

    public ListCustomerAdapter(Context context, ArrayList<Customer> objects) {
        super(context, R.layout.item_customer, objects);
        origin = objects;
        release = (ArrayList<Customer>) objects.clone();
    }

    @Override
    public int getCount() {
        return release.size();
    }

    @Override
    public Customer getItem(int position) {
        return release.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_customer, parent, false);
            holder = new ViewHolder();
            holder.idView = (TextView) convertView.findViewById(R.id.customer_id);
            holder.typeView = (TextView) convertView.findViewById(R.id.customer_type);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Customer item = getItem(position);
        holder.idView.setText(String.format(Locale.getDefault(), "%d  %s", item.getId(), item.getName()));
        holder.typeView.setText(String.format(Locale.getDefault(), "%s  %s", item.getNumber(), item.getType()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() != 0) {
                    ArrayList<Customer> temp = new ArrayList<>();
                    for (Customer item : origin) {
                        if (item.getName().contains(constraint)
                                || item.getNumber().contains(constraint)
                                || item.getType().contains(constraint)
                                || String.valueOf(item.getId()).contains(constraint))
                            temp.add(item);
                    }
                    results.values = temp;
                    results.count = temp.size();
                } else {
                    results.values = origin;
                    results.count = origin.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                release.clear();
                release.addAll((ArrayList<Customer>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        TextView idView;
        TextView typeView;
    }

}
