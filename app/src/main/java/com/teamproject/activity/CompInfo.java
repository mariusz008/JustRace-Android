package com.teamproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.teamproject.conn.TurningOnGPS;
import com.teamproject.functions.DialogCommunications;
import com.teamproject.functions.RestController;
import com.teamproject.functions.TimeValidation;
import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;


public class CompInfo extends Activity {
	private Button button2, button1;
	final Context context = this;
	private TextView datarozTV, nazwaTV, miejscowoscTV, typTV, godzrozpTV, datazakTV, godzzakTV, limitTV, oplataTV, opisTV, katTV, kat;
	Intent intent4 = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	TurningOnGPS gpssx;
	String error, error1, ret, ret1, whichList1, kategoria, ktore_zawody= "";
	final competitionDTO competition = CompList.comp;
	String ID_zad = competition.getID_zawodow();
	DialogCommunications comm = new DialogCommunications(context);
	ImageView typIV;
	Spinner mSpinner;
	final userDTO user1 = Login.user;
	String ID_usera = user1.getID_uzytkownika();
	TimeValidation tv = new TimeValidation();
	boolean flaga1, flaga2, mappc, mapoi, matrase, mozezapisac;
	int inn, ilosc_linii;
	Intent intent2, intent3, intent5, intentmapa, intentlista;
	String success1, success;
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
		intent3 = new Intent(CompInfo.this, MakingRoute.class);
		intent5 = new Intent(CompInfo.this, StartComp.class);
		intentlista = new Intent(CompInfo.this, CompetitorsList.class);
		intentmapa = new Intent(CompInfo.this, DrawRoute.class);
		String url="http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition?id="+ID_zad;
		sendHttpRequest(url, "GET", 0, true);

