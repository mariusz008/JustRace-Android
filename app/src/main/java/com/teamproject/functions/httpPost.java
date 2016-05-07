package com.teamproject.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.Toast;

@SuppressLint("NewApi")
public class httpPost {

	
	public String POST(String url1)
	{
		String ostateczne = "";
		try {		    
		      URL url = new URL(url1);
		      HttpURLConnection con = (HttpURLConnection) url.openConnection();	
		      con.setDoOutput(true);
		      con.setRequestMethod("POST");
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
	
}
