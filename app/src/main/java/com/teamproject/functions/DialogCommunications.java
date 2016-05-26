package com.teamproject.functions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by 008M on 2016-05-20.
 */
public class DialogCommunications {
    private final Context context;
    public DialogCommunications(Context c){
        this.context = c;
    }

    public void one(String com, String mes, final int close){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(com);
        alertDialogBuilder
                .setMessage(mes)
                .setCancelable(false)
                .setNeutralButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                        if (close==1){
                            ((Activity) context).finish();
                        }
                    }});
        AlertDialog alertDialog1 = alertDialogBuilder.create();
        alertDialog1.show();
    }
    public void alertDialog(String title, String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
