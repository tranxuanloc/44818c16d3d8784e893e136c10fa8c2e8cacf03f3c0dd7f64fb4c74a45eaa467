package com.scsvn.whc_2016.main.nhaphoso;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReceivingOrderDetailsAdapter extends ArrayAdapter<ReceivingOrderDetailsInfo> {
    private LayoutInflater inflater;

    public ReceivingOrderDetailsAdapter(Context context, List<ReceivingOrderDetailsInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_receiving_order_details, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        ReceivingOrderDetailsInfo info = getItem(position);
        String SPalletID = String.format("%d", info.getPalletID());
        String SCustomerRef = info.getCustomerRef();
        String SCartonDescription = info.getProductName();
        final String SLocationNumber = info.getLocationNumber();
        String SCurrentQuantity = String.format("%.2f", info.getCurrentQuantity());
        String SRO = info.getRO();
        String SCustomerNumber = info.getCustomerNumber();
        String SProductNumber = info.getProductNumber();
        String SScannedTime = info.getScannedTime();
        int SRecordFirst = info.getRecordStatus();
        String SUser = info.getScannedUser();
        int SStatus = info.getStatus();
        int SLocationID = info.getLocationID();
        boolean recordFirst = info.isRecordFirst();

        holder.txtSPalletID.setText(SPalletID);
        holder.txtSCustomerRef.setText(SCustomerRef);
        holder.txtSCartonSize.setText(SCurrentQuantity);
        holder.txtSCartonDescription.setText(SCartonDescription);
        holder.txtSCustomerNumber.setText(SCustomerNumber);
        holder.txtSProductNumber.setText(SProductNumber);
        holder.txtUser.setText(String.format("%s %s", SUser, SScannedTime));

        SpannableString SPSLocationNumber = new SpannableString(SLocationNumber);
        SPSLocationNumber.setSpan(new UnderlineSpan(), 0, SLocationNumber.length(), 0);
        holder.txtSLocationNumber.setText(SPSLocationNumber);

        SpannableString SPSRO = new SpannableString(SRO);
        SPSRO.setSpan(new UnderlineSpan(), 0, SRO.length(), 0);
        holder.txtSRO.setText(SPSRO);
        if (SStatus == 1 && !(SLocationID == 1)) {
            convertView.setBackgroundColor(Color.parseColor("#CCFFCC"));
        } else {
            if (recordFirst)
                convertView.setBackgroundColor(Color.parseColor("#FFFF00"));
            else {
                convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }


        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_CartonNewID)
        TextView txtSPalletID;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_CrowRefID)
        TextView txtSCustomerRef;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_Size)
        TextView txtSCartonSize;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_CartonDescription)
        TextView txtSCartonDescription;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_LocationNumber)
        TextView txtSLocationNumber;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_CustomerNumber)
        TextView txtSCustomerNumber;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_ProductNumber)
        TextView txtSProductNumber;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_ReceivingOrderNumber)
        TextView txtSRO;
        @Bind(R.id.txt_Items_ScanDSReceivingOrders_UserUpdate)
        TextView txtUser;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
//        holder.txtSLocationNumber.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Adasd
//                Intent mintentPlt = new Intent(getContext(), LocationChecking.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mintentPlt.putExtra("Intent_LocationID", SLocationNumber);
//                context.startActivity(mintentPlt);
//            }
//        });