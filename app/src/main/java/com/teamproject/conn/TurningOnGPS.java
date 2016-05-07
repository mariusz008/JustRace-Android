package com.teamproject.conn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
 
public class TurningOnGPS {
	
		private Context ctx;
		public TurningOnGPS(Context context){
	        this.ctx = context;
	    }
		public void turnGPSOn()
		{
		     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		     intent.putExtra("enabled", true);
		     this.ctx.sendBroadcast(intent);
		
		    @SuppressWarnings("deprecation")
			String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    if(!provider.contains("gps")){ //if gps is disabled
		        final Intent poke = new Intent();
		        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
		        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		        poke.setData(Uri.parse("3")); 
		        this.ctx.sendBroadcast(poke);
		    }
		}
		public boolean checkingGPSStatus()
		{
		    @SuppressWarnings("deprecation")
			String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    if(!provider.contains("gps")){ //disabled
		    	return false;
		    } //enabled
		    else return true;
		}

		public void turnGPSOff()
		{
		    @SuppressWarnings("deprecation")
			String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    if(provider.contains("gps")){ //if gps is enabled
		        final Intent poke = new Intent();
		        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		        poke.setData(Uri.parse("3")); 
		        this.ctx.sendBroadcast(poke);
		    }
		}
}