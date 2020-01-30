package com.example.sos_app_ui.background_service;

import android.content.Context;

import com.example.sos_app_ui.MainActivity;

public class DelayThreadSendSms extends Thread{
    private int delay;
    private Context context;

    public DelayThreadSendSms(Context context, int delay){
        this.delay = delay;
        this.context = context;
    }

    public void run(){
        super.run();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainActivity.sendSms(context);
    }
}
