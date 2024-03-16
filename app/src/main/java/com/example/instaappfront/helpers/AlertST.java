package com.example.instaappfront.helpers;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class AlertST {
    public static AlertDialog BuildErrorAlert(Context context, String title, String message){
        return GetBuilder(context, title, message)
                .setPositiveButton("OK", ((dialogInterface, i) -> {}))
                .create();
    }

    public static AlertDialog.Builder GetBuilder(Context context, String title, String message){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);
    }

    public static AlertDialog.Builder GetCustomBuilder(Context context, String title){
        return new AlertDialog.Builder(context)
                .setTitle(title);
    }

}
