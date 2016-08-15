package com.scsvn.whc_2016.main.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by tranxuanloc on 8/11/2016.
 */
public class InviteUserAdapter extends ArrayAdapter<EmployeeInfo> {
    public InviteUserAdapter(Context context, ArrayList<EmployeeInfo> objects) {
        super(context, R.layout.item_invitees_user, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_invitees_user, parent, false);
            holder.name = (TextView) convertView;
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        EmployeeInfo item = getItem(position);
        holder.name.setText(String.format(Locale.getDefault(), "%d %s", item.getEmployeeID(), item.getEmployeeName()));
        return convertView;
    }

    private class ViewHolder {
        TextView name;
    }
}
