package com.leomediadigital.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.leomediadigital.adapter.OurMediaPartenerAdapter;
import com.leomediadigital.model.MediaPartner;
import com.leomediadigital.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OurMediaPartenerActivity extends AppCompatActivity {

    RecyclerView recyclerMediaPartner;
    ProgressDialog progressDialog;
    ArrayList<MediaPartner> mediaPartnerArrayList;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_media_partener);
        progressDialog = new ProgressDialog(this);
        imgback =findViewById(R.id.imgback);
        progressDialog.setMessage("Loading...");
        recyclerMediaPartner = findViewById(R.id.recyclerMediaPartner);
        recyclerMediaPartner.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        recyclerMediaPartner.setLayoutManager(linearLayoutManager);
        mediaPartnerArrayList = new ArrayList<>();
       getMediaPartnerData();


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }
    public void getMediaPartnerData(){
        progressDialog.show();
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, Constant.MEDIA_PARTNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getMediaPartnerData", "onResponse: "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code, message = jsonObject.getString("message"), otp_id;
                    code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONArray jsonaray = jsonObject.getJSONArray("media");
                        for (int i = 0; i < jsonaray.length(); i++) {

                           MediaPartner mediaPartner = new MediaPartner();
                            JSONObject jsonobj = jsonaray.getJSONObject(i);
                            mediaPartner.setId(jsonobj.getString("id"));
                            mediaPartner.setType(jsonobj.getString("type"));
                            mediaPartner.setDescription(jsonobj.getString("description"));
                            mediaPartner.setPhotos(jsonobj.getString("photos"));
                            mediaPartner.setMedia(jsonobj.getString("media"));
                            mediaPartner.setFileType(jsonobj.getString("filetype"));
                            mediaPartnerArrayList.add(mediaPartner);
                        }

                        progressDialog.dismiss();
                        OurMediaPartenerAdapter ourMediaPartenerAdapter = new OurMediaPartenerAdapter(OurMediaPartenerActivity.this,mediaPartnerArrayList);
                        recyclerMediaPartner.setAdapter(ourMediaPartenerAdapter);

//                    } else if (code.equals("10")) {
//                        progressDialog.cancel();
//                    } else if (code.equals("102")) {
//                        progressDialog.cancel();
////                        FunctionConstants.logout(getApplicationContext());
//                    } else if (code.equals("101")) {
//                        progressDialog.cancel();
//                    } else if (code.equals("5")) {
//                        progressDialog.cancel();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
