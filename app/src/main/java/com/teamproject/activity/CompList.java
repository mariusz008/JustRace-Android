package com.teamproject.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.teamproject.functions.RestController;
import com.teamproject.models.competitionDTO;
import com.teamproject.models.userDTO;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompList extends Activity {
	private Button button, button1;
	private EditText nazwaET, miejsET;
	final Context context = this;
	static competitionDTO comp = new competitionDTO();
	Intent intent, intent1;
	SlidingDrawer slidingdrawer;
	Button SlidingButton;
	int flow, row;
	String whichList, typ, url1, ileOsob;
	boolean focus;
	ArrayList<String> stringArray = new ArrayList<String>();
	ArrayList<String> stringArray1 = new ArrayList<String>();
	ArrayList<String> stringArray2 = new ArrayList<String>();
	ArrayList<String> stringArray3 = new ArrayList<String>();
	ArrayList<String> stringArray4 = new ArrayList<String>();
	ArrayList<String> stringArray5 = new ArrayList<String>();
	ArrayList<String> stringArray6 = new ArrayList<String>();
	Spinner spinner;
	ArrayAdapter<CharSequence> adapter;

	public void onCreate(Bundle savedInstanceState) {
		slidingdrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		SlidingButton = (Button) findViewById(R.id.handle);
		@SuppressLint("NewApi")
		final userDTO us = Login.user;
		final String ID_usera = us.getID_uzytkownika();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comp_list);
		Intent intentX = getIntent();
		whichList = intentX.getExtras().getString("ktore");
		intent = new Intent(this, CompInfo.class);
		intent1 = new Intent(this, ResultsList.class);
		nazwaET = (EditText) findViewById(R.id.editText2);
		miejsET = (EditText) findViewById(R.id.editText3);
		button = (Button) findViewById(R.id.buttonAlert);
		button1 = (Button) findViewById(R.id.button1);

		if (whichList.contains("OGOLNE") || whichList.contains("OBSERW")) {
			url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/all?type=&name=&place=";
		}
		if (whichList.contains("OSOBISTE")) {
			url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/user/list?user_id=" + ID_usera + "&type=&name=&place=";
		}
		if (whichList.contains("ORG")) {
			url1 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/my?user_id=" + ID_usera + "&type=&name=&place=";
		}
		sendHttpRequest(url1, "GET");
		focus = false;
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
				typ = String.valueOf(spinner.getSelectedItem());
				String typ1 = new String(typ.replace(" ", "%20"));
				String nazwa = nazwaET.getText().toString();
				String miejsc = miejsET.getText().toString();
				if (whichList.equals("OGOLNE") || whichList.contains("OBSERW")) {

					url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/all?"
							+ "type=" + typ1 + "&name=" + nazwa + "&place=" + miejsc;
				}
				if (whichList.equals("OSOBISTE")) {
					url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/user/list?"
							+ "user_id=" + ID_usera + "&type=" + typ1 + "&name=" + nazwa + "&place=" + miejsc;
				}
				if (whichList.equals("ORG")) {
					url2 = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/competition/my?"
							+ "user_id=" + ID_usera + "&type=" + typ1 + "&name=" + nazwa + "&place=" + miejsc;
				}
				sendHttpRequest(url2, "GET");
			}
		});

		spinner = (Spinner) findViewById(R.id.spinner1);
		adapter = ArrayAdapter.createFromResource(this, R.array.competitions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner.setAdapter(adapter);

	}

	private void populateButtons(int i, ArrayList<String> data, ArrayList<String> nazwa, ArrayList<String> miasto,
								 final ArrayList<String> id, ArrayList<String> typ, ArrayList<String> ileOsob,
								 ArrayList<String> pcc) {
		TableLayout table = (TableLayout) findViewById(R.id.tableButtons);
		table.removeAllViews();
		for (row = 0; row < i; row++) {
			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.MATCH_PARENT
			));
			table.addView(tableRow);
			Button button = new Button(this);
			button.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.WRAP_CONTENT,
					TableRow.LayoutParams.WRAP_CONTENT
			));
			final String id_zawodow = id.get(row);
			button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

			if (typ.get(row).contains("arciars")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_narciarskie, 0, 0, 0);
			} else if (typ.get(row).contains("Kolarstwo") || typ.get(row).contains("kolarstwo")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_rowerowe, 0, 0, 0);
			} else if
					(typ.get(row).contains("Bieg") || typ.get(row).contains("Chód") || typ.get(row).contains("bieg")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_biegi, 0, 0, 0);
			} else if (typ.get(row).equals("Wyścig samolotów") || typ.get(row).equals("Wyścig balonów")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_powietrzne, 0, 0, 0);
			} else if (typ.get(row).contains("Wyścig") || typ.get(row).contains("Karting")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_motorowe, 0, 0, 0);
			} else if (typ.get(row).contains("Kajakarstwo") || typ.get(row).contains("Wioślarstwo")) {
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_lodzie, 0, 0, 0);
			} else
				button.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_inne, 0, 0, 0);
			button.setTextColor(getApplication().getResources().getColor(R.color.navyblue));
			button.setCompoundDrawablePadding(30);
			button.setBackground(getResources().getDrawable(R.drawable.rounded_border_comp));
			button.setText(data.get(row) + ",\n" + nazwa.get(row) + ",\n" + miasto.get(row));
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
			DateTime eventDate = formatter.parseDateTime(data.get(row));
			eventDate = formatter.parseDateTime(data.get(row));
			if (!focus) {
				if (!eventDate.isBeforeNow()) {
					focus = true;
					flow = row + 4;
				}

			} else {
				if (row == flow) {
					button.setFocusable(true);
					button.setFocusableInTouchMode(true);
					button.requestFocus();
				}
			}
			final int tmp = row;
			if(whichList.contains("RESULTS")) {
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						comp.setID_zawodow(id_zawodow);
						//intent.putExtra("ktory", whichList);
						startActivity(intent1);
					}

				});
			}
			else {
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						comp.setID_zawodow(id_zawodow);
						intent.putExtra("ktory", whichList);
						startActivity(intent);
					}

				});
			}
			if(whichList.contains("OGOLNERESULTS"))
			{
				if(!(ileOsob.get(row).equals("0"))&&(pcc.get(row).equals("1")))
					tableRow.addView(button);
			}
			else
			tableRow.addView(button);
		}
	}


	public void parsingJSON(String JSON) throws JSONException {
		//Toast.makeText(CompList.this, JSON, Toast.LENGTH_LONG).show();
		stringArray.clear();
		stringArray1.clear();
		stringArray2.clear();
		stringArray3.clear();
		stringArray4.clear();
		stringArray5.clear();
		stringArray6.clear();
		int i;
		JSONArray jsonarray = new JSONArray(JSON);
		for (i = 0; i < jsonarray.length(); i++) {

			JSONObject obj = jsonarray.getJSONObject(i);
			stringArray.add(obj.getString("DATA_ROZP"));
			stringArray1.add(obj.getString("NAME"));
			stringArray2.add(obj.getString("MIEJSCOWOSC"));
			stringArray3.add(obj.getString("COMPETITION_ID"));
			stringArray4.add(obj.getString("TYP"));
			if(obj.toString().contains("ILE_OSOB"))
					stringArray5.add(obj.getString("ILE_OSOB"));
			else stringArray5.add("");
			if(obj.toString().contains("ROUTE_ID"))
				stringArray6.add("1");
			else stringArray6.add("0");
		}
		populateButtons(i, stringArray, stringArray1, stringArray2, stringArray3, stringArray4, stringArray5, stringArray6);
	}

	public void sendHttpRequest(String url, String operation){
		RestController rc = new RestController(this){
			@Override
			public void onResponseReceived(String result) {
				try {
					parsingJSON(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		rc.setAddress(url);
		rc.setOperation(operation);
		rc.setShowPD(true);
		rc.execute();
	}
}

