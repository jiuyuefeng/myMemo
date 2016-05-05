package com.vlife.mymemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class DateLine extends TextView{

    private Paint paint = new Paint();
    public DateLine(Context context) {
        super(context);
    }
    public DateLine (Context context, AttributeSet attrs){
        super(context,attrs);
        this.paint.setColor(16777216);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine(0.0F, 40.0F, getWidth(), 40.0F, this.paint);
        super.onDraw(canvas);
    }
}
