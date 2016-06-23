package com.scsvn.whc_2016.main.phieuhomnay;

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
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/8/2016.
 */
public class KhachHangAdapter extends ArrayAdapter<InOutToDayUnFinishInfo> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<InOutToDayUnFinishInfo> dataOrigin;
    private ArrayList<InOutToDayUnFinishInfo> dataRelease;

    public KhachHangAdapter(Context context, ArrayList<InOutToDayUnFinishInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public InOutToDayUnFinishInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.items_khach_hang, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        InOutToDayUnFinishInfo info = dataRelease.get(position);
        holder.cusName.setText(info.getCustomerName());
        holder.cusNumber.setText(info.getCustomerNumber());
        holder.quantity.setText(String.format(Locale.US, "%d", info.getOrderQty()));
        holder.tvTotalWeight.setText(String.format("W: %s", NumberFormat.getInstance().format(info.getTotalWeight())));
        holder.tvTotalPackage.setText(String.format(Locale.US, "Ctn: %d", info.getTotalPackages()));
        Utilities.setUnderLine(holder.tvScannedOrderQty, info.getScannedOrderQty());
        holder.tvScannedOrderQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);

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
                    ArrayList<InOutToDayUnFinishInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        InOutToDayUnFinishInfo info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getCustomerName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String nameNoSpace = Normalizer.normalize(info.getCustomerName().toLowerCase(), Normalizer.Form.NFD).replaceAll(" ", "").replaceAll("[^\\p{ASCII}]", "");
                        String number = Normalizer.normalize(info.getCustomerNumber().toLowerCase(), Normalizer.Form.NFD).replaceAll("-", "");
                        if (name.contains(keyword) || nameNoSpace.contains(keyword) || number.contains(keyword))
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
                dataRelease = (ArrayList<InOutToDayUnFinishInfo>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_customerName)
        TextView cusName;
        @Bind(R.id.tv_customerNumber)
        TextView cusNumber;
        @Bind(R.id.tv_quantity)
        TextView quantity;
        @Bind(R.id.tv_ScannedOrderQty)
        TextView tvScannedOrderQty;
        @Bind(R.id.tv_total_package)
        TextView tvTotalWeight;
        @Bind(R.id.tv_total_weight)
        TextView tvTotalPackage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
