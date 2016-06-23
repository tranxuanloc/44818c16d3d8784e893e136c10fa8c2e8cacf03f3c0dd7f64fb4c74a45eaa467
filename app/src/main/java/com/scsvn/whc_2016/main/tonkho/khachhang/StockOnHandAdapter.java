package com.scsvn.whc_2016.main.tonkho.khachhang;

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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/8/2016.
 */
public class StockOnHandAdapter extends ArrayAdapter<StockOnHandInfo> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<StockOnHandInfo> dataOrigin;
    private ArrayList<StockOnHandInfo> dataRelease;

    public StockOnHandAdapter(Context context, ArrayList<StockOnHandInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public StockOnHandInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_stock_on_hand, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        StockOnHandInfo info = dataRelease.get(position);
        holder.tvCusName.setText(info.getCustomerName());
        holder.tvCusNumber.setText(info.getCustomerNumber());
        holder.tvTotalAfter.setText(NumberFormat.getInstance().format(info.getTotalAfterDPCtns()));
        holder.tvTotalCurrent.setText(NumberFormat.getInstance().format(info.getTotalCurrentCtns()));
        holder.tvTotalLocation.setText(NumberFormat.getInstance().format(info.getTotalLocation()));
        holder.tvTotalPallet.setText(NumberFormat.getInstance().format(info.getTotalPallet()));
        holder.tvTotalWeight.setText(NumberFormat.getInstance().format(info.getTotalWeight()));
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
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<StockOnHandInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        StockOnHandInfo info = dataOrigin.get(i);
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
                dataRelease = (ArrayList<StockOnHandInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder {
        @Bind(R.id.item_tv_stock_on_hand_CusName)
        android.widget.TextView tvCusName;
        @Bind(R.id.item_tv_stock_on_hand_CusNumber)
        TextView tvCusNumber;
        @Bind(R.id.item_tv_stock_on_hand_TotalAfter)
        TextView tvTotalAfter;
        @Bind(R.id.item_tv_stock_on_hand_TotalCurrent)
        TextView tvTotalCurrent;
        @Bind(R.id.item_tv_stock_on_hand_TotalLocation)
        TextView tvTotalLocation;
        @Bind(R.id.item_tv_stock_on_hand_TotalPallet)
        TextView tvTotalPallet;
        @Bind(R.id.item_tv_stock_on_hand_TotalWeight)
        TextView tvTotalWeight;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
