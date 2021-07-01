package com.example.customlayouttest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 *
 *
 *  在layout布局上添加一个浅红色的半透明蒙板
 */
public class ForegroundLinearLayout extends LinearLayout {
    public ForegroundLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawColor(Color.parseColor("#50FF0000"));
    }
}
