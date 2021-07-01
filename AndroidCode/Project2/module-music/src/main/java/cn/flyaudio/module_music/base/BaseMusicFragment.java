package cn.flyaudio.module_music.base;


import androidx.databinding.ViewDataBinding;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;

public abstract class BaseMusicFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<V, VM> {
    public static final int INVALID_INDEX = -1;

    public abstract boolean isDetail();

    public abstract void onBack();

    public boolean onClickBack() {
        if (isDetail()) {
            this.onBack();
            return true;
        }
        return false;
    }
}
