package com.licheng.example.musicplayer;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.licheng.example.musicplayer.adapter.MusicAdapter;
import com.licheng.example.musicplayer.model.Music;
import com.licheng.example.musicplayer.receiver.NotificationMusicReceiver;
import com.licheng.example.musicplayer.service.MusicService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Music> musicList;
    private MusicHelper musicHelper;
    private ListView listView;
    private MusicAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listView);

        musicHelper = new MusicHelper(MainActivity.this);
        musicList = musicHelper.getMusicList();
        adapter = new MusicAdapter(MainActivity.this,musicList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(listener);

        //注册广播
        NotificationMusicReceiver receiver = new NotificationMusicReceiver(MainActivity.this);
        IntentFilter filter = new IntentFilter("com.licheng.example.musicplayer");
        registerReceiver(receiver,filter);

    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, MusicService.class);
            intent.putExtra("position",position);
            startService(intent);

            Intent intent1 = new Intent();
            intent1.setClass(MainActivity.this,MusicActivity.class);
            startActivity(intent1);
        }
    };
}
