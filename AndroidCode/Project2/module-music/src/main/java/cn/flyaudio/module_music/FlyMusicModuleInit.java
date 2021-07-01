package cn.flyaudio.module_music;

import android.app.Application;

import cn.flyaudio.library_base.base.IModuleInit;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.util.SharedPreferencesUtil;


public class FlyMusicModuleInit implements IModuleInit {

    @Override
    public boolean onInitAhead(Application application) {
        SharedPreferencesUtil.getInstance().init(application);
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }

}
