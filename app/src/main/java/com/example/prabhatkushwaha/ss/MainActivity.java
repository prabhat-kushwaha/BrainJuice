package com.example.prabhatkushwaha.ss;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tooltip.Tooltip;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Properties;

import static android.content.pm.PackageInstaller.*;

public class MainActivity extends AppCompatActivity {
    RelativeLayout rellay1, rellay2;
    TextView forgotlinks;
    ProgressDialog pDialog;
    HashMap<String, String> forgohashmap;
    httpConnection httpConnection1;
    String response1;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String urlbase = "https://still-life-views.000webhostapp.com/login.php";
    String forgourl = "https://still-life-views.000webhostapp.com/forgotpassword.php";
    HashMap<String, String> logdata;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    com.google.android.gms.common.SignInButton gsignInButton;
    //---------------------------------------variable
    EditText etusername, etpassword;
    Button btnsignIn, btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpConnection1 = new httpConnection();
        sharedPreferences = getApplicationContext().getSharedPreferences("userdet", MODE_PRIVATE);
        forgotlinks = findViewById(R.id.forgot);
        //-----------------------------login animation
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
        //----------------------------login animation
        Intent intent1 = new Intent(MainActivity.this, mainWindow.class);

        editor = sharedPreferences.edit();
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
            startActivity(intent1);
        }
        btnSignUp = findViewById(R.id.signup);
        etusername = findViewById(R.id.userName);
        etpassword = findViewById(R.id.password);
        btnsignIn = findViewById(R.id.login);
       /*tooltip demo*/
        etusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip.Builder builder;
                builder = new Tooltip.Builder(view, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(false)
                        .setCornerRadius(15f)
                        .setGravity(Gravity.BOTTOM)
                        .setText("Username contain only a-z or A-Z");
                builder.show();

            }
        });
        etpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip.Builder builder;
                builder = new Tooltip.Builder(view, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(false)
                        .setCornerRadius(10f)
                        .setGravity(Gravity.BOTTOM)
                        .setText("Username contain only a-z or A-Z");
                builder.show();

            }
        });

        /*tooltip demo*/


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sigup = new Intent(MainActivity.this, register.class);
                startActivity(sigup);
            }
        });

        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isempty = false;
                boolean validate = true;
                String lusername = etusername.getText().toString();
                String lpassword = etpassword.getText().toString();
                //---------------------validaation
                if (TextUtils.isEmpty(lusername) == true || TextUtils.isEmpty(lpassword) == true) {
                    isempty = true;
                    showMessage("Error", "some fields are Empty..\nplease fill all fields.");
                } else if (!isPasswordValid(lpassword)) {
                    showMessage("Error", "Please provide right password ");
                    validate = false;
                } else if (!isUserNameValid(lusername)) {
                    showMessage("Error", "Please provide right username");
                    validate = false;
                }
                if (validate && !isempty && isUserNameValid(lusername) == true && isPasswordValid(lpassword) == true) {
                    logdata = new HashMap<String, String>();
                    logdata.put("lusername1", lusername);
                    logdata.put("lpassword1", lpassword);
                    new checkLogin().execute();
                }

            }
        });


        forgotlinks.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this).setTitle("Enter Email ID");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setGravity(View.TEXT_ALIGNMENT_CENTER);
                input.setTextColor(R.color.darkorange);
                ab.setView(input);
                ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if (TextUtils.isEmpty(m_Text)) {
                            showMessage("error", "please enter Some Data email id");
                        }
                        boolean chk = true;
                        if (!isEmailValid(m_Text)) {
                            chk = false;
                            showMessage("error", "please enter valid email id");
                        }
                        if (chk) {
                            String to = m_Text;
                            forgohashmap = new HashMap<>();
                            forgohashmap.put("email", m_Text);
                            new forgotasytask().execute();
                        }


                    }
                });
                ab.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                ab.show();

            }
        });

    }


    public class forgotasytask extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                response1 = httpConnection1.ServerData(forgourl, forgohashmap);
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
            if (result.equals("ok")) {
                showMessage("Thank you :)", " your password send successfuly\nplease check your mail ");
            } else {
                    showMessage("Error",result);

            }

        }

    }



    private boolean isPasswordValid(String password) {
        return password.matches("^[a-zA-Z]{5,12}$");

    }

    private boolean isUserNameValid(String username) {
        return username.matches("^[a-zA-Z]{5,12}$");

    }

    private boolean isEmailValid(String email) {
        return email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");

    }


public class checkLogin extends AsyncTask<Void, Void, String> {
    String ans;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(Void... arg0) {
        try {
            response1 = httpConnection1.ServerData(urlbase, logdata);
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
        if (result.equals("ok")) {
            Toast.makeText(MainActivity.this, "login successfully", Toast.LENGTH_LONG).show();
            Intent i = new Intent(MainActivity.this, mainWindow.class);
            editor.putString("username", logdata.get("lusername1"));
            editor.putString("password", logdata.get("lpassword1"));
            //i.putExtra("loginuser", logdata.get("lusername1"));
            editor.commit();
            //Log.d("username",sharedPreferences.getString("username",null));
            //Toast.makeText(MainActivity.this,sharedPreferences.getString("username",null).toString(),Toast.LENGTH_LONG).show();
            startActivity(i);
            finish();

            //Log.d("logdata",logdata.get("lusername1"));

        } else {
            showMessage("error", "wrong username or password\nSignUp and Try Again");
        }
    }

}


    public void showMessage(String title, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        ab.setCancelable(true).setTitle(title).setMessage(msg).show();

    }


}
