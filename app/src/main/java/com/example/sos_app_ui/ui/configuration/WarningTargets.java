package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sos_app_ui.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WarningTargets extends AppCompatActivity {

    private ListView ls;
    private ArrayList<Android_Contact> newContactsList;
    private Set<Android_Contact> currentContactsList;
    private ArrayList<Android_Contact> currentContactsList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_targets);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ls = findViewById(R.id.listView1);
        currentContactsList = new HashSet<Android_Contact>();
        currentContactsList2 = new ArrayList<Android_Contact>();

        Button btn = findViewById(R.id.chooseBtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent appInfo = new Intent(v.getContext(),ContactsList.class);
                startActivityForResult(appInfo,1);
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
                ls.setAdapter(adapter);
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
            currentContactsList.add(p1);
        }
        for(Android_Contact p1 : currentContactsList)
        {
            currentContactsList2.add(p1);
        }
    }

    class ContactsComparator implements Comparator<Android_Contact>
    {
        public int compare(Android_Contact c1, Android_Contact c2)
        {
            return c1.android_contact_Name.compareTo(c2.android_contact_Name);
        }
    }
}
