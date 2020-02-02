package com.example.sos_app_ui.ui.configuration;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.sos_app_ui.R;


/**
 *  Created by Daniel Duczymiński
 *
 *  That is an activity with additional options for app (still in progress)
 *  Here user can check or uncheck options he wants to add
 */

public class AdditionalSettingsPanel extends AppCompatActivity {

    private CheckBox ch1;
    private CheckBox ch2;
    private CheckBox ch3;
    private CheckBox ch4;
    private CheckBox ch5;
    private CheckBox ch6;
    private CheckBox ch7;
    private CheckBox ch8;
    private CheckBox ch9;
    private CheckBox ch10;
    private Button btn;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_settings_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkBoxSettings();
    }

    /**
     * Method that fill window with all options checkboxes (needs to be optimized)
     */
    public void checkBoxSettings()
    {
        this.ch1 = findViewById(R.id.checkBox1);
        this.ch2 = findViewById(R.id.checkBox2);
        this.ch3 = findViewById(R.id.checkBox3);
        this.ch4 = findViewById(R.id.checkBox4);
        this.ch5 = findViewById(R.id.checkBox5);
        this.ch6 = findViewById(R.id.checkBox6);
        this.ch7 = findViewById(R.id.checkBox7);
        this.ch8 = findViewById(R.id.checkBox8);
        this.ch9 = findViewById(R.id.checkBox9);
        this.ch10 = findViewById(R.id.checkBox10);


        btn = findViewById(R.id.confirmBtn4);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AdditionalSettingsPanel.this);
                builder1.setMessage("You did not fill all the options. Would you like to continue?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();


                if(!ch1.isChecked() && !ch2.isChecked())
                    alert11.show();
                else if(!ch3.isChecked() && !ch4.isChecked())
                    alert11.show();
                else if(!ch5.isChecked() && !ch6.isChecked())
                    alert11.show();
                else if(!ch7.isChecked() && !ch8.isChecked())
                    alert11.show();
                else if(!ch9.isChecked() && !ch10.isChecked())
                    alert11.show();
                else
                {
                    Intent i = new Intent(AdditionalSettingsPanel.this,PopActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch2.setChecked(false);
                    ch2.setSelected(false);


                    if (ContextCompat.checkSelfPermission(AdditionalSettingsPanel.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(AdditionalSettingsPanel.this, "You have already granted this permission!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        requestLocationAccess();
                    }
                }
                else
                {

                }

            }
        });

        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch1.setChecked(false);
                    ch1.setSelected(false);
                }
            }
        });

        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch4.setChecked(false);
                    ch4.setSelected(false);
                }
            }
        });

        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch3.setChecked(false);
                    ch3.setSelected(false);
                }
            }
        });

        ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch6.setChecked(false);
                    ch6.setSelected(false);
                }
            }
        });

        ch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch5.setChecked(false);
                    ch5.setSelected(false);
                }
            }
        });

        ch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch8.setChecked(false);
                    ch8.setSelected(false);
                }
            }
        });

        ch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch7.setChecked(false);
                    ch7.setSelected(false);
                }
            }
        });

        ch9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch10.setChecked(false);
                    ch10.setSelected(false);
                }
            }
        });

        ch10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    ch9.setChecked(false);
                    ch9.setSelected(false);
                }
            }
        });
    }

    private void requestLocationAccess() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("If you don't agree, warnings will be send without your location. Do you want to continue?")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AdditionalSettingsPanel.this,
                                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



}