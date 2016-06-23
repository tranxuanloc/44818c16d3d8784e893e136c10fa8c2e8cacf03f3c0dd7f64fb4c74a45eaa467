package com.scsvn.whc_2016.main.containerandtruckinfor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.List;
import java.util.Locale;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class ContainerAndTruckAdapter extends ArrayAdapter<ContainerAndTruckInfo> {
    private LayoutInflater inflater;

    public ContainerAndTruckAdapter(Context context, List<ContainerAndTruckInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_container_truck, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final ContainerAndTruckInfo info = getItem(position);
        String phoneText = String.format(Locale.US, "SĐT: 0%d", info.DriverMobilePhone);
        SpannableString spanPhone = new SpannableString(phoneText);
        spanPhone.setSpan(new UnderlineSpan(), 5, phoneText.length(), 0);
        holder.tvNum.setText(info.ContainerNum);
        holder.tvCusName.setText(info.CustomerName);
        holder.tvCusNum.setText(info.CustomerNumber);
        holder.tvProgress.setText(String.format(Locale.US, "%d%%", info.TaskProgress));
        holder.tvTimeIn.setText(Utilities.formatDate_HHmm(info.TimeIn));
        holder.tvDefaultTime.setText(Utilities.formatDate_HHmm(info.DefaultProcessTime));
        String expect = Utilities.formatDate_HHmm(info.ExpectedProcessTime);
        if (expect.length() > 0) {
            holder.tvExpectTime.setText(expect);
            holder.tvExpectTime.setVisibility(View.VISIBLE);
        }
        holder.tvExpectTime.setVisibility(View.GONE);

        if (info.CustomerRequirement.length() > 0) {
            holder.tvRequirement.setText(String.format("Note: %s", info.CustomerRequirement));
            holder.tvRequirement.setVisibility(View.VISIBLE);
        } else
            holder.tvRequirement.setVisibility(View.GONE);
        if (info.DriverMobilePhone != 0) {
            holder.tvPhone.setText(spanPhone);
            holder.tvPhone.setVisibility(View.VISIBLE);
        } else holder.tvPhone.setVisibility(View.GONE);

        if (info.Reason.equals("N"))
            holder.tvType.setBackgroundColor(Color.argb(0xFF, 0x00, 0xC8, 0x53));
        else if (info.Reason.equals("X"))
            holder.tvType.setBackgroundColor(Color.argb(0xFF, 0xF1, 0xAD, 0x00));
        else
            holder.tvType.setBackgroundColor(Color.argb(0xFF, 0x3F, 0x51, 0xB5));
        holder.tvType.setText(String.format("%s %s", info.ContainerType, info.Reason));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorAlternativeRow));
        else
            convertView.setBackgroundColor(Color.WHITE);
        holder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext()
                        , android.R.layout.simple_list_item_1
                        , new String[]{"Call", "Send Message"});
                final String phone = String.format(Locale.US, "0%d", info.DriverMobilePhone);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 1) {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("smsto:" + phone));
                                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                        getContext().startActivity(Intent.createChooser(intent, "Send via..."));
                                    }
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + phone));
                                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                        getContext().startActivity(Intent.createChooser(intent, "Call via..."));
                                    }
                                }
                                dialog.dismiss();
                            }
                        })
                        .setTitle(phone)
                        .create();
                dialog.show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvNum;
        TextView tvCusName;
        TextView tvCusNum;
        TextView tvProgress;
        TextView tvTimeIn;
        TextView tvRequirement;
        TextView tvPhone;
        TextView tvType;
        TextView tvExpectTime;
        TextView tvDefaultTime;

        public ViewHolder(View view) {
            tvNum = (TextView) view.findViewById(R.id.tv_cont_num);
            tvCusName = (TextView) view.findViewById(R.id.tv_cont_customer_name);
            tvCusNum = (TextView) view.findViewById(R.id.tv_cont_number);
            tvProgress = (TextView) view.findViewById(R.id.tv_cont_progress);
            tvTimeIn = (TextView) view.findViewById(R.id.tv_cont_time_in);
            tvRequirement = (TextView) view.findViewById(R.id.tv_cont_requirement);
            tvPhone = (TextView) view.findViewById(R.id.tv_cont_phone);
            tvType = (TextView) view.findViewById(R.id.tv_cont_type);
            tvExpectTime = (TextView) view.findViewById(R.id.tv_cont_expect_time);
            tvDefaultTime = (TextView) view.findViewById(R.id.tv_cont_default_time);
        }
    }

}
