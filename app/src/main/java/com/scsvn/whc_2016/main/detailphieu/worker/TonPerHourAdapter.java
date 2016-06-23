package com.scsvn.whc_2016.main.detailphieu.worker;

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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 5/24/2016.
 */
public class TonPerHourAdapter extends ArrayAdapter<EmployeeWorkingTonPerHourInfo> implements Filterable {
    private LayoutInflater inflater;
    private List<EmployeeWorkingTonPerHourInfo> dataRelease;
    private List<EmployeeWorkingTonPerHourInfo> dataOrigin;

    public TonPerHourAdapter(Context context, List<EmployeeWorkingTonPerHourInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public EmployeeWorkingTonPerHourInfo getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_ton_per_hour, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        EmployeeWorkingTonPerHourInfo info = getItem(position);
        holder.tvTon.setText(NumberFormat.getInstance().format(info.getTonPerHour()));
        holder.tvPercent.setText(NumberFormat.getInstance().format(info.getPercentage()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    List<EmployeeWorkingTonPerHourInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        EmployeeWorkingTonPerHourInfo info = dataOrigin.get(i);
                        String id = String.valueOf(info.getPercentage());
                        if (id.contains(constraint))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = dataOrigin.size();
                    results.values = dataOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataRelease = (List<EmployeeWorkingTonPerHourInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @Bind(R.id.item_ton_per_hour)
        TextView tvTon;
        @Bind(R.id.item_ton_per_hour_percent)
        TextView tvPercent;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}