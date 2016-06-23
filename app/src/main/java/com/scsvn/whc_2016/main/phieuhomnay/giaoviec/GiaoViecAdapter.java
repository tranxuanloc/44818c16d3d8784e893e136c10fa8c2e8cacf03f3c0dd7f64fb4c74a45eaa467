package com.scsvn.whc_2016.main.phieuhomnay.giaoviec;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/12/2016.
 */
public class GiaoViecAdapter extends ArrayAdapter<GiaoViecInfo> {
    private LayoutInflater inflater;

    public GiaoViecAdapter(Context context, ArrayList<GiaoViecInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_giao_viec, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        GiaoViecInfo info = getItem(position);
        holder.emplID.setText(String.format("%d", info.getEmployeeID()));
        holder.emplName.setText(info.getEmployeeName());
        holder.emplPercent.setText(String.format("%d", info.getPercentage()));
        holder.emplRemark.setText(info.getRemark());
        holder.emplProdQty.setText(String.format("%d", info.getProductionQuantity()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(Color.argb(255, 204, 255, 144));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_employee_id)
        TextView emplID;
        @Bind(R.id.tv_employee_name)
        TextView emplName;
        @Bind(R.id.tv_employee_percent)
        TextView emplPercent;
        @Bind(R.id.tv_employee_cv)
        TextView emplRemark;
        @Bind(R.id.tv_production_qty)
        TextView emplProdQty;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
