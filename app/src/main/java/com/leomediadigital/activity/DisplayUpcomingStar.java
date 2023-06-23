package com.leomediadigital.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.leomediadigital.adapter.DisplayUpcomingStarAdapter;
import com.leomediadigital.model.UpcomingStarModel;
import com.leomediadigital.util.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayUpcomingStar extends AppCompatActivity {

    TextView stardesc,txtname;
    ImageView imgstr,imgback;
    RecyclerView phorecycler;
    ArrayList<UpcomingStarModel> upcomingStarModelArrayList;
    Context context;
    String star_id,id;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaystar);
        progressDialog = new ProgressDialog(this);
        stardesc = findViewById(R.id.strdesc);
        txtname =findViewById(R.id.name);
        imgstr=findViewById(R.id.strimg);
        imgback =findViewById(R.id.imgback);
        phorecycler= findViewById(R.id.photorecycler);
        phorecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new GridLayoutManager(context,2);
        phorecycler.setLayoutManager(linearLayoutManager);
        upcomingStarModelArrayList= new ArrayList<>();
        String name = (getIntent().getStringExtra("name"));
        String desc = (getIntent().getStringExtra("desc"));
       //description_ID= (getIntent().getStringExtra("descID"));
         id= (getIntent().getStringExtra("id"));
         Log.e("id","imageid2d" + id );



        stardesc.setText(desc);
        txtname.setText(name);
        String imgurl=(getIntent().getStringExtra("image"));
        Picasso.with(getApplicationContext()).load(Constant.IMGPATHUPCOMINSTR+imgurl).into(imgstr);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        imgstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        upcomingStar();
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


    public void upcomingStar(){
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, Constant.UPCOMINSTAR_DSCRIPTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("upcomingStar_", "onResponse: "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code, message = jsonObject.getString("message"), otp_id;
                    code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONArray jsonaray = jsonObject.getJSONArray("media");

                        for (int i = 0; i < jsonaray.length(); i++) {
                            UpcomingStarModel upcomingStarModel = new UpcomingStarModel();
                            JSONObject jsonobj1 = jsonaray.getJSONObject(i);
                            upcomingStarModel.setStarID(jsonobj1.getString("star_id"));
                            upcomingStarModel.setImage(jsonobj1.getString("image"));
                            upcomingStarModelArrayList.add(upcomingStarModel);

                        }
                        progressDialog.dismiss();
                        DisplayUpcomingStarAdapter displayUpcomingStarAdapter = new  DisplayUpcomingStarAdapter(DisplayUpcomingStar.this,upcomingStarModelArrayList);
                        phorecycler.setAdapter(displayUpcomingStarAdapter);

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
//                params.put("shop_sub_category",shopsubcat);
                stringMap.put("star_id",id);
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

}
