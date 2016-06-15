package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamproject.conn.ConnectionDetector;
import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.RestController;
import com.teamproject.maintabs.UserMain;
import com.teamproject.models.userDTO;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity {
	public static final String SPF_NAME = "vidslogin";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	boolean flaga = true;
	boolean flaga1;
	String error, success = "";
	boolean rem_pass = false;
	final static userDTO user = new userDTO();
	final Context context = this;
	private Button button, button1, button2;
	private TextView rej_click, forget_pass;
	private EditText login, haslo;
	private CheckBox remember_pass;
	Intent intent, intent1, intent2, intent3;
	DialogCommunications comm;
	ConnectionDetector cd = new ConnectionDetector(context);
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
		intent2 = new Intent(this, UserMain.class);
		intent3 = new Intent(this, ReminderPass.class);
		comm = new DialogCommunications(context);
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
				String url1 = validation();
				if (url1!="") {
					sendHttpRequest(url1, "GET");
					try {
						rememberPass();
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

	public void rememberPass() throws JSONException{
		        String strUserName = login.getText().toString().trim();
		        String strPassword = haslo.getText().toString().trim();
		        if (null == strUserName || strUserName.length() == 0)
		                    {
		            login.requestFocus();
		        } else if (null == strPassword || strPassword.length() == 0)
		                    {
		            haslo.requestFocus();
		        } else
		                    {
		            if (remember_pass.isChecked())
		                            {
		                SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
		                loginPreferences.edit().putString(USERNAME, strUserName).putString(PASSWORD, strPassword).commit();
		            }}
		        
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

	public void sendHttpRequest(String url, String operation){
			RestController rc = new RestController(this) {
				@Override
				public void onResponseReceived(String result) {
					try {
						checkResponse(result);
						parsingJSON(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			rc.setAddress(url);
			rc.setOperation(operation);
			rc.setShowPD(true);
			rc.execute();
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
				comm.alertDialog("Komunikat", error);
			}
			else {
				Login.this.finish();
				startActivity(intent2);
			}
		
		
    }
    public String validation() {
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
			comm.alertDialog("Komunikat o błędzie", error);
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

	