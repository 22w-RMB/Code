package com.example.lockscreen.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.example.lockscreen.activity.LockScreenActivity;
import com.example.lockscreen.util.Parser;

import static com.example.lockscreen.util.Parser.KEY_GUARD_INSTANCES;

public class LockScreenReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_SCREEN_OFF.equals(action)){
            // 手机状态为未来电的空闲状态
            if (Parser.sPhoneCallState == TelephonyManager.CALL_STATE_IDLE){
                if (!KEY_GUARD_INSTANCES.isEmpty()){
                    for (Activity activity:KEY_GUARD_INSTANCES){
                        activity.finish();
                    }
                }

                Intent lockScreen = new Intent(context, LockScreenActivity.class);
                lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(lockScreen);

            }
        }else {
            Parser.killBackgroundProcess(context);
        }
    }
}
