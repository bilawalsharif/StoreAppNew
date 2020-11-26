package com.example.chofem.store.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chofem.store.R;
import com.example.chofem.store.responses.LoginResponse;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Login_Activity extends AppCompatActivity {
    private EditText etPassword, etEmail;
    private Context mContext;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        iniatViews();
    }

    private void iniatViews() {
        mContext = Login_Activity.this;
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

    }

    public void login(View view) {
        boolean isValid = true;
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        if (email.trim().length()==0) {
            etEmail.setError("Enter Email");
            isValid = false;
        }
        if (pass.trim().length()==0) {
            etPassword.setError("Enter Password");
            isValid = false;
        }
        if (isValid) {
            doLogin(email, pass);
            /*
            JsonObject request = new JsonObject();
            request.addProperty("email",email);
            request.addProperty("password", pass);
            HashMap<String, String> data = new HashMap<>();
            data.put("email", email);

            LoginRequest  loginRequest=new LoginRequest(email,pass);
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            apiInterface.login(data).enqueue(new Callback<LoginResonse>() {
                @Override
                public void onResponse(Call<LoginResonse> call, Response<LoginResonse> response) {
                    if (response.isSuccessful())
                    {
                        LoginResonse loginResonse=response.body();
                        if (loginResonse.STATUS)
                        {
                            Toast.makeText(Login_Activity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login_Activity.this,DefaultDrawerActivity.class));
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResonse> call, Throwable t) {
                    Toast.makeText(Login_Activity.this, ""+t, Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }

    public void onClickRegister(View view) {
        startActivity(new Intent(mContext, SignUpActivity.class));
    }

    public void doLogin(final String email, final String pass) {
        progressDialog = new CustomProgressDialog(mContext, "Signing...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("email", etEmail.getText().toString());
        jsonParams.put("password", etPassword.getText().toString());

        final ResponseObject responseObject=new ResponseObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.URL_LOGIN, new JSONObject(jsonParams),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            responseObject.setMESSAGE(response.getString("MESSAGE"));
                            if (response.getString("MESSAGE").equals("Sucessfully Logged-in"))
                            {
                                responseObject.setMESSAGE(response.getString("MESSAGE"));
                                LoginResponse response2=new LoginResponse();
                                response2.setSTORE_ID(response.getString("STORE_ID"));
                                response2.setSTATUS(response.getBoolean("STATUS"));
                                response2.setFIRST_NAME(response.getString("FIRST_NAME"));
                                response2.setLAST_NAME(response.getString("LAST_NAME"));
                                response2.setEMAIL(response.getString("EMAIL"));
                                response2.setLANGUAGE(response.getString("LANGUAGE"));
                                response2.setPROFILE_IMAGE(response.getString("PROFILE_IMAGE"));
                                response2.setLATITUDE(response.getString("LATITUDE"));
                                response2.setLONGITUDE(response.getString("CATEGORY"));
                                response2.setLOCATION(response.getString("LOCATION"));
                                response2.setPHONE(response.getString("PHONE"));
                                response2.setPROFILE_IMAGE(response.getString("PROFILE_IMAGE"));

                                //session
                                SharedPref.getInstance(mContext).saveData(response2);
                                //if (SharedPref.getInstance(mContext).getBooleanPref("isNewUser",false))
                                //{
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), DefaultDrawerActivity.class));
                          //      }


                            }
                            else {
                                progressDialog.dismiss();
                                General.dialog(mContext,responseObject.getMESSAGE());
                               // Toast.makeText(mContext, "INVALID USERNAME/PASSWORD", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //   mResponseListener.requestCompleted(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        General.dialog(mContext,"server error");
                       // Toast.makeText(mContext, "Netword Error", Toast.LENGTH_SHORT).show();
                        if (null != error.networkResponse) {

                           // mResponseListener.requestEndedWithError(error);
                        }
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);

        //if everything is fine
       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  progressBar.setVisibility(View.GONE);
                if (!response.equalsIgnoreCase(" "))
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(response);
                        boolean status = obj.getBoolean("STATUS");
                        String msg = obj.getString("MESSAGE");
                        //if no error in response
                        if (status == true && msg.equalsIgnoreCase("Sucessfully Logged-in")) {
                            //getting the user from the response
                            //   JSONObject userJson = obj.getJSONObject("user");

                            //creating a new user object
*//*                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email"),
                                        userJson.getString("gender")
                                );*//*

                            //storing the user in shared preferences
                            //SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), DefaultDrawerActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(js);
        */
    }
}
