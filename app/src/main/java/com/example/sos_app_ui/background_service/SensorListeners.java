package com.example.sos_app_ui.background_service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Vibrator;

import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.logs.LastActivityFragment;
import com.example.sos_app_ui.logs.model.LogModel;

import java.sql.Timestamp;

/**
 * Class that keep accelerometer Listeners and use Calculate classes to detect impact
 */
public class SensorListeners {
    //private float gyroscopeStrX = (float) 0;
    //private float gyroscopeStrY = (float) 0;
    //private float gyroscopeStrZ = (float) 0;
    private float accValX = (float) 0;
    private float accValY = (float) 0;
    private float accValZ = (float) 0;

    private CalculateFallClass calculateFall;
    private CalculateSensorClass calculateX;
    private CalculateSensorClass calculateY;
    private CalculateSensorClass calculateZ;

    private StringBuilder accelerometerStrX = new StringBuilder();
    private StringBuilder accelerometerStrY = new StringBuilder();
    private StringBuilder accelerometerStrZ = new StringBuilder();

    private boolean FALL;

    private boolean saved;
    private Context context;
    private BackgroundNotificationService backgroundNotificationService;

    public SensorListeners(Context context, BackgroundNotificationService backgroundNotificationService) {
        setCalculations();

        this.context = context;
        this.backgroundNotificationService = backgroundNotificationService;
    }

    /**
     * Method sets calculate classes
     */
    private void setCalculations(){
        int listOfImpactLength = 3;
        int listofNotMoveLength = 2000;
        int highimpactValue = 35;
        int lowImpactValue = 10;
        double notMoveValue = 2;
        int stopAlarmMaxCounterValue = 300;
        double timeAfterImpact = 2;
        double notMoveTime = 10;
        int listLength = 4;

        calculateX = new CalculateSensorClass(listLength);
        calculateY = new CalculateSensorClass(listLength);
        calculateZ = new CalculateSensorClass(listLength);
        calculateFall = new CalculateFallClass(listOfImpactLength, listofNotMoveLength, highimpactValue,
                lowImpactValue, notMoveValue, stopAlarmMaxCounterValue, timeAfterImpact, notMoveTime);
    }

    /**
     * Method start accelerometer event listener to calculate impact
     * @return sensor event listener
     */
    SensorEventListener setAccelerometerEventListener() {
        SensorEventListener accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                accValX = sensorEvent.values[0];
                float val = calculateX.addElement(accValX);
                calculateFall.setAccValueX(val);
                //calculateFall.setAccValueX(calculateX.addElement(accValX));
                //accelerometerStrX.append(accValX+" : "+val+"\n");

                //System.out.println(val);

                accValY = sensorEvent.values[1];
                //calculateFall.setAccValueY(calculateY.addElement(accValY));
                val = calculateY.addElement(accValY);
                calculateFall.setAccValueY(val);
                //accelerometerStrY.append(accValY+" : "+val+"\n");

                accValZ = sensorEvent.values[2];
                //calculateFall.setAccValueZ(calculateZ.addElement(accValZ));
                val = calculateZ.addElement(accValZ);
                calculateFall.setAccValueZ(val);
                //accelerometerStrZ.append(accValZ+" : "+val+"\n");

                if (calculateFall.calculate()) {
                    if(!FALL) {
                        FALL = true;
                        setCalculations();
                        backgroundNotificationService.createForeground();
                        vibrate(2000);
                    }
                    LogModel logModel = new LogModel(new Timestamp(System.currentTimeMillis()), "Fall detected");
                    LastActivityFragment.logs.add(logModel);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        return accelerometerListener;
    }


//    SensorEventListener setGyroscopeEventListener() {
//        return new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                gyroscopeStrX = sensorEvent.values[0];
//
//                gyroscopeStrY = sensorEvent.values[1];
//
//                gyroscopeStrZ = sensorEvent.values[2];
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//            }
//        };
//    }

    // saving
    private void saveResults() {
        FileHelper fileAccelerometerX = new FileHelper("accX.txt", context);
        FileHelper fileAccelerometerY = new FileHelper("accY.txt", context);
        FileHelper fileAccelerometerZ = new FileHelper("accZ.txt", context);

        fileAccelerometerX.writeToFile(accelerometerStrX.toString());
        fileAccelerometerY.writeToFile(accelerometerStrY.toString());
        fileAccelerometerZ.writeToFile(accelerometerStrZ.toString());
    }

    private void vibrate(Integer time){
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(time);
    }
}
