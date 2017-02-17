package com.scsvn.whc_2016.main.detailphieu.chuphinh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class AttachmentInfoAdapter extends ArrayAdapter<AttachmentInfo> {
    private LayoutInflater inflater;

    public AttachmentInfoAdapter(Context context, List<AttachmentInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_attachment_info, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        AttachmentInfo info = getItem(position);
        holder.tvFileName.setText(info.getOriginalFileName());
        holder.tvFileSize.setText(String.format(Locale.getDefault(), "Size: %dkb", info.getAttachmentFileSize()));
        holder.tvFileCreateTime.setText(Utilities.formatDate_ddMMyyyyHHmm(info.getAttachmentDate()));
        holder.tvFileDescription.setText(info.getAttachmentDescription());
        holder.tvFileCreateUser.setText(String.format("User: %s", info.getAttachmentUser()));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_file_name)
        TextView tvFileName;
        @Bind(R.id.tv_file_size)
        TextView tvFileSize;
        @Bind(R.id.tv_file_last_modifier)
        TextView tvFileCreateTime;
        @Bind(R.id.tv_file_description)
        TextView tvFileDescription;
        @Bind(R.id.tv_file_user)
        TextView tvFileCreateUser;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
