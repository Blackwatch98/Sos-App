package com.example.sos_app_ui.background_service;

import android.content.Context;
import android.os.Handler;

import com.example.sos_app_ui.MainActivity;

/**
 * Simple class that waits period of time to start Send Sms method.
 */
public class SendSmsDelayClass {
    private int timeMillis;
    private Context context;

    public SendSmsDelayClass(int timeMillis, Context context) {
        this.timeMillis = timeMillis;
        this.context = context;
    }

    public void sendSms(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.sendSms(context);
            }
        }, timeMillis);
    }
}
