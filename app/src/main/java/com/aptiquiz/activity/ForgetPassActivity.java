package com.aptiquiz.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aptiquiz.util.Constant;
import com.leomediadigital.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ForgetPassActivity extends AppCompatActivity {

    EditText etxEmailID;
    Button btnForgetPass;
    Button btnLogin;
ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setTitle("Forget Password");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        etxEmailID = findViewById(R.id.etxEmailID);
        btnForgetPass = findViewById(R.id.btnForgetPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etxEmailID.getText().toString())){
                    etxEmailID.setError("Email");
                    etxEmailID.requestFocus();
                }
//                else if (isValidEmailId(etxEmailID.getText().toString().trim())){
//                    etxEmailID.setError("Valid Email");
//                    etxEmailID.requestFocus();
//                }
                else {
                    forgetPass();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private static boolean isValidEmailId(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

//    private boolean isValidEmailId(String email){
//
//        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
//                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
//                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
//    }

    private void forgetPass() {

        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FORGET_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("code").equalsIgnoreCase("200")) {

//                        JSONObject jsonObject1 = jsonObject.getJSONObject("login");

                        Toasty.success(ForgetPassActivity.this,"Password Sent On Mail").show();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        startActivity(new Intent(ForgetPassActivity.this, LoginActivity.class));
                        finish();

                    } else {
                        Toasty.info(ForgetPassActivity.this, jsonObject.getString("message")).show();
                        Log.e("TAG", "onResponseLogin: " + jsonObject.getString("message"));
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(ForgetPassActivity.this, e.getMessage()).show();
                    Log.e("TAG", "onErrorResponse1: " + e.getMessage());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(ForgetPassActivity.this, error.getMessage()).show();
                Log.e("TAG", "onErrorResponse2: " + error.getMessage());
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("email", etxEmailID.getText().toString().trim());
                return hashMap;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> paramsHeader = new HashMap<>();
                String credentials = Constant.USERNAME + ":" + Constant.PASSWORD;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                paramsHeader.put("Authorization", auth);
                paramsHeader.put("token", Constant.TOKEN);
                return paramsHeader;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000
                , 5
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
