package com.teamproject.windows;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ReminderPass extends Activity {
	boolean flaga, flaga1;
	String ret="";
	final Context context = this;
	ProgressDialog progress;
	String error, success = "";
	private EditText emailT, loginT;
	
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
		  	success = "Wygenerowano nowe hasło, proszę sprawdzić skrzynkę pocztową.";
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.reminder_activity);
	        final Context context = this;
	        emailT = (EditText) findViewById(R.id.editText1);
	        loginT = (EditText) findViewById(R.id.editText2);
			Button button = (Button) findViewById(R.id.buttonAlert);
			Button button1 = (Button) findViewById(R.id.submitButton);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ReminderPass.this.finish();
					}
				});
			button1.setOnClickListener(new OnClickListener() {			
				public void onClick(View arg0) {
					String url1 = Validation();
					if (url1.length()!=0)
					{
						sendGetRequest(arg0, url1);
					}			
					}
				});	
   	 }
	  public boolean CheckResponse(String wejscie)
	    {
	    	String komunikat="";
	    	flaga1 = true;				
				if (wejscie.contains("Wrong login or email")){
				flaga1 = false;
				error = "Zły email lub hasło!";
				}
				if (wejscie.contains("Email sent")){
				flaga1 = true;
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
				.setNeutralButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();				
					if (ret==success){
						ReminderPass.this.finish();
					}
					}});
				AlertDialog alertDialog1 = alertDialogBuilder.create();
				alertDialog1.show();	
	    	
	    	return flaga1;
				
	    }
	  public String Validation() {
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
	  public void sendGetRequest(View View, String url) {
	    	GetClass1 getclass = new GetClass1(this);
	    	getclass.setAdres(url);
		    getclass.execute();
		}

		private class GetClass1 extends AsyncTask<String, Void, String> {
			 
	        private final Context context;
	        private String url1;   
	        public String getAdres(){
	    		return url1;
	    	}
	    	public void setAdres(String i){
	    		this.url1 = i;
	    	}
	        public GetClass1(Context c){
	            this.context = c;
	        }
	        
	    protected void onPreExecute(){
	        progress= new ProgressDialog(this.context);
	        progress.setMessage("Loading");
	        progress.show();
	    }

	    protected String doInBackground(String... params) {
	        String wynik1 = "";
	        try {

	            URL url = new URL(url1);
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
			 
			 ReminderPass.this.runOnUiThread(new Runnable() {
			 @Override
			 public void run() {
			 progress.dismiss();
			 CheckResponse(wynik);
			 }
			 });
			 
			 } catch (MalformedURLException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 }
			 return wynik1;
			 }
	  
	    }
}