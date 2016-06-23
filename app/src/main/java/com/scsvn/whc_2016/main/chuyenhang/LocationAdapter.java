package com.scsvn.whc_2016.main.chuyenhang;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class LocationAdapter extends ArrayAdapter<LocationInfo> {
    private static final String TAG = LocationAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    public LocationAdapter(Context context, List<LocationInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chuyen_hang, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final LocationInfo info = getItem(position);
        holder.tvName.setText(info.getProductName());
        holder.tvNumber.setText(info.getProductNumber());
        holder.tvPalletID.setText(String.format("%d", info.getPalletID()));
        holder.tvQty.setText(String.format("%d", info.getCurrentQuantity()));
        holder.tvQtyMoved.setText(String.format("%d", info.getAfterDPQuantity()));
        holder.tvRef.setText(info.getCustomerRef());
        holder.tvRO.setText(info.getRO());
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                info.setIsChecked(isChecked);
                Log.e(TAG, "onCheckedChanged: " + position + " " + isChecked);
            }
        });
        if (info.isChecked())
            holder.cb.setChecked(true);
        else
            holder.cb.setChecked(false);
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_chuyen_hang_name)
        TextView tvName;
        @Bind(R.id.item_tv_chuyen_hang_number)
        TextView tvNumber;
        @Bind(R.id.item_tv_chuyen_hang_pallet_id)
        TextView tvPalletID;
        @Bind(R.id.item_tv_chuyen_hang_qty)
        TextView tvQty;
        @Bind(R.id.item_tv_chuyen_hang_qty_moved)
        TextView tvQtyMoved;
        @Bind(R.id.item_tv_chuyen_hang_ref)
        TextView tvRef;
        @Bind(R.id.item_tv_chuyen_hang_ro)
        TextView tvRO;
        @Bind(R.id.item_cb_chuyen_hang)
        CheckBox cb;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}