package com.example.sos_app_ui.ui.current_activity;

import android.Manifest;
import android.app.AlertDialog;
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

    private FileHelper fileHelper;
    private StringBuilder strBuilder;
    private CurrentActivityViewModel dashboardViewModel;

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
        strBuilder = new StringBuilder();
        fileHelper = new FileHelper("test.txt");

        readGyroscope(root, textView);
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


        if(gyroscopeSensor == null) {
            System.out.println("gyroscope sensor not finded");
            return false;
        }

        if(accelerometerSensor == null) {
            System.out.println("acceleromete sensor not finded");
            return false;
        }

        // Create a gyroscope listener
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Float value = sensorEvent.values[0];
                strBuilder.append("gyroscope X: "+value+"\n");
                gyroscopeX.setText(value.toString());

                value = sensorEvent.values[1];
                strBuilder.append("gyroscope Y: "+value+"\n");
                gyroscopeY.setText(value.toString());

                value = sensorEvent.values[2];
                strBuilder.append("gyroscope Z: "+value+"\n");
                gyroscopeZ.setText(value.toString());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Create a gyroscope listener
        SensorEventListener accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Float value = sensorEvent.values[0];
                strBuilder.append("accelerometer X: "+value+"\n");
                accelerometerX.setText(value.toString());

                value = sensorEvent.values[1];
                strBuilder.append("accelerometer Y: "+value+"\n");
                accelerometerY.setText(value.toString());

                value = sensorEvent.values[2];
                strBuilder.append("accelerometer Z: "+value+"\n\n");
                accelerometerZ.setText(value.toString());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register the listeners
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button btnSaveFile = view.findViewById(R.id.saveTest);

        btnSaveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fileHelper.writeToFile(strBuilder.toString()))
                    textView.setText("write error");
                else
                    textView.setText(fileHelper.getPath());
            }
        });

        return true;
    }

}