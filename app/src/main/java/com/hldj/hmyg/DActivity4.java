package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.louisgeek.louisshopcart.adapter.MyBaseExpandableListAdapter;
import com.louisgeek.louisshopcart.bean.GoodsBean;
import com.louisgeek.louisshopcart.bean.StoreBean;

public class DActivity4 extends Activity {

	// 定义父列表项List数据集合
	List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
	// 定义子列表项List数据集合
	List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();

	MyBaseExpandableListAdapter myBaseExpandableListAdapter;
	CheckBox id_cb_select_all;
	LinearLayout id_ll_normal_all_state;
	LinearLayout id_ll_editing_all_state;
	ExpandableListView expandableListView;
	RelativeLayout id_rl_cart_is_empty;
	RelativeLayout id_rl_foot;

	TextView id_tv_edit_all;
	private int pageSize = 20;
	private int pageIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_d4);
		if (getIntent().getStringExtra("type") != null
				&& "1".equals(getIntent().getStringExtra("type"))) {
			ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
			btn_back.setVisibility(View.VISIBLE);
			btn_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			onRefresh();
		}
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

	}

	public void initView() {
		expandableListView = (ExpandableListView) findViewById(R.id.id_elv_listview);
		myBaseExpandableListAdapter = new MyBaseExpandableListAdapter(this,
				parentMapList, childMapList_list,expandableListView);
		expandableListView.setAdapter(myBaseExpandableListAdapter);

		expandableListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(DActivity4.this, "click：" + position,
								Toast.LENGTH_SHORT).show();
					}
				});

		for (int i = 0; i < parentMapList.size(); i++) {
			expandableListView.expandGroup(i);
		}

		ImageView id_iv_back = (ImageView) findViewById(R.id.id_iv_back);
		id_iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		id_ll_normal_all_state = (LinearLayout) findViewById(R.id.id_ll_normal_all_state);
		id_ll_editing_all_state = (LinearLayout) findViewById(R.id.id_ll_editing_all_state);
		id_rl_cart_is_empty = (RelativeLayout) findViewById(R.id.id_rl_cart_is_empty);
		TextView id_tv_save_star_all = (TextView) findViewById(R.id.id_tv_save_star_all);
		id_tv_save_star_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(DActivity4.this, "收藏多选商品", Toast.LENGTH_SHORT)
						.show();
			}
		});
		TextView id_tv_delete_all = (TextView) findViewById(R.id.id_tv_delete_all);
		id_tv_delete_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myBaseExpandableListAdapter.removeGoods();
				// Toast.makeText(MainActivity.this, "删除多选商品",
				// Toast.LENGTH_SHORT).show();

			}
		});

		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);

		id_tv_edit_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v instanceof TextView) {
					TextView tv = (TextView) v;
					if (MyBaseExpandableListAdapter.EDITING.equals(tv.getText())) {
						myBaseExpandableListAdapter.setupEditingAll(true);
						tv.setText(MyBaseExpandableListAdapter.FINISH_EDITING);
						changeFootShowDeleteView(true);// 这边类似的功能 后期待使用观察者模式
					} else {
						myBaseExpandableListAdapter.setupEditingAll(false);
						tv.setText(MyBaseExpandableListAdapter.EDITING);
						changeFootShowDeleteView(false);// 这边类似的功能 后期待使用观察者模式
					}

				}
			}
		});

		id_cb_select_all = (CheckBox) findViewById(R.id.id_cb_select_all);
		id_cb_select_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v instanceof CheckBox) {
					CheckBox checkBox = (CheckBox) v;
					myBaseExpandableListAdapter.setupAllChecked(checkBox
							.isChecked());
				}
			}
		});

		final TextView id_tv_totalPrice = (TextView) findViewById(R.id.id_tv_totalPrice);

		final TextView id_tv_totalCount_jiesuan = (TextView) findViewById(R.id.id_tv_totalCount_jiesuan);
		id_tv_totalCount_jiesuan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(DActivity4.this, "click：结算", Toast.LENGTH_SHORT)
						.show();
			}
		});
		myBaseExpandableListAdapter
				.setOnGoodsCheckedChangeListener(new MyBaseExpandableListAdapter.OnGoodsCheckedChangeListener() {
					@Override
					public void onGoodsCheckedChange(int totalCount,
							double totalPrice) {
						id_tv_totalPrice.setText(String.format(
								getString(R.string.total), totalPrice));
						id_tv_totalCount_jiesuan.setText(String.format(
								getString(R.string.jiesuan), totalCount));
					}
				});

		myBaseExpandableListAdapter
				.setOnAllCheckedBoxNeedChangeListener(new MyBaseExpandableListAdapter.OnAllCheckedBoxNeedChangeListener() {
					@Override
					public void onCheckedBoxNeedChange(
							boolean allParentIsChecked) {
						id_cb_select_all.setChecked(allParentIsChecked);
					}
				});

		myBaseExpandableListAdapter
				.setOnEditingTvChangeListener(new MyBaseExpandableListAdapter.OnEditingTvChangeListener() {
					@Override
					public void onEditingTvChange(boolean allIsEditing) {

						changeFootShowDeleteView(allIsEditing);// 这边类似的功能
																// 后期待使用观察者模式

					}
				});

		myBaseExpandableListAdapter
				.setOnCheckHasGoodsListener(new MyBaseExpandableListAdapter.OnCheckHasGoodsListener() {
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

	private void setupViewsShow(boolean isHasGoods) {
		if (isHasGoods) {
			expandableListView.setVisibility(View.VISIBLE);
			id_rl_cart_is_empty.setVisibility(View.GONE);
			id_rl_foot.setVisibility(View.VISIBLE);
			id_tv_edit_all.setVisibility(View.VISIBLE);
		} else {
			expandableListView.setVisibility(View.GONE);
			id_rl_cart_is_empty.setVisibility(View.VISIBLE);
			id_rl_foot.setVisibility(View.GONE);
			id_tv_edit_all.setVisibility(View.GONE);
		}
	}

	public void changeFootShowDeleteView(boolean showDeleteView) {

		if (showDeleteView) {
			id_tv_edit_all.setText(MyBaseExpandableListAdapter.FINISH_EDITING);

			id_ll_normal_all_state.setVisibility(View.INVISIBLE);
			id_ll_editing_all_state.setVisibility(View.VISIBLE);
		} else {
			id_tv_edit_all.setText(MyBaseExpandableListAdapter.EDITING);

			id_ll_normal_all_state.setVisibility(View.INVISIBLE);
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
		if (getIntent().getStringExtra("type") != null
				&& "1".equals(getIntent().getStringExtra("type"))) {
		} else {
			onRefresh();
		}

		super.onResume();
	}

	public void onRefresh() {
		parentMapList.clear();
		childMapList_list.clear();
		if (myBaseExpandableListAdapter != null) {
			myBaseExpandableListAdapter.notifyDataSetChanged();
			id_cb_select_all.setChecked(false);
			id_tv_edit_all.setText(MyBaseExpandableListAdapter.EDITING);
			id_ll_editing_all_state.setVisibility(View.INVISIBLE);
		}
		pageSize = 20;
		pageIndex = 0;
		initCartData();
		if (myBaseExpandableListAdapter != null && parentMapList.size() == 0) {
			setupViewsShow(false);
		}
	}

	private void initCartData() {

		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/cart/list", params,
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
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								JSONObject jsonObject5 = JsonGetInfo
										.getJSONObject(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"),
												"freeValidateData");
								JSONArray jsonArray2 = JsonGetInfo
										.getJsonArray(jsonObject5, "cartList");
								// 直接下单
								if (jsonArray2.length() > 0) {
									HashMap<String, Object> hMap2 = new HashMap<String, Object>();
									hMap2.put("isCheck", false);
									hMap2.put("cityName", "");
									hMap2.put("cityCode", "");
									hMap2.put("totalPrice", "");
									// 提供父列表的数据
									Map<String, Object> parentMap2 = new HashMap<String, Object>();

									parentMap2.put("parentName", new StoreBean(
											"noNeedValidate", "以下苗源直接下单",
											false, false, Color.RED, "下单"));

									parentMapList.add(parentMap2);
									// 提供当前父列的子列数据
									List<Map<String, Object>> childMapList2 = new ArrayList<Map<String, Object>>();

									if (jsonArray2.length() > 0) {

										for (int j = 0; j < jsonArray2.length(); j++) {
											JSONObject jsonObject4 = JsonGetInfo
													.getJSONObject(jsonArray2
															.getJSONObject(j),
															"seedling");

											Map<String, Object> products_hash = new HashMap<String, Object>();
											GoodsBean goodsBean = new GoodsBean(
													JsonGetInfo
															.getJsonString(
																	jsonArray2
																			.getJSONObject(j),
																	"id"),
													JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"name"),
													"url",
													"地径："
															+ JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"diameter")
															+ "高度："
															+ JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"height")
															+ "冠幅："
															+ JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"crown"),
													150,
													JsonGetInfo.getJsonDouble(
															jsonObject4,
															"price"),
													JsonGetInfo.getJsonInt(
															jsonObject5,
															"count"),
													GoodsBean.STATUS_VALID,
													false, false);
											products_hash.put("clickTpye", "1");
											products_hash.put("show_type",
													"seedling_list");
											products_hash.put("id", JsonGetInfo
													.getJsonString(jsonObject4,
															"id"));
											products_hash.put(
													"name",
													JsonGetInfo
															.getJsonString(
																	jsonObject4,
																	"name"));
											products_hash.put("imageUrl",
													JsonGetInfo.getJsonString(
															jsonObject4,
															"largeImageUrl"));
											products_hash.put("cityName",
													JsonGetInfo.getJsonString(
															jsonObject4,
															"cityName"));
											products_hash.put("price",
													JsonGetInfo.getJsonDouble(
															jsonObject4,
															"price"));
											products_hash.put("count",
													JsonGetInfo.getJsonInt(
															jsonObject4,
															"count"));
											products_hash.put("unitTypeName",
													JsonGetInfo.getJsonString(
															jsonObject4,
															"unitTypeName"));
											products_hash.put("diameter",
													JsonGetInfo.getJsonDouble(
															jsonObject4,
															"diameter"));
											products_hash.put("height",
													JsonGetInfo.getJsonDouble(
															jsonObject4,
															"height"));
											products_hash.put("crown",
													JsonGetInfo.getJsonDouble(
															jsonObject4,
															"crown"));
											products_hash.put("cityName",
													JsonGetInfo.getJsonString(
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
																							jsonObject4,
																							"ownerJson"),
																			"realName"));
											products_hash
													.put("companyName",
															JsonGetInfo
																	.getJsonString(
																			JsonGetInfo
																					.getJSONObject(
																							jsonObject4,
																							"ownerJson"),
																			"companyName"));
											products_hash
													.put("publicName",
															JsonGetInfo
																	.getJsonString(
																			JsonGetInfo
																					.getJSONObject(
																							jsonObject4,
																							"ownerJson"),
																			"publicName"));
											products_hash.put("status",
													JsonGetInfo.getJsonString(
															jsonObject4,
															"status"));
											products_hash.put("statusName",
													JsonGetInfo.getJsonString(
															jsonObject4,
															"statusName"));
											products_hash.put("closeDate",
													JsonGetInfo.getJsonString(
															jsonObject4,
															"closeDate"));
											products_hash.put("childName",
													goodsBean);
											products_hash.put("plantType",
													JsonGetInfo.getJsonString(
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
											products_hash.put("cashOnDelivery",
													JsonGetInfo.getJsonBoolean(
															jsonObject4,
															"cashOnDelivery")); // 担
																				// 资金担保
											products_hash
													.put("freeDeliveryPrice",
															JsonGetInfo
																	.getJsonBoolean(
																			jsonObject4,
																			"freeDeliveryPrice"));// 免发货费
											products_hash.put("freeValidate",
													JsonGetInfo.getJsonBoolean(
															jsonObject4,
															"freeValidate")); // 免验苗
											childMapList2.add(products_hash);
										}

										childMapList_list.add(childMapList2);

									}
								}

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
															"cartList");
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

											parentMapList.add(parentMap);
											// 提供当前父列的子列数据
											List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
											if (jsonArray_cartList.length() > 0) {
												for (int j = 0; j < jsonArray_cartList
														.length(); j++) {
													JSONObject jsonObject4 = JsonGetInfo.getJSONObject(
															jsonArray_cartList
																	.getJSONObject(j),
															"seedling");

													Map<String, Object> products_hash = new HashMap<String, Object>();
													GoodsBean goodsBean = new GoodsBean(
															JsonGetInfo
																	.getJsonString(
																			jsonArray_cartList
																					.getJSONObject(j),
																			"id"),
															JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"name"),
															"url",
															"地径："
																	+ JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"diameter")
																	+ "高度："
																	+ JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"height")
																	+ "冠幅："
																	+ JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"crown"),
															150,
															JsonGetInfo
																	.getJsonDouble(
																			jsonObject4,
																			"price"),
															JsonGetInfo
																	.getJsonInt(
																			jsonArray_cartList
																					.getJSONObject(j),
																			"count"),
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
																					"price"));
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
																					jsonObject4,
																					"diameter"));
													products_hash
															.put("height",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"height"));
													products_hash
															.put("crown",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
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
																									jsonObject4,
																									"ownerJson"),
																					"realName"));
													products_hash
															.put("companyName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ownerJson"),
																					"companyName"));
													products_hash
															.put("publicName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
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
																					jsonObject4,
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
													childMapList
															.add(products_hash);
												}

												childMapList_list
														.add(childMapList);
											}

										}
										//
									}

								}

								initView();
								pageIndex++;

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
						Toast.makeText(DActivity4.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});


	}

}
