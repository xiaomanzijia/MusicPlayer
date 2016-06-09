package com.licheng.example.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.licheng.example.musicplayer.model.Music;
import com.licheng.example.musicplayer.service.MusicService;
import com.licheng.example.musicplayer.utils.CommonUtils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by licheng on 9/5/16.
 */
public class MusicActivity extends Activity {
    private ImageButton ibtnPlay,ibtnPreview,ibtnNext,ibtnRandom;
    private SeekBar musicSeekBar;
    private TextView textMusicStartTime,textMusicEndTime,textCurrentMusic,textAllMusic,textMusicTitle,textMusicSinger;
    private int position;
    private Music music;
    public static Timer timer;
    public static TimerTask timerTask;
    private boolean isFromNotification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        initData();
        initView();
    }

    private void initData() {
        position = MusicService.position;
        isFromNotification = getIntent().getBooleanExtra("fromnotification",false);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.music_play:
                    if(MusicService.player.isPlaying()){
                        MusicService.player.pause();
                        ibtnPlay.setBackgroundResource(R.drawable.play1);
                    }else {
                        MusicService.player.start();
                        ibtnPlay.setBackgroundResource(R.drawable.pause1);
                    }
                    break;
                case R.id.music_foward:
                    playMusic(position, true);
                    break;
                case R.id.music_rewind:
                    playMusic(position, false);
                    break;
                case R.id.music_random:
                    Random random = new Random();
                    int rand = random.nextInt(MusicService.musicList.size()) + 1;
                    playMusic(rand, false);
                    break;
                default:
                    break;
            }
        }
    };

    private void playMusic(int pos, boolean isNext){
        if(isNext){
            ++ pos;
            if(pos >= MusicService.musicList.size()) pos = 0;
        }else {
            -- pos;
            if(pos < 0) pos = MusicService.musicList.size() - 1;
        }
        position = pos;

        Intent intent = new Intent(MusicActivity.this,MusicService.class);
        intent.putExtra("position",position);
        startService(intent);

        initPlayView(position);
    }

    private void initView() {
        ibtnPlay = (ImageButton) findViewById(R.id.music_play);
        ibtnNext = (ImageButton) findViewById(R.id.music_foward);
        ibtnPreview = (ImageButton) findViewById(R.id.music_rewind);
        ibtnRandom = (ImageButton) findViewById(R.id.music_random);
        musicSeekBar = (SeekBar) findViewById(R.id.music_seekBar);
        textAllMusic = (TextView) findViewById(R.id.allmusic);
        textCurrentMusic = (TextView) findViewById(R.id.currentmusic);
        textMusicEndTime = (TextView) findViewById(R.id.music_end_time);
        textMusicStartTime = (TextView) findViewById(R.id.music_start_time);
        textMusicTitle = (TextView) findViewById(R.id.music_nameq);
        textMusicSinger = (TextView) findViewById(R.id.music_singerq);
        ibtnNext.setOnClickListener(listener);
        ibtnPlay.setOnClickListener(listener);
        ibtnPreview.setOnClickListener(listener);
        ibtnRandom.setOnClickListener(listener);

        initPlayView(position);
    }

    private void initPlayView(int position) {
        music = MusicService.musicList.get(position);
        textMusicTitle.setText(music.getTitle());
        textMusicSinger.setText(music.getSinger());
        textCurrentMusic.setText((position + 1) + "");
        textAllMusic.setText(MusicService.musicList.size()+"");

        textMusicEndTime.setText(CommonUtils.toTime((int) music.getTime()));
        musicSeekBar.setMax((int)music.getTime());

        if(isFromNotification){ //来自通知栏点击
            textMusicStartTime.setText(CommonUtils.toTime((int) current * 1000));
        }else {
            current = 0;
            textMusicStartTime.setText("00:00");
        }

        //如果点击另外一首音乐 计时器和任务需取消重新启动
        if(timer != null){
            timer.cancel();
            Log.i("Music","111");
        }

        if(timerTask != null){
            timerTask.cancel();
            Log.i("Music","222");
        }

        //如果来自通知栏点击打开事件 之前的定时器需要关闭
        timer = new Timer(); //启动计时器

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = ++current;
                handler.sendMessage(msg);
            }
        };

        timer.schedule(timerTask,0,1000);

        musicSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

    }

    public static int current = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int cur = (int) msg.obj;
                    Log.i("Music",cur+"");
                    musicSeekBar.setProgress(cur * 1000);
                    textMusicStartTime.setText(CommonUtils.toTime(cur * 1000));
                    if((cur * 1000) == music.getTime()) {
                        timer.cancel();
                        timerTask.cancel();
                        current = 0;
                        position ++;
                        playMusic(position,true);
                    }
                    break;
                default:break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textMusicStartTime.setText(CommonUtils.toTime(progress));
            seekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MusicService.player.seekTo(seekBar.getProgress());
            seekBar.setProgress(seekBar.getProgress());
            current = seekBar.getProgress() / 1000;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //事件销毁的时候一定记得关闭定时器
        timer.cancel();
        timerTask.cancel();
    }
}
