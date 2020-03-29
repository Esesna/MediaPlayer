package com.example.vdnh_pushkin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import static com.example.vdnh_pushkin.MainActivity.DATA_SD;
import static com.example.vdnh_pushkin.MainActivity.endTime;
import static com.example.vdnh_pushkin.MainActivity.id;

public class SongAdapter extends BaseAdapter {
    private boolean On_off = false;
    private ArrayList<Song> songs;
    private LayoutInflater songInf;
    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs = theSongs;
        songInf = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(position);
        songLay.setId(MainActivity.map.get(currSong.getTitle()));
        songLay.setOnClickListener(myCheckClickList);
        return songLay;

    }

    View.OnClickListener myCheckClickList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity Mv = new MainActivity();

            if(On_off == false){
                On_off = true;
                mediaPlayer.setPath(DATA_SD + "/" + Mv.files[id[(Integer) v.getTag()]].getName());
                mediaPlayer.setAudioStream();
                mediaPlayer.start();

            }else{
                mediaPlayer.mPlayer.reset();
                mediaPlayer.setPath(DATA_SD + "/" + Mv.files[id[(Integer) v.getTag()]].getName());
                mediaPlayer.setAudioStream();
                mediaPlayer.start();
            }

            Mv.imageView1.setImageResource(android.R.drawable.ic_media_pause);
            String temp = ""+songs.get((int)v.getTag()).getTitle();
            Mv.textView.setText(temp);

            int TimeMax = mediaPlayer.mPlayer.getDuration();
            TimeConvert.msToSecAndMin(TimeMax);
            Mv.seekBar.setMax(TimeMax);
            ProgressUpdater.startPlayProgressUpdater(Mv);
        }
    };
    public void startPlayMusic(){

    }

}