package com.example.sos_app_ui;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.sos_app_ui.background_service.BackgroundNotificationService;
import com.example.sos_app_ui.logs.LastActivityFragment;
import com.example.sos_app_ui.logs.model.LogModel;
import com.example.sos_app_ui.ui.configuration.AndroidContact;
import com.example.sos_app_ui.ui.configuration.ConfigurationFragment;
import com.example.sos_app_ui.ui.configuration.CurrentConfiguration;
import com.example.sos_app_ui.ui.configuration.MessagePanel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private CurrentConfiguration workingConfig;
    private ListView listView_Android_Contacts;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public static boolean activityOn;
    public static boolean smsFlag = true;

    public static int getMyPermissionsRequestSendSms() {
        return MY_PERMISSIONS_REQUEST_SEND_SMS;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getConfig();
        if (!checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);

        if (!checkPermission(android.Manifest.permission.SEND_SMS))
            requestPermission(android.Manifest.permission.SEND_SMS, MY_PERMISSIONS_REQUEST_SEND_SMS);

        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
        }

        activityOn = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.locaution_white);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_logs)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void getConfig()
    {
        Intent i = getIntent();
        this.workingConfig = (CurrentConfiguration)i.getSerializableExtra("FinalConfig");
        /*
        if(workingConfig != null)
            workingConfig.display();
        */
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission, int permissionCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, permissionCode);
        }
    }

    public String gps(){
        String cord = "Wspolrzedne";

        GPSTracker gpsTracker = new GPSTracker(this);
        double latitude = gpsTracker.getLatitude();
        DecimalFormat df = new DecimalFormat("##.####");
        double longitude = gpsTracker.getLongitude();
        cord = df.format(latitude) + " " + df.format(longitude);
        return cord;
    }

    public static int setSmsFlag(boolean toSend) {
        System.out.println("set flag to " + toSend);
        smsFlag = toSend;
        return 0;
    }

    public static int sendSms(Context context) {
        System.out.println("flag is " + smsFlag);
        if (smsFlag) {
            String phoneNo = "500859950";
//            CurrentConfiguration config = ConfigurationFragment.getWorkingConf();
//            List<AndroidContact> contacts = config.getTargets();
//            StringBuilder textMessage = new StringBuilder(config.getMessageText());
//            if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//                textMessage.append("My location is: ")
//                        .append(MainActivity.gps());
//            }
            SmsManager smsManager = SmsManager.getDefault();
//            for (AndroidContact contact : contacts) {
//                smsManager.sendTextMessage(contact.android_contact_TelefonNr,
//                        null,
//                        textMessage.toString(),
//                        BackgroundNotificationService.sentPI,
//                        null);
//            }
            smsManager.sendTextMessage(phoneNo,
                        null,
                        "Something bad might have happened to me.",
                        BackgroundNotificationService.sentPI,
                        null);
            System.out.println("sent");
            BackgroundNotificationService.createNotification("SOS", "messages sent!", context);
            MainActivity.setSmsFlag(true);
            return 0;
        } else {
            System.out.println("cancelled");
            MainActivity.setSmsFlag(true);
            return 1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive.");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive.");
                }
                break;
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can send SMS.");
                } else {
                    Log.e("value", "Permission Denied, You cannot send SMS.");
                }
            }
            return;
        }
    }
}