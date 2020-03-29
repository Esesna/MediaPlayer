package com.example.vdnh_pushkin;


public class Song {
    private long id;
    private String title;
    private String artist;
    public Song(long songID,String songTitle,String songArtist){
        id = songID;
        title = songTitle;
        artist = songArtist;
    }

    public long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }


}
