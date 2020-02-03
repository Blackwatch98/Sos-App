package com.example.sos_app_ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


public class Localization
        extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        System.out.println("Moj gps: "+gps());
                    }
                });
            }
        }, 0, 100);
    }


    public String gps(){
        String cord = "Wspolrzedne";

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        DecimalFormat df = new DecimalFormat("##.####");
        double longitude = gps.getLongitude();
        cord = df.format(latitude) + " " + df.format(longitude);
        return cord;
    }

}

class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    boolean isGPSEnabled = true; //status gps

    boolean isNetworkEnabled = true; //status sieci

    boolean canGetLocation = true; //sprawdzenie czy mozna pobrac lokalizacje

    Location location; // lokalizacja
    double latitude; // szerokosc geograficzna
    double longitude; // dlugosc geograficzna

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters minimalny dystans do aktualizacji

    private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1; // 5 seconds minimalny czas miedzy aktualizacjami

    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER); //pobranie statusu GPS

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER); //pobranie statusu sieci


            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                //GPS jest dostepny pobierz parametry
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
        }
        return location;
    }

    /**
     * Funkcja do pobrania szerokosci geograficznej
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Funkcja do pobrania dlugosci geograficznej
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }


    /**
     * Funkcja do zatrzymania uzywania GPS
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }




    /**
     * Funkcja sprawdzajaca czy WiFi i GPS jest wlaczony
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */

    @Override
    public void onLocationChanged(Location location) {
        float bestAccuracy = -1f;
        if (location.getAccuracy() != 0.0f
                && (location.getAccuracy() < bestAccuracy) || bestAccuracy == -1f) {
            locationManager.removeUpdates(this);
        }
        bestAccuracy = location.getAccuracy();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
