package com.licheng.example.musicplayer.utils;

/**
 * Created by licheng on 6/5/16.
 */
public class CommonUtils {

    public static String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        minute %= 60;
        int second = time % 60;
        @SuppressWarnings("unused")
        int hour = minute / 60;
        return String.format("%02d:%02d", minute, second);
    }
}
