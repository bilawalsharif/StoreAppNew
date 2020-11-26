package com.example.chofem.store.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chofem.store.R;
import com.example.chofem.store.Ui.UpdateProductActivity;
import com.example.chofem.store.adapters.ShowProdcutsAdapter;
import com.example.chofem.store.connection.APIInterface;
import com.example.chofem.store.dialog.MapDialogFragment;
import com.example.chofem.store.interfaces.ProductListner;
import com.example.chofem.store.responses.GetStoreProductResponse;
import com.example.chofem.store.responses.LoginResponse;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.General;
import com.example.chofem.store.utils.SharedPref;
import com.example.storeapp.connection.APIClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyStockFragment extends Fragment implements ProductListner {
    private View view;
    private RecyclerView rvDisplayProducts;
    private ShowProdcutsAdapter showProdcutsAdapter;
    private CustomProgressDialog customProgressDialog;
    private  MapDialogFragment mapDialogFragment;
    private Context mContext;
    private List<GetStoreProductResponse.PRODUCT> productList=new ArrayList<>();
    public MyStockFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_stock, container,
                false);
        widgets();
        showProducts();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (SharedPref.getInstance(getActivity()).getFirstTimeLogin())
        {
            SharedPref.getInstance(getActivity()).setFirstTimeLogin(false);
            mapDialogFragment = new MapDialogFragment(getActivity());
            mapDialogFragment.show(getFragmentManager(), "example");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapDialogFragment!=null)
        if (mapDialogFragment.isAdded())
        mapDialogFragment.onDestroy();
    }
    private void widgets() {
        mContext=getActivity();
        rvDisplayProducts=view.findViewById(R.id.rvDisplayProducts);
    }
    public void showProducts() {
        customProgressDialog = new CustomProgressDialog(getActivity(), "Loading...");
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();

        LoginResponse loginResponse=SharedPref.getInstance(mContext).getData();
        Map<String, String> data = new HashMap<>();
        data.put("storeAdmin_id", loginResponse.getSTORE_ID());

        //LoginRequest  loginRequest=new LoginRequest(email,pass);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface.showProducts(data).enqueue(new Callback<GetStoreProductResponse>() {
            @Override
            public void onResponse(Call<GetStoreProductResponse> call, Response<GetStoreProductResponse> response) {
                if (response.isSuccessful()) {
                    customProgressDialog.dismiss();
                    GetStoreProductResponse response1 = response.body();
                    if (response1.getSTATUS()) {
                        setData(response1);
                        //Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(Login_Activity.this,DefaultDrawerActivity.class));
                    }
                    else {
                        General.dialog(mContext,"server error.");
                    }
                }
            }
            @Override
            public void onFailure(Call<GetStoreProductResponse> call, Throwable t) {
                customProgressDialog.dismiss();
                General.dialog(mContext,"server error.");
              //  Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setData(GetStoreProductResponse response) {
        productList=response.getPRODUCT();
        showProdcutsAdapter=new ShowProdcutsAdapter(getActivity(),productList).setListner(this);
       // rvDisplayProducts.setLayoutManager(new GridLayoutManager(getActivity(),3));
       rvDisplayProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDisplayProducts.setAdapter(showProdcutsAdapter);
    }

    @Override
    public void deleteProduct(final int position, final GetStoreProductResponse.PRODUCT getStoreProductResponse) {
                    new AlertDialog.Builder(mContext)
                            .setTitle(getString(R.string.app_name))
                            .setMessage("Do You want to Delete this Product?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                //   Snackbar.make(v, "Empty Leg Removed", Snackbar.LENGTH_SHORT).show();
                                    deleteProductCall(position,getStoreProductResponse.getPId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
    @Override
    public void updateProduct(int position, GetStoreProductResponse.PRODUCT getStoreProductResponse) {
        Intent obj=new Intent(mContext, UpdateProductActivity.class);
        obj.putExtra("position",position);
        obj.putExtra("storeData", (Serializable) getStoreProductResponse);

        //Gson gson = new Gson();
       // String myJson = gson.toJson(getStoreProductResponse);
     //   obj.putExtra("myjson", myJson);
        startActivity(obj);

    }
    public void deleteProductCall(final int position, String pId)
    {
        customProgressDialog = new CustomProgressDialog(getActivity(), "Deleting Product...");
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
        LoginResponse loginResponse=SharedPref.getInstance(mContext).getData();

        Map<String, String> data = new HashMap<>();
        data.put("store", loginResponse.getSTORE_ID());
        data.put("product", pId);

        //LoginRequest  loginRequest=new LoginRequest(email,pass);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface.deleteProduct(data).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.isSuccessful()) {
                    customProgressDialog.dismiss();
                    ResponseObject responseObject = response.body();
                    General.dialog(mContext,responseObject.getMESSAGE());
                    if (responseObject.STATUS) {
                        productList.remove(position);
                        showProdcutsAdapter.notifyItemRemoved(position);
                        showProdcutsAdapter.notifyItemChanged(position);
                        showProdcutsAdapter.notifyItemRangeChanged(position, productList.size());
                        showProdcutsAdapter.notifyDataSetChanged();
                        //setData(loginResonse);
                        //Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(Login_Activity.this,DefaultDrawerActivity.class));
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                General.dialog(mContext,"server error.");
                customProgressDialog.dismiss();
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

}