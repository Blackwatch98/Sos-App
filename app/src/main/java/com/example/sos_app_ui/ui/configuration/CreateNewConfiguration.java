package com.example.sos_app_ui.ui.configuration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sos_app_ui.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateNewConfiguration extends AppCompatActivity {

    private CurrentConfiguration conf;
    private CurrentConfiguration validateClass;
    private Context context;
    private ListView list;
    private Button btn;
    private String[] functions = {"Personal Data", "Message", "Warning Targets", "Settings"};
    private ArrayList<String> funList;
    private boolean isFilled[] = {false,false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_configuration);

        list = (ListView) findViewById(R.id.listView2);


        conf = new CurrentConfiguration();
        validateClass = new CurrentConfiguration();
        context = this;


        btn = findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validateClass.validateChanges(conf)) {
                    String path = context.getExternalFilesDir("Configurations").toString();
                    File directory = new File(path);
                    File[] files = directory.listFiles();

                    String name = "Config" + (files.length + 1) + ".txt";
                    conf.writeConfigToFile(name, conf, context);

                    Bundle args = new Bundle();
                    args.putString("fileName", name);
                    args.putSerializable("fullyConfig", (Serializable) conf);
                    Intent resultIntent = new Intent(v.getContext(),
                            ConfigurationFragment.class);
                    resultIntent.putExtras(args);
                    setResult(RESULT_OK, resultIntent);

                    finish();
                }
                else
                {
                    Toast.makeText(v.getContext(), "You did not fill everything properly!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        funList = new ArrayList<String>();
        funList.addAll(Arrays.asList(functions));

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row, funList);


        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                if (position == 0) {
                    Intent appInfo = new Intent(view.getContext(), PersonalDataPanel.class);
                    startActivityForResult(appInfo, 1);
                }
                if (position == 1) {
                    Intent appInfo = new Intent(view.getContext(), MessagePanel.class);
                    startActivityForResult(appInfo, 2);
                }
                if (position == 2) {
                    Intent appInfo = new Intent(view.getContext(), WarningTargets.class);
                    startActivityForResult(appInfo, 3);
                }
                if (position == 3) {
                    Intent appInfo = new Intent(view.getContext(), AdditionalSettingsPanel.class);
                    startActivity(appInfo);
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1) {
            if (data != null) {
                Bundle extras = data.getExtras();
                conf.setfName(extras.get("fname").toString());
                conf.setsName(extras.get("sname").toString());
                conf.setAge(Integer.valueOf(extras.get("age").toString()));
                isFilled[0] = true;
            }
        }
            if (requestCode == 2) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    conf.setMessageText(extras.get("message").toString());
                    isFilled[1] = true;
                }
            }
            if (requestCode == 3) {
                if (data != null) {
                    Bundle args = data.getBundleExtra("finalList");
                    conf.setTargets((ArrayList<Android_Contact>) args.getSerializable("fList"));
                    isFilled[2] = true;
                }
            }

            list.setAdapter(new ArrayAdapter<String>(this, R.layout.row, funList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View row = super.getView(position, convertView, parent);

                    if(isFilled[position])
                        row.setBackgroundColor (Color.GREEN);
                    return row;
                }
            });

        }
    }
