package cn.flyaudio.module_music.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import cn.flyaudio.module_music.R;

public class MusicDialog extends Dialog {

    public MusicDialog(@NonNull Context context) {
        super(context);
        init();
    }

    OnHandleListener onHandleListener;

    public OnHandleListener getOnHandleListener() {
        return onHandleListener;
    }

    public void setOnHandleListener(OnHandleListener onHandleListener) {
        this.onHandleListener = onHandleListener;
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fly_music_dialog);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.getDecorView().setBackgroundColor(Color.parseColor("#00000000"));

        View btn_cancel = findViewById(R.id.btn_cancel);
        View btn_sure = findViewById(R.id.btn_sure);
        btn_cancel.setOnClickListener(v -> {
            dismiss();
            if (onHandleListener != null) {
                onHandleListener.onCancel();
            }
        });
        btn_sure.setOnClickListener(v -> {
            dismiss();
            if (onHandleListener != null) {
                onHandleListener.onSure();
            }
        });
    }


    public interface OnHandleListener {
        void onCancel();

        void onSure();
    }
}
