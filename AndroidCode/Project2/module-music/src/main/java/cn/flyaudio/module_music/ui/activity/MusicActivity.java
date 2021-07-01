package cn.flyaudio.module_music.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import cn.flyaudio.library_base.router.RouterActivityPath;
import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.ui.fragment.MusicMainFragment;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.ContainerActivity;

public class MusicActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // makeStatusBarTransparent(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        MusicMainFragment fragment = new MusicMainFragment();
        trans.replace(R.id.content, fragment);
        trans.commitAllowingStateLoss();
    }

    private void makeStatusBarTransparent(Activity activity) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof BaseFragment) {
            if (!((BaseFragment) fragment).isBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
