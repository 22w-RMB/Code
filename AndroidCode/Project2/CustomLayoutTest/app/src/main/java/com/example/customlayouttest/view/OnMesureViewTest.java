package com.example.customlayouttest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


/**
 *
 * Android 系统给我们提供了一个　MeasureSpec类，通过它来帮助我们测量 View，MeasureSpec 是一个 32 的 int 值，
 *          其中高 ２ 为为测量模式，低30位为测量的大小，这难道是二进制！是的，但是大可放心，
 *          google 还是挺为广大数学渣着想的，通过 MeasureSpec 的方法可以获取我们需要的数值。
 *
 * 再来说说测量模式把，测试模式可以为一下三种：
 *
 * EXACTLY:精确模式 当我们将控件的 layout_width 与 layout_height 属性制定为具体数值时(排除 wrap_content)，
 *          系统就使用这个模式，(同时 View 类的 onMeasure 默认是 EXACTLY 模式，所以不重写 onMeasure 的话，就只能使用默认模式)
 * AT_MOST:最大值模式 当控件的 layout_width 属性或 layout_height 属性为 wrap_content 时，
 *          控件大小一般随着子控件或内容的变化而变化，此时控件的此尺寸只要不超过父控件允许的最大尺寸。
 * UNSPECIFIED： 它不能其大小测量模式，View想多大就多大，通常情况在回执自定义View时才使用。
 *
 */
public class OnMesureViewTest extends LinearLayout {

    private static final String TAG = "OnMesureViewTest";

    public OnMesureViewTest(Context context) {
        super(context);
    }

    public OnMesureViewTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OnMesureViewTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置控件的宽高，记住这里默认是px，记得要分辨率转换实现适配
        setMeasuredDimension(getSize(widthMeasureSpec),getSize(heightMeasureSpec));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawColor(Color.parseColor("#50FF0000"));
    }

    private int getSize(int measureSpec){

        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode){
            case MeasureSpec.EXACTLY:
                //当layout_width或layout_height　match_parent 为固定数值走这里
                result = 500;
//                result = specSize-300;
//                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                //当layout_width或layout_height定义为 wrap_content　就走这里
                result = Math.min(200,specSize);
                Log.d(TAG,"result: "+result+"");
                Log.d(TAG,"specSize: "+specSize+"");
                break;
            case MeasureSpec.UNSPECIFIED:
                //如果没有指定大小
                result = 400;
                break;
        }
        return result;
    }

}
