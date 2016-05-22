package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.teamproject.functions.RestController;
import com.teamproject.models.userDTO;

public class ChangePass extends Activity{
	boolean flaga, flaga1;
	String ret="";
	final Context context = this;
	String error, success = "";
	private EditText starehasloT, nowehaslo1T, nowehaslo2T;
	public static final String loginZap  = Login.SPF_NAME;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final userDTO user1 = Login.user;
        setContentView(R.layout.change_pass);
        starehasloT = (EditText) findViewById(R.id.editText1);
        nowehaslo1T = (EditText) findViewById(R.id.editText2);
        nowehaslo2T = (EditText) findViewById(R.id.editText3);
		Button button = (Button) findViewById(R.id.buttonAlert);
		Button button1 = (Button) findViewById(R.id.submitButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ChangePass.this.finish();
				}
			});
		button1.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				String url1 = Validation(user1.getID_uzytkownika());
				if (url1.length()!=0)
				{
					sendHttpRequest(url1, "POST");
				}			
				}
			});	
}
	public String Validation(String id) {
    	String url="";
    	flaga = true;
        String starehaslo = starehasloT.getText().toString(); 
        String nowehaslo1 = nowehaslo1T.getText().toString();
        String nowehaslo2 = nowehaslo2T.getText().toString();
        String ID  = id;
        
        if (!nowehaslo1.equals(nowehaslo2)){
        	flaga = false;
        	error = "Nowe hasła nie są identyczne!";
        } 
        if ((nowehaslo1.matches("[A-Za-z0-9]+") == false))
    	{
    		flaga = false;
    		error = "Hasło może składać się tylko z liter i cyfr (bez polskich znaków)";
    	}
        if ((nowehaslo1.length()<8 || nowehaslo1.length()>20)){
      		flaga = false;
      		error = "Hasło powinno mieć od 8 do 20 znaków!";
      	} 
        if ((nowehaslo1.length() == 0 || nowehaslo2.length() == 0 || starehaslo.length() == 0 ))
        {
        	flaga = false;
        	error = "Proszę wypełnić wszystkie pola";
        }
    	if (flaga == false){
    		Context context1 = this;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);
			alertDialogBuilder.setTitle("Komunikat o błędzie");
			alertDialogBuilder
				.setMessage(error)
				.setCancelable(false)
				.setNeutralButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();			
    	}
    	else {	
    		url = URLaddress(ID, starehaslo, nowehaslo1); 
    	}
    	return url;
    	
    }
  public String URLaddress(String ID, String hasloS, String hasloN1)
	{
		String URL = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/user/password?"
				+ "user_id="+ID+
				"&old_password="+hasloS+
				"&new_password="+hasloN1+"";
		return URL;
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

	public void checkResponse(String wejscie)
    {
    	String komunikat="";
    	flaga1 = true;				
			if (wejscie.contains("Wrong password")){
			flaga1 = false;
			error = "Podałeś złe hasło!";
			}
			else if (wejscie.contains("No such user")){
			flaga1 = false;
			error = "Nie ma takiego użytkownika!";
			}
			else if (wejscie.contains("Password changed")){
			flaga1 = true;
				success = "Udało Ci się zmienić hasło! Przy następnym logowaniu będziesz musiał(a) wpisać login oraz hasło!";
			}				
		
		if (flaga1==false){
			ret=error;
			komunikat = "Komunikat";
		}
		else{ 
			ret=success;
			komunikat = "Udana zmiana hasła";
		}
			
    	Context context3 = this;   	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context3);
		alertDialogBuilder.setTitle(komunikat);
		alertDialogBuilder
			.setMessage(ret)
			.setCancelable(false)
			.setNeutralButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();				
				if (ret==success){
					logOut();
					ChangePass.this.finish();
				}
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();
    }
	public void logOut(){
    	{
            SharedPreferences loginPreferences = this.getSharedPreferences(loginZap, Context.MODE_PRIVATE);
            loginPreferences.edit().clear().commit();
        }

    }
}
