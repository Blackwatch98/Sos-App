package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import com.example.sos_app_ui.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;

public class PersonalDataPanel extends AppCompatActivity {


    private TextInputEditText fname;
    private TextInputEditText sname;
    private TextInputEditText age;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = findViewById(R.id.confirmBtn1);
        fname = findViewById(R.id.fNameInput);
        sname = findViewById(R.id.sNameInput);
        age = findViewById(R.id.ageInput);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String s1 = fname.getText().toString();
                String s2 =sname.getText().toString();
                String s3 =age.getText().toString();

                Bundle args = new Bundle();
                Intent resultIntent = new Intent(v.getContext(),
                        CreateNewConfiguration.class);

                args.putString("fname",s1);
                args.putString("sname",s2);
                args.putString("age",s3);

                System.out.println(args);
                resultIntent.putExtras(args);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

    }

}
