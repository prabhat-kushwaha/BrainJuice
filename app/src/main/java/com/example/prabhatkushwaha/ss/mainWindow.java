package com.example.prabhatkushwaha.ss;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class mainWindow extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Button chem, bio, logic, math;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;
    String url2 = "https://still-life-views.000webhostapp.com/startQuiz.php";
    HashMap<String, String> hashMap;
    String qtype;
    static int attemp = 1;
    View view;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

//===========
        sharedPreferences = getApplicationContext().getSharedPreferences("userdet", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//===========
        Toolbar toolbar = findViewById(R.id.demotoolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.draw_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //Log.d("prefrence value",sharedPreferences.getString("username",""));
        final Bundle extras = getIntent().getExtras();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.startQuiz:
                        Toast.makeText(mainWindow.this, "already in start Quiz page", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.profile:
                        //Toast.makeText(mainWindow.this, "profile1", Toast.LENGTH_LONG).show();
                        Intent i1 = new Intent(mainWindow.this, profile1.class);
                        i1.putExtra("lu_name", sharedPreferences.getString("username", null).toString());
                        //i1.putExtra("lu_name",sharedPreferences.getString("username", ""));

                        startActivity(i1);
                        break;
                    case R.id.feedback:
                        Intent i3 = new Intent(mainWindow.this, feedback.class);
                        i3.putExtra("username", sharedPreferences.getString("username", null).toString());
                        startActivity(i3);
                        break;
                    case R.id.setting:
                        //Toast.makeText(mainWindow.this, "Setting", Toast.LENGTH_LONG).show();
                        Intent i4 = new Intent(mainWindow.this, setting.class);
                        i4.putExtra("username", sharedPreferences.getString("username", null).toString());
                        startActivity(i4);

                        break;
                    case R.id.Exit:
                        Toast.makeText(mainWindow.this, "exit", Toast.LENGTH_LONG).show();
                        editor.clear();
                        editor.commit();
                        Intent i5 = new Intent(mainWindow.this, MainActivity.class);
                        startActivity(i5);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ///---------------------------------- start quiz
        chem = findViewById(R.id.StartChemQuiz);
        bio = findViewById(R.id.StartBioQuiz);
        math = findViewById(R.id.StartMathQuiz);
        logic = findViewById(R.id.StartLogicalQuiz);


        //$q_type=$_POST["qtype"];
        //$attempt=$_POST["attempt"];


        chem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String q_type1;
                qtype = "chemistryQuiz";
                q_type1 = "chem";
                hashMap = new HashMap<>();
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                hashMap.put("qtype", q_type1);
                String etxt = sharedPreferences.getString("username", null).toString();
                hashMap.put("username", etxt);
                hashMap.put("cdate", date);

                new StartQuizASY().execute();
            }
        });

        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q_type2;
                qtype = "biologyQuiz";
                q_type2 = "bio";
                hashMap = new HashMap<>();
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                hashMap.put("qtype", q_type2);
                String etxt = sharedPreferences.getString("username", null).toString();
                hashMap.put("username", etxt);
                hashMap.put("cdate", date);

                new StartQuizASY().execute();


            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q_type3;
                qtype = "mathsQuiz";
                q_type3 = "math";

                //String sp=sharedPreferences.getString("username",null);
                //Log.d("username",sp);
                hashMap = new HashMap<>();
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                hashMap.put("qtype", q_type3);
                String etxt = sharedPreferences.getString("username", null).toString();
                hashMap.put("username", etxt);
                hashMap.put("cdate", date);

                new StartQuizASY().execute();
            }
        });
        logic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q_type4;
                qtype = "logicalQuiz";
                q_type4 = "logical";
                hashMap = new HashMap<>();
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                hashMap.put("qtype", q_type4);
                String etxt = sharedPreferences.getString("username", null).toString();
                hashMap.put("username", etxt);
                hashMap.put("cdate", date);
                new StartQuizASY().execute();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public class StartQuizASY extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mainWindow.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                // ans = GetUserProfileDetails.ServerData(url2,hashMap);
                httpConnection obj = new httpConnection();
                ans = obj.ServerData(url2, hashMap);
                //ans.replaceAll("\\" , "" );
                Log.d("response", ans);


            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return ans;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                Iterator<String> keyset = jsonObject.keys();
                Map<String, Map<String, String>> hashMap = new HashMap<>();

                while (keyset.hasNext()) {
                    String temp = keyset.next();
                    JSONObject jb1 = new JSONObject((String) jsonObject.get(temp));
                    Map<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("ans", (String) jb1.get("ans"));
                    hashMap1.put("opt1", (String) jb1.get("opt1"));
                    hashMap1.put("opt2", (String) jb1.get("opt2"));
                    hashMap1.put("opt3", (String) jb1.get("opt3"));
                    hashMap1.put("opt4", (String) jb1.get("opt4"));

                    hashMap.put(temp, hashMap1);
                }

                if (qtype.equals("chemistryQuiz")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hashmap", (Serializable) hashMap);
                    Intent i = new Intent(mainWindow.this, Quiz_main_window.class);
                    i.putExtra("quiztype", "chemistryQuiz");
                    i.putExtras(bundle);
                    startActivity(i);

                }
                if (qtype.equals("mathsQuiz")) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("hashmap", (Serializable) hashMap);
                    Intent i2 = new Intent(mainWindow.this, Quiz_main_window.class);
                    i2.putExtra("quiztype", "mathsQuiz");
                    i2.putExtras(bundle1);
                    //  new getset().finalhashMap= hashMap;
                    startActivity(i2);


                }
                if (qtype.equals("biologyQuiz")) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("hashmap", (Serializable) hashMap);
                    Intent i = new Intent(mainWindow.this, Quiz_main_window.class);
                    i.putExtra("quiztype", "biologyQuiz");
                    i.putExtras(bundle2);
                    startActivity(i);

                }

                if (qtype.equals("logicalQuiz")) {

                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("hashmap", (Serializable) hashMap);
                    Intent i = new Intent(mainWindow.this, Quiz_main_window.class);
                    i.putExtra("quiztype", "logicalQuiz");
                    i.putExtras(bundle3);
                    startActivity(i);
                }

            } catch (Exception e) {
                //JSONException e) {
                e.printStackTrace();
            }

        }

    }


}

