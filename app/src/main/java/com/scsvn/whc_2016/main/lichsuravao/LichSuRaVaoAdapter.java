package com.scsvn.whc_2016.main.lichsuravao;

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
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class LichSuRaVaoAdapter extends ArrayAdapter<EmployeeInOutInfo> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<EmployeeInOutInfo> dataRelease;
    private ArrayList<EmployeeInOutInfo> dataOrigin;

    public LichSuRaVaoAdapter(Context context, ArrayList<EmployeeInOutInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public EmployeeInOutInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_lich_su_ra_vao, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        EmployeeInOutInfo info = getItem(position);
        holder.tvID.setText(String.format("%d", info.getEmployeeID()));
        holder.tvName.setText(info.getVietnamName());
        holder.tvPosition.setText(info.getVietnamPosition());
        holder.tvDepartment.setText(info.getDepartmentNameShort());
        holder.tvShift.setText(info.getShift());
        holder.tvTimeWork.setText(info.getTimeWork());
        holder.tvStatus.setText(info.getTimeKeepingStatus());

        if (info.getPayrollRemark().length() > 0) {
            holder.tvRemark.setText(info.getPayrollRemark());
            holder.tvRemark.setVisibility(View.VISIBLE);
        } else
            holder.tvRemark.setVisibility(View.GONE);

        if (info.getTimekeeper().length() > 0) {
            holder.tvTimeKeeper.setText(info.getTimekeeper());
            holder.tvTimeKeeper.setVisibility(View.VISIBLE);
        } else
            holder.tvTimeKeeper.setVisibility(View.GONE);

        if (info.isNightShift())
            holder.tvNight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_toggle_check_box, 0);
        else
            holder.tvNight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_toggle_check_box_outline_blank, 0);
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
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<EmployeeInOutInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        EmployeeInOutInfo info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getVietnamName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        if (name.contains(keyword) || Integer.toString(info.getEmployeeID()).contains(keyword))
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
                dataRelease = (ArrayList<EmployeeInOutInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_in_out_id)
        TextView tvID;
        @Bind(R.id.item_tv_in_out_name)
        TextView tvName;
        @Bind(R.id.item_tv_in_out_position)
        TextView tvPosition;
        @Bind(R.id.item_tv_in_out_department)
        TextView tvDepartment;
        @Bind(R.id.item_tv_in_out_night_shift)
        TextView tvNight;
        @Bind(R.id.item_tv_in_out_time_work)
        TextView tvTimeWork;
        @Bind(R.id.item_tv_in_out_shift)
        TextView tvShift;
        @Bind(R.id.item_tv_in_out_status)
        TextView tvStatus;
        @Bind(R.id.item_tv_in_out_time_keeper)
        TextView tvTimeKeeper;
        @Bind(R.id.item_tv_in_out_remark)
        TextView tvRemark;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}