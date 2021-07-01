package cn.flyaudio.module_music.module;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.ObservableArrayList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.flyaudio.module_music.bean.Album;
import cn.flyaudio.module_music.bean.Folder;
import cn.flyaudio.module_music.bean.Singer;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.event.DeleteSongEvent;
import cn.flyaudio.module_music.event.RenameSongEvent;
import cn.flyaudio.module_music.event.UpdateSongDataEvent;
import cn.flyaudio.module_music.util.SharedPreferencesUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class DataManager {
    private static volatile DataManager mInstance;
    private ObservableArrayList<Song> allSong;
    List<Album> allAlbum;
    List<Folder> allFolder;
    List<Singer> allSinger;

    List<Song> rebuildSongList = new ArrayList<>();
    List<Album> rebuildAlbumList = new ArrayList<>();
    List<Folder> rebuildFolderList = new ArrayList<>();
    List<Singer> rebuildSingerList = new ArrayList<>();

    /**
     * 重新构建列表(过滤无效的音乐文件)
     */
    public void rebuildSongList() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                rebuildSongList.clear();
                rebuildFolderList.clear();
                rebuildAlbumList.clear();
                rebuildSingerList.clear();
                if (allSong != null && allSong.size() > 0) {
                    for (Song song : allSong) {
                        if (!TextUtils.isEmpty(song.getPath())) {
                            if (new File(song.getPath()).exists()) {
                                rebuildSongList.add(song);
                                addToAlbum(song);
                                addToFolder(song);
                                addToSinger(song);
                            }
                        }
                    }
                }
                setAllData(rebuildSongList, rebuildFolderList, rebuildSingerList, rebuildAlbumList);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });
    }


    private void addToAlbum(Song song) {
        if (song == null || TextUtils.isEmpty(song.getAlbumName())) {
            return;
        }
        int targetPosition = -1;
        for (int i = 0; i < rebuildAlbumList.size(); i++) {
            if (song.getAlbumName().equals(rebuildAlbumList.get(i).getName())) {
                targetPosition = i;
                break;
            }
        }
        if (targetPosition != -1) {
            rebuildAlbumList.get(targetPosition).getList().add(song);
        } else {
            Album album = new Album();
            ObservableArrayList<Song> list = new ObservableArrayList<>();
            list.add(song);
            album.setSinger(song.getSinger());
            album.setName(song.getAlbumName());
            album.setList(list);
            rebuildAlbumList.add(album);
        }
    }

    private void addToFolder(Song song) {
        if (song == null || TextUtils.isEmpty(song.getParentPath())) {
            return;
        }
        int targetPosition = -1;
        for (int i = 0; i < rebuildFolderList.size(); i++) {
            if (song.getParentPath().equals(rebuildFolderList.get(i).getPath())) {
                targetPosition = i;
                break;
            }
        }
        if (targetPosition != -1) {
            rebuildFolderList.get(targetPosition).getList().add(song);
        } else {
            Folder folder = new Folder();
            ObservableArrayList<Song> list = new ObservableArrayList<>();
            list.add(song);
            folder.setPath(song.getParentPath());
            folder.setName(new File(song.getParentPath()).getName());
            folder.setList(list);
            rebuildFolderList.add(folder);
        }
    }

    private void addToSinger(Song song) {
        if (song == null || TextUtils.isEmpty(song.getSinger())) {
            return;
        }
        int targetPosition = -1;
        for (int i = 0; i < rebuildSingerList.size(); i++) {
            if (song.getSinger().equals(rebuildSingerList.get(i).getName())) {
                targetPosition = i;
                break;
            }
        }
        if (targetPosition != -1) {
            rebuildSingerList.get(targetPosition).getList().add(song);
        } else {
            Singer singer = new Singer();
            ObservableArrayList<Song> list = new ObservableArrayList<>();
            list.add(song);
            singer.setName(song.getSinger());
            singer.setList(list);
            rebuildSingerList.add(singer);
        }
    }


    private DataManager() {
        allSong = new ObservableArrayList<>();
        allFolder = new ArrayList<>();
        allSinger = new ArrayList<>();
        allAlbum = new ArrayList<>();
    }

    public static DataManager getInstance() {
        if (mInstance == null) {
            synchronized (DataManager.class) {
                if (mInstance == null) {
                    mInstance = new DataManager();
                }
            }
        }
        return mInstance;
    }


    public void init(Context context) {
        List<Song> allSong = SharedPreferencesUtil.getInstance().getAllSong();
        List<Folder> allFolder = SharedPreferencesUtil.getInstance().getAllFolder();
        List<Album> allAlbum = SharedPreferencesUtil.getInstance().getAllAlbum();
        List<Singer> allSinger = SharedPreferencesUtil.getInstance().getAllSinger();
        if (allSong != null && allSong.size() > 0) {
            this.allSong.addAll(allSong);
        }
        if (allFolder != null && allFolder.size() > 0) {
            this.allFolder.addAll(allFolder);
        }
        if (allAlbum != null && allAlbum.size() > 0) {
            this.allAlbum.addAll(allAlbum);
        }
        if (allSinger != null && allSinger.size() > 0) {
            this.allSinger.addAll(allSinger);
        }
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public ObservableArrayList<Song> getAllSong() {
        return allSong;
    }

    public List<Album> getAllAlbum() {
        return allAlbum;
    }

    public List<Folder> getAllFolder() {
        return allFolder;
    }

    public List<Singer> getAllSinger() {
        return allSinger;
    }


    public void setAllData(List<Song> list, List<Folder> folderList, List<Singer> singerList, List<Album> albumList) {
        this.allSong.clear();
        this.allFolder.clear();
        this.allSinger.clear();
        this.allAlbum.clear();

        this.allSong.addAll(list);
        this.allFolder.addAll(folderList);
        this.allSinger.addAll(singerList);
        this.allAlbum.addAll(albumList);
        // 通知数据发生改变
        EventBus.getDefault().post(new UpdateSongDataEvent());
        saveAllSongToSharePreference();
    }


    /**
     * 处理删除某一首歌
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void deleteSong(DeleteSongEvent event) {
        if (event == null || event.getSong() == null || TextUtils.isEmpty(event.getSong().getPath())) {
            return;
        }
        Song deleteSong = event.getSong();
        File file = new File(deleteSong.getPath());
        if (file.exists()) {
            file.delete();
        }
        List<Album> deleteAlbums = new ArrayList<>();
        List<Folder> deleteFolders = new ArrayList<>();
        List<Singer> deleteSingers = new ArrayList<>();
        List<Song> deleteSongs = new ArrayList<>();
        if (allSong.size() > 0) {
            for (Song song : allSong) {
                if (deleteSong.getPath().equals(song.getPath())) {
                    deleteSongs.add(song);
                    break;
                }
            }
        }
        if (deleteSongs.size() > 0) {
            allSong.removeAll(deleteSongs);
            deleteSongs.clear();
        }

        if (!TextUtils.isEmpty(deleteSong.getAlbumName())) {
            for (Album album : allAlbum) {
                if (deleteSong.getName().equals(album.getName())) {
                    ObservableArrayList<Song> list = album.getList();
                    for (Song song : list) {
                        if (deleteSong.getPath().equals(song.getPath())) {
                            deleteSongs.add(song);
                            break;
                        }
                    }
                    if (deleteSongs.size() > 0) {
                        album.getList().removeAll(deleteSongs);
                        if (album.getList().size() == 0) {
                            deleteAlbums.add(album);
                        }
                    }
                    break;
                }
            }
            if (deleteAlbums.size() > 0) {
                allAlbum.removeAll(deleteAlbums);
            }
        }


        if (!TextUtils.isEmpty(deleteSong.getSinger())) {
            for (Singer singer : allSinger) {
                if (deleteSong.getSinger().equals(singer.getName())) {
                    ObservableArrayList<Song> list = singer.getList();
                    for (Song song : list) {
                        if (deleteSong.getPath().equals(song.getPath())) {
                            deleteSongs.add(song);
                            break;
                        }
                    }
                    if (deleteSongs.size() > 0) {
                        singer.getList().removeAll(deleteSongs);
                        if (singer.getList().size() == 0) {
                            deleteSingers.add(singer);
                        }
                    }
                    break;
                }
            }
            if (deleteSingers.size() > 0) {
                allSinger.removeAll(deleteSingers);
            }
        }

        if (!TextUtils.isEmpty(deleteSong.getParentPath())) {
            for (Folder folder : allFolder) {
                if (deleteSong.getParentPath().equals(folder.getPath())) {
                    ObservableArrayList<Song> list = folder.getList();
                    for (Song song : list) {
                        if (deleteSong.getPath().equals(song.getPath())) {
                            deleteSongs.add(song);
                            break;
                        }
                    }
                    if (deleteSongs.size() > 0) {
                        folder.getList().removeAll(deleteSongs);
                        if (folder.getList().size() == 0) {
                            deleteFolders.add(folder);
                        }
                    }
                    break;
                }
            }
            if (deleteFolders.size() > 0) {
                allFolder.removeAll(deleteFolders);
            }
        }
        // 通知数据发生改变
        EventBus.getDefault().post(new UpdateSongDataEvent());
        saveAllSongToSharePreference();
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

        // 所有歌曲
        for (int i = 0; i < allSong.size(); i++) {
            Song song = allSong.get(i);
            if (renameSong.getPath().equals(song.getPath())) {
                allSong.get(i).setPath(newPath);
                allSong.get(i).setName(newName);
                break;
            }
        }

        //专辑
        if (!TextUtils.isEmpty(renameSong.getAlbumName())) {
            for (int i = 0; i < allAlbum.size(); i++) {
                Album album = allAlbum.get(i);
                if (renameSong.getAlbumName().equals(album.getName())) {
                    ObservableArrayList<Song> list = album.getList();
                    for (int j = 0; j < list.size(); j++) {
                        Song song = list.get(j);
                        if (renameSong.getPath().equals(song.getPath())) {
                            allAlbum.get(i).getList().get(j).setPath(newPath);
                            allAlbum.get(i).getList().get(j).setName(newName);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        // 歌手
        if (!TextUtils.isEmpty(renameSong.getSinger())) {
            for (int i = 0; i < allSinger.size(); i++) {
                Singer singer = allSinger.get(i);
                if (renameSong.getSinger().equals(singer.getName())) {
                    ObservableArrayList<Song> list = singer.getList();
                    for (int j = 0; j < list.size(); j++) {
                        Song song = list.get(j);
                        if (renameSong.getPath().equals(song.getPath())) {
                            allSinger.get(i).getList().get(j).setPath(newPath);
                            allSinger.get(i).getList().get(j).setName(newName);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        // 文件夹
        if (!TextUtils.isEmpty(renameSong.getParentPath())) {
            for (int i = 0; i < allFolder.size(); i++) {
                Folder folder = allFolder.get(i);
                if (renameSong.getParentPath().equals(folder.getPath())) {
                    ObservableArrayList<Song> list = folder.getList();
                    for (int j = 0; j < list.size(); j++) {
                        Song song = list.get(j);
                        if (renameSong.getPath().equals(song.getPath())) {
                            allFolder.get(i).getList().get(j).setPath(newPath);
                            allFolder.get(i).getList().get(j).setName(newName);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        saveAllSongToSharePreference();
    }


    private void saveAllSongToSharePreference() {
        SharedPreferencesUtil.getInstance().saveAllSong(allSong);
        SharedPreferencesUtil.getInstance().saveAllFolder(allFolder);
        SharedPreferencesUtil.getInstance().saveAllSinger(allSinger);
        SharedPreferencesUtil.getInstance().saveAllAlbum(allAlbum);
    }

    public void saveCurrentSong(Song song) {
        SharedPreferencesUtil.getInstance().saveCurrentSong(song);
    }

    public Song getSaveSong() {
        return SharedPreferencesUtil.getInstance().getSaveSong();
    }

    /**
     * 保存当前播放进度
     *
     * @param currentPosition
     */
    public void saveCurrentProgress(int currentPosition) {
        SharedPreferencesUtil.getInstance().saveCurrentProgress(currentPosition);
    }

    public int getSaveProgress() {
        return SharedPreferencesUtil.getInstance().getSaveProgress();
    }


    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
