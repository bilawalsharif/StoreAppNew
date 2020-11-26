package com.example.chofem.store.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.chofem.store.R;


public class CustomProgressDialog extends ProgressDialog {
    Context context;
    String msg;

    public CustomProgressDialog(Context context, String message) {
        super(context);
        this.context = context;
        this.msg = message;
    }
    public void show() {
        super.show();
        setContentView(R.layout.custom_progress);
       // (TextView).findViewById(R.id.txtProgress);
       // View view = View.inflate(context, R.layout.custom_progress, null);
        TextView txtProgress = findViewById(R.id.txtProgress);
        txtProgress.setText(msg);
    }

    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }
}
