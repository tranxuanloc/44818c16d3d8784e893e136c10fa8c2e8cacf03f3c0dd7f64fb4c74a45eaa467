package com.scsvn.whc_2016.main.chuyenhang.lichsu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class HistoryAdapter extends ArrayAdapter<StockMovementHistoriesInfo> {
    private LayoutInflater inflater;

    public HistoryAdapter(Context context, List<StockMovementHistoriesInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lich_su_chuyen_hang, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        StockMovementHistoriesInfo info = getItem(position);
        holder.tvReceivingOrderNumber.setText(info.getRO());
        holder.tvProductNumber.setText(info.getProductNumber());
        holder.tvProductName.setText(info.getProductName());
        holder.tvPalletID.setText(String.format(Locale.UK, "%d", info.getPalletID()));
        holder.tvAuthorisedBy.setText(info.getCreatedBy());
        holder.tvFromLocation.setText(info.getFromLocation());
        holder.tvReasonMovement.setText(info.getReasonMovement());
        holder.tvToLocation.setText(info.getToLocation());
        if (info.getReasonMovement().equals("Moved"))
            holder.ivReason.setImageResource(R.drawable.left_arrow24x24);
        else if (info.getReasonMovement().equals("Reversed"))
            holder.ivReason.setImageResource(R.drawable.adept_update);
        else
            holder.ivReason.setImageResource(R.drawable.left_24x24);
        if (info.getActualLocation().equals(info.getToLocation()))
            convertView.setBackgroundColor(Color.WHITE);
        else
            convertView.setBackgroundColor(Color.argb(200, 170, 0, 0));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_lsch_ToLocation)
        TextView tvToLocation;
        @Bind(R.id.item_tv_lsch_AuthorisedBy)
        TextView tvAuthorisedBy;
        @Bind(R.id.item_tv_lsch_FromLocation)
        TextView tvFromLocation;
        @Bind(R.id.item_tv_lsch_PalletID)
        TextView tvPalletID;
        @Bind(R.id.item_tv_lsch_ProductName)
        TextView tvProductName;
        @Bind(R.id.item_tv_lsch_ProductNumber)
        TextView tvProductNumber;
        @Bind(R.id.item_tv_lsch_ReasonMovement)
        TextView tvReasonMovement;
        @Bind(R.id.item_tv_lsch_ReceivingOrderNumber)
        TextView tvReceivingOrderNumber;
        @Bind(R.id.item_iv_lsch_ImgReason)
        ImageView ivReason;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}