package com.teamproject.route;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.teamproject.activity.R;

public class TrackPOI extends Fragment {
    Context context;
    private Button button1, button2;
    private EditText nazwaET;
    OnHeadlineSelectedListener mCallback;

    public interface OnHeadlineSelectedListener {
        public void operation2(String i);
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
        View v = inflater.inflate(R.layout.trackpoi_tab, container, false);
        button1 = (Button) v.findViewById(R.id.Button1);
        button2 = (Button) v.findViewById(R.id.Button2);
        nazwaET = (EditText) v.findViewById(R.id.editText1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String nazwa = nazwaET.getText().toString();
                if (nazwa.length()==0)
                    mCallback.operation2(nazwa);
                else
                mCallback.operation2(nazwa);
            }});
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.operation2("ZapPOI");
            }});

        return v;
    }

}