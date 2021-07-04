package com.example.customlayouttest.event;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class ViewInner extends Button {
    public ViewInner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("songzheweiwang","[ViewInner-->dispatchTouchEvent]event="+EventUtil.parseAction(event.getAction()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("songzheweiwang","[ViewInner-->onTouchEvent]event="+EventUtil.parseAction(event.getAction()));
        return super.onTouchEvent(event);
    }
}
