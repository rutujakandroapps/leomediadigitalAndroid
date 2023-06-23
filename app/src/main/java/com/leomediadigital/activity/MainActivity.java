package com.leomediadigital.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leomediadigital.R;
import com.leomediadigital.model.Film_personalityModel;
import com.leomediadigital.util.Constant;
import com.leomediadigital.util.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lnrOurMediaPartner;
    LinearLayout lnrArticles;
    LinearLayout lnrFilmsPersonality;
    LinearLayout lnrContacts;
    LinearLayout lnrPhotoVideo;
    ProgressDialog progressDialog ;
    LinearLayout lnrUpcomingStar;
    String media = "" ;
    LinearLayout lnrlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressDialog = new ProgressDialog(this);
        lnrOurMediaPartner = findViewById(R.id.lnrOurMediaPartner);
        lnrFilmsPersonality = findViewById(R.id.lnrFilmsPersonality);
        lnrContacts = findViewById(R.id.inrtitle);
        lnrPhotoVideo = findViewById(R.id.lnrPhotoVideo);
        lnrUpcomingStar = findViewById(R.id.lnrUpcomingStar);
        lnrlogout = findViewById(R.id.lnrlogout);

        lnrOurMediaPartner.setOnClickListener(this);
        lnrFilmsPersonality.setOnClickListener(this);
        lnrContacts.setOnClickListener(this);
        lnrPhotoVideo.setOnClickListener(this);
        lnrUpcomingStar.setOnClickListener(this);
        lnrlogout.setOnClickListener(this);

        getFilmsPersonalities();

    }

    public void getFilmsPersonalities(){
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, Constant.FILMS_PERSONALITIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getFilmsPersonalities", "onResponse: "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code, message = jsonObject.getString("message"), otp_id;
                    code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONArray jsonaray = jsonObject.getJSONArray("Film_personality");
                        for (int i = 0; i < jsonaray.length(); i++) {

                            Film_personalityModel film_personalityModel = new Film_personalityModel();
                            JSONObject jsonobj = jsonaray.getJSONObject(i);
//
                            media=jsonobj.getString("media");
                        }

                        progressDialog.dismiss();
//
                    }else {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    progressDialog.cancel();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please Connect to the Internet And Try Again..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<String, String>();
                return stringMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> paramsHeader = new HashMap<>();
                String credentials = Constant.USERNAME + ":" + Constant.PASSWORD;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                paramsHeader.put("Authorization", auth);
                paramsHeader.put("token", Constant.TOKEN);

                return paramsHeader;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 50000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringrequest1.setRetryPolicy(policy);
        requestQueue.add(stringrequest1);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.lnrOurMediaPartner:
                Intent mediaIntent = new Intent(MainActivity.this,OurMediaPartenerActivity.class);
                startActivity(mediaIntent);
                break;
            case R.id.lnrFilmsPersonality:
//                Intent interviewsIntent = new Intent(MainActivity.this,FilmsPersonalitiesActivity.class);
//                startActivity(interviewsIntent);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.IMGPATH_FILMPERSONALITIES+ media));
                startActivity(browserIntent);

                break;
            case R.id.inrtitle:
                Intent contactIntet = new Intent(MainActivity.this, TitleActivity.class);
                startActivity(contactIntet);
                break;
            case R.id.lnrPhotoVideo:
                Intent photoVideoIntent = new Intent(MainActivity.this,PhotovideoFolder.class);
                startActivity(photoVideoIntent);
                break;
            case R.id.lnrUpcomingStar:
                Intent upcomingStarIntent = new Intent(MainActivity.this,UpcominStarActivity.class);
                startActivity(upcomingStarIntent);
                break;
            case R.id.lnrlogout:
                AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
                alertdialog.setTitle("Log Out");
                alertdialog.setMessage("Do you want to Log Out?");
                alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPref.setLogin(MainActivity.this,false);
                        Intent logOutIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logOutIntent);
                        finish();
                    }
                });

                alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert=alertdialog.create();
                alertdialog.show();
                break;

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
