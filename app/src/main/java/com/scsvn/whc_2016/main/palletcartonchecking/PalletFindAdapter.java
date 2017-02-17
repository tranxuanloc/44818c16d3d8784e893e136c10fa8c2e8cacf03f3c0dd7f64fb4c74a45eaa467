package com.scsvn.whc_2016.main.palletcartonchecking;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class PalletFindAdapter extends ArrayAdapter<PalletFind> {
    private LayoutInflater inflater;

    public PalletFindAdapter(Context context, ArrayList<PalletFind> objects) {
        super(context, R.layout.item_pallet_find, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pallet_find, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        PalletFind info = getItem(position);
        String receivingOrderNumber = info.getReceivingOrderNumber();
        holder.orderNumberTv.setText(receivingOrderNumber);
        holder.palletIdTV.setText(String.format("%s", info.getPalletId()));
        holder.customerRefTV.setText(String.format("CusRef:  %s", info.getCustomerRef()));
        holder.locationNumberTV.setText(String.format("%s", info.getLocationNumber().trim()));
        holder.currentQtyTV.setText(String.format(Locale.getDefault(), "Tồn:  %d", info.getCurrentQuantity()));
        holder.afterQtyTV.setText(String.format(Locale.getDefault(), "SL:  %d", info.getAfterDPQuantity()));
        holder.roDateTV.setText(String.format("Date:  %s", Utilities.formatDate_ddMMyyyy(info.getReceivingOrderDate())));
        holder.remarkTV.setText(String.format("%s", info.getRemark()));
        holder.createdTimeTV.setText(String.format("Created: %s by %s", Utilities.formatDate_ddMMyyHHmm(info.getCreatedTime()), info.getScannedBy()));
        holder.deviceTV.setText(String.format("Device: %s    Result: %s", info.getDeviceNumber(), info.getResult()));

        if (receivingOrderNumber.contains("RO")) {
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.parseColor("#C7EDFC"));
            holder.currentQtyTV.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView orderNumberTv;
        TextView palletIdTV;
        TextView customerRefTV;
        TextView locationNumberTV;
        TextView currentQtyTV;
        TextView afterQtyTV;
        TextView roDateTV;
        TextView remarkTV;
        TextView createdTimeTV;
        TextView deviceTV;

        public ViewHolder(View view) {
            orderNumberTv = (TextView) view.findViewById(R.id.item_pallet_find_order_number);
            palletIdTV = (TextView) view.findViewById(R.id.item_pallet_find_pallet_id);
            customerRefTV = (TextView) view.findViewById(R.id.item_pallet_find_customer_ref);
            locationNumberTV = (TextView) view.findViewById(R.id.item_pallet_find_location);
            currentQtyTV = (TextView) view.findViewById(R.id.item_pallet_find_current);
            afterQtyTV = (TextView) view.findViewById(R.id.item_pallet_find_after);
            roDateTV = (TextView) view.findViewById(R.id.item_pallet_find_ro_date);
            remarkTV = (TextView) view.findViewById(R.id.item_pallet_find_ro_remark);
            createdTimeTV = (TextView) view.findViewById(R.id.item_pallet_find_created_time);
            deviceTV = (TextView) view.findViewById(R.id.item_pallet_find_device);
        }
    }
}