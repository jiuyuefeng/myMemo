package com.vlife.mymemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * listView文本栏划线
 * Created by Administrator on 2016/4/27 0027.
 */
public class TextViewLine extends TextView {
    private Paint paint = new Paint();

    public TextViewLine(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.paint.setColor(Color.GRAY);
        this.paint.setStyle(Paint.Style.STROKE);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i=getLineCount();
        for (int j = 0; ; ++j) {
            if (j >= i) {
                super.onDraw(canvas);
                return;
            }
            float[] arrayOfFloat = new float[4];
            arrayOfFloat[0] = 15.0F;
            arrayOfFloat[1] = ((j + 1) * getLineHeight());
            arrayOfFloat[2] = (-20 + getWidth());
            arrayOfFloat[3] = ((j + 1) * getLineHeight());
            canvas.drawLines(arrayOfFloat, this.paint);
        }
    }
}
