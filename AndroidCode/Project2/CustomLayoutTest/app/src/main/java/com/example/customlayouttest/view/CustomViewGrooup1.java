package com.example.customlayouttest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CustomViewGrooup1 extends ViewGroup {

    public CustomViewGrooup1(Context context) {
        this(context,null);
    }

    public CustomViewGrooup1(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomViewGrooup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        measureChildren(widthMeasureSpec,heightMeasureSpec);
//
//        int chileCount = getChildCount();
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        Log.d("hh","count: " + chileCount);
//
//        if (chileCount == 0 ){
//            setMeasuredDimension(0,0);
//        }else {
//            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
//                int height = getTotleHeight();
//                int width = getMaxChildWidth();
//                setMeasuredDimension(width,height);
//                Log.d("hh","width:" + width + ",  height :" + height);
//            }else if (heightMode == MeasureSpec.AT_MOST ){
//                //如果只有高度是包裹内容
//                //宽度设置为ViewGroup自己的测量宽度，高度设置为所有子View的高度总和
//                setMeasuredDimension(widthSize, getTotleHeight());
//            }else if(widthMode == MeasureSpec.AT_MOST){
//                //如果只有宽度是包裹内容
//                //宽度设置为子View中宽度最大的值，高度设置为ViewGroup自己的测量值
//                setMeasuredDimension(getMaxChildWidth(), heightSize);
//            }else
//                setMeasuredDimension(widthSize,heightSize);
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //将所有的子View进行测量，这会触发每个子View的onMeasure函数
        //注意要与measureChild区分，measureChild是对单个view进行测量
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("hh","CustomViewGrooup1 : widthSize : " + widthSize + " , heightSize : " + heightSize);
        Log.d("hh","CustomViewGrooup1 : widthMode : " + widthMode + " , heightMode : " + heightMode);

        int childCount = getChildCount();

        if (childCount == 0) {//如果没有子View,当前ViewGroup没有存在的意义，不用占用空间
            setMeasuredDimension(0, 0);
        } else {
            //如果宽高都是包裹内容
            if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
                //我们将高度设置为所有子View的高度相加，宽度设为子View中最大的宽度
                int height = getTotleHeight();
                int width = getMaxChildWidth();
                Log.d("hh","widthMode = MeasureSpec.AT_MOST && heightMode = MeasureSpec.AT_MOST");
                setMeasuredDimension(width, height);
            } else if (heightMode == MeasureSpec.AT_MOST) {
                //如果只有高度是包裹内容
                //宽度设置为ViewGroup自己的测量宽度，高度设置为所有子View的高度总和
                Log.d("hh","heightMode = MeasureSpec.AT_MOST");
                setMeasuredDimension(widthSize, getTotleHeight());
            } else if (widthMode == MeasureSpec.AT_MOST) {
                //如果只有宽度是包裹内容
                //宽度设置为子View中宽度最大的值，高度设置为ViewGroup自己的测量值
                Log.d("hh","widthMode = MeasureSpec.AT_MOST ");
                setMeasuredDimension(getMaxChildWidth(), heightSize);

            }else{
                Log.d("hh","widthMode != MeasureSpec.AT_MOST && heightMode != MeasureSpec.AT_MOST");
                setMeasuredDimension(widthSize,heightSize);
            }
        }




    }

    /***
     * 获取子View中宽度最大的值
     */
    private int getMaxChildWidth() {

        int childCount = getChildCount();
        int max = 0;
        for (int i = 0;i<childCount;i++){
            View view  = getChildAt(i);

            max = Math.max(max,view.getMeasuredWidth());
        }

        return max;
//        int childCount = getChildCount();
//        int maxWidth = 0;
//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            if (childView.getMeasuredWidth() > maxWidth)
//                maxWidth = childView.getMeasuredWidth();
//
//        }
//
//        return maxWidth;
    }

    /***
     * 将所有子View的高度相加
     **/
    private int getTotleHeight() {
        int childCount = getChildCount();
        int total = 0;
        for (int i = 0;i<childCount;i++){
            View view  = getChildAt(i);
            total += view.getMeasuredHeight();
        }

        return total;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        //记录当前的高度位置
        int curHeight = 0;
        int curwidth = 0;
        Log.d("hh","left: " + l + " , top: " + t);
        //将子View逐个摆放
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int height = child.getMeasuredHeight();
            int width = child.getMeasuredWidth();
            Log.d("hh","child " + i + " : width： " + width+ " , height： "+height);
            //摆放子View，参数分别是子View矩形区域的左、上、右、下边
            // layout中的参数是相对于 父布局 的偏移量
            child.layout(curwidth, curHeight, curwidth + width, curHeight + height);
            curHeight += height;
        }

    }
}
