package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sos_app_ui.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Daniel Duczymiński
 *
 * This is an activity where user modifies final list of message targets
 * Contains previous, current lists and also set to eliminate duplicates
 */

public class WarningTargets extends AppCompatActivity {

    private ListView finalList;
    private ArrayList<AndroidContact> newContactsList;
    private Set<AndroidContact> currentContactsSet;
    private ArrayList<AndroidContact> currentContactsList2;
    private ArrayList<AndroidContact> removeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_targets);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        finalList = findViewById(R.id.listView1);
        currentContactsSet = new HashSet<AndroidContact>();
        currentContactsList2 = new ArrayList<AndroidContact>();
        removeList = new ArrayList<AndroidContact>();

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

                AndroidContactsAdapter adapter = new AndroidContactsAdapter(WarningTargets.this,  currentContactsList2, "dark");

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
                //System.out.println("ID " + i);
            }
        });

    }

    /**
     * Method to prevent losing content by activity when phone orientation is changed
     * It makes copy in memory to restore later
     * @param outState currenct state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putSerializable("currentList2", currentContactsList2);
        outState.putSerializable("currentSet", (Serializable)currentContactsSet);
        super.onSaveInstanceState(outState);
    }

    /**
     * Method to restore data from previous one
     */
    @Override
    protected  void onRestoreInstanceState(Bundle saveInstanceState)
    {
        super.onRestoreInstanceState(saveInstanceState);

        currentContactsList2 = (ArrayList)saveInstanceState.getSerializable("currentList2");
        currentContactsSet = (Set)saveInstanceState.getSerializable("currentSet");

        AndroidContactsAdapter adapter = new AndroidContactsAdapter(this,  currentContactsList2, "dark");
        finalList.setAdapter(adapter);

        for(AndroidContact p1 : currentContactsList2)
        {
            System.out.println(p1.getAndroid_contact_Name());
        }
    }

    /**
     * This method enable to exchange data between activities
     * @param requestCode which method
     * @param resultCode is data sent successfully
     * @param data  data that has been sent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle args = data.getBundleExtra("selectedContacts");
                newContactsList = (ArrayList<AndroidContact>) args.getSerializable("ARRAYLIST");

                tabelViewUpdater();

                Collections.sort(currentContactsList2, new Comparator<AndroidContact>() {
                    @Override
                    public int compare(AndroidContact c1, AndroidContact c2) {
                        return c1.android_contact_Name.compareTo(c2.android_contact_Name);
                    }
                });


                AndroidContactsAdapter adapter = new AndroidContactsAdapter(this,  currentContactsList2, "dark");

                finalList.setAdapter(adapter);
            }
            if(resultCode == RESULT_CANCELED)
            {
                System.out.println("Nothing selected");
            }
        }
    }

    /**
     * This method update final contacts list with new ones or delete chosen
     */
    public void tabelViewUpdater()
    {
        currentContactsList2.clear();
        for(AndroidContact p1 : newContactsList)
        {
            currentContactsSet.add(p1);
        }
        for(AndroidContact p1 : currentContactsSet)
        {
            currentContactsList2.add(p1);
        }
    }
}
