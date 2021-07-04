package com.example.customlayouttest.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.customlayouttest.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("songzheweiwang","[EventDemoActivity-->dispatchTouchEvent]ev="+EventUtil.parseAction(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("songzheweiwang","[EventDemoActivity-->onTouchEvent]event="+EventUtil.parseAction(event.getAction()));
        return super.onTouchEvent(event);
    }
}