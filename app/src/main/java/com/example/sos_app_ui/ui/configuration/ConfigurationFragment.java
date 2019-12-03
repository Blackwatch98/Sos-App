package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigurationFragment extends Fragment {

    private ConfigurationViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(ConfigurationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuration, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        ListView list = (ListView) root.findViewById(R.id.listView1);

        String functions[] = {"Personal Data", "Phone number", "Warning Targets", "Additional settings"};

        ArrayList<String> carL = new ArrayList<String>();
        carL.addAll( Arrays.asList(functions) );

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.row, carL);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                if(position == 0)
                {
                    Intent appInfo = new Intent(view.getContext(),PersonalDataPanel.class);
                    startActivity(appInfo);
                }
                if(position == 3)
                {
                    Intent appInfo = new Intent(view.getContext(),AdditionalSettingsPanel.class);
                    startActivity(appInfo);
                }

            }
        });

        return root;
    }


}