package com.hldj.hmyg.broker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.maxwin.view.EPXListView;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.saveForCart;
import com.hldj.hmyg.buy.bean.GoodsBean;
import com.hldj.hmyg.buy.bean.OrderPreSave;
import com.hldj.hmyg.buy.bean.StoreBean;
import com.hldj.hmyg.buyer.CheckOutValidateActivity;
import com.hldj.hmyg.buyer.SaveForCartActivity;
import com.hldj.hmyg.buyer.SerializableMaplist;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class BrokerYanmiaoOrderActivity extends NeedSwipeBackActivity implements
		me.maxwin.view.EPXListView.IXListViewListener {

	// 定义父列表项List数据集合
	List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
	// 定义子列表项List数据集合
	List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();
	List<Map<String, Object>> parentMapList_tocheck = new ArrayList<Map<String, Object>>();
	
	// 定义子列表项List数据集合
	List<List<Map<String, Object>>> childMapList_list_tocheck = new ArrayList<List<Map<String, Object>>>();

	BrokerValidateListAdapter ListAdapter;
	CheckBox id_cb_select_all;
	LinearLayout id_ll_normal_all_state;
	LinearLayout id_ll_editing_all_state;
	EPXListView expandableListView;
	RelativeLayout id_rl_cart_is_empty;
	RelativeLayout id_rl_foot;
	TextView id_tv_edit_all;
	private int pageSize = 20;
	private int pageIndex = 0;
	boolean loadItems;
	private String acceptStatus = "";
	int to_totalCount = 0;
	double to_totalPrice = 0.00;
	boolean getdata; // 避免刷新多出数据
	private Gson gson;
	private TextView id_tv_totalPrice;
	private TextView id_tv_totalCount_jiesuan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broker_validate);
		gson = new Gson();
		onRefresh1();
		// initData();
		// initCartData();
		/*    *//**
		 * 第一个参数 应用程序接口 this 第二个父列List<?extends Map<String,Object>>集合
		 * 为父列提供数据 第三个参数 父列显示的组件资源文件 第四个参数 键值列表 父列Map字典的key 第五个要显示的父列组件id 第六个
		 * 子列的显示资源文件 第七个参数 键值列表的子列Map字典的key 第八个要显示子列的组件id
		 * 
		 * 第五个参数groupTo - The group views that should display column in the
		 * "groupFrom" parameter. These should all be TextViews. The first N
		 * views in this list are given the values of the first N columns in the
		 * groupFrom parameter.
		 */
		/*
		 * 
		 * SimpleExpandableListAdapter simpleExpandableListAdapter = new
		 * SimpleExpandableListAdapter( this, parentMapList,
		 * R.layout.parent_layout, new String[] { "parentName"}, new int[] {
		 * R.id.tv_title_parent}, childMapList_list, R.layout.child_layout, new
		 * String[] { "childName"}, new int[] { R.id.tv_items_child});
		 * expandableListView.setAdapter(simpleExpandableListAdapter);
		 */

	}

	public void initView() {
		expandableListView = (EPXListView) findViewById(R.id.id_elv_listview);
		expandableListView.setDivider(null);
		expandableListView.setPullLoadEnable(true);
		expandableListView.setPullRefreshEnable(true);
		expandableListView.setXListViewListener(this);
		if (ListAdapter == null) {
			ListAdapter = new BrokerValidateListAdapter(this, parentMapList,
					childMapList_list);
			expandableListView.setAdapter(ListAdapter);
		} else {
			ListAdapter.notifyDataSetChanged();
		}

		expandableListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(BrokerYanmiaoOrderActivity.this,
								"click：" + position, Toast.LENGTH_SHORT).show();
					}
				});

		for (int i = 0; i < parentMapList.size(); i++) {
			expandableListView.expandGroup(i);
		}

		ImageView id_iv_back = (ImageView) findViewById(R.id.id_iv_back);
		id_iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(BrokerYanmiaoOrderActivity.this, "click :back",
						Toast.LENGTH_SHORT).show();
			}
		});

		id_ll_normal_all_state = (LinearLayout) findViewById(R.id.id_ll_normal_all_state);
		id_ll_editing_all_state = (LinearLayout) findViewById(R.id.id_ll_editing_all_state);
		id_rl_cart_is_empty = (RelativeLayout) findViewById(R.id.id_rl_cart_is_empty);

		TextView id_tv_save_star_all = (TextView) findViewById(R.id.id_tv_save_star_all);
		id_tv_save_star_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(BrokerYanmiaoOrderActivity.this, "收藏多选商品",
						Toast.LENGTH_SHORT).show();
			}
		});
		TextView id_tv_delete_all = (TextView) findViewById(R.id.id_tv_delete_all);
		id_tv_delete_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ListAdapter.removeGoods();
				// Toast.makeText(MainActivity.this, "删除多选商品",
				// Toast.LENGTH_SHORT).show();

			}
		});

		id_tv_edit_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v instanceof TextView) {
					TextView tv = (TextView) v;
					if (ListAdapter.EDITING.equals(tv.getText())) {
						ListAdapter.setupEditingAll(true);
						tv.setText(ListAdapter.FINISH_EDITING);
						changeFootShowDeleteView(true);// 这边类似的功能 后期待使用观察者模式
					} else {
						//
						parentMapList_tocheck.clear();
						childMapList_list_tocheck.clear();
						for (int i = 0; i < parentMapList.size(); i++) {
							StoreBean storeBean = (StoreBean) parentMapList
									.get(i).get("parentName");
							if (storeBean.isChecked()) {
								parentMapList_tocheck.add(parentMapList.get(i));
								childMapList_list_tocheck.add(childMapList_list
										.get(i));
							}
							
						}
						if (parentMapList_tocheck.size() > 0
								&& childMapList_list_tocheck.size() > 0
								&& (parentMapList_tocheck.size() == childMapList_list_tocheck
										.size())) {
							Intent intent = new Intent(
									BrokerYanmiaoOrderActivity.this,
									SearchAgent4TransferValidateApplyAActivity.class);
							Bundle bundleObject = new Bundle();
							final SerializableMaplist myMap = new SerializableMaplist();
							myMap.setMap(parentMapList_tocheck);
							myMap.setChildMapList(childMapList_list_tocheck);
							bundleObject.putSerializable("map", myMap);
							intent.putExtras(bundleObject);
							startActivityForResult(intent, 1);
						} else {
							ListAdapter.setupEditingAll(false);
							tv.setText(ListAdapter.EDITING);
							changeFootShowDeleteView(false);// 这边类似的功能
															// 后期待使用观察者模式
						}

					}

				}
			}
		});

		id_cb_select_all = (CheckBox) findViewById(R.id.id_cb_select_all);
		/*
		 * 要么遍历判断再选择 id_cb_select_all.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // Toast.makeText(MainActivity.this,
		 * "all isChecked：" + isChecked, Toast.LENGTH_SHORT).show();
		 * BrokerValidateListAdapter.setupAllChecked(isChecked); } });
		 */
		id_cb_select_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v instanceof CheckBox) {
					CheckBox checkBox = (CheckBox) v;
					ListAdapter.setupAllChecked(checkBox.isChecked());
				}
			}
		});

		id_tv_totalPrice = (TextView) findViewById(R.id.id_tv_totalPrice);

		id_tv_totalCount_jiesuan = (TextView) findViewById(R.id.id_tv_totalCount_jiesuan);
		if ("unpay".equals(acceptStatus)) {
			id_tv_totalCount_jiesuan.setText(R.string.jiesuan_0);
		} else if ("verifyed".equals(acceptStatus)) {
			id_tv_totalCount_jiesuan.setText(R.string.xiadan_0);
		}
		id_tv_totalCount_jiesuan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(BuyerValidateApplyActivity.this, "click：结算",
				// Toast.LENGTH_SHORT).show();
				parentMapList_tocheck.clear();
				childMapList_list_tocheck.clear();
				for (int i = 0; i < parentMapList.size(); i++) {
					StoreBean storeBean = (StoreBean) parentMapList.get(i).get(
							"parentName");
					if (storeBean.isChecked()) {
						parentMapList_tocheck.add(parentMapList.get(i));
						childMapList_list_tocheck.add(childMapList_list.get(i));
					}
				}
				if (parentMapList_tocheck.size() > 0
						&& childMapList_list_tocheck.size() > 0
						&& (parentMapList_tocheck.size() == childMapList_list_tocheck
								.size())) {

					if ("unpay".equals(acceptStatus)) {
						Intent intent = new Intent(
								BrokerYanmiaoOrderActivity.this,
								CheckOutValidateActivity.class);
						Bundle bundleObject = new Bundle();
						final SerializableMaplist myMap = new SerializableMaplist();
						myMap.setMap(parentMapList_tocheck);
						myMap.setChildMapList(childMapList_list_tocheck);
						bundleObject.putSerializable("map", myMap);
						bundleObject.putInt("to_totalCount", to_totalCount);
						bundleObject.putDouble("to_totalPrice", to_totalPrice);
						bundleObject.putString("acceptStatus", acceptStatus);
						intent.putExtras(bundleObject);
						startActivityForResult(intent, 1);

					} else if ("verifyed".equals(acceptStatus)) {
						// 已验苗
						StringBuffer cartIds = new StringBuffer();
						int num = 0;
						ArrayList<OrderPreSave> orderPreSaves = new ArrayList<OrderPreSave>();
						ArrayList<saveForCart> saveForCarts = new ArrayList<saveForCart>();
						for (int i = 0; i < parentMapList_tocheck.size(); i++) {
							List<Map<String, Object>> list = childMapList_list_tocheck
									.get(i);
							for (int j = 0; j < list.size(); j++) {
								GoodsBean goodsBean = (GoodsBean) list.get(j)
										.get("childName");
								cartIds.append(goodsBean.getId() + ",");
								num++;
								orderPreSaves.add(new OrderPreSave(list.get(j)
										.get("id").toString(), "proxy",
										goodsBean.getCount()));
								// saveForCarts.add(new
								// saveForCart(parentMapList_tocheck
								// .get(i).get("id").toString(), goodsBean
								// .getCount(), "proxy"));
								// 添加ItemID
								saveForCarts.add(new saveForCart(list.get(j)
										.get("id").toString(), goodsBean
										.getCount(), "proxy"));
								// 添加ItemID
								cartIds.deleteCharAt(cartIds.length() - 1);
							}
						}
						preSave(num, gson.toJson(orderPreSaves),
								gson.toJson(saveForCarts), cartIds.toString());
					}

				}
			}
		});
		ListAdapter
				.setOnGoodsCheckedChangeListener(new BrokerValidateListAdapter.OnGoodsCheckedChangeListener() {

					@Override
					public void onGoodsCheckedChange(int totalCount,
							double totalPrice) {

						id_tv_totalPrice.setText(String.format(
								getString(R.string.total), totalPrice));
						to_totalPrice = totalPrice;
						if ("unpay".equals(acceptStatus)) {
							id_tv_totalCount_jiesuan.setText(String.format(
									getString(R.string.jiesuan), totalCount));
						} else if ("verifyed".equals(acceptStatus)) {
							id_tv_totalCount_jiesuan.setText(String.format(
									getString(R.string.xiadan), totalCount));
						}
						to_totalCount = totalCount;

						acceptStatus = getIntent().getStringExtra(
								"acceptStatus");
						loadItems = getIntent().getBooleanExtra("loadItems",
								false);
					}
				});

		ListAdapter
				.setOnAllCheckedBoxNeedChangeListener(new BrokerValidateListAdapter.OnAllCheckedBoxNeedChangeListener() {
					@Override
					public void onCheckedBoxNeedChange(
							boolean allParentIsChecked) {
						id_cb_select_all.setChecked(allParentIsChecked);
					}
				});

		ListAdapter
				.setOnEditingTvChangeListener(new BrokerValidateListAdapter.OnEditingTvChangeListener() {
					@Override
					public void onEditingTvChange(boolean allIsEditing) {

						changeFootShowDeleteView(allIsEditing);// 这边类似的功能
																// 后期待使用观察者模式

					}
				});

		ListAdapter
				.setOnCheckHasGoodsListener(new BrokerValidateListAdapter.OnCheckHasGoodsListener() {
					@Override
					public void onCheckHasGoods(boolean isHasGoods) {
						setupViewsShow(isHasGoods);
					}
				});

		/** ====include进来方式可能会导致view覆盖listview的最后一个item 解决 */
		// 在onCreate方法中一般没办法直接调用view.getHeight方法来获取到控件的高度
		id_rl_foot = (RelativeLayout) findViewById(R.id.id_rl_foot);
		/** ========== */

		if (parentMapList != null && parentMapList.size() > 0) {
			setupViewsShow(true);
		} else {
			setupViewsShow(false);
		}
	}

	public void preSave(final int num, final String orderPreSaves,
			final String str_itemData, final String cartIds) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ordersData", orderPreSaves);
		finalHttp.post(GetServerUrl.getUrl() + "admin/buyer/order/preSave",
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
								Toast.makeText(BrokerYanmiaoOrderActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code) && !jsonObject.isNull("data")) {
								// 传递值到下单页面
								// 已验苗去下单
								Intent intent = new Intent(
										BrokerYanmiaoOrderActivity.this,
										SaveForCartActivity.class);
								intent.putExtra("str_SaveForCart",
										orderPreSaves);
								intent.putExtra("str_itemData", str_itemData);
								intent.putExtra("cartIds", cartIds);
								intent.putExtra("t", t.toString());
								intent.putExtra("tag", "order");
								startActivity(intent);
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
						Toast.makeText(BrokerYanmiaoOrderActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private void setupViewsShow(boolean isHasGoods) {
		if (isHasGoods) {
			expandableListView.setVisibility(View.VISIBLE);
			id_rl_cart_is_empty.setVisibility(View.GONE);
			id_rl_foot.setVisibility(View.VISIBLE);
			id_tv_edit_all.setVisibility(View.VISIBLE);
			setAcceptStatus();
		} else {
			expandableListView.setVisibility(View.GONE);
			id_rl_cart_is_empty.setVisibility(View.VISIBLE);
			id_rl_foot.setVisibility(View.GONE);
			id_tv_edit_all.setVisibility(View.VISIBLE);
			setAcceptStatus();
		}
	}

	public void changeFootShowDeleteView(boolean showDeleteView) {

		if (showDeleteView) {
			id_tv_edit_all.setText(ListAdapter.FINISH_EDITING);

			id_ll_normal_all_state.setVisibility(View.INVISIBLE);
			id_ll_editing_all_state.setVisibility(View.VISIBLE);
		} else {
			id_tv_edit_all.setText(ListAdapter.EDITING);

			id_ll_normal_all_state.setVisibility(View.VISIBLE);
			// id_ll_normal_all_state.setVisibility(View.VISIBLE);
			id_ll_editing_all_state.setVisibility(View.INVISIBLE);
		}
	}

	public int dp2px(Context context, float dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void onRefresh1() {
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		;
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		acceptStatus = getIntent().getStringExtra("acceptStatus");
		loadItems = getIntent().getBooleanExtra("loadItems", false);
		if ("".equals(acceptStatus)) {
			tv_title.setText("全部验苗订单");
		} else if ("unpay".equals(acceptStatus)) {
			tv_title.setText("待付款");
		} else if ("unaccept".equals(acceptStatus)) {
			tv_title.setText("待受理");
		} else if ("verifing".equals(acceptStatus)) {
			tv_title.setText("验苗中");
		} else if ("verifyed".equals(acceptStatus)) {
			tv_title.setText("已验苗");
		} else if ("ordered".equals(acceptStatus)) {
			tv_title.setText("已下单");
		}
		id_rl_foot = (RelativeLayout) findViewById(R.id.id_rl_foot);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		setAcceptStatus();
		parentMapList.clear();
		childMapList_list.clear();
		if (ListAdapter != null) {
			ListAdapter.notifyDataSetChanged();
			id_cb_select_all.setChecked(false);
			id_tv_edit_all.setText(ListAdapter.EDITING);
			id_ll_editing_all_state.setVisibility(View.INVISIBLE);
		}
		pageIndex = 0;
		initCartData();
		if (ListAdapter != null && parentMapList.size() == 0) {
			setupViewsShow(false);
		}
	}

	public void setAcceptStatus() {
		if ("unpay".equals(acceptStatus) || "verifyed".equals(acceptStatus)) {
			id_tv_edit_all.setVisibility(View.VISIBLE);
			id_rl_foot.setVisibility(View.VISIBLE);
		} else {
			id_tv_edit_all.setVisibility(View.VISIBLE);
			id_rl_foot.setVisibility(View.GONE);
		}
	}

	private void initCartData() {
		getdata = false;
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("loadItems", true + "");
		params.put("acceptStatus", acceptStatus);
		finalHttp.post(
				GetServerUrl.getUrl() + "admin/agent/validateApply/list",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						Log.e("admin/buyer/validateApply/list", t.toString());
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								// 购物车 需要验苗
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									if (jsonArray.length() > 0) {
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject3 = jsonArray
													.getJSONObject(i);
											HashMap<String, Object> hMap = new HashMap<String, Object>();
											hMap.put("isCheck", false);
											hMap.put("cityName", JsonGetInfo
													.getJsonString(jsonObject3,
															"cityName"));
											hMap.put("cityCode", JsonGetInfo
													.getJsonString(jsonObject3,
															"cityCode"));
											hMap.put("totalPrice", JsonGetInfo
													.getJsonDouble(jsonObject3,
															"totalPrice"));
											JSONArray jsonArray_cartList = JsonGetInfo
													.getJsonArray(jsonObject3,
															"itemListJson");
											// 提供父列表的数据
											Map<String, Object> parentMap = new HashMap<String, Object>();

											parentMap
													.put("parentName",
															new StoreBean(
																	"" + i,
																	JsonGetInfo
																			.getJsonString(
																					jsonObject3,
																					"cityName"),
																	false,
																	false,
																	Color.BLACK,
																	"验苗"));
											parentMap.put("id", JsonGetInfo
													.getJsonString(jsonObject3,
															"id"));
											parentMap.put("cityName",
													JsonGetInfo.getJsonString(
															jsonObject3,
															"cityName"));
											parentMap.put("cityCode",
													JsonGetInfo.getJsonString(
															jsonObject3,
															"cityCode"));
											parentMap.put("createDate",
													JsonGetInfo.getJsonString(
															jsonObject3,
															"createDate"));
											parentMap.put("num", JsonGetInfo
													.getJsonString(jsonObject3,
															"num"));
											parentMap.put("validatePrice",
													JsonGetInfo.getJsonDouble(
															jsonObject3,
															"validatePrice"));
											parentMap.put("itemCountJson",
													JsonGetInfo.getJsonInt(
															jsonObject3,
															"itemCountJson"));
											parentMap
													.put("verifyedCountJson",
															JsonGetInfo
																	.getJsonInt(
																			jsonObject3,
																			"verifyedCountJson"));
											parentMap.put("acceptStatus",
													acceptStatus);
											parentMapList.add(parentMap);
											// 提供当前父列的子列数据
											List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
											if (jsonArray_cartList.length() > 0) {
												for (int j = 0; j < jsonArray_cartList
														.length(); j++) {
													JSONObject jsonObject4 = jsonArray_cartList
															.getJSONObject(j);
													Map<String, Object> products_hash = new HashMap<String, Object>();
													GoodsBean goodsBean = new GoodsBean(
															JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"id"),
															JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"name"),
															"url",
															JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"specText"),
															JsonGetInfo
																	.getJsonDouble(
																			jsonObject4,
																			"seedlingPrice"),
															JsonGetInfo
																	.getJsonDouble(
																			jsonObject4,
																			"seedlingPrice"),
															JsonGetInfo
																	.getJsonInt(
																			jsonObject4,
																			"applyCount"),
															GoodsBean.STATUS_VALID,
															false, false);
													products_hash.put(
															"clickTpye", "2");
													products_hash.put(
															"show_type",
															"seedling_list");
													products_hash
															.put("id",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"id"));
													products_hash
															.put("name",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"name"));
													products_hash
															.put("imageUrl",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"largeImageUrl"));
													products_hash
															.put("cityName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"cityName"));
													products_hash
															.put("price",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"seedlingPrice"));
													products_hash
															.put("count",
																	JsonGetInfo
																			.getJsonInt(
																					jsonObject4,
																					"count"));
													products_hash
															.put("unitTypeName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"unitTypeName"));
													products_hash
															.put("diameter",
																	JsonGetInfo
																			.getJsonDouble(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"seedlingJson"),
																					"diameter"));
													products_hash
															.put("height",
																	JsonGetInfo
																			.getJsonDouble(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"seedlingJson"),
																					"height"));
													products_hash
															.put("crown",
																	JsonGetInfo
																			.getJsonDouble(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"seedlingJson"),
																					"crown"));
													products_hash
															.put("cityName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"cityName"));
													products_hash
															.put("fullName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ciCity"),
																					"fullName"));
													products_hash
															.put("ciCity_name",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ciCity"),
																					"name"));
													products_hash
															.put("realName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									JsonGetInfo
																											.getJSONObject(
																													jsonObject4,
																													"seedlingJson"),
																									"ownerJson"),
																					"realName"));
													products_hash
															.put("companyName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									JsonGetInfo
																											.getJSONObject(
																													jsonObject4,
																													"seedlingJson"),
																									"ownerJson"),
																					"companyName"));
													products_hash
															.put("publicName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									JsonGetInfo
																											.getJSONObject(
																													jsonObject4,
																													"seedlingJson"),
																									"ownerJson"),
																					"publicName"));
													products_hash
															.put("status",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"status"));
													products_hash
															.put("statusName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"statusName"));
													products_hash
															.put("closeDate",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"seedlingJson"),
																					"closeDate"));
													products_hash.put(
															"childName",
															goodsBean);
													products_hash
															.put("plantType",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"plantType"));
													products_hash
															.put("isSelfSupport",
																	JsonGetInfo
																			.getJsonBoolean(
																					jsonObject4,
																					"isSelfSupportJson")); // 自营
													products_hash
															.put("freeValidatePrice",
																	JsonGetInfo
																			.getJsonBoolean(
																					jsonObject4,
																					"freeValidatePrice")); // 返验苗费
													products_hash
															.put("cashOnDelivery",
																	JsonGetInfo
																			.getJsonBoolean(
																					jsonObject4,
																					"cashOnDelivery")); // 担
																										// 资金担保
													products_hash
															.put("freeDeliveryPrice",
																	JsonGetInfo
																			.getJsonBoolean(
																					jsonObject4,
																					"freeDeliveryPrice"));// 免发货费
													products_hash
															.put("freeValidate",
																	JsonGetInfo
																			.getJsonBoolean(
																					jsonObject4,
																					"freeValidate")); // 免验苗
													products_hash
															.put("tagList",
																	JsonGetInfo
																			.getJsonArray(
																					jsonObject4,
																					"tagList")
																			.toString());//
													childMapList
															.add(products_hash);
												}

												childMapList_list
														.add(childMapList);
											}

										}
										initView();
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
						Toast.makeText(BrokerYanmiaoOrderActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
		// for (int i = 0; i < 4; i++) {
		// String store = "旗舰店";
		// if (i % 2 == 0) {
		// store = "专营店";
		// }
		// // 提供父列表的数据
		// Map<String, Object> parentMap = new HashMap<String, Object>();
		//
		// parentMap.put("parentName", new StoreBean("" + i, store + i, false,
		// false));
		// parentMapList.add(parentMap);
		// // 提供当前父列的子列数据
		// List<Map<String, Object>> childMapList = new ArrayList<Map<String,
		// Object>>();
		// for (int j = 0; j < 3; j++) {
		// Map<String, Object> childMap = new HashMap<String, Object>();
		//
		// GoodsBean goodsBean = new GoodsBean(i + "_" + j, store + i
		// + "下的商品" + j, "url", "均码，红色", 150, 120, 1,
		// GoodsBean.STATUS_VALID, false, false);
		// childMap.put("childName", goodsBean);
		// childMapList.add(childMap);
		// }
		// childMapList_list.add(childMapList);
		// }

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if ("unpay".equals(acceptStatus)) {
			id_tv_totalCount_jiesuan.setText(R.string.jiesuan_0);
		} else if ("verifyed".equals(acceptStatus)) {
			id_tv_totalCount_jiesuan.setText(R.string.xiadan_0);
		}
		expandableListView.setPullLoadEnable(false);
		pageIndex = 0;
		parentMapList.clear();
		childMapList_list.clear();
		if (ListAdapter == null) {
			ListAdapter = new BrokerValidateListAdapter(this, parentMapList,
					childMapList_list);
			expandableListView.setAdapter(ListAdapter);
		} else {
			ListAdapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			initCartData();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		expandableListView.setPullRefreshEnable(false);
		initCartData();
		onLoad();
	}

	private void onLoad() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				expandableListView.stopRefresh();
				expandableListView.stopLoadMore();
				expandableListView.setRefreshTime(new Date().toLocaleString());
				expandableListView.setPullLoadEnable(true);
				expandableListView.setPullRefreshEnable(true);

			}
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

	private void initData() {

		for (int i = 0; i < 15; i++) {
			// 提供父列表的数据
			Map<String, Object> parentMap = new HashMap<String, Object>();
			parentMap.put("parentName", "parentName" + i);
			if (i % 2 == 0) {
				parentMap.put("parentIcon", R.drawable.ic_launcher);
			} else {
				parentMap.put("parentIcon", R.drawable.ic_launcher);
			}
			parentMapList.add(parentMap);
			// 提供当前父列的子列数据
			List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < 10; j++) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put("childName", "parentName" + i + "下面的childName" + j);
				childMapList.add(childMap);
			}
			childMapList_list.add(childMapList);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			id_cb_select_all.setChecked(false);
			id_tv_totalPrice.setText(R.string.total_0);
			if ("unpay".equals(acceptStatus)) {
				id_tv_totalCount_jiesuan.setText(R.string.jiesuan_0);
			} else if ("verifyed".equals(acceptStatus)) {
				id_tv_totalCount_jiesuan.setText(R.string.xiadan_0);
			}

			onRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
