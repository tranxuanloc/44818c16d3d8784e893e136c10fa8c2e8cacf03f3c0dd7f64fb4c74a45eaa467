package com.scsvn.whc_2016.main.technical.assign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class CommentAdapter extends ArrayAdapter<CommentInfo> {
    private LayoutInflater inflater;

    public CommentAdapter(Context context, List<CommentInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_qhse_comment, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        CommentInfo info = getItem(position);
        holder.tvBy.setText(info.getCommentBy());
        holder.tvComment.setText(info.getComment());
        holder.tvTime.setText(info.getCommentTime());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_qhse_comment_by)
        TextView tvBy;
        @Bind(R.id.item_tv_qhse_comment_comment)
        TextView tvComment;
        @Bind(R.id.item_tv_qhse_comment_time)
        TextView tvTime;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}