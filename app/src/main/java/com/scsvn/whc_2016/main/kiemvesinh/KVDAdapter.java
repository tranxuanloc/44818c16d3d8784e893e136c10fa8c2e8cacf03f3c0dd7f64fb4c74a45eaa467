package com.scsvn.whc_2016.main.kiemvesinh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;

/**
 * Created by xuanloc on 3/18/2017.
 */

public class KVDAdapter extends ArrayAdapter<HouseKeepingCheck> {
    private final LayoutInflater inflater;

    public KVDAdapter(Context context, @NonNull ArrayList<HouseKeepingCheck> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_kvs, parent, false);
            holder.tvRoom = (TextView) convertView.findViewById(R.id.item_kvs_room);
            holder.tvTime = (TextView) convertView.findViewById(R.id.item_kvs_time);
            holder.tvCheckBy = (TextView) convertView.findViewById(R.id.item_kvs_check_by);
            holder.tvResult = (TextView) convertView.findViewById(R.id.item_kvs_result);
            holder.tvRemark = (TextView) convertView.findViewById(R.id.item_kvs_remark);

            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        HouseKeepingCheck item = getItem(position);
        holder.tvRoom.setText(item.roomDescription);
        holder.tvTime.setText(Utilities.formatDate_ddMMyyHHmm(item.checkDate));
        holder.tvCheckBy.setText(item.checkBy);
        holder.tvResult.setText(item.checkGrade);
        holder.tvRemark.setText(item.checkRemark);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvRoom;
        TextView tvTime;
        TextView tvCheckBy;
        TextView tvResult;
        TextView tvRemark;
    }
}
