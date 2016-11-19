package com.teamproject.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.teamproject.functions.RestController;
import com.teamproject.models.competitionDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 008M on 2016-05-23.
 */
public class CompetitorsList extends Activity {
    private Button buttonWyjdz, buttonFiltruj;
    private EditText plecET, wiekET, kategoriaET, frazaET;
    final Context context = this;
    final competitionDTO competition = CompList.comp;
    String ID_zaw = competition.getID_zawodow();
    ArrayList<String> imieAL = new ArrayList<String>();
    ArrayList<String> nazwiskoAL = new ArrayList<String>();
    ArrayList<String> numerAL = new ArrayList<String>();
    ArrayList<String> wiekAL = new ArrayList<String>();
    ArrayList<String> kategoriaAL = new ArrayList<String>();
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    int row;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competitors_list);
        buttonWyjdz = (Button) findViewById(R.id.buttonAlert);
        buttonFiltruj = (Button) findViewById(R.id.button1);
        plecET = (EditText) findViewById(R.id.editText1);
        wiekET = (EditText) findViewById(R.id.editText2);
        kategoriaET = (EditText) findViewById(R.id.editText3);
        frazaET = (EditText) findViewById(R.id.editText4);
        String url = "http://192.168.0.2:8080/Rest/rest/competition/event/list?" +
                "competition_id="+ID_zaw+"&sex=&age=&phrase=&category=";
        sendHttpRequest(url, "GET");
        buttonWyjdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CompetitorsList.this.finish();
            }
        });
        buttonFiltruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String plec = String.valueOf(spinner.getSelectedItem());
                String wiek = wiekET.getText().toString();
                String kat = kategoriaET.getText().toString();
                String fraza = frazaET.getText().toString();
                String url = "http://192.168.0.2:8080/Rest/rest/competition/event/list?" +
                        "competition_id="+ID_zaw+"&sex="+plec+"&age="+wiek+"&phrase="+fraza+"&category="+kat;
                sendHttpRequest(url, "GET");
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner1);
        adapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
    }
    public void sendHttpRequest(String url, final String operation){
        RestController rc = new RestController(this){
            @Override
            public void onResponseReceived(String result){
                    try {
                        parsingJSON(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        };
        rc.setAddress(url);
        rc.setOperation(operation);
        rc.setShowPD(true);
        rc.execute();
    }
    public void parsingJSON(String JSON) throws JSONException {
        imieAL.clear();
        nazwiskoAL.clear();
        kategoriaAL.clear();
        numerAL.clear();
        wiekAL.clear();
        int i;
        JSONArray jsonarray = new JSONArray(JSON);
        for (i = 0; i < jsonarray.length(); i++) {
            JSONObject obj = jsonarray.getJSONObject(i);
            imieAL.add(obj.getString("IMIE"));
            nazwiskoAL.add(obj.getString("NAZWISKO"));
            kategoriaAL.add(obj.getString("CATEGORY"));
            if(obj.toString().contains("EVENT_NR"))
            numerAL.add(obj.getString("EVENT_NR"));
            else numerAL.add("    ");
            wiekAL.add(obj.getString("WIEK"));
        }
        pupulateButtons(i, imieAL, nazwiskoAL, kategoriaAL, numerAL, wiekAL);
       // if (i>0) Toast.makeText(CompetitorsList.this, i + " zawodników", Toast.LENGTH_SHORT).show();
    }

    public void pupulateButtons(int i, ArrayList<String> imie, ArrayList<String> nazwisko, ArrayList<String> kategoria,
                                 ArrayList<String> numer, ArrayList<String> wiek) {
        TableLayout table = (TableLayout) findViewById(R.id.tableButtons);
        table.removeAllViews();
        for (row = 0; row < i; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            table.addView(tableRow);
            Button button = new Button(this);
            button.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            button.setCompoundDrawablePadding(30);
            if (row % 2 == 0){
                button.setBackground(getResources().getDrawable(R.drawable.rounded_border_competitorslist));
        }
            else             button.setBackground(getResources().getDrawable(R.drawable.rounded_border_competitorslist1));
            button.setText("\n  " +numer.get(row) + "   " + imie.get(row) + " " + nazwisko.get(row) + "   " + kategoria.get(row) + "  " + wiek.get(row)+"\n");
            button.setTextSize(19);
            tableRow.addView(button);
        }
    }

}