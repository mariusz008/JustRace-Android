package com.teamproject.windows;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.teamproject.models.competitionDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    double szerokosc, dlugosc;
    ProgressDialog progress;
    final competitionDTO competition = CompList.comp;
    String ID_zaw = competition.getID_zawodow();
    double y1, x1, y2, x2;
    List<String> trasa = new ArrayList<String>();
    List<String> pk_start = new ArrayList<String>();
    List<String> pk_pk = new ArrayList<String>();
    List<String> pk_meta = new ArrayList<String>();
    List<String> pk_all = new ArrayList<String>();
    List<String> pk_POI = new ArrayList<String>();
    List<String> nazwaPOI = new ArrayList<String>();
    List<Polyline> polylines = new ArrayList<Polyline>();
    LatLngBounds.Builder builder;
    LatLngBounds bounds;
    PolylineOptions route;
    String nazwa_point;
    LatLng p1, p2, p3;
    Marker tmpm;
    int il_poi, ile_route, il_pk;
    double j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        sendGetRequest();
        builder = new LatLngBounds.Builder();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    public void setPOI(List<String> p, List<String> name,  float x){

        for(int i=0; i<(il_poi)*2;i=i+2) {
            y1 = Double.parseDouble(p.get(i));
            x1 = Double.parseDouble(p.get(i + 1));
            LatLng p2 = new LatLng(y1, x1);
            nazwa_point = name.get((i / 2));
            tmpm = mMap.addMarker(new MarkerOptions().position(p2).title(nazwa_point).icon(BitmapDescriptorFactory.defaultMarker(x)));
        }
    }
    public void setPoint(List<String> p, String name1,  float x){
        int i;
        for(i=0, j=1.0; i<p.size(); i=i+2,j++) {
            y1 = Double.parseDouble(p.get(i));
            x1 = Double.parseDouble(p.get(i + 1));
            LatLng p2 = new LatLng(y1, x1);
            if (p.size()>4)
            nazwa_point = name1 + (int)(Math.ceil((j/2)));
            else nazwa_point = name1;
            tmpm = mMap.addMarker(new MarkerOptions().position(p2).title(nazwa_point).icon(BitmapDescriptorFactory.defaultMarker(x)));
        }

    }
    public void drawLine(List<String> p, int x){
        for(int k=0;  k<p.size(); k=k+4){
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
    public void drawRoute(List<String> p)
    {
        for(int i=0; i<p.size(); i=i+2) {
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
        y1=Double.parseDouble(p.get(0));
        x1 = Double.parseDouble(p.get(1));
        p3 = new LatLng(y1, x1);
        fixZoom(p3);
    }

    private void fixZoom(LatLng p) {
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(p,15F);
        mMap.animateCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    public void sendGetRequest() {
        GetClass getclass = new GetClass(this);
        getclass.execute();
    }
    private class GetClass extends AsyncTask<String, Void, String> {

        private final Context context;
        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String wynik1="";
            try {
                URL url = new URL("http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/gps/all?competition_id="+ID_zaw);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                final String wynik = responseOutput.toString();
                wynik1=wynik;
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(progress.isShowing())
                progress.dismiss();
            return wynik1;
        }
        protected void onPostExecute(String result) {
            try {
                parsingJSON(result);
            } catch (JSONException e) {
                Toast.makeText(MapsActivity.this, e.toString() , Toast.LENGTH_LONG).show();
            }
        }
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
        String ilosc_pk = checkpoints.getString("COUNT");
        il_pk = Integer.parseInt(ilosc_pk);
        for(int i=0;i<il_pk;i++){
            pk_pk.add(checkpoints.getString("PUNKT"+i+"Ay"));
            pk_pk.add(checkpoints.getString("PUNKT"+i+"Ax"));
            pk_pk.add(checkpoints.getString("PUNKT"+i+"By"));
            pk_pk.add(checkpoints.getString("PUNKT"+i+"Bx"));
        }
        pk_meta.add(checkpoints.getString("META1y"));
        pk_meta.add(checkpoints.getString("META1x"));
        pk_meta.add(checkpoints.getString("META2y"));
        pk_meta.add(checkpoints.getString("META2x"));


        String ilosc_poi = poi.getString("COUNT");
        il_poi = Integer.parseInt(ilosc_poi);
        for(int i=0;i<il_poi;i++){
            pk_POI.add(poi.getString("POINT_POIY"+i));
            pk_POI.add(poi.getString("POINT_POIX"+i));
            nazwaPOI.add(poi.getString("POINT_POINAME"+i));
        }

        String ilosc_track = route.getString("COUNT");
        ile_route = Integer.parseInt(ilosc_track);
        for(int i=0;i<ile_route;i++){
            trasa.add(route.getString("POINTY" + i));
            trasa.add(route.getString("POINTX"+i));
        }

        drawRoute(trasa);
        setPOI(pk_POI, nazwaPOI, BitmapDescriptorFactory.HUE_VIOLET);
        setPoint(pk_start, "Start ", BitmapDescriptorFactory.HUE_BLUE);
        setPoint(pk_pk,"Punkt kontrolny nr ", BitmapDescriptorFactory.HUE_ORANGE);
        setPoint(pk_meta, "Meta ", BitmapDescriptorFactory.HUE_GREEN);
        drawLine(pk_start, Color.BLUE);
        drawLine(pk_pk, Color.parseColor("#FF6600"));
        drawLine(pk_meta, Color.GREEN);

    }
}