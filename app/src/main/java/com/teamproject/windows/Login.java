package com.teamproject.windows;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamproject.functions.httpGet;
import com.teamproject.models.userDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity {
	public static final String SPF_NAME = "vidslogin"; //  <--- Add this
	private static final String USERNAME = "username";  //  <--- To save username
	private static final String PASSWORD = "password";  //  <--- To save password
	boolean flaga = true;
	boolean flaga1;
	String ret="";
	String wyjscieOST="";
	ProgressDialog progress;
	String error, success = "";
	httpGet httpget = new httpGet();
	boolean rem_pass = false;
	String JSON="";
	int yy, mm, dd = 0;
	final static userDTO user = new userDTO();
	final Context context = this;
	private Button button, button1, button2;
	private TextView rej_click, forget_pass;
	private EditText login, haslo;
	private CheckBox remember_pass;
	Intent intent, intent1, intent2, intent3, intent4;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_log);
		login = (EditText) findViewById(R.id.editText1); 
	    haslo = (EditText) findViewById(R.id.editText2); 
		button = (Button) findViewById(R.id.buttonAlert);
		button1 = (Button) findViewById(R.id.LoginButton);
		button2 = (Button) findViewById(R.id.ObservButton);		
		rej_click = (TextView) findViewById(R.id.textView3);
		forget_pass = (TextView) findViewById(R.id.textView4);
		remember_pass = (CheckBox) findViewById(R.id.checkBox1);
		intent = new Intent(this, Registration.class);
		intent1 = new Intent(this, ObserverActivity.class);
		intent2 = new Intent(this, UserMainActivity.class);
		intent3 = new Intent(this, ReminderPass.class);
		
		SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME,
	            Context.MODE_PRIVATE);
	    login.setText(loginPreferences.getString(USERNAME, ""));
	    haslo.setText(loginPreferences.getString(PASSWORD, ""));
		button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Login.this.finish();
			}
		});
		button1.setOnClickListener(new OnClickListener() {			
		public void onClick(View arg0) {
			String url1 = Validation();
			if (url1.length()!=0)
			{
				try {
					sendGetRequest(arg0, url1);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				try {
				zapamietuj();
					intent2.putExtra("username", login.getText().toString());
				
			} catch (JSONException e) {
				Toast.makeText(Login.this, e.toString(),
			       		   Toast.LENGTH_LONG).show();
			}
			}
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(intent1);
				}
			});		
		rej_click.setOnClickListener(new OnClickListener() {
		public void onClick(View arg0) {
			startActivity(intent);
			}
		});
		forget_pass.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(intent3);
				}
			});
		
	}
	public void onCheckboxClicked(View view) {
        
        if (remember_pass.isChecked()){
        	rem_pass=true;
        }
        else rem_pass=false;
    }

	public void zapamietuj() throws JSONException{
		
		    //   ADD  to save  and  read next time
		        String strUserName = login.getText().toString().trim();
		        String strPassword = haslo.getText().toString().trim();
		        if (null == strUserName || strUserName.length() == 0)
		                    {
		            //  showToast("Enter Your Name");
		            login.requestFocus();
		        } else if (null == strPassword || strPassword.length() == 0)
		                    {
		                //      showToast("Enter Your Password");
		            haslo.requestFocus();
		        } else
		                    {
		            if (remember_pass.isChecked())
		                            {
		                SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
		                loginPreferences.edit().putString(USERNAME, strUserName).putString(PASSWORD, strPassword).commit();
		            } 
		                    }
		        
}
	public void parsingJSON(String JSON) throws JSONException
	{
		
		JSONObject obj = new JSONObject(JSON);
		
		String imie = obj.getString("IMIE");
		String id = obj.getString("ID");		
		Toast.makeText(Login.this, "Witaj "+imie+"!",
	        		   Toast.LENGTH_LONG).show();	
		user.setImie(imie);
		user.setID_uzytkownika(id);


	}
	public void sendGetRequest(View View, String url) throws JSONException {
    	GetClass1 getclass = new GetClass1(this);
    	getclass.setAdres(url);
	    getclass.execute();
	}

	private class GetClass1 extends AsyncTask<String, Void, String> {
		 
        private final Context context;
        private String url1;      
    	public void setAdres(String i){
    		this.url1 = i;
    	}
        public GetClass1(Context c){
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
		 Login.this.runOnUiThread(new Runnable() {
		 @Override
		 public void run() {

		 }
		 });	 
		 } catch (MalformedURLException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		progress.dismiss();
		return wynik1;
		 }

    protected void onPostExecute(String result) {
    	try {
			checkResponse(result);
			parsingJSON(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    }
    
	
    public void checkResponse(String wejscie) throws JSONException
    {   	
    	
    	flaga1 = true;				
			if (wejscie.contains("WRONG PASSWORD OR LOGIN")){
			flaga1 = false;
			error = "Podałeś zły login lub hasło";
			}
			else if (wejscie.contains("Account not activated")){
			flaga1 = false;
			error = "To konto jest nieaktywne. Proszę aktywować je na swoim e-mailu!";
			}
			else if (wejscie.length() == 0){
			flaga1 = false;
			error = "Wykryto problem w próbie połączenia z bazą. Spróbuj ponownie później";
			}
									
			if (flaga1==false){
	    	Context context2 = this;   	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
			alertDialogBuilder.setTitle("Komunikat");
			alertDialogBuilder
			.setMessage(error)
			.setCancelable(false)
			.setNeutralButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();				
				progress.dismiss();
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();
			}
			else {
				Login.this.finish();
				startActivity(intent2);
			}
		
		
    }
    public String Validation() {
    	String url="";
    	flaga = true;
		
		String loginC = login.getText().toString(); 
        String hasloC = haslo.getText().toString();
		     
        if ((hasloC.matches("[A-Za-z0-9]+") == false))
    	{
    		flaga = false;
    		error = "Wypełnij poprawnie hasło";
    	}
        if ((loginC.matches("[A-Za-z0-9]+") == false))
    	{
    		flaga = false;
    		error = "Wypełnij poprawnie login";
    	}
        if(loginC.length()==0 || hasloC.length()==0)
        {
        	flaga = false;
        	error = "Proszę wypełnić pola login oraz hasło";
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
    		url = URLaddress(loginC, hasloC); 
    	}
    	return url;  	
    }
    
	public String URLaddress(String loginS, String hasloS)
	{
		String URL = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/user/login?"
				+ "login="+loginS+"&password="+hasloS+"";
		return URL;
	}
}

	