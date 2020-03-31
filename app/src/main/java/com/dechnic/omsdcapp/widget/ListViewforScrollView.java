package com.dechnic.omsdcapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ListViewforScrollView extends ListView{
    public ListViewforScrollView(Context context) {
        super(context);
    }

    public ListViewforScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewforScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
