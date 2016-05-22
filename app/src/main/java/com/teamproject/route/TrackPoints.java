package com.teamproject.route;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.teamproject.activity.R;

public class TrackPoints extends Fragment {
    Context context;
    private Button buttonS1, buttonS2, buttonPK1, buttonPK2, buttonM1, buttonM2, buttonPotw, buttonZap, buttonPowrot;
    OnHeadlineSelectedListener mCallback;


    public interface OnHeadlineSelectedListener {
        public void operation(String i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trackpoints_tab, container, false);
        buttonS1 = (Button) v.findViewById(R.id.Button2);
        buttonS2 = (Button) v.findViewById(R.id.Button3);
        buttonPK1 = (Button) v.findViewById(R.id.Button4);
        buttonPK2 = (Button) v.findViewById(R.id.Button5);
        buttonM1 = (Button) v.findViewById(R.id.Button6);
        buttonM2 = (Button) v.findViewById(R.id.Button7);
        buttonPotw = (Button) v.findViewById(R.id.ButtonPotwierdz);
        buttonZap = (Button) v.findViewById(R.id.ButtonZapisz);

        buttonS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("S1");
            }});
        buttonS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("S2");
            }});
        buttonM1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("M1");
            }});
        buttonM2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("M2");
            }});
        buttonPK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("PK1");
            }});
        buttonPK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("PK2");
            }});
        buttonPotw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("Potw");
            }});
        buttonZap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation("Zap1");
            }});


        return v;
    }

}