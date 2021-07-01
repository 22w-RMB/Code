package cn.flyaudio.module_music.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.flyaudio.module_music.BR;
import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.base.BaseMusicFragment;
import cn.flyaudio.module_music.bean.Album;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicFragmentMusicListBinding;
import cn.flyaudio.module_music.event.UpdatePlayInfoEvent;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.ui.adapter.AlbumAdapter;
import cn.flyaudio.module_music.ui.adapter.SongsAdapter;
import cn.flyaudio.module_music.ui.viewmodel.AlbumViewModel;


/**
 * Created by yaoyuqing
 */
public class AlbumFragment extends BaseMusicFragment<FlyMusicFragmentMusicListBinding, AlbumViewModel> implements AlbumAdapter.OnItemClickListener {

    private AlbumAdapter albumAdapter;

    public static AlbumFragment getInstance() {
        AlbumFragment songsFragment = new AlbumFragment();
        return songsFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fly_music_fragment_music_list;
    }

//    /**
//     * 设置持久化数据
//     *
//     * @param allAlbum
//     */
//    public void setSaveData(List<Album> allAlbum) {
//        if (allAlbum != null && allAlbum.size() > 0) {
//            viewModel.albums.clear();
//            viewModel.albums.addAll(allAlbum);
//        }
//    }

    @Override
    public void initParam() {
        super.initParam();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        albumAdapter = new AlbumAdapter();

        viewModel.albums.clear();
        viewModel.albums.addAll(DataManager.getInstance().getAllAlbum());

        albumAdapter.setData(viewModel.albums);
        albumAdapter.setOnItemClickListener(this);
        binding.itemRecyclerview.setAdapter(albumAdapter);
        if (albumAdapter.getSelectIndex() != INVALID_INDEX) {
            binding.itemRecyclerview.smoothScrollToPosition(albumAdapter.getSelectIndex());
        }

    }


    @Override
    public void onClickItem(Album album) {
        ObservableArrayList<Song> allSongs = album.getList();
        SongsAdapter songsAdapter = new SongsAdapter(getActivity(), allSongs);
        binding.itemRecyclerview.setAdapter(songsAdapter);
    }

    @Override
    public boolean isDetail() {
        return binding.itemRecyclerview.getAdapter() instanceof SongsAdapter;
    }

    @Override
    public void onBack() {
        RecyclerView.Adapter adapter = binding.itemRecyclerview.getAdapter();
        if (adapter instanceof SongsAdapter) {
            if (((SongsAdapter) adapter).getIsEdit()) {
                ((SongsAdapter) adapter).setIsEdit(false);
            } else {
                initData();
            }
        }
    }

    // 更新选中的item
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSongDataEvent(UpdatePlayInfoEvent event) {
        RecyclerView.Adapter adapter = binding.itemRecyclerview.getAdapter();
        adapter.notifyDataSetChanged();
        if (adapter instanceof AlbumAdapter) {
            int selectIndex = ((AlbumAdapter) adapter).getSelectIndex();
            if (selectIndex != INVALID_INDEX) {
                binding.itemRecyclerview.smoothScrollToPosition(selectIndex);
            }
        } else if (adapter instanceof SongsAdapter) {
            int selectIndex = ((SongsAdapter) adapter).getSelectIndex();
            if (selectIndex != INVALID_INDEX) {
                binding.itemRecyclerview.smoothScrollToPosition(selectIndex);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
