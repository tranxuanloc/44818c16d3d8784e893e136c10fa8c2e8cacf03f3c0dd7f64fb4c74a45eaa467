package com.scsvn.whc_2016.main.crm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by tranxuanloc on 8/11/2016.
 */
public class GridViewInvite extends GridView {
    public GridViewInvite(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewInvite(Context context) {
        super(context);
    }

    public GridViewInvite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
