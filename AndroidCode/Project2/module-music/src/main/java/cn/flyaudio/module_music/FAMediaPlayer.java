package cn.flyaudio.module_music;

import android.content.ComponentName;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.event.UpdatePlayProgressEvent;
import cn.flyaudio.module_music.event.UpdatePlayStateEvent;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.util.TimeUtil;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * Describe:
 * <p>音乐播放器帮助类</p>
 * 可播放格式：AAC、AMR、FLAC、MP3、MIDI、OGG、PCM
 *
 * @author zhouhuan
 * @Date 2020/11/19
 */
public class FAMediaPlayer implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener {


    /**
     * Exception Listenner flag
     **/
    public static final int MEDIA_ERROR_SET_DATASOURCE_IOEXCEPTION = 2001;
    public static final int MEDIA_ERROR_DATASOURCE_PREPARE_TIMEOUT = 2002;

    public static String TAG = FAMediaPlayer.class.getSimpleName();
    private static int MSG_CODE = 101;
    private static int MSG_TIME = 1000;
    /**
     * 播放器
     */
    private MediaPlayer player;
    private MusicPlayerHelperHanlder mHandler;
    UpdatePlayStateEvent playStateEvent = new UpdatePlayStateEvent();
    private ComponentName mediaEventComponentName;

    /**
     * 当前的播放歌曲信息
     */
    private Song songModel;
    private static volatile FAMediaPlayer mInstance;
    //歌曲准备好之后，seekTo到哪个地方
    private int seekProgress = 0;

    public static FAMediaPlayer getInstance() {
        if (mInstance == null) {
            synchronized (FAMediaPlayer.class) {
                if (mInstance == null) {
                    mInstance = new FAMediaPlayer();
                }
            }
        }
        return mInstance;
    }

    public FAMediaPlayer() {
        mHandler = new MusicPlayerHelperHanlder(this);
        player = new MediaPlayer();
        // 设置媒体流类型
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
        player.setOnBufferingUpdateListener(this);
        player.setOnSeekCompleteListener(this);
    }

    public int getAudioSessionId() {
        return player.getAudioSessionId();
    }

    /**
     * 当前 Song 已经准备好
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        KLog.d("onPrepared");
        mp.start();
        if (seekProgress > 0) {
            mp.seekTo(seekProgress);
            seekProgress = 0;
        }
        playStateEvent.setPlay(true);
        EventBus.getDefault().post(playStateEvent);
    }


    OnErrorListener onErrorListener;

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void seekTo(int progress) {
        player.seekTo(progress);
    }

    /**
     * 准备完成后需要seekTo到哪个进度
     *
     * @param progress
     */
    public void setSeekProgress(int progress) {
        seekProgress = progress;
    }

    public interface OnErrorListener {
        boolean onError(MediaPlayer mp, int what, int extra);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (onErrorListener != null) {
            onErrorListener.onError(mp, what, extra);
        }
        return false;
    }

    /**
     * 当前 Song 播放完毕
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        KLog.d(TAG, "onCompletion");
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(mp);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        int currentPosition = player.getCurrentPosition();
        int duration = player.getDuration();

        UpdatePlayProgressEvent event = new UpdatePlayProgressEvent();
        event.setDuration(duration);
        event.setCurrentPosition(currentPosition);
        EventBus.getDefault().post(event);
    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {
        KLog.d("onSeekComplete");
    }


    /**
     * 播放
     *
     * @param songModel    播放源
     * @param isRestPlayer true 切换歌曲 false 不切换
     */
    public void playBySongModel(Song songModel, Boolean isRestPlayer) {
        seekProgress = 0;
        this.songModel = songModel;
        DataManager.getInstance().saveCurrentSong(songModel);
        KLog.d(TAG, "playBySongModel Url: " + songModel.getPath());
        if (isRestPlayer) {
            player.reset(); //重置多媒体
            if (!TextUtils.isEmpty(songModel.getPath())) {
                try {
                    player.setDataSource(songModel.getPath());  // 设置数据源
                    player.prepareAsync();// 建议使用异步加载方式，不阻塞 UI 线程
                } catch (Exception e) {
                    onError(MEDIA_ERROR_SET_DATASOURCE_IOEXCEPTION);
                }
            }
        } else {
            player.start();
            playStateEvent.setPlay(true);
            EventBus.getDefault().post(playStateEvent);
        }
        //发送更新命令
        mHandler.removeMessages(MSG_CODE);
        mHandler.sendEmptyMessage(MSG_CODE);
    }

