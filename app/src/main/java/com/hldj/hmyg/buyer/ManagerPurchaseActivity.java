package com.hldj.hmyg.buyer;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buy.adapter.ManagerPurchaseAdapter;
import com.hldj.hmyg.buy.adapter.ManagerQuoteAdapter;
import com.hldj.hmyg.buy.bean.ManagerPurchase;
import com.hldj.hmyg.saler.ManagerQuoteActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

@SuppressLint("NewApi")
public class ManagerPurchaseActivity extends NeedSwipeBackActivity implements
		OnClickListener {

	private ArrayList<ManagerPurchase> gd_datas = new ArrayList<ManagerPurchase>();
	private GridView gd_00;
	private ImageView btn_back;
	int expired = 0;
	int backed = 0;
	int unaudit = 0;
	int closed = 0;
	int published = 0;
	int unsubmit = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager_puchase);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		gd_00 = (GridView) findViewById(R.id.gd_00);

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("Quote", "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/statusCount",
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

								JSONObject countMap = JsonGetInfo
										.getJSONObject(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"), "countMap");
								expired = JsonGetInfo.getJsonInt(countMap,
										"expired");
								backed = JsonGetInfo.getJsonInt(countMap,
										"backed");
								unaudit = JsonGetInfo.getJsonInt(countMap,
										"unaudit");
								closed = JsonGetInfo.getJsonInt(countMap,
										"closed");
								published = JsonGetInfo.getJsonInt(countMap,
										"published");
								unsubmit = JsonGetInfo.getJsonInt(countMap,
										"unsubmit");

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
						gd_datas.add(new ManagerPurchase("未提交", "" + unsubmit,
								R.drawable.maijia_caigouguangl_weitijiao, "",
								"unsubmit"));
						gd_datas.add(new ManagerPurchase("审核中", "" + unaudit,
								R.drawable.maijia_caigouguangl_daishenhei, "",
								"unaudit"));
						gd_datas.add(new ManagerPurchase("已发布", "" + published,
								R.drawable.maijia_caigouguangl_yifabu, "",
								"published"));
						gd_datas.add(new ManagerPurchase("被退回", "" + backed,
								R.drawable.maijia_caigouguangl_beituihui, "",
								"backed"));
						gd_datas.add(new ManagerPurchase("已过期", "" + expired,
								R.drawable.maijia_caigouguangl_yiguoqi, "",
								"expired"));
						gd_datas.add(new ManagerPurchase("已关闭", "" + closed,
								R.drawable.maijia_caigouguangl_yiguanbi, "",
								"closed"));
						if (gd_datas.size() > 0) {
							ManagerPurchaseAdapter myadapter = new ManagerPurchaseAdapter(
									ManagerPurchaseActivity.this, gd_datas);
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
