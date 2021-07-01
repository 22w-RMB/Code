package cn.flyaudio.module_music.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import cn.flyaudio.module_music.bean.Singer;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.event.RenameSongEvent;
import cn.flyaudio.module_music.event.UpdateSongDataEvent;
import cn.flyaudio.module_music.module.DataManager;
import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by yaoyuqing
 */

public class SingerViewModel extends BaseViewModel {
    public ObservableArrayList<Singer> singers=new ObservableArrayList<>();

    public SingerViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    // 更新数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSongDataEvent(UpdateSongDataEvent event) {
        List<Singer> singers = DataManager.getInstance().getAllSinger();
        if (singers != null) {
            this.singers.clear();
            this.singers.addAll(singers);
        }
    }

    /**
     * 重命名
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void reNameSong(RenameSongEvent event) {
        Song renameSong = event.getSong();
        String newName = event.getNewName();
        String newPath = event.getNewPath();
        if (renameSong == null || TextUtils.isEmpty(renameSong.getPath()) ||
                TextUtils.isEmpty(newName) || TextUtils.isEmpty(newPath) || !new File(newPath).exists()) {
            return;
        }

        // 歌手
        if (!TextUtils.isEmpty(renameSong.getSinger())) {
            for (int i = 0; i < singers.size(); i++) {
                Singer singer = singers.get(i);
                if (renameSong.getSinger().equals(singer.getName())) {
                    ObservableArrayList<Song> list = singer.getList();
                    for (int j = 0; j < list.size(); j++) {
                        Song song = list.get(j);
                        if (renameSong.getPath().equals(song.getPath())) {
                            singers.get(i).getList().get(j).setPath(newPath);
                            singers.get(i).getList().get(j).setName(newName);
                            break;
                        }
                    }
                    break;
                }
            }
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
