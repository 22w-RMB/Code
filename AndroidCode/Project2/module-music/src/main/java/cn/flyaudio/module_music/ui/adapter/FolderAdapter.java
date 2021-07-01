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
import cn.flyaudio.module_music.bean.Folder;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicItemFolderBinding;
import cn.flyaudio.module_music.databinding.FlyMusicItemFolderBinding;
import cn.flyaudio.module_music.module.PlayManager;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private ObservableArrayList<Folder> folders;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlyMusicItemFolderBinding binding = FlyMusicItemFolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FolderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        holder.bind(folders.get(position));
    }

    @Override
    public int getItemCount() {
        return folders == null ? 0 : folders.size();
    }

    public void setData(ObservableArrayList<Folder> Folders) {
        if (Folders != null) {
            this.folders = Folders;
        }
        this.folders.addOnListChangedCallback(new BaseListChangedCallBack<FolderAdapter, Folder>(this));
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {
        private final FlyMusicItemFolderBinding binding;

        public FolderViewHolder(FlyMusicItemFolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Folder folder) {
            binding.setModel(folder);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClickItem(folder);
                    }
                }
            });
            if (isSelect(folder.getPath())) {
                binding.tvFolder.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_select_textview_color));
                binding.tvDeiver.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_select_textview_color));
                binding.tvPath.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_select_textview_color));
            } else {
                binding.tvFolder.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_unselect_textview_color));
                binding.tvDeiver.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_unselect_textview_color));
                binding.tvPath.setTextColor(binding.getRoot().getResources().getColor(R.color.fly_music_unselect_textview_color));
            }
        }
    }


    private boolean isSelect(String path) {
        if (!TextUtils.isEmpty(path) && PlayManager.getInstance().getCurrentSong() != null) {
            return path.equals(PlayManager.getInstance().getCurrentSong().getParentPath());
        }
        return false;
    }

    public interface OnItemClickListener {
        void onClickItem(Folder Folder);
    }


    public int getSelectIndex() {
        int selectIndex = -1;
        Song currentSong = PlayManager.getInstance().getCurrentSong();
        if (currentSong != null && folders != null && !TextUtils.isEmpty(currentSong.getParentPath())) {
            for (int i = 0; i < folders.size(); i++) {
                String path = folders.get(i).getPath();
                if (currentSong.getParentPath().equals(path)) {
                    selectIndex = i;
                    break;
                }
            }
        }
        return selectIndex;
    }
}
