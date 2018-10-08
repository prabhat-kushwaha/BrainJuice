package com.example.prabhatkushwaha.ss;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tooltip.Tooltip;

import java.util.HashMap;

public class register extends AppCompatActivity {
    EditText rrusername, rrpassword, rremail;
    Button btnposttoserver;
    HashMap<String, String> regdata;
    String response1;
    String op;
    ProgressDialog pDialog;
    String url1 = "https://still-life-views.000webhostapp.com/register.php";
    httpConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rremail = findViewById(R.id.Email);
        connection = new httpConnection();
        rrpassword = findViewById(R.id.rpassword);
        rrusername = findViewById(R.id.rusername);
        btnposttoserver = findViewById(R.id.post_register_btn);
//variable


        rremail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Tooltip.Builder builder;
                builder = new Tooltip.Builder(view, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(false)
                        .setCornerRadius(15f)
                        .setGravity(Gravity.BOTTOM)
                        .setText("Enter valid email id ex. abc @ domain name.com");
                builder.show();
            }
        });

        rrpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip.Builder builder;
                builder = new Tooltip.Builder(view, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(false)
                        .setCornerRadius(15f)
                        .setGravity(Gravity.BOTTOM)
                        .setText("Password contain only a-z or A-Z and the length of password is between 5-20 char ex. Prabhat");
                builder.show();
            }
        });

        rrusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tooltip.Builder builder;
                builder = new Tooltip.Builder(view, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(false)
                        .setCornerRadius(15f)
                        .setGravity(Gravity.BOTTOM)
                        .setText("Username contain only a-z or A-Z and the length of username is between 5-20 char ex. PrabhatKushwaha");
                builder.show();
            }
        });
        btnposttoserver.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {

                boolean isempty=false;
                String rusername1 = rrusername.getText().toString();
                String rpassword1 = rrpassword.getText().toString();
                String remail1 = rremail.getText().toString();
                boolean validate=true;

                if (TextUtils.isEmpty(rusername1) == true || TextUtils.isEmpty(rpassword1) == true || TextUtils.isEmpty(remail1) == true) {
                    isempty=true;
                    showMessage("Error", "some fields are Empty..\nplease fill all fields.");
                }
                else if(!isEmailValid(remail1)){
                    showMessage("Error", "Please provide right emailID ");
                    validate=false;
                }
                else if(!isPasswordValid(rpassword1)){
                    showMessage("Error", "Please provide right password ");
                    validate=false;
                }
                else if(!isUserNameValid(rusername1)){
                    showMessage("Error", "Please provide right username");
                    validate=false;
                }
                if(validate && !isempty && isEmailValid(remail1)==true && isUserNameValid(rusername1)==true && isPasswordValid(rpassword1)==true){
                    regdata = new HashMap<String, String>();
                    regdata.put("rusername", rusername1);
                    regdata.put("rpassword", rpassword1);
                    regdata.put("remail", remail1);

                        new PostDataTOServer().execute();

                }

            }
        });


    }

    public class PostDataTOServer extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(register.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                response1 = connection.ServerData(url1, regdata);
                Log.d("response", response1);

            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return response1;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (result.equals(" ok")) {
                // Toast.makeText(activity_register.this, "data" + result, Toast.LENGTH_LONG).show();
                showMessage("data inserted", "yes");
            } else {
                showMessage("data not inserted", result);
                //Toast.makeText(activity_register.this, "already exist" + result, Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean isEmailValid(String email){
        return email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");

    }

    private boolean isPasswordValid(String password){
        return password.matches("^[a-zA-Z]{5,20}$");

    }
    private boolean isUserNameValid(String username){
        return username.matches("^[a-zA-Z]{5,20}$");

    }



    public void showMessage(String title, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(register.this);
        ab.setCancelable(true).setTitle(title).setMessage(msg).show();

    }

}
