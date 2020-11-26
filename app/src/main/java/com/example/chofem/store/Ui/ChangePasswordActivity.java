package com.example.chofem.store.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chofem.store.R;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText etOldPassword, etNewpassword, etConfirmPassword;
    private String newPassword, oldPassword;
    private CustomProgressDialog progressDialog;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        widgets();

    }

    private void widgets() {
        mContext=ChangePasswordActivity.this;
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewpassword = findViewById(R.id.etNewpassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

    }

    public void changePassword(View view) {
        newPassword = etNewpassword.getText().toString();
        oldPassword = etOldPassword.getText().toString();

        progressDialog = new CustomProgressDialog(mContext, "Updating Your Profile...");
        progressDialog.show();
        //if everything is fine
        String storeId= SharedPref.getInstance(mContext).getData().getSTORE_ID();

        @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("newPass", newPassword);
        jsonParams.put("currentPass", oldPassword);
        jsonParams.put("current_id", storeId);

        final ResponseObject responseObject = new ResponseObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.URL_UPDATE_PASSWORD, new JSONObject(jsonParams),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            responseObject.setMESSAGE(response.getString("MESSAGE"));
                            responseObject.setSTATUS(response.getBoolean("STATUS"));

                                General.dialog(mContext, responseObject.getMESSAGE());
                                // Snackbar.make(v,"Server Error", Snackbar.LENGTH_SHORT).show();
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
                        // Snackbar.make(v,"Server Error", Snackbar.LENGTH_SHORT).show();
                        General.dialog(mContext, "server error");
                        if (null != error.networkResponse) {
                            // mResponseListener.requestEndedWithError(error);
                        }
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void isValid() {

    }
}