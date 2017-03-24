package com.hldj.hmyg.saler;

import java.util.ArrayList;
import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class ChooseFirstTypeActivity extends NeedSwipeBackActivity {

	private ListView listView;
	ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_fir_type);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		listView = (ListView) findViewById(R.id.listView);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		initDataGetFirstType();
		btn_back.setOnClickListener(multipleClickProcess);
	}

	private void initDataGetFirstType() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "seedlingType/getFirstType",
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
							if (!"".equals(msg)) {
							}
							if ("1".equals(code)) {
								JSONArray typeList = jsonObject.getJSONObject(
										"data").getJSONArray("typeList");
								for (int i = 0; i < typeList.length(); i++) {
									JSONObject jsonObject2 = typeList
											.getJSONObject(i);
									HashMap<String, Object> hMap = new HashMap<String, Object>();
									hMap.put("id", JsonGetInfo.getJsonString(
											jsonObject2, "id"));
									hMap.put("isNewRecord", JsonGetInfo
											.getJsonBoolean(jsonObject2,
													"isNewRecord"));
									hMap.put("createBy", JsonGetInfo
											.getJsonString(jsonObject2,
													"createBy"));
									hMap.put("createDate", JsonGetInfo
											.getJsonString(jsonObject2,
													"createDate"));
									hMap.put("updateBy", JsonGetInfo
											.getJsonString(jsonObject2,
													"updateBy"));
									hMap.put("updateDate", JsonGetInfo
											.getJsonString(jsonObject2,
													"updateDate"));
									hMap.put("delFlag", JsonGetInfo
											.getJsonString(jsonObject2,
													"delFlag"));
									hMap.put("name", JsonGetInfo.getJsonString(
											jsonObject2, "name"));
									hMap.put("aliasName", JsonGetInfo
											.getJsonString(jsonObject2,
													"aliasName"));
									hMap.put("parentId", JsonGetInfo
											.getJsonString(jsonObject2,
													"parentId"));
									hMap.put("level", JsonGetInfo.getJsonInt(
											jsonObject2, "level"));
									hMap.put("firstPinyin", JsonGetInfo
											.getJsonString(jsonObject2,
													"firstPinyin"));
									hMap.put("fullPinyin", JsonGetInfo
											.getJsonString(jsonObject2,
													"fullPinyin"));
									hMap.put("seedlingParams", JsonGetInfo
											.getJsonString(jsonObject2,
													"seedlingParams"));
									hMap.put("sort", JsonGetInfo.getJsonInt(
											jsonObject2, "sort"));
									hMap.put("isTop",
											JsonGetInfo.getJsonString(
													jsonObject2, "isTop"));
									hMap.put("url", JsonGetInfo.getJsonString(
											jsonObject2, "url"));
									hMap.put("ossThumbnailImagePath",
											JsonGetInfo.getJsonString(
													jsonObject2,
													"ossThumbnailImagePath"));
									datas.add(hMap);
								}

								if (datas.size() > 0) {
									FirstTypeAdapter ftadapter = new FirstTypeAdapter(
											ChooseFirstTypeActivity.this, datas);
									listView.setAdapter(ftadapter);
								}
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
						Toast.makeText(ChooseFirstTypeActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

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

	public class FirstTypeAdapter extends BaseAdapter {
		private static final String TAG = "FirstTypeAdapter";

		private ArrayList<HashMap<String, Object>> data = null;
		private Context context = null;
		private FinalBitmap fb;
		private LayoutParams l_params;

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		public FirstTypeAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.no_image_show);
			l_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			WindowManager wm = ((Activity) context).getWindowManager();
			l_params.height = (int) (wm.getDefaultDisplay().getWidth() * 9 / 20);
		}

		@Override
		public int getCount() {
			return this.data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return this.data.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.list_item_first_type, null);
			TextView title = (TextView) inflate.findViewById(R.id.title);
			title.setText(data.get(position).get("name").toString());
			inflate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("firstSeedlingTypeName", data.get(position)
							.get("name").toString());
					intent.putExtra("seedlingParams",
							data.get(position).get("seedlingParams").toString());
					intent.putExtra("firstSeedlingTypeId", data.get(position)
							.get("id").toString());
					setResult(1, intent);
					finish();
				}
			});

			return inflate;
		}

		public void notify(ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			notifyDataSetChanged();
		}

	}

}
