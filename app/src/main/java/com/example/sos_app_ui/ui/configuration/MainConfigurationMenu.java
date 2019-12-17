package com.example.sos_app_ui.ui.configuration;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sos_app_ui.R;

import java.util.ArrayList;

public class MainConfigurationMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_configuration_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.MainConfList);

        String functions[] = {"Create New Configuration", "Load Configuration"};
        ArrayList<String> funList = new ArrayList<String>();

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row, funList);

        list.setAdapter(adapter);
    }

}
