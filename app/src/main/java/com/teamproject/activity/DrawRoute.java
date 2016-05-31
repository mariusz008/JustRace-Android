package com.teamproject.activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Polyline;
import com.teamproject.functions.ObjectsOnMap;
import com.teamproject.functions.RestController;
import com.teamproject.models.competitionDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DrawRoute extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private RadioGroup rg;
    final competitionDTO competition = CompList.comp;
    String ID_zaw = competition.getID_zawodow();
    List<String> trasa = new ArrayList<String>();
    List<String> pk_start = new ArrayList<String>();
    List<String> pk_pk = new ArrayList<String>();
    List<String> pk_meta = new ArrayList<String>();
    List<String> pk_all = new ArrayList<String>();
    List<String> pk_POI = new ArrayList<String>();
    List<String> nazwaPOI = new ArrayList<String>();
    List<Polyline> polylines = new ArrayList<Polyline>();
    int il_poi, ile_route, il_pk;
    ObjectsOnMap oom = new ObjectsOnMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        rg = (RadioGroup) findViewById(R.id.radio_group_list_selector);
        String url = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/gps/all?competition_id="+ID_zaw;
        sendHttpRequest(url, "GET");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        oom.setPOI(pk_POI, nazwaPOI, BitmapDescriptorFactory.HUE_VIOLET, il_poi, mMap);
        if(JSON.contains("POINTX0")) {
            String ilosc_track = route.getString("COUNT");
            ile_route = Integer.parseInt(ilosc_track);
            for (int i = 0; i < ile_route; i++) {
                trasa.add(route.getString("POINTY" + i));
                trasa.add(route.getString("POINTX" + i));
            }
            oom.drawRoute(trasa, mMap, polylines);
        }

        oom.setPoint(pk_start, "Start ", BitmapDescriptorFactory.HUE_AZURE, 0, mMap);
        oom.setPoint(pk_pk, "Punkt kontrolny nr ", BitmapDescriptorFactory.HUE_ORANGE, 1, mMap);
        oom.setPoint(pk_meta, "Meta ", BitmapDescriptorFactory.HUE_GREEN, 0,mMap);

        oom.drawLine(pk_start, Color.BLUE, mMap);
        oom.drawLine(pk_pk, Color.parseColor("#FF6600"), mMap);
        oom.drawLine(pk_meta, Color.GREEN, mMap);

    }
}