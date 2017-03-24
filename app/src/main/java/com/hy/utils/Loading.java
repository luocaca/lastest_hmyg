
package com.hy.utils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Loading {
    private static final String TAG = "ToastUtils";

    private WeakReference<ProgressDialog> mProgress;

    public Loading(Context context, String str) {
        if (mProgress == null) {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setIndeterminate(false);
            dialog.setMessage(str);
            mProgress = new WeakReference<ProgressDialog>(dialog);
        }
    }

    public Loading(Context context) {
        if (mProgress == null) {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setIndeterminate(false);
            dialog.setMessage("加载中...");
            mProgress = new WeakReference<ProgressDialog>(dialog);
        }
    }

    public boolean isRunning() {
        final ProgressDialog progressDialog = mProgress.get();
        if (progressDialog == null)
            return false;
        return progressDialog.isShowing();
    }

    public void cancel() {
        final ProgressDialog progressDialog = mProgress.get();
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            android.util.Log.w(TAG, "Ignoring exception while dismissing dialog: " + e.toString());
        }
    }

    public void showToastAlong() {
        final ProgressDialog progressDialog = mProgress.get();
        if (progressDialog == null)
            return;
        progressDialog.show();
    }

    public void showToastAlong(long delay) {
        showToastAlong();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Loading.this.cancel();
            }
        };
        timer.schedule(task, delay);
    }

    public static void showToast(Context context, String str) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public static void showLongToast(Context context, String str) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
