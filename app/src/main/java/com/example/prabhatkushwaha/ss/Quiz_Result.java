package com.example.prabhatkushwaha.ss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Quiz_Result extends AppCompatActivity {
    String updateurl = "https://still-life-views.000webhostapp.com/updatetest.php";
    TextView textresult;
    ProgressDialog pDialog;
    String response1;
    HashMap<String, String> updateHashmap;
    ListView listViewqna, rlistview;
    ArrayList<String> arrayListshowdata;
    ArrayList<String> arrayListshowdatarightans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__result);
        textresult = findViewById(R.id.result);
        VideoView v = findViewById(R.id.vv);

        Bundle b = getIntent().getExtras();
        updateHashmap = new HashMap<>();
        String m = b.get("score").toString();
        updateHashmap.put("score", m);

        new updatetest().execute();

        int i = Integer.parseInt(b.get("score").toString());
        listViewqna = findViewById(R.id.userans);
        listViewqna.setDivider(null);
        listViewqna.setDividerHeight(0);

        rlistview = findViewById(R.id.actans);
        rlistview.setDivider(null);
        rlistview.setDividerHeight(0);

        Map<String, String> map = (Map<String, String>) b.get("ansMap");
        arrayListshowdata = new ArrayList<String>();

        arrayListshowdatarightans = new ArrayList<>();
        //Log.d("keyset", String.valueOf(map.keySet()));

        Set<String> keys = map.keySet();
        for (String key : keys) {
            String t = "Q." + key + "\nAns." + map.get(key);
            arrayListshowdata.add(t);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(Quiz_Result.this, R.layout.listviewitem, arrayListshowdata);
        listViewqna.setAdapter(arrayAdapter);
//-------------------------------------------------
        Map<String, String> mapr = (Map<String, String>) b.get("rightMap");

        Set<String> keys1 = mapr.keySet();
        for (String key : keys1) {
            String t1 = "Q." + key + "\nAns." + mapr.get(key);
            arrayListshowdatarightans.add(t1);
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter<String>(Quiz_Result.this, R.layout.listviewitem, arrayListshowdatarightans);
        rlistview.setAdapter(arrayAdapter1);


        if (i > 7) {
            v.setVisibility(View.VISIBLE);
            String UrlPath = "android.resource://" + getPackageName() + "/" + R.raw.newgif;
            v.setVideoURI(Uri.parse(UrlPath));
            textresult.setText(b.get("score").toString() + "/10 \n Good score :) ");
            v.start();
        } else {
            textresult.setText(b.get("score").toString() + "/10 \n Not Good score :( ");
        }
    }

    public class updatetest extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                httpConnection hc = new httpConnection();
                response1 = hc.ServerData(updateurl, updateHashmap);
                Log.i("res1", response1);
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return response1.replace(" ", "");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("ok")) {
                Toast.makeText(Quiz_Result.this, "result stored", Toast.LENGTH_LONG).show();
                //Log.d("logdata",logdata.get("lusername1"));

            } else {
                showMessage("error", "wrong username or password\nSignUp and Try Again");
            }
        }

    }

    public void showMessage(String title, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(Quiz_Result.this);
        ab.setCancelable(true).setTitle(title).setMessage(msg).show();
    }


}
