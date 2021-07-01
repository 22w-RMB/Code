package cn.flyaudio.module_music.module;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.flyaudio.module_music.FAMediaPlayer;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.constant.SPKey;
import cn.flyaudio.module_music.event.ControlEvent;
import cn.flyaudio.module_music.event.RenameSongEvent;
import cn.flyaudio.module_music.event.SeekBarTrackEvent;
import cn.flyaudio.module_music.event.UpdatePlayInfoEvent;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.SPUtils;

public class PlayManager {
    private static final String TAG = "PlayManager";
    private FAMediaPlayer helper;
    List<Song> allSongs = new ArrayList<>();

    Song currentSong = null;
    int currentIndex = -1;
    private static volatile PlayManager mInstance;
    private Context mContext;
    private AudioManager mAudioManager;
    private ComponentName mediaEventComponentName;

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    private PlayManager() {
        helper = FAMediaPlayer.getInstance();
        helper.setOnErrorListener(onErrorListener);
        setPlayMode();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void handleControlEvent(ControlEvent event) {
        switch (event.getControlType()) {
            case ControlEvent.CONTROL_PREV:
                prev();
                break;
            case ControlEvent.CONTROL_PLAY:
                if (currentIndex == -1) {
                    currentIndex = 0;
                }
                if (allSongs.size() > 0) {
                    play(allSongs.get(currentIndex), false);
                }
                break;
            case ControlEvent.CONTROL_NEXT:
                next();
                break;
            case ControlEvent.CONTROL_LIST_LOOP:
                helper.setOnCompletionListener(mp -> next());
                break;
            case ControlEvent.CONTROL_SINGLE_LOOP:
                helper.setOnCompletionListener(mp -> loop());
                break;
            case ControlEvent.CONTROL_SHUFFLE_LOOP:
                helper.setOnCompletionListener(mp -> random());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void handleControlSeekBar(SeekBarTrackEvent event) {
        if (event.getEvent() == SeekBarTrackEvent.EVENT_START) {
            helper.onStartTrackingTouch();
        } else {
            helper.onStopTrackingTouch(event.getCurrentProgress(), event.getMaxProgress());
        }
    }

    /**
     * 设置播放循环模式
     */
    private void setPlayMode() {
        int playMode = SPUtils.getInstance().getInt(SPKey.PLAY_LOOP, SPKey.PLAY_LOOP_MODE_LIST_LOOP);
        switch (playMode) {
            case SPKey.PLAY_LOOP_MODE_LIST_LOOP:
                helper.setOnCompletionListener(mp -> next());
                break;
            case SPKey.PLAY_LOOP_MODE_SINGLE_LOOP:
                helper.setOnCompletionListener(mp -> loop());
                break;
            case SPKey.PLAY_LOOP_MODE_SHUFFLE_LOOP:
                helper.setOnCompletionListener(mp -> random());
                break;
        }
    }

    public static PlayManager getInstance() {
        if (mInstance == null) {
            synchronized (PlayManager.class) {
                if (mInstance == null) {
                    mInstance = new PlayManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 播放记忆的歌曲
     *
     * @param list
     * @param index
     * @param progress 记忆的进度
     */
    public void playSaveSong(List<Song> list, int index, int progress) {
        this.allSongs = list;
        currentIndex = index;
        play(allSongs.get(index), true);
        if (progress > 0) {
            helper.setSeekProgress(progress);
        }
    }

    public void playList(List<Song> list, int index) {
        this.allSongs = list;
        currentIndex = index;
        play(allSongs.get(index), true);
    }

    FAMediaPlayer.OnErrorListener onErrorListener = new FAMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                case MediaPlayer.MEDIA_ERROR_IO:
                case FAMediaPlayer.MEDIA_ERROR_SET_DATASOURCE_IOEXCEPTION:
                case FAMediaPlayer.MEDIA_ERROR_DATASOURCE_PREPARE_TIMEOUT:
                    release();
                    next();
                    break;

                default:
                    break;
            }
            return false;
        }
    };

    private void release() {
        helper.release();
    }

    public void play() {
        if (currentSong == null && allSongs.size() > 0) {
            currentSong = allSongs.get(0);
        }
        if (currentSong != null) {
            requestAudioFocus();
            helper.playBySongModel(currentSong, false);
        }
    }


    /**
     * 重命名
     * @param event
     */
    public void reNameSong(RenameSongEvent event) {
        Song renameSong = event.getSong();
        String newName = event.getNewName();
        String newPath = event.getNewPath();
        if (renameSong == null || TextUtils.isEmpty(renameSong.getPath()) ||
                TextUtils.isEmpty(newName) || TextUtils.isEmpty(newPath) || !new File(newPath).exists()) {
            return;
        }
        if (currentSong != null && renameSong.getPath().equals(currentSong.getPath())) {
            currentSong.setPath(newPath);
            currentSong.setName(newName);
            UpdatePlayInfoEvent playInfoEvent = new UpdatePlayInfoEvent();
            playInfoEvent.setSong(currentSong);
            EventBus.getDefault().post(playInfoEvent);
            DataManager.getInstance().saveCurrentSong(currentSong);
        }
    }

    public void play(Song song, Boolean isRestPlayer) {
        if (!TextUtils.isEmpty(song.getPath()) || !new File(song.getPath()).exists()) {
            UpdatePlayInfoEvent playInfoEvent = new UpdatePlayInfoEvent();
            playInfoEvent.setSong(song);
            EventBus.getDefault().post(playInfoEvent);

            if (!isRestPlayer && helper.isPlaying()) {    // 当前若是播放，则进行暂停
                pause();
            } else {
                requestAudioFocus();
                currentSong = song;
                //进行切换歌曲播放
                helper.playBySongModel(song, isRestPlayer);
            }
        } else {
            KLog.d("当前的播放地址无效");
        }
    }


    public boolean isPlay() {
        return helper.isPlaying();
    }

    /**
     * 上一首
     */
    public void prev() {
        if (allSongs.size() == 0) {
            return;
        }
        currentIndex--;
        //如果上一曲小于0则取最后一首
        if (currentIndex < 0) {
            currentIndex = allSongs.size() - 1;
        }
        play(allSongs.get(currentIndex), true);
    }

    /**
     * 下一首
     */
    public void next() {
        if (allSongs.size() == 0) {
            pause();
            release();
            return;
        }
        currentIndex++;
        //如果下一曲大于歌曲数量则取第一首
        if (currentIndex >= allSongs.size()) {
            currentIndex = 0;
        }
        play(allSongs.get(currentIndex), true);
    }

    public void seekTo(int progress) {
        helper.seekTo(progress);
    }

    private int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * 循环播放
     */
    private void loop() {
        play(allSongs.get(currentIndex), true);
    }

    /**
     * 随机播放
     */
    private void random() {
        Random r = new Random();
        int i = r.nextInt(allSongs.size() - 1) + 1;//给出介于1（含）和歌曲总数（不含）之间的随机整数
        play(allSongs.get(i), true);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        helper.pause();
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * 请求音频焦点
     */
    private void requestAudioFocus() {
        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        // 获得音频焦点的时候注册媒体事件的广播
        if (mediaEventComponentName == null) {
            Log.d(TAG, "requestAudioFocus: registerReceiver mediaEventReceiver ");
            mediaEventComponentName = new ComponentName(mContext.getPackageName(), "cn.flyaudio.module_music.broadcast.MediaEventReceiver");
            mAudioManager.registerMediaButtonEventReceiver(mediaEventComponentName);
        }
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            KLog.d(TAG, "AudioFocus: received  focusChange = " + focusChange);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS:
                    pause();
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {// 长时间失去才设置
                        // 失去音频焦点的时候解绑媒体事件广播
                        if (mediaEventComponentName != null) {
                            Log.d(TAG, "onAudioFocusChanged: unregisterMediaButtonEventReceiver ");
                            mAudioManager.unregisterMediaButtonEventReceiver(mediaEventComponentName);
                            mediaEventComponentName = null;
                        }
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    KLog.d(TAG, "AudioFocus: received AUDIOFOCUS_GAIN  ");
                    play();
                    break;
                default:
                    KLog.e(TAG, "Unknown audio focus change code");
                    break;
            }
        }
    };
}
