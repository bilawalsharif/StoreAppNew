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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chofem.store.R;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SelectLanguageFragment extends Fragment {
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

    public SelectLanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select_language, container,
                false);
       // widgets();
        //showProduct();
        return view;
    }

}