package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamproject.conn.ConnectionDetector;
import com.teamproject.conn.TurningOnGPS;
import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.GpsTracker;
import com.teamproject.functions.LineIntersection;
import com.teamproject.functions.ObjectsOnMap;
import com.teamproject.functions.RestController;
import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 008M on 2016-05-07.
 */
public class StartComp extends FragmentActivity implements OnMapReadyCallback {
    final Context context = this;
    boolean flaga1, startB, startComp, moznaWyslac, czySaNiewyslaneCzasy, przekroczonyStart, soundOn;
    private Marker now;
    private ImageButton buttonSound;
    private TextView info, info1, timerValue, info3;
    private com.google.android.gms.maps.GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    TurningOnGPS gps;
    private RadioGroup rg;
    GpsTracker gpstracker;
    Polyline polyliness;
    PolylineOptions route;
    ConnectionDetector cd;
    double szerokosc, dlugosc, start_xSr, start_ySr, j, y1, x1, y2, x2, tmpResult;
    float minX;
    float min;
    long startTime, estimatedTime;
    String szer, dl, time, nazwa_point, timeSend;
    List<String> trasa = new ArrayList<String>();
    List<String> pk_start = new ArrayList<String>();
    List<String> pk_pk = new ArrayList<String>();
    List<String> pk_meta = new ArrayList<String>();
    List<String> pk_all = new ArrayList<String>();
    List<String> pk_POI = new ArrayList<String>();
    List<String> nazwaPOI = new ArrayList<String>();
    List<String> ilPunktowPomiaru = new ArrayList<String>();
    List<String> czasyPrzebiegu = new ArrayList<String>();
    List<Double> countingPK = new ArrayList<Double>();
    List<Double> routeDouble = new ArrayList<Double>();
    List<Polyline> polylines = new ArrayList<Polyline>();
    LineIntersection line = new LineIntersection();
    DialogCommunications comm = new DialogCommunications(context);
    LatLng p1, p2, p3;
    Marker tmpm;
    Circle mapCircle;
    ObjectsOnMap oom = new ObjectsOnMap();
    double A[] = new double[2];
    double B[] = new double[2];
    int Z[];
    int il_poi, ile_route, il_pk, freq, gc, mn, makeLine, ilePomiarowCzasu, ktoryPomiar, i , pc, jk;
    private long startTime1, startTime2, timeBetween, timeBetween2, tmptime, timeInMilliseconds, timeSwapBuff, updatedTime = 0L;
    Date czasGPS1;
    private Handler customHandler = new Handler();
    final competitionDTO competition = CompList.comp;
    String ID_zaw = competition.getID_zawodow();
    final userDTO user1 = Login.user;
    String ID_usera = user1.getID_uzytkownika();
    int h=0, radius=4;
    MediaPlayer mediaPlayer;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startcomp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        soundOn=true;
        startB = true;
        tmpResult=501;
        startComp = true;
        sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        cd = new ConnectionDetector(getApplicationContext());
        gps = new TurningOnGPS(getApplicationContext());
        info = (TextView) findViewById(R.id.TextView2);
        info1 = (TextView) findViewById(R.id.TextView3);
        buttonSound = (ImageButton) findViewById(R.id.buttonSound);
        timerValue = (TextView) findViewById(R.id.timerValue);
        rg = (RadioGroup) findViewById(R.id.radio_group_list_selector);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.radio1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.radio2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case R.id.radio3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.radio4:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    default:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/gps/all?competition_id=" + ID_zaw;
        sendHttpRequest(url1, "GET", 0);
        buttonSound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            if(soundOn)
            {
                soundOn=false;
                Toast.makeText(StartComp.this, "Wyłączyłeś efekty dźwiękowe", Toast.LENGTH_SHORT).show();
                buttonSound.setBackgroundResource(R.mipmap.ic_volume_off);
            }
                else if (!soundOn){
                    soundOn=true;
                    Toast.makeText(StartComp.this, "Włączyłeś efekty dźwiękowe", Toast.LENGTH_SHORT).show();
                    buttonSound.setBackgroundResource(R.mipmap.ic_volume_on);
                }
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {
                comm.alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
            }
            @Override
            public void onLocationChanged(Location location) {
                //h++;
                if (now != null) {
                    now.remove();
                }
                flaga1 = true;
                szerokosc = location.getLatitude();
                dlugosc = location.getLongitude();
                oom.drawCenteredCircle(dlugosc, szerokosc, radius, Color.RED, mMap);
                gpstracker.stopUsingGPS();
                LatLng p2 = new LatLng(szerokosc, dlugosc);
                now = mMap.addMarker(new MarkerOptions().position(p2).title("Tu jesteś"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(p2));
//                if(h%2==0)
//                    startTime1 = (location.getTime());
//                if(h%2==1){
//                    timeBetween2 = location.getTime() - startTime1;
//                    czasGPS1 = new Date(timeBetween2- TimeUnit.HOURS.toMillis(1));
//                    timeSend = sdf.format(czasGPS1);
//                    Toast.makeText(StartComp.this, timeSend, Toast.LENGTH_SHORT).show();
//                }
                if (startComp) {
                    if (Z[ktoryPomiar] != 0) ktoryPomiar++;
                    if (makeLine == 0) {
                        A[0] = dlugosc;
                        A[1] = szerokosc;
                    } else {
                        B[0] = dlugosc;
                        B[1] = szerokosc;
                        startTime2 = location.getTime();
                        if (startTime1!=0) {
                            timeBetween = location.getTime() - startTime1;
                        }
                        if(timeMeasureByCircle(dlugosc, szerokosc, 4*ktoryPomiar, location)!=1)
                        timeMeasure(A[0], A[1], B[0], B[1], 4 * ktoryPomiar, location);
                        A[0] = B[0];
                        A[1] = B[1];
                    }
                    countDistance(dlugosc, szerokosc, countingPK, pc, location);
                    if((przekroczonyStart)&&(makeLine%3==0)&&(routeDouble.size()>1)) ifIsFarAwayFromRoute(dlugosc, szerokosc);
                    makeLine++;
                }
                if (cd.isConnectingToInternet() && czySaNiewyslaneCzasy) {
                    //tutaj wysyalnie tablicy
                    String nrPoints = ilPunktowPomiaru.toString();
                    String timeOnPoint = czasyPrzebiegu.toString();
                    nrPoints = nrPoints.substring(1);
                    nrPoints = nrPoints.substring(0, nrPoints.length() - 1);
                    nrPoints = nrPoints.replaceAll("\\s+", "");
                    timeOnPoint = timeOnPoint.substring(1);
                    timeOnPoint = timeOnPoint.substring(0, timeOnPoint.length() - 1);
                    timeOnPoint = timeOnPoint.replaceAll("\\s+", "");
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+nrPoints+"&time="+timeOnPoint;
                    sendHttpRequest(url2, "PUT", 1);
                    czySaNiewyslaneCzasy = false;
                }

            }
        };

