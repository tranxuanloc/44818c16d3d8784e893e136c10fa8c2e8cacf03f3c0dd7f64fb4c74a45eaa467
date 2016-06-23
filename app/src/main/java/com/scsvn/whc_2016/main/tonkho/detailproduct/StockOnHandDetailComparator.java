package com.scsvn.whc_2016.main.tonkho.detailproduct;

import java.util.Comparator;

/**
 * Created by tranxuanloc on 4/12/2016.
 */
public class StockOnHandDetailComparator implements Comparator<StockOnHandDetailsInfo> {
    private String field;
    private int i;

    public StockOnHandDetailComparator(String field, int sort) {
        this.field = field;
        this.i = sort;
    }

    @Override
    public int compare(StockOnHandDetailsInfo lhs, StockOnHandDetailsInfo rhs) {
        if (field.equals("Location"))
            return i * lhs.getLocationNumber().compareTo(rhs.getLocationNumber());
        else if (field.equals("Status"))
            return i * lhs.getPalletStatus().compareTo(rhs.getPalletStatus());
        else if (field.equals("NSX"))
            return i * lhs.getProductionDate().compareTo(rhs.getProductionDate());
        else if (field.equals("HSD"))
            return i * lhs.getUseByDate().compareTo(rhs.getUseByDate());
        else if (field.equals("RODate"))
            return i * lhs.getReceivingOrderDate().compareTo(rhs.getReceivingOrderDate());
        return 0;
    }
}
