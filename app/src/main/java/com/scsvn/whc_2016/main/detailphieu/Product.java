package com.scsvn.whc_2016.main.detailphieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 8/2/2016.
 */
public class Product implements Item {
    private String title;
    private int total;

    public Product(String title, int total) {
        this.title = title;
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public View getItem(Context context, LayoutInflater inflater, View convertView) {
        final GroupViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof GroupViewHolder)) {
            convertView = inflater.inflate(R.layout.items_detail_phieu_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (GroupViewHolder) convertView.getTag();

        holder.prodName.setText(getTitle());
        holder.prodTotal.setText(String.valueOf(total));
        return convertView;
    }

    static class GroupViewHolder {
        @Bind(R.id.tv_prod_name)
        TextView prodName;
        @Bind(R.id.tv_prod_total)
        TextView prodTotal;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
