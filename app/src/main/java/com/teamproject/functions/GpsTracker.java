package com.teamproject.functions;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by 008M on 2016-05-21.
 */

public class GpsTracker extends Service implements LocationListener, GpsIF {

    private Context mcontext;

    boolean isGPSEnabled = false;
    boolean canGetLocation = false;
    boolean isNetworkEnabled = false;
    Location location;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;

    protected LocationManager locationManager;

    public GpsTracker(Context context) {
        this.mcontext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("GPS");
        alertDialog.setMessage("GPS jest wyłączony. Chcesz przejść do ustawień?");
        alertDialog.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mcontext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        showSettingAlert();
    }
    @Override
    public void onLocationChanged(Location location) {
        onLocationReceived(location.getLatitude(), location.getLongitude());
        // TODO Auto-generated method stub
//        if (now != null) {
//            now.remove();
//        }
//        szerokosc = location.getLatitude();
//        dlugosc = location.getLongitude();
//        if(nagrywanie){
//            szer = Double.toString(szerokosc);
//            dl = Double.toString(dlugosc);
//            trasa.add(dl);
//            trasa.add(szer);
//            if(trasa.size()>3)
//                drawRoute(trasa);
//        }
//        LatLng p2 = new LatLng(szerokosc, dlugosc);
//        if(nagrywanie){
//            now = mMap.addMarker(new MarkerOptions()
//                    .position(p2)
//                    .title("Tu jesteś")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//        }
//        else
//            now = mMap.addMarker(new MarkerOptions().position(p2).title("Tu jesteś"));
//        if (jk==0)
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p2, 16F));
//        else mMap.moveCamera(CameraUpdateFactory.newLatLng(p2));
//        jk=1;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onLocationReceived(double x, double y) {

    }
}
