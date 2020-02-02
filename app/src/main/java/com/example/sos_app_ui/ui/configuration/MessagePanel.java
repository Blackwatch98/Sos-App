package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sos_app_ui.R;

/**
 * Created by Daniel Duczymiński
 *
 * This activity is a panel where user can modify template of message he wants to be sent
 */
public class MessagePanel extends AppCompatActivity {


    private static String message;
    private Button confirmBtn;
    private EditText table;


    public static String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setMessage("Hello,\nI might be injured badly and gonna need help. " +
                "   Please check if everything is all right with me.\n");

        table = findViewById(R.id.messageTable);
        table.setText(getMessage() + "My location is: [here link to your location will be attached]");

        confirmBtn = findViewById(R.id.confirmBtn2);
        confirmBtn.setOnClickListener(new View.OnClickListener() {

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
