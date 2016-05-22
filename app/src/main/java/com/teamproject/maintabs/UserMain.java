package com.teamproject.maintabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.teamproject.activity.R;


public class UserMain extends FragmentActivity {
    private FragmentTabHost mTabHost;
    final Context context11 = this;
    String ktore_zawody="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Zawodnik", null),
                MainPart.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Organizator", null),
                MainOrg.class, null);
    }

}