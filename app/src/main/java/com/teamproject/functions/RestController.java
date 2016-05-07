package com.teamproject.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

import com.teamproject.windows.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class RestController extends AsyncTask<String, Void, String> {
		
        private final Context context;
        private String url1;
        private String akcja;
        private Activity activity;
		private ProgressDialog progress; 
		private String wyjscie;
		public void setAct(Activity act){
			this.activity = act;
		}
		public void setAkcja(String i){
    		this.akcja = i;
    	}
        public String getAdres(){
    		return url1;
    	}
    	public void setAdres(String i){
    		this.url1 = i;
    	}
        public RestController(Context c){
            this.context = c;
        }
        
    protected void onPreExecute(){
        progress= new ProgressDialog(this.context);
        progress.setMessage("Loading");
        //progress.setCancelable(false);
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
		 	
		 activity.runOnUiThread(new Runnable() {
		 public void run() {
		 progress.dismiss();
	 
		 }});
		 
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
    	setWyjscie(result);
//    	try {
//    		if (akcja == "LOGIN"){
//			log.checkResponse(result);
//			
////			log.parsingJSON(result);
//    		}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
    }
	public String getWyjscie() {
		return wyjscie;
	}
	public void setWyjscie(String wyjscie) {
		this.wyjscie = wyjscie;
	}
    }
    
