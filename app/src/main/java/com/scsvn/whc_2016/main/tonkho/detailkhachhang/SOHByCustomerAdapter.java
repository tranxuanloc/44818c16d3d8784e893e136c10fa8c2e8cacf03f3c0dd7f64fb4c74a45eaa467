package com.scsvn.whc_2016.main.tonkho.detailkhachhang;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class SOHByCustomerAdapter extends ArrayAdapter<StockOnHandByCustomerInfo> {
    private LayoutInflater inflater;

    public SOHByCustomerAdapter(Context context, List<StockOnHandByCustomerInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_stock_on_hand_by_customer, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        StockOnHandByCustomerInfo info = getItem(position);
        holder.tvProductName.setText(info.getProductName());
        holder.tvProductNumber.setText(info.getProductNumber());
        holder.tvTotalAfter.setText(NumberFormat.getInstance().format(info.getTotalAfterDPCtns()));
        holder.tvTotalCurrent.setText(NumberFormat.getInstance().format(info.getTotalCurrentCtns()));
        holder.tvTotalLocation.setText(NumberFormat.getInstance().format(info.getTotalLocation()));
        holder.tvTotalPallet.setText(NumberFormat.getInstance().format(info.getTotalPallet()));
        holder.tvTotalWeight.setText(NumberFormat.getInstance().format(info.getTotalWeight()));
        holder.tvTotalUnit.setText(NumberFormat.getInstance().format(info.getTotalUnit()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_stock_on_hand_ProductName)
        TextView tvProductName;
        @Bind(R.id.item_tv_stock_on_hand_ProductNumber)
        TextView tvProductNumber;
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
        @Bind(R.id.item_tv_stock_on_hand_TotalUnit)
        TextView tvTotalUnit;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}