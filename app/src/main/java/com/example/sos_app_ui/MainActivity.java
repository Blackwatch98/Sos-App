package com.example.sos_app_ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sos_app_ui.ui.current_activity.CurrentActivityFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private ListView listView_Android_Contacts;
    private ListView list ;
    private ArrayAdapter<String> adapter ;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //checkPermissionContacts();
        //checkPermissionWriteExternalStorage();
        //checkPermissionReadExternalStorage();
        //checkPermissionLocation();

        if(!checkPermission())
            requestPermission();

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

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
        //listView_Android_Contacts = (ListView) this.findViewById(R.id.listView_Android_Contacts);
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

    public void checkPermissionContacts(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PERMISSION_GRANTED) {

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

    public void checkPermissionLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.LOCATION_HARDWARE)
                != PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.LOCATION_HARDWARE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    public void checkPermissionWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.LOCATION_HARDWARE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    public void checkPermissionReadExternalStorage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.LOCATION_HARDWARE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
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
