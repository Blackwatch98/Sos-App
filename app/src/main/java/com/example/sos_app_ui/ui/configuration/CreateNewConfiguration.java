package com.example.sos_app_ui.ui.configuration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sos_app_ui.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateNewConfiguration extends AppCompatActivity {

    private CurrentConfiguration conf;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_configuration);

        ListView list = (ListView)findViewById(R.id.listView2);

        conf = new CurrentConfiguration();
        context = this;
        //conf.writeConfigToFile("Config1.txt",conf,context);

        String functions[] = {"Personal Data", "Message", "Warning Targets", "Settings"};

        ArrayList<String> funList = new ArrayList<String>();
        funList.addAll( Arrays.asList(functions) );

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row, funList);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                if(position == 0)
                {
                    Intent appInfo = new Intent(view.getContext(),PersonalDataPanel.class);
                    startActivityForResult(appInfo,1);
                }
                if(position == 1)
                {
                    Intent appInfo = new Intent(view.getContext(),MessagePanel.class);
                    startActivityForResult(appInfo,2);
                }
                if(position == 2)
                {
                    Intent appInfo = new Intent(view.getContext(),WarningTargets.class);
                    startActivityForResult(appInfo,3);
                }
                if(position == 3)
                {
                    Intent appInfo = new Intent(view.getContext(),AdditionalSettingsPanel.class);
                    startActivity(appInfo);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            Bundle extras = data.getExtras();
            conf.setfName(extras.get("fname").toString());
            conf.setsName(extras.get("sname").toString());
            conf.setAge(Integer.valueOf(extras.get("age").toString()));
        }
        if(requestCode == 2)
        {
            Bundle extras = data.getExtras();
            conf.setMessageText(extras.get("message").toString());
            conf.writeConfigToFile("Config1.txt", conf,context);
        }/*
        if(requestCode == 3)
        {
            Bundle extras = data.getExtras();
            conf.setfName(extras.get("fname").toString());
            conf.setsName(extras.get("sname").toString());
            conf.setAge(Integer.valueOf(extras.get("age").toString()));

            conf.writeConfigToFile("Config1.txt", conf,context);
        }*/

    }
}
