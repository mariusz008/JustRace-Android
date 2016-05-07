package com.teamproject.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class httpGet {

	
	public String GET(String url1)
	{
		String ostateczne = "";
		try {		    
		      URL url = new URL(url1);
		      HttpURLConnection con = (HttpURLConnection) url.openConnection();		      
		      String wynik = readStream(con.getInputStream());	      
		    ostateczne = wynik;
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		return ostateczne;
	}
	
	private static String readStream(InputStream in) {
	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
	      
	      String nextLine = "";
	      while ((nextLine = reader.readLine()) != null) {
	        sb.append(nextLine);
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return sb.toString();
	  }
	
//	 private class MyWorkerThread extends AsyncTask<String, Void, String> {
//
//         @Override
//         protected String doInBackground(String... params) {
//                 // TODO Auto-generated method stub
//
//                 GET
//         }
//
//         // Return-value from doInBackground is the parameter here...
//      protected void onPostExecute(String result) {
//              //memo.setText(result);
//      }

//  }
}
