package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sos_app_ui.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class WarningTargets extends AppCompatActivity {

    private ListView finalList;
    private ArrayList<Android_Contact> newContactsList;
    private Set<Android_Contact> currentContactsSet;
    private ArrayList<Android_Contact> currentContactsList2;
    private ArrayList<Android_Contact> removeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_targets);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        finalList = findViewById(R.id.listView1);
        currentContactsSet = new HashSet<Android_Contact>();
        currentContactsList2 = new ArrayList<Android_Contact>();
        removeList = new ArrayList<Android_Contact>();

        Button btn = findViewById(R.id.chooseBtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent appInfo = new Intent(v.getContext(),ContactsList.class);
                startActivityForResult(appInfo,1);
            }
        });

        Button btn2 = findViewById(R.id.removeBtn);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(removeList.size() == 0)
                    Toast.makeText(WarningTargets.this, "No contacts marked", Toast.LENGTH_SHORT).show();

                currentContactsList2.removeAll(removeList);
                Adapter_for_Android_Contacts adapter = new Adapter_for_Android_Contacts(WarningTargets.this,  currentContactsList2);
                finalList.setAdapter(adapter);
            }
        });

        Button btn3 = findViewById(R.id.confirmBtn3);
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("fList",(Serializable)currentContactsList2);
                Intent resultIntent = new Intent(v.getContext(),
                        CreateNewConfiguration.class);
                resultIntent.putExtra("finalList",args);
                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });

        finalList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        finalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int color = Color.TRANSPARENT;
                Drawable background = view.getBackground();
                if (background instanceof ColorDrawable)
                    color = ((ColorDrawable) background).getColor();


                if(color == Color.TRANSPARENT)
                {
                    view.setBackgroundColor(Color.LTGRAY);
                    removeList.add(currentContactsList2.get(i));
                }
                else if(color == Color.LTGRAY)
                {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    removeList.remove(currentContactsList2.get(i));
                }
                System.out.println("ID " + i);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle args = data.getBundleExtra("selectedContacts");
                newContactsList = (ArrayList<Android_Contact>) args.getSerializable("ARRAYLIST");

                tabelViewUpdater();

                Collections.sort(currentContactsList2, new Comparator<Android_Contact>() {
                    @Override
                    public int compare(Android_Contact c1, Android_Contact c2) {
                        return c1.android_contact_Name.compareTo(c2.android_contact_Name);
                    }
                });

                Adapter_for_Android_Contacts adapter = new Adapter_for_Android_Contacts(this,  currentContactsList2);
                finalList.setAdapter(adapter);
            }
            if(resultCode == RESULT_CANCELED)
            {
                System.out.println("Nothing selected");
            }
        }
    }

    public void tabelViewUpdater()
    {
        currentContactsList2.clear();
        for(Android_Contact p1 : newContactsList)
        {
            currentContactsSet.add(p1);
        }
        for(Android_Contact p1 : currentContactsSet)
        {
            currentContactsList2.add(p1);
        }
    }
}