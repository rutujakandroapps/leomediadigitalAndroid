package com.aptiquiz.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.aptiquiz.model.Film_personalityModel;
import com.aptiquiz.util.Constant;
import com.leomediadigital.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FilmsPersonalitiesActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ImageView imgdownloaded;
  static  String media;
  ImageView imgback;
    String url="http://142.93.220.120/Leo_Media_Digital/uploads/film_personality_image/ajje.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.films_personalities);
        imgback =findViewById(R.id.imgback);
        progressDialog = new ProgressDialog(this);
        imgdownloaded =findViewById(R.id.imgdownloaded);
        imgdownloaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PdfWebview.class));

//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
               // downloadPDFList(Constant.IMGPATH_FILMPERSONALITIES + media+"");
            }
        });
        //getFilmsPersonalities();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        getFilmsPersonalities();
    }
    public void getFilmsPersonalities(){
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringrequest1 = new StringRequest(Request.Method.POST, Constant.FILMS_PERSONALITIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

    public void downloadPDFList(String path){
        Toast.makeText(getApplicationContext(), "Downloading Start....", Toast.LENGTH_SHORT).show();
        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }
}
