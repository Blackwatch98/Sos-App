package com.example.sos_app_ui.ui.configuration;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

import com.example.sos_app_ui.R;

public class MessagePanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String message = "Hello,\nI might be injured badly and gonna need help. " +
                "   Please check if everything is all right with me.\n" +
                "My location is: [here link to your location will be attached]";

        EditText table = findViewById(R.id.messageTable);
        table.setText(message);
    }

}
