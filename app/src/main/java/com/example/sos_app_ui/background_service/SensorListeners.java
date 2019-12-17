package com.example.sos_app_ui.background_service;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class SensorListeners {
    private float gyroscopeStrX = (float) 0;
    private float gyroscopeStrY = (float) 0;
    private float gyroscopeStrZ = (float) 0;
    private float accelerometerStrX = (float) 0;
    private float accelerometerStrY = (float) 0;
    private float accelerometerStrZ = (float) 0;


    public float getGyroscopeStrX() {
        return gyroscopeStrX;
    }

    public float getGyroscopeStrY() {
        return gyroscopeStrY;
    }

    public float getGyroscopeStrZ() {
        return gyroscopeStrZ;
    }

    public float getAccelerometerStrX() {
        return accelerometerStrX;
    }

    public float getAccelerometerStrY() {
        return accelerometerStrY;
    }

    public float getAccelerometerStrZ() {
        return accelerometerStrZ;
    }

    SensorEventListener setAccelerometerEventListener(){
        SensorEventListener accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                accelerometerStrX = sensorEvent.values[0];

                accelerometerStrY = sensorEvent.values[1];

                accelerometerStrZ = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        return accelerometerListener;
    }

    SensorEventListener setGyroscopeEventListener(){
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                gyroscopeStrX = sensorEvent.values[0];

                gyroscopeStrY = sensorEvent.values[1];

                gyroscopeStrZ = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }
}
