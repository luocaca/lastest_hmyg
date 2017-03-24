package com.zzy.flowers.widget.popwin;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.ManagerPurchaseListActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class EditP4 extends ExtensionDataBottomPopwin {

	String id;
	private ManagerPurchaseListActivity activity;
	public EditP4(Context context, String str, int maxDay, String id, ManagerPurchaseListActivity activity) {
		super(context, str, maxDay);
		this.id = id;
		this.activity = activity;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleClickListener(EditText et) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && activity.getCurrentFocus() != null
				&& activity.getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
		}
		String day = et.getText().toString();
		purchaseRevoke(id, day);

	}

	private void purchaseRevoke(String id, String day) {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("days", day);
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/saveExtension",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						activity.fresh();
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {
						
							} else if ("6007".equals(code)) {

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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

}
