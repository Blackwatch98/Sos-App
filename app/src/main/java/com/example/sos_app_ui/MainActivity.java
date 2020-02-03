package com.example.sos_app_ui;


import android.Manifest;
import android.app.Activity;
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
import com.example.sos_app_ui.ui.configuration.CurrentConfiguration;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private CurrentConfiguration workingConfig;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public static boolean activityOn;
    public static boolean smsFlag = true;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getConfig();
        checkPermissions(this);

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

    private static void checkPermissions(Context context){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void getConfig()
    {
        Intent i = getIntent();
        this.workingConfig = (CurrentConfiguration)i.getSerializableExtra("FinalConfig");
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
            String phoneNo = "604584611";
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
            BackgroundNotificationService.createNotification("SOS", "messages have been sent!", context);
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