package com.example.sos_app_ui.ui.current_activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.R;

import static android.content.Context.SENSOR_SERVICE;

public class CurrentActivityFragment extends Fragment {
    private StringBuilder gyroscopeStrX = new StringBuilder();
    private StringBuilder gyroscopeStrY = new StringBuilder();
    private StringBuilder gyroscopeStrZ = new StringBuilder();
    private StringBuilder accelerometerStrX = new StringBuilder();
    private StringBuilder accelerometerStrY = new StringBuilder();
    private StringBuilder accelerometerStrZ = new StringBuilder();
    private CurrentActivityViewModel dashboardViewModel;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(CurrentActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_current_activity, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        context = getContext();

        if(!readGyroscope(root, textView));
            textView.setText("Sensors Error");
        return root;
    }

    private boolean readGyroscope(View view, final TextView textView){
        final TextView gyroscopeX = view.findViewById(R.id.gyroscopeX);
        final TextView gyroscopeY = view.findViewById(R.id.gyroscopeY);
        final TextView gyroscopeZ = view.findViewById(R.id.gyroscopeZ);
        final TextView accelerometerX = view.findViewById(R.id.accelerometerX);
        final TextView accelerometerY = view.findViewById(R.id.accelerometerY);
        final TextView accelerometerZ = view.findViewById(R.id.accelerometerZ);

        SensorManager sensorManager = (SensorManager)getActivity().getSystemService(SENSOR_SERVICE);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(!checkSensors(accelerometerSensor, gyroscopeSensor))
            return false;


        SensorEventListener accelerometerListener = setAccelerometerEventListener(accelerometerX, accelerometerY, accelerometerZ);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

        SensorEventListener gyroscopeSensorListener = setGyroscopeEventListener(gyroscopeX, gyroscopeY, gyroscopeZ);
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);

        setButton(view, textView);

        return true;
    }

    private SensorEventListener setAccelerometerEventListener(final TextView aX, final TextView aY, final TextView aZ){
        SensorEventListener accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Float value = sensorEvent.values[0];
                accelerometerStrX.append(value+"\n");
                aX.setText(value.toString());

                value = sensorEvent.values[1];
                accelerometerStrY.append(value+"\n");
                aY.setText(value.toString());

                value = sensorEvent.values[2];
                accelerometerStrZ.append(value+"\n");
                aZ.setText(value.toString());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        return accelerometerListener;
    }

    private SensorEventListener setGyroscopeEventListener(final TextView gX, final TextView gY, final TextView gZ){
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Float value = sensorEvent.values[0];
                gyroscopeStrX.append(value+"\n");
                gX.setText(value.toString());

                value = sensorEvent.values[1];
                gyroscopeStrY.append(value+"\n");
                gY.setText(value.toString());

                value = sensorEvent.values[2];
                gyroscopeStrZ.append(value+"\n");
                gZ.setText(value.toString());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        return gyroscopeSensorListener;
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

    private void setButton(View view, final TextView textView){
        Button btnSaveFile = view.findViewById(R.id.saveTest);

        btnSaveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileHelper fileAccelerometerX = new FileHelper("accX.txt", context);
                FileHelper fileAccelerometerY = new FileHelper("accY.txt", context);
                FileHelper fileAccelerometerZ = new FileHelper("accZ.txt", context);

                FileHelper fileGyroscopeX = new FileHelper("gyroX.txt", context);
                FileHelper fileGyroscopeY = new FileHelper("gyroY.txt", context);
                FileHelper fileGyroscopeZ = new FileHelper("gyroZ.txt", context);

                if(fileAccelerometerX.writeToFile(accelerometerStrX.toString()) && fileAccelerometerY.writeToFile(accelerometerStrY.toString()) &&
                fileAccelerometerZ.writeToFile(accelerometerStrZ.toString()) && fileGyroscopeX.writeToFile(gyroscopeStrX.toString()) &&
                fileGyroscopeY.writeToFile(gyroscopeStrY.toString()) && fileGyroscopeZ.writeToFile(gyroscopeStrZ.toString()))
                    textView.setText("files saved");
                else
                    textView.setText("files saving error");
            }
        });
    }

}