package com.example.chofem.store.Fragment;

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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chofem.store.R;
import com.example.chofem.store.responses.LoginResponse;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductFragment extends Fragment {
    public static final int PICK_IMAGE = 1;
    private View view;
    private EditText etProductName,etProductDescription ,etProductQuantity, etProductPrice,etCategory;
    private LinearLayout viewChooseProductPhoto;
    private Button btnAddProduct;
    private ImageView imgShowProductPhoto;
    private Context mContext;
    private String filePath = "",storeID;
    private Bitmap bitmap;
    private CustomProgressDialog customProgressDialog;

    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_product, container,
                false);
        widgets();
        //showProduct();
        return view;
    }

    private void widgets() {
        mContext = getActivity();
        //MapDialogFragment dialog = new MapDialogFragment(getActivity());
        //dialog.show(getFragmentManager(), "example");
        viewChooseProductPhoto = view.findViewById(R.id.viewChooseProductPhoto);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        etProductName = view.findViewById(R.id.etProductName);
        etProductDescription=view.findViewById(R.id.etProductDescription);
      //  etProductQuantity = view.findViewById(R.id.etProductQuantity);
        etProductPrice = view.findViewById(R.id.etProductPrice);
        etCategory = view.findViewById(R.id.etCategory);
        imgShowProductPhoto = view.findViewById(R.id.imgShowProductPhoto);

        LoginResponse loginResponse= SharedPref.getInstance(mContext).getData();
        if (!loginResponse.getSTORE_ID().equalsIgnoreCase(""))
            storeID=loginResponse.getSTORE_ID();

        viewChooseProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=true;
                if (etProductName.getText().toString().trim().length()==0)
                {
                    etProductName.setError("Please Enter Product Name");
                    flag=false;
                }
                if (etProductDescription.getText().toString().trim().length()==0)
                {
                    etProductDescription.setError("Please Enter Product Description");
                    flag=false;
                }
                if (etCategory.getText().toString().trim().length()==0)
                {
                    etCategory.setError("Please Enter Product Category");
                    flag=false;
                }
                if (etProductPrice.getText().toString().trim().length()==0)
                {
                    etProductPrice.setError("Please Enter Product Price");
                    flag=false;
                }
                if (flag)
                {
                    if (bitmap!=null && !filePath.equalsIgnoreCase(""))
                    {
                        if (General.isInternetOn(mContext))
                        addProduct();
                    }
                    else {
                        Toast.makeText(mContext, "Please Select Product Picture", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void choosePhoto() {
      /*  Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);*/


//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("*/*");
//            String [] mimeTypes = {"image/*", "application/pdf"};
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//            intent.setType("application/pdf","image/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent, PICK_IMAGE);

//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("*/*");
//            String[] mimetypes = {"image/*"};
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//            startActivityForResult(intent, PICK_IMAGE);


//
//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                    .setType("*/*");
//            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
//            startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_IMAGE);


//        Intent intent = new Intent(Intent.ACTION_PICK,  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("*/*");
//        String[] mimetypes = {"image/*"};
//
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_IMAGE);


        // Creating intent.
/*        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), PICK_IMAGE);*/


        if ((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }
        } else {
            //  Log.e("Else", "Else");
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
  /*          Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
            imgShowProductPhoto.setImageBitmap(bmImg);*/
            //Check if the intent was to pick image, was successful and an image was picked
/*        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PICK_IMAGE) {
            Uri newUri = data.getData();
            imgShowProductPhoto.setImageURI(Uri.parse(getFilename(newUri)));
        }*/
/*            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                imgShowProductPhoto.setImageBitmap(bitmap);

            }
            catch (IOException e) {

                e.printStackTrace();
           } */

        try {
            assert data != null;
            if (requestCode == PICK_IMAGE && data.getData() != null) {
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                if (filePath != null) {
                    //  textView.setText("File Selected");
                    //Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    // uploadBitmap(bitmap);
                    //loadPhoto(filePath);
                   imgShowProductPhoto.setImageBitmap(BitmapFactory.decodeFile(filePath));
                  //  imgShowProductPhoto.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getActivity(), "no image selected",
                            Toast.LENGTH_LONG).show();
                }
            }

        }catch (Exception ex)
        {

        }
    }

    private void loadPhoto(String url) {
        Picasso.get()
                .load(url)
                .noFade()
                .into(imgShowProductPhoto);
    }

    public String getPath(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void addProduct() {
        customProgressDialog = new CustomProgressDialog(mContext, "Adding Product...");
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_ADD_PRODUCT,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            customProgressDialog.dismiss();
                            JSONObject obj = new JSONObject(new String(response.data));
                            ResponseObject resonseObject=new ResponseObject();
                            resonseObject.setMESSAGE(obj.getString("MESSAGE"));
                            resonseObject.setSTATUS(obj.getBoolean("STATUS"));
                            if (resonseObject.STATUS)
                            {
                                General.dialog(mContext,resonseObject.getMESSAGE());
                            }
                           // Toast.makeText(getContext(), obj.getString(resonseObject.getMESSAGE()), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                           // Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customProgressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonParams = new HashMap<>();
                jsonParams.put("name", etProductName.getText().toString());
                jsonParams.put("description", etProductDescription.getText().toString());
                jsonParams.put("price", etProductPrice.getText().toString());
                jsonParams.put("status", "Available");
                jsonParams.put("category", etCategory.getText().toString());
                jsonParams.put("store_id", storeID);
                return jsonParams;
            }
            /*            @Override
                        protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            long imagename = System.currentTimeMillis();

                           // params.put("primaryImage", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                            params.put("primaryImage", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));

                            return params;
                        }
                    };*/
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
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

}