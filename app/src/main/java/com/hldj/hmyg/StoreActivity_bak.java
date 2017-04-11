package com.hldj.hmyg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hldj.hmyg.util.D;


public class StoreActivity_bak extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        D.e("hellwo world");
        D.e("hellwo world");
        D.e("hellwo world");
        setContentView(R.layout.activity_store_bak);


    }

    /**
     * 快速跳转本届面
     *
     * @param context
     * @param code
     */
    public static void start2StoreActivity(Context context, String code) {

        Intent intent = new Intent(context, StoreActivity_bak.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

}
