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
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.aptiquiz.R;
import com.aptiquiz.util.Constant;
import com.aptiquiz.util.SharedPref;
import com.leomediadigital.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {


    EditText etxUserName;
    EditText etxUserPass;
    Button btnLogIn;
    Button btnRegister;
    LinearLayout layoutForgetPass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setTitle("Login");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        etxUserName = findViewById(R.id.etxUserName);
        etxUserPass = findViewById(R.id.etxUserPass);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnRegister = findViewById(R.id.btnRegister);
        layoutForgetPass = findViewById(R.id.layoutForgetPass);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etxUserName.getText().toString())){
                    etxUserName.setError("Mobile Number");
                    etxUserName.requestFocus();
                }else if (TextUtils.isEmpty(etxUserPass.getText().toString())){
                    etxUserPass.setError("Password");
                    etxUserPass.requestFocus();
                }else {
                    loginUser();
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                finish();

            }
        });

        layoutForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent forgetIntent = new Intent(LoginActivity.this,ForgetPassActivity.class);
                startActivity(forgetIntent);
            }
        });

    }

    private void loginUser() {

        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("code").equalsIgnoreCase("200")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("login");
                        SharedPref.setUserId(LoginActivity.this,jsonObject1.getString("id"));
                        SharedPref.setUserName(LoginActivity.this,jsonObject1.getString("full_name"));
                        SharedPref.setUserNumber(LoginActivity.this,jsonObject1.getString("mobile"));
                        SharedPref.setUserEmail(LoginActivity.this,jsonObject1.getString("email_id"));
                        SharedPref.setUserProfession(LoginActivity.this,jsonObject1.getString("profession"));
                        SharedPref.setLogin(LoginActivity.this,true);

                        Toasty.success(LoginActivity.this,"Successfully Login").show();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toasty.info(LoginActivity.this, jsonObject.getString("message")).show();
                        Log.e("TAG", "onResponseLogin: " + jsonObject.getString("message"));
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(LoginActivity.this, e.getMessage()).show();
                    Log.e("TAG", "onErrorResponse1: " + e.getMessage());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(LoginActivity.this, error.getMessage()).show();
                Log.e("TAG", "onErrorResponse2: " + error.getMessage());
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("mobile", etxUserName.getText().toString().trim());
                hashMap.put("password", etxUserPass.getText().toString().trim());

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
        finishAffinity();
    }
}
