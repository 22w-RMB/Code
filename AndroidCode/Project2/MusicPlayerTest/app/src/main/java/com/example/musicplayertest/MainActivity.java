package com.example.musicplayertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayertest.adapter.SongAdapter;
import com.example.musicplayertest.bean.Song;
import com.example.musicplayertest.helper.MusicHelper;
import com.example.musicplayertest.util.ScanMusicUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "MainActivity";


    private RecyclerView recyclerView;
    private TextView songInfo;
    private Button lastBtn;
    private Button playOrPauseBtn;
    private Button stopBtn;
    private Button nextBtn;
    private SeekBar seekBar;

    private MusicHelper musicHelper;

    private List<Song> songList;

    private SongAdapter songAdapter;

    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        songList = ScanMusicUtils.getMusicData(this);
        initView();
        initData();
        initListener();

    }

    private void initData() {
        musicHelper = new MusicHelper(seekBar,songInfo);

        songAdapter = new SongAdapter(songList);
        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void clickSong(Song song, int position) {
                mPosition = position;
                play(song,true);
            }
        });
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_btn:
                play(songList.get(mPosition),false);
                break;
            case R.id.stop_btn:
                stop();
                break;
            case R.id.last_btn:
                last();
                break;
            case R.id.next_btn:
                next();
                break;
        }
    }




    private void initView() {

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        songInfo = (TextView)findViewById(R.id.song_info);
        lastBtn = (Button)findViewById(R.id.last_btn);
        playOrPauseBtn = (Button)findViewById(R.id.start_btn);
        stopBtn = (Button)findViewById(R.id.stop_btn);
        nextBtn = (Button)findViewById(R.id.next_btn);
        seekBar = (SeekBar)findViewById(R.id.seek_bar);

    }

    private void initListener() {
        lastBtn.setOnClickListener(this);
        playOrPauseBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        musicHelper.setOnCompletionListener(new MusicHelper.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG,"next()");
                next();
            }
        });
    }

    public void play(Song song,boolean isResetPlayer){
        if (!TextUtils.isEmpty(song.getPath())) {

            if (!isResetPlayer && musicHelper.isPlaying()){
                playOrPauseBtn.setText(R.string.start_btn);
                musicHelper.pause();
            }else {
                musicHelper.play(song,isResetPlayer);
                playOrPauseBtn.setText(R.string.pause_btn);
                for (int i = 0;i<songList.size();i++){
                    songList.get(i).setPlaying(mPosition==i);
                }
                songAdapter.notifyDataSetChanged();
            }
        }else {
            Toast.makeText(this,"播放地址无效",Toast.LENGTH_SHORT).show();
        }
    }

    public void next(){
        mPosition++;
        if (mPosition>=songList.size()){
            mPosition = 0;
        }
        play(songList.get(mPosition),true);
    }

    public void last(){
        mPosition--;
        if (mPosition<0){
            mPosition = songList.size() - 1;
        }
        play(songList.get(mPosition),true);
    }

    public void stop(){
        playOrPauseBtn.setText(R.string.start_btn);
        musicHelper.stop();
    }


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        musicHelper.destroy();
        super.onDestroy();
    }

    private void checkPermissions() {

        String[] allPermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_FORMAT_FILESYSTEMS,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        };

        int targetVersion = 0;

        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(),0);
            targetVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (targetVersion >= Build.VERSION_CODES.M){

                boolean isAllGranted = checkAllPermissionGranted(allPermissions);
                if (isAllGranted){
                    Log.d(TAG,"所有权限已授权");
                    return;
                }
                ActivityCompat.requestPermissions(this,allPermissions,1);

            }
        }


    }

    private boolean checkAllPermissionGranted(String[] allPermissions) {
        for (String p : allPermissions){
            if (ContextCompat.checkSelfPermission(this,p)
            != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == 1){
            boolean isAllGranted = true;
            for (int result:grantResults){
                if (result!=PackageManager.PERMISSION_GRANTED){
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted){
                Log.d(TAG,"所有权限已经授权");
            }else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("警告")
                        .setMessage("部分权限未授权")
                        .setCancelable(false)
                        .setPositiveButton("前往授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package",getPackageName(),null));
                                startActivity(intent);
                            }
                        })
                        .show();

            }
        }
    }


}