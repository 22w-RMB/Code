package com.example.customlayouttest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 *
 *  TextView 文字下方显示红色下划线
 */
@SuppressLint("AppCompatCustomView")
public class UnderLineTextView extends TextView {
    public UnderLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        int width = getWidth();
        int height = getHeight();
        canvas.drawLine(0,height,width,height,paint);
    }
}
