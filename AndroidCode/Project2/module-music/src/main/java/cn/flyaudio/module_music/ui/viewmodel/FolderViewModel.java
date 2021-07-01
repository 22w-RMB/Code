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

import cn.flyaudio.module_music.bean.Folder;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.event.RenameSongEvent;
import cn.flyaudio.module_music.event.UpdateSongDataEvent;
import cn.flyaudio.module_music.module.DataManager;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.KLog;

public class FolderViewModel extends BaseViewModel {

    public ObservableArrayList<Folder> folders = new ObservableArrayList<>();

    public FolderViewModel(@NonNull Application application) {
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
        List<Folder> folders = DataManager.getInstance().getAllFolder();
        KLog.d("updateSongDataEvent: " + folders.size());
        if (folders != null) {
            this.folders.clear();
            this.folders.addAll(folders);
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
        // 文件夹
        if (!TextUtils.isEmpty(renameSong.getParentPath())) {
            for (int i = 0; i < folders.size(); i++) {
                Folder folder = folders.get(i);
                if (renameSong.getParentPath().equals(folder.getPath())) {
                    ObservableArrayList<Song> list = folder.getList();
                    for (int j = 0; j < list.size(); j++) {
                        Song song = list.get(j);
                        if (renameSong.getPath().equals(song.getPath())) {
                            folders.get(i).getList().get(j).setPath(newPath);
                            folders.get(i).getList().get(j).setName(newName);
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
