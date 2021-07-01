package com.example.musicplayertest.helper;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicplayertest.bean.Song;
import com.example.musicplayertest.util.ScanMusicUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class MusicHelper implements
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnPreparedListener,
    SeekBar.OnSeekBarChangeListener{

    private static final String TAG = "MusicHelper";
    private static final int MSG_CODE = 0X01;
    private static final long MSG_TIME = 1_000l;

    private MediaPlayer player;

    private SeekBar seekBar;

    private TextView songInfo;

    private Song song;

    private MusicPlayerHelperHander hander;

    private OnCompletionListener onCompletionListener;


    public MusicHelper(SeekBar seekBar,TextView songInfo) {
        this.player = new MediaPlayer();
        this.seekBar = seekBar;
        this.songInfo = songInfo;
        this.hander = new MusicPlayerHelperHander(this);

//        this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.player.setOnBufferingUpdateListener(this);
        this.player.setOnCompletionListener(this);
        this.player.setOnPreparedListener(this);

    }



    public void play(Song song,boolean isResetPlayer){
        this.song = song;
        Log.d(TAG,"play" + song.getPath());
        if (isResetPlayer){
            this.player.reset();
            if (!TextUtils.isEmpty(song.getPath())){
                try {
                    this.player.setDataSource(song.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.player.prepareAsync();
        }else
            this.player.start();

        this.seekBar.setMax(song.getDuration());

        // 发送更新命令
        hander.sendEmptyMessage(MSG_CODE);

    }

    public void pause(){
        Log.d(TAG,"pause");
        if (player.isPlaying()){
            player.pause();
        }

        hander.removeMessages(MSG_CODE);

    }

    public void stop(){
        hander.removeMessages(MSG_CODE);
        if (song != null){
            Log.d(TAG,"stop current Music " + song.getName());
            this.player.stop();
            this.player.reset();
            seekBar.setProgress(0);
            songInfo.setText("停止播放");
            return;
        }
        Log.d(TAG,"当前无音乐播放");
    }


    /**
     *
     * 媒体资源缓冲状态
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
        int currentProgress = mp.getCurrentPosition()/mp.getDuration();
        Log.d(TAG,currentProgress + " % play  --->  " + percent + "% buffer");
    }

    /**
     * 当 song 播放完毕
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG,"当前歌曲播放完毕");
        if (onCompletionListener != null){
            onCompletionListener.onCompletion(mp);
        }
    }

    /**
     * 当前 song 已经准备好了
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG,"onPrepared");
        mp.start();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG,"onStartTrackingTouch");
        hander.removeMessages(MSG_CODE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG,"onStopTrackingTouch");
        int progress = this.seekBar.getProgress();
        this.player.seekTo(progress);
        hander.sendEmptyMessageDelayed(MSG_CODE,MSG_TIME);
    }

    public boolean isPlaying(){
        return this.player.isPlaying();
    }

    public void destroy(){
        player.release();
        hander.removeCallbacksAndMessages(null);
    }

    private String getCurrentPlayInfo(int currentTime,int maxTime){
        String info = String.format("正在播放： %s\t\t",song.getName());
        return String.format("%s %s/%s",info,
                ScanMusicUtils.formatTime(currentTime),ScanMusicUtils.formatTime(maxTime));
    }


    public interface OnCompletionListener{
        void onCompletion(MediaPlayer mp);
    }

    public void setOnCompletionListener(OnCompletionListener listener){
        this.onCompletionListener = listener;
    }

    static class MusicPlayerHelperHander extends Handler{

        private WeakReference<MusicHelper> wr;

        public MusicPlayerHelperHander(MusicHelper helper){
            super(Looper.getMainLooper());
            wr = new WeakReference<MusicHelper>(helper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CODE){
                int pos = 0;
                if (wr.get().player.isPlaying()&& !wr.get().seekBar.isPressed()){
                    pos = wr.get().player.getCurrentPosition();
                    int duration = wr.get().player.getDuration();
                    wr.get().songInfo.setText(wr.get().getCurrentPlayInfo(pos,duration));
                }
                wr.get().seekBar.setProgress(pos);
                sendEmptyMessageDelayed(MSG_CODE,MSG_TIME);
            }
        }
    }

}
