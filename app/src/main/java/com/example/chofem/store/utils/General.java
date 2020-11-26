package com.example.chofem.store.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.EventLogTags;
import android.view.View;

import androidx.appcompat.app.AlertDialog;


public class General {

    public static void dialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Store App");
        builder.setMessage(msg);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

}
