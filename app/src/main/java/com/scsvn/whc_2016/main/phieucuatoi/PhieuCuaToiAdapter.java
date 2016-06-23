package com.scsvn.whc_2016.main.phieucuatoi;

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
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class PhieuCuaToiAdapter extends ArrayAdapter<PhieuCuaToiInfo> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<PhieuCuaToiInfo> dataRelease;
    private ArrayList<PhieuCuaToiInfo> dataOrigin;

    public PhieuCuaToiAdapter(Context context, ArrayList<PhieuCuaToiInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataRelease = objects;
        dataOrigin = objects;
    }

    @Override
    public PhieuCuaToiInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.items_phieu, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        PhieuCuaToiInfo info = dataRelease.get(position);
        holder.orderNumber.setText(info.getOrderNumber());
        holder.orderDate.setText(info.getOrderDate());
        holder.specialRequirement.setText(info.getSpecialRequirement());
        holder.customerNumber.setText(String.format("%s  %s", info.getCustomerNumber(), info.getCustomerName()));
        holder.dockNumber.setText(info.getDockNumber());
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<PhieuCuaToiInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        PhieuCuaToiInfo info = dataOrigin.get(i);
                        String orderNumber = Normalizer.normalize(info.getOrderNumber().toLowerCase(), Normalizer.Form.NFD).replace("-", "");
                        if (orderNumber.contains(keyword))
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
                dataRelease = (ArrayList<PhieuCuaToiInfo>) results.values;
                notifyDataSetChanged();
//                clear();
//                for (int i = 0; i < dataRelease.size(); i++)
//                    add(dataRelease.get(i));
//                notifyDataSetInvalidated();
            }
        };
        return filter;
    }

    static class ViewHolder {
        @Bind(R.id.order_number)
        TextView orderNumber;
        @Bind(R.id.order_date)
        TextView orderDate;
        @Bind(R.id.dock_number)
        TextView dockNumber;
        @Bind(R.id.special_requirement)
        TextView specialRequirement;
        @Bind(R.id.customer_number)
        TextView customerNumber;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
