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
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 5/13/2016.
 */
public class EmployeePresentAdapter extends ArrayAdapter<EmployeeInfo> implements Filterable {
    private Context context;
    private List<EmployeeInfo> dataRelease = new LinkedList<>();
    private List<EmployeeInfo> dataOrigin = new LinkedList<>();

    public EmployeePresentAdapter(Context context, List<EmployeeInfo> objects) {
        super(context, 0, objects);
        this.context = context;
        dataOrigin.addAll(objects);
        dataRelease.addAll(objects);
    }

    @Override
    public EmployeeInfo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_employee_present_worker, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        EmployeeInfo info = getItem(position);
        holder.tvID.setText(String.format(Locale.US, "%d", info.getEmployeeID()));
        holder.tvName.setText(info.getEmployeeName());
        holder.tvTimeIn.setText(info.getTimeIn());
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
                if (constraint != null && constraint.length() > 0) {
                    LinkedList<EmployeeInfo> arrayFilter = new LinkedList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        EmployeeInfo info = dataOrigin.get(i);
                        String id = String.valueOf(info.getEmployeeID());
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
                dataRelease = (LinkedList<EmployeeInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_employee_id)
        TextView tvID;
        @Bind(R.id.item_tv_employee_name)
        TextView tvName;
        @Bind(R.id.item_tv_employee_time_in)
        TextView tvTimeIn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
