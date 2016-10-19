package com.scsvn.whc_2016.main.detailphieu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/3/2016.
 */
public class OrderDetail implements Item {
    @SerializedName("BarcodeScanDetailID")
    public int BarcodeScanDetailID;
    @SerializedName("DO")
    private String DO;
    @SerializedName("SpecialRequirement")
    private String SpecialRequirement;
    @SerializedName("PalletID")
    private String PalletID;
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("Result")
    private String Result;
    @SerializedName("QuantityOfPackages")
    private String QuantityOfPackages;
    @SerializedName("IsRecordNew")
    private byte IsRecordNew;
    @SerializedName("DispatchingOrderDetailID")
    private int dispatchingOrderDetailID;
    @SerializedName("DispatchingLocationRemark")
    private String dispatchingLocationRemark;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("UseByDate")
    private String useByDate;
    @SerializedName("ProductionDate")
    private String productionDate;
    @SerializedName("Label")
    private String Label;
    @SerializedName("ScannedType")
    private String ScannedType;
    @SerializedName("RemainByProductAtLocation")
    private int RemainByProductAtLocation;

    public String getSpecialRequirement() {
        return SpecialRequirement;
    }

    public String getScannedType() {
        return ScannedType;
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyy(productionDate);
    }

    public String getLabel() {
        return Label;
    }

    public String getUseByDate() {
        return Utilities.formatDate_ddMMyy(useByDate);
    }

    public String getDispatchingLocationRemark() {
        return dispatchingLocationRemark;
    }


    public String getRemark() {
        return remark;
    }


    public int getDispatchingOrderDetailID() {
        return dispatchingOrderDetailID;
    }


    public String getPalletID() {
        return PalletID;
    }


    public String getProductNumber() {
        return ProductNumber;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getResult() {
        return Result;
    }

    public String getQuantityOfPackages() {
        return QuantityOfPackages;
    }

    public byte getIsRecordNew() {
        return IsRecordNew;
    }

    public String getDO() {
        return DO;
    }

    public int getBarcodeScanDetailID() {
        return BarcodeScanDetailID;
    }

    public int getRemainByProductAtLocation() {
        return RemainByProductAtLocation;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "PalletID: %s\nProductNumber: %s\nProductName: %s\nResult: %s\nQuantityOfPackages: %s\nDispatchingOrderDetailID: %d\nRemark: %s\nDispatchingLocationRemark: %s\nProductionDate: %s\nUseByDate: %s\nLabel: %s\nScannedType: %s\nDO: %s\nBarcodeScanDetailID: %d\nRemainByProductAtLocation: %d\nSpecialRequirement: %s", getPalletID(), getProductNumber(), getProductName(), getResult(), getQuantityOfPackages(), getDispatchingOrderDetailID(), getRemark(), getDispatchingLocationRemark(), getProductionDate(), getUseByDate(), getLabel(), getScannedType(), getDO(), getBarcodeScanDetailID(), getRemainByProductAtLocation(), getSpecialRequirement());
    }

    @Override
    public View getItem(Context context, LayoutInflater inflater, View convertView) {
        ChildViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof ChildViewHolder)) {
            convertView = inflater.inflate(R.layout.item_detail_phieu, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ChildViewHolder) convertView.getTag();
        holder.palletId.setText(getPalletID());
        holder.remark.setText(getRemark());
        holder.location.setText(getDispatchingLocationRemark());
        holder.quantity.setText(getQuantityOfPackages());
        holder.result.setText(getResult());
        holder.tvScannedType.setText(getScannedType());
        holder.tvRemainByProductAtLocation.setText(String.valueOf(RemainByProductAtLocation));
        holder.tvRemainByProductAtLocation.setVisibility(RemainByProductAtLocation != 0 ? View.VISIBLE : View.GONE);
        holder.tvHsd.setText(String.format("%s\n%s", getProductionDate(), getUseByDate()));
        holder.tvLabel.setText(getLabel().trim());
        if (getResult().equalsIgnoreCase("OK")) {
            convertView.setBackgroundColor(Color.argb(255, 76, 175, 80));
            if (getIsRecordNew() == 1)
                convertView.setBackgroundColor(Color.argb(255, 255, 238, 88));
        } else if (getResult().equalsIgnoreCase("NO"))
            convertView.setBackgroundColor(Color.argb(255, 183, 28, 28));
        else if (getResult().equalsIgnoreCase("XX"))
            convertView.setBackgroundColor(Color.argb(255, 144, 202, 249));
        else convertView.setBackgroundColor(Color.argb(0, 76, 175, 80));
        return convertView;
    }

    static class ChildViewHolder {
        @Bind(R.id.tv_PalletID)
        TextView palletId;
        @Bind(R.id.tv_remark)
        TextView remark;
        @Bind(R.id.tv_location)
        TextView location;
        @Bind(R.id.tv_Quantity)
        TextView quantity;
        @Bind(R.id.tv_Result)
        TextView result;
        @Bind(R.id.tv_nsd)
        TextView tvHsd;
        @Bind(R.id.tv_label)
        TextView tvLabel;
        @Bind(R.id.tv_scanned_type)
        TextView tvScannedType;
        @Bind(R.id.tv_RemainByProductAtLocation)
        TextView tvRemainByProductAtLocation;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
