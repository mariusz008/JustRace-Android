package com.teamproject.windows;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.teamproject.conn.TurningOnGPS;

import java.util.ArrayList;

//import com.teamproject.windows.Registration.GetClass;



public class GPS extends Activity {
	boolean flaga, flaga1, stop, start, pkt_kon, meta, recording, check, check1, check2;
	private Button button2, button1, startButton, kontrButton, metaButton;
	final Context context = this;
	private TextView szerokoscTV, dlugoscTV;
	ProgressDialog progress;
	//GPStracker gpstracker;
	Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	TurningOnGPS gps;
	private LocationManager locationManager;
	private LocationListener locationListener;
	double szerokosc, dlugosc, szerokoscPoint, dlugoscPoint;
	String szer, dl, szerPoint, dlPoint;
	int i, j;
	ArrayList<String> track = new ArrayList<String>();
	ArrayList<String> control_points = new ArrayList<String>();
	Intent intent2;
	GPStracker gpstracker;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_test);
		recording = false;
		flaga1=false;
		flaga = false;
		stop = false;
		start = false;
		pkt_kon = false;
		check = false;
		check1 = false;
		check2 = false;
		i = 0;
		j = 0;
		intent2 = new Intent(this, MapsActivity.class);
		gps = new TurningOnGPS(getApplicationContext());
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		startButton = (Button) findViewById(R.id.button3);
		kontrButton = (Button) findViewById(R.id.button4);
		metaButton = (Button) findViewById(R.id.button5);

		szerokoscTV = (TextView) findViewById(R.id.textView2);
		dlugoscTV = (TextView) findViewById(R.id.textView4);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(GPS.this, "Rozpocząłeś nagrywanie", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onProviderDisabled(String provider) {
				startActivity(intent);
			}

			@Override
			public void onLocationChanged(Location location) {
				flaga1 = true;
				szerokosc = location.getLatitude();
				dlugosc = location.getLongitude();
				szer = Double.toString(szerokosc);
				dl = Double.toString(dlugosc);
				track.add(szer);
				track.add(dl);
				j++;
				szerokoscTV.setText(szer);
				dlugoscTV.setText(dl);
				if (start == true && pkt_kon == false && meta == false && check == false) {
					check = true;
					control_points.add(szer);
					control_points.add(dl);
					Toast.makeText(context, "Dodałeś punkt startu: " + szer + " " + dl, Toast.LENGTH_SHORT).show();
				} else if (start == true && pkt_kon == true && meta == false && check1 == false) {
					check1 = true;
					control_points.add(szer);
					control_points.add(dl);
					Toast.makeText(context, "Dodałeś punkt kontrolny: " + szer + " " + dl, Toast.LENGTH_SHORT).show();
				} else if (start == true && pkt_kon == true && meta == true && check2 == false) {
					check2 = true;
					control_points.add(szer);
					control_points.add(dl);
					Toast.makeText(context, "Dodałeś punkt mety: " + szer + " " + dl, Toast.LENGTH_SHORT).show();
				}
			}
		};

		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (szerokosc != 0) {
					intent2.putExtra("sz", szerokosc);
					intent2.putExtra("dl", dlugosc);
					startActivity(intent2);
				} else
					Toast.makeText(context, "Poczekaj na pobranie lokalizacji", Toast.LENGTH_LONG).show();
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				if (gps.checkingGPSStatus()) {
//	            if ((i%2)==1){
//	            	recording = false;
//	            	  Toast.makeText(GPS.this, "Zatrzymałeś nagrywanie", Toast.LENGTH_LONG).show();
//
//				locationManager.removeUpdates(locationListener); //1, 3, 5
//					//gpstracker.stopUsingGPS();
//	            } else {
//
//
//                    if(gps.checkingGPSStatus())
//		            	  Toast.makeText(GPS.this, "Rozpocząłeś nagrywanie, poczekaj na pobranie lokalizacji", Toast.LENGTH_LONG).show();
//	            	recording = true;
//					locationManager.requestLocationUpdates("gps", 2000, 0, locationListener); //0, 2, 4
//	            }
//				i++;
					if ((i % 2) == 1) {
						recording = false;
						Toast.makeText(GPS.this, "Zatrzymałeś nagrywanie", Toast.LENGTH_LONG).show();
						locationManager.removeUpdates(locationListener); //1, 3, 5
						gpstracker.stopUsingGPS();

					} else {
						locationManager.requestLocationUpdates("gps", 2000, 0, locationListener); //0, 2, 4
						if(flaga1==false) {
							gpstracker = new GPStracker(GPS.this);
							recording = true;
							Toast.makeText(GPS.this, "Rozpocząłeś nagrywanie", Toast.LENGTH_LONG).show();
							szerokosc = gpstracker.getLatitude();
							dlugosc = gpstracker.getLongitude();
							szer = Double.toString(szerokosc);
							dl = Double.toString(dlugosc);
							szerokoscTV.setText(szer);
							dlugoscTV.setText(dl);
						}
					}
					i++;
				} else {
					Toast.makeText(context, "Proszę włączyć usługę GPS", Toast.LENGTH_LONG).show();
					startActivity(intent);
				}
			}



		});
		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String szz = szerokoscTV.getText().toString();
				if (start == false) {
					if (pkt_kon == false && meta == false && recording == true && szz.length() != 0) {
						start = true;

					} else
						Toast.makeText(context, "Włącz nagrywanie trasy i poczekaj na pobranie lokalizacji", Toast.LENGTH_LONG).show();
				}
				if (start == true && pkt_kon == false && meta == false && check == false) {
					check = true;
					control_points.add(szer);
					control_points.add(dl);
					Toast.makeText(context, "Dodałeś punkt startu: " + szer + " " + dl, Toast.LENGTH_SHORT).show();
				}
				}
		});
		kontrButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (meta == false) {
					if (start == true && recording == true) {
						pkt_kon = true;
						check1 = false;
					} else
						Toast.makeText(context, "Włącz nagrywanie trasy a następnie dodaj START", Toast.LENGTH_LONG).show();
				}
				if (start == true && pkt_kon == true && meta == false && check1 == false) {
					check1 = true;
					control_points.add(szer);
					control_points.add(dl);
					Toast.makeText(context, "Dodałeś punkt kontrolny: " + szer + " " + dl, Toast.LENGTH_SHORT).show();
				}
				}
		});
		metaButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (meta == false) {
					if (start == true && pkt_kon == true && recording == true) {
						meta = true;
						add_button();
					} else
						Toast.makeText(context, "Włącz nagrywanie trasy a następnie dodaj START oraz PUNKTY KONTROLNE", Toast.LENGTH_LONG).show();
				}
				if (start == true && pkt_kon == true && meta == true && check2 == false) {
					check2 = true;
					control_points.add(szer);
					control_points.add(dl);
					Toast.makeText(context, "Dodałeś punkt mety: " + szer + " " + dl, Toast.LENGTH_SHORT).show();
				}
			}
		});

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
		button.setBackgroundResource(R.color.teal);
		button.setHeight(100);
		button.setWidth(400);
		button.setText("Zobacz trasę");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Punkty trasy: " + track.toString(), Toast.LENGTH_LONG).show();
				Toast.makeText(context, "Punkty kontrolne: " + control_points.toString(), Toast.LENGTH_LONG).show();
			}

		});
		tableRow.addView(button);
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message, final int statusone) {
		AlertDialog builder = new AlertDialog.Builder(context).create();
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (statusone == 1) {
					startActivity(intent);
				}
			}
		});
		builder.show();
	}


	public class GPStracker extends Service implements LocationListener {

		private Context context;

		boolean isGPSEnabled = false;
		boolean canGetLocation = false;
		boolean isNetworkEnabled = false;
		Location location;

		double latitude;
		double longitude;

		private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
		private static final long MIN_TIME_BW_UPDATES = 0;

		protected LocationManager locationManager;

		public GPStracker(Context context) {
			this.context = context;
			getLocation();
		}

		public Location getLocation() {
			try {
				locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
				isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

				if (!isGPSEnabled && !isNetworkEnabled) {

				} else {
					this.canGetLocation = true;

					if (isNetworkEnabled) {
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES,
								this);

						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}

					if (isGPSEnabled) {
						if (location == null) {
							locationManager.requestLocationUpdates(
									LocationManager.GPS_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_CHANGE_FOR_UPDATES,
									this);

							if (locationManager != null) {
								location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

								if (location != null) {
									latitude = location.getLatitude();
									longitude = location.getLongitude();

								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return location;
		}

		public void stopUsingGPS() {
			if (locationManager != null) {
				locationManager.removeUpdates(GPStracker.this);
			}
		}

		public double getLatitude() {
			if (location != null) {
				latitude = location.getLatitude();
			}
			return latitude;
		}

		public double getLongitude() {
			if (location != null) {
				longitude = location.getLongitude();
			}
			return longitude;
		}

		public boolean canGetLocation() {
			return this.canGetLocation;
		}

		public void showSettingAlert() {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle("GPS");
			alertDialog.setMessage("GPS jest wyłączony. Chcesz przejść do ustawień?");
			alertDialog.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(intent);
				}
			});
			alertDialog.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			alertDialog.show();
		}

		@Override
		public void onLocationChanged(Location location) {
			if (canGetLocation()) {
				// TODO Auto-generated method stub
				szerokosc = location.getLatitude();
				dlugosc = location.getLongitude();
				szer = Double.toString(szerokosc);
				dl = Double.toString(dlugosc);
				szerokoscTV.setText(szer);
				dlugoscTV.setText(dl);
				track.add(szer);
				track.add(dl);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			startActivity(intent);
		}

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}