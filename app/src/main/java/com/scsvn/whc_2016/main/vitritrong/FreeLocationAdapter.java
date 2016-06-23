package com.scsvn.whc_2016.main.vitritrong;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
public class FreeLocationAdapter extends ArrayAdapter<FreeLocationInfo> {
    private LayoutInflater inflater;

    public FreeLocationAdapter(Context context, List<FreeLocationInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_free_location, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        FreeLocationInfo info = getItem(position);
        holder.tvFreeQty.setText(String.format("%d", info.getQtyOfFree()));
        holder.tvPalletQty.setText(String.format("%d", info.getQtyOfPallets_OnHand()));
        holder.tvH.setText(String.format("%d", info.getQtyFree_High()));
        holder.tvL.setText(String.format("%d", info.getQtyFree_Low()));
        holder.tvRoomID.setText(info.getRoomID());
        holder.tvVH.setText(String.format("%d", info.getQtyFree_VeryHigh()));
        holder.tvVL.setText(String.format("%d", info.getQtyFree_VeryLow()));
        holder.tvWF.setText(String.format("%d", info.getQtyFreeAfterDP()));
        holder.tvTotal.setText(String.format("%d", info.getQtyLocation()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tvFreeLocationFreeQty)
        TextView tvFreeQty;
        @Bind(R.id.tvFreeLocationH)
        TextView tvH;
        @Bind(R.id.tvFreeLocationL)
        TextView tvL;
        @Bind(R.id.tvFreeLocationVH)
        TextView tvVH;
        @Bind(R.id.tvFreeLocationVL)
        TextView tvVL;
        @Bind(R.id.tvFreeLocationWF)
        TextView tvWF;
        @Bind(R.id.tvFreeLocationPalletQty)
        TextView tvPalletQty;
        @Bind(R.id.tvFreeLocationRoomID)
        TextView tvRoomID;
        @Bind(R.id.tvFreeLocationTotal)
        TextView tvTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}