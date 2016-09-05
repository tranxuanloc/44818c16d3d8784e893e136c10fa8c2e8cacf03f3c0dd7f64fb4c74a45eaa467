package com.scsvn.whc_2016.main.mms;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;

/**
 * Created by tranxuanloc on 8/19/2016.
 */
public class MaintenanceJobAdapter extends RecyclerView.Adapter<MaintenanceJobAdapter.ViewHolder> implements Filterable {
    private final int widthPixels;
    private ArrayList<MaintenanceJob> origin;
    private ArrayList<MaintenanceJob> release;
    private Context context;

    public MaintenanceJobAdapter(Context context, ArrayList<MaintenanceJob> objects) {
        origin = objects;
        this.context = context;
        release = (ArrayList<MaintenanceJob>) objects.clone();
        widthPixels = context.getResources().getDisplayMetrics().widthPixels;
    }


    public MaintenanceJob getItem(int position) {
        return release.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() != 0) {
                    constraint = constraint.toString().toLowerCase();

                    ArrayList<MaintenanceJob> temp = new ArrayList<>();
                    for (MaintenanceJob item : origin) {
                        String date = item.getDate();
                        String id = Integer.toString(item.getId());
                        String week = Float.toString(item.getWeek());
                        String createBy = item.getCreateBy();
                        String remark = item.getRemark();
                        if (date.contains(constraint) ||
                                id.contains(constraint) ||
                                week.contains(constraint) ||
                                createBy.contains(constraint) ||
                                remark.contains(constraint))
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
                release = (ArrayList<MaintenanceJob>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_maintenance_job, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MaintenanceJob item = getItem(position);
        String infoBuilder = new StringBuilder()
                .append("ID: ").append(item.getId()).append("        ")
                .append("Date: ").append(item.getDate()).append("        ")
                .append("Week: ").append(item.getWeek()).append("\n")
                .append("Create by: ").append(item.getCreateBy()).append("\n")
                .append("Confirm by: ").append(item.getConfirmBy()).append("\n")
                .append("Remark: ").append(item.getRemark())
                .toString();

        holder.info.setText(infoBuilder);

        if (position % 2 == 0)
            holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAlternativeRow));
        else
            holder.view.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return release.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView info;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            info = (TextView) view.findViewById(R.id.item_maintenance_job_info);
            info.setMinWidth(widthPixels);
        }
    }

}
