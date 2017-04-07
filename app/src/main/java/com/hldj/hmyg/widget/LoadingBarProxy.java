package com.hldj.hmyg.widget;

import android.app.Activity;
import android.content.Context;

import com.hldj.hmyg.R;

public class LoadingBarProxy {

    private Context context;
    private LoadingBar loadingBar;

    public LoadingBarProxy(Context context) {
        this.context = context;
        this.loadingBar = new LoadingBar(context);
    }

    /**
     * 显示loading对话框
     */
    public void showLoadingDialog() {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            loadingBar.setMessage("加载中…");
            loadingBar.setSpinnerType(0);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }
    }

    /**
     * 取消loading对话框 msg
     */
    public void dismissWithFailure(String msg) {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            // loadingBar.ivProgressSpinner.setImageResource(R.drawable.success1);
            // loadingBar.dismissWithSuccess("OK");
            loadingBar.dismissWithFailure(msg);
        }
    }

    /**
     * 取消loading对话框 ,我的团队中用
     *
     * @author lulu
     *
     */
    public void dismissWithFailure(String msg, int drawable) {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            // loadingBar.ivProgressSpinner.setImageResource(R.drawable.faild_white);
            // loadingBar.dismissWithSuccess("OK");
            loadingBar.dismissWithFailureTeam(msg, drawable);
        }
    }

    public void dissmissWithNull() {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            loadingBar.dismiss();
        }
    }

    public void dismissWithSuccess() {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            loadingBar.ivProgressSpinner.setImageResource(R.drawable.success);
            loadingBar.dismissWithSuccess("");
        }
    }

    public void dismissWithSuccess(String text) {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            loadingBar.ivProgressSpinner.setImageResource(R.drawable.success);
            loadingBar.dismissWithSuccess(text);
        }
    }

    public void dismissWithSuccess(String text, int sleepTime) {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            loadingBar.ivProgressSpinner.setImageResource(R.drawable.success);
            loadingBar.dismissWithSuccess(text, sleepTime);
        }
    }

    /**
     * 取消loading对话框 msg,带显示延迟时间
     */
    public void dismissWithFailureDelay(String msg, int sleepTime) {
        if (loadingBar != null && context != null
                && !((Activity) context).isFinishing()) {
            // loadingBar.ivProgressSpinner.setImageResource(R.drawable.success1);
            // loadingBar.dismissWithSuccess("OK");
            loadingBar.dismissWithFailure(msg, sleepTime);
        }
    }
}
