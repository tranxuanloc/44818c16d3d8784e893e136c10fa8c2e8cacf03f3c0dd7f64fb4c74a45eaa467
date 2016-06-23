package com.scsvn.whc_2016.main.detailphieu.so_do_day;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.scsvn.whc_2016.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by tranxuanloc on 5/4/2016.
 */
public class SoDoDayAdapter extends RecyclerView.Adapter<SoDoDayAdapter.SDDViewHolder> {

    private static final String TAG = SoDoDayAdapter.class.getSimpleName();
    private List<LoadingReportInfo> data;
    private int totalRow, totalCarton, tempTotalCarton;
    private OnEditorActionListener onEditorActionListener;
    private int currentPositionFocus = -1;

    public SoDoDayAdapter(Context context, List<LoadingReportInfo> data, int totalCarton) {
        this.data = data;
        this.totalCarton = totalCarton;
        this.tempTotalCarton = totalCarton;
        totalRow = data.size() + 2;
        onEditorActionListener = (OnEditorActionListener) context;

    }

    @Override
    public SDDViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_so_do_day, parent, false);
        return new SDDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SDDViewHolder holder, int position) {
        final int iRow = getRowIndexByPosition(totalRow, position);
        holder.et.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        holder.et.setGravity(Gravity.CENTER);
        holder.et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    onEditorActionListener.onEditorAction(totalRow, holder.getAdapterPosition());
                }
                return false;
            }
        });
        holder.et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    currentPositionFocus = holder.getAdapterPosition();
                else currentPositionFocus = -1;
            }
        });
        holder.et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currentPositionFocus != -1 && currentPositionFocus == holder.getAdapterPosition() && iRow > 0 && iRow < totalRow - 1) {
                    LoadingReportInfo info = data.get(iRow - 1);
                    info.setChanged(true);
                    int amountCarton;
                    if (s.length() > 0)
                        amountCarton = Integer.parseInt(s.toString());
                    else amountCarton = 0;
                    switch ((holder.getAdapterPosition() - iRow) / totalRow - 4) {
                        case -4:
                            info.setSorting(amountCarton);
                            break;
                        case 1:
                            info.setD1(amountCarton);
                            break;
                        case 2:
                            info.setD2(amountCarton);
                            break;
                        case 3:
                            info.setD3(amountCarton);
                            break;
                        case 4:
                            info.setD4(amountCarton);
                            break;
                        case 5:
                            info.setD5(amountCarton);
                            break;
                        case 6:
                            info.setD6(amountCarton);
                            break;
                        case 7:
                            info.setD7(amountCarton);
                            break;
                        case 8:
                            info.setD8(amountCarton);
                            break;
                        case 9:
                            info.setD9(amountCarton);
                            break;
                        case 10:
                            info.setD10(amountCarton);
                            break;
                        case 11:
                            info.setD11(amountCarton);
                            break;
                        case 12:
                            info.setD12(amountCarton);
                            break;
                        case 13:
                            info.setD13(amountCarton);
                            break;
                        case 14:
                            info.setD14(amountCarton);
                            break;
                        case 15:
                            info.setD15(amountCarton);
                            break;
                        case 16:
                            info.setD16(amountCarton);
                            break;
                        case 17:
                            info.setD17(amountCarton);
                            break;
                        case 18:
                            info.setD18(amountCarton);
                            break;
                        case 19:
                            info.setD19(amountCarton);
                            break;
                        case 20:
                            info.setD20(amountCarton);
                            break;
                        case 21:
                            info.setD21(amountCarton);
                            break;
                        case 22:
                            info.setD22(amountCarton);
                            break;
                        case 23:
                            info.setD23(amountCarton);
                            break;
                        case 24:
                            info.setD24(amountCarton);
                            break;
                        case 25:
                            info.setD25(amountCarton);
                            break;
                        case 26:
                            info.setD26(amountCarton);
                            break;
                        case 27:
                            info.setD27(amountCarton);
                            break;
                        case 28:
                            info.setD28(amountCarton);
                            break;
                        case 29:
                            info.setD29(amountCarton);
                            break;
                        case 30:
                            info.setD30(amountCarton);
                            break;
                        case 31:
                            info.setD31(amountCarton);
                            break;
                        case 32:
                            info.setD32(amountCarton);
                            break;
                        case 33:
                            info.setD33(amountCarton);
                            break;
                        case 34:
                            info.setD34(amountCarton);
                            break;
                        case 35:
                            info.setD35(amountCarton);
                            break;
                        case 36:
                            info.setD36(amountCarton);
                            break;
                        case 37:
                            info.setD37(amountCarton);
                            break;
                        case 38:
                            info.setD38(amountCarton);
                            break;
                        case 39:
                            info.setD39(amountCarton);
                            break;
                        case 40:
                            info.setD40(amountCarton);
                            break;
                        case 41:
                            info.setD41(amountCarton);
                            break;
                        case 42:
                            info.setD42(amountCarton);
                            break;
                        case 43:
                            info.setD43(amountCarton);
                            break;
                        case 44:
                            info.setD44(amountCarton);
                            break;
                        case 45:
                            info.setD45(amountCarton);
                            break;
                    }
                }

            }
        });
        if (position % totalRow == 0) {
            holder.et.setEnabled(false);
            holder.et.setTextColor(Color.argb(255, 170, 0, 0));
            holder.et.setTypeface(null, Typeface.BOLD);
            if (position == 0)
                holder.et.setText(R.string.stt);
            else if (position == totalRow)
                holder.et.setText(R.string.id);
            else if (position == totalRow * 2) {
                holder.et.setText(R.string.name);
            } else if (position == totalRow * 3)
                holder.et.setText(R.string.reference);
            else if (position == totalRow * 4)
                holder.et.setText(R.string.qty);
            else
                holder.et.setText(String.format(Locale.US, "%d", position / totalRow - 4));
        } else if (iRow < totalRow - 1) {
            final LoadingReportInfo info = data.get(iRow - 1);

            holder.et.setTextColor(Color.argb(255, 0, 0, 0));
            holder.et.setTypeface(null, Typeface.NORMAL);
            if ((position - iRow) == 0) {
                holder.et.setEnabled(true);
                holder.et.setGravity(Gravity.NO_GRAVITY);
                holder.et.setText(String.format(Locale.US, "%d", info.getSorting()));
            } else if ((position - iRow) / totalRow == 1) {
                holder.et.setEnabled(false);
                holder.et.setText(info.getProductNumber());
            } else if ((position - iRow) / totalRow == 2) {
                holder.et.setEnabled(false);
                holder.et.setText(info.getProductName());
            } else if ((position - iRow) / totalRow == 3) {
                holder.et.setEnabled(false);
                holder.et.setText(info.getRemark());
            } else if ((position - iRow) / totalRow == 4) {
                holder.et.setEnabled(false);
                holder.et.setText(String.format(Locale.US, "%d", info.getQuantity()));
            } else {
                holder.et.setEnabled(true);
                holder.et.setGravity(Gravity.NO_GRAVITY);
                switch ((position - iRow) / totalRow - 4) {
                    case 1:
                        if (info.getD1() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD1()));
                        break;
                    case 2:
                        if (info.getD2() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD2()));
                        break;
                    case 3:
                        if (info.getD3() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD3()));
                        break;
                    case 4:
                        if (info.getD4() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD4()));
                        break;
                    case 5:
                        if (info.getD5() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD5()));
                        break;
                    case 6:
                        if (info.getD6() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD6()));
                        break;
                    case 7:
                        if (info.getD7() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD7()));
                        break;
                    case 8:
                        if (info.getD8() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD8()));
                        break;
                    case 9:
                        if (info.getD9() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD9()));
                        break;
                    case 10:
                        if (info.getD10() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD10()));
                        break;
                    case 11:
                        if (info.getD11() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD11()));
                        break;
                    case 12:
                        if (info.getD12() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD12()));
                        break;
                    case 13:
                        if (info.getD13() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD13()));
                        break;
                    case 14:
                        if (info.getD14() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD14()));
                        break;
                    case 15:
                        if (info.getD15() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD15()));
                        break;
                    case 16:
                        if (info.getD16() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD16()));
                        break;
                    case 17:
                        if (info.getD17() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD17()));
                        break;
                    case 18:
                        if (info.getD18() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD18()));
                        break;
                    case 19:
                        if (info.getD19() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD19()));
                        break;
                    case 20:
                        if (info.getD20() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD20()));
                        break;
                    case 21:
                        if (info.getD21() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD21()));
                        break;
                    case 22:
                        if (info.getD22() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD22()));
                        break;
                    case 23:
                        if (info.getD23() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD23()));
                        break;
                    case 24:
                        if (info.getD24() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD24()));
                        break;
                    case 25:
                        if (info.getD25() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD25()));
                        break;
                    case 26:
                        if (info.getD26() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD26()));
                        break;
                    case 27:
                        if (info.getD27() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD27()));
                        break;
                    case 28:
                        if (info.getD28() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD28()));
                        break;
                    case 29:
                        if (info.getD29() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD29()));
                        break;
                    case 30:
                        if (info.getD30() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD30()));
                        break;
                    case 31:
                        if (info.getD31() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD31()));
                        break;
                    case 32:
                        if (info.getD32() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD32()));
                        break;
                    case 33:
                        if (info.getD33() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD33()));
                        break;
                    case 34:
                        if (info.getD34() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD34()));
                        break;
                    case 35:
                        if (info.getD35() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD35()));
                        break;
                    case 36:
                        if (info.getD36() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD36()));
                        break;
                    case 37:
                        if (info.getD37() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD37()));
                        break;
                    case 38:
                        if (info.getD38() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD38()));
                        break;
                    case 39:
                        if (info.getD39() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD39()));
                        break;
                    case 40:
                        if (info.getD40() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD40()));
                        break;
                    case 41:
                        if (info.getD41() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD41()));
                        break;
                    case 42:
                        if (info.getD42() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD42()));
                        break;
                    case 43:
                        if (info.getD43() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD43()));
                        break;
                    case 44:
                        if (info.getD44() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD44()));
                        break;
                    case 45:
                        if (info.getD45() == 0) holder.et.setText("");
                        else holder.et.setText(String.format(Locale.US, "%d", info.getD45()));
                        break;
                }
            }
        } else if ((position - iRow) / totalRow == 4) {
            holder.et.setEnabled(false);
            holder.et.setTextColor(Color.argb(255, 170, 0, 0));
            holder.et.setText(NumberFormat.getInstance().format(totalCarton));
        } else
            holder.et.setText("");

    }

    private int getLocation(int iRow, int position) {
        return (position - iRow) / totalRow - 4;
    }

    private int getRowIndexByPosition(int rowNumber, int pos) {
        return pos % rowNumber;
    }

    @Override
    public int getItemCount() {
        return totalRow * 50;
    }

    interface OnEditorActionListener {
        void onEditorAction(int rowCount, int pos);
    }

    class SDDViewHolder extends RecyclerView.ViewHolder {
        EditText et;

        public SDDViewHolder(View itemView) {
            super(itemView);
            et = (EditText) itemView.findViewById(R.id.item_et_so_do_day);
        }
    }
}

