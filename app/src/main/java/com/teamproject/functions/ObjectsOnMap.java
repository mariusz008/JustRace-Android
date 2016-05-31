package com.teamproject.functions;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamproject.activity.R;

import java.util.List;

/**
 * Created by 008M on 2016-05-31.
 */
public class ObjectsOnMap {

    Circle mapCircle;

    public void setPOI(List<String> p, List<String> name, float x, int il_poi, GoogleMap mMap) {
        double y1, x1;
        for (int i = 0; i < (il_poi) * 2; i = i + 2) {
            y1 = Double.parseDouble(p.get(i));
            x1 = Double.parseDouble(p.get(i + 1));
            LatLng p2 = new LatLng(y1, x1);
            String nazwa_point = name.get((i / 2));
            Marker tmpm = mMap.addMarker(new MarkerOptions().position(p2).title(nazwa_point).icon(BitmapDescriptorFactory.defaultMarker(x)));
        }
    }

    public void setPoint(List<String> p, String name1,  float x, int h, GoogleMap mMap){
        int i;
        //int b = 0;
        String nazwa_point;
        double y1, x1, j;
        for(i=0, j=1.0; i<p.size(); i=i+2,j++) {
            y1 = Double.parseDouble(p.get(i));
            x1 = Double.parseDouble(p.get(i + 1));
            LatLng p2 = new LatLng(y1, x1);
            if (h==1)
                nazwa_point = name1 + (int)(Math.ceil((j/2)));
            else nazwa_point = name1;
            Marker tmpm = mMap.addMarker(new MarkerOptions().position(p2).title(nazwa_point).icon(BitmapDescriptorFactory.defaultMarker(x)));
//            if(x==BitmapDescriptorFactory.HUE_AZURE)b=Color.BLUE;
//            else if(x==BitmapDescriptorFactory.HUE_ORANGE)b=Color.parseColor("#FF6600");
//            else if(x==BitmapDescriptorFactory.HUE_GREEN)b=Color.GREEN;
            //drawCenteredCircle(x1, y1, 3, b);
        }
        if(h==0) {
            y1 = Double.parseDouble(p.get(0));
            x1 = Double.parseDouble(p.get(1));
            LatLng p3 = new LatLng(y1, x1);
            fixZoom(p3, mMap);
        }
    }
    public void fixZoom(LatLng p, GoogleMap mMap) {
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(p, 15F);
        mMap.animateCamera(cu);
    }

    public void drawLine(List<String> p, int x, GoogleMap mMap){
        double y1,x1,y2,x2;
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
    public void drawRoute(List<String> p, GoogleMap mMap, List<Polyline> polylines) {
        double y1,x1,y2,x2;
        for (int i = 0; i < p.size(); i = i + 2) {
            if (!((i + 3) > p.size())) {
                y1 = Double.parseDouble(p.get(i));
                x1 = Double.parseDouble(p.get(i + 1));
                y2 = Double.parseDouble(p.get(i + 2));
                x2 = Double.parseDouble(p.get(i + 3));
                PolylineOptions route = new PolylineOptions()
                        .add(new LatLng(y1, x1), new LatLng(y2, x2))
                        .width(12).color(R.color.teal700);
                polylines.add(mMap.addPolyline(route));
            }
        }
        y1 = Double.parseDouble(p.get(0));
        x1 = Double.parseDouble(p.get(1));
        LatLng p3 = new LatLng(y1, x1);
    }
    public void drawCenteredCircle(double x, double y, int r, int h, GoogleMap mMap) {
        if(mapCircle!=null){
            mapCircle.remove();
        }
        mapCircle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(y, x))
                .radius(r)
                .strokeColor(h)
                .strokeWidth(1)
                .fillColor(0x50000000+h));
    }
}
