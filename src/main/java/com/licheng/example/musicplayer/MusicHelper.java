package com.licheng.example.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.licheng.example.musicplayer.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licheng on 6/5/16.
 */
public class MusicHelper {
    Context mContext;

    public MusicHelper(Context mContext) {
        this.mContext = mContext;
    }

    public List<Music> getMusicList(){
        List<Music> list = new ArrayList<>();
        ContentResolver resolver = mContext.getContentResolver();
        if(resolver != null){
            Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,
                    null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if(null != cursor){
                if (cursor.moveToFirst()){
                    do{
                        Music music = new Music();
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String singer = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        if ("<unknown>".equals(singer)) {
                            singer = "未知艺术家";
                        }
                        long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        String album = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        long size = cursor.getLong(cursor
                                .getColumnIndex(MediaStore.Audio.Media.SIZE));
                        long time = cursor.getLong(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DURATION));
                        String url = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DATA));
                        String name = cursor
                                .getString(cursor
                                        .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        String sbr = name.substring(name.length() - 3,
                                name.length());
                        if (sbr.equals("mp3") && (time >= 1000 && time <= 900000)) {
                            music.setTitle(title);
                            music.setSinger(singer);
                            music.setSize(size);
                            music.setAlbum(album);
                            music.setTime(time);
                            music.setUrl(url);
                            music.setName(name);
                            music.setAlbumid(albumId);
                            list.add(music);
                        }

                    }while (cursor.moveToNext());
                }
            }
            if(null != cursor){
                cursor.close();
            }
        }
        return list;
    }
}
