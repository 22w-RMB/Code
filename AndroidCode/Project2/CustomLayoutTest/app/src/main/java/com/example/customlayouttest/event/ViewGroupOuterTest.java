package com.example.customlayouttest.event;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.customlayouttest.R;

public class ViewGroupOuterTest extends RelativeLayout {

    private static final String TAG = "ViewGroupOuter";

    private Paint paint;
    private String text;

    public ViewGroupOuterTest(Context context) {
        this(context,null);
    }

    public ViewGroupOuterTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ViewGroupOuterTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);
        text = typedArray.getString(R.styleable.HistogramView_ccontent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = getTop();
        int left  = getLeft();
        int right  = getRight();
        int bottom  = getBottom();
        Log.d(TAG,"top:" + top);
        Log.d(TAG,"left:" + left);
        Log.d(TAG,"right:" + right);
        Log.d(TAG,"bottom:" + bottom);
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setTextSize(35);
//        canvas.drawText("hello",(left+right)/2,(top+bottom)/2 ,paint);
        canvas.drawText(text,getWidth()/2 - 150,30 ,paint);
    }


}
