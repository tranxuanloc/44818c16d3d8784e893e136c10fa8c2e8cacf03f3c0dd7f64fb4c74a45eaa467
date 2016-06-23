package com.scsvn.whc_2016.main.tonkho.detailkhachhang;

import java.util.Comparator;

/**
 * Created by tranxuanloc on 4/11/2016.
 */
public class StockByCustomerComparator implements Comparator<StockOnHandByCustomerInfo> {
    private String field;
    private int i;

    public StockByCustomerComparator(String field, int i) {
        this.field = field;
        this.i = i;
    }

    @Override
    public int compare(StockOnHandByCustomerInfo lhs, StockOnHandByCustomerInfo rhs) {
        if (field.equals("ProductID"))
            return i * lhs.getProductNumber().compareTo(rhs.getProductNumber());
        else if (field.equals("ProductName"))
            return i * lhs.getProductName().compareTo(rhs.getProductName());
        return 0;
    }
}
