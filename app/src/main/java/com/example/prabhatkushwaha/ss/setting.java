package com.example.prabhatkushwaha.ss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class setting extends AppCompatActivity {
    ProgressDialog pDialog;
    String response1;
    HashMap<String, String> histmap;
    String histurl = "https://still-life-views.000webhostapp.com/history.php";
    ListView listViewhist;
    android.support.v7.widget.CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button b = findViewById(R.id.share);
        Button historybtn = findViewById(R.id.history);
        listViewhist = findViewById(R.id.listhist);
        listViewhist.setDividerHeight(0);
        listViewhist.setDivider(null);
        cardView = findViewById(R.id.card1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationInfo app = getApplicationContext().getApplicationInfo();
                String filePath = app.sourceDir;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                File originalApk = new File(filePath);
                try {
                    File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
                    if (!tempFile.isDirectory())
                        if (!tempFile.mkdirs())
                            return;
                    tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ", "").toLowerCase() + ".apk");
                    if (!tempFile.exists()) {
                        if (!tempFile.createNewFile()) {
                            return;
                        }
                    }
                    InputStream in = new FileInputStream(originalApk);
                    OutputStream out = new FileOutputStream(tempFile);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    System.out.println("File copied.");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                    startActivity(Intent.createChooser(intent, "Share app via"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });

        historybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.VISIBLE);
                listViewhist.setVisibility(View.VISIBLE);
                histmap = new HashMap<>();
                Bundle b = getIntent().getExtras();
                String usnm = b.get("username").toString();
                histmap.put("username", usnm);
                new gethist().execute();
            }
        });

    }

    public class gethist extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(setting.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                httpConnection hc = new httpConnection();
                response1 = hc.ServerData(histurl, histmap);
                Log.i("res1", response1);
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return response1.replace(" ", "");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Map<String, Map<String, String>> hashMap;
            JSONObject jsonObject = null;
            try {
                hashMap = new HashMap<>();
                jsonObject = new JSONObject(result);
                Iterator<String> keyset = jsonObject.keys();
                while (keyset.hasNext()) {
                    //String temp = keyset.next();
                    //hashMap.put(keyset.next().toString(), (String) jsonObject.get(keyset.next()));
                    String temp = keyset.next();
                    JSONObject jb1 = new JSONObject((String) jsonObject.get(temp));
                    Map<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("tdate", (String) jb1.get("testdate"));
                    hashMap1.put("tscore", (String) jb1.get("score"));
                    hashMap.put(temp, hashMap1);

                }
                Set<String> keyset1 = hashMap.keySet();
                ArrayList<String> arrayList = new ArrayList<>();
                for (String key : keyset1) {
                    String t1 = "Test ID: " + key + "\n Test Date:" + hashMap.get(key).get("tdate") + "\n Test Score" + hashMap.get(key).get("tscore");
                    arrayList.add(t1);
                }
                ArrayAdapter arrayAdapter1 = new ArrayAdapter<String>(setting.this, R.layout.listviewitem, arrayList);
                listViewhist.setAdapter(arrayAdapter1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
