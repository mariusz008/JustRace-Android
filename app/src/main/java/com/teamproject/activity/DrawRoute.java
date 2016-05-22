package com.teamproject.activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamproject.functions.RestController;
import com.teamproject.models.competitionDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DrawRoute extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
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
        String url = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/gps/all?competition_id="+ID_zaw;
        sendHttpRequest(url, "GET");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
    public void setPoint(List<String> p, String name1,  float x, int h){
        int i;
        for(i=0, j=1.0; i<p.size(); i=i+2,j++) {
            y1 = Double.parseDouble(p.get(i));
            x1 = Double.parseDouble(p.get(i + 1));
            LatLng p2 = new LatLng(y1, x1);
            if (h==1)
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
    public void drawRoute(List<String> p) {
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
    public void fixZoom(LatLng p) {
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(p,15F);
        mMap.animateCamera(cu);
    }
    public void sendHttpRequest(String url, String operation){
        RestController rc = new RestController(this){
            @Override
            public void onResponseReceived(String result) {
                try {
                    parsingJSON(result);
                } catch (JSONException e) {
                    Toast.makeText(DrawRoute.this, e.toString() , Toast.LENGTH_LONG).show();
                }
            }
        };
        rc.setAddress(url);
        rc.setOperation(operation);
        rc.setShowPD(true);
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

        if(JSON.contains("POINT_POINAME")) {
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
        for(int i=0;i<ile_route;i++){
            trasa.add(route.getString("POINTY" + i));
            trasa.add(route.getString("POINTX"+i));
        }

        drawRoute(trasa);
        setPoint(pk_start, "Start ", BitmapDescriptorFactory.HUE_AZURE, 0);
        setPoint(pk_pk,"Punkt kontrolny nr ", BitmapDescriptorFactory.HUE_ORANGE, 1);
        setPoint(pk_meta, "Meta ", BitmapDescriptorFactory.HUE_GREEN, 0);
        drawLine(pk_start, Color.BLUE);
        drawLine(pk_pk, Color.parseColor("#FF6600"));
        drawLine(pk_meta, Color.GREEN);

    }
}