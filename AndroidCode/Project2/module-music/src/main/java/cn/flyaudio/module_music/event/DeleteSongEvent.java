package cn.flyaudio.module_music.event;

import cn.flyaudio.module_music.bean.Song;

public class DeleteSongEvent {
    public DeleteSongEvent(Song song) {
        this.song = song;
    }

    Song song;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}

