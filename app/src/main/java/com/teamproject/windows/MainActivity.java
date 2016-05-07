package com.teamproject.windows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.teamproject.conn.ConnectionDetector;
import com.teamproject.conn.TurningOnGPS;


public class MainActivity extends Activity {

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    TurningOnGPS gps;
    int status = 0;
    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    Intent intent1, intent123;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        Button btnStatus = (Button) findViewById(R.id.LoginButton);
        intent1 = new Intent(this, Login.class);
        intent123 = new Intent(this, TestowanieHttp.class);
        cd = new ConnectionDetector(getApplicationContext());
        gps = new TurningOnGPS(getApplicationContext());
        //Check Internet status button click event        
        btnStatus.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {                
                isInternetPresent = cd.isConnectingToInternet(); 
                if (isInternetPresent) {                                  
//                    if(!gps.checkingGPSStatus())
//                    {
//                    	showAlertDialog(MainActivity.this, "Wykrywanie lokalizacji", "Prosz� w��czy� us�ug� lokalizacji. Zaraz zostaniesz przekierowany do ustawie�...", 1);
//                        //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                    else {
                    	//showAlertDialog(MainActivity.this, "Connection", "Zapraszamy!", 2);
                    MainActivity.this.finish();
                    startActivity(intent1);

                   // }
                } else {
                    showAlertDialog(MainActivity.this, "Połączenie z internetem","Sprawdź połączenie z internetem i spróbuj ponownie", 0);
                }
            } 
        });
    }
 
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message, final int statusone) {
        AlertDialog builder  = new AlertDialog.Builder(context).create();
     
        builder.setTitle(title);
        builder.setMessage(message);         

        // Setting OK Button
        builder.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if (statusone == 1){
                    startActivity(intent);
            	}
//            	else if (statusone == 2){
//                	startActivity(intent1);
//            	}
            }
        });

        builder.show();
              
    }
    

}