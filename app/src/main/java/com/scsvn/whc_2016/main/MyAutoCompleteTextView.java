package com.scsvn.whc_2016.main;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by tranxuanloc on 9/7/2016.
 */
public class MyAutoCompleteTextView extends AppCompatAutoCompleteTextView {
    public MyAutoCompleteTextView(Context context) {
        super(context);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }


    public void performFiltering() {
        performFiltering(getText(), 0);
    }
}
