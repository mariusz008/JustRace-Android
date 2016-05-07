package com.teamproject.windows;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.teamproject.models.userDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangePass extends Activity{
	boolean flaga, flaga1;
	String ret="";
	final Context context = this;
	ProgressDialog progress;
	String error, success = "";
	private EditText starehasloT, nowehaslo1T, nowehaslo2T;
	public static final String loginZap  = Login.SPF_NAME;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	  	success = "Udało Ci się zmienić hasło! Przy następnym logowaniu będziesz musiał(a) wpisać login oraz hasło!";
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
					sendGetRequest(arg0, url1);
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
  public String URLaddress(String ID, String hasloS, String hasloN1 )
	{
		String URL = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/user/password?"
				+ "user_id="+ID+
				"&old_password="+hasloS+
				"&new_password="+hasloN1+"";
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
		 connection.setRequestMethod("POST");	 
		 int responseCode = connection.getResponseCode();
		 BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		 String line = "";
		 StringBuilder responseOutput = new StringBuilder();
		 
		 while((line = br.readLine()) != null ) {
		 responseOutput.append(line);
		 }
		 br.close();
		 final String wynik = responseOutput.toString();
		 wynik1=wynik;

		 ChangePass.this.runOnUiThread(new Runnable() {
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
		 return wynik1;
		 }
  	protected void onPostExecute(String result) {
			CheckResponse(result);	
  }
	
  }
	public boolean CheckResponse(String wejscie)
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
					wyloguj();
					ChangePass.this.finish();
				}
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();	
    	
    	return flaga1;
			
    }
	public void wyloguj(){
    	{
            SharedPreferences loginPreferences = this.getSharedPreferences(loginZap, Context.MODE_PRIVATE);
            loginPreferences.edit().clear().commit();
        }

    }
}
