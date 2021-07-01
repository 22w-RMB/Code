package cn.flyaudio.module_music.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Arrays;

import cn.flyaudio.module_music.BR;

/**
 * Describe:
 * <p>歌曲实体模型</p>
 *
 * @author zhouhuan
 * @Date 2021/5/17
 */
public class Song extends BaseObservable {
    private String albumName;
    private String parentPath;
    private String extName;

    /**
     * 歌曲ID
     */
    private long id;
    /**
     * 专辑ID
     */
    private long albumId;
    /**
     * 专辑ID
     */
    private long artistId;
    /**
     * 歌曲名字
     */
    private String name;
    /**
     * 歌曲照片
     */
    private String imagePath;

    private byte[] imgPath;
    /**
     * 作家
     */
    private String singer;
    /**
     * 路径
     */
    private String path;
    /**
     * 时长
     */
    private int duration;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 是否正在播放
     */
    private Boolean isPlaying = false;


    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        notifyPropertyChanged(BR.imagePath);
    }

    @Bindable
    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
        notifyPropertyChanged(BR.singer);
    }

    @Bindable
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        notifyPropertyChanged(BR.path);
    }

    @Bindable
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
        notifyPropertyChanged(BR.size);
    }

    @Bindable
    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    @Bindable
    public byte[] getImgPath() {
        return imgPath;
    }

    public void setImgPath(byte[] imgPath) {
        this.imgPath = imgPath;
        notifyPropertyChanged(BR.imgPath);
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
        notifyPropertyChanged(BR.albumName);
    }

    @Bindable
    public String getAlbumName() {
        return albumName;
    }

    @Bindable
    public String getExtName() {
        return extName;
    }

    @Bindable
    public long getAlbumId() {
        return albumId;
    }

    @Bindable
    public long getArtistId() {
        return artistId;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
        notifyPropertyChanged(BR.parentPath);
    }

    @Bindable
    public String getParentPath() {
        return parentPath;
    }

    public void setExtName(String extName) {
        this.extName = extName;
        notifyPropertyChanged(BR.extName);
    }

    @Override
    public String toString() {
        return "Song{" +
                "albumName='" + albumName + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", extName='" + extName + '\'' +
                ", id=" + id +
                ", albumId=" + albumId +
                ", artistId=" + artistId +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imgPath=" + Arrays.toString(imgPath) +
                ", singer='" + singer + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", isPlaying=" + isPlaying +
                '}';
    }
}
