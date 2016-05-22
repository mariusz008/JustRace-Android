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
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamproject.conn.TurningOnGPS;
import com.teamproject.functions.GpsTracker;
import com.teamproject.functions.RestController;
import com.teamproject.models.GpsDTO;
import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;
import com.teamproject.route.TrackPOI;
import com.teamproject.route.TrackPoints;
import com.teamproject.route.TrackRoute;
import java.util.ArrayList;
import java.util.List;


public class MakingRoute extends FragmentActivity implements OnMapReadyCallback, TrackPoints.OnHeadlineSelectedListener,
        TrackRoute.OnHeadlineSelectedListener, TrackPOI.OnHeadlineSelectedListener{
    final Context context = this;
    boolean flaga1, flaga2, butS1, butS2, butPK1, butPK2, butM1, butM2, butPotw, butZap1, butStart, butMeta, butZap2, butRes, butNic, butPotw2, butZap3;
    boolean nagrywanie, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14;
    private Marker now, m1, m2, m3;
    private FragmentTabHost mTabHost;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private com.google.android.gms.maps.GoogleMap mMap;
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    Intent intent2;
    TurningOnGPS gps;
    GpsTracker gpstracker;
    Polyline route;
    double szerokosc, dlugosc, szerokoscPoint, dlugoscPoint;
    int i, ktory, jk;
    String szer, dl, szerPoint, dlPoint, error1, j, ret1, success1, nazwaPOI, s;
    TrackPoints tp = new TrackPoints();
    List<String> trasa = new ArrayList<String>();
    List<String> pk_start = new ArrayList<String>();
    List<String> pk_pk = new ArrayList<String>();
    List<String> pk_meta = new ArrayList<String>();
    List<String> pk_all = new ArrayList<String>();
    List<String> pk_POI = new ArrayList<String>();
    List<Polyline> polylines = new ArrayList<Polyline>();
    final static GpsDTO gpsdto = new GpsDTO();
    private GoogleApiClient client;
    final userDTO us = Login.user;
    final String ownerID = us.getID_uzytkownika();
    final competitionDTO competition = CompList.comp;
    String ID_zaw = competition.getID_zawodow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker);
        gps = new TurningOnGPS(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);


        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Dodaj punkty pomiaru czasu", null),
                TrackPoints.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Dodaj POI", null),
                TrackPOI.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Nagraj trasę", null),
                TrackRoute.class, null);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {
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
            @Override
            public void onLocationChanged(Location location) {
                if(now != null){
                    now.remove();
                }
                flaga1 = true;
                szerokosc = location.getLatitude();
                dlugosc = location.getLongitude();
                LatLng p2 = new LatLng(szerokosc, dlugosc);
                if(nagrywanie){
                    szer = Double.toString(szerokosc);
                    dl = Double.toString(dlugosc);
                    trasa.add(dl);
                    trasa.add(szer);
                    if(trasa.size()>3)
                    drawRoute(trasa);
                        now = mMap.addMarker(new MarkerOptions()
                                .position(p2)
                                .title("Tu jesteś")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
                else
                now = mMap.addMarker(new MarkerOptions().position(p2).title("Tu jesteś"));
                gpstracker.stopUsingGPS();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(p2));
            }
        };

        //pobieranie lokalizacji
        if (gps.checkingGPSStatus()) {
            locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
            if (flaga1 == false) {
                gpstracker = new GpsTracker(this){
                    @Override
                    public void onLocationReceived(double latitude, double longitude) {
                        if (now != null) {
                                now.remove();
                            }
                            szerokosc = latitude;
                            dlugosc = longitude;
                             LatLng p2 = new LatLng(szerokosc, dlugosc);
                            if(nagrywanie){
                                szer = Double.toString(szerokosc);
                                dl = Double.toString(dlugosc);
                                trasa.add(dl);
                                trasa.add(szer);
                                if(trasa.size()>3)
                                    drawRoute(trasa);
                                now = mMap.addMarker(new MarkerOptions()
                                        .position(p2)
                                        .title("Tu jesteś")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            }
                            else
                                now = mMap.addMarker(new MarkerOptions().position(p2).title("Tu jesteś"));
                            if (jk==0)
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p2, 16F));
                            else mMap.moveCamera(CameraUpdateFactory.newLatLng(p2));
                            jk=1;
                    }

                };
                szerokosc = gpstracker.getLatitude();
                dlugosc = gpstracker.getLongitude();
                }
        } else {
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

    public void setPoint(double one, double two, String name, Marker tmpm, float x){
        LatLng p2 = new LatLng(one, two);
        tmpm = mMap.addMarker(new MarkerOptions().position(p2).title(name).icon(BitmapDescriptorFactory.defaultMarker(x)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p2));
    }
    public void drawLine(int x,  List<String> p, int j){
        double x1 = Double.parseDouble(p.get(j));
        double y1 = Double.parseDouble(p.get(j+1));
        double x2 = Double.parseDouble(p.get(j+2));
        double y2 = Double.parseDouble(p.get(j+3));
        Polyline route = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(y1, x1), new LatLng(y2, x2))
                        .width(9).color(x)
                );
    }
    public void drawRoute(List<String> p)
    {
        int j = p.size();
        double x1 = Double.parseDouble(p.get(j-4));
        double y1 = Double.parseDouble(p.get(j-3));
        double x2 = Double.parseDouble(p.get(j-2));
        double y2 = Double.parseDouble(p.get(j - 1));
        polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(y1, x1), new LatLng(y2, x2))
                        .width(12).color(R.color.teal700)
        ));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (gpstracker!=null)
        gpstracker.stopUsingGPS();
        if (locationManager!=null)
        locationManager.removeUpdates(locationListener);
        this.finish();
    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
        mMap = googleMap;
        if (szerokosc != 0) {
            LatLng p1 = new LatLng(szerokosc, dlugosc);
            now = mMap.addMarker(new MarkerOptions().position(p1).title("Tu jesteś"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p1));
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(p1, 16F);
            mMap.animateCamera(cu);
        }
    }

    @Override
    public void operation(String i) {
        checkButtonClicked(i);
    }
    @Override
    public void operation1(String i) {
        checkButtonClicked(i);
    }
    @Override
    public void operation2(String i) {
        checkButtonClicked(i);
    }
    public void checkButtonClicked(String k){
        if (k.equals("S1")) {butS1=true;butPK1=false;butM1=false;butPotw=false;butZap1=false;}
        else if (k.equals("S2") ) {if(f1)butS2=true;k="s2";butS1=false;butPK1=false;butM1=false;butPK2=false;butM2=false;butPotw=false;butZap1=false;}
        else if (k.equals("PK1")) {butPK1=true;butS1=false;butM1=false;butPotw=false;butZap1=false;}
        else if (k.equals("PK2") ) {if(f5)butPK2=true;k="pk2";butPK1=false;butS1=false;butM1=false;butS2=false;butM2=false;butPotw=false;butZap1=false;}
        else if (k.equals("M1")) {butM1=true;butS1=false;butPK1=false;butPotw=false;butZap1=false;}
        else if (k.equals("M2") ) {if(f3)butM2=true;k="m2";butM1=false;butS1=false;butPK1=false;butS2=false;butPK2=false;butPotw=false;butZap1=false;}
        else if (k.equals("Potw")) butPotw=true;
        else if (k.equals("Zap1")) butZap1=true;
        else if (k.equals("START")) {butStart=true;k="start";butZap2=false; butMeta=false;butRes=false;}
        else if (k.equals("META")) {butMeta=true;k="meta";butRes=false;}
        else if (k.equals("Zap2")) {butZap2=true;butRes=false;}
        else if (k.equals("Res")) {butRes=true;butStart=false;butMeta=false;butZap2=false;}
        else if (k.equals("ZapPOI")) {butZap3=true; butPotw2=false;}
        else if (k.length()!=0) {nazwaPOI=k;butPotw2=true;butZap3=false;}
        else if (k.length()==0) {butPotw2=false;butNic=true;}
        buttonsHandler(k);
        butPotw2=false;
        butNic=false;
        butRes=false;
        butZap3=false;
        butZap2=false;
        butZap1=false;
        butPotw=false;
    }
    public void buttonsHandler(String g) {
        if (butS1 && butPotw && !f1) {
            Toast.makeText(MakingRoute.this, "Dodałeś początek linii startu", Toast.LENGTH_SHORT).show();
            butS1 = false;
            butPotw = false;
            f1 = true;
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            setPoint(szerokoscPoint, dlugoscPoint, "Początek linii startu", m1, BitmapDescriptorFactory.HUE_AZURE);
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            pk_start.add(dlPoint);
            pk_start.add(szerPoint);
        } else if (butS2 && butPotw && !f2) {
            Toast.makeText(MakingRoute.this, "Dodałeś koniec linii startu", Toast.LENGTH_SHORT).show();
            butS2 = false;
            butPotw = false;
            f2 = true;
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            setPoint(szerokoscPoint, dlugoscPoint, "Koniec linii startu", m1, BitmapDescriptorFactory.HUE_AZURE);
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            pk_start.add(dlPoint);
            pk_start.add(szerPoint);
            drawLine(Color.BLUE, pk_start, 0);
        } else if (butM1 && butPotw && !f3) {
            Toast.makeText(MakingRoute.this, "Dodałeś początek linii mety", Toast.LENGTH_SHORT).show();
            butM1 = false;
            butPotw = false;
            f3 = true;
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            setPoint(szerokoscPoint, dlugoscPoint, "Początek linii mety", m2, BitmapDescriptorFactory.HUE_GREEN);
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            pk_meta.add(dlPoint);
            pk_meta.add(szerPoint);
        } else if (butM2 && butPotw && !f4) {
            Toast.makeText(MakingRoute.this, "Dodałeś koniec linii mety", Toast.LENGTH_SHORT).show();
            butM2 = false;
            butPotw = false;
            f4 = true;
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            setPoint(szerokoscPoint, dlugoscPoint, "Koniec linii mety", m2, BitmapDescriptorFactory.HUE_GREEN);
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            pk_meta.add(dlPoint);
            pk_meta.add(szerPoint);
            drawLine(Color.GREEN, pk_meta, 0);
        } else if (butPK1 && butPotw && !f5 && !f9) {
            Toast.makeText(MakingRoute.this, "Dodałeś początek linii punktu kontrolnego", Toast.LENGTH_SHORT).show();
            butPK1 = false;
            butPotw = false;
            f5 = true;
            f6 = false;
            f10 = false;
            f11 = true;
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            setPoint(szerokoscPoint, dlugoscPoint, "Początek linii punktu kontrolnego", m3, BitmapDescriptorFactory.HUE_ORANGE);
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            pk_pk.add(dlPoint);
            pk_pk.add(szerPoint);
        } else if (butPK2 && butPotw && !f6 && !f9) {
            Toast.makeText(MakingRoute.this, "Dodałeś koniec linii punktu kontrolnego", Toast.LENGTH_SHORT).show();
            butPK2 = false;
            butPotw = false;
            f6 = true;
            f5 = false;
            f10 = true;
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            setPoint(szerokoscPoint, dlugoscPoint, "Koniec linii punktu kontrolnego", m3, BitmapDescriptorFactory.HUE_ORANGE);
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            pk_pk.add(dlPoint);
            pk_pk.add(szerPoint);
            drawLine(Color.parseColor("#FF6600"), pk_pk, ktory);
            ktory = ktory + 4;
        }

        if (g.equals("s2") && !f1 && butPotw)
            Toast.makeText(MakingRoute.this, "Najpierw dodaj początek linii startu", Toast.LENGTH_SHORT).show();
        else if (g.equals("pk2") && !f11 && butPotw)
            Toast.makeText(MakingRoute.this, "Najpierw dodaj początek punktu kontrolnego", Toast.LENGTH_SHORT).show();
        else if (g.equals("m2") && !f3 && butPotw)
            Toast.makeText(MakingRoute.this, "Najpierw dodaj początek linii mety", Toast.LENGTH_SHORT).show();

        //wyslanie punktow
        if (f2 && f4 && f10 && butZap1 && !f9) {
            for (int i = 0; i < pk_start.size(); i++)
                pk_all.add(pk_start.get(i));
            for (int i = 0; i < pk_pk.size(); i++)
                pk_all.add(pk_pk.get(i));
            for (int i = 0; i < pk_meta.size(); i++)
                pk_all.add(pk_meta.get(i));
            String points = pk_all.toString();
            points = points.substring(1);
            points = points.substring(0, points.length() - 1);
            points = points.replaceAll("\\s+","");

            String url = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/route?owner_id="+ownerID+
                    "&competition_id="+ID_zaw+"&points="+points;
            sendHttpRequest(url, "POST");
            f9 = true;
        } else if (butZap1 && !f9)
            Toast.makeText(MakingRoute.this, "Najpierw dodaj wszystkie punkty", Toast.LENGTH_SHORT).show();


        //trasa
        if (butStart && !f7) {
            f7 = true;
            Toast.makeText(MakingRoute.this, "Rozpocząłeś nagrywanie trasy", Toast.LENGTH_SHORT).show();
            nagrywanie=true;
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            trasa.add(dlPoint);
            trasa.add(szerPoint);
        }
        if (butMeta && !f8 && f7) {
            f8 = true;
            Toast.makeText(MakingRoute.this, "Zakonczyłeś nagrywanie trasy", Toast.LENGTH_SHORT).show();
            nagrywanie=false;
        }
        if (butRes && !f12 && (f7 || f8)) {
            Toast.makeText(MakingRoute.this, "Zresetowałeś trase", Toast.LENGTH_SHORT).show();
            f7 = false;
            f8 = false;
            trasa.clear();
            nagrywanie=false;
            for(Polyline line : polylines)
            {
                line.remove();
            }
            polylines.clear();
        } else if (butRes && !f12 && !(f7 || f8))
            Toast.makeText(MakingRoute.this, "Nie nagrywasz trasy", Toast.LENGTH_SHORT).show();
        else if (butRes && f12)
            Toast.makeText(MakingRoute.this, "Już wysłałeś trasę", Toast.LENGTH_SHORT).show();
        if (butZap2 && g.equals("start") && !f12 && !butRes && f7)
            Toast.makeText(MakingRoute.this, "Dodaj metę", Toast.LENGTH_SHORT).show();
        else if (butMeta && g.equals("meta") && !f12 && !f7 && !butZap2)
            Toast.makeText(MakingRoute.this, "Najpierw dodaj start", Toast.LENGTH_SHORT).show();

        //wysylanie trasy
        if (f7 && f8 && butZap2 && !f12) {
            String trackroute = trasa.toString();
            trackroute = trackroute.substring(1);
            trackroute = trackroute.substring(0, trackroute.length() - 1);
            trackroute = trackroute.replaceAll("\\s+", "");
            String url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/track?owner_id="+ownerID+
            "&competition_id="+ID_zaw+"&points="+trackroute;
            sendHttpRequest(url1, "POST");
            f12 = true;
        } else if (butZap2 && !f12 && !f7 && !f8)
            Toast.makeText(MakingRoute.this, "Najpierw dodaj start i metę", Toast.LENGTH_SHORT).show();


        //POI
        if(butPotw2 && !f14){
            f13=true;
            Toast.makeText(MakingRoute.this, "Dodałeś " + nazwaPOI, Toast.LENGTH_SHORT).show();
            szerokoscPoint = szerokosc;
            dlugoscPoint = dlugosc;
            setPoint(szerokoscPoint, dlugoscPoint, nazwaPOI, m1, BitmapDescriptorFactory.HUE_VIOLET);
            szerPoint = Double.toString(szerokoscPoint);
            dlPoint = Double.toString(dlugoscPoint);
            pk_POI.add(dlPoint);
            pk_POI.add(szerPoint);
            pk_POI.add(nazwaPOI);
        }
        else if (!butPotw2 && !butZap3 && !f14 && butNic)
        {
            Toast.makeText(MakingRoute.this, "Nie podałeś nazwy POI", Toast.LENGTH_SHORT).show();
            f13=false;
        }
        if(butZap3 && f13 && !f14) {
            f13=false;
            f14=true;
            String POI = pk_POI.toString();
            POI = POI.substring(1);
            POI = POI.substring(0, POI.length() - 1);
            POI = POI.replaceAll("\\s+", "");

            String url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/poi?owner_id="+ownerID+
                    "&competition_id="+ID_zaw+"&points="+POI;
            sendHttpRequest(url1, "POST");
        }
        else if(butZap3 && !f13 && !f14) Toast.makeText(MakingRoute.this, "Nie dodałeś żadnego POI", Toast.LENGTH_SHORT).show();

    }
    public void sendHttpRequest(String url, String operation){
        RestController rc = new RestController(this){
            @Override
            public void onResponseReceived(String result) {
                checkResponse(result);
            }
        };
        rc.setAddress(url);
        rc.setOperation(operation);
        rc.setShowPD(true);
        rc.execute();
    }
    public boolean checkResponse(String wejscie)
    {
       String komunikat1="";
       flaga2 = true;

       if (wejscie.contains("Route track saved")) {
           flaga2 = true;
           success1 = "Udało Ci się poprawnie dodać wszystkie punkty kontrolne.";
       }
       else if(wejscie.contains("POIs saved")) {
            flaga2 = true;
            success1 = "Udało Ci się poprawnie dodać wszystkie POI.";
       }
       else if(wejscie.contains("Track saved")){
           flaga2 = true;
           success1 = "Udało Ci się poprawnie dodać trasę zawodów.";
       }
        else {
            flaga2 = false;
            error1 = "Wystąpił nieoczekiwany błąd. Spróbuj ponownie";
        }
       if (flaga2 == false) {
           ret1 = error1;
           komunikat1 = "Komunikat";
       } else {
           ret1 = success1;
           komunikat1 = "Tworzenie trasy";
       }
       final View arg0 = null;
       Context context2 = this;
       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
       alertDialogBuilder.setTitle(komunikat1);
       alertDialogBuilder
               .setMessage(ret1)
               .setCancelable(false)
               .setNeutralButton("OK",new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog,int id) {
                       dialog.cancel();
                       }
                   });
       AlertDialog alertDialog1 = alertDialogBuilder.create();
       alertDialog1.show();

       return flaga2;
   }
}