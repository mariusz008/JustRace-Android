package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.RestController;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration extends Activity {
	boolean flaga, flaga1;
	final Context context = this;
	private CheckBox checkBox1, checkBox2, checkBox3;
	private EditText imieT, nazwiskoT, loginT, haslo1T, haslo2T, emailT, wiekT, klubT, obywT, nrtelT, iceT;
	String error, success, ret;
	DialogCommunications comm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
		Button button = (Button) findViewById(R.id.buttonAlert);
		Button button1 = (Button) findViewById(R.id.RegButton);
		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        imieT = (EditText) findViewById(R.id.editText1);
        nazwiskoT = (EditText) findViewById(R.id.editText2);
        loginT = (EditText) findViewById(R.id.editText3); 
        haslo1T = (EditText) findViewById(R.id.editText4);
        haslo2T = (EditText) findViewById(R.id.editText5);
        emailT = (EditText) findViewById(R.id.editText6);
        wiekT = (EditText) findViewById(R.id.editText7);
        klubT = (EditText) findViewById(R.id.editText8);
        obywT = (EditText) findViewById(R.id.editText9);
        nrtelT = (EditText) findViewById(R.id.editText10);
        iceT = (EditText) findViewById(R.id.editText11);
		comm = new DialogCommunications(context);
		button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Registration.this.finish();
			}
		});
		button1.setOnClickListener(new OnClickListener() {			
		public void onClick(View arg0) {
			String url1 = "";
			try {
				url1 = validation();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (url1.length()!=0)
			{
				sendHttpRequest(url1, "PUT");
			}
			}
		});	
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
	
    public void checkResponse(String wejscie)
    {
    	String komunikat="";
    	flaga1 = true;				
			if (wejscie.contains("Login already in use")){
			flaga1 = false;
			error = "Użytkownik o takim loginie już istnieje!";
			}
			else if (wejscie.contains("Email already in use")){
			flaga1 = false;
			error = "Użytkownik o takim emailu już istnieje!";
			}
			else if (wejscie.contains("User added")){
			flaga1 = true;
				success = "Zostałeś poprawnie zarejestrowany. Zweryfikuj swoje konto klikając w link wysłany na podany przez Ciebie adres e-mail.";
			}
			else {
				flaga1 = false;
				error = "Wystąpił nieoczekiwany błąd - spróbuj ponownie później";
			}
		
			if (flaga1==false){
				ret=error;
				komunikat = "Komunikat o błędzie";
			}
			else{ 
				ret=success;
				komunikat = "Udana rejestracja";
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
					Registration.this.finish();
				}
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();
    }

    public void onCheckboxClicked(View view) {
        switch(view.getId()) {
            case R.id.checkBox1:
                    checkBox2.setChecked(false);
                break;
            case R.id.checkBox2:
                    checkBox1.setChecked(false);
                break;
        }
    }
    
    public String validation() throws UnsupportedEncodingException {
    	String url="";
    	flaga = true;
        String imie = imieT.getText().toString(); 
        String nazwisko = nazwiskoT.getText().toString();
        String login = loginT.getText().toString();
        String haslo = haslo1T.getText().toString();
        String haslo1 = haslo2T.getText().toString();
        String email = emailT.getText().toString();       
        String klub = klubT.getText().toString();           	 	   	                                   
        String wiek = wiekT.getText().toString();
        String plec = "";
        String obywatelstwo = obywT.getText().toString();
        String nr_tel = nrtelT.getText().toString();
        String ICE = iceT.getText().toString();
        
        //sprawdzenie emaila
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        

        if (checkBox1.isChecked())
        {
        	plec = "M";
        }
        if (checkBox2.isChecked())
        {
        	plec = "K";
        }
        if (!checkBox3.isChecked()){
        	flaga = false;
        	error = "Wymagana jest zgoda na przetwarzanie danych osobowych";
        }
//        if ((matcher.matches() == false)) {
//        	flaga = false;
//        	error = "Podany email jest niepoprawny!";
//        }       
        if (!haslo.equals(haslo1)){
        	flaga = false;
        	error = "Podane hasła nie są identyczne!";
        } 
        if ((haslo.matches("[A-Za-z0-9]+") == false))
    	{
    		flaga = false;
    		error = "Hasło może składać się tylko z liter i cyfr (bez polskich znaków)";
    	}
        if ((haslo.length()<8 || haslo.length()>20)){
      		flaga = false;
      		error = "Hasło powinno mieć od 8 do 20 znaków!";
      	}      
    	if ((login.matches("[A-Za-z0-9]+") == false))
    	{
    		flaga = false;
    		error = "Login może składać się tylko z liter i cyfr (bez polskich znaków)";
    	}
    	if ((login.length()<5 || login.length()>20)){
    		flaga = false;
    		error = "Login powinien mieć od 5 do 20 znaków!";
    	}
    	if ((nazwisko.matches("[A-Za-z]+") == false))
    	{
    		flaga = false;
    		error = "Nazwisko może składać się tylko z liter (bez polskich znaków)";
    	}
    	if ((imie.matches("[A-Za-z]+") == false))
    	{
    		flaga = false;
    		error = "Imię może składać się tylko z liter (bez polskich znaków)";
    	}
    	if ((imie.length() == 0 || nazwisko.length() == 0 || login.length() == 0 || 
        		haslo.length() == 0 || email.length() == 0 || wiek.length()==0 || plec.length()==0))
        {
        	flaga = false;
        	error = "Proszę wypełnić wszystkie wymagane pola (oznaczone gwiazdką)";
        }
    	if (flaga == false){
			comm.alertDialog("Komunikat o błędzie", error);
    	}
    	else {	
    		url = URLaddress(imie, nazwisko, login, haslo, email, wiek, plec, klub, obywatelstwo, nr_tel, ICE); 
    	}
    	return url;
    	
    }
    
	public String URLaddress(String imieS, String nazwiskoS, String loginS, String hasloS, 
			String emailS, String wiekS, String plecS, String klubS, String obywS, String nrtel, String ICE) throws UnsupportedEncodingException
	{
		String klub = URLEncoder.encode(klubS,"UTF-8");
		String obyw = URLEncoder.encode(obywS,"UTF-8");
		String URL = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/user?"
				+ "name="+imieS+
				"&surname="+nazwiskoS+
				"&login="+loginS+
				"&password="+hasloS+				
				"&email="+emailS+
				"&age="+wiekS+
				"&sex="+plecS+
				"&club="+klub+
				"&obywatelstwo="+obyw+
				"&nr_tel="+nrtel+
				"&ice="+ICE+"";
		return URL;
	}
	
}



