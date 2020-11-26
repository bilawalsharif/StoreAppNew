package com.example.chofem.store.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chofem.store.R;
import com.example.chofem.store.Ui.Login_Activity;
import com.example.chofem.store.responses.LoginResponse;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleySingleton;
import com.goodiebag.pinview.Pinview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class OTPActivity extends AppCompatActivity {
    private Pinview pin;
    private Context mContext;
    private CustomProgressDialog progressDialog;
    private Intent intent;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        pin = (Pinview) findViewById(R.id.pinview);
        intent=getIntent();
        mContext=OTPActivity.this;
        if (intent!=null)
        {
            email=intent.getStringExtra("email");
            sendOTP();
        }
       // myLayout.addView(pin);
        pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                Toast.makeText(OTPActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void resendOTP(View view)
    {
        sendOTP();
    }
    public void sendOTP()
    {
        progressDialog = new CustomProgressDialog(mContext, "Sending OTP...");
        progressDialog.show();
        LoginResponse loginResponse = SharedPref.getInstance(mContext).getData();
        TextView txtEmail=findViewById(R.id.txtEmail);
        txtEmail.setText("at your given Email "+email);

        @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("email", email);
        final ResponseObject responseObject=new ResponseObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.URL_SEND_OTP, new JSONObject(jsonParams),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            responseObject.setMESSAGE(response.getString("MESSAGE"));
                            if (response.getString("MESSAGE").equals("OTP SEND TO EMAIL"))
                            {
                                General.dialog(mContext,responseObject.getMESSAGE());
                            }
                            else {
                                progressDialog.dismiss();
                                General.dialog(mContext,responseObject.getMESSAGE());
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
                        General.dialog(mContext,"server error.");
                        if (null != error.networkResponse) {
                            // mResponseListener.requestEndedWithError(error);
                        }
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
    public void verifyOTP(View view)
    {
        if (pin.getValue().length()==0)
        {
            General.dialog(mContext,"Please Enter OTP");
        }
        else
        {
            progressDialog = new CustomProgressDialog(mContext, "Verifying OTP...");
            progressDialog.show();
            LoginResponse loginResponse =SharedPref.getInstance(mContext).getData();

            @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("email", email);
            jsonParams.put("otp",pin.getValue());
            final ResponseObject responseObject=new ResponseObject();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.URL_VERIFY_OTP, new JSONObject(jsonParams),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                progressDialog.dismiss();
                                responseObject.setMESSAGE(response.getString("MESSAGE"));
                                if (responseObject.getMESSAGE().equalsIgnoreCase("OTP Code Verified"))
                                {
                                    SharedPref.getInstance(mContext).savePref("isNewUser",true);
                                    finish();
                                    //startActivity(new Intent(getApplicationContext(), DefaultDrawerActivity.class));
                                    startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                                }
                                else {
                                    progressDialog.dismiss();
                                    General.dialog(mContext,responseObject.getMESSAGE());
                                    // Toast.makeText(mContext, "INVALID OTP", Toast.LENGTH_SHORT).show();
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
                            General.dialog(mContext,"server error.");
                            if (null != error.networkResponse) {
                                // mResponseListener.requestEndedWithError(error);
                            }
                        }
                    });
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        }
    }
}