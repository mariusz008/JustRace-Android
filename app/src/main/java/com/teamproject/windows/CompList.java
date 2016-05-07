package com.teamproject.windows;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CompList extends Activity {
	private Button button, button1;
	private EditText typET, nazwaET, miejsET;
	final Context context = this;
	boolean flaga, flaga1;
	String error, ret, success="";
	public ProgressDialog progress;
	static competitionDTO comp = new competitionDTO();
	Intent intent;
	SlidingDrawer slidingdrawer;
	Button SlidingButton;
	int flow;
	String whichList="";
	boolean focus = false;
	ArrayList<String> stringArray = new ArrayList<String>();
	ArrayList<String> stringArray1 = new ArrayList<String>();
	ArrayList<String> stringArray2 = new ArrayList<String>();
	ArrayList<String> stringArray3 = new ArrayList<String>();
	ArrayList<String> stringArray4 = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
		slidingdrawer = (SlidingDrawer)findViewById(R.id.slidingDrawer1);
		SlidingButton = (Button)findViewById(R.id.handle);
        @SuppressLint("NewApi")
		final userDTO us = Login.user;
		final String imie = us.getImie();
		final String ID_usera = us.getID_uzytkownika();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comp_list);	
		Intent intentX = getIntent();
		
		whichList = intentX.getExtras().getString("ktore");
		intent = new Intent(this, CompInfo.class);
		typET = (EditText) findViewById(R.id.editText1);
		nazwaET = (EditText) findViewById(R.id.editText2);
		miejsET = (EditText) findViewById(R.id.editText3);
		button = (Button) findViewById(R.id.buttonAlert);
		button1 = (Button) findViewById(R.id.button1);
		final Context context = this;
		View arg0 = null;
		//Toast.makeText(context, whichList, Toast.LENGTH_LONG).show();
		String url1 = "";
		if (whichList.equals("OGOLNE") || whichList.equals("OBSERW")){
		url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/all?type=&name=&place=";
		}
		if (whichList.equals("OSOBISTE")){
			url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/user/list?user_id="+ID_usera+"&type=&name=&place=";
		}
		if (whichList.equals("ORG")){
			url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/my?user_id="+ID_usera+"&type=&name=&place=";
		}
		sendGetRequest(arg0, url1);			
		
		
        button.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
				CompList.this.finish();
    			}
    		});
        button1.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			String url2 = "";
    			String typ = typET.getText().toString();
    			String nazwa = nazwaET.getText().toString();
    			String miejsc = miejsET.getText().toString();
    			if (whichList.equals("OGOLNE")){
    				
    			url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/all?"
    					+ "type="+typ+"&name="+nazwa+"&place="+miejsc;
    			}
    			if (whichList.equals("OSOBISTE")){
    				url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/user/list?"
    						+ "user_id="+ID_usera+"&type="+typ+"&name="+nazwa+"&place="+miejsc;
    			}
    			if (whichList.equals("ORG")){       			
    				url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/my?"
    						+ "user_id="+ID_usera+"&type="+typ+"&name="+nazwa+"&place="+miejsc;

    			}
    			
    			sendGetRequest(arg0, url2);	
    		}
        });
	}

	private void populateButtons(int i, ArrayList<String> data, ArrayList<String> nazwa, ArrayList<String> miasto, 
			final ArrayList<String> id, ArrayList<String> typ) {
		TableLayout table = (TableLayout) findViewById(R.id.tableButtons);
		int row;
		table.removeAllViews();
		for (row=0;row<i;row++){
			
			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.MATCH_PARENT
					));
			table.addView(tableRow);
			Button button = new Button(this);
			button.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.MATCH_PARENT
					));
			final String id_zawodow = id.get(row);
			button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);			
			button.setHeight(300);
			button.setWidth(750);
				if (typ.get(row).contains("arciars")){
					button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_narciarskie, 0, 0, 0);
				}
			else if (typ.get(row).contains("Kolarstwo") || typ.get(row).contains("kolarstwo")){
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_rowerowe, 0, 0, 0);
				} 
			else if 
				(typ.get(row).contains("Bieg") || typ.get(row) == "Chód" || typ.get(row).contains("bieg")){
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_biegi, 0, 0, 0);			
				}
			else if (typ.get(row).equals("Wyścig samolotów") || typ.get(row).equals("Wyścig balonów")){
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_powietrzne, 0, 0, 0);
				}
			else if (typ.get(row).contains("Wyścig") || typ.get(row).contains("Karting")){
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_motorowe, 0, 0, 0);
				}
			else if (typ.get(row).contains("Kajakarstwo") || typ.get(row).contains("Wioślarstwo")){
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_lodzie, 0, 0, 0);
				}
			else
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_inne, 0, 0, 0);

			button.setCompoundDrawablePadding(50);
			button.setBackground(getResources().getDrawable(R.drawable.rounded_shape));
			button.setText(data.get(row) + ",\n" + nazwa.get(row) + ",\n" + miasto.get(row));
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
			DateTime eventDate = formatter.parseDateTime(data.get(row));			
			eventDate = formatter.parseDateTime(data.get(row));			 
				if (!focus){
				if(!eventDate.isBeforeNow())
				{
					focus = true;
					flow = row+2;
				}
				}
				else{
					if (row == flow)
					{
						button.setFocusableInTouchMode(true);
						button.requestFocus();
					}
				}
		    final int tmp = row;
			button.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					comp.setID_zawodow(id_zawodow);
					intent.putExtra("ktory", whichList);
					startActivity(intent);
				}

			});
			//button.setLayoutParams(params);
			tableRow.addView(button);
		}
	}
	
	
	public void parsingJSON(String JSON) throws JSONException
	{
		int i;		
		JSONArray jsonarray = new JSONArray(JSON);
		for (i=0; i < jsonarray.length(); i++) {
			
			 JSONObject obj = jsonarray.getJSONObject(i);
			 stringArray.add(obj.getString("DATA_ROZP"));
			 stringArray1.add(obj.getString("NAME"));
			 stringArray2.add(obj.getString("MIEJSCOWOSC"));
			 stringArray3.add(obj.getString("COMPETITION_ID"));		
			 stringArray4.add(obj.getString("TYP"));
		}
		populateButtons(i, stringArray, stringArray1, stringArray2, stringArray3, stringArray4);
		Toast.makeText(CompList.this, i+" zawodów", Toast.LENGTH_SHORT).show();
	}
	
	public void sendGetRequest(View View, String adr) {
    	GetClass1 getclass = new GetClass1(this);
	    getclass.execute();
	    getclass.setAdres(adr);
	}


	private class GetClass1 extends AsyncTask<String, Void, String> {
		 
        private final Context context;  
        private String adres;
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

            URL url = new URL(adres);
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
		 
		 CompList.this.runOnUiThread(new Runnable() {
		 @Override
		 public void run() {
		 try {
				parsingJSON(wynik);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public void setAdres(String adres) {
		this.adres = adres;
	}
  
    }
 
	 
}