        if (gps.checkingGPSStatus()) {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            if (!flaga1) {
                gpstracker = new GpsTracker(this){
                    @Override
                    public void onLocationReceived(double latitude, double longitude) {
                        if (now != null) {
                            now.remove();
                        }
                        szerokosc = latitude;
                        dlugosc = longitude;
                        LatLng p2 = new LatLng(szerokosc, dlugosc);
                        now = mMap.addMarker(new MarkerOptions().position(p2).title("Tu jesteś"));
                        if (jk == 0)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p2, 15F));
                        jk = 1;
                    }

                };
                szerokosc = gpstracker.getLatitude();
                dlugosc = gpstracker.getLongitude();
            }
        } else {
            comm.alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Pomiar czasu");
        alertDialogBuilder
                .setMessage("Czy na pewno chcesz przerwać pomiar czasu?")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (gpstracker != null)
                            gpstracker.stopUsingGPS();
                        if (locationManager != null)
                            locationManager.removeUpdates(locationListener);
                        ((Activity) context).finish();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
        mMap = googleMap;

        if (szerokosc != 0) {
            LatLng p1 = new LatLng(szerokosc, dlugosc);
            now = mMap.addMarker(new MarkerOptions().position(p1).title("Tu jesteś"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p1));
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(p1, 15F);
            mMap.animateCamera(cu);
        }
    }
    public void ifIsFarAwayFromRoute(double x, double y){
        min = Float.MAX_VALUE;
        for(int xc=0;xc<routeDouble.size(); xc=xc+2) {
            minX = checkDistanceFromRoute(x, y, routeDouble, xc);
            if(minX<min) min=minX;
        }
        if(min>500.00) {
            Toast.makeText(StartComp.this, "Opuściłeś trasę!", Toast.LENGTH_SHORT).show();
            playVoiceSound(R.raw.opuscilestrase);
        }
    }
    public void playSound(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playVoiceSound(int id){
        if(soundOn) {
            mediaPlayer = MediaPlayer.create(this, id);
            mediaPlayer.start();
        }
    }
    public int whichPKSound(int tmp){
        int Rx=11;
        switch(tmp){
            case 1 :
                Rx=R.raw.pk1;
                break;
            case 2 :
                Rx=R.raw.pk2;
                break;
            case 3 :
                Rx=R.raw.pk3;
                break;
            case 4 :
                Rx=R.raw.pk4;
                break;
            case 5 :
                Rx=R.raw.pk5;
                break;
            case 6 :
                Rx=R.raw.pk6;
                break;
            case 7 :
                Rx=R.raw.pk7;
                break;
            case 8 :
                Rx=R.raw.pk8;
                break;
            case 9 :
                Rx=R.raw.pk9;
                break;
            case 10 :
                Rx=R.raw.pk10;
                break;
            default : Rx=R.raw.pkx;
        }
        return Rx;
    }
    public void timeMeasure(double x1, double y1, double x2, double y2, int z, Location location) {
        if (polyliness != null) polyliness.remove();
        polyliness = this.mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(y1, x1), new LatLng(y2, x2))
                .width(5).color(Color.RED));
        if ((line.przynaleznosc(countingPK.get(z + 1), countingPK.get(z), countingPK.get(z + 3), countingPK.get(z + 2), x1, y1) == 1)
                || (line.przynaleznosc(countingPK.get(z + 1), countingPK.get(z), countingPK.get(z + 3), countingPK.get(z + 2), x2, y2) == 1)
                || (line.przynaleznosc(x1, y1, x2, y2, countingPK.get(z + 1), countingPK.get(z)) == 1)
                || (line.przynaleznosc(x1, y1, x2, y2, countingPK.get(z + 3), countingPK.get(z + 2)) == 1)) {
            if(z==0){
                if(line.przynaleznosc(countingPK.get(z+1), countingPK.get(z),countingPK.get(z+3), countingPK.get(z+2), x1, y1)==1){
                    startTime1 = location.getTime();
                    startTime = SystemClock.uptimeMillis();
                }
                info1.setText("Rozpocząłeś wyścig");
                playVoiceSound(R.raw.rozpoczaleswyscig);
                customHandler.postDelayed(updateTimerThread, 0);
                Z[0]=1;
                pc=pc+4;
                przekroczonyStart = true;
            }
            else if(z==pk_all.size()-4){
                if(line.przynaleznosc(countingPK.get(z+1), countingPK.get(z),countingPK.get(z+3), countingPK.get(z+2), x1, y1)==1){
                    timeBetween2 = location.getTime() - startTime1;
                }
                customHandler.removeCallbacks(updateTimerThread);
                czasGPS1 = new Date(timeBetween2- TimeUnit.HOURS.toMillis(1));
                timeSend = sdf.format(czasGPS1);
                if (cd.isConnectingToInternet()) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT", 1);
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    comm.alertDialog("Pomiar czasu", "Gratulacje, zakończyłeś wyścig! Proszę włączyć połączenie z siecią aby przesłać wyniki do bazy");
                    czySaNiewyslaneCzasy=true;
                }
                info1.setText("Zakończyłeś wyścig");
                playVoiceSound(R.raw.zakonczyleswyscig);
                info.setText("Koniec");
                startComp=false;
            }
            else{
                if(line.przynaleznosc(countingPK.get(z+1), countingPK.get(z), countingPK.get(z + 3), countingPK.get(z + 2), x1, y1) == 1) {
                    timeBetween2 = location.getTime() - startTime1;
                }
                czasGPS1 = new Date(timeBetween2- TimeUnit.HOURS.toMillis(1));
                timeSend = sdf.format(czasGPS1);
                info1.setText("Przekroczyłeś punkt kontrolny nr: " + z / 4 + " w czasie " + timeSend);
                playVoiceSound(whichPKSound(z/4));
                if (cd.isConnectingToInternet()) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT", 0);
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    czySaNiewyslaneCzasy=true;
                }
                Z[ktoryPomiar] = 1;
                pc = pc + 4;
            }
        } else if ((line.det_matrix(countingPK.get(z + 1), countingPK.get(z), countingPK.get(z + 3), countingPK.get(z + 2), x1, y1)) *
                (line.det_matrix(countingPK.get(z + 1), countingPK.get(z), countingPK.get(z + 3), countingPK.get(z + 2), x2, y2)) >= 0)
            ;
        else if ((line.det_matrix(x1, y1, x2, y2, countingPK.get(z + 1), countingPK.get(z))) *
                (line.det_matrix(x1, y1, x2, y2, countingPK.get(z + 3), countingPK.get(z + 2))) >= 0)
            ;
        else {
            if (z == 0) {
                info1.setText("Rozpocząłeś wyścig");
                playVoiceSound(R.raw.rozpoczaleswyscig);
                startTime1 = (location.getTime() + startTime2) / 2;
                startTime = (SystemClock.uptimeMillis() + (startTime2 - startTime1));
                customHandler.postDelayed(updateTimerThread, 0);
                Z[0] = 1;
                pc = pc + 4;
                przekroczonyStart = true;
            } else if (z == pk_all.size() - 4) {
                timeBetween2 = (location.getTime()-startTime2)/2 + timeBetween;
                customHandler.removeCallbacks(updateTimerThread);
                czasGPS1 = new Date(timeBetween2- TimeUnit.HOURS.toMillis(1));
                timeSend = sdf.format(czasGPS1);
                info1.setText("Zakończyłeś wyścig");
                playVoiceSound(R.raw.zakonczyleswyscig);
                if (cd.isConnectingToInternet()) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT", 1);
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    comm.alertDialog("Pomiar czasu", "Gratulacje, zakończyłeś wyścig! Proszę włączyć połączenie z siecią aby przesłać wyniki do bazy");
                    czySaNiewyslaneCzasy=true;
                }
                info.setText("Koniec");
                startComp = false;
            } else {
                timeBetween2 = (location.getTime()-startTime2)/2 + timeBetween;
                czasGPS1 = new Date(timeBetween2- TimeUnit.HOURS.toMillis(1));
                timeSend = sdf.format(czasGPS1);
                info1.setText("Przekroczyłeś punkt kontrolny nr: " + z / 4 + " w czasie " + timeSend);
                playVoiceSound(whichPKSound(z / 4));
                if (cd.isConnectingToInternet()) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT", 0);
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    czySaNiewyslaneCzasy=true;
                }
                Z[ktoryPomiar] = 1;
                pc = pc + 4;
            }
        }
    }
    public int timeMeasureByCircle(double x1, double y1, int z, Location location)
    {
        int wy=0;
        float[] distance = new float[1];
        float[] distance1 = new float[1];
        Location.distanceBetween(y1, x1,countingPK.get(z), countingPK.get(z + 1), distance);
        Location.distanceBetween(y1, x1, countingPK.get(z+2),countingPK.get(z + 3), distance1);

        if ((distance[0] < radius) || (distance1[0] < radius)) {
            if (z == 0) {
                info1.setText("Rozpocząłeś wyścig");
                playVoiceSound(R.raw.rozpoczaleswyscig);
                startTime1 = location.getTime();
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                Z[0] = 1;
                pc = pc + 4;
                przekroczonyStart = true;
            } else if (z == pk_all.size() - 4) {
                timeBetween2 = location.getTime() - startTime1;
                customHandler.removeCallbacks(updateTimerThread);
                czasGPS1 = new Date(timeBetween2- TimeUnit.HOURS.toMillis(1));
                timeSend = sdf.format(czasGPS1);
                info1.setText("Zakończyłeś wyścig");
                playVoiceSound(R.raw.zakonczyleswyscig);
                if (cd.isConnectingToInternet()) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT", 1);
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    comm.alertDialog("Pomiar czasu", "Gratulacje, zakończyłeś wyścig! Proszę włączyć połączenie z siecią aby przesłać wyniki do bazy");
                    czySaNiewyslaneCzasy=true;
                }
                info.setText("Koniec");
                startComp = false;
            } else {
                timeBetween2 = location.getTime() - startTime1;
                czasGPS1 = new Date(timeBetween2- TimeUnit.HOURS.toMillis(1));
                timeSend = sdf.format(czasGPS1);
                info1.setText("Przekroczyłeś punkt kontrolny nr: " + z / 4 + " w czasie " + timeSend);
                playVoiceSound(whichPKSound(z / 4));
                if (cd.isConnectingToInternet()) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT", 0);
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    czySaNiewyslaneCzasy=true;
                }
                Z[ktoryPomiar] = 1;
                pc = pc + 4;
            }
            wy=1;
        }
        return wy;
    }
    public void changeStringToDoubles(List<String> p, int k) {
        if(k==0) {
            for (int g = 0; g < p.size(); g++) {
                countingPK.add(Double.parseDouble(p.get(g)));
            }
        }
        else if(k==1){
            for (int g = 0; g < p.size(); g++) {
                routeDouble.add(Double.parseDouble(p.get(g)));
            }
        }
    }
    public float checkDistanceFromRoute(double x, double y, List<Double> p, int d) {
        start_ySr = p.get(d);
        start_xSr = p.get(d + 1);
        float[] results = new float[1];
        Location.distanceBetween(y, x,
                start_ySr, start_xSr, results);
        results[0] *= 100;
        results[0] = Math.round(results[0]);
        results[0] /= 100;
        return results[0];
    }
    public void countDistance(double x, double y, List<Double> p, int d, Location loc) {
        start_ySr = (p.get(d) + p.get(d + 2)) / 2;
        start_xSr = (p.get(d + 1) + p.get(d + 3)) / 2;
        float[] results = new float[1];
        Location.distanceBetween(y, x,
                start_ySr, start_xSr, results);
        results[0] *= 100;
        results[0] = Math.round(results[0]);
        results[0] /= 100;
        String wynikk = String.valueOf(results[0]);
        if ((results[0] > 500) && !(tmpResult>500)) {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }
        else if (((results[0] < 500) && (results[0] > 200))&& !(tmpResult<500) && !(tmpResult>200)){
            locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
        }
        else if ((results[0] < 200) && !(tmpResult<200)){
            locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
         }
        if (ktoryPomiar == 0) {
            info.setText("Odległość do startu: " + wynikk + "m");
        } else if (4 * ktoryPomiar == pk_all.size() - 4) {
            if (startComp) info.setText("Odległość do mety: " + wynikk + "m");
        } else
            info.setText("Odległość do najbliższego punktu pomiaru czasu: " + wynikk + "m");
        tmpResult = results[0];
    }
    public void sendHttpRequest(String url, final String operation, final int k){
        RestController rc = new RestController(this){
            @Override
            public void onResponseReceived(String result) {
                if(operation.equals("GET")) {
                    try {
                        parsingJSON(result);
                    } catch (JSONException e) {
                        //Toast.makeText(StartComp.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                if(k==1){
                    if (!startComp) comm.alertDialog("Pomiar czasu", "Poprawnie przesłano wszystkie wyniki do bazy");
                }
            }
        };
        rc.setAddress(url);
        rc.setOperation(operation);
        if(k==1) rc.setShowPD(true);
        else rc.setShowPD(false);
        rc.execute();
    }
    public void parsingJSON(String JSON) throws JSONException {
            JSONArray jsonarray = new JSONArray(JSON);
            JSONObject checkpoints = jsonarray.getJSONObject(0);
            JSONObject poi = jsonarray.getJSONObject(1);
            JSONObject route = jsonarray.getJSONObject(2);

            pk_start.add(checkpoints.getString("START1y"));
            pk_start.add(checkpoints.getString("START1x"));
            pk_start.add(checkpoints.getString("START2y"));
            pk_start.add(checkpoints.getString("START2x"));
            pk_all.addAll(pk_start);
            String ilosc_pk = checkpoints.getString("COUNT");
            il_pk = Integer.parseInt(ilosc_pk);
            for (int i = 0; i < il_pk; i++) {
                pk_pk.add(checkpoints.getString("PUNKT" + i + "Ay"));
                pk_pk.add(checkpoints.getString("PUNKT" + i + "Ax"));
                pk_pk.add(checkpoints.getString("PUNKT" + i + "By"));
                pk_pk.add(checkpoints.getString("PUNKT" + i + "Bx"));
            }
            pk_all.addAll(pk_pk);
            pk_meta.add(checkpoints.getString("META1y"));
            pk_meta.add(checkpoints.getString("META1x"));
            pk_meta.add(checkpoints.getString("META2y"));
            pk_meta.add(checkpoints.getString("META2x"));
            pk_all.addAll(pk_meta);
            ilePomiarowCzasu = pk_all.size() / 4;
            Z = new int[ilePomiarowCzasu];
            changeStringToDoubles(pk_all, 0);
            if (JSON.contains("POINT_POINAME")) {
                String ilosc_poi = poi.getString("COUNT");
                il_poi = Integer.parseInt(ilosc_poi);
                for (int i = 0; i < il_poi; i++) {
                    pk_POI.add(poi.getString("POINT_POIY" + i));
                    pk_POI.add(poi.getString("POINT_POIX" + i));
                    nazwaPOI.add(poi.getString("POINT_POINAME" + i));
                }
            }
          oom.setPOI(pk_POI, nazwaPOI, BitmapDescriptorFactory.HUE_VIOLET, il_poi, mMap);

          if(JSON.contains("POINTX0")) {
                String ilosc_track = route.getString("COUNT");
                ile_route = Integer.parseInt(ilosc_track);
                for (int i = 0; i < ile_route; i++) {
                    trasa.add(route.getString("POINTY" + i));
                    trasa.add(route.getString("POINTX" + i));
                }
                oom.drawRoute(trasa, mMap, polylines);
                changeStringToDoubles(trasa, 1);
            }
        oom.setPoint(pk_start, "Start ", BitmapDescriptorFactory.HUE_AZURE, 0, mMap);
        oom.setPoint(pk_pk, "Punkt kontrolny nr ", BitmapDescriptorFactory.HUE_ORANGE, 1, mMap);
        oom.setPoint(pk_meta, "Meta ", BitmapDescriptorFactory.HUE_GREEN, 0, mMap);

        oom.drawLine(pk_start, Color.BLUE, mMap);
        oom.drawLine(pk_pk, Color.parseColor("#FF6600"), mMap);
        oom.drawLine(pk_meta, Color.GREEN, mMap);

    }
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;;
            time = timeFormat(updatedTime);
            timerValue.setText(time);
            customHandler.postDelayed(this, 0);
        }
    };
    public String timeFormat(long time) {
        int secs = (int) (time / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        secs = secs % 60;
        mins = mins % 60;
        int milliseconds = (int) (time % 1000);
        String ret = String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds);
        return ret;
    }
}
