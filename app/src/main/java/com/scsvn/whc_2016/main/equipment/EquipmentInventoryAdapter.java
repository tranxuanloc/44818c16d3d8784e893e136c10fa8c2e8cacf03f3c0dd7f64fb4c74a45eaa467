package com.scsvn.whc_2016.main.equipment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.scsvn.whc_2016.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by buu on 13/05/2016.
 */
public class EquipmentInventoryAdapter extends ArrayAdapter<EquipmentInventoryInfo> {
    private LayoutInflater inflater;

    public EquipmentInventoryAdapter(Context context, List<EquipmentInventoryInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_equipment_inventory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        return convertView;
    }

    static class ViewHolder {

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
