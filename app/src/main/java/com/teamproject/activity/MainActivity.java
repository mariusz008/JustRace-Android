package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teamproject.conn.ConnectionDetector;
import com.teamproject.conn.TurningOnGPS;
import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.RestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity {
    ConnectionDetector cd;
    TurningOnGPS gps;
    final Context context = this;
    DialogCommunications comm;
    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    Intent intent1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comm  = new DialogCommunications(context);
        Button btnStatus = (Button) findViewById(R.id.LoginButton);
        intent1 = new Intent(this, Login.class);
        cd = new ConnectionDetector(getApplicationContext());
        gps = new TurningOnGPS(getApplicationContext());
        btnStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    MainActivity.this.finish();
                    startActivity(intent1);
                } else {
                    comm.alertDialog("Połączenie z internetem", "Sprawdź połączenie z internetem i spróbuj ponownie");
                }
            }
        });
    }
}