package com.licheng.example.musicplayer.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.licheng.example.musicplayer.MusicActivity;
import com.licheng.example.musicplayer.R;
import com.licheng.example.musicplayer.model.Music;
import com.licheng.example.musicplayer.service.MusicService;

/**
 * Created by licheng on 9/5/16.
 */
public class NotificationMusicReceiver extends BroadcastReceiver {
    private Context mContext;
    private NotificationManager notificationManager;

    public NotificationMusicReceiver(Context mContext) {
        this.mContext = mContext;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Music music = MusicService.musicList.get(MusicService.position);
        if(null != music){
            setNotification(music);
        }
    }

    private void setNotification(Music m){
        Notification notification = new Notification(R.drawable.ic_launcher,m.getTitle(), System.currentTimeMillis());
        Intent intent = new Intent(mContext, MusicActivity.class);
        intent.putExtra("fromnotification",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        notification.contentIntent = pendingIntent;
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),R.layout.notification);
        remoteView.setTextViewText(R.id.noticationname,m.getTitle());
        remoteView.setTextViewText(R.id.noticationsinger,m.getSinger());
        Intent intent1 = new Intent();
        intent1.setAction("com.licheng.example.notificationplay");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(mContext,0,intent1,0);
        Intent intent2 = new Intent();
        intent2.setAction("com.licheng.example.notificationext");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(mContext,0,intent2,0);
        remoteView.setOnClickPendingIntent(R.id.notificationplay,pendingIntent1);
        remoteView.setOnClickPendingIntent(R.id.notificationnext,pendingIntent2);
        notification.contentView = remoteView;
        notificationManager.notify(0,notification);
        //关闭之前的定时器
        if(MusicActivity.timer != null) MusicActivity.timer.cancel();
        if(MusicActivity.timerTask != null) MusicActivity.timerTask.cancel();
    }

}