    /**
     * 暂停
     */
    public void pause() {
        KLog.d(TAG, "pause");
        if (player.isPlaying()) {
            player.pause();
            playStateEvent.setPlay(false);
            EventBus.getDefault().post(playStateEvent);
        }
        mHandler.removeMessages(MSG_CODE); //移除更新命令
    }

    /**
     * 停止
     */
    public void stop() {
        KLog.d(TAG, "stop");
        player.stop();
        mHandler.removeMessages(MSG_CODE);        //移除更新命令
    }


    /**
     * 是否正在播放
     */
    public Boolean isPlaying() {
        return player.isPlaying();
    }

    /**
     * 消亡 必须在 Activity 或者 Frament onDestroy() 调用 以防止内存泄露
     */
    public void release() {
        // 释放掉播放器
        if (player != null) {
            player.release();
        }
        mHandler.removeCallbacksAndMessages(null);
    }


    /**
     * 用于监听SeekBar开始拖动
     */
    public void onStartTrackingTouch() {
        mHandler.removeMessages(MSG_CODE);
    }

    /**
     * 用于监听SeekBar停止拖动  SeekBar停止拖动后的事件
     */
    public void onStopTrackingTouch(int currentProgress, int maxProgress) {
        int musicMax = player.getDuration();        // 得到该首歌曲最长秒数
        // SeekBar最大值
        float msec = currentProgress / (maxProgress * 1.0F) * musicMax;        //计算相对当前播放器歌曲的应播放时间
        // 跳到该曲该秒
        player.seekTo((int) msec);
        mHandler.removeMessages(MSG_CODE);
        mHandler.sendEmptyMessage(MSG_CODE);
    }

    private String getCurrentPlayingInfo(int currentTime, int maxTime) {
        String info = String.format("正在播放:  %s\t\t", songModel.getName());
        return String.format("%s %s / %s", info, TimeUtil.formatTime(currentTime), TimeUtil.formatTime(maxTime));
    }

    private OnCompletionListener mOnCompletionListener;


    /**
     * Register a callback to be invoked when the end of a media source
     * has been reached during playback.
     *
     * @param listener the callback that will be run
     */
    public void setOnCompletionListener(@NonNull OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }


    /**
     * Interface definition for a callback to be invoked when playback of
     * a media source has completed.
     */
    public interface OnCompletionListener {
        /**
         * Called when the end of a media source is reached during playback.
         *
         * @param mp the MediaPlayer that reached the end of the file
         */
        void onCompletion(MediaPlayer mp);
    }

    static class MusicPlayerHelperHanlder extends Handler {
        WeakReference<FAMediaPlayer> weakReference;

        public MusicPlayerHelperHanlder(FAMediaPlayer helper) {
            super(Looper.getMainLooper());
            this.weakReference = new WeakReference<>(helper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CODE) {
                if (weakReference.get() != null && weakReference.get().player.isPlaying()) {
                    int currentPosition = weakReference.get().player.getCurrentPosition();
                    int duration = weakReference.get().player.getDuration();
                    UpdatePlayProgressEvent event = new UpdatePlayProgressEvent();
                    event.setDuration(duration);
                    event.setCurrentPosition(currentPosition);
                    EventBus.getDefault().post(event);
                    //记录当前进度
                    DataManager.getInstance().saveCurrentProgress(currentPosition);
                }
                sendEmptyMessageDelayed(MSG_CODE, MSG_TIME);
            }
        }
    }


    private void onError(int errorCode) {
        if (onErrorListener != null) {
            onErrorListener.onError(player, errorCode, 0);
        }
    }


}
