package com.scsvn.whc_2016.main.detailphieu;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by tranxuanloc on 5/30/2016.
 */
public interface Item {
    int getViewType();

    View getItem(LayoutInflater inflater, View convertView);
}
