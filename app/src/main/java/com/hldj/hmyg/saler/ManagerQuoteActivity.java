package com.hldj.hmyg.saler;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buy.adapter.ManagerQuoteAdapter;
import com.hldj.hmyg.buy.bean.ManagerPurchase;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

@SuppressLint("NewApi")
public class ManagerQuoteActivity extends NeedSwipeBackActivity implements OnClickListener {

	private ArrayList<ManagerPurchase> gd_datas = new ArrayList<ManagerPurchase>();
	private ArrayList<ManagerPurchase> gd_datas_has_tag = new ArrayList<ManagerPurchase>();
	private GridView gd_00;
	private ImageView btn_back;
	private int invalid=0;
	private int backed=0;
	private int unaudit=0;
	private int quoted=0;
	private int unsubmit=0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager_quote);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		gd_00 = (GridView) findViewById(R.id.gd_00);

		

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("Quote", "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/statusCount",
				params, new AjaxCallBack<Object>() {

			
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if ("1".equals(code)) {
								
								JSONObject countMap = JsonGetInfo.getJSONObject(JsonGetInfo.getJSONObject(jsonObject, "data"), "countMap");
								invalid = JsonGetInfo.getJsonInt(countMap, "invalid");
								backed = JsonGetInfo.getJsonInt(countMap, "backed");
								unaudit = JsonGetInfo.getJsonInt(countMap, "unaudit");
								quoted = JsonGetInfo.getJsonInt(countMap, "quoted");
								unsubmit = JsonGetInfo.getJsonInt(countMap, "unsubmit");
								
								init();
								
							} else {
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					public void init() {
						gd_datas.add(new ManagerPurchase("全部报价", "",
								R.drawable.baojiaguangli_quanbu, "", ""));
						gd_datas.add(new ManagerPurchase("未提交", ""+unsubmit,
								R.drawable.baojiaguangli_weitijiao, "", "unsubmit"));
						gd_datas.add(new ManagerPurchase("审核中", ""+unaudit,
								R.drawable.baojiaguangli_shenhezhong, "", "unaudit"));
						gd_datas.add(new ManagerPurchase("已报价", ""+quoted,
								R.drawable.baojiaguangli_yibaojia, "", "quoted"));
						gd_datas.add(new ManagerPurchase("被退回", ""+backed,
								R.drawable.baojiaguangli_beituihui, "", "backed"));
						gd_datas.add(new ManagerPurchase("已失效", ""+invalid,
								R.drawable.baojiaguangli_yishixiao, "", "invalid"));
						if (gd_datas.size() > 0) {
							ManagerQuoteAdapter myadapter = new ManagerQuoteAdapter(
									ManagerQuoteActivity.this, gd_datas);
							gd_00.setAdapter(myadapter);
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						init();
						super.onFailure(t, errorNo, strMsg);
					}

				});

		
		btn_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
		default:
			break;
		}
	}

}
