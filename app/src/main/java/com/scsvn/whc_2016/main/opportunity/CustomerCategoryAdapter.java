package com.scsvn.whc_2016.main.opportunity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;

/**
 * Created by xuanloc on 9/27/2016.
 */
public class CustomerCategoryAdapter extends ArrayAdapter<OpportunityCustomerCategory> implements Filterable {

    private final int viewPadding;
    private ArrayList<OpportunityCustomerCategory> dataRelease;
    private ArrayList<OpportunityCustomerCategory> dataOrigin;

    public CustomerCategoryAdapter(Context context, ArrayList<OpportunityCustomerCategory> objects) {
        super(context, 0, objects);
        dataOrigin = objects;
        dataRelease = objects;
        viewPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getContext().getResources().getDisplayMetrics());
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public OpportunityCustomerCategory getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        OpportunityCustomerCategory item = getItem(position);
        if (convertView == null) {
            view = new TextView(getContext());
            view.setPadding(viewPadding, viewPadding, viewPadding, viewPadding);
        } else view = (TextView) convertView;
        view.setText(item.getDescription());
        if (position % 2 == 0)
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else
            view.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<OpportunityCustomerCategory> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        OpportunityCustomerCategory info = dataOrigin.get(i);
                        if (info.getDescription().toLowerCase().contains(constraint))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = dataOrigin.size();
                    results.values = dataOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataRelease = (ArrayList<OpportunityCustomerCategory>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
