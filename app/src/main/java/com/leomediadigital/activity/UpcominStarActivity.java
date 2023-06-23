package com.leomediadigital.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.leomediadigital.adapter.UpcominStarAdapter;
import com.leomediadigital.model.UpcomingStarModel;
import com.leomediadigital.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpcominStarActivity extends AppCompatActivity {


    ArrayList<UpcomingStarModel> upcomingStarModelArrayList;
    Context context;
    RecyclerView recyclerViewupcoming;
    ProgressDialog progressDialog;
    ImageView imgback;
    ArrayList<String> namelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomin_star);
         namelist=new ArrayList<>();
        progressDialog= new ProgressDialog(this);
        imgback =findViewById(R.id.imgback);
        recyclerViewupcoming= findViewById(R.id.recyclerupcominstar);
        recyclerViewupcoming.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewupcoming.setLayoutManager(linearLayoutManager);
        upcomingStarModelArrayList= new ArrayList<>();
        upcomingStar();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }

    public void upcomingStar(){
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, Constant.UPCOMINSTAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("upcomingStar", "onResponse: "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code, message = jsonObject.getString("message"), otp_id;
                    code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONArray jsonaray = jsonObject.getJSONArray("media");

                        for (int i = 0; i < jsonaray.length(); i++) {

//                            JSONObject jsonobj1 = jsonaray.getJSONObject(i);
//
//                            JSONArray jsonaray1 = jsonobj1 .getJSONArray("getPhoto");
//
//                            JSONArray jsonaray2 = jsonobj1 .getJSONArray("Stars");

//                            for (int j=0;j<jsonaray1.length();j++){
//                                UpcomingStarModel upcomingStarModel = new UpcomingStarModel();
//                                JSONObject jsonobj2 = jsonaray1.getJSONObject(j);
//                                upcomingStarModel.setId(jsonobj2.getString("id"));
//                                upcomingStarModel.setImage(jsonobj2.getString("images"));
//                                upcomingStarModelArrayList.add(upcomingStarModel);
//
//                            }
//
//                            for (int k=0;k<jsonaray.length();k++)
                            {
                                UpcomingStarModel upcomingStarModel = new UpcomingStarModel();
                                JSONObject jsonobj3 = jsonaray.getJSONObject(i);
                                upcomingStarModel.setId(jsonobj3.getString("id"));
                                upcomingStarModel.setDescription(jsonobj3.getString("description"));
                                upcomingStarModel.setFile_name(jsonobj3.getString("file_name"));
                                namelist.add(jsonobj3.getString("name"));
                                upcomingStarModel.setName(jsonobj3.getString("name"));
                               upcomingStarModelArrayList.add(upcomingStarModel);

                            }
                        }

                        progressDialog.dismiss();
                        UpcominStarAdapter upcominStarAdapter = new UpcominStarAdapter(UpcominStarActivity.this,upcomingStarModelArrayList,namelist);
                        recyclerViewupcoming.setAdapter(upcominStarAdapter);

                    }

                    else {
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
