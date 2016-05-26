package com.teamproject.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ObserverActivity extends Activity {
	  private Button button, button1, button2;
	  Intent intent1, intent2;
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_obser);
	        final Context context = this;
			button = (Button) findViewById(R.id.buttonAlert);
		  	button1 = (Button) findViewById(R.id.button1);
			button2 = (Button) findViewById(R.id.button2);
		   intent1 = new Intent(ObserverActivity.this, CompList.class);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ObserverActivity.this.finish();
				}
			});
			  button2.setOnClickListener(new OnClickListener() {
				  @Override
				  public void onClick(View arg0) {
					  intent1.putExtra("ktore", "OBSERW");
					  startActivity(intent1);
				  }
			  });
		  button1.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View arg0) {
				  intent1.putExtra("ktore", "OBSERWRESULTS");
				  startActivity(intent1);
			  }
		  });
		  button1.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
		  button2.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
    }
    

}