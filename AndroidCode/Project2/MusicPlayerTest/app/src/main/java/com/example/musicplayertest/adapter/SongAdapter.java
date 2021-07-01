package com.example.musicplayertest.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayertest.R;
import com.example.musicplayertest.bean.Song;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private List<Song> songList;
    private OnItemClickListener onItemClickListener;


    public SongAdapter(List<Song> songList) {
        this.songList = songList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View v;
        TextView songName;
        ImageView songImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.v = itemView;
            this.songImage = itemView.findViewById(R.id.song_image);
            this.songName = itemView.findViewById(R.id.song_name);
        }
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        ViewHolder holder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                onItemClickListener.clickSong(songList.get(position),position);
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SongAdapter.ViewHolder holder, int position) {
        Song s = songList.get(position);
        holder.songName.setText(s.getName());
//        holder.songImage.setImageBitmap(BitmapFactory.decodeFile(s.getImagePath()));


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public interface OnItemClickListener{
        void clickSong(Song song,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
