package com.scsvn.whc_2016.main.giaonhanhoso.cartonreturn;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 3/5/2016.
 */
public class ReceivingCartonReturnAdapter extends ArrayAdapter<DSReceivingCartonReturnListInfo> implements Filterable {
    private static final String TAG = "HomNayAdapter";
    private LayoutInflater inflater;
    private ChoiceListener choiceListener;
    private ArrayList<DSReceivingCartonReturnListInfo> dataRelease;
    private ArrayList<DSReceivingCartonReturnListInfo> dataOrigin;

    public ReceivingCartonReturnAdapter(Context context, ArrayList<DSReceivingCartonReturnListInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        choiceListener = (ChoiceListener) getContext();
        dataOrigin = objects;
        dataRelease = objects;
    }


    @Override
    public DSReceivingCartonReturnListInfo getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_receiving_carton_return, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        DSReceivingCartonReturnListInfo info = getItem(position);
        holder.tvCategory.setText(info.getCategoryDescription());
        holder.tvDes.setText(info.getCartonDescription());
        holder.tvID.setText(String.format(Locale.US, "%d", info.getCartonNewID()));
        holder.tvRef.setText(info.getCustomerRef());
        holder.tvSize.setText(NumberFormat.getInstance().format(info.getCartonSize()));
        holder.cb.setChecked(info.isChecked());
        holder.tvDes.setVisibility(info.getCartonDescription().length() > 0 ? View.VISIBLE : View.GONE);
        holder.tvRef.setVisibility(info.getCustomerRef().length() > 0 ? View.VISIBLE : View.GONE);
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem(position).setChecked(holder.cb.isChecked());
                choiceListener.OnChoiceListener(position);
            }
        });
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);

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
                    ArrayList<DSReceivingCartonReturnListInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        DSReceivingCartonReturnListInfo info = dataOrigin.get(i);
                        String des = Normalizer.normalize(info.getCartonDescription().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String category = Normalizer.normalize(info.getCategoryDescription().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String ref = Normalizer.normalize(info.getCustomerRef().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String id = Integer.toString(info.getCartonNewID());
                        String size = Float.toString(info.getCartonSize());
                        if (des.contains(keyword) || category.contains(keyword) || ref.contains(keyword) || id.contains(keyword) || size.contains(keyword))
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
                dataRelease = (ArrayList<DSReceivingCartonReturnListInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ChoiceListener {
        void OnChoiceListener(int position);
    }

    static class ViewHolder {
        @Bind(R.id.item_tv_carton_return_category)
        TextView tvCategory;
        @Bind(R.id.item_tv_carton_return_des)
        TextView tvDes;
        @Bind(R.id.item_tv_carton_return_id)
        TextView tvID;
        @Bind(R.id.item_tv_carton_return_ref)
        TextView tvRef;
        @Bind(R.id.item_tv_carton_return_size)
        TextView tvSize;
        @Bind(R.id.item_cb_carton_return)
        CheckBox cb;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
