package com.scsvn.whc_2016.main.kiemcontainer;

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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class ListContainerAdapter extends ArrayAdapter<ContainerInfo> {
    private LayoutInflater inflater;

    public ListContainerAdapter(Context context, List<ContainerInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_list_container, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        ContainerInfo info = getItem(position);
        holder.tvContNumber.setText(info.getContainerNum());
        holder.tvContType.setText(info.getContainerType());
        holder.tvContCusName.setText(info.getCustomerName());
        holder.tvContInformation.setText(String.format("In: %s - Last: %s - Bởi: %s", info.getTimeIn(), Utilities.formatDate_ddMMyyHHmm(info.getCheckingTime()), info.getUserCheck()));
        ;
        holder.tvContDock.setText(info.getDockNumber());
        holder.tvContOperation.setText(info.getReason());
        if (Utilities.getMinute(info.getCheckingTime()) >= 5)
            convertView.setBackgroundColor(Color.parseColor("#FFFF66"));
        else if (Utilities.getMinute(info.getCheckingTime()) > 0)
            convertView.setBackgroundColor(Color.parseColor("#CCFFCC"));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_cont_number)
        TextView tvContNumber;
        @Bind(R.id.tv_cont_type)
        TextView tvContType;
        @Bind(R.id.tv_cont_customer_name)
        TextView tvContCusName;
        @Bind(R.id.tv_cont_operation)
        TextView tvContOperation;
        @Bind(R.id.tv_cont_dock)
        TextView tvContDock;
        @Bind(R.id.tv_cont_information)
        TextView tvContInformation;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
