package com.teamproject.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamproject.conn.ConnectionDetector;
import com.teamproject.conn.TurningOnGPS;
import com.teamproject.functions.GpsTracker;
import com.teamproject.functions.LineIntersection;
import com.teamproject.functions.RestController;
import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 008M on 2016-05-07.
 */
public class StartComp extends FragmentActivity implements OnMapReadyCallback {
    final Context context = this;
    boolean flaga1, isInternetPresent, startB, startComp, moznaWyslac, czySaNiewyslaneCzasy;
    private Marker now;
    private TextView info, info1, timerValue, info3;
    private com.google.android.gms.maps.GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    TurningOnGPS gps;
    GpsTracker gpstracker;
    Polyline polyliness;
    PolylineOptions route;
    ConnectionDetector cd;
    double szerokosc, dlugosc, start_xSr, start_ySr, j, y1, x1, y2, x2, tmpResult;
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
    List<Polyline> polylines = new ArrayList<Polyline>();
    LineIntersection line = new LineIntersection();
    LatLng p1, p2, p3;
    Marker tmpm;
    double A[] = new double[2];
    double B[] = new double[2];
    int Z[];
    int il_poi, ile_route, il_pk, freq, gc, mn, makeLine, ilePomiarowCzasu, ktoryPomiar, i , pc, jk;
    private long startTime1, startTime2, timeBetween, timeBetween2, tmptime, timeInMilliseconds, timeSwapBuff, updatedTime = 0L;
    private Handler customHandler = new Handler();
    final competitionDTO competition = CompList.comp;
    String ID_zaw = competition.getID_zawodow();
    final userDTO user1 = Login.user;
    String ID_usera = user1.getID_uzytkownika();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startcomp);
        startB = true;
        tmpResult=501;
        startComp = true;
        cd = new ConnectionDetector(getApplicationContext());
        gps = new TurningOnGPS(getApplicationContext());
        info = (TextView) findViewById(R.id.TextView2);
        info1 = (TextView) findViewById(R.id.TextView3);
        timerValue = (TextView) findViewById(R.id.timerValue);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/gps/all?competition_id=" + ID_zaw;
        sendHttpRequest(url1, "GET");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {
                alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
            }
            @Override
            public void onLocationChanged(Location location) {
                if (now != null) {
                    now.remove();
                }
                flaga1 = true;
                szerokosc = location.getLatitude();
                dlugosc = location.getLongitude();
                gpstracker.stopUsingGPS();
                LatLng p2 = new LatLng(szerokosc, dlugosc);
                now = mMap.addMarker(new MarkerOptions().position(p2).title("Tu jesteś"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(p2));
                if (startComp) {
                    if (Z[ktoryPomiar] != 0) ktoryPomiar++;

                    if (makeLine == 0) {
                        A[0] = dlugosc;
                        A[1] = szerokosc;
                    } else {
                        B[0] = dlugosc;
                        B[1] = szerokosc;
                        startTime2 = System.currentTimeMillis();
                        if (startTime1!=0) timeBetween = System.currentTimeMillis() - startTime1;
                        timeMeasure(A[0], A[1], B[0], B[1], 4 * ktoryPomiar);
                        A[0] = B[0];
                        A[1] = B[1];
                    }
                    countDistance(dlugosc, szerokosc, countingPK, pc, location);
                    makeLine++;
                }
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent && czySaNiewyslaneCzasy) {
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
                    sendHttpRequest(url2, "PUT");
                    czySaNiewyslaneCzasy = false;
                }
            }
        };

        if (gps.checkingGPSStatus()) {
            locationManager.requestLocationUpdates("gps", 2000, 0, locationListener); //0, 2, 4
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
            alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
        }
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
    public void timeMeasure(double x1, double y1, double x2, double y2, int z) {
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
                    startTime1 = System.currentTimeMillis();
                    startTime = SystemClock.uptimeMillis();
                }
                info1.setText("Rozpocząłeś wyścig");
                customHandler.postDelayed(updateTimerThread, 0);
                Z[0]=1;
                pc=pc+4;
            }
            else if(z==pk_all.size()-4){
                if(line.przynaleznosc(countingPK.get(z+1), countingPK.get(z),countingPK.get(z+3), countingPK.get(z+2), x1, y1)==1){
                    timeBetween2 = System.currentTimeMillis() - startTime1;
                }
                customHandler.removeCallbacks(updateTimerThread);
                timeSend = timeFormat(timeBetween2);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT");
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    alertDialog("Pomiar czasu", "Proszę włączyć połączenie z siecią aby przesłać wyniki do bazy");
                    //Toast.makeText(StartComp.this, "Proszę włączyć połączenie z siecią aby przesłać wyniki do bazy", Toast.LENGTH_LONG).show();
                    czySaNiewyslaneCzasy=true;
                }
                info1.setText("Zakończyłeś wyścig");
                info.setText("Koniec");
                startComp=false;
            }
            else{
                if(line.przynaleznosc(countingPK.get(z+1), countingPK.get(z),countingPK.get(z+3), countingPK.get(z+2), x1, y1)==1){
                    timeBetween2 = System.currentTimeMillis() - startTime1;
                }
                timeSend = timeFormat(timeBetween2);
                info1.setText("Przekroczyłeś punkt kontrolny nr: " + z / 4 + " w czasie " + timeSend);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT");
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    Toast.makeText(StartComp.this, "Brak połączenia z siecią. Zapisanie wyniku do tablicy", Toast.LENGTH_LONG).show();
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
                startTime1 = (System.currentTimeMillis() + startTime2) / 2;
                startTime = (SystemClock.uptimeMillis() + (startTime2 - startTime1));
                customHandler.postDelayed(updateTimerThread, 0);
                Z[0] = 1;
                pc = pc + 4;
            } else if (z == pk_all.size() - 4) {
                timeBetween2 = (System.currentTimeMillis()-startTime2)/2 + timeBetween;
                customHandler.removeCallbacks(updateTimerThread);
                timeSend = timeFormat(timeBetween2);
                info1.setText("Zakończyłeś wyścig");
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT");
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    alertDialog("Pomiar czasu", "Proszę włączyć połączenie z siecią aby przesłać wyniki do bazy");
                    //Toast.makeText(StartComp.this, "Proszę włączyć połączenie z siecią aby przesłać wyniki do bazy", Toast.LENGTH_LONG).show();
                    czySaNiewyslaneCzasy=true;
                }
                info.setText("Koniec");
                startComp = false;
            } else {
                timeBetween2 = (System.currentTimeMillis()-startTime2)/2 + timeBetween;
                timeSend = timeFormat(timeBetween2);
                info1.setText("Przekroczyłeś punkt kontrolny nr: " + z / 4 + " w czasie " + timeSend);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/time?competition_id=" + ID_zaw+
                            "&user_id="+ID_usera+"&point_nr="+z/4+"&time="+timeSend;
                    sendHttpRequest(url2, "PUT");
                } else {
                    String pkt = String.valueOf(z/4);
                    ilPunktowPomiaru.add(pkt);
                    czasyPrzebiegu.add(timeSend);
                    Toast.makeText(StartComp.this, "Brak połączenia z siecią. Zapisanie wyniku do tablicy", Toast.LENGTH_LONG).show();
                    czySaNiewyslaneCzasy=true;
                }
                Z[ktoryPomiar] = 1;
                pc = pc + 4;
            }
        }
    }
    public void changeStringToDoubles(List<String> p) {
        for (int g = 0; g < p.size(); g++) {
            countingPK.add(Double.parseDouble(p.get(g)));
        }
    }
    public void countDistance(double x, double y, List<Double> p, int d, Location loc) {
        start_ySr = (p.get(d) + p.get(d + 2)) / 2;
        start_xSr = (p.get(d + 1) + p.get(d + 3)) / 2;
        float[] results = new float[1];
        Location.distanceBetween(szerokosc, dlugosc,
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
    public void setPOI(List<String> p, List<String> name, float x) {

        for (int i = 0; i < (il_poi) * 2; i = i + 2) {
            y1 = Double.parseDouble(p.get(i));
            x1 = Double.parseDouble(p.get(i + 1));
            LatLng p2 = new LatLng(y1, x1);
            nazwa_point = name.get((i / 2));
            tmpm = mMap.addMarker(new MarkerOptions().position(p2).title(nazwa_point).icon(BitmapDescriptorFactory.defaultMarker(x)));
        }
    }
    public void setPoint(List<String> p, String name1, float x, int h) {
        int i;
        for (i = 0, j = 1.0; i < p.size(); i = i + 2, j++) {
            y1 = Double.parseDouble(p.get(i));
            x1 = Double.parseDouble(p.get(i + 1));
            LatLng p2 = new LatLng(y1, x1);
            if (h == 1)
                nazwa_point = name1 + (int) (Math.ceil((j / 2)));
            else nazwa_point = name1;
            tmpm = mMap.addMarker(new MarkerOptions().position(p2).title(nazwa_point).icon(BitmapDescriptorFactory.defaultMarker(x)));
        }

    }
    public void drawLine(List<String> p, int x) {
        for (int k = 0; k < p.size(); k = k + 4) {
            y1 = Double.parseDouble(p.get(k));
            x1 = Double.parseDouble(p.get(k + 1));
            y2 = Double.parseDouble(p.get(k + 2));
            x2 = Double.parseDouble(p.get(k + 3));
            Polyline route = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(y1, x1), new LatLng(y2, x2))
                            .width(9).color(x)
            );
        }
    }
    public void drawRoute(List<String> p) {
        for (int i = 0; i < p.size(); i = i + 2) {
            if (!((i + 3) > p.size())) {
                y1 = Double.parseDouble(p.get(i));
                x1 = Double.parseDouble(p.get(i + 1));
                y2 = Double.parseDouble(p.get(i + 2));
                x2 = Double.parseDouble(p.get(i + 3));
                route = new PolylineOptions()
                        .add(new LatLng(y1, x1), new LatLng(y2, x2))
                        .width(12).color(R.color.teal700);
                polylines.add(this.mMap.addPolyline(route));
            }
        }
        y1 = Double.parseDouble(p.get(0));
        x1 = Double.parseDouble(p.get(1));
        p3 = new LatLng(y1, x1);
    }
    public void onBackPressed() {
        super.onBackPressed();
        if (gpstracker != null)
            gpstracker.stopUsingGPS();
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
        this.finish();
    }
    public void sendHttpRequest(String url, final String operation){
        RestController rc = new RestController(this){
            @Override
            public void onResponseReceived(String result) {
                if(operation.equals("GET")) {
                    try {
                        parsingJSON(result);
                    } catch (JSONException e) {
                        Toast.makeText(StartComp.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            else if(operation.equals("PUT"))
            {
                Toast.makeText(StartComp.this, "Zanotowanie czasu w bazie", Toast.LENGTH_SHORT).show();
            }
            }
        };
        rc.setAddress(url);
        rc.setOperation(operation);
        rc.setShowPD(false);
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
            changeStringToDoubles(pk_all);
            if (JSON.contains("POINT_POINAME")) {
                String ilosc_poi = poi.getString("COUNT");
                il_poi = Integer.parseInt(ilosc_poi);
                for (int i = 0; i < il_poi; i++) {
                    pk_POI.add(poi.getString("POINT_POIY" + i));
                    pk_POI.add(poi.getString("POINT_POIX" + i));
                    nazwaPOI.add(poi.getString("POINT_POINAME" + i));
                }
            }
            setPOI(pk_POI, nazwaPOI, BitmapDescriptorFactory.HUE_VIOLET);
            String ilosc_track = route.getString("COUNT");
            ile_route = Integer.parseInt(ilosc_track);
            for (int i = 0; i < ile_route; i++) {
                trasa.add(route.getString("POINTY" + i));
                trasa.add(route.getString("POINTX" + i));
            }

            drawRoute(trasa);
            setPoint(pk_start, "Start ", BitmapDescriptorFactory.HUE_AZURE, 0);
            setPoint(pk_pk, "Punkt kontrolny nr ", BitmapDescriptorFactory.HUE_ORANGE, 1);
            setPoint(pk_meta, "Meta ", BitmapDescriptorFactory.HUE_GREEN, 0);
            drawLine(pk_start, Color.BLUE);
            drawLine(pk_pk, Color.parseColor("#FF6600"));
            drawLine(pk_meta, Color.GREEN);

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
    public void alertDialog(String title, String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Pobieranie lokalizacji");
        alertDialogBuilder
                .setMessage("Proszę włączyć usługę GPS")
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
