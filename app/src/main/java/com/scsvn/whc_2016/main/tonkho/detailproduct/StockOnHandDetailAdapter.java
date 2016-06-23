package com.scsvn.whc_2016.main.tonkho.detailproduct;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/8/2016.
 */
public class StockOnHandDetailAdapter extends ArrayAdapter<StockOnHandDetailsInfo> implements Filterable {
    private LayoutInflater inflater;

    public StockOnHandDetailAdapter(Context context, ArrayList<StockOnHandDetailsInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_stock_on_hand_detail, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        StockOnHandDetailsInfo info = getItem(position);
        holder.tvCustomerRef.setText(info.getCustomerRef());
        holder.tvLocationNumber.setText(info.getLocationNumber());
        holder.tvProductionDate.setText(info.getProductionDate());
        holder.tvReceivingOrderDate.setText(info.getReceivingOrderDate());
        holder.tvReceivingOrderID.setText(String.format(Locale.CANADA, "%d", info.getReceivingOrderID()));
        holder.tvRemark.setText(info.getRemark());
        holder.tvStatus.setText(info.getPalletStatus());
        holder.tvUseByDater.setText(info.getUseByDate());
        holder.tvTotalAfter.setText(NumberFormat.getInstance().format(info.getTotalAfterDPCtns()));
        holder.tvTotalCurrent.setText(NumberFormat.getInstance().format(info.getTotalCurrentCtns()));
        holder.tvTotalWeight.setText(NumberFormat.getInstance().format(info.getTotalWeight()));
        holder.tvTotalUnit.setText(NumberFormat.getInstance().format(info.getTotalUnits()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);

        return convertView;
    }


    public static class ViewHolder {
        @Bind(R.id.item_tv_stock_on_hand_detail_Status)
        TextView tvStatus;
        @Bind(R.id.item_tv_stock_on_hand_detail_TotalUnit)
        TextView tvTotalUnit;
        @Bind(R.id.item_tv_stock_on_hand_detail_CustomerRef)
        TextView tvCustomerRef;
        @Bind(R.id.item_tv_stock_on_hand_detail_LocationNumber)
        TextView tvLocationNumber;
        @Bind(R.id.item_tv_stock_on_hand_detail_ProductionDate)
        TextView tvProductionDate;
        @Bind(R.id.item_tv_stock_on_hand_detail_ReceivingOrderDate)
        TextView tvReceivingOrderDate;
        @Bind(R.id.item_tv_stock_on_hand_detail_ReceivingOrderID)
        TextView tvReceivingOrderID;
        @Bind(R.id.item_tv_stock_on_hand_detail_Remark)
        TextView tvRemark;
        @Bind(R.id.item_tv_stock_on_hand_detail_TotalAfter)
        TextView tvTotalAfter;
        @Bind(R.id.item_tv_stock_on_hand_detail_TotalCurrent)
        TextView tvTotalCurrent;
        @Bind(R.id.item_tv_stock_on_hand_detail_TotalWeight)
        TextView tvTotalWeight;
        @Bind(R.id.item_tv_stock_on_hand_detail_UseByDater)
        TextView tvUseByDater;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
