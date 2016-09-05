package com.scsvn.whc_2016.main.mms.equipment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.Normalizer;
import java.util.ArrayList;

/**
 * Created by tranxuanloc on 8/19/2016.
 */
public class EquipmentAdapter extends ArrayAdapter<Equipment> implements Filterable {
    private ArrayList<Equipment> origin;
    private ArrayList<Equipment> release;

    public EquipmentAdapter(Context context, ArrayList<Equipment> objects) {
        super(context, R.layout.item_equipment, objects);
        origin = objects;
        release = (ArrayList<Equipment>) objects.clone();
    }

    @Override
    public int getCount() {
        return release.size();
    }

    @Override
    public Equipment getItem(int position) {
        return release.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_equipment, parent, false);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.equipment_id);
            holder.name = (TextView) convertView.findViewById(R.id.equipment_name);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        Equipment item = getItem(position);
        holder.id.setText(item.getId());
        holder.name.setText(item.getName());
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() != 0) {
                    ArrayList<Equipment> temp = new ArrayList<>();
                    for (Equipment item : origin) {
                        String name = Normalizer.normalize(item.getName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String id = item.getId().toLowerCase();
                        if (name.contains(constraint)
                                || id.contains(constraint))
                            temp.add(item);
                    }
                    results.values = temp;
                    results.count = temp.size();
                } else {
                    results.values = origin;
                    results.count = origin.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                release = (ArrayList<Equipment>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        TextView id;
        TextView name;
    }

}
