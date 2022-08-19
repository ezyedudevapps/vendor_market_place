package com.ezyedu.vendor.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.ezyedu.vendor.R;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void StartLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

   public void DismisDialog()
    {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
