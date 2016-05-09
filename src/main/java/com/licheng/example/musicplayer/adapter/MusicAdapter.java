package com.licheng.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheng.example.musicplayer.R;
import com.licheng.example.musicplayer.model.Music;
import com.licheng.example.musicplayer.utils.CommonUtils;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
	private List<Music> listMusic;
	private Context context;

	public MusicAdapter(Context context, List<Music> listMusic) {
		this.context = context;
		this.listMusic = listMusic;
	}

	public void setListItem(List<Music> listMusic) {
		this.listMusic = listMusic;
	}

	@Override
	public int getCount() {
		return listMusic.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listMusic.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.music_item, null);
		}
		Music m = listMusic.get(position);
		TextView textMusicName = (TextView) convertView
				.findViewById(R.id.music_item_name);
		textMusicName.setText(m.getName().subSequence(0,
				m.getName().length() - 4));

		TextView textMusicSinger = (TextView) convertView
				.findViewById(R.id.music_item_singer);
		textMusicSinger.setText(m.getSinger());
		TextView textMusicTime = (TextView) convertView
				.findViewById(R.id.music_item_time);
		textMusicTime.setText(CommonUtils.toTime((int) m.getTime()));
		return convertView;
	}

}
