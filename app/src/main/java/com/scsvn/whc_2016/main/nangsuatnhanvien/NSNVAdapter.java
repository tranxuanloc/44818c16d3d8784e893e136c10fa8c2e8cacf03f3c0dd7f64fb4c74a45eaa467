package com.scsvn.whc_2016.main.nangsuatnhanvien;

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

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class NSNVAdapter extends ArrayAdapter<EmployeeWorkingByDate> implements Filterable {
    private ArrayList<EmployeeWorkingByDate> dataRelease;
    private ArrayList<EmployeeWorkingByDate> dataOrigin;

    public NSNVAdapter(Context context, ArrayList<EmployeeWorkingByDate> objects) {
        super(context, R.layout.item_schedule_job, objects);
        dataOrigin = objects;
        dataRelease = objects;

    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public EmployeeWorkingByDate getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_nsnv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        EmployeeWorkingByDate info = getItem(position);

        holder.nameTV.setText(info.getVietnamName());
        holder.jobTV.setText(info.getJobName());
        holder.nsTV.setText(NumberFormat.getInstance().format(info.getTotal()));

        if (position % 2 == 0)
            convertView.setBackgroundColor(Color.WHITE);
        else
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<EmployeeWorkingByDate> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        EmployeeWorkingByDate info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getVietnamName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        if (name.contains(constraint))
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
                dataRelease = (ArrayList<EmployeeWorkingByDate>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        TextView nameTV;
        TextView nsTV;
        TextView jobTV;

        public ViewHolder(View view) {
            nameTV = (TextView) view.findViewById(R.id.item_nsnv_name);
            nsTV = (TextView) view.findViewById(R.id.item_nsnv_nang_suat);
            jobTV = (TextView) view.findViewById(R.id.item_nsnv_job);
        }
    }
}
