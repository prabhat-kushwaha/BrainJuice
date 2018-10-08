package com.example.prabhatkushwaha.ss;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class feedback extends AppCompatActivity {
    EditText feeddata1;
    Button btn_feedback_submit;
    static ArrayList<String> arrayList1;
    HashMap<String,String> hashMap;
    ProgressDialog pDialog;
    String url3="https://still-life-views.000webhostapp.com/feedback.php";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayList1 = new ArrayList<>();
        setContentView(R.layout.activity_feedback);
        btn_feedback_submit = findViewById(R.id.submit_feedback);
        //btn_feedback_delete = findViewById(R.id.delete_feedback);

        feeddata1 = findViewById(R.id.feeddata);


        final ListView listView1 = findViewById(R.id.listview);
        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(feedback.this, R.layout.listviewitem, arrayList1);

        btn_feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = feeddata1.getText().toString();
                //String date = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                //Date d = new Date();
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());
                boolean isdata=true;
                hashMap=new HashMap<>();
                Bundle b=getIntent().getExtras();
                hashMap.put("username", (String) b.get("username"));
                hashMap.put("feedback",feedback);
                hashMap.put("date",date);
                if(TextUtils.isEmpty(feedback)){
                    showMessage("Error","please provide some content ");
                    isdata=false;
                }
                //String str = "Feedback:" + feedback + "\nDate:" + date;
                //arrayList1.add(str);
                feeddata1.setText("");
                if(isdata)
                    new PostDataTOServerFeedback().execute();

                //arrayAdapter.notifyDataSetChanged();
            }

        });
        //listView1.setAdapter(arrayAdapter);
/*
*/
    }

    public class PostDataTOServerFeedback extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(feedback.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                httpConnection obj = new httpConnection();
                ans = obj.ServerData(url3, hashMap);
                Log.d("response", ans);

            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return ans.replace(" ", "");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (result.equals("ok")) {
               showMessage("Thanks :)","your feedback save succsessfully");
            }
            else{
                showMessage("Error",result);
            }
        }

    }



    public void showMessage(String title, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(feedback.this);
        ab.setCancelable(true).setTitle(title).setMessage(msg).show();

    }
}
 