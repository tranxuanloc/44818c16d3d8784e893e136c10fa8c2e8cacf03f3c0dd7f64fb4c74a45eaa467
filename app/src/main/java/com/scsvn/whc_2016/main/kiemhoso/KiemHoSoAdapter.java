package com.scsvn.whc_2016.main.kiemhoso;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class KiemHoSoAdapter extends ArrayAdapter<KiemHoSoInfo> {
    private LayoutInflater inflater;

    public KiemHoSoAdapter(Context context, List<KiemHoSoInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_kiem_ho_so, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        KiemHoSoInfo info = getItem(position);
        holder.tvCartonDescription.setText(info.getCartonDescription());
        holder.tvCartonNewID.setText(String.format(Locale.US, "%d", info.getCartonNewID()));
        holder.tvCartonRef.setText(info.getCartonRef());
        holder.tvCartonSize.setText(Utilities.formatFloat(info.getCartonSize()));
        holder.tvCustomerName.setText(info.getCustomerName());
        holder.tvCustomerNumber.setText(info.getCustomerNumber());
        holder.tvDestructionDate.setText(info.getDestructionDate());
        holder.tvOrderDate.setText(info.getOrderDate());
        holder.tvOrderNumber.setText(info.getOrderNumber());
        holder.tvRemark.setText(info.getRemark());
        holder.tvResul.setText(info.getResult());
        if (info.getResult().equalsIgnoreCase("OK")) {
            convertView.setBackgroundColor(Color.argb(255, 76, 175, 80));
            if (info.isRecordNew())
                convertView.setBackgroundColor(Color.argb(255, 255, 238, 88));
        } else if (info.getResult().equalsIgnoreCase("NO"))
            convertView.setBackgroundColor(Color.argb(255, 183, 28, 28));
        else convertView.setBackgroundColor(Color.argb(0, 76, 175, 80));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_khs_CartonDescription)
        TextView tvCartonDescription;
        @Bind(R.id.item_tv_khs_CartonNewID)
        TextView tvCartonNewID;
        @Bind(R.id.item_tv_khs_CartonRef)
        TextView tvCartonRef;
        @Bind(R.id.item_tv_khs_CartonSize)
        TextView tvCartonSize;
        @Bind(R.id.item_tv_khs_CustomerName)
        TextView tvCustomerName;
        @Bind(R.id.item_tv_khs_CustomerNumber)
        TextView tvCustomerNumber;
        @Bind(R.id.item_tv_khs_DestructionDate)
        TextView tvDestructionDate;
        @Bind(R.id.item_tv_khs_OrderDate)
        TextView tvOrderDate;
        @Bind(R.id.item_tv_khs_OrderNumber)
        TextView tvOrderNumber;
        @Bind(R.id.item_tv_khs_Remark)
        TextView tvRemark;
        @Bind(R.id.item_tv_khs_Result)
        TextView tvResul;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}