package com.aptiquiz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
//import com.aptiquiz.R;
import com.aptiquiz.adapter.PhotoVideoAdapter;
import com.aptiquiz.model.PhotoVideoModel;
import com.aptiquiz.util.Constant;
import com.leomediadigital.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhotoVideoActivity extends AppCompatActivity {

    ArrayList<PhotoVideoModel> photoVideoModelArrayList;
    Context context;
    RecyclerView recyclerViewphoto;
    ProgressDialog progressDialog;
    String descid;
    public static String videolink;
    ImageView imgback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        descid = getIntent().getStringExtra("descID");
        setContentView(R.layout.activity_photo_video);
        progressDialog= new ProgressDialog(this);
        recyclerViewphoto = findViewById(R.id.recyclerphoto);
        imgback =findViewById(R.id.imgback);
        recyclerViewphoto.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context,2);
        recyclerViewphoto.setLayoutManager(linearLayoutManager);
        photoVideoModelArrayList= new ArrayList<>();
        getPhotovideo();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }

    public  void getPhotovideo(){
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, Constant.PHOTOLiSTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code, type="",message = jsonObject.getString("message"), otp_id;
                    code = jsonObject.getString("code");

                    if (code.equals("200")) {

                        JSONArray jsonaray = jsonObject.getJSONArray("photos");

                        for (int i = 0; i < jsonaray.length(); i++) {
                            PhotoVideoModel photoVideoModel = new PhotoVideoModel();
                            JSONObject jsonobj = jsonaray.getJSONObject(i);
                            photoVideoModel.setPhotos(jsonobj.getString("photos"));
                            photoVideoModel.setId(jsonobj.getString("id"));
                            photoVideoModelArrayList.add(photoVideoModel);


                        }

                        progressDialog.dismiss();
                        PhotoVideoAdapter photoVideoAdapter = new PhotoVideoAdapter(PhotoVideoActivity.this,photoVideoModelArrayList);
                        recyclerViewphoto.setAdapter(photoVideoAdapter);

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
                PhotoVideoModel photoVideoModel = new PhotoVideoModel();
                stringMap.put("description_id",descid);
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
    }

}
