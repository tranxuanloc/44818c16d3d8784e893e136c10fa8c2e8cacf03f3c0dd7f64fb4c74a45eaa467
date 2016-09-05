package com.scsvn.whc_2016.main.mms.part;

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
import java.util.Locale;

/**
 * Created by tranxuanloc on 8/19/2016.
 */
public class PartRemainAdapter extends ArrayAdapter<PartRemain> implements Filterable {
    private ArrayList<PartRemain> origin;
    private ArrayList<PartRemain> release;

    public PartRemainAdapter(Context context, ArrayList<PartRemain> objects) {
        super(context, R.layout.item_part_remain_list_view, objects);
        origin = objects;
        release = (ArrayList<PartRemain>) objects.clone();
    }

    @Override
    public int getCount() {
        return release.size();
    }

    @Override
    public PartRemain getItem(int position) {
        return release.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_part_remain_list_view, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_part_remain_name);
            holder.des = (TextView) convertView.findViewById(R.id.item_part_remain_description);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        PartRemain item = getItem(position);
        holder.des.setText(String.format(Locale.getDefault(), "%s - In: %d    Out: %d    Remain: %d", item.getOriginal(), item.getIn(), item.getOut(), item.getRemain()));
        holder.name.setText(String.format(Locale.getDefault(), "%d ~ %s", item.getId(), item.getName()));
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
                    ArrayList<PartRemain> temp = new ArrayList<>();
                    for (PartRemain item : origin) {
                        String name = Normalizer.normalize(item.getName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String id = item.getOriginal().toLowerCase();
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
                release = (ArrayList<PartRemain>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        TextView des;
        TextView name;
    }

}
