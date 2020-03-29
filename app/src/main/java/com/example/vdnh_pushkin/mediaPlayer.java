package com.example.vdnh_pushkin;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class mediaPlayer {
    public static MediaPlayer mPlayer = new MediaPlayer();

    public static void setPath(String Path){
        try {
            mPlayer.setDataSource(Path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setAudioStream(){
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }
    public static void start(){
        try {
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }
    public static void pause(){

        mPlayer.pause();
    }
    public static void ret(){
        mPlayer.start();
    }
}
