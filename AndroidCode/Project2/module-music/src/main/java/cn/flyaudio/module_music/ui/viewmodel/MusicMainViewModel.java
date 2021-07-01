package cn.flyaudio.module_music.ui.viewmodel;


import android.app.Application;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.alibaba.android.arouter.launcher.ARouter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import cn.flyaudio.library_base.router.RouterActivityPath;
import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.bean.Album;
import cn.flyaudio.module_music.bean.Folder;
import cn.flyaudio.module_music.bean.Singer;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.constant.SPKey;
import cn.flyaudio.module_music.event.ControlEvent;
import cn.flyaudio.module_music.event.UpdatePlayInfoEvent;
import cn.flyaudio.module_music.event.UpdatePlayProgressEvent;
import cn.flyaudio.module_music.event.UpdatePlayStateEvent;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.module.PlayManager;
import cn.flyaudio.module_music.scan.MediaScanner;
import cn.flyaudio.module_music.util.AlbumUtil;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.SPUtils;

import static me.goldze.mvvmhabit.utils.Utils.getContext;

/**
 * Created by yaoyuqing
 */

public class MusicMainViewModel extends BaseViewModel {
    public static final int PAGE_ALL = 0;
    public static final int PAGE_SINGER = 1;
    public static final int PAGE_ALBUM = 2;
    public static final int PAGE_FOLDER = 3;

    private Application application;
    public ObservableField<Bitmap> currentAlbumBitmap = new ObservableField<>();

    public MusicMainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public ObservableField<String> loadingPath = new ObservableField();
    public ObservableField<String> songName = new ObservableField();
    public ObservableField<String> songSinger = new ObservableField();
    public ObservableField<Boolean> playState = new ObservableField();
    public ObservableField<Boolean> loadingState = new ObservableField();


    public ObservableField<Integer> currentProgress = new ObservableField();
    public ObservableField<Integer> maxProgress = new ObservableField();
    public ObservableField<Integer> loopMode = new ObservableField();

    public SingleLiveEvent<Integer> viewPagerChooseEvent = new SingleLiveEvent();

    // 循环模式
    private int playMode = 0;

