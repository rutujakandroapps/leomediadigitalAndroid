package com.aptiquiz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import com.codewaves.youtubethumbnailview.ThumbnailLoader;
//import com.aptiquiz.R;
import com.aptiquiz.adapter.VideoAdapter;
import com.aptiquiz.model.VideoModel;
import com.aptiquiz.util.Constant;
import com.leomediadigital.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ArrayList<VideoModel> videoModelArrayList;
    Context context;
    ImageView imgback;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubevideolayout);
        imgback =findViewById(R.id.imgback);
        progressDialog= new ProgressDialog(this);

        ThumbnailLoader.initialize(getApplicationContext());
        recyclerView= findViewById(R.id.recyclvideo);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoModelArrayList = new ArrayList<>();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        getPhotovideo();
    }

    public  void getPhotovideo(){
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, Constant.PHOTOVIDEO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code, type="",message = jsonObject.getString("message"), otp_id;
                    code = jsonObject.getString("code");

                    if (code.equals("200")) {

                        JSONArray jsonaray = jsonObject.getJSONArray("photos");

                        for (int i = 0; i < jsonaray.length(); i++) {


                            JSONObject jsonobj = jsonaray.getJSONObject(i);
                            JSONArray jsonaray1 = jsonobj .getJSONArray("getPhoto");
                            //JSONArray jsonaray2 = jsonobj .getJSONArray("Description");

                            for (int j = 0; j < jsonaray1.length(); j++) {
                               VideoModel videoModel = new VideoModel();
                                JSONObject jsonobj1 = jsonaray1.getJSONObject(j);
                                type=jsonobj1.getString("type");
                                if (type.equals("1")){

                                    videoModel.setVideolink(jsonobj1.getString("photos"));
                                    videoModelArrayList.add(videoModel);

                                }
//
                            }
//

                        }

                        progressDialog.dismiss();
                     VideoAdapter videoAdapter = new VideoAdapter(VideoActivity.this,videoModelArrayList);
                        recyclerView.setAdapter(videoAdapter);

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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
