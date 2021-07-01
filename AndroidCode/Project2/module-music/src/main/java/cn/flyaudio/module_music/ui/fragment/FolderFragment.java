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
import cn.flyaudio.module_music.bean.Folder;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicFragmentMusicListBinding;
import cn.flyaudio.module_music.event.UpdatePlayInfoEvent;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.ui.adapter.FolderAdapter;
import cn.flyaudio.module_music.ui.adapter.SongsAdapter;
import cn.flyaudio.module_music.ui.viewmodel.FolderViewModel;
import me.goldze.mvvmhabit.utils.KLog;


/**
 * Created by yaoyuqing
 */
public class FolderFragment extends BaseMusicFragment<FlyMusicFragmentMusicListBinding, FolderViewModel> implements FolderAdapter.OnItemClickListener {

    private FolderAdapter folderAdapter;

    public static FolderFragment getInstance() {
        FolderFragment songsFragment = new FolderFragment();
        return songsFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fly_music_fragment_music_list;
    }

//    public void setSaveData(List<Folder> allFolder) {
//        if (allFolder != null && allFolder.size() > 0) {
//            viewModel.folders.clear();
//            viewModel.folders.addAll(allFolder);
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
        folderAdapter = new FolderAdapter();
        viewModel.folders.clear();
        viewModel.folders.addAll(DataManager.getInstance().getAllFolder());
        folderAdapter.setData(viewModel.folders);

        KLog.d("viewModel.folders " + viewModel.folders.size());
        folderAdapter.setOnItemClickListener(this);
        binding.itemRecyclerview.setAdapter(folderAdapter);
        if (folderAdapter.getSelectIndex() != INVALID_INDEX) {
            binding.itemRecyclerview.smoothScrollToPosition(folderAdapter.getSelectIndex());
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
    public void onClickItem(Folder folder) {
        ObservableArrayList<Song> allSongs = folder.getList();
        SongsAdapter songsAdapter = new SongsAdapter(getActivity(), allSongs);
        binding.itemRecyclerview.setAdapter(songsAdapter);

    }

    // 更新选中的item
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSongDataEvent(UpdatePlayInfoEvent event) {
        RecyclerView.Adapter adapter = binding.itemRecyclerview.getAdapter();
        adapter.notifyDataSetChanged();
        if (adapter instanceof FolderAdapter) {
            int selectIndex = ((FolderAdapter) adapter).getSelectIndex();
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
