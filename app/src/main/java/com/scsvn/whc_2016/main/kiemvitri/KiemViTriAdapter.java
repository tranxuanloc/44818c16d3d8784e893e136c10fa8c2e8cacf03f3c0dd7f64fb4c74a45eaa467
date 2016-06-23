package com.scsvn.whc_2016.main.kiemvitri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class KiemViTriAdapter extends ArrayAdapter<LocationCheckingInfo> {
    private LayoutInflater inflater;

    public KiemViTriAdapter(Context context, List<LocationCheckingInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_kiem_vi_tri, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        LocationCheckingInfo info = getItem(position);
        holder.tvCurrentQuantity.setText(String.format("%d", info.getCurrentQuantity()));
        holder.tvCustomerNumber.setText(info.getCustomerNumber());
        holder.tvCustomerRef.setText(String.format("%s %s -> %s", info.getCustomerRef(), info.getProductionDate(), info.getUseByDate()));
        holder.tvLocationNumber.setText(info.getLocationNumber());
        holder.tvPalletID.setText(String.format("%d", info.getPalletID()));
        holder.tvProductName.setText(info.getProductName());
        holder.tvProductNumber.setText(info.getProductNumber());
        holder.tvReceivingOrderNumber.setText(info.getReceivingOrderNumber());
        holder.tvNSX.setText(String.format("NSX:%s", info.getProductionDate()));
        holder.tvHSD.setText(String.format("HSD:%s", info.getUseByDate()));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_kvt_CurrentQuantity)
        TextView tvCurrentQuantity;
        @Bind(R.id.item_tv_kvt_CustomerNumber)
        TextView tvCustomerNumber;
        @Bind(R.id.item_tv_kvt_CustomerRef)
        TextView tvCustomerRef;
        @Bind(R.id.item_tv_kvt_LocationNumber)
        TextView tvLocationNumber;
        @Bind(R.id.item_tv_kvt_PalletID)
        TextView tvPalletID;
        @Bind(R.id.item_tv_kvt_ProductName)
        TextView tvProductName;
        @Bind(R.id.item_tv_kvt_ProductNumber)
        TextView tvProductNumber;
        @Bind(R.id.item_tv_kvt_ReceivingOrderNumber)
        TextView tvReceivingOrderNumber;
        @Bind(R.id.item_tv_kvt_product_date)
        TextView tvNSX;
        @Bind(R.id.item_tv_kvt_use_by_date)
        TextView tvHSD;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}