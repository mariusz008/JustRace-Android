package com.teamproject.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
 * Created by 008M on 2016-05-24.
 */
public class ResultsList extends Activity {
    private Button buttonWyjdz;
    private TextView tv1;
    private LinearLayout linearLayout;
    final Context context = this;
    final competitionDTO competition = CompList.comp;
    String ID_zaw = competition.getID_zawodow();
    ArrayList<String> imieAL = new ArrayList<String>();
    ArrayList<String> nazwiskoAL = new ArrayList<String>();
    ArrayList<String> numerAL = new ArrayList<String>();
    ArrayList<String> wiekAL = new ArrayList<String>();
    ArrayList<String> kategoriaAL = new ArrayList<String>();
    ArrayList<String> czas = new ArrayList<String>();
    int row, ileP;
    String ilePKT;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_list);
        buttonWyjdz = (Button) findViewById(R.id.buttonAlert);
        String url = "http://209785serwer.iiar.pwr.edu.pl/Rest/rest/result/list?" +
                "competition_id=" + ID_zaw;
        sendHttpRequest(url, "GET");
        buttonWyjdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ResultsList.this.finish();
            }
        });
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
        czas.clear();
        int i;
        ileP = 0;
        JSONArray jsonarray = new JSONArray(JSON);
        JSONObject obj1 = jsonarray.getJSONObject(0);
        ilePKT = obj1.getString("POINTS_COUNT");
        ileP = Integer.parseInt(ilePKT);
        for (i = 1; i < jsonarray.length(); i++) {
            JSONObject obj = jsonarray.getJSONObject(i);
            imieAL.add(obj.getString("IMIE"));
            nazwiskoAL.add(obj.getString("NAZWISKO"));
            kategoriaAL.add(obj.getString("CATEGORY"));
            if(obj.toString().contains("EVENT_NR"))
                numerAL.add(obj.getString("EVENT_NR"));
            else numerAL.add("    ");
            wiekAL.add(obj.getString("WIEK"));
            for (int j = 1; j <= ileP; j++){
                if(obj.toString().contains("POINT"))
                czas.add(obj.getString("POINT"+j+"_TIME"));
                else czas.add("");
            }
        }
        pupulateButtons(i-1, imieAL, nazwiskoAL, kategoriaAL, numerAL, wiekAL, czas);
    }
    public void pupulateButtons(int i, ArrayList<String> imie, ArrayList<String> nazwisko, ArrayList<String> kategoria,
                                ArrayList<String> numer, ArrayList<String> wiek, ArrayList<String> czas) {
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
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
            ));
            button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            button.setTextColor(getApplication().getResources().getColor(R.color.navyblue));
            button.setCompoundDrawablePadding(30);
            if (row % 2 == 0){
                button.setBackground(getResources().getDrawable(R.drawable.rounded_border_competitorslist));
                button.setTextColor(getResources().getColor(R.color.white));
            }
            else             button.setBackground(getResources().getDrawable(R.drawable.rounded_border_competitorslist1));
            button.setText("   " + numer.get(row) + "   " + imie.get(row) + " " + nazwisko.get(row) + "   " + kategoria.get(row) + "  " + wiek.get(row) + "\n");
            for(int h=ileP*row;h<(ileP*row+ileP);h++){
                button.append(czas.get(h)+"    ");
            }
            tableRow.addView(button);
        }
    }
}
