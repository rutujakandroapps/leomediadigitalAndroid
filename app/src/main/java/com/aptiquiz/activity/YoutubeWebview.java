package com.aptiquiz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.aptiquiz.model.PhotoVideoModel;
import com.aptiquiz.util.Constant;
import com.leomediadigital.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YoutubeWebview extends AppCompatActivity {

    ArrayList<PhotoVideoModel> photoVideoModelArrayList;
    Context context;
    RecyclerView recyclerViewphoto;
    ProgressDialog progressDialog;
    public static String videolink;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubewebview);
        WebView urlWebView = findViewById(R.id.webview);
        progressDialog=new ProgressDialog(this);
        urlWebView.setWebViewClient(new AppWebViewClients());
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setLoadWithOverviewMode(true);
        urlWebView.getSettings().setUseWideViewPort(true);
        urlWebView.getSettings().setBuiltInZoomControls(true);
        urlWebView.getSettings().setDisplayZoomControls(false);
        urlWebView.loadUrl(videolink);
        photoVideoModelArrayList= new ArrayList<>();
       // getPhotovideo();
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

                            PhotoVideoModel photoVideoModel = new PhotoVideoModel();
                            JSONObject jsonobj = jsonaray.getJSONObject(i);
                            JSONArray jsonaray1 = jsonobj .getJSONArray("getPhoto");
                            JSONArray jsonaray2 = jsonobj .getJSONArray("Description");

                            for (int j = 0; j < jsonaray1.length(); j++) {

                                JSONObject jsonobj1 = jsonaray1.getJSONObject(j);
                                type=jsonobj1.getString("type");
                                if (type.equals("1")){

                                    videolink=jsonobj1.getString("photos");
                                }
                            }

                            for (int k = 0; k < jsonaray2.length(); k++) {
                                JSONObject jsonobj2 = jsonaray2.getJSONObject(k);
                                photoVideoModel.setId(jsonobj2.getString("id"));
                                photoVideoModel.setDescription(jsonobj2.getString("description"));
                                photoVideoModelArrayList.add(photoVideoModel);

                            }


                        }

                        progressDialog.dismiss();

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

    public class AppWebViewClients extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);


        }
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


