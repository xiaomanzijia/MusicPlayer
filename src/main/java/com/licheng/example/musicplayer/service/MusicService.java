package com.licheng.example.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.licheng.example.musicplayer.MusicHelper;
import com.licheng.example.musicplayer.model.Music;

import java.io.IOException;
import java.util.List;

/**
 * Created by licheng on 6/5/16.
 */
public class MusicService extends Service {
    public static MediaPlayer player;
    public static List<Music> musicList;
    public static int position;
    private MusicHelper helper;

    @Override
    public void onCreate() {
        super.onCreate();
        helper = new MusicHelper(getApplicationContext());
        musicList = helper.getMusicList();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position = intent.getIntExtra("position",0);

        String url = musicList.get(position).getUrl();
        Uri uri = Uri.parse(url);

        Log.i("MusicService",uri.toString());

        if(player != null){
            player.release();
            player = null;
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        try {
            player.setDataSource(getApplicationContext(),uri);
            player.prepare();
            player.setOnPreparedListener(onPreparedListener);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        stopSelf();
    }

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            player.start();
            //发送广播
            Intent intent = new Intent("com.licheng.example.musicplayer");
            sendBroadcast(intent);
        }
    };


}
