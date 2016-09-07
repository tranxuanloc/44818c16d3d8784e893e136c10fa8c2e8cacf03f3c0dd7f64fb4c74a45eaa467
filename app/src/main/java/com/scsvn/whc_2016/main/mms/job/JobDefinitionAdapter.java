package com.scsvn.whc_2016.main.mms.job;

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
public class JobDefinitionAdapter extends ArrayAdapter<JobDefinition> implements Filterable {
    private ArrayList<JobDefinition> origin;
    private ArrayList<JobDefinition> release;

    public JobDefinitionAdapter(Context context, ArrayList<JobDefinition> objects) {
        super(context, 0, objects);
        origin = objects;
        release = (ArrayList<JobDefinition>) objects.clone();
    }

    @Override
    public int getCount() {
        return release.size();
    }

    @Override
    public JobDefinition getItem(int position) {
        return release.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_job_definition_listview, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_job_definition_name);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        JobDefinition item = getItem(position);
        holder.name.setText(String.format(Locale.getDefault(), "%s ~ %s", item.getId(), item.getName()));
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
                    ArrayList<JobDefinition> temp = new ArrayList<>();
                    for (JobDefinition item : origin) {
                        String nameVN = Normalizer.normalize(item.getVietnameseName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String name = item.getName().toLowerCase();
                        String id = item.getId().toLowerCase();
                        if (name.contains(constraint)
                                || id.contains(constraint) || nameVN.contains(constraint))
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
                release = (ArrayList<JobDefinition>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        TextView name;
    }

}
