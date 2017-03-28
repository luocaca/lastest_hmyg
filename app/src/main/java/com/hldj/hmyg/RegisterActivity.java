package com.hldj.hmyg;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.loginjudge.LoginJudge;

public class RegisterActivity extends NeedSwipeBackActivity {

    private static final String TAG = "RegisterActivity";
    /**
     */
    private ImageView btn_back;
    private TextView register;
    private EditText et_phone;
    private EditText et_passward;
    private EditText et_code;
    private Editor e;
    private ImageButton btn_clear_password;
    private ImageButton btn_clear_num;
    private String istab_c;
    private Button btn_get_code;
    private String phString;


    int sdk_version = 11 << 19;

    protected void hideBottomUIMenu() {//隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT == sdk_version) {

            View decorView = getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//        View decorview = getWindow().getDecorView();
//        if (Build.VERSION.SDK_INT >= 21) {//5.0以上的系统支持
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//表示让应用主题内容占据系统状态栏的空间
//            decorview.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.parseColor("#00000000"));//设置状态栏颜色为透明
//        }
//        hideBottomUIMenu();


//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//			localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_register);
//				linearLayout.setFitsSystemWindows(true);
//				linearLayout.setClipToPadding(false);
//
//				/**
//				 *  //将侧边栏顶部延伸至status bar
//				 mDrawerLayout.setFitsSystemWindows(true);
//				 //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
//				 mDrawerLayout.setClipToPadding(false);
//				 */
//
//			}
//		}

        time = new TimeCount(60000, 1000);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        register = (TextView) findViewById(R.id.register);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_passward = (EditText) findViewById(R.id.et_passward);
        et_code = (EditText) findViewById(R.id.et_code);
        et_inventer = (EditText) findViewById(R.id.et_inventer);
        btn_clear_num = (ImageButton) findViewById(R.id.btn_clear_num);
        btn_clear_password = (ImageButton) findViewById(R.id.btn_clear_password);
        btn_get_code = (Button) findViewById(R.id.btn_get_code);
        login = (TextView) findViewById(R.id.login);

        e = MyApplication.Userinfo.edit();

        et_phone.addTextChangedListener(watcher_num);
        et_passward.addTextChangedListener(watcher_password);
        MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
        btn_clear_num.setOnClickListener(multipleClickProcess);
        btn_clear_password.setOnClickListener(multipleClickProcess);
        btn_get_code.setOnClickListener(multipleClickProcess);
        btn_back.setOnClickListener(multipleClickProcess);
        register.setOnClickListener(multipleClickProcess);
        login.setOnClickListener(multipleClickProcess);
    }

    TextWatcher watcher_num = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            if (s.length() > 0) {
                btn_clear_num.setVisibility(View.VISIBLE);
                if (et_phone.getText().toString().length() == 11
                        && et_passward.getText().toString().length() > 5) {
                    register.setEnabled(true);
                    register.setTextColor(getResources()
                            .getColor(R.color.white));
                }
            } else {
                btn_clear_num.setVisibility(View.GONE);
                register.setEnabled(false);
                register.setTextColor(getResources().getColor(
                        R.color.main_color));

            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };
    TextWatcher watcher_password = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            if (s.length() > 0) {
                btn_clear_password.setVisibility(View.VISIBLE);
                if (et_phone.getText().toString().length() == 11
                        && et_passward.getText().toString().length() > 5) {
                    register.setEnabled(true);
                    register.setTextColor(getResources()
                            .getColor(R.color.white));
                }
            } else {
                btn_clear_password.setVisibility(View.GONE);
                register.setEnabled(false);
                register.setTextColor(getResources().getColor(
                        R.color.main_color));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };
    private TimeCount time;
    private EditText et_inventer;
    private TextView login;

