package com.hldj.hmyg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


@SuppressLint("Override")
public class StoreActivity_bak extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
