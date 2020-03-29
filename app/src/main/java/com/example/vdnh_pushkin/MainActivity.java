package com.example.vdnh_pushkin;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    //===========================TEMP
    public static HashMap<String,Integer> map = new HashMap<>();
    //TextView=======================
    public static TextView textView ;
    public static TextView endTime ;
    public static TextView nowTime ;
    //===============================
    public static File[] files;
    public static String DATA_SD;
    public static int[] id;
    public static ImageView imageView1;
    public static SeekBar seekBar;

    private static final String TAG = "Yarik";
    public ArrayList<Song> songList;
    private ListView songView;

    int i = 0;

    //cостояние плеера(играет или нет)
    boolean mediaStation;
    final boolean Play = true;
    final boolean Stop = false;
    final public static Handler handler = new Handler();

    //

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //==========================================================================================
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на доступ к памяти
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
            } else {
                Log.d(TAG, "Permission is revoked");
                //запрашиваем разрешение
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            //если доступ не дали
        }
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на доступ к памяти
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
            } else {
                Log.d(TAG, "Permission is revoked");
                //запрашиваем разрешение
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            //если доступ не дали
        }
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на доступ к памяти
            if (checkSelfPermission(Manifest.permission.MEDIA_CONTENT_CONTROL)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
            } else {
                Log.d(TAG, "Permission is revoked");
                //запрашиваем разрешение
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MEDIA_CONTENT_CONTROL}, 1);
            }
        } else {
            //если доступ не дали
        }
        //==========================================================================================
        textView = findViewById(R.id.Titles);
        endTime = findViewById(R.id.endTime);
        nowTime = findViewById(R.id.nowTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer.mPlayer.isPlaying()){
                    mediaPlayer.mPlayer.seekTo(seekBar.getProgress());
                    textView.setText("" + seekBar.getProgress());
                }
                return false;
            }
        });
        songView = (ListView) findViewById(R.id.ListMusic);
        songList = new ArrayList<Song>();
        DATA_SD = Environment.getExternalStorageDirectory()
                + "/MusicRS";
        File myFolder = new File(DATA_SD);
        files = myFolder.listFiles();
        String temp;
        //String ansTest = "";
        id = new int[files.length];
        int count = 0;
        for(i = 0;i<files.length;i++){

            temp = getExtension(files[i]);
            if (temp.equals("mp3")) {
                getSongList(files[i].getName());
                id[count] = i;
                count++;
            }

            //ansTest += "\n"+files[i].getName();
        }
        //textView.setText(files.length+ansTest);

        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        ProgressUpdater.startPlayProgressUpdater(this);
        //------выше мы генерили красивый список----------------------------------------------------
        //mediaPlayer.OnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener(){
        //})
        imageView1 = findViewById(R.id.imageViewPlay);
        imageView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.setBackgroundColor(Color.parseColor("#E6E4E4"));
                    //v.setBackgroundColor(R.color.PlayDown);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    //v.setBackgroundColor(R.color.PlayUp);
                }
                return false;
            }
        });
    }

    public void playAndStop(View v){

        if (mediaStation == Stop) {
            try{
                imageView1.setImageResource(android.R.drawable.ic_media_pause);
                mediaPlayer.start();
                mediaStation = Play;
                textView.setText("play");
                ProgressUpdater.startPlayProgressUpdater(this);
            }catch (IllegalStateException e) {
                mediaPlayer.pause();
                imageView1.setImageResource(android.R.drawable.ic_media_pause);
                imageView1.setImageResource(android.R.drawable.ic_media_play);

            }
        }else {

            imageView1.setImageResource(android.R.drawable.ic_media_play);
            mediaStation = Stop;
            textView.setText("pause");
            mediaPlayer.pause();
        }

    }
    public void getSongList(String name){
        ContentResolver musicResolver = getContentResolver();
        String DATA_SD = Environment.getExternalStorageDirectory()
                + "/MusicRS/"
                + name;
        try {
            Mp3File musicInf = new Mp3File(DATA_SD);
            ID3v1 id3v1Tag = musicInf.getId3v1Tag();
            map.put(id3v1Tag.getTitle(),i);
            songList.add(new Song(i,id3v1Tag.getTitle(),id3v1Tag.getArtist()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
    }
    /* * Get the extension of a file. */
    public String getExtension(File f) {
        String ext = "null"; String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}