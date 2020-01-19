package com.example.sos_app_ui;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

public class NotificationIntentService extends IntentService {
    public static final String CANCEL_SMS = "com.example.sos_app_ui.action.CANCEL_SMS";

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (CANCEL_SMS.equals(action)) {
                System.out.println("Cancel sms");
                MainActivity.setSmsFlag(false);
            }
        }
    }
}
