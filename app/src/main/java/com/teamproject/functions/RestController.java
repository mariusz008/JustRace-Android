package com.teamproject.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.teamproject.activity.Login;
import com.teamproject.conn.ConnectionDetector;

public abstract class RestController extends AsyncTask<String, Void, String> implements RestClientIF {

        private Context context;
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
				ConnectionDetector cd = new ConnectionDetector(context);
				if(cd.isConnectingToInternet()) {
					if (showPD) {
						progress = new ProgressDialog(this.context);
						progress.setMessage("Loading");
						progress.show();
					}
				}
			else {
					DialogCommunications comm = new DialogCommunications(context);
					comm.alertDialog("Połączenie z internetem", "Sprawdź połączenie z internetem i spróbuj ponownie");
					cancel(true);
				}
		}

		protected String doInBackground(String... params) {
			String wynik1 = "";
				try {
					URL url = new URL(url1);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod(operation);
					BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String line = "";
					StringBuilder responseOutput = new StringBuilder();
					while ((line = br.readLine()) != null) {
						responseOutput.append(line);
					}
					br.close();
					final String wynik = responseOutput.toString();
					wynik1 = wynik;

					RestController.runOnUI(new Runnable() {
						public void run() {
							if (showPD)
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
			if(result.contains("Action forbidden. Login again.")) {
				Toast.makeText(context, "Zaloguj się ponownie", Toast.LENGTH_LONG).show();
				Intent intentLog = new Intent(context, Login.class);
				intentLog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				((Activity) context).finish();
				context.startActivity(intentLog);
			} else
			onResponseReceived(result);
		}


}
    
