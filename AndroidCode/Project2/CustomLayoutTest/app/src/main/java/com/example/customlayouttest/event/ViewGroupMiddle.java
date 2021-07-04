package com.example.customlayouttest.event;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class ViewGroupMiddle extends RelativeLayout {

    public ViewGroupMiddle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("songzheweiwang","[ViewGroupMiddle-->dispatchTouchEvent]ev="+EventUtil.parseAction(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("songzheweiwang","[ViewGroupMiddle-->onInterceptTouchEvent]ev="+EventUtil.parseAction(ev.getAction()));
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("songzheweiwang","[ViewGroupMiddle-->onTouchEvent]event="+EventUtil.parseAction(event.getAction()));
        return super.onTouchEvent(event);
    }
}