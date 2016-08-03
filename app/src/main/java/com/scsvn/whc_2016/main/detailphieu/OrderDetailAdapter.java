package com.scsvn.whc_2016.main.detailphieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Trần Xuân Lộc on 1/3/2016.
 */
public class OrderDetailAdapter extends ArrayAdapter<Item> {

    public OrderDetailAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Item info = getItem(position);
        return info.getItem(getContext(), inflater, convertView);
    }

}
