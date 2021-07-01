package cn.flyaudio.module_music.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;

import cn.flyaudio.module_music.R;
import me.goldze.mvvmhabit.utils.KLog;

public class RenameDialog extends Dialog {

    private EditText et_name;

    public RenameDialog(@NonNull Context context) {
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
        setContentView(R.layout.fly_music_rename_dialog);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.getDecorView().setBackgroundColor(Color.parseColor("#00000000"));

        View btn_cancel = findViewById(R.id.btn_cancel);
        View btn_sure = findViewById(R.id.btn_sure);
        et_name = findViewById(R.id.et_name);
        btn_cancel.setOnClickListener(v -> {
            dismiss();
            if (onHandleListener != null) {
                onHandleListener.onCancel();
            }
        });
        btn_sure.setOnClickListener(v -> {

            dismiss();
            if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                KLog.d("change name  to null , return ");
                return;
            }
            if (onHandleListener != null) {
                onHandleListener.onSure(et_name.getText().toString().trim());
            }
        });
    }

    public void setText(String text) {
        et_name.setText(text);
    }

    public interface OnHandleListener {
        void onCancel();

        void onSure(String name);
    }
}
