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

import com.teamproject.activity.CompList;
import com.teamproject.activity.Login;
import com.teamproject.activity.R;

public class MainOrg extends Fragment {
	Context context;
	private Button button, button3, button4;
	public static final String costam  = Login.SPF_NAME;
	Intent intent1, intent2, intent4, intent6;
	String ktore_zawody="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.org_tab, container, false);
        button = (Button) v.findViewById(R.id.buttonAlert);
        button3 = (Button) v.findViewById(R.id.button3);

        button4 = (Button) v.findViewById(R.id.button4);
        
        intent1 = new Intent(getActivity(), Login.class);

        intent4 = new Intent(getActivity(), CompList.class);
        button.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			wyloguj();
    			startActivity(intent1);
    			getActivity().finish();
    			
    			
    		}});
        button4.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			intent4.putExtra("ktore", "ORG");
    			startActivity(intent4);  			
    		}});
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intent4.putExtra("ktore", "ORGRESULTS");
                startActivity(intent4);
            }});

        button3.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
        button4.setBackground(getResources().getDrawable(R.drawable.rounded_border_button));
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