package com.scsvn.whc_2016.main.giaonhanhoso;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.Normalizer;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 3/5/2016.
 */
public class GiaoHoSoAdapter extends ArrayAdapter<DSDispatchingOrdersInfo> implements Filterable {
    private static final String TAG = "HomNayAdapter";
    private LayoutInflater inflater;
    private ArrayList<DSDispatchingOrdersInfo> dataRelease;
    private ArrayList<DSDispatchingOrdersInfo> dataOrigin;

    public GiaoHoSoAdapter(Context context, ArrayList<DSDispatchingOrdersInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public DSDispatchingOrdersInfo getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_giao_hs, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        DSDispatchingOrdersInfo info = getItem(position);
        holder.tvGhsCustomerNameVsNumber.setText(String.format("%s  -  %s", info.getCustomerName(), info.getCustomerNumber()));
        holder.tvGhsOrderNumber.setText(info.getDispatchingOrderNumber());
        holder.tvGhsDate.setText(info.getDispatchingOrderDate());
        holder.tvGhsTotalCarton.setText(String.format("Carton: %d", info.getTotalCarton()));
        holder.tvGhsTotalVolume.setText(String.format("Volume: %.2f", info.getTotalVolume()));
        holder.tvGhsRemark.setText(info.getRemark());

        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        if (info.isOrderStatus())
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorD68f00));

        return convertView;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<DSDispatchingOrdersInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        DSDispatchingOrdersInfo info = dataOrigin.get(i);
                        String customerName = Normalizer.normalize(info.getCustomerName().toLowerCase(), Normalizer.Form.NFD);
                        String customerNumber = Normalizer.normalize(info.getCustomerNumber().toLowerCase(), Normalizer.Form.NFD).replace("-", "");
                        String orderNumber = Normalizer.normalize(info.getDispatchingOrderNumber().toLowerCase(), Normalizer.Form.NFD).replace("-", "");
                        if (orderNumber.contains(keyword) || customerName.contains(keyword) || customerNumber.contains(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = dataOrigin.size();
                    results.values = dataOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataRelease = (ArrayList<DSDispatchingOrdersInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @Bind(R.id.tvGhsCustomerNameVsNumber)
        TextView tvGhsCustomerNameVsNumber;
        @Bind(R.id.tvGhsOrderNumber)
        TextView tvGhsOrderNumber;
        @Bind(R.id.tvGhsDate)
        TextView tvGhsDate;
        @Bind(R.id.tvGhsTotalCarton)
        TextView tvGhsTotalCarton;
        @Bind(R.id.tvGhsTotalVolume)
        TextView tvGhsTotalVolume;
        @Bind(R.id.tvGhsRemark)
        TextView tvGhsRemark;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
