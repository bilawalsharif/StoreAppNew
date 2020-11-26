package com.example.chofem.store.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chofem.store.R;
import com.example.chofem.store.otp.OTPActivity;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText etFirstName,etLastName,etEmail,etPassword,etCellNo,etConfirmPassword;
    private Context mContext;
    private CustomProgressDialog progressDialog;
    private Button btnRegister;
    private Spinner sp_languages,sp_category;
    private String language,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        iniatViews();
    }

    private void iniatViews() {
        mContext=SignUpActivity.this;
        btnRegister=findViewById(R.id.btnRegister);
        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etEmail=findViewById(R.id.etEmail);
        etCellNo=findViewById(R.id.etCellNo);
        etPassword=findViewById(R.id.etPassword);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);
        sp_languages=findViewById(R.id.sp_languages);
        sp_category=findViewById(R.id.sp_category);

        sp_languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language=sp_languages.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=sp_category.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid())
                registerStore();
            }
        });
    }
    private void registerStore()
    {
        progressDialog = new CustomProgressDialog(mContext, "Registering...");
        progressDialog.show();
        //if everything is fine

        @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("fname", etFirstName.getText().toString());
        jsonParams.put("lname", etLastName.getText().toString());
        jsonParams.put("email",etEmail.getText().toString().trim());
        jsonParams.put("language",language);
        jsonParams.put("phone", etCellNo.getText().toString());
        jsonParams.put("password", etPassword.getText().toString());
        jsonParams.put("category", category);

        final ResponseObject responseObject=new ResponseObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.URL_REGISTER, new JSONObject(jsonParams),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responseObject.setMESSAGE(response.getString("MESSAGE"));
                            if (response.getBoolean("STATUS"))
                            {
                                responseObject.setMESSAGE(response.getString("MESSAGE"));
                                SharedPref.getInstance(mContext).setFirstTimeLogin(true);
                                Intent obj=new Intent(mContext, OTPActivity.class);
                                obj.putExtra("email",etEmail.getText().toString());
                                finish();
                                startActivity(obj);
                               // startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                            }
                            else {
                                progressDialog.dismiss();
                                General.dialog(mContext,responseObject.getMESSAGE());
                                //Toast.makeText(mContext, "INVALID USERNAME/PASSWORD", Toast.LENGTH_SHORT).show();
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
                        if (null != error.networkResponse) {

                            // mResponseListener.requestEndedWithError(error);
                        }
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }
    public boolean isValid() {
        boolean flag = true;
        if (etEmail.getText().toString().trim().length()==0) {
            etEmail.setError("Enter Email");
            return false;
        }
        if (etPassword.getText().toString().trim().length()==0) {
            etPassword.setError("Enter Password");
            return false;
        }
         if (etPassword.getText().toString().trim().length()<5) {
            etPassword.setError("Enter Password having length more than 5");
             return false;
        }
        if (etConfirmPassword.getText().toString().trim().length()==0)
        {
            etPassword.setError("Enter Confirm Password");
            return false;
        }
         if (!etConfirmPassword.getText().toString().trim().equalsIgnoreCase(etPassword.getText().toString().trim()))
        {
            etConfirmPassword.setError("Password and Confirm Password not Match");
            return false;
        }
         if (etFirstName.getText().toString().trim().length()==0) {
            etFirstName.setError("Enter First Name");
             return false;
        }
         if (etCellNo.getText().toString().trim().length()==0) {
            etCellNo.setError("Enter Cell Number");
             return false;
        }
         if (language.length()==0) {
            Toast.makeText(mContext, "Select Language", Toast.LENGTH_SHORT).show();
             return false;
        }
        if (category.length()==0) {
            Toast.makeText(mContext, "Select Store Category", Toast.LENGTH_SHORT).show();
            return false;
        }
       return true;
    }
    public void login(View view) {
        finish();
        startActivity(new Intent(mContext,Login_Activity.class));
    }
}