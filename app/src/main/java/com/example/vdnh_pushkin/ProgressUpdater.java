package com.example.vdnh_pushkin;

public class ProgressUpdater {

    public static void startPlayProgressUpdater(final MainActivity v) {
        v.seekBar.setProgress(mediaPlayer.mPlayer.getCurrentPosition());

        if (mediaPlayer.mPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    //MainActivity.textView.setText(mediaPlayer.mPlayer.getCurrentPosition()+"");
                    TimeConvert.timeView(v,mediaPlayer.mPlayer.getCurrentPosition(),mediaPlayer.mPlayer.getDuration());
                    startPlayProgressUpdater(v);
                }
            };
            v.handler.postDelayed(notification,1000);
        }else{
            mediaPlayer.pause();
            //v.textView.setText("pause");
        }
    }
}
