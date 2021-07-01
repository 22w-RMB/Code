package com.example.customlayouttest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


/**
 *
 *  简单的柱形图
 *
 */
public class HistogramView extends View {

    private Paint paint;
    private Path path;

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制坐标轴
        paint.reset();
        path.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        // moveTo：移动下一次操作的起点位置
        path.moveTo(100,100);
        // lineTo：添加上一个点到当前点之间的直线到Path
        //  rXxx：方法是基于当前点坐标系(偏移量)
        path.rLineTo(0,402);
        path.rLineTo(800,0);
        canvas.drawPath(path,paint);

        // 绘制文字
        paint.reset();
        paint.setTextSize(30);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Froyo",160,540,paint);
        canvas.drawText("CB",280,540,paint);
        canvas.drawText("ICS",380,540,paint);
        canvas.drawText("J",480,540,paint);
        canvas.drawText("KitKat",560,540,paint);
        canvas.drawText("L",690,540,paint);
        canvas.drawText("M",790,540,paint);

        // 绘制直方图，下面的柱形图是用比较粗的直线来实现的
        paint.reset();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(80);
        float[] lines3={
                200,500,200,495,
                300,500,300,480,
                400,500,400,480,
                500,500,500,300,
                600,500,600,200,
                700,500,700,150,
                800,500,800,350,
        };
        canvas.drawLines(lines3,paint);
        // sa
    }
}
