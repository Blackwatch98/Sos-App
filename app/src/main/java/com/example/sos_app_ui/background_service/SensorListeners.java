package com.example.sos_app_ui.background_service;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensorListeners {
    private float gyroscopeStrX = (float) 0;
    private float gyroscopeStrY = (float) 0;
    private float gyroscopeStrZ = (float) 0;
    private float accValX = (float) 0;
    private float accValY = (float) 0;
    private float accValZ = (float) 0;

    private CalculateFallClass calculateFall;
    private CalculateSensorClass calculateX;
    private CalculateSensorClass calculateY;
    private CalculateSensorClass calculateZ;

    private boolean FALL;

    public SensorListeners() {
        calculateFall = new CalculateFallClass(2,1000,
                35,10,2, (long) 100, (double)5);
        calculateX = new CalculateSensorClass(4);
        calculateY = new CalculateSensorClass(4);
        calculateZ = new CalculateSensorClass(4);
    }

    public boolean getFALL(){
        return FALL;
    }

    public float getGyroscopeStrX() {
        return gyroscopeStrX;
    }

    public float getGyroscopeStrY() {
        return gyroscopeStrY;
    }

    public float getGyroscopeStrZ() {
        return gyroscopeStrZ;
    }

    public float getaccValX() {
        return accValX;
    }

    public float getaccValY() {
        return accValY;
    }

    public float getaccValZ() {
        return accValZ;
    }

    SensorEventListener setAccelerometerEventListener(){
        SensorEventListener accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                accValX = sensorEvent.values[0];
                calculateFall.setAccValueX(calculateX.addElement(accValX));
                
                accValY = sensorEvent.values[1];
                calculateFall.setAccValueY(calculateY.addElement(accValY));

                accValZ = sensorEvent.values[2];
                calculateFall.setAccValueZ(calculateZ.addElement(accValZ));

                if(calculateFall.calculate())
                    FALL = true;
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

    // saving
//    FileHelper fileAccelerometerX = new FileHelper("accX.txt", context);
//                FileHelper fileAccelerometerY = new FileHelper("accY.txt", context);
//                FileHelper fileAccelerometerZ = new FileHelper("accZ.txt", context);
//
//                FileHelper fileGyroscopeX = new FileHelper("gyroX.txt", context);
//                FileHelper fileGyroscopeY = new FileHelper("gyroY.txt", context);
//                FileHelper fileGyroscopeZ = new FileHelper("gyroZ.txt", context);
//
//                if(fileAccelerometerX.writeToFile(accelerometerStrX.toString()) && fileAccelerometerY.writeToFile(accelerometerStrY.toString()) &&
//                fileAccelerometerZ.writeToFile(accelerometerStrZ.toString()) && fileGyroscopeX.writeToFile(gyroscopeStrX.toString()) &&
//                fileGyroscopeY.writeToFile(gyroscopeStrY.toString()) && fileGyroscopeZ.writeToFile(gyroscopeStrZ.toString()))
}