		if (whichList1.equals("OGOLNE")){
			button1.setText("Zapisz się na zawody");
			button1.setBackgroundResource(R.color.navyblue);
			inn = 1;
		}
		if (whichList1.equals("OSOBISTE")){
			button1.setText("Wypisz się z zawodów");
			button1.setBackgroundResource(R.color.navyblue);
			inn = 2;
		}
		if (whichList1.equals("ORG")){
			button1.setText("Ustal trasę");
			button1.setBackgroundResource(R.color.navyblue);
			inn = 3;
		}
		if (whichList1.equals("OBSERW")){
			button1.setText("Zobacz listę uczestników");
			button1.setBackgroundResource(R.color.navyblue);
			inn = 4;
		}


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
					if(mozezapisac) {
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
										addSpinners();
									}
								});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
					else{
						comm.alertDialog("Zapis na zawody", "Organizator nie stworzył kategorii do tych zawodów. Nie możesz się na nie zapisać");
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
									sendHttpRequest(url3, "DELETE", 0, true);
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
						comm.alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
					}
				}
				if (inn == 4){
					startActivity(intentlista);
				}
				
				}
			});
	}

	public void addSpinners(){
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
				sendHttpRequest(url2, "PUT", 0, true);
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
			kategoria = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void addButton(final String x, final Intent y, int z) {
		TableLayout table = (TableLayout) findViewById(R.id.tableButtons);
		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT
		));
		TableLayout.LayoutParams tableRowParams=
				new TableLayout.LayoutParams
						(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
		tableRowParams.setMargins(0, 0, 0, 10);
		tableRow.setLayoutParams(tableRowParams);
		table.addView(tableRow);
		Button button = new Button(this);
		button.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT
		));
		button.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		button.setBackgroundResource(z);
		button.setTextColor(getApplication().getResources().getColor(R.color.white));
		button.setText(x);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (y == null) {
					setCompTime();
				} else if (x.equals("START")) {
					if (gpssx.checkingGPSStatus()) {
						String url = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/user/start?competition_id="
								+ID_zad+"&user_id="+ID_usera;
						sendHttpRequest(url, "PUT", 0, true);
					} else {
						comm.alertDialog("Pobieranie lokalizacji", "Proszę włączyć usługę GPS");
					}
				} else
					startActivity(y);
			}

		});
		tableRow.addView(button);
	}

	public void setCompTime(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		final View arg2 = null;
		final EditText input = new EditText(CompInfo.this);
		input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		alertDialogBuilder.setView(input);
		alertDialogBuilder.setTitle("Zarządzanie zawodami");
		alertDialogBuilder
				.setMessage("Podaj godzinę startu zawodów:")
				.setCancelable(false)
				.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String time = input.getText().toString();
						if (time.contains("-")) time = new String(time.replace("-", ":"));
						else if (time.contains("/")) time = new String(time.replace("/", ":"));
						if (!tv.validate(time)) {
							comm.alertDialog("Komunikat", "Wpisz poprawny format daty (hh:mm)");
						} else {
							String url4 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/event/start?competition_id=" + ID_zad +
									"&owner_id=" + ID_usera + "&time=" + time;
							sendHttpRequest(url4, "PUT", 1, true);
						}
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void sendHttpRequest(String url, final String operation, final int i, boolean showpd){
		RestController rc = new RestController(this){
			@Override
			public void onResponseReceived(String result) {
				if (operation == "GET"&& i==0){
					//pobierz dane zawodow
					try {
						parsingJSON(result);
					} catch (JSONException e) {

					}
					String url1 ="http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/category/list?competition_id="+ID_zad;
					//pobierz kategorie
					sendHttpRequest(url1, "GET", 1, false);
				}
				else if (operation == "GET"&& i == 1){
					//pobranie kategorii
					try {
						getCategory(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} //wypisz sie z zawodow
				else if (operation == "DELETE"){
					checkResponseSignOutAndSetTime(result);
				} //ustalanie godziny
				else if (operation == "PUT" && i == 1){
					checkResponseSignOutAndSetTime(result);
				} //zapisywanie na zawody
				else if (operation == "PUT" && i == 0){
					checkResponseSignUpAndCheckStart(result);
				}
			}
		};
		rc.setAddress(url);
		rc.setOperation(operation);
		if(showpd) rc.setShowPD(true);
		rc.execute();
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

	public void checkResponseSignUpAndCheckStart(String wejscie)
    {
    	String komunikat="";
    	flaga1 = true;				  		
    		if (wejscie.contains("Ok")){
				flaga1 = true;
				success = "Udało Ci się zapisać na zawody!";
			}
			else if (wejscie.contains("User set as ready")){
				flaga1 = true;
				success = "Jesteś gotowy aby wziąć udział w zawodach. Przejdź do panelu pomiaru czasu";
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
			else if (wejscie.contains("Competition disactivated")){
				flaga1 = false;
				error = "Zawody zostały dezaktywowane przez organizatora";
			}
			else if (wejscie.contains("Competition don't have start time set yet")){
				flaga1 = false;
				error = "Zawody nie mają jeszcze ustalonej dokładnej godziny startu";
			}
			else if (wejscie.contains("Competition already started")){
				flaga1 = false;
				error = "Start zawodów już się odbył. Spóźniłeś się";
			}
			else if (wejscie.contains("Number not set")){
				flaga1 = false;
				error = "Nie masz jeszcze nadanego numeru zawodnika";
			}
			else if (wejscie.length()==0){
				flaga1 = false;
				error = "Wystąpił problem w połączeniu z serwerem. Spróuj ponownie później";
			}

		if (flaga1 == false) {
			ret = error;
		} else {
			ret = success;
		}
		final String tt = wejscie;
		Context context2 = this;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
		alertDialogBuilder.setTitle("Komunikat");
		alertDialogBuilder
				.setMessage(ret)
			.setCancelable(false)
			.setNeutralButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
					if ((ret1==success1) && tt.contains("User set as ready")){
						startActivity(intent5);
					}
				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();
    }
	public void checkResponseSignOutAndSetTime(String wejscie)
	{
		String komunikat1="";
    	flaga2 = true;				
   		
    		if (wejscie.contains("Ok")){
			flaga2 = true;
				success1 = "Udało Ci się wypisać z zawodów!";
				komunikat1 = "Udany wypis z zawodów";
			}
			else if (wejscie.contains("Start set")){
				flaga2 = true;
				success1 = "Udało Ci się ustalić godzinę startu zawodów";
				komunikat1 = "Ustalanie godziny startu";
			}
			else if (wejscie.contains("No such record")){
    			flaga2 = false;
    			error1 = "Nie jesteś zapisany na te zawody";
    			}
			else if (wejscie.contains("Zawody juz sie odbyly")){
    			flaga2 = false;
    			error1 = "Zawody już się odbyły. Nie możesz sie z nich wypisać";
    			}
			else if (wejscie.length()==0){
				flaga1 = false;
				error = "Wystąpił problem w połączeniu z serwerem. Spróuj ponownie później";
			}
    		
			if (flaga2==false){
				ret1=error1;
			}
			else{ 
				ret1=success1;
			}
			final View arg0 = null;
		final String tt = wejscie;
    	Context context2 = this;   	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context2);
		alertDialogBuilder.setTitle("Komunikat");
		alertDialogBuilder
			.setMessage(ret1)
			.setCancelable(false)
			.setNeutralButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				if ((ret1==success1) && tt.contains("Ok")){
					ktore_zawody = "OSOBISTE";
	    			intent2.putExtra("ktore", ktore_zawody);
	    			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    			startActivity(intent2);					
				}

				}});
			AlertDialog alertDialog1 = alertDialogBuilder.create();
			alertDialog1.show();
	}

	public void parsingJSON(String JSON) throws JSONException
	{
		if(JSON.contains("ROUTE_ID")) mappc=true;
		if(JSON.contains("ROUTEPOI_ID")) mapoi=true;
		if(JSON.contains("TRACK_ID")) matrase=true;
		if (whichList1.equals("OSOBISTE")){
			if(matrase&&mappc)
				addButton("START", intent5, R.color.navyblue);
		}
		if (mappc || matrase) addButton("Zobacz trasę", intentmapa, R.color.navyblue);
		if (whichList1.equals("ORG")){
			if(matrase && mappc)
				addButton("Ustal godzinę startu", null, R.color.navyblue);
		}
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
		ilosc_linii=(int)(spanned.length()/25);
		if (ilosc_linii==0) ilosc_linii=2;
		++ilosc_linii;
		opisTV.setLines(ilosc_linii);
		opisTV.setText(spanned);

		if (typ.contains("arciars")){
			typIV.setImageResource(R.mipmap.ic_narciarskie);
			}
		else if (typ.contains("Kolarstwo") || typ.contains("kolarstwo")){
			typIV.setImageResource(R.mipmap.ic_rowerowe);
			} 
		else if 
			(typ.contains("Bieg") || typ.contains("Chód") || typ.contains("bieg")){
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

		if(JSON.length()>3) {
			mozezapisac = true;
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

