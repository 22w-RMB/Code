package cn.flyaudio.module_music.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import cn.flyaudio.module_music.BR;
import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.base.BaseMusicFragment;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicFragmentMusicListBinding;
import cn.flyaudio.module_music.event.RenameSongEvent;
import cn.flyaudio.module_music.event.UpdatePlayInfoEvent;
import cn.flyaudio.module_music.event.UpdateSongDataEvent;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.ui.adapter.SongsAdapter;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.KLog;


/**
 * Created by yaoyuqing
 */
public class AllSongsFragment extends BaseMusicFragment<FlyMusicFragmentMusicListBinding, BaseViewModel> {
    private static final String TAG = "SongsFragment";
    private final ObservableArrayList<Song> allSongs = new ObservableArrayList<>();
    private SongsAdapter songsAdapter;

    public static AllSongsFragment getInstance() {
        AllSongsFragment songsFragment = new AllSongsFragment();
        return songsFragment;
    }

    @Override
    public void initParam() {
        super.initParam();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fly_music_fragment_music_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        initPlay();
    }

    private void initPlay() {
        allSongs.clear();
        allSongs.addAll(DataManager.getInstance().getAllSong());
        songsAdapter = new SongsAdapter(getActivity(), allSongs);
        binding.itemRecyclerview.setAdapter(songsAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(UpdateSongDataEvent event) {
        ObservableArrayList<Song> songList = DataManager.getInstance().getAllSong();
        allSongs.clear();
        allSongs.addAll(songList);
        songsAdapter.notifyDataSetChanged();
        KLog.d("notifyDataSetChanged");
    }

    // 更新选中的item
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSongDataEvent(UpdatePlayInfoEvent event) {
        songsAdapter.notifyDataSetChanged();
        int selectIndex = songsAdapter.getSelectIndex();
        if (selectIndex != INVALID_INDEX) {
            binding.itemRecyclerview.smoothScrollToPosition(selectIndex);
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
        if (renameSong == null ||TextUtils.isEmpty(renameSong.getPath()) ||
                TextUtils.isEmpty(newName) || TextUtils.isEmpty(newPath) || !new File(newPath).exists()) {
            return;
        }
        // 所有歌曲
        for (int i = 0; i < allSongs.size(); i++) {
            Song song = allSongs.get(i);
            if (renameSong.getPath().equals(song.getPath())) {
                allSongs.get(i).setPath(newPath);
                allSongs.get(i).setName(newName);
                break;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean isDetail() {
        return songsAdapter.getIsEdit();
    }

    @Override
    public void onBack() {
        songsAdapter.setIsEdit(false);
    }

}
