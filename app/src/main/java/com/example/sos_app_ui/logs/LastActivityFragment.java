package com.example.sos_app_ui.logs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sos_app_ui.R;
import com.example.sos_app_ui.logs.model.LogModel;

import java.util.ArrayList;
import java.util.List;

/**
 * class runs in foreground and listens for important events that are to be logged and added to the LogsList
 */
public class LastActivityFragment extends Fragment {

    public static List<LogModel> logs = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onStart();
        View root = inflater.inflate(R.layout.fragment_last_activity, container, false);

        ListView list = root.findViewById(R.id.listView_Android_logs);

        ArrayAdapter<LogModel> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                logs
        );

        list.setAdapter(listViewAdapter);

        Handler handler = new Handler();
        int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                listViewAdapter.notifyDataSetChanged();
                handler.postDelayed(this, delay);
            }
        }, delay);

        return root;
    }
}
