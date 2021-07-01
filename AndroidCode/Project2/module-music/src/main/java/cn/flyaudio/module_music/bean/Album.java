/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package cn.flyaudio.module_music.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;

import cn.flyaudio.module_music.BR;

public class Album extends BaseObservable {
//    public final long artistId;
//    public final String artistName;
//    public final long id;
//    public final int songCount;
//    public final String title;
//    public final int year;
//
//    public Album() {
//        this.id = -1;
//        this.title = "";
//        this.artistName = "";
//        this.artistId = -1;
//        this.songCount = -1;
//        this.year = -1;
//    }
//
//    public Album(long _id, String _title, String _artistName, long _artistId, int _songCount, int _year) {
//        this.id = _id;
//        this.title = _title;
//        this.artistName = _artistName;
//        this.artistId = _artistId;
//        this.songCount = _songCount;
//        this.year = _year;
//    }

    boolean isPlay = false;
    String singer;
    String name;
    ObservableArrayList<Song> list;

    @Bindable
    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
        notifyPropertyChanged(BR.singer);
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
    public ObservableArrayList<Song> getList() {
        return list;
    }

    public void setList(ObservableArrayList<Song> list) {
        this.list = list;
        notifyPropertyChanged(BR.list);
    }

    @Bindable
    public boolean getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(boolean isPlay) {
        this.isPlay = isPlay;
        notifyPropertyChanged(BR.isPlay);
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", list=" + list +
                '}';
    }
}
