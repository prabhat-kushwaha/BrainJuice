package com.example.prabhatkushwaha.ss;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class profile1 extends AppCompatActivity {
    ImageView userdp, barcodeimg;
    TextView txt_username, txt_Email, txt_userid;
    String user_id, user_name, user_email;
    int IMAGE_PICKER = 1;
    String url2 = "https://still-life-views.000webhostapp.com/profile.php";
    ProgressDialog pDialog;
    HashMap<String, String> hashMap;

    Bitmap bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userdp = findViewById(R.id.imageView1);
        barcodeimg = findViewById(R.id.barcode);
        txt_userid = findViewById(R.id.user_id1);
        txt_username = findViewById(R.id.user_name1);
        txt_Email = findViewById(R.id.user_email1);


        Bundle b = getIntent().getExtras();
        String usnm = (String) b.get("lu_name");
        Log.d("usnm", usnm);
        hashMap = new HashMap<>();
        hashMap.put("username", usnm);

        new PostDataTOServerprofile().execute();

        userdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER);
            }
        });

        barcodeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdilog = new AlertDialog.Builder(profile1.this);
                alertdilog.setCancelable(true);
                alertdilog.setTitle("Save Image");
                alertdilog.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        storeImage(bitmap1);
                        Toast.makeText(profile1.this,"image save successfully :)",Toast.LENGTH_LONG).show();
                    }
                });
                alertdilog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertdilog.show();
            }
        });


    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("error",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("error", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("error", "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    public class PostDataTOServerprofile extends AsyncTask<Void, Void, String> {
        String ans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(profile1.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                httpConnection obj = new httpConnection();
                ans = obj.ServerData(url2, hashMap);
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
                String name = (String) jsonObject.get("name");
                String id = (String) jsonObject.get("id");
                String email = (String) jsonObject.get("email");
                String password=(String) jsonObject.get("password");
                txt_userid.setText(id);
                txt_username.setText(name);
                txt_Email.setText(email);
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    String text = "Username :" + name + "\n ID :" + id + "\n emailID :" + email+"\n password :"+password;
                    Log.d("barcode text",text);
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    bitmap1 = bitmap;
                    barcodeimg.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER && resultCode == RESULT_OK && null != data) {
            try {
                final Uri uriImage = data.getData();
                final InputStream inputStream = getContentResolver().openInputStream(uriImage);
                final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                userdp.setImageBitmap(imageMap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
