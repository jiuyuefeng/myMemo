package com.vlife.mymemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class DrawLine extends EditText {
    private Paint ePaint;

    public DrawLine(Context context, AttributeSet attrs) {
        super(context, attrs);

        ePaint = new Paint();
        ePaint.setColor(Color.BLACK);
        ePaint.setStyle(Paint.Style.STROKE);
    }

    public void onDraw(Canvas canvas) {
        int count = getLineCount();
        for (int i = 0; i < count + 11; i++) {
            float[] pts = { 15.0F, (i + 1) * getLineHeight(),
                    this.getWidth() - 20.0F, (i + 1) * getLineHeight() };

            canvas.drawLines(pts, ePaint);
        }
        super.onDraw(canvas);
    }
}
