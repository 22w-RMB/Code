package cn.flyaudio.module_music.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import cn.flyaudio.module_music.module.PlayManager;

public class MediaEventReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaEventReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            try {
                KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                Log.d(TAG, "onReceive: event " + event.toString());
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            PlayManager.getInstance().next();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            PlayManager.getInstance().prev();
                            break;

                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            PlayManager.getInstance().pause();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                            PlayManager.getInstance().play();
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
