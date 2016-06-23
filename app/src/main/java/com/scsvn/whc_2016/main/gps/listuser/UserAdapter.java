package com.scsvn.whc_2016.main.gps.listuser;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tranxuanloc on 4/19/2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<UserInfo> data;
    private List<UserInfo> dataFiltered;
    private SparseBooleanArray selectedItems;

    public UserAdapter(Context context, List<UserInfo> data) {
        this.context = context;
        this.data = data;
        dataFiltered = data;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserInfo info = dataFiltered.get(position);
        holder.tvUserName.setText(info.getVietnamName());
        holder.tvUserID.setText(info.getUserName());
        holder.tvLastDate.setText(info.getLastActivityDate());
        holder.tvLastAction.setText(info.getLastActionTime());
        if (info.getLastActivityDate().length() > 0)
            holder.tvLastDate.setVisibility(View.VISIBLE);
        else holder.tvLastDate.setVisibility(View.GONE);
        if (info.getLastActionTime().length() > 0)
            holder.tvLastAction.setVisibility(View.VISIBLE);
        else holder.tvLastAction.setVisibility(View.GONE);
        if (info.isOnline())
            holder.ivState.setImageResource(R.drawable.ic_online);
        else
            holder.ivState.setImageResource(R.drawable.ic_offline);
        if (selectedItems.get(position, false)) {
            holder.cvRoot.setCardBackgroundColor(Color.argb(255, 221, 221, 221));
        } else {
            holder.cvRoot.setCardBackgroundColor(Color.argb(255, 255, 255, 255));
        }
    }

    @Override
    public int getItemCount() {
        return dataFiltered.size();
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public UserInfo getItem(int position) {
        for (int i = 0; i < position; i++) {

        }
        return dataFiltered.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<UserInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        UserInfo info = data.get(i);
                        String name = Normalizer.normalize(info.getVietnamName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String id = Normalizer.normalize(info.getUserName().toLowerCase(), Normalizer.Form.NFD);
                        if (name.contains(keyword) || id.contains(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = data.size();
                    results.values = data;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataFiltered = (ArrayList<UserInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserID;
        TextView tvUserName;
        TextView tvLastAction;
        TextView tvLastDate;
        ImageView ivState;
        CardView cvRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.item_tv_user_name);
            tvUserID = (TextView) itemView.findViewById(R.id.item_tv_user_id);
            tvLastDate = (TextView) itemView.findViewById(R.id.item_tv_last_activity_date);
            tvLastAction = (TextView) itemView.findViewById(R.id.item_tv_last_action_time);
            ivState = (ImageView) itemView.findViewById(R.id.item_iv_state);
            cvRoot = (CardView) itemView.findViewById(R.id.item_cv_root);
        }
    }

}
