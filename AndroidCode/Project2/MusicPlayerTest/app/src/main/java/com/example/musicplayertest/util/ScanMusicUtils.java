package com.example.musicplayertest.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayertest.bean.Song;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ScanMusicUtils {

    private static final String TAG = "ScanMusicUtils";

    public static List<Song> getMusicData(Context context){
        Log.d(TAG,"搜索");
        List<Song> songList = new ArrayList<>();
//        String selection = MediaStore.Audio.Media.DATA + " like ?";
//        String[] selectionArgs = new String[]{"%Music%"};
        String selection = null;
        String[] selectionArgs = null;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.IS_MUSIC
        );
        Log.d(TAG,"cursor长度：" + cursor.getCount());
        if (cursor!=null){
            while (cursor.moveToNext()){
                Song song = new Song();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                song.setDuration(duration);
                song.setName(name);
                song.setSinger(singer);
                song.setSize(size);
                song.setPath(path);

                if (song.getSize() > 1000*800){
                    String n = song.getName();
                    if (name!=null&& name.contains("-")){
                        String[] str = name.split("-");
                        song.setName(str[1]);
                        song.setSinger(str[0]);
                    }
                }

                songList.add(song);
            }
            cursor.close();
        }
        return songList;
    }


    public static String formatTime(int time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(time);
    }

}
