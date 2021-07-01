package cn.flyaudio.module_music.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.flyaudio.module_music.ui.activity.MusicActivity;
import cn.flyaudio.module_music.ui.fragment.MusicMainFragment;


/**
 * 组件单独运行时的调试界面，不会被编译进release里
 * Created by yaoyuqing
 */

public class DebugActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MusicActivity.class);
        intent.putExtra("fragment", MusicMainFragment.class.getCanonicalName());
        this.startActivity(intent);
        finish();
    }
}
