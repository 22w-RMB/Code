package cn.flyaudio.module_music.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.flyaudio.module_music.bean.Album;
import cn.flyaudio.module_music.bean.Folder;
import cn.flyaudio.module_music.bean.Singer;
import cn.flyaudio.module_music.bean.Song;

public class SharedPreferencesUtil {
    //文件名称为config
    private static final String PREFERENCE_NAME = "cn_flyaudio_music_sp";

    private SharedPreferences sharedPreferences;
    private Context context;


    private static volatile SharedPreferencesUtil mInstance;

    private SharedPreferencesUtil() {
    }

    public static SharedPreferencesUtil getInstance() {
        if (mInstance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreferencesUtil();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        this.context = context;
    }


    private static final String ALL_SONG = "all_Song";
    private static final String ALL_FOLDER = "all_Folder";
    private static final String ALL_SINGER = "all_Singer";
    private static final String ALL_ALBUM = "all_Album";
    private static final String CURRENT_SONG = "current_song";
    private static final String CURRENT_PROGRESS = "current_progress";

    public void saveCurrentSong(Song song) {
        Gson gson = new Gson();
        putString(CURRENT_SONG, gson.toJson(song));
    }

    public Song getSaveSong() {
        String string = getString(CURRENT_SONG, "");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        Gson gson = new Gson();
        Song song = gson.fromJson(string, Song.class);
        return song;
    }

    public void saveCurrentProgress(int progress) {
        putInt(CURRENT_PROGRESS, progress);
    }

    public int getSaveProgress() {
        return getInt(CURRENT_PROGRESS, -1);
    }

    public void saveAllSong(List<Song> allSong) {
        Gson gson = new Gson();
        String allSongStr = gson.toJson(allSong);
        putString(ALL_SONG, allSongStr);
    }

    public List<Song> getAllSong() {
        String allSongStr = getString(ALL_SONG, "");
        if (TextUtils.isEmpty(allSongStr)) {
            return null;
        }
        Gson gson = new Gson();
        List<Song> list = gson.fromJson(allSongStr, new TypeToken<List<Song>>() {
        }.getType());
        return list;
    }


    public void saveAllFolder(List<Folder> allFolder) {
        Gson gson = new Gson();
        String str = gson.toJson(allFolder);
        putString(ALL_FOLDER, str);
    }

    public List<Folder> getAllFolder() {
        String str = getString(ALL_FOLDER, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Gson gson = new Gson();
        List<Folder> list = gson.fromJson(str, new TypeToken<List<Folder>>() {
        }.getType());
        return list;
    }

    public void saveAllSinger(List<Singer> allSinger) {
        Gson gson = new Gson();
        String str = gson.toJson(allSinger);
        putString(ALL_SINGER, str);
    }

    public List<Singer> getAllSinger() {
        String str = getString(ALL_SINGER, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Gson gson = new Gson();
        List<Singer> list = gson.fromJson(str, new TypeToken<List<Singer>>() {
        }.getType());
        return list;
    }


    public void saveAllAlbum(List<Album> allAlbum) {
        Gson gson = new Gson();
        String str = gson.toJson(allAlbum);
        putString(ALL_ALBUM, str);
    }

    public List<Album> getAllAlbum() {
        String str = getString(ALL_ALBUM, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Gson gson = new Gson();
        List<Album> list = gson.fromJson(str, new TypeToken<List<Album>>() {
        }.getType());
        return list;
    }

    /**
     * 写入Boolean变量至sharedPreferences中
     *
     * @param key   存储节点名称
     * @param value 存储节点的值
     */
    public void putBoolean(String key, boolean value) {
        //(存储节点文件名称，读写方式)
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 读取boolean标识从sharedPreferences中
     *
     * @param key   存储节点名称
     * @param value 没有此节点的默认值
     * @return 默认值或者此节点读取到的结果
     */
    public boolean getBoolean(String key, boolean value) {
        //(存储节点文件名称,读写方式)
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, value);
    }

    /**
     * 写入String变量至sharedPreferences中
     *
     * @param key   存储节点名称
     * @param value 存储节点的值String
     */
    public void putString(String key, String value) {
        //存储节点文件的名称，读写方式
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 读取String标识从sharedPreferences中
     *
     * @param key      存储节点名称
     * @param defValue 没有此节点的默认值
     * @return 返回默认值或者此节点读取到的结果
     */
    public String getString(String key, String defValue) {
        //存储节点文件的名称，读写方式
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 写入int变量至sharedPreferences中
     *
     * @param key   存储节点名称
     * @param value 存储节点的值String
     */
    public void putInt(String key, int value) {
        //存储节点文件的名称，读写方式
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 读取int标识从sharedPreferences中
     *
     * @param key      存储节点名称
     * @param defValue 没有此节点的默认值
     * @return 返回默认值或者此节点读取到的结果
     */
    public int getInt(String key, int defValue) {
        //存储节点文件的名称，读写方式
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * 从sharedPreferences中移除指定节点
     *
     * @param key 需要移除节点的名称
     */
    public void remove(String key) {
        //存储节点文件的名称，读写方式
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().remove(key).commit();
    }


}
