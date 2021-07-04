package com.example.customlayouttest.event;

import android.view.MotionEvent;

public class EventUtil {
    public static String parseAction(int action) {
        String actionName = "Unknow:action=" + action;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionName = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                actionName = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                actionName = "ACTION_UP";
                break;
            default:
                break;
        }
        return actionName;
    }
}
