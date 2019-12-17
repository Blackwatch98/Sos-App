package com.example.sos_app_ui.ui.current_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sos_app_ui.R;
import com.example.sos_app_ui.background_service.BackgroundNotificationService;

import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;

public class CurrentActivityFragment extends Fragment {

    private Context context;
    public boolean activity;
    private CurrentActivityViewModel currentActivityViewModel;


    private CalculateSensorClass calculateAccX;
    private CalculateSensorClass calculateAccY;
    private CalculateSensorClass calculateAccZ;
    private CalculateFallClass calculateFall;

    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        currentActivityViewModel = ViewModelProviders.of(this).get(CurrentActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_current_activity, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
        currentActivityViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        context = getContext();
        activity = false;

        final Button clickButton = root.findViewById(R.id.activityButton);
        final TextView status = root.findViewById(R.id.status);
        final ProgressBar progressBar = root.findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        //update when opening the fragment
        if(MainActivity.activityOn == true)
            startActivityButton(root, status, progressBar, clickButton);
        else
            stopActivityButton(root, status, progressBar, clickButton);
        //set listener
        startActivityButtonListener(root, status, progressBar, clickButton);
//
//        if(!readSensors(root, textView));
//            textView.setText("Sensors Error");

        Intent intent = new Intent(getActivity(), BackgroundNotificationService.class);
        Bundle b = new Bundle();
        b.putString("data", "5");
        intent.putExtras(b);
        getActivity().startService(intent);

//        calculateAccX = new CalculateSensorClass(4, "X");
//        calculateAccY = new CalculateSensorClass(4,"Y");
//        calculateAccZ = new CalculateSensorClass(4, "Z");
//        calculateFall = new CalculateFallClass(2,1000,35,
//                                                   10,2, (long)100, (double)5);
//
//        if(!readSensors(root, textView));
//            textView.setText("Sensors Error");
      return root;
    }




    private boolean readSensors(View view, final TextView textView){
//        final TextView gyroscopeX = view.findViewById(R.id.gyroscopeX);
//        final TextView gyroscopeY = view.findViewById(R.id.gyroscopeY);
//        final TextView gyroscopeZ = view.findViewById(R.id.gyroscopeZ);
//        final TextView accelerometerX = view.findViewById(R.id.accelerometerX);
//        final TextView accelerometerY = view.findViewById(R.id.accelerometerY);
//        final TextView accelerometerZ = view.findViewById(R.id.accelerometerZ);

        SensorManager sensorManager = (SensorManager)getActivity().getSystemService(SENSOR_SERVICE);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(!checkSensors(accelerometerSensor, gyroscopeSensor))
            return false;


//        SensorEventListener accelerometerListener = setAccelerometerEventListener(accelerometerX, accelerometerY, accelerometerZ);
//        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
//
//        SensorEventListener gyroscopeSensorListener = setGyroscopeEventListener(gyroscopeX, gyroscopeY, gyroscopeZ);
//        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);

        //setButton(view, textView);

        return true;
    }


    private boolean checkSensors(Sensor accelerometer, Sensor gyroscope){
        if(gyroscope == null) {
            System.out.println("gyroscope sensor not finded");
            return false;
        }

        if(accelerometer == null) {
            System.out.println("acceleromete sensor not finded");
            return false;
        }
        return true;
    }

    private void startActivityButtonListener(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.activityOn == true)
                    stopActivityButton(view, textView, progressBar, clickButton);
                else
                    startActivityButton(view, textView, progressBar, clickButton);
            }
        });
    }

    private void startActivityButton(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        textView.setText("Stop\nactivity");
        progressBar.setVisibility(View.VISIBLE);
        clickButton.setScaleX(0.9f);
        clickButton.setScaleY(0.9f);
        MainActivity.activityOn = true;
    }
    private void stopActivityButton(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        textView.setText("Start\nactivity");
        progressBar.setVisibility(View.INVISIBLE);
        clickButton.setScaleX(1f);
        clickButton.setScaleY(1f);
        MainActivity.activityOn = false;
    }
//    private void setButton(View view, final TextView textView){
//        Button btnSaveFile = view.findViewById(R.id.saveTest);
//
//        btnSaveFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FileHelper fileAccelerometerX = new FileHelper("accX.txt", context);
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
//                    textView.setText("files saved");
//                else
//                    textView.setText("files saving error");
//            }
//        });
//    }

}