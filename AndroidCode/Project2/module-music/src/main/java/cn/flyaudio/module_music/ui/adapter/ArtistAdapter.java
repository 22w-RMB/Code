package cn.flyaudio.module_music.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;


import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.base.BaseListChangedCallBack;
import cn.flyaudio.module_music.bean.Album;
import cn.flyaudio.module_music.bean.Singer;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicItemSingerBinding;
import cn.flyaudio.module_music.databinding.FlyMusicItemSingerBinding;
import cn.flyaudio.module_music.module.PlayManager;


public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.SingerViewHolder> {
    private ObservableArrayList<Singer> singers;
    ArtistAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ArtistAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ArtistAdapter.SingerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlyMusicItemSingerBinding binding = FlyMusicItemSingerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtistAdapter.SingerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ArtistAdapter.SingerViewHolder holder, int position) {
        holder.bind(singers.get(position));
    }

    @Override
    public int getItemCount() {
        return singers == null ? 0 : singers.size();
    }

    public void setData(ObservableArrayList<Singer> singers) {
        if (singers != null) {
            this.singers = singers;
        }
        this.singers.addOnListChangedCallback(new BaseListChangedCallBack<ArtistAdapter, Album>(this));
    }

    class SingerViewHolder extends RecyclerView.ViewHolder {
        private final FlyMusicItemSingerBinding binding;

        public SingerViewHolder(FlyMusicItemSingerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Singer singer
        ) {
            Song song = PlayManager.getInstance().getCurrentSong();
            if (song != null && !TextUtils.isEmpty(song.getAlbumName())) {
                singer.setIsPlay(song.getAlbumName().equals(singer.getName()));
            }
            binding.setSinger(singer);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClickItem(singer);
                    }
                }
            });
            if (isSelect(singer.getName())) {
                binding.tvTitle.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_select_textview_color));
                binding.tvGong.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_select_textview_color));
                binding.tvSize.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_select_textview_color));
                binding.tvSong.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_select_textview_color));
            } else {
                binding.tvTitle.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_unselect_textview_color));
                binding.tvGong.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_unselect_textview_color));
                binding.tvSize.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_unselect_textview_color));
                binding.tvSong.setTextColor(binding.tvTitle.getResources().getColor(R.color.fly_music_unselect_textview_color));
            }
        }
    }


    private boolean isSelect(String albumName) {
        if (!TextUtils.isEmpty(albumName) && PlayManager.getInstance().getCurrentSong() != null) {
            return albumName.equals(PlayManager.getInstance().getCurrentSong().getAlbumName());
        }
        return false;
    }

    public interface OnItemClickListener {
        void onClickItem(Singer album);
    }


    public int getSelectIndex() {
        int selectIndex = -1;
        Song currentSong = PlayManager.getInstance().getCurrentSong();
        if (currentSong != null && singers != null && !TextUtils.isEmpty(currentSong.getSinger())) {
            for (int i = 0; i < singers.size(); i++) {
                String name = singers.get(i).getName();
                if (currentSong.getSinger().equals(name)) {
                    selectIndex = i;
                    break;
                }
            }
        }
        return selectIndex;
    }

}
