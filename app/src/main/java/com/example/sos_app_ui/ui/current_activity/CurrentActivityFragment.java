package com.example.sos_app_ui.ui.current_activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.R;
import com.example.sos_app_ui.background_service.BackgroundNotificationService;

import java.util.Calendar;

public class CurrentActivityFragment extends Fragment {

    public boolean activity;
    private CurrentActivityViewModel currentActivityViewModel;
    //private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        currentActivityViewModel = ViewModelProviders.of(this).get(CurrentActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_current_activity, container, false);
        currentActivityViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
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

      return root;
    }

    private void startActivityButtonListener(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        clickButton.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(MainActivity.activityOn == true)
                    stopActivityButton(view, textView, progressBar, clickButton);
                else
                    startActivityButton(view, textView, progressBar, clickButton);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startActivityButton(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        textView.setText("Stop\nactivity");
        progressBar.setVisibility(View.VISIBLE);
        clickButton.setScaleX(0.9f);
        clickButton.setScaleY(0.9f);
        MainActivity.activityOn = true;

        AlarmManager scheduler = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplicationContext(), BackgroundNotificationService.class );
        PendingIntent scheduledIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        scheduler.setAndAllowWhileIdle(scheduler.RTC_WAKEUP,cal.getTimeInMillis(),scheduledIntent);

        //Intent intent = new Intent(getActivity(), BackgroundNotificationService.class);
        //getActivity().startService(intent);
    }
    private void stopActivityButton(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        textView.setText("Start\nactivity");
        progressBar.setVisibility(View.INVISIBLE);
        clickButton.setScaleX(1f);
        clickButton.setScaleY(1f);
        MainActivity.activityOn = false;


        AlarmManager scheduler = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplicationContext(),BackgroundNotificationService.class );
        PendingIntent scheduledIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduler.cancel(scheduledIntent); // not work

        //Intent intent = new Intent(getActivity(), BackgroundNotificationService.class);
        //getActivity().stopService(intent);
    }

}