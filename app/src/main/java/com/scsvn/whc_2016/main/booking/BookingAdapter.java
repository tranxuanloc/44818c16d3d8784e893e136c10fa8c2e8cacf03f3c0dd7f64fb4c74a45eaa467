package com.scsvn.whc_2016.main.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by xuanloc on 12/12/2016.
 */
public class BookingAdapter extends ArrayAdapter<Booking> {

    private final LayoutInflater layoutInflater;

    public BookingAdapter(Context context, ArrayList<Booking> objects) {
        super(context, 0, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_booking, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();
        Booking item = getItem(position);
        holder.tvTimeSlot.setText(item.getTimeSlot());
        holder.tvWeight.setText(NumberFormat.getInstance().format(item.getWeightAll()));
        holder.tvWeightR.setText(NumberFormat.getInstance().format(item.getWeightRO()));
        holder.tvWeightD.setText(NumberFormat.getInstance().format(item.getWeightDO()));
        holder.tvPallet.setText(String.format(Locale.getDefault(), "%d", item.getPalletAll()));
        holder.tvPalletR.setText(String.format(Locale.getDefault(), "%d", item.getPalletRO()));
        holder.tvPalletD.setText(String.format(Locale.getDefault(), "%d", item.getPalletDO()));

        return convertView;
    }

    private class ViewHolder {
        TextView tvTimeSlot;
        TextView tvWeight;
        TextView tvWeightR;
        TextView tvWeightD;
        TextView tvPallet;
        TextView tvPalletR;
        TextView tvPalletD;

        public ViewHolder(View view) {
            tvTimeSlot = (TextView) view.findViewById(R.id.item_booking_tv_time_slot);
            tvWeight = (TextView) view.findViewById(R.id.item_booking_tv_weight);
            tvWeightR = (TextView) view.findViewById(R.id.item_booking_tv_weight_r);
            tvWeightD = (TextView) view.findViewById(R.id.item_booking_tv_weight_d);
            tvPallet = (TextView) view.findViewById(R.id.item_booking_tv_pallet);
            tvPalletR = (TextView) view.findViewById(R.id.item_booking_tv_pallet_r);
            tvPalletD = (TextView) view.findViewById(R.id.item_booking_tv_pallet_d);
        }

    }
}
