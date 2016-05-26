package com.teamproject.maintabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.teamproject.activity.UserProfile;
import com.teamproject.conn.TurningOnGPS;
import com.teamproject.activity.CompList;
import com.teamproject.activity.Login;
import com.teamproject.activity.R;

public class MainPart extends Fragment {
	Context context;
	private Button button, button1, button2, button3, button4, button5;
	public static final String costam  = Login.SPF_NAME;
	Intent intent1, intent2, intent3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.part_tab, container, false);
        button = (Button) v.findViewById(R.id.buttonAlert);
        button1 = (Button) v.findViewById(R.id.button1);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);
        button4 = (Button) v.findViewById(R.id.button4);
        button5 = (Button) v.findViewById(R.id.button5);
             
        intent1 = new Intent(getActivity(), Login.class);
        intent2 = new Intent(getActivity(), CompList.class);
        intent3 = new Intent(getActivity(), UserProfile.class);
        button.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			wyloguj();
    			startActivity(intent1);
    			getActivity().finish();
    		}});

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(intent3);
            }
        });
        button1.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intent2.putExtra("ktore", "OGOLNE");
                startActivity(intent2);
            }
        });
        button2.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intent2.putExtra("ktore", "OSOBISTE");
                startActivity(intent2);
            }
        });
        button3.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intent2.putExtra("ktore", "OGOLNERESULTS");
                startActivity(intent2);
            }
        });
        button5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intent2.putExtra("ktore", "OSOBISTERESULTS");
                startActivity(intent2);
            }
        });
        button4.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
        button5.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));

        return v;
    }
    public void wyloguj(){
    	{
            SharedPreferences loginPreferences = this.getActivity().getSharedPreferences(costam, Context.MODE_PRIVATE);
            loginPreferences.edit().clear().commit();
        }
        Toast.makeText(getActivity(), "Zostałeś poprawnie wylogowany", Toast.LENGTH_LONG ).show();;

    }
	

}