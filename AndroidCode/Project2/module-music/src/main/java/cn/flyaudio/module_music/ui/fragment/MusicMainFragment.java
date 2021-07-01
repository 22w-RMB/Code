package cn.flyaudio.module_music.ui.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.flyaudio.library_base.router.RouterActivityPath;
import cn.flyaudio.library_base.router.RouterFragmentPath;
import cn.flyaudio.module_music.BR;
import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.base.BaseMusicFragment;
import cn.flyaudio.module_music.databinding.FlyMusicFragmentMusicMainBinding;
import cn.flyaudio.module_music.event.SeekBarTrackEvent;
import cn.flyaudio.module_music.module.DataManager;
import cn.flyaudio.module_music.module.PlayManager;
import cn.flyaudio.module_music.ui.viewmodel.MusicMainViewModel;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;


/**
 * Created by yaoyuqing
 */
@Route(path = RouterFragmentPath.Music.MAIN)
public class MusicMainFragment extends BaseFragment<FlyMusicFragmentMusicMainBinding, MusicMainViewModel> {
    private List<BaseMusicFragment> fragments = new ArrayList<>();
    private DeviceReceiver deviceReceiver;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fly_music_fragment_music_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        DataManager.getInstance().init(getActivity());
        super.initParam();
        IntentFilter deviceFilter = new IntentFilter();
        // 挂载
        deviceFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
//        deviceFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        // 卸载
        deviceFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        deviceReceiver = new DeviceReceiver();
        deviceFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        getActivity().registerReceiver(deviceReceiver, deviceFilter);
        PlayManager.getInstance().init(getActivity());
    }


    class DeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_MEDIA_EJECT.equals(action) || Intent.ACTION_MEDIA_REMOVED.equals(action)) {
                deviceUnmounted();
            } else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                deviceMounted();
            }
        }
    }

    /**
     * U盘挂载
     */
    private void deviceMounted() {

    }

    /**
     * U盘卸载
     */
    private void deviceUnmounted() {
        // U盘卸载需要重新构建音乐列表
        DataManager.getInstance().rebuildSongList();
    }

    @Override
    public void initData() {
        initView();
        // 请求读写权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        ToastUtils.showLong("缺少存储权限，将会导致部分功能无法使用");
                    } else {

                    }
                });
    }

    private void initView() {
        AllSongsFragment songsFragment = AllSongsFragment.getInstance();
        SingerFragment artistFragment = SingerFragment.getInstance();
        AlbumFragment albumFragment = AlbumFragment.getInstance();
        FolderFragment folderFragment = FolderFragment.getInstance();

        fragments.add(songsFragment);
        fragments.add(artistFragment);
        fragments.add(albumFragment);
        fragments.add(folderFragment);

        binding.vp1.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
        binding.vp1.setOffscreenPageLimit(4);
        binding.vp1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModel.viewPagerChooseEvent.setValue(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewModel.viewPagerChooseEvent.setValue(0);
        viewModel.viewPagerChooseEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer currentPage) {
                KLog.d("onChanged: " + currentPage);
                binding.vp1.setCurrentItem(currentPage, false);
                binding.ivAll.setChecked(false);
                binding.ivAlbum.setChecked(false);
                binding.ivSinger.setChecked(false);
                binding.ivFolder.setChecked(false);
                switch (currentPage) {
                    case MusicMainViewModel.PAGE_ALL:
                        binding.ivAll.setChecked(true);
                        break;
                    case MusicMainViewModel.PAGE_ALBUM:
                        binding.ivAlbum.setChecked(true);
                        break;
                    case MusicMainViewModel.PAGE_SINGER:
                        binding.ivSinger.setChecked(true);
                        break;
                    case MusicMainViewModel.PAGE_FOLDER:
                        binding.ivFolder.setChecked(true);
                        break;
                }
            }
        });
        SeekBarChangeListener seekBarChangeListener = new SeekBarChangeListener();
        binding.seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            EventBus.getDefault().post(new SeekBarTrackEvent(SeekBarTrackEvent.EVENT_START));

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            SeekBarTrackEvent seekBarTrackEvent = new SeekBarTrackEvent(SeekBarTrackEvent.EVENT_STOP);
            seekBarTrackEvent.setCurrentProgress(seekBar.getProgress());
            seekBarTrackEvent.setMaxProgress(seekBar.getMax());
            EventBus.getDefault().post(seekBarTrackEvent);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<BaseMusicFragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<BaseMusicFragment> list) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayManager.getInstance().onDestroy();
        getActivity().unregisterReceiver(deviceReceiver);
        DataManager.getInstance().onDestroy();

    }

    @Override
    public boolean isBackPressed() {
        int currentItem = binding.vp1.getCurrentItem();
        return fragments.get(currentItem).onClickBack();
    }
}
