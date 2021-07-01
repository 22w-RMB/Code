package cn.flyaudio.module_music.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.base.BaseListChangedCallBack;
import cn.flyaudio.module_music.bean.Album;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicItemAlbumBinding;
import cn.flyaudio.module_music.databinding.FlyMusicItemAlbumBinding;
import cn.flyaudio.module_music.module.PlayManager;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private ObservableArrayList<Album> albums;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlyMusicItemAlbumBinding binding = FlyMusicItemAlbumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.bind(albums.get(position));
    }

    @Override
    public int getItemCount() {
        return albums == null ? 0 : albums.size();
    }

    public void setData(ObservableArrayList<Album> albums) {
        if (albums != null) {
            this.albums = albums;
        }
        this.albums.addOnListChangedCallback(new BaseListChangedCallBack<AlbumAdapter, Album>(this));
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        private final FlyMusicItemAlbumBinding binding;

        public AlbumViewHolder(FlyMusicItemAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Album album) {
            Song song = PlayManager.getInstance().getCurrentSong();
            if (song != null && !TextUtils.isEmpty(song.getAlbumName())) {
                album.setIsPlay(song.getAlbumName().equals(album.getName()));
            }
            binding.setAlbum(album);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClickItem(album);
                    }
                }
            });
            if (isSelect(album.getName())) {
                binding.tvAlbum.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_select_textview_color));
                binding.tvDeiver.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_select_textview_color));
                binding.tvSinger.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_select_textview_color));
            } else {
                binding.tvAlbum.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_unselect_textview_color));
                binding.tvDeiver.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_unselect_textview_color));
                binding.tvSinger.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_unselect_textview_color));
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
        void onClickItem(Album album);
    }


    public int getSelectIndex() {
        int selectIndex = -1;
        Song currentSong = PlayManager.getInstance().getCurrentSong();
        if (currentSong != null && albums != null && !TextUtils.isEmpty(currentSong.getAlbumName())) {
            for (int i = 0; i < albums.size(); i++) {
                String name = albums.get(i).getName();
                if (currentSong.getAlbumName().equals(name)) {
                    selectIndex = i;
                    break;
                }
            }
        }
        return selectIndex;
    }
}
