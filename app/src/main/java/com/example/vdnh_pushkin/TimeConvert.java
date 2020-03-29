package com.example.vdnh_pushkin;

public class TimeConvert {
    public static String msToSecAndMin(int Time){
        int sec = 0;
        int min = 0;
        sec = Time / 1000;
        while(sec > 59){
            sec -=60;
            min++;
        }
        String textView = "";
        if(min < 10){
            textView = "0" + min;
        }
        else{
            textView = "" + min;
        }
        if(sec < 10){
            textView += ":0" + sec;
        }
        else{
            textView += ":" + sec;
        }
        return textView;
    }

    public static void timeView(MainActivity v,int nTime,int eTime){
        v.nowTime.setText(TimeConvert.msToSecAndMin(nTime));
        v.endTime.setText(TimeConvert.msToSecAndMin(eTime));
    }
}
