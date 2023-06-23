package com.leomediadigital.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfWebview  extends AppCompatActivity {

    ProgressDialog progressDialog;
    ProgressBar progressBar;
    String document;
    ImageView imgdownloaded, imgsearch, imgback;
//    String url = "http://142.93.220.120/Leo_Media_Digital/uploads/film_personality_image/ajje.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfwebview);
        WebView urlWebView = findViewById(R.id.webview);
        imgback = findViewById(R.id.imgback);

        urlWebView.setWebViewClient(new MyWebViewClient());
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setLoadWithOverviewMode(true);
        urlWebView.getSettings().setUseWideViewPort(true);
        urlWebView.getSettings().setBuiltInZoomControls(true);
        urlWebView.getSettings().setDisplayZoomControls(false);
        progressDialog = new ProgressDialog(PdfWebview.this);
        progressDialog.setMessage("Please wait ...");
        urlWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="+Constant.IMGPATH_FILMPERSONALITIES+FilmsPersonalitiesActivity. document);

//        urlWebView.setWebViewClient(new MyWebViewClient());
//        urlWebView.getSettings().setJavaScriptEnabled(true);
//        progressDialog = new ProgressDialog(PdfWebview.this);
//        urlWebView.getSettings().setJavaScriptEnabled(true);
//        urlWebView.getSettings().setLoadWithOverviewMode(true);
//        urlWebView.getSettings().setUseWideViewPort(true);
//        urlWebView.getSettings().setBuiltInZoomControls(true);
//        urlWebView.getSettings().setDisplayZoomControls(false);
//        urlWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +Constant.IMGPATH_FILMPERSONALITIES+FilmsPersonalitiesActivity. media);
        imgdownloaded = findViewById(R.id.downnload);
        imgsearch = findViewById(R.id.search);
        //getFilmsPersonalities();
        //pdf share
//        imgsearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//               // shareTextUrl();
//            }
//        });

        imgdownloaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadPDFList(Constant.IMGPATH_FILMPERSONALITIES + FilmsPersonalitiesActivity.document + "");
               //showPdf();
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }

    private void shareTextUrl() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "ajje.pdf");
        Uri uri = Uri.fromFile(outputFile);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setPackage("com.whatsapp");

        startActivity(share);
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

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.setMessage("Please wait ...");

            try {
                progressDialog.show();
            } catch (WindowManager.BadTokenException e) {
                //use a log message
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressDialog != null) {

                try {
                    if (!PdfWebview.this.isFinishing() && progressDialog != null) {
                        progressDialog.dismiss();
                    }


                } catch (WindowManager.BadTokenException e) {
                    //use a log message
                }
            }
        }
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

    public void getFilmsPersonalities() {
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
                        JSONArray jsonaray = jsonObject.getJSONArray("media");
                        for (int i = 0; i < jsonaray.length(); i++) {

                            Film_personalityModel film_personalityModel = new Film_personalityModel();
                            JSONObject jsonobj = jsonaray.getJSONObject(i);
//
                            document = jsonobj.getString("document");
                        }

                        progressDialog.dismiss();
//
                    } else {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    public void downloadPDFList(String path) {
        Toast.makeText(getApplicationContext(), "Downloading Start....", Toast.LENGTH_SHORT).show();
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }


    public class Downloader {

        public  void DownloadFile(String fileURL, File directory) {
            try {

                FileOutputStream f = new FileOutputStream(directory);
                URL u = new URL(fileURL);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                InputStream in = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void showPdf()
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/pdf/Read.pdf");
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }


}
