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
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BackgroundNotificationService extends Service{
    private static final String SMS_SENT_INTENT_FILTER = "com.yourapp.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.yourapp.sms_delivered";
    public static PendingIntent sentPI = null;
    public static PendingIntent deliveredPI = null;
    private static final int NOTIF_ID = 1;
    private static final String CHANNEL_ID = "1";
    private static int notificationId;
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
        MainActivity.sendSms(this);
        startForeground();      // TO MA ZNIKNAC
        return super.onStartCommand(intent, flags, startId);
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

    public void startForeground() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                "default") // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Fall detected")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_notifications_black_24dp, "Send sms!",
                        null)   // tutaj intent do wyslania sms
                .addAction(R.drawable.ic_notifications_black_24dp, "Nothing happened, I'm ok.",
                        null)  // tutaj intent do niczego?
                .build());

        createNotificationChannel();

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, intent, 0);

//        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
//                SMS_SENT_INTENT_FILTER), 0);
//        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(
//                SMS_DELIVERED_INTENT_FILTER), 0);
    }

    public static void createNotification(String notificationTitle, String notificationText, Context context){
        initChannels(context);
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_black)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

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