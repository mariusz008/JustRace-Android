package com.teamproject.activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
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

public class Transmission extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private RadioGroup rg;
    private Button button;
    final competitionDTO comp = CompList.comp;
    String ID_zaw = comp.getID_zawodow();
    List<String> trasa = new ArrayList<String>();
    List<String> pk_start = new ArrayList<String>();
    List<String> pk_pk = new ArrayList<String>();
    List<String> pk_meta = new ArrayList<String>();
    List<String> pk_all = new ArrayList<String>();
    List<String> pk_POI = new ArrayList<String>();
    List<String> nazwaPOI = new ArrayList<String>();
    List<Polyline> polylines = new ArrayList<Polyline>();
    ArrayList<String> imieAL = new ArrayList<String>();
    ArrayList<String> nazwiskoAL = new ArrayList<String>();
    ArrayList<String> numerAL = new ArrayList<String>();
ArrayList<String> wynik = new ArrayList<>();
    int il_poi, ile_route, il_pk, intP;
    final competitionDTO competition = CompList.comp;
    String ID_zad = competition.getID_zawodow();
    ObjectsOnMap oom = new ObjectsOnMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transmission);
        rg = (RadioGroup) findViewById(R.id.radio_group_list_selector);
        String url = "http://192.168.0.2:8080/Rest/rest/competition/gps/all?competition_id="+ID_zaw;
        sendHttpRequest(url, "GET", 1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = (Button) findViewById(R.id.Button1);


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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //AlertDialogView();
               String x = "http://192.168.0.2:8080/Rest/rest/competition/event/list/event?competition_id="+ID_zad+"&sex=&age=&phrase=&category=";
               sendHttpRequest(x, "GET", 2);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
    private void AlertDialogView(ArrayList<String> data) {
        final CharSequence[] items = {" Easy "," Medium "," Hard "," Very Hard "};//data.toArray(new CharSequence[data.size()]);
        //final CharSequence[] items = data;
        final ArrayList seletedItems=new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(Transmission.this)
                .setTitle("Select The Difficulty Level")
                .setMultiChoiceItems((String []) items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedItems.add(indexSelected);
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                    }
                }).create();
        dialog.show();
    }
    public void sendHttpRequest(String url, String operation, final int xx){
        RestController rc = new RestController(this){
            @Override
            public void onResponseReceived(String result) throws JSONException {
                if(xx==1) {
                    try {
                        parsingJSON(result);
                    } catch (JSONException e) {
                        Toast.makeText(Transmission.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else if (xx==2){
                    parsingJSON1(result);
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

    public void parsingJSON1(String JSON) throws JSONException {
        imieAL.clear();
        nazwiskoAL.clear();
        numerAL.clear();
        wynik.clear();
        int i;
        JSONArray jsonarray = new JSONArray(JSON);
        JSONObject obj1 = jsonarray.getJSONObject(0);

        for (i = 1; i < jsonarray.length(); i++) {
            JSONObject obj = jsonarray.getJSONObject(i);
            imieAL.add(obj.getString("IMIE"));
            nazwiskoAL.add(obj.getString("NAZWISKO"));
            numerAL.add(obj.getString("EVENT_NR"));
            wynik.add(obj.getString("IMIE")+" "+obj.getString("NAZWISKO")+" - "+obj.getString("EVENT_NR") + ",");
        }
        //pupulateButtons(i-1, imieAL, nazwiskoAL, kategoriaAL, numerAL, wiekAL, czas);
        AlertDialogView(wynik);
    }
}