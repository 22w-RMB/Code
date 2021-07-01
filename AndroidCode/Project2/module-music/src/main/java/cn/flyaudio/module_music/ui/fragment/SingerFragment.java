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
import cn.flyaudio.module_music.bean.Singer;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicFragmentMusicListBinding;
import cn.flyaudio.module_music.event.UpdatePlayInfoEvent;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.ui.adapter.ArtistAdapter;
import cn.flyaudio.module_music.ui.adapter.SongsAdapter;
import cn.flyaudio.module_music.ui.viewmodel.SingerViewModel;


/**
 * Created by yaoyuqing
 */
public class SingerFragment extends BaseMusicFragment<FlyMusicFragmentMusicListBinding, SingerViewModel> implements ArtistAdapter.OnItemClickListener {

    private ArtistAdapter artistAdapter;


    public static SingerFragment getInstance() {
        SingerFragment songsFragment = new SingerFragment();
        return songsFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fly_music_fragment_music_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

//    public void setSaveData(List<Singer> allSinger) {
//        if (allSinger != null && allSinger.size() > 0) {
//            viewModel.singers.clear();
//            viewModel.singers.addAll(allSinger);
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
    public void initData() {
        artistAdapter = new ArtistAdapter();
        viewModel.singers.clear();
        viewModel.singers.addAll(DataManager.getInstance().getAllSinger());
        artistAdapter.setData(viewModel.singers);
        artistAdapter.setOnItemClickListener(this);
        binding.itemRecyclerview.setAdapter(artistAdapter);

        if (artistAdapter.getSelectIndex() != INVALID_INDEX) {
            binding.itemRecyclerview.smoothScrollToPosition(artistAdapter.getSelectIndex());
        }
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

    @Override
    public void onClickItem(Singer album) {
        ObservableArrayList<Song> allSongs = album.getList();
        SongsAdapter songsAdapter = new SongsAdapter(getActivity(), allSongs);
        binding.itemRecyclerview.setAdapter(songsAdapter);

    }


    // 更新选中的item
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSongDataEvent(UpdatePlayInfoEvent event) {
        RecyclerView.Adapter adapter = binding.itemRecyclerview.getAdapter();
        adapter.notifyDataSetChanged();
        if (adapter instanceof ArtistAdapter) {
            int selectIndex = ((ArtistAdapter) adapter).getSelectIndex();
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
