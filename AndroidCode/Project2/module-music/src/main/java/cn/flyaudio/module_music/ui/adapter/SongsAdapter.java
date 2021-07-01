package cn.flyaudio.module_music.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.flyaudio.module_music.R;
import cn.flyaudio.module_music.base.BaseListChangedCallBack;
import cn.flyaudio.module_music.bean.Song;
import cn.flyaudio.module_music.databinding.FlyMusicItemSongBinding;
import cn.flyaudio.module_music.event.DeleteSongEvent;
import cn.flyaudio.module_music.event.RenameSongEvent;
import cn.flyaudio.module_music.module.PlayManager;
import cn.flyaudio.module_music.ui.dialog.MusicDialog;
import cn.flyaudio.module_music.ui.dialog.RenameDialog;
import cn.flyaudio.module_music.util.DocumentsUtils;
import me.goldze.mvvmhabit.utils.KLog;


public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {
    private ObservableArrayList<Song> data;
    private Context mContext;

    boolean isEdit = false;

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public SongsAdapter(Context context, ObservableArrayList<Song> data) {
        this.mContext = context;
        if (data != null) {
            this.data = data;
            this.data.addOnListChangedCallback(new BaseListChangedCallBack<SongsAdapter, Song>(this));
        }
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FlyMusicItemSongBinding binding = FlyMusicItemSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        holder.bind(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class SongsViewHolder extends RecyclerView.ViewHolder {

        private final FlyMusicItemSongBinding binding;

        public SongsViewHolder(FlyMusicItemSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Song song, int position) {
            binding.llRoot.setOnLongClickListener(v -> {
                isEdit = true;
                notifyDataSetChanged();
                return true;
            });

            binding.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEdit) {
                        PlayManager.getInstance().playList(data, position);
                    }
                }
            });
            binding.ivDel.setOnClickListener(v -> {
                showNormalDialog(song, position);
            });
            binding.ivRename.setOnClickListener(v -> {
                // TODO: 21-5-29 重命名
                showRenameDialog(song);
            });

            binding.setNumber(position + 1 + ".");
            binding.setSong(song);

            Song currentSong = PlayManager.getInstance().getCurrentSong();
            // 当前是正在播放的歌曲
            ColorStateList csl;
            if (currentSong != null && !TextUtils.isEmpty(currentSong.getPath())
                    && currentSong.getPath().equals(song.getPath())) {
                binding.ivDel.setBackgroundResource(R.drawable.fly_music_del_p);
                binding.ivRename.setBackgroundResource(R.drawable.fly_music_rename_p);
                binding.ivPlay.setVisibility(View.VISIBLE);
                csl = mContext.getResources().getColorStateList(R.color.fly_music_color_item_delete_p);
            } else {
                binding.ivDel.setBackgroundResource(R.drawable.fly_music_del_n);
                binding.ivRename.setBackgroundResource(R.drawable.fly_music_rename_n);
                binding.ivPlay.setVisibility(View.GONE);
                csl = mContext.getResources().getColorStateList(R.color.fly_music_color_item_delete_u);
            }
            binding.songTitle.setTextColor(csl);
            binding.tvNumber.setTextColor(csl);
            if (isEdit) {
                // 如果当前处于编辑状态，则不显示播放状态标识
                binding.ivPlay.setVisibility(View.GONE);
                binding.ivRename.setVisibility(View.VISIBLE);
                binding.ivDel.setVisibility(View.VISIBLE);
            } else {
                binding.ivDel.setVisibility(View.GONE);
                binding.ivRename.setVisibility(View.GONE);
            }
            binding.executePendingBindings();
        }
    }

    private void showRenameDialog(Song song) {
        RenameDialog dialog = new RenameDialog(mContext);
        dialog.setText(song.getName());
        dialog.setOnHandleListener(new RenameDialog.OnHandleListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String name) {
                String filePath = song.getPath();
                File src = new File(filePath);
                String prefix = filePath.substring(filePath.lastIndexOf(".") + 1);
                File des = new File(src.getParent(), name + "." + prefix);
                KLog.d("  name+prefix " + (name + prefix) + "onSure:src  " + src.getAbsolutePath() + "onSure:des  " + des.getAbsolutePath());
                boolean isSuccess = DocumentsUtils.renameTo(mContext, src, des);
                KLog.d("onSure: " + isSuccess);
                if (isSuccess) {
                    RenameSongEvent event = new RenameSongEvent(song, name, des.getAbsolutePath());
                    // 由于异步问题，需要将当前正在播放的歌曲先进行换名字,再通知列表的更新，否则，会出现列表先更新，当前正在播放的对象也会被更新，导致PlayManager.getInstance().reNameSong的逻辑无法处理
                    PlayManager.getInstance().reNameSong(event);
                    EventBus.getDefault().post(event);
                }
                isEdit = false;
                notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void showNormalDialog(Song song, int position) {
        MusicDialog dialog = new MusicDialog(mContext);
        dialog.setOnHandleListener(new MusicDialog.OnHandleListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure() {
                data.remove(position);
                isEdit = false;
                notifyDataSetChanged();

                Song currentPlaySong = PlayManager.getInstance().getCurrentSong();
                // 判断当前删除的歌曲是否是正在播放的歌曲，是，则播放下一曲
                if (currentPlaySong != null && !TextUtils.isEmpty(currentPlaySong.getPath())
                        && currentPlaySong.getPath().equals(song.getPath())) {
                    PlayManager.getInstance().next();
                }

                EventBus.getDefault().post(new DeleteSongEvent(song));
            }
        });
        dialog.show();
    }

    public int getSelectIndex() {
        int selectIndex = -1;
        Song currentSong = PlayManager.getInstance().getCurrentSong();
        if (currentSong != null && !TextUtils.isEmpty(currentSong.getAlbumName())) {
            for (int i = 0; i < data.size(); i++) {
                String name = data.get(i).getName();
                if (currentSong.getAlbumName().equals(name)) {
                    selectIndex = i;
                    break;
                }
            }
        }
        return selectIndex;
    }
}


