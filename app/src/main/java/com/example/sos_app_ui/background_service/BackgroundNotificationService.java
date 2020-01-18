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

import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackgroundNotificationService extends Service{

    private static final int NOTIF_ID = 1;

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
        //startForeground(true);
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

    public void startForeground(boolean fall) {
        initChannels(this);
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        if(fall) {
            startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                    "default") // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Fall detected")
                    .setContentIntent(pendingIntent)
                    .build());
        }
    }

    public void initChannels(Context context) {
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