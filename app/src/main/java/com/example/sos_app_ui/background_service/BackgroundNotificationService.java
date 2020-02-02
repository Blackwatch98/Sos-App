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
import android.os.Handler;
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


/**
 * That class is responsible for work in background and shows notifications
 */
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
    //ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static Context context;

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
        context = getApplicationContext();
        createForeground();
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

    /**
     * Method creates and show Foreground that fall has been happened.
     * Also sets delay to send messages
     */
    public void createForeground() {
        SendSmsDelayClass sendSmsDelay = new SendSmsDelayClass(30000, this);
        sendSmsDelay.sendSms();
        initChannels(context);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        PendingIntent dismissIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent cancelSmsIntent = new Intent(this, NotificationIntentService.class);
        cancelSmsIntent.setAction(NotificationIntentService.CANCEL_SMS);
        PendingIntent cancelSmsPendingIntent = PendingIntent.getService(this, 0,
                cancelSmsIntent, 0);
        //cancelSmsPendingIntent.getActivity(this, 0, intent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                "default") // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Fall detected")
                .setContentText("Sending sms notifications in 30 sec.")
                .setContentIntent(
                        PendingIntent.getActivity(
                                this,
                                0,
                                new Intent(this, NotificationIntentService.class),
                                0))
                .addAction(R.drawable.ic_notifications_black_24dp, "Uff! I'm ok.",
                        cancelSmsPendingIntent)
                .addAction(R.drawable.ic_notifications_black_24dp, "There was no accident.",
                        cancelSmsPendingIntent)
                .build());

        createNotificationChannel();
    }

    /**
     * Method creates and show notifications.
     */
    public static void createNotification(String notificationTitle, String notificationText, Context context){
        initChannels(context);
        //Intent notificationIntent = new Intent(context, MainActivity.class);

//        Intent cancelSmsIntent = new Intent(context, NotificationIntentService.class);
//        cancelSmsIntent.setAction(NotificationIntentService.CANCEL_SMS);
//        PendingIntent cancelSmsPendingIntent = PendingIntent.getService(context, 0,
//                cancelSmsIntent, 0);

        MainActivity.setSmsFlag(true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_black)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }

    /**
     * Creates channel for communicating with the rest of the app
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * runs the notifications channel
     * @param context
     * context of the app that will create notifications
     */

    private static void initChannels(Context context) {
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