package com.teamproject.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.RestController;

public class ReminderPass extends Activity {
	boolean flaga, flaga1;
	String ret="";
	final Context context = this;
	String error, success = "";
	private EditText emailT, loginT;
	DialogCommunications comm;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.reminder_activity);
	        emailT = (EditText) findViewById(R.id.editText1);
	        loginT = (EditText) findViewById(R.id.editText2);
			Button button = (Button) findViewById(R.id.buttonAlert);
			Button button1 = (Button) findViewById(R.id.submitButton);
		    comm = new DialogCommunications(context);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ReminderPass.this.finish();
					}
				});
			button1.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					String url1 = validation();
					if (url1.length()!=0) {
						sendHttpRequest(url1, "GET");
					}
				}
			});
   	 }
	  public void checkResponse(String wejscie)
	    {
	    	String komunikat="";
	    	flaga1 = true;				
				if (wejscie.contains("Wrong login or email")){
				flaga1 = false;
				error = "Zły email lub hasło!";
				}
				if (wejscie.contains("Email sent")){
				flaga1 = true;
					success = "Wygenerowano nowe hasło, proszę sprawdzić skrzynkę pocztową.";
				}				
			
				if (flaga1==false){
					ret=error;
					komunikat = "Komunikat o błędzie";
				}
				else{ 
					ret=success;
					komunikat = "Udany reset hasła";
				}
				
	    	Context context2 = this;   	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
			alertDialogBuilder.setTitle(komunikat);
			alertDialogBuilder
				.setMessage(ret)
				.setCancelable(false)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						if (ret == success) {
							ReminderPass.this.finish();
						}
					}
				});
				AlertDialog alertDialog1 = alertDialogBuilder.create();
				alertDialog1.show();
	    }
	  public String validation() {
	    	String url="";
	    	flaga = true;
	        String email = emailT.getText().toString(); 
	        String login = loginT.getText().toString();
       
	    	if ((login.matches("[A-Za-z0-9]+") == false))
	    	{
	    		flaga = false;
	    		error = "Login może składać się tylko z liter i cyfr (bez polskich znaków)";
	    	}
	    	if ((login.length() == 0 || email.length() == 0 ))
	        {
	        	flaga = false;
	        	error = "Proszę wypełnić wszystkie oba pola";
	        }
	    	if (flaga == false){
				comm.alertDialog("Komunikat o błędzie", error);
	    	}
	    	else {	
	    		url = URLaddress(login, email); 
	    	}
	    	return url;
	    	
	    }
	  public String URLaddress(String loginS, String emailS )
		{
			String URL = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/user/password?"
					+ "login="+loginS+
					"&email="+emailS+"";
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

}