package com.example.chofem.store.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chofem.store.R;


public class Orders_Status_Fragment extends Fragment {
    private View view;
    private Button btnLogin;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.order_status_layout, container,
                false);
        widgets();
        return view;
    }

    private void widgets() {

    }
}
