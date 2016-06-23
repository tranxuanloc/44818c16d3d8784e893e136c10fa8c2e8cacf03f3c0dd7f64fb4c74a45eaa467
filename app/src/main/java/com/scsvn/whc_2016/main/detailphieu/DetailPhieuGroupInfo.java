package com.scsvn.whc_2016.main.detailphieu;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/18/2016.
 */
public class DetailPhieuGroupInfo implements Item {
    public int total;
    private String productName;
    private String productNumber;

    public DetailPhieuGroupInfo(String productName, String productNumber) {
        this.productName = productName;
        this.productNumber = productNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int getViewType() {
        return DetailPhieuAdapter.RowType.TYPE_HEADER.ordinal();
    }

    @Override
    public View getItem(LayoutInflater inflater, View convertView) {
        final GroupViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof GroupViewHolder)) {
            convertView = inflater.inflate(R.layout.items_detail_phieu_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (GroupViewHolder) convertView.getTag();
        holder.prodNumber.setText(getProductNumber());
        holder.prodName.setText(getProductName());
        holder.prodTotal.setText(String.valueOf(total));
        return convertView;
    }

    static class GroupViewHolder {
        @Bind(R.id.tv_prod_name)
        TextView prodName;
        @Bind(R.id.tv_prod_number)
        TextView prodNumber;
        @Bind(R.id.tv_prod_total)
        TextView prodTotal;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
