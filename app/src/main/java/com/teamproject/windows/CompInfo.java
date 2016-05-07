package com.teamproject.windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamproject.conn.TurningOnGPS;
import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;
//import com.teamproject.windows.Registration.GetClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class CompInfo extends Activity {	
	private Button button2, button1;
	final Context context = this;
	private TextView datarozTV, nazwaTV, miejscowoscTV, typTV, godzrozpTV, datazakTV, godzzakTV, limitTV, oplataTV, opisTV, katTV, kat;
	ProgressDialog progress;
	Intent intent4 = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	TurningOnGPS gpssx;
	String jakis = "re";
	String error, error1, ret, ret1= "";
	String whichList1, kategoria="";
	String ktore_zawody="";
	final competitionDTO competition = CompList.comp;
	String ID_zad = competition.getID_zawodow();
	//final Spinner input1 = new Spinner(CompInfo.this);
	ImageView typIV;
	Spinner mSpinner;
	final userDTO user1 = Login.user;
	String ID_usera = user1.getID_uzytkownika();
	boolean flaga1, flaga2, mappc, mapoi, matrase, mozezapisac;
	int inn=0;
	Intent intent2, intent3, intentmapa;
	String success = "Udało Ci się zapisać na zawody!";
	String success1 = "Udało Ci się wypisać z zawodów!";
	ArrayList<String> category = new ArrayList<String>();
	ArrayList<String> description = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.competition);	
		Intent intentX = getIntent();
		gpssx = new TurningOnGPS(getApplicationContext());
		whichList1 = intentX.getExtras().getString("ktory");
		button2 = (Button) findViewById(R.id.Button2);	
		button1 = (Button) findViewById(R.id.Button1);	
		datarozTV = (TextView) findViewById(R.id.TextView1);
		nazwaTV = (TextView) findViewById(R.id.TextView2);
		miejscowoscTV = (TextView) findViewById(R.id.TextView3);
		typTV = (TextView) findViewById(R.id.TextView5);
		godzrozpTV = (TextView) findViewById(R.id.TextView7);
		datazakTV = (TextView) findViewById(R.id.TextView9);
		godzzakTV = (TextView) findViewById(R.id.TextView11);
		limitTV = (TextView) findViewById(R.id.TextView13);
		oplataTV = (TextView) findViewById(R.id.TextView15);
		kat = (TextView) findViewById(R.id.TextView16);
		katTV = (TextView) findViewById(R.id.TextView17);
		opisTV = (TextView) findViewById(R.id.TextView19);
		typIV = (ImageView) findViewById(R.id.imageView1);
		intent2 = new Intent(CompInfo.this, CompList.class);
		intent3 = new Intent(CompInfo.this, GoogleMap.class);
		intentmapa = new Intent(CompInfo.this, MapsActivity.class);
		if (whichList1.equals("OGOLNE")){
			button1.setText("Zapisz się na zawody");
			button1.setBackgroundResource(R.color.teal700);
			inn = 1;
		}
		if (whichList1.equals("OSOBISTE")){
			button1.setText("Wypisz się z zawodów");
			button1.setBackgroundResource(R.color.teal700);
			inn = 2;
		}
		if (whichList1.equals("ORG")){
			button1.setText("Ustal trasę");
			button1.setBackgroundResource(R.color.teal700);
			inn = 3;
		}
		if (whichList1.equals("OBSERW")){
			button1.setText("Zobacz listę uczestników");
			button1.setBackgroundResource(R.color.teal700);
			inn = 4;
		}
		String url="http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition?id="+ID_zad;
		sendGetRequest(0, url, "GET");

		new DownloadImageTask(typIV)
        .execute("http://209785serwer.iiar.pwr.edu.pl/RestImage/rest/competition/get/image?competition_id="+ID_zad);

		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CompInfo.this.finish();
				}
			});	
		button1.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg1) {
				if (inn == 1){
					if(matrase) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder.setTitle("Zapis na zawody");
						alertDialogBuilder
								.setMessage("Zapisując się na zawody wyrażasz zgodze na przetwarzanie Twoich danych osobowych")
								.setCancelable(false)
								.setNegativeButton("Nie zgadzam się", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								})
								.setPositiveButton("Zgadzam się", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										//positiveResponse();
										dodajSpinnery();
									}
								});

						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						alertDialogBuilder.setTitle("Zapis na zawody");
						alertDialogBuilder
								.setMessage("Organizator nie stworzył kategorii do tych zawodów. Nie możesz się na nie zapisać")
								.setCancelable(false)
								.setNeutralButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
									}
								});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					
				}
				if (inn == 2){
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
						alertDialogBuilder.setTitle("Wypis z zawodów");
						alertDialogBuilder
							.setMessage("Czy na pewno chcesz się wypisać z zawodów?")
							.setCancelable(false)
								.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								})
							.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									String url3 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/leave?competition_id=" + ID_zad + "&user_id=" + ID_usera;
									sendGetRequest(0, url3, "DELETE");
								}
							});

							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();										
				}
				if (inn == 3){
					if (gpssx.checkingGPSStatus()) {
						if (mappc && matrase && mapoi) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);
							alertDialogBuilder.setTitle("Ustal trasę");
							alertDialogBuilder
									.setMessage("Te zawody posiadają już trasę. Czy chcesz ją edytować?")
									.setCancelable(false)
									.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									})
									.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											startActivity(intent3);
										}
									});
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
						else startActivity(intent3);
					} else {
						Toast.makeText(CompInfo.this, "Proszę włączyć usługę GPS", Toast.LENGTH_LONG).show();
						startActivity(intent4);
					}
				}
				if (inn == 4){
						Toast.makeText(CompInfo.this, "W trakcie tworzenia", Toast.LENGTH_LONG).show();
				}
				
				}
			});
	}

	public void dodajSpinnery(){
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.my_dialog_layout, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptsView);

		alertDialogBuilder.setTitle("Wybierz kategorię");
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();
		final Spinner mSpinner= (Spinner) promptsView
				.findViewById(R.id.spinner1);
		final Button mButton = (Button) promptsView
				.findViewById(R.id.btnSubmit);
		final Button bButton = (Button) promptsView
				.findViewById(R.id.btnBack);
		mSpinner.setOnItemSelectedListener(new OnSpinnerItemClicked());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, category) {

			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				TextView tv = ((TextView) v);
				tv.setSingleLine();
				tv.setEllipsize(TextUtils.TruncateAt.END);
				tv.setTextSize(20);
				return v;
			}
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event?user_id=" + ID_usera + "&competition_id=" + ID_zad +
						"&category_name=" + kategoria;
				sendGetRequest1(url2);
				alertDialog.cancel();
			}
		});
		bButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alertDialog.cancel();
			}
		});

	}
	public class OnSpinnerItemClicked implements AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent,
								   View view, int pos, long id) {
//			Toast.makeText(parent.getContext(), "Clicked : " +
//					parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
			kategoria = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void add_button() {
		TableLayout table = (TableLayout) findViewById(R.id.tableButtons);
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
		button.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		button.setBackgroundResource(R.color.teal1);
		button.setHeight(100);
		button.setWidth(150);
		button.setTextColor(getApplication().getResources().getColor(R.color.white));
		button.setText("Zobacz trasę");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(intentmapa);
			}

		});
		tableRow.addView(button);
	}
	public void sendGetRequest(int i, String url, String oper) {
		    GetClass getclass = new GetClass(this);
		    getclass.setAdres(url);
		    getclass.setAkcja(oper);
			getclass.setKtoryGet(i);
		     getclass.execute();
		}
	private class GetClass extends AsyncTask<String, Void, String> {
		 
        private final Context context;
        private String adres;
        private String akcja;
		private int ktoryGet;
        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }      
            
        @Override
        protected String doInBackground(String... params) {            
            String wynik1="";
            try {           	
                URL url = new URL(adres);
			 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			 connection.setRequestMethod(akcja);
			 
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
			 CompInfo.this.runOnUiThread(new Runnable() {			 
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
        	if (akcja == "GET"&& ktoryGet==0){

        		try {
        			parsingJSON(result);
        		} catch (JSONException e) {
        			Toast.makeText(CompInfo.this, e.toString(), Toast.LENGTH_LONG).show();
        		}
				String url1 ="http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/category/list?competition_id="+ID_zad;
				sendGetRequest(1, url1, "GET");
        	}
			else if (akcja == "DELETE"){
				checkResponse1(result);
			}
			else if (ktoryGet == 1){
				progress.dismiss();
				try {
					getCategory(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
        }
        
		public void setAdres(String adres) {
			this.adres = adres;
		}
		public void setAkcja(String akcja) {
			this.akcja = akcja;
		}

		public int getKtoryGet() {
			return ktoryGet;
		}

		public void setKtoryGet(int ktoryGet) {
			this.ktoryGet = ktoryGet;
		}
	}
	public void sendGetRequest1(String url) {
	    GetClass1 getclass1 = new GetClass1(this);
	    getclass1.setAdres(url);
		getclass1.execute();
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
        
    @Override
    protected String doInBackground(String... params) {            
        String wynik1="";
        try {           	
            URL url = new URL(adres);
		 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		 connection.setRequestMethod("PUT");
		 
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
		 CompInfo.this.runOnUiThread(new Runnable() {			 
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
			checkResponse(result);
    }
    
	public void setAdres(String adres) {
		this.adres = adres;
	}
}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	if (result!=null)
	        bmImage.setImageBitmap(result);
	    }
	}

	public boolean checkResponse(String wejscie)
    {
    	String komunikat="";
    	flaga1 = true;				  		
    		if (wejscie.contains("Ok")){
			flaga1 = true;
			}	
    		else if (wejscie.length()==0){
    			flaga1 = false;
    			error = "Wykryto błąd w próbie połączenia z bazą. Spróbuj ponownie";
    			}
    		else if (wejscie.contains("Juz zapisany na te zawody")){
    			flaga1 = false;
    			error = "Jesteś już zapisany na te zawody!";
    			}
    		else if (wejscie.contains("Brak wolnych miejsc")){
    			flaga1 = false;
    			error = "Brak wolnych miejsc!";
    			}
    		else if (wejscie.contains("Zawody juz sie odbyly")){
    			flaga1 = false;
    			error = "Zawody już się odbyły. Nie możesz się na nie zapisać";
    			}
    		
			if (flaga1==false){
				ret=error;
				komunikat = "Komunikat";
			}
			else{ 
				ret=success;
				komunikat = "Udany zapis na zawody";
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
//				if (ret==success){
//					CompInfo.this.finish();
//				}
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();	
    	
    	return flaga1;
			
    }
	public boolean checkResponse1(String wejscie)
	{
		String komunikat1="";
    	flaga2 = true;				
   		
    		if (wejscie.contains("Ok")){
			flaga2 = true;
			}	
			else if (wejscie.contains("No such record")){
    			flaga2 = false;
    			error1 = "Nie jesteś zapisany na te zawody";
    			}
			else if (wejscie.contains("Zawody juz sie odbyly")){
    			flaga2 = false;
    			error1 = "Zawody już się odbyły. Nie możesz sie z nich wypisać";
    			}
    		
			if (flaga2==false){
				ret1=error1;
				komunikat1 = "Komunikat";
			}
			else{ 
				ret1=success1;
				komunikat1 = "Udany wypis z zawodów";
			}
			final View arg0 = null;
    	Context context2 = this;   	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
		alertDialogBuilder.setTitle(komunikat1);
		alertDialogBuilder
			.setMessage(ret1)
			.setCancelable(false)
			.setNeutralButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				if (ret1==success1){	    							
					ktore_zawody = "OSOBISTE";
	    			intent2.putExtra("ktore", ktore_zawody);
	    			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    			startActivity(intent2);					
				}
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();	
    	
    	return flaga2;
	}
	public void parsingJSON(String JSON) throws JSONException
	{

		if(JSON.contains("ROUTE_ID")) mappc=true;
		if(JSON.contains("ROUTEPOI_ID")) mapoi=true;
		if(JSON.contains("TRACK_ID")) matrase=true;

		if(mappc&&matrase) add_button();
		JSONObject obj = new JSONObject(JSON);		
		String naz = obj.getString("NAME");
		String miej = obj.getString("MIEJSCOWOSC");
		String typ = obj.getString("TYP");
		String datro = obj.getString("DATA_ROZP");
		String godzro = obj.getString("CZAS_ROZP");
		String datza = obj.getString("DATA_ZAK");
		String godzza = obj.getString("CZAS_ZAK");
		String lim = obj.getString("LIMIT_UCZ");
		String opl = obj.getString("OPLATA");
		String opis = obj.getString("OPIS");
		
		datarozTV.setText(datro);
		nazwaTV.setText(naz);
		miejscowoscTV.setText(miej);
		typTV.setText(typ);
		godzrozpTV.setText(godzro);
		datazakTV.setText(datza);
		godzzakTV.setText(godzza);
		limitTV.setText(lim);
		oplataTV.setText(opl);
		Spanned spanned = Html.fromHtml(opis);
		opisTV.setText(spanned);
		
		if (typ.contains("arciars")){
			typIV.setImageResource(R.mipmap.ic_narciarskie);
			}
		else if (typ.contains("Kolarstwo") || typ.contains("kolarstwo")){
			typIV.setImageResource(R.mipmap.ic_rowerowe);
			} 
		else if 
			(typ.contains("Bieg") || typ == "Chód" || typ.contains("bieg")){
			typIV.setImageResource(R.mipmap.ic_biegi);			
			}
		else if (typ.equals("Wyścig samolotów") || typ.equals("Wyścig balonów")){
			typIV.setImageResource(R.mipmap.ic_powietrzne);
			}
		else if (typ.contains("Wyścig") || typ.contains("Karting")){
			typIV.setImageResource(R.mipmap.ic_motorowe);
			}
		else if (typ.contains("Kajakarstwo") || typ.contains("Wioślarstwo")){
			typIV.setImageResource(R.mipmap.ic_lodzie);
			}
		else
		 typIV.setImageResource(R.mipmap.ic_inne);
		
	}
	public void getCategory(String JSON) throws JSONException {

		if(!(JSON.equals("[]"))) {
			mozezapisac = true;
			//Toast.makeText(CompInfo.this, JSON, Toast.LENGTH_LONG).show();
			JSONArray jsonarray = new JSONArray(JSON);
			kat.setLines(jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject obj = jsonarray.getJSONObject(i);
				category.add(obj.getString("NAME"));
				description.add(obj.getString("DESCRIPTION"));
				katTV.append(category.get(i) + ": " + description.get(i) + "\n");
			}

		}
	}

}

