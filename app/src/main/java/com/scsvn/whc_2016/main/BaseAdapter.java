package com.scsvn.whc_2016.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.kiemcontainer.ContainerInfo;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class BaseAdapter extends ArrayAdapter<ContainerInfo> {
    private LayoutInflater inflater;

    public BaseAdapter(Context context, List<ContainerInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_login, parent, false);
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