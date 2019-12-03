package com.example.sos_app_ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView_Android_Contacts;
    private ListView list ;
    private ArrayAdapter<String> adapter ;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        checkPermission();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //loadConfigurationListView();
    }

    public void loadConfigurationListView(){
        list = (ListView) findViewById(R.id.listView1);

        String cars[] = {"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen"};

        ArrayList<String> carL = new ArrayList<String>();
        carL.addAll( Arrays.asList(cars) );

        adapter = new ArrayAdapter<String>(this, R.layout.row, carL);

        list.setAdapter(adapter);
    }

    public void btnLoad_AndroidContacts_onClick(View view) {
        listView_Android_Contacts = (ListView) this.findViewById(R.id.listView_Android_Contacts);
        fp_get_Android_Contacts();
    }


    //=============================< Functions: Android.Contacts >=============================
    public class Android_Contact {
        //----------------< fritzbox_Contacts() >----------------
        public String android_contact_Name = "";
        public String android_contact_TelefonNr = "";
        public int android_contact_ID=0;
        //----------------</ fritzbox_Contacts() >----------------
    }

    public void fp_get_Android_Contacts() {
        //----------------< fp_get_Android_Contacts() >----------------
        ArrayList<Android_Contact> arrayList_Android_Contacts = new ArrayList<Android_Contact>();

        //--< get all Contacts >--
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }
        //--</ get all Contacts >--

        //----< Check: hasContacts >----
        if(cursor_Android_Contacts != null) {
            if (cursor_Android_Contacts.getCount() > 0) {
                //----< has Contacts >----
                //----< @Loop: all Contacts >----
                while (cursor_Android_Contacts.moveToNext()) {
                    //< init >
                    Android_Contact android_contact = new Android_Contact();
                    String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                    String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    System.out.println(contact_display_name);
                    //</ init >

                    //----< set >----
                    android_contact.android_contact_Name = contact_display_name;


                    //----< get phone number >----
                    int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {

                        Cursor phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                , null
                                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                                , new String[]{contact_id}
                                , null);

                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            System.out.println(phoneNumber);
                            //< set >
                            android_contact.android_contact_TelefonNr = phoneNumber;
                            //</ set >
                        }
                        phoneCursor.close();
                    }
                    //----</ set >----
                    //----</ get phone number >----

                    // Add the contact to the ArrayList
                    arrayList_Android_Contacts.add(android_contact);
                }
                //----</ @Loop: all Contacts >----

                //< show results >
                Adapter_for_Android_Contacts adapter = new Adapter_for_Android_Contacts(this, arrayList_Android_Contacts);
                listView_Android_Contacts.setAdapter(adapter);
                //</ show results >


            }
        }
            //----</ Check: hasContacts >----

            // ----------------</ fp_get_Android_Contacts() >----------------
    }

    public void checkPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    public class Adapter_for_Android_Contacts extends BaseAdapter {
        //----------------< Adapter_for_Android_Contacts() >----------------
            //< Variables >
        Context mContext;
        List<Android_Contact> mList_Android_Contacts;
        //</ Variables >

        //< constructor with ListArray >
        public Adapter_for_Android_Contacts(Context mContext, List<Android_Contact> mContact) {
            this.mContext = mContext;
            this.mList_Android_Contacts = mContact;
        }
        //</ constructor with ListArray >

        @Override
        public int getCount() {
            return mList_Android_Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return mList_Android_Contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //----< show items >----
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(mContext,R.layout.contactlist_items,null);
        //< get controls >
            TextView textview_contact_Name= (TextView) view.findViewById(R.id.textview_android_contact_name);
            TextView textview_contact_TelefonNr= (TextView) view.findViewById(R.id.textview_android_contact_phoneNr);
        //</ get controls >

        //< show values >
            textview_contact_Name.setText(mList_Android_Contacts.get(position).android_contact_Name);
            textview_contact_TelefonNr.setText(mList_Android_Contacts.get(position).android_contact_TelefonNr);
        //</ show values >


            view.setTag(mList_Android_Contacts.get(position).android_contact_Name);
            return view;
        }
        //----</ show items >----
        //----------------</ Adapter_for_Android_Contacts() >----------------
    }
        //=============================</ Functions: Android.Contacts >=============================
}
