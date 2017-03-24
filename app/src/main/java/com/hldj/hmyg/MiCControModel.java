package com.hldj.hmyg;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * 文件名：MiCControModel
 * 描  述：前端与js交互模型
 * 作  者：Created by CCXiong on 16/8/1 16:11
 */
public class MiCControModel {
    private Context mContxt;

    private IJsControllListener jsListener;

    public MiCControModel(Context mContxt) {
        this.mContxt = mContxt;
    }

    /**
     * @param miCStoreId 米车门店id
     * @describe 打开相机 之前在js端不能收到返回的字符串是因为我在mainactivity使用了子线程更新view
     */
    @JavascriptInterface
    public void callback() {
        //前端做对应操作
        Toast.makeText(mContxt, "从js传过来的值为", Toast.LENGTH_SHORT).show();
        if(jsListener!=null)
        {
            jsListener.callback();
        }
    }

    /**
     * @param miCStoreId 米车门店id
     * @describe 得到位置信息, 比如经纬度
     */
    @JavascriptInterface
    public void getTheLocation(String miCStoreId) {
        //前端做对应操作
    }

    /**
     * @param
     * @describe 返回是否为米车APP
     */
    @JavascriptInterface
    public String isMiCheApp(){
        return "yes";
    }


    public String getToken(){
        return "dddddaf";
    }

    public interface IJsControllListener{
        void callback();
    }

    public void setOnJsControlListener(IJsControllListener jsListener){
        this.jsListener = jsListener;
    }

    @JavascriptInterface
    public void getHtml(String html){
        Log.d("html","html="+html);
    }
}  