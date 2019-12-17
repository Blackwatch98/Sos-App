package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sos_app_ui.R;

public class MessagePanel extends AppCompatActivity {

    private Button btn;
    private EditText table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String message = "Hello,\nI might be injured badly and gonna need help. " +
                "Please check if everything is all right with me.\n" +
                "My location is: [here link to your location will be attached]";

        table = findViewById(R.id.messageTable);
        table.setText(message);

        btn = findViewById(R.id.confirmBtn2);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String s1 = table.getText().toString();

                Bundle args = new Bundle();
                Intent resultIntent = new Intent(v.getContext(),
                        CreateNewConfiguration.class);

                args.putString("message",s1);

                resultIntent.putExtras(args);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });


    }

}
