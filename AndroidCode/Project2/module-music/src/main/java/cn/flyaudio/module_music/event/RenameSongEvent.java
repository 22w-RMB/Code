package cn.flyaudio.module_music.event;

import cn.flyaudio.module_music.bean.Song;

public class RenameSongEvent {
    String newPath;
    String newName;
    Song song;

    public RenameSongEvent(Song song, String newName, String newPath) {
        this.song = new Song();
        this.song.setName(song.getName());
        this.song.setPath(song.getPath());
        this.song.setAlbumName(song.getAlbumName());
        this.song.setDuration(song.getDuration());
        this.song.setExtName(song.getExtName());
        this.song.setId(song.getId());
        this.song.setImagePath(song.getPath());
        this.song.setImgPath(song.getImgPath());
        this.song.setParentPath(song.getParentPath());
        this.song.setPlaying(song.getPlaying());
        this.song.setSinger(song.getSinger());
        this.song.setSize(song.getSize());
        this.newName = newName;
        this.newPath = newPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}

