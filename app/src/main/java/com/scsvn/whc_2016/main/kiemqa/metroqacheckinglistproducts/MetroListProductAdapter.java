package com.scsvn.whc_2016.main.kiemqa.metroqacheckinglistproducts;

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
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class MetroListProductAdapter extends ArrayAdapter<QACheckingListProductsInfo> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<QACheckingListProductsInfo> dataRelease;
    private ArrayList<QACheckingListProductsInfo> dataOrigin;

    public MetroListProductAdapter(Context context, ArrayList<QACheckingListProductsInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public QACheckingListProductsInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_metro_list_product, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        QACheckingListProductsInfo info = getItem(position);
        holder.tvName.setText(info.getProductName());
        holder.tvActual.setText(NumberFormat.getInstance().format(info.getActualQuantity()));
        holder.tvOrder.setText(NumberFormat.getInstance().format(info.getOrderQuantity()));
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
                    ArrayList<QACheckingListProductsInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        QACheckingListProductsInfo info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getProductName().toLowerCase(), Normalizer.Form.NFD)/*.replaceAll("[^\\p{ASCII}]", "")*/;
                        if (name.contains(keyword))
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
                dataRelease = (ArrayList<QACheckingListProductsInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_metro_list_product_name)
        TextView tvName;
        @Bind(R.id.item_tv_metro_list_product_actual_quantity)
        TextView tvActual;
        @Bind(R.id.item_tv_metro_list_product_order_quantity)
        TextView tvOrder;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}