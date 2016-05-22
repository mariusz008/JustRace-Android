package com.teamproject.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class RestController extends AsyncTask<String, Void, String> implements RestClientIF {

        private final Context context;
        private String url1;
        private String operation;
		private boolean showPD;
		private ProgressDialog progress;
		public void setOperation(String i){
				this.operation = i;
			}
    	public void setAddress(String i){
    		this.url1 = i;
    	}
		public void setShowPD(boolean showPD) {
			this.showPD = showPD;
		}
        public RestController(Context c){
            this.context = c;
        }
		public static Handler UIHandler;

		@Override
		public abstract void onResponseReceived(String result);

		static
		{
			UIHandler = new Handler(Looper.getMainLooper());
		}
		public static void runOnUI(Runnable runnable) {
			UIHandler.post(runnable);
		}
		protected void onPreExecute(){
			if(showPD) {
				progress = new ProgressDialog(this.context);
				progress.setMessage("Loading");
				progress.show();
			}
		}

		protected String doInBackground(String... params) {
			String wynik1 = "";
			try {
				 URL url = new URL(url1);
				 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				 connection.setRequestMethod(operation);
				 BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				 String line = "";
				 StringBuilder responseOutput = new StringBuilder();
				 while((line = br.readLine()) != null ) {
				 responseOutput.append(line);
				 }
				 br.close();
				 final String wynik = responseOutput.toString();
				 wynik1 = wynik;

				 RestController.runOnUI(new Runnable() {
					 public void run() {
						 if(showPD)
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
			onResponseReceived(result);
		}


}
    