    MediaScanner.IScanUICallback scanUICallback = new MediaScanner.IScanUICallback() {

        @Override
        public void setScanState(Boolean isScanning) {
            loadingState.set(isScanning);
        }

        @Override
        public void setScanMusicNum(int hasScan) {

        }

        @Override
        public void setScanPath(String path) {
            loadingPath.set(path);
        }

        @Override
        public void notifyUpdateData(Boolean updateplaylist) {

        }

        @Override
        public void setScanResult(List<Song> list, List<Folder> folderList,
                                  List<Singer> singerList, List<Album> albumList) {
            DataManager.getInstance().setAllData(list, folderList, singerList, albumList);
            // 扫描完成自动播放 第一首
            if (PlayManager.getInstance().getCurrentSong() == null && list != null && list.size() > 0) {
                PlayManager.getInstance().playList(list, 0);
            }
        }
    };

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            KLog.d(" onClick  ");
            if (v.getId() == R.id.ll_all || v.getId() == R.id.iv_all) {
                viewPagerChooseEvent.setValue(PAGE_ALL);
            } else if (v.getId() == R.id.ll_singer || v.getId() == R.id.iv_singer) {
                viewPagerChooseEvent.setValue(PAGE_SINGER);
            } else if (v.getId() == R.id.ll_album || v.getId() == R.id.iv_album) {
                viewPagerChooseEvent.setValue(PAGE_ALBUM);
            } else if (v.getId() == R.id.ll_folder || v.getId() == R.id.iv_folder) {
                viewPagerChooseEvent.setValue(PAGE_FOLDER);
            } else if (v.getId() == R.id.tv_bt_music) {
                // 跳转蓝牙音乐
                ARouter.getInstance()
                        .build(RouterActivityPath.BTMusic.MAIN)
                        .navigation();
            }
        }
    };

    public BindingCommand audioEffectOnClick = new BindingCommand(() -> {
        ARouter.getInstance()
                .build(RouterActivityPath.EQEffect.MAIN)
                .navigation();
    });

    public BindingCommand searchOnClick = new BindingCommand(() -> {
        MediaScanner.getInstance(application, scanUICallback).doScanandPaser();
    });

    public BindingCommand lastOnClick = new BindingCommand(() -> {
        ControlEvent event = new ControlEvent(ControlEvent.CONTROL_PREV);
        EventBus.getDefault().post(event);
        resetProgress();
    });

    public BindingCommand nextOnClick = new BindingCommand(() -> {
        ControlEvent event = new ControlEvent(ControlEvent.CONTROL_NEXT);
        EventBus.getDefault().post(event);
        resetProgress();
    });

    public BindingCommand playOnClick = new BindingCommand(() -> {
        ControlEvent event = new ControlEvent(ControlEvent.CONTROL_PLAY);
        EventBus.getDefault().post(event);
    });

    private void resetProgress() {
        currentProgress.set(0);
        maxProgress.set(0);
    }

    /***
     * 1:列表循环 2：单曲循环 3：随机播放
     */
    public View.OnClickListener loopOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (playMode >= 3) {
                playMode = 0;
            }
            switch (++playMode) {
                case SPKey.PLAY_LOOP_MODE_LIST_LOOP: {
                    SPUtils.getInstance().put(SPKey.PLAY_LOOP, SPKey.PLAY_LOOP_MODE_LIST_LOOP);
                    loopMode.set(SPKey.PLAY_LOOP_MODE_LIST_LOOP);
                    ControlEvent event = new ControlEvent(ControlEvent.CONTROL_LIST_LOOP);
                    EventBus.getDefault().post(event);
                }
                break;
                case SPKey.PLAY_LOOP_MODE_SINGLE_LOOP: {
                    SPUtils.getInstance().put(SPKey.PLAY_LOOP, SPKey.PLAY_LOOP_MODE_SINGLE_LOOP);
                    loopMode.set(SPKey.PLAY_LOOP_MODE_SINGLE_LOOP);
                    ControlEvent event = new ControlEvent(ControlEvent.CONTROL_SINGLE_LOOP);
                    EventBus.getDefault().post(event);
                }
                break;

                case SPKey.PLAY_LOOP_MODE_SHUFFLE_LOOP: {
                    SPUtils.getInstance().put(SPKey.PLAY_LOOP, SPKey.PLAY_LOOP_MODE_SHUFFLE_LOOP);
                    loopMode.set(SPKey.PLAY_LOOP_MODE_SHUFFLE_LOOP);
                    ControlEvent event = new ControlEvent(ControlEvent.CONTROL_SHUFFLE_LOOP);
                    EventBus.getDefault().post(event);
                }
                break;
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        int loop = SPUtils.getInstance().getInt(SPKey.PLAY_LOOP, SPKey.PLAY_LOOP_MODE_LIST_LOOP);
        loopMode.set(loop);
    }

    @Override
    public void onResume() {
        super.onResume();
        KLog.d("onResume");
        playSong();
    }

    private void playSong() {
        Song currentSong = PlayManager.getInstance().getCurrentSong();
        if (currentSong == null) {  // 如果当前没有播放音乐,则获取记忆的音乐
            currentSong = DataManager.getInstance().getSaveSong();
            if (currentSong != null && new File(currentSong.getPath()).exists()) {
                PlayManager.getInstance().play(currentSong, true);
                ObservableArrayList<Song> allSong = DataManager.getInstance().getAllSong();
                for (int i = 0; i < allSong.size(); i++) {
                    Song song = allSong.get(i);
                    if (currentSong.getPath().equals(song.getPath())) {
                        int saveProgress = DataManager.getInstance().getSaveProgress();
                        if (new File(allSong.get(i).getPath()).exists()){
                            PlayManager.getInstance().playSaveSong(allSong, i, saveProgress);
                        }
                        break;
                    }
                }
            }
        } else {
            if (!PlayManager.getInstance().isPlay()){
                PlayManager.getInstance().play();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void updatePlayInfo(UpdatePlayInfoEvent event) {
        Song song = event.getSong();
        if (song != null && new File(song.getPath()).exists()) {
            String name = song.getName();
            String singer = song.getSinger();
            songName.set(name);
            songSinger.set(singer);
            currentAlbumBitmap.set(AlbumUtil.getAlbumBitmap(event.getSong().getPath()));
        } else {
            songName.set(getContext().getString(R.string.fly_music_song_invalid));
            songSinger.set("");
            currentAlbumBitmap.set(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePlayState(UpdatePlayStateEvent event) {
        boolean playState = event.isPlay();
        this.playState.set(playState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePlayProgress(UpdatePlayProgressEvent event) {
        currentProgress.set(event.getCurrentPosition());
        maxProgress.set(event.getDuration());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
