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

public class TrackRoute extends Fragment {
    Context context;
    private Button buttonS, buttonM, buttonZap, buttonRes;
    OnHeadlineSelectedListener mCallback;

    public interface OnHeadlineSelectedListener {
        public void operation1(String i);
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
        View v = inflater.inflate(R.layout.trackrecord_tab, container, false);

        buttonS = (Button) v.findViewById(R.id.Button2);
        buttonM = (Button) v.findViewById(R.id.Button3);
        buttonZap = (Button) v.findViewById(R.id.ButtonZapisz);
        buttonRes = (Button) v.findViewById(R.id.Button4);


        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation1("START");
            }});
        buttonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation1("META");
            }});
        buttonZap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation1("Zap2");
            }});
        buttonRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation1("Res");
            }});
        return v;
    }

}