    @Override
    public void onBackPressed() {
        if (LoginJudge.isTabc) {
            LoginJudge.isTabc = false;
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
        finish();
        overridePendingTransition_back();
//		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public class MultipleClickProcess implements OnClickListener {
        private boolean flag = true;

        private synchronized void setFlag() {
            flag = false;
        }

        public void onClick(View view) {
            if (flag) {
                switch (view.getId()) {
                    case R.id.btn_back:
                        onBackPressed();
                        break;
                    case R.id.btn_clear_num:
                        et_phone.setText("");
                        break;
                    case R.id.btn_clear_password:
                        et_passward.setText("");
                        break;
                    case R.id.login:
                        Intent toLoginActivity = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        startActivityForResult(toLoginActivity, 4);
//					overridePendingTransition(R.anim.slide_in_left,
//							R.anim.slide_out_right);
                        overridePendingTransition_open();
                        finish();
                        break;
                    case R.id.btn_get_code:

                        if (!TextUtils.isEmpty(et_phone.getText().toString())) {
                            if (et_phone.getText().toString().length() < 11 && !et_phone.getText().toString().startsWith("1")) {
                                Toast.makeText(RegisterActivity.this, "手机格式有问题，请检查后重试",
                                        Toast.LENGTH_SHORT).show();
                            }
                            phString = et_phone.getText().toString();
                            time.start();
                            Checkphone();
                        } else {
                            Toast.makeText(RegisterActivity.this, "电话不能为空",
                                    Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.register:
                        // 判断是否为空
                        if ("".equals(et_code.getText().toString())
                                || "".equals(et_phone.getText().toString())
                                || "".equals(et_passward.getText().toString())) {
                            Toast.makeText(RegisterActivity.this,
                                    "以上内容都需要填写！请检查...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Reg();

                        break;

                    default:
                        break;
                }
                setFlag();
                // do some things
                new TimeThread().start();
            }
        }

        /**
         * 计时线程（防止在一定时间段内重复点击按钮）
         */
        private class TimeThread extends Thread {
            public void run() {
                try {
                    Thread.sleep(Data.loading_time);
                    flag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void Checkphone() {
        // TODO Auto-generated method stub
        FinalHttp finalHttp = new FinalHttp();
        GetServerUrl.addHeaders(finalHttp, false);
        AjaxParams params = new AjaxParams();
        params.put("phone", phString);
        //    http://hmeg.cn:93/common/getSmsCode/&phone=17074990702
        finalHttp.post(GetServerUrl.getUrl() + "common/getSmsCode", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        try {
                            JSONObject jsonObject = new JSONObject(t.toString());
                            String code = JsonGetInfo.getJsonString(jsonObject,
                                    "code");
                            String msg = JsonGetInfo.getJsonString(jsonObject,
                                    "msg");
                            if (!"".equals(msg)) {
                                Toast.makeText(RegisterActivity.this, msg,
                                        Toast.LENGTH_SHORT).show();
                            }
                            if ("1".equals(code)) {
                                Toast.makeText(RegisterActivity.this,
                                        "验证码已经发送", Toast.LENGTH_SHORT).show();
                            } else {

                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(RegisterActivity.this, R.string.error_net,
                                Toast.LENGTH_SHORT).show();
                        super.onFailure(t, errorNo, strMsg);
                    }

                });
    }

    private void Reg() {

        FinalHttp finalHttp = new FinalHttp();
        GetServerUrl.addHeaders(finalHttp, false);
        AjaxParams params = new AjaxParams();
        params.put("phone", phString);
        params.put("password", et_passward.getText().toString());
        params.put("recommendPhone", et_inventer.getText().toString());
        params.put("smsCode", et_code.getText().toString());
        String str = et_code.getText().toString();
        String str1 = et_code.getText().toString();
        String str2 = et_code.getText().toString();
        Log.e(TAG, "this url is :" + GetServerUrl.getUrl() + "user/register" + params.toString());
        finalHttp.post(GetServerUrl.getUrl() + "user/register", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        String json = t.toString();

                        try {
                            JSONObject jsonObject = new JSONObject(t.toString());
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("msg");
                            if (!"".equals(msg)) {
                                Toast.makeText(RegisterActivity.this, msg,
                                        Toast.LENGTH_SHORT).show();
                            }
                            if ("1".equals(code)) {
                                String userId = jsonObject
                                        .getJSONObject("data").getString(
                                                "userId");
                                onBackPressed();
                            } else if ("1006".equals(code)) {
                                // 已注册
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        super.onSuccess(t);
                    }


                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Log.e(TAG, "is error occr");
                        super.onFailure(t, errorNo, strMsg);
                    }
                });


    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_get_code.setClickable(false);
            btn_get_code.setText(millisUntilFinished / 1000 + "S");
        }

        @Override
        public void onFinish() {
            btn_get_code.setText("重新获取");
            btn_get_code.setClickable(true);
        }
    }


}
