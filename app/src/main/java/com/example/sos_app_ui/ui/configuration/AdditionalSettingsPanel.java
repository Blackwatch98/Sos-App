package com.example.sos_app_ui.ui.configuration;

import android.os.Bundle;
import android.widget.CheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.sos_app_ui.R;

public class AdditionalSettingsPanel extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_settings_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkBoxSettings();

    }


    public void checkBoxSettings()
    {
        CheckBox ch1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox ch2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox ch3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox ch4 = (CheckBox) findViewById(R.id.checkBox4);
        CheckBox ch5 = (CheckBox) findViewById(R.id.checkBox5);
        CheckBox ch6 = (CheckBox) findViewById(R.id.checkBox6);
        CheckBox ch7 = (CheckBox) findViewById(R.id.checkBox7);
        CheckBox ch8 = (CheckBox) findViewById(R.id.checkBox8);
        CheckBox ch9 = (CheckBox) findViewById(R.id.checkBox9);
        CheckBox ch10 = (CheckBox) findViewById(R.id.checkBox10);

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch2 = (CheckBox) findViewById(R.id.checkBox2);
                if ( isChecked )
                    ch2.setClickable(false);
                else
                    ch2.setClickable(true);
            }
        });

        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch1= (CheckBox) findViewById(R.id.checkBox1);
                if ( isChecked )
                    ch1.setClickable(false);
                else
                    ch1.setClickable(true);
            }
        });

        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch4 = (CheckBox) findViewById(R.id.checkBox4);
                if ( isChecked )
                    ch4.setClickable(false);
                else
                    ch4.setClickable(true);
            }
        });

        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch3 = (CheckBox) findViewById(R.id.checkBox3);
                if ( isChecked )
                    ch3.setClickable(false);
                else
                    ch3.setClickable(true);
            }
        });

        ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch6 = (CheckBox) findViewById(R.id.checkBox6);
                if ( isChecked )
                    ch6.setClickable(false);
                else
                    ch6.setClickable(true);
            }
        });

        ch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch5 = (CheckBox) findViewById(R.id.checkBox5);
                if ( isChecked )
                    ch5.setClickable(false);
                else
                    ch5.setClickable(true);
            }
        });

        ch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch8 = (CheckBox) findViewById(R.id.checkBox8);
                if ( isChecked )
                    ch8.setClickable(false);
                else
                    ch8.setClickable(true);
            }
        });

        ch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch7 = (CheckBox) findViewById(R.id.checkBox7);
                if ( isChecked )
                    ch7.setClickable(false);
                else
                    ch7.setClickable(true);
            }
        });

        ch9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch10 = (CheckBox) findViewById(R.id.checkBox10);
                if ( isChecked )
                    ch10.setClickable(false);
                else
                    ch10.setClickable(true);
            }
        });

        ch10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBox ch9 = (CheckBox) findViewById(R.id.checkBox9);
                if ( isChecked )
                    ch9.setClickable(false);
                else
                    ch9.setClickable(true);
            }
        });
    }



}
