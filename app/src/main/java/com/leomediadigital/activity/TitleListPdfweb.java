package com.leomediadigital.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.leomediadigital.R;
import com.leomediadigital.model.ContactModel;

public class TitleListPdfweb extends AppCompatActivity {


    ImageView imgback;
    ProgressDialog progressDialog;
    ContactModel contactModel = new ContactModel();
    String url;
            //="http://142.93.220.120/Leo_Media_Digital/uploads/title/sample.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titlepdfview);
        imgback = findViewById(R.id.imgback);
        url = getIntent().getStringExtra("pdf");
        progressDialog = new ProgressDialog(this);
        WebView urlWebView = findViewById(R.id.webview);
        imgback =findViewById(R.id.imgback);
        urlWebView.setWebViewClient(new MyWebViewClient());
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setLoadWithOverviewMode(true);
        urlWebView.getSettings().setUseWideViewPort(true);
        urlWebView.getSettings().setBuiltInZoomControls(true);
        urlWebView.getSettings().setDisplayZoomControls(false);
        progressDialog = new ProgressDialog(TitleListPdfweb.this);
        progressDialog.setMessage("Please wait ...");
        urlWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

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


            try {
                progressDialog.show();
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(progressDialog !=null){

                try {
                    if (!TitleListPdfweb.this.isFinishing() && progressDialog != null) {
                        progressDialog.dismiss();
                    }


                }
                catch (WindowManager.BadTokenException e) {
                    //use a log message
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    }

