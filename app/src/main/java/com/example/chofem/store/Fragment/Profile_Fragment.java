package com.example.chofem.store.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chofem.store.R;
import com.example.chofem.store.responses.LoginResponse;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.ImagePickerActivity;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleyMultipartRequest;
import com.example.chofem.store.volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile_Fragment extends Fragment {
    public static final int REQUEST_IMAGE = 100;
    private static final int PICK_IMAGE = 12;

    private View view;
    private Button btnLogin;
    private EditText etFirstName,etLastName,etEmail,etPassword,etCellNo,etLanguage;
    private Context mContext;
    private CustomProgressDialog progressDialog;
    private Button btnUpdateProfile;
    private Spinner sp_languages;
    private String language;
    private String storeId="";
    private ImageView imgBackPress;
    private TextView tv_edit_photo;
    private CircleImageView img_profile;
    private String filePath;
    private Uri photoURI;
    private CustomProgressDialog customProgressDialog;
    private Bitmap bitmap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.profile_layout, container,
                false);
        widgets();
        return view;
    }
    private void widgets() {

        mContext= getActivity();
        img_profile=view.findViewById(R.id.img_profile);
        btnUpdateProfile=view.findViewById(R.id.btnUpdateProfile);
        etFirstName=view.findViewById(R.id.etFirstName);
        etLastName=view.findViewById(R.id.etLastName);
        etEmail=view.findViewById(R.id.etEmail);
        etCellNo=view.findViewById(R.id.etCellNo);
        etPassword=view.findViewById(R.id.etPassword);
        etLanguage=view.findViewById(R.id.etLanguage);
        imgBackPress=view.findViewById(R.id.imgBackPress);
        tv_edit_photo=view.findViewById(R.id.tv_edit_photo);
        LoginResponse loginResponse=SharedPref.getInstance(mContext).getData();
        if (loginResponse!=null)
        {
            try {
                etFirstName.setText(loginResponse.getFIRST_NAME());
                etLastName.setText(loginResponse.getLAST_NAME());
                etEmail.setText(loginResponse.getEMAIL());
                storeId=loginResponse.getSTORE_ID();
                etLanguage.setText(loginResponse.getLANGUAGE());
                etCellNo.setText(loginResponse.getPHONE());

                Picasso.get()
                        .load(loginResponse.getPROFILE_IMAGE()).placeholder(R.drawable.student)
                        .noFade()
                        .into(img_profile);

            }catch (Exception ex)
            {

            }
        }

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid())
                {
                    updateProfile(v);
                }
            }
        });
        imgBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        tv_edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

    }
    private void updateProfile(final View v)
    {
        progressDialog = new CustomProgressDialog(mContext, "Updating Your Profile...");
        progressDialog.show();
        //if everything is fine

        @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("fname", etFirstName.getText().toString());
        jsonParams.put("lname", etLastName.getText().toString());
        jsonParams.put("email",etEmail.getText().toString().trim());
        jsonParams.put("language",language);
        jsonParams.put("phone", etCellNo.getText().toString());
        jsonParams.put("password", etPassword.getText().toString());
        jsonParams.put("current_id", storeId);

        final ResponseObject responseObject=new ResponseObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.URL_UPDATE_PROFILE, new JSONObject(jsonParams),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responseObject.setMESSAGE(response.getString("MESSAGE"));
                            responseObject.setSTATUS(response.getBoolean("STATUS"));

                            LoginResponse response2=new LoginResponse();
                            response2.setSTORE_ID(response.getString("STORE_ID"));
                            response2.setSTATUS(response.getBoolean("STATUS"));
                            response2.setFIRST_NAME(response.getString("first_name"));
                            response2.setLAST_NAME(response.getString("last_name"));
                            response2.setEMAIL(response.getString("email"));
                            response2.setLANGUAGE(response.getString("LANGUAGE"));
                            response2.setPROFILE_IMAGE(response.getString("PROFILE_IMAGE"));
                            response2.setLATITUDE(response.getString("LATITUDE"));
                            response2.setLONGITUDE(response.getString("CATEGORY"));
                            response2.setLOCATION(response.getString("LOCATION"));
                            response2.setPHONE(response.getString("phone"));
                            response2.setPROFILE_IMAGE(response.getString("PROFILE_IMAGE"));

                            //session
                            SharedPref.getInstance(mContext).saveData(response2);
                         //   SharedPref.getInstance(mContext).saveData(loginResponse);

                            if (responseObject.getMESSAGE().equalsIgnoreCase("Profile Successfully Updated"))
                            {
                                responseObject.setMESSAGE(response.getString("MESSAGE"));
                                Snackbar.make(v,"Profile Updated Successfully", Snackbar.LENGTH_SHORT).show();
                            }
                            else {
                                progressDialog.dismiss();
                                General.dialog(mContext,responseObject.getMESSAGE());
                               // Snackbar.make(v,"Server Error", Snackbar.LENGTH_SHORT).show();
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
                        customProgressDialog.dismiss();
                       // Snackbar.make(v,"Server Error", Snackbar.LENGTH_SHORT).show();
                        General.dialog(mContext,"server error");
                        if (null != error.networkResponse) {
                            // mResponseListener.requestEndedWithError(error);
                        }
                    }
                });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    public boolean isValid() {
        boolean flag = true;
        if (etEmail.getText().toString().trim().length()==0) {
            etEmail.setError("Enter Email");
            flag = false;
        }
/*        else if (etPassword.getText().toString().trim().length()==0) {
            etPassword.setError("Enter Password");
            flag = false;
        }*/
        else if (etFirstName.getText().toString().trim().length()==0) {
            etFirstName.setError("Enter First Name");
            flag = false;
        }
        else if (etCellNo.getText().toString().trim().length()==0) {
            etCellNo.setError("Enter Cell Number");
            flag = false;
        }
/*        else if (etLanguage.getText().toString().trim().length()==0) {
            //etEmail.setError("Enter Password/Email");
            Toast.makeText(mContext, "Enter Language", Toast.LENGTH_SHORT).show();
            flag = false;
        }*/
        return flag;
    }
    public void pickImage()
    {
        /*Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
        // ******** code for crop image
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 100);
        i.putExtra("aspectY", 100);
        i.putExtra("outputX", 256);
        i.putExtra("outputY", 356);

        try {
            i.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), 0);
        }catch (ActivityNotFoundException ex){
            ex.printStackTrace();
        }

*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
/*        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();*/
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(mContext, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            intent.setClipData(ClipData.newRawUri("", photoURI));
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            img_profile.setImageBitmap(bitmap);
            cursor.close();
            updateProfilePhoto();
        } else {
            Toast.makeText(getActivity(), "Photo not selected", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    public String getPath(Uri uri) {
        ContentResolver resolver=getActivity().getContentResolver();
        Cursor returnCursor = resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
    private void loadProfile(String url,Bitmap bitmap) {
       // Log.d(TAG, "Image cache path: " + url);

  /*      Picasso.get()
                .load(url)
                .noFade()
                .into(img_profile);*/
        img_profile.setImageBitmap(bitmap);
      /*  GlideApp.with(this).load(url)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(mContext, android.R.color.transparent));*/
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void updateProfilePhoto() {
        customProgressDialog = new CustomProgressDialog(mContext, "Updating Profile Picture...");
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_UPDATE_PROFILE_PICTURE,
                new com.android.volley.Response.Listener<NetworkResponse>() {
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
                new com.android.volley.Response.ErrorListener() {
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
                jsonParams.put("current_id", storeId);
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
                    params.put("profileImage", new DataPart(imagename + ".jpg", byteArray));
                    //}
                }
                return params;
            }
        };
        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
