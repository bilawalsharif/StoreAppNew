package com.example.chofem.store.connection;

import com.example.chofem.store.responses.GetStoreProductResponse;
import com.example.chofem.store.responses.ResponseObject;
import com.example.chofem.store.responses.SignUpResponse;
import com.google.gson.JsonObject;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST("store/login.php")
    Call<ResponseObject> login(@Body HashMap<String,String> loginRequest);


    @FormUrlEncoded
    @POST("api/store/login.php")
    public void insertUser(
            @Field("password") String password,
            @Field("email") String email,
            Callback<ResponseObject> callback);

    @POST("/api/store/register")
    Call<SignUpResponse> register();

    @POST("api/store/display_product.php")
    Call<GetStoreProductResponse> showProducts(@Body Map<String,String> map);
    @POST("api/store/delete_product.php")
    Call<ResponseObject> deleteProduct(@Body Map<String,String> map);


}
