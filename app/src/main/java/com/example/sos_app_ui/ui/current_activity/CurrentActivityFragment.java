package com.example.sos_app_ui.ui.current_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.R;
import com.example.sos_app_ui.background_service.BackgroundNotificationService;
import com.example.sos_app_ui.logs.LastActivityFragment;
import com.example.sos_app_ui.logs.model.LogModel;

import java.sql.Time;
import java.sql.Timestamp;

public class CurrentActivityFragment extends Fragment {

    public boolean activity;
    private CurrentActivityViewModel currentActivityViewModel;

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
        clickButton.setOnClickListener(v -> {
            if(MainActivity.activityOn)
                stopActivityButton(view, textView, progressBar, clickButton);
            else
                startActivityButton(view, textView, progressBar, clickButton);
            logActivityStart(MainActivity.activityOn);
        });
    }

    private void startActivityButton(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        textView.setText("Stop\nactivity");
        progressBar.setVisibility(View.VISIBLE);
        clickButton.setScaleX(0.9f);
        clickButton.setScaleY(0.9f);
        MainActivity.activityOn = true;

        Intent intent = new Intent(getActivity(), BackgroundNotificationService.class);
        getActivity().startService(intent);
    }
    private void stopActivityButton(final View view, final TextView textView, final ProgressBar progressBar, final Button clickButton) {
        textView.setText("Start\nactivity");
        progressBar.setVisibility(View.INVISIBLE);
        clickButton.setScaleX(1f);
        clickButton.setScaleY(1f);
        MainActivity.activityOn = false;

        Intent intent = new Intent(getActivity(), BackgroundNotificationService.class);
        getActivity().stopService(intent);
    }

    private void logActivityStart(boolean hasStarted){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String message = hasStarted ? "Activity started" : "Activity stopped";
        LogModel logModel = new LogModel(now, message);

        LastActivityFragment.logs.add(logModel);
    }


}