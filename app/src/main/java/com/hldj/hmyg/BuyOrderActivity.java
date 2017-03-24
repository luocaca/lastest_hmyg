package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.OrderGood;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class BuyOrderActivity extends NeedSwipeBackActivity {

	private Button button1;
	private Button button2;
	private Button button3;
	private ListView listView;
	private List<OrderGood> list;
	private static TextView price;
	public static int num = 0;
	public static Double pric = 0.0;
	private Adper adapter;
	private Button button4;
	private int pageSize = 20;
	private int pageIndex = 0;
	boolean getdata; // 避免刷新多出数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer_order);
		button1 = (Button) findViewById(R.id.button1);// 全选
		button2 = (Button) findViewById(R.id.button2);// 反选
		button3 = (Button) findViewById(R.id.button3);// 全不选
		button4 = (Button) findViewById(R.id.button4);// 删除
		listView = (ListView) findViewById(R.id.listveiw);// 价格

		price = (TextView) findViewById(R.id.price);
		list = new ArrayList<OrderGood>();
		// 赋值
		initData();
		// for (int i = 0; i < 60; i++) {
		// list.add(new OrderGood(i + "", false));
		// }
		// // 适配
		// adapter = new Adper(list, BuyOrderActivity.this);
		// listView.setAdapter(adapter);
		// 全选
		button1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				num = 0;
				pric = 0.0;

				for (int i = 0; i < list.size(); i++) {
					// 改变boolean
					list.get(i).setBo(true);
					// 如果为选中
					if (list.get(i).getBo()) {

						num++;
						pric += Double.parseDouble(list.get(i).getName());

					}
				}
				// 刷新
				adapter.notifyDataSetChanged();
				// 显示
				price.setText("一共选了" + num + "件," + "价格是" + pric + "元");

			}
		});
		// 反选
		button2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				num = 0;
				pric = 0.0;
				for (int i = 0; i < list.size(); i++) {
					// 改值
					if (list.get(i).getBo()) {
						list.get(i).setBo(false);
					} else {
						list.get(i).setBo(true);
					}
					// 刷新
					adapter.notifyDataSetChanged();
					// 如果为选中
					if (list.get(i).getBo()) {

						num++;
						pric += Double.parseDouble(list.get(i).getName());

					}
				}
				// 用TextView显示
				price.setText("一共选了" + num + "件," + "价格是" + pric + "元");
			}
		});
		// 全不选
		button3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				num = 0;
				pric = 0.0;
				for (int i = 0; i < list.size(); i++) {
					// 改值
					list.get(i).setBo(false);
					// 刷新
					adapter.notifyDataSetChanged();
					// 如果为选中
					if (list.get(i).getBo()) {
						num++;
						pric += Double.parseDouble(list.get(i).getName());
					}

				}
				price.setText("一共选了" + num + "件," + "价格是" + pric + "元");
			}
		});
		// 删除3
		button4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 获取list集合对应的迭代器
				Iterator it = list.iterator();
				while (it.hasNext()) {
					// 得到对应集合元素
					OrderGood g = (OrderGood) it.next();
					// 判断
					if (g.getBo()) {
						// 从集合中删除上一次next方法返回的元素
						it.remove();
					}
				}
				// 刷新
				adapter.notifyDataSetChanged();
				num = 0;
				pric = 0.0;
				// 显示
				price.setText("一共选了" + num + "件," + "价格是" + pric + "元");
			}
		});

	}

	
	public static void showprice() {
		price.setText("一共选了" + num + "件," + "价格是" + pric + "元");
	}
	
	
	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("status", "finished");
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/buyer/order/list",
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
								Toast.makeText(BuyOrderActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray_cartList = JsonGetInfo
											.getJsonArray(jsonObject2, "data");

									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										HashMap<String, Object> products_hash = new HashMap<String, Object>();
										products_hash.put("id", JsonGetInfo
												.getJsonString(jsonObject4,
														"id"));
										products_hash.put("price", JsonGetInfo
												.getJsonDouble(jsonObject4,
														"price"));
										list.add(new OrderGood(JsonGetInfo
												.getJsonString(jsonObject4,
														"price"), false));
										if (adapter != null) {
											adapter.notifyDataSetChanged();
										}
									}
									if (adapter == null) {
										adapter = new Adper(list,
												BuyOrderActivity.this);
										listView.setAdapter(adapter);
									}
									pageIndex++;

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
						Toast.makeText(BuyOrderActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

}
