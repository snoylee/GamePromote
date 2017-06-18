package com.xygame.sg.utils;

import android.app.Dialog;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.HostDialog;
import com.xygame.sg.activity.commen.LoadingDialog;
import com.xygame.sg.activity.commen.NoButtonDialog;
import com.xygame.sg.activity.commen.OneButtonDialog;

/**
 * Created by minhua on 2015/11/10.
 */
public class ShowMsgDialog {
    public static void showHost(android.app.Activity activity,boolean cancelable) {
        final HostDialog dialog = new HostDialog(activity, "", R.style.dineDialog, null);
        dialog.setCancelable(cancelable);
        alert = dialog;
        dialog.show();
    }
    public static void showOne(android.app.Activity activity, String msg, boolean cancelable, ButtonOneListener buttonOneListener) {
        final OneButtonDialog dialog = new OneButtonDialog(activity, msg, R.style.dineDialog, buttonOneListener);
        dialog.setCancelable(cancelable);
        alert = dialog;
        dialog.show();
    }

    static Dialog alert;

    public static Dialog show(android.app.Activity activity, String msg, boolean cancelable) {
        NoButtonDialog dialog = new NoButtonDialog(activity, msg, R.style.dineDialog, new ButtonOneListener() {

            @Override
            public void confrimListener(Dialog dialog) {

            }
        });
        dialog.setCancelable(cancelable);
        alert = dialog;
        dialog.show();
        return dialog;
    }

    public static void showNoMsg(android.app.Activity activity, boolean cancelable) {
        LoadingDialog dialog = new LoadingDialog(activity,  R.style.dineDialog);
        dialog.setCancelable(cancelable);
        alert = dialog;
        dialog.show();
    }

    public static void cancel() {
        if (alert != null && alert.isShowing()) {
            alert.dismiss();
            alert.cancel();
            alert = null;
        }
    }

    public static boolean isShowing() {
        return alert != null && alert.isShowing();
    }
}
