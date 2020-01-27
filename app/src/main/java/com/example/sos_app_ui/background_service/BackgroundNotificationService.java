package com.example.sos_app_ui.background_service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.NotificationIntentService;
import com.example.sos_app_ui.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BackgroundNotificationService extends Service{
    private static final String SMS_SENT_INTENT_FILTER = "com.yourapp.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.yourapp.sms_delivered";
    public static PendingIntent sentPI = null;
    public static PendingIntent deliveredPI = null;
    private static final int NOTIF_ID = 1;
    private static final String CHANNEL_ID = "1";
    private static int notificationId = 0;
    private SensorListeners sensorListeners;
    private SensorManager sensorManager;
    private SensorEventListener accelerometerSensorListener;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BackgroundNotificationService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setUpSensors();
        startForeground(true);      // TO MA ZNIKNAC
        Thread thread = new Thread() {
            public void run() {
                MainActivity.sendSms();
            }
        };
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        destroyListener();
        super.onDestroy();
    }

    private void setUpSensors() {
        sensorListeners = new SensorListeners(getApplicationContext(), this);

        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);

        Sensor accelerometerSender = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accelerometerSensorListener = sensorListeners.setAccelerometerEventListener();
        sensorManager.registerListener(accelerometerSensorListener, accelerometerSender, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void destroyListener(){
        sensorManager.unregisterListener(accelerometerSensorListener);
    }

    public void startForeground(boolean fall) {
        initChannels(this);
    }

    public static void createNotification(String notificationTitle, String notificationText, Context context){
        initChannels(context);
        Intent notificationIntent = new Intent(context, MainActivity.class);

        Intent cancelSmsIntent = new Intent(context, NotificationIntentService.class);
        cancelSmsIntent.setAction(NotificationIntentService.CANCEL_SMS);
        PendingIntent cancelSmsPendingIntent = PendingIntent.getService(context, 0,
                cancelSmsIntent, 0);

        MainActivity.setSmsFlag(true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_black)
                .setContentTitle("Fall detected")
                .setContentText("Sending sms notifications in 30 sec.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, NotificationIntentService.class),
                                0))
                .setAutoCancel(true)
                .addAction(R.drawable.ic_notifications_black_24dp, "Uff! I'm ok.",
                        cancelSmsPendingIntent)
                .addAction(R.drawable.ic_notifications_black_24dp, "There was no accident.",
                        cancelSmsPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
}