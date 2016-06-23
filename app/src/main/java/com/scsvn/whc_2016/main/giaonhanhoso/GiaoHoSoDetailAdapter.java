package com.scsvn.whc_2016.main.giaonhanhoso;

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
 * Created by tranxuanloc on 3/5/2016.
 */
public class GiaoHoSoDetailAdapter extends ArrayAdapter<DSDispatchingOrderDetailsInfo> {
    private static final String TAG = GiaoHoSoDetailAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    public GiaoHoSoDetailAdapter(Context context, ArrayList<DSDispatchingOrderDetailsInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_detail_giao_ho_so, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        DSDispatchingOrderDetailsInfo info = getItem(position);
        holder.tvGhsDtCartonId.setText(String.format("%d", info.getCartonNewID()));
        holder.tvGhsDtCustomerReference.setText(info.getCustomerRef());
        holder.tvGhsDtDesctiption.setText(info.getCartonDescription());
        holder.tvGhsDtVolume.setText(String.format("%.2f", info.getCartonSize()));
        holder.tvGhsDtResult.setText(info.getResult());
        if (info.getResult().equals("OK")) {
            convertView.setBackgroundColor(Color.argb(255, 0x4c, 0xaf, 0x50));
            if (info.isRecordNew() == 1)
                convertView.setBackgroundColor(Color.argb(255, 0xff, 0xee, 0x58));
        } else if (info.getResult().equalsIgnoreCase("NO"))
            convertView.setBackgroundColor(Color.argb(0b11111111, 0xb7, 0x1c, 0x1c));
        else if (info.getResult().equalsIgnoreCase("XX"))
            convertView.setBackgroundColor(Color.argb(0b11111111, 0b10010000, 0b11001010, 0b11111001));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.tvGhsDtCartonId)
        TextView tvGhsDtCartonId;
        @Bind(R.id.tvGhsDtCustomerReference)
        TextView tvGhsDtCustomerReference;
        @Bind(R.id.tvGhsDtDesctiption)
        TextView tvGhsDtDesctiption;
        @Bind(R.id.tvGhsDtVolume)
        TextView tvGhsDtVolume;
        @Bind(R.id.tvGhsDtResult)
        TextView tvGhsDtResult;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
