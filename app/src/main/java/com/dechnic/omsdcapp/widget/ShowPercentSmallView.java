package com.dechnic.omsdcapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.dechnic.omsdcapp.R;


/**
 * Created by Administrator on 2017/4/6.
 */

public class ShowPercentSmallView extends View {
    private Paint percentPaint;

    private Paint textPaint;
    private int textSize = 30;

    private int percent;
    private int allLineColor;
    private int percentLineColorLow;
    private int percentLineColorHight;

    private int allLineWidth = 2;
    private int percentLineWidth =2;
    private int lineHeight = 15;

    public ShowPercentSmallView(Context context) {
        super(context);
        init(null, 0);
    }

    public ShowPercentSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ShowPercentSmallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ShowPercentView, defStyle, 0);
        percent = a.getInt(R.styleable.ShowPercentView_percent, 0);
        allLineColor = a.getColor(R.styleable.ShowPercentView_allLineColor, Color.GRAY);
        percentLineColorLow = a.getColor(R.styleable.ShowPercentView_percentLineColorLow, Color.WHITE);
        percentLineColorHight = a.getColor(R.styleable.ShowPercentView_percentLineColorHight, Color.WHITE);

        a.recycle();

        percentPaint = new Paint();
        percentPaint.setAntiAlias(true);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int pointX =  width/2;
        int pointY = height/2;


        percentPaint.setColor(allLineColor);
        percentPaint.setStrokeWidth(allLineWidth);

        float degrees = (float) (285.0/100);

        canvas.save();
        canvas.translate(0,pointY);
        canvas.rotate(-30, pointX, 0);
        for(int i = 0;i<43;i++){
            if (i==5||i==21||i==37) {
                canvas.drawLine(0, 0, 30, 0, percentPaint);
                canvas.rotate(degrees*2, pointX, 0);
            }else {
                canvas.drawLine(0, 0, lineHeight, 0, percentPaint);
                canvas.rotate(degrees*2, pointX, 0);
            }

        }
        canvas.restore();

//        percentPaint.setColor(percentLineColorLow);
//        percentPaint.setStrokeWidth(percentLineWidth);
//        canvas.save();
//        canvas.translate(0,pointY);
//        canvas.rotate(-30, pointX, 0);
//        for(int i = 0;i<40;i++){
//            canvas.drawLine(0, 0, lineHeight, 0, percentPaint);
//            canvas.rotate(degrees, pointX, 0);
//
//        }
//        canvas.restore();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        setMeasuredDimension(d,d);
    }

    public void setPercent(int percent) {
        // TODO Auto-generated method stub
        this.percent = percent;
        postInvalidate();
    }
}
