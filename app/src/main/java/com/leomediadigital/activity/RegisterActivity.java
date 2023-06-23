package com.leomediadigital.activity;

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
import com.leomediadigital.R;
import com.leomediadigital.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    EditText etxFirstName;
    EditText etxLastName;
    EditText etxEmail;
    EditText etxPhoneNumber;
    EditText etxProfession;
    EditText etxPassword;
    EditText etxConfirmPass;
    Button btnRegister;
    Button btnLogIn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        etxFirstName = findViewById(R.id.etxFirstName);
        etxLastName = findViewById(R.id.etxLastName);
        etxEmail = findViewById(R.id.etxEmail);
        etxPhoneNumber = findViewById(R.id.etxPhoneNumber);
        etxProfession = findViewById(R.id.etxProfession);
        etxPassword = findViewById(R.id.etxPassword);
        etxConfirmPass = findViewById(R.id.etxConfirmPass);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etxFirstName.getText().toString())) {
                    etxFirstName.setError("First Name");
                    etxFirstName.requestFocus();
                } else if (TextUtils.isEmpty(etxLastName.getText().toString())) {
                    etxLastName.setError("Last Name");
                    etxLastName.requestFocus();
                } else if (TextUtils.isEmpty(etxEmail.getText().toString())) {
                    etxEmail.setError("Email");
                    etxEmail.requestFocus();
                }
//                else if (isValidEmailId(etxEmail.getText().toString().trim())) {
//                    etxEmail.setError("Valid Email");
//                    etxEmail.requestFocus();
//                }
                else if (TextUtils.isEmpty(etxPhoneNumber.getText().toString())) {
                    etxPhoneNumber.setError("Number");
                    etxPhoneNumber.requestFocus();
                } else if (TextUtils.isEmpty(etxProfession.getText().toString())) {
                    etxProfession.setError("Profession");
                    etxProfession.requestFocus();
                } else if (TextUtils.isEmpty(etxPassword.getText().toString())) {
                    etxPassword.setError("Password");
                    etxPassword.requestFocus();
                } else if (TextUtils.isEmpty(etxConfirmPass.getText().toString())) {
                    etxConfirmPass.setError("Confirm Password");
                    etxConfirmPass.requestFocus();
                } else if (!etxPassword.getText().toString().trim().equals(etxConfirmPass.getText().toString().trim())) {
                    etxConfirmPass.setError("Password Not Matched");
                    etxConfirmPass.requestFocus();
                } else {
                    registerUser();
                }

            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

//    private boolean isValidEmailId(String email) {
//
//        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
//                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
//                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
//    }

    private static boolean isValidEmailId(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void registerUser() {

        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.e("TAG", "onResponseRegister: " + response);

                    if (jsonObject.getString("code").equalsIgnoreCase("200")) {

                        Toasty.success(RegisterActivity.this, jsonObject.getString("message")).show();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toasty.info(RegisterActivity.this, jsonObject.getString("message")).show();
                        Log.e("TAG", "onResponseLogin: " + jsonObject.getString("message"));
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(RegisterActivity.this, e.getMessage()).show();
                    Log.e("TAG", "onErrorResponse1: " + e.getMessage());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegisterActivity.this, error.getMessage()).show();
                Log.e("TAG", "onErrorResponse2: " + error.getMessage());
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("name", etxFirstName.getText().toString().trim() + " " + etxLastName.getText().toString().trim());
                hashMap.put("mobile", etxPhoneNumber.getText().toString().trim());
                hashMap.put("email", etxEmail.getText().toString().trim());
                hashMap.put("profession", etxProfession.getText().toString().trim());
                hashMap.put("password", etxConfirmPass.getText().toString().trim());

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
