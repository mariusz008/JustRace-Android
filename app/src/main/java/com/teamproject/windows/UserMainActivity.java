package com.teamproject.windows;

import com.teamproject.organizer.mainOrg;
import com.teamproject.participant.mainPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


public class UserMainActivity extends FragmentActivity {
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
                mainPart.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Organizator", null),
                mainOrg.class, null);
    }
}