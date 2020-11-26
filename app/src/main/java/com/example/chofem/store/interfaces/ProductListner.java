package com.example.chofem.store.interfaces;


import com.example.chofem.store.responses.GetStoreProductResponse;

public interface ProductListner {
    void deleteProduct(int position, GetStoreProductResponse.PRODUCT getStoreProductResponse);
    void updateProduct(int position, GetStoreProductResponse.PRODUCT getStoreProductResponse);
}
