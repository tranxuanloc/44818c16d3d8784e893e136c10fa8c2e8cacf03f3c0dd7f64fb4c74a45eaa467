package com.scsvn.whc_2016.main.postiamge;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;

/**
 * Created by xuanloc on 11/7/2016.
 */
public class ThumbImageAdapter extends ArrayAdapter<Thumb> {
    private final DisplayMetrics displayMetrics;
    private LayoutInflater inflater;
    private int widthContainer;

    public ThumbImageAdapter(Context context, ArrayList<Thumb> values) {
        super(context, 0, values);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        displayMetrics = context.getResources().getDisplayMetrics();
        widthContainer = displayMetrics.widthPixels - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 37, displayMetrics);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThumbViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_thumb_image, parent, false);
            holder = new ThumbViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ThumbViewHolder) convertView.getTag();
        }
        Thumb item = getItem(position);
        holder.thumbIV.setImageBitmap(item.getBitmap());

        return convertView;
    }

    private class ThumbViewHolder {
        ImageView thumbIV;

        public ThumbViewHolder(View view) {
            thumbIV = (ImageView) view.findViewById(R.id.item_thumb_iv);
            thumbIV.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, widthContainer / 2));
        }
    }
}
