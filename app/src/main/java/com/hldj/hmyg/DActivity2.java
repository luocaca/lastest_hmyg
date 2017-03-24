package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.fengg.app.lcb.adapter.ProductAdapter;
import net.fengg.app.lcb.adapter.StoreAdapter;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Product;
import com.hldj.hmyg.bean.Store;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class DActivity2 extends Activity implements IXListViewListener,
		OnClickListener, OnItemClickListener {
	private XListView xListView;

	boolean getdata = true; // 避免刷新多出数据
	private StoreAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	ArrayList<Store> list = new ArrayList<Store>();
	private CheckBox cb_select_all;
	private TextView tv_amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_d);
		cb_select_all = (CheckBox) findViewById(R.id.cb_select_all);
		tv_amount = (TextView) findViewById(R.id.tv_amount);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		// initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_delete:
			delSelect();
			break;
		case R.id.cb_select_all:
			selectAll();
			break;

		default:
			break;
		}

	}

	public void delSelect() {
		ListIterator<Boolean> storeB = listAdapter.getSelect().listIterator();
		ListIterator<Store> storeL = list.listIterator();
		ListIterator<ProductAdapter> adapterL = listAdapter.getPAdapterList()
				.listIterator();
		while (storeB.hasNext()) {
			boolean it = storeB.next();
			Store store = storeL.next();
			ProductAdapter pAdapter = adapterL.next();
			ListIterator<Boolean> productB = pAdapter.getSelect()
					.listIterator();
			ListIterator<Product> productL = store.getProducts().listIterator();
			if (it) {
				while (productB.hasNext()) {
					productB.next();
					productB.remove();
					productL.next();
					productL.remove();
				}
				storeB.remove();
				storeL.remove();
				adapterL.remove();
			} else {
				while (productB.hasNext()) {
					productL.next();
					if (productB.next()) {
						productB.remove();
						productL.remove();
					}
				}
			}
		}
		updateAmount();
		listAdapter.notifyDataSetChanged();
		cb_select_all.setChecked(false);
	}

	public void selectAll() {
		boolean flag = cb_select_all.isChecked();
		for (int i = 0; i < listAdapter.getSelect().size(); i++) {
			listAdapter.getSelect().set(i, flag);
			for (int j = 0; j < listAdapter.getPAdapter(i).getSelect().size(); j++) {
				listAdapter.getPAdapter(i).getSelect().set(j, flag);
			}
		}
		updateAmount();
		listAdapter.notifyDataSetChanged();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/cart/list", params,
				new AjaxCallBack<Object>() {

					@SuppressWarnings("unchecked")
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
								Toast.makeText(DActivity2.this, msg,
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
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									if (jsonArray.length() > 0) {

										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject3 = jsonArray
													.getJSONObject(i);
											String cityName1 = JsonGetInfo
													.getJsonString(jsonObject3,
															"cityName");
											String cityCode = JsonGetInfo
													.getJsonString(jsonObject3,
															"cityCode");
											Double totalPrice = JsonGetInfo
													.getJsonDouble(jsonObject3,
															"totalPrice");

											Store store = new Store();
											store.setCityName(cityName1);
											store.setCityCode(cityCode);
											store.setTotalPrice(totalPrice);

											JSONArray jsonArray_cartList = JsonGetInfo
													.getJsonArray(jsonObject3,
															"cartList");

											if (jsonArray_cartList.length() > 0) {

												List<Product> plist = new ArrayList<Product>();
												for (int j = 0; j < jsonArray_cartList
														.length(); j++) {
													JSONObject jsonObject4 = JsonGetInfo.getJSONObject(
															jsonArray_cartList
																	.getJSONObject(j),
															"seedling");
													String show_type = "seedling_list";
													String id = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"id");
													String name = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"name");
													String imageUrl = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"largeImageUrl");
													String cityName = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"cityName");
													Double price = JsonGetInfo
															.getJsonDouble(
																	jsonObject4,
																	"price");
													int count = JsonGetInfo
															.getJsonInt(
																	jsonObject4,
																	"count");
													String unitTypeName = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"unitTypeName");
													Double diameter = JsonGetInfo
															.getJsonDouble(
																	jsonObject4,
																	"diameter");
													Double height = JsonGetInfo
															.getJsonDouble(
																	jsonObject4,
																	"height");
													Double crown = JsonGetInfo
															.getJsonDouble(
																	jsonObject4,
																	"crown");
													String fullName = JsonGetInfo.getJsonString(
															JsonGetInfo
																	.getJSONObject(
																			jsonObject4,
																			"ciCity"),
															"fullName");
													String ciCity_name = JsonGetInfo.getJsonString(
															JsonGetInfo
																	.getJSONObject(
																			jsonObject4,
																			"ciCity"),
															"name");
													String realName = JsonGetInfo.getJsonString(
															JsonGetInfo
																	.getJSONObject(
																			jsonObject4,
																			"ownerJson"),
															"realName");
													String companyName = JsonGetInfo.getJsonString(
															JsonGetInfo
																	.getJSONObject(
																			jsonObject4,
																			"ownerJson"),
															"companyName");
													String publicName = JsonGetInfo.getJsonString(
															JsonGetInfo
																	.getJSONObject(
																			jsonObject4,
																			"ownerJson"),
															"publicName");
													String status = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"status");
													String statusName = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"statusName");
													String closeDate = JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"closeDate");

													Product products = new Product(
															show_type, id,
															name, imageUrl,
															cityName, price,
															count,
															unitTypeName,
															diameter, height,
															crown, fullName,
															ciCity_name,
															realName,
															publicName, status,
															statusName,
															closeDate);
													plist.add(products);
												}
												store.setProducts(plist);
												list.add(store);
											}
											if (listAdapter != null) {
												listAdapter
														.notifyDataSetChanged();
											}
										}

										if (listAdapter == null) {
											listAdapter = new StoreAdapter(
													DActivity2.this, list);
											xListView.setAdapter(listAdapter);
										}

										pageIndex++;
									}

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
						Toast.makeText(DActivity2.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		list.clear();
		if (listAdapter == null) {
			listAdapter = new StoreAdapter(DActivity2.this, list);
			xListView.setAdapter(listAdapter);
		} else {
			listAdapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			initData();
		}
		onLoad();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// if(listAdapter!=null){
		// selectAll();
		// delSelect();
		// }
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		list.clear();
		initData();
		super.onResume();
	}

	@Override
	public void onLoadMore() {
		xListView.setPullRefreshEnable(false);
		initData();
		onLoad();
	}

	private void onLoad() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				xListView.stopRefresh();
				xListView.stopLoadMore();
				xListView.setRefreshTime(new Date().toLocaleString());
				xListView.setPullLoadEnable(true);
				xListView.setPullRefreshEnable(true);

			}
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

	public void checkAll(boolean checked) {
		cb_select_all.setChecked(checked);
	}

	public void updateAmount() {
		double amount = 0;
		tv_amount.setText("");
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).getProducts().size(); j++) {
				if (listAdapter.getPAdapter(i).getSelect().get(j)) {
					amount += list.get(i).getProducts().get(j).getPrice()
							* list.get(i).getProducts().get(j).getCount();
				}
			}
		}

		if (amount != 0)
			tv_amount.setText(amount + "");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
