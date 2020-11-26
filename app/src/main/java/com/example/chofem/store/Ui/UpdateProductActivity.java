package com.example.chofem.store.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chofem.store.R;
import com.example.chofem.store.responses.GetStoreProductResponse;
import com.example.chofem.store.responses.LoginResponse;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleyMultipartRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.chofem.store.Fragment.SelectLanguageFragment.PICK_IMAGE;


public class UpdateProductActivity extends AppCompatActivity {
    private Context mContext;
    private CustomProgressDialog customProgressDialog;
    private ImageView btnUpdateProductImage,imgShowProductPhoto;
    private Button btnUpdateProduct;
    private EditText etProductName,etProductDescription ,etProductQuantity, etProductPrice,etCategory;
    private Bitmap bitmap;
    private String filePath,storeID,productID;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product_main);
        widgets();
    }
    private void widgets() {
        mContext =UpdateProductActivity.this;
        btnUpdateProduct = findViewById(R.id.btnAddProduct);
        etProductName = findViewById(R.id.etProductName);
        etProductDescription=findViewById(R.id.etProductDescription);
        //  etProductQuantity = view.findViewById(R.id.etProductQuantity);
        btnUpdateProductImage=findViewById(R.id.btnUpdateProductImage);
        etProductPrice = findViewById(R.id.etProductPrice);
        etCategory = findViewById(R.id.etCategory);
        imgShowProductPhoto = findViewById(R.id.imgShowProductPhoto);

        LoginResponse loginResponse= SharedPref.getInstance(mContext).getData();
        if (!loginResponse.getSTORE_ID().equalsIgnoreCase(""))
        storeID=loginResponse.getSTORE_ID();


        intent=getIntent();
        if (intent!=null)
        {
            GetStoreProductResponse.PRODUCT model = (GetStoreProductResponse.PRODUCT) intent.getSerializableExtra("storeData");
            etProductName.setText(model.getPName());
            etProductPrice.setText(model.getPPrice());
            etProductDescription.setText(model.getPDescription());
            etCategory.setText(model.getPCategory());
            productID=model.getPId();

            Picasso.get()
                    .load(model.getPrimaryImage())
                    .noFade()
                    .into(imgShowProductPhoto);

          //  Gson gson = new Gson();
          //  GetStoreProductResponse.PRODUCT ob = gson.fromJson(getIntent().getStringExtra("myjson"), GetStoreProductResponse.PRODUCT.class);
        }

        btnUpdateProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }
    public void choosePhoto() {
        if ((ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(UpdateProductActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(UpdateProductActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(UpdateProductActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }
        } else {
            showFileChooser();
        }

    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            filePath=getPath(selectedImage);
            updateProductPhoto();
        } else {
            Toast.makeText(mContext, "No photo Selected", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String getPath(Uri picUri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(picUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgShowProductPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        cursor.close();
        return picturePath;
    }

    private void updateProductPhoto() {

        if (General.isInternetOn(mContext))
        {
            customProgressDialog = new CustomProgressDialog(mContext, "Updating Prodcut Picture...");
            customProgressDialog.setCancelable(false);
            customProgressDialog.show();

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_UPDATE_PRODUCT_IMAGE,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                customProgressDialog.dismiss();
                                JSONObject obj = new JSONObject(new String(response.data));
                                ResponseObject resonseObject=new ResponseObject();
                                resonseObject.setMESSAGE(obj.getString("MESSAGE"));
                                resonseObject.setSTATUS(obj.getBoolean("STATUS"));
                                General.dialog(mContext,resonseObject.getMESSAGE());
                                // Toast.makeText(getContext(), obj.getString(resonseObject.getMESSAGE()), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                // Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            customProgressDialog.dismiss();
                            General.dialog(mContext,"server error.");
                            Log.e("GotError", "" + error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> jsonParams = new HashMap<>();
                    jsonParams.put("store", storeID);
                    jsonParams.put("product", productID);
                    return jsonParams;
                }
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    byte[] byteArray = getFileDataFromDrawable(bitmap);
                    if (byteArray != null) {
                        //  if (extension.equalsIgnoreCase("jpg")) {
                        //Important convert your image into byte Array
                        //filePath.substring(filePath.lastIndexOf(".")
                        params.put("primaryImage", new DataPart(imagename + ".jpg", byteArray));
                        //}
                    }
                    return params;
                }
            };
            //adding the request to volley
            Volley.newRequestQueue(mContext).add(volleyMultipartRequest);
        }
        else {
            General.dialog(mContext,"No Internet Available");
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void updateProductInfo(View view)
    {
       // JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("name",etProductName.getText().toString());
            jsonObject.put("description", etProductDescription.getText().toString());
            jsonObject.put("price", etProductPrice.getText().toString());
            jsonObject.put("status", "Available");
            jsonObject.put("category", etCategory.getText().toString());
            jsonObject.put("store", Integer.parseInt(storeID));
            jsonObject.put("product", Integer.parseInt(productID));
          //  jsonArray.put(jsonObject);
            Log.i("jsonString", jsonObject.toString());


        }catch(Exception e){

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLs.URL_UPDATE_PRODUCT, jsonObject,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        ResponseObject responseObject=new ResponseObject();
                        Log.e("Response", response.toString());
                        try {
                            responseObject.setMESSAGE(response.getString("MESSAGE"));
                            General.dialog(mContext,responseObject.getMESSAGE());
                           // JSONArray arrData = response.getJSONArray("data");
                            //parseData(arrData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        General.dialog(mContext,"server error.");
                        Log.e("Error.Response", error.toString());
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    json = new String(response.data);
                                    System.out.println(json);
                                    break;
                            }
                            //Additional cases
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }
}