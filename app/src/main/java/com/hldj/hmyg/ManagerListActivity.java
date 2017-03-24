package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.kaede.tagview.OnTagDeleteListener;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter.AllCaps;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barryzhang.temptyview.TViewUtil;
import com.cn2che.androids.swipe.ListViewSwipeGesture;
import com.hldj.hmyg.adapter.ProductListAdapterForManager;
import com.hldj.hmyg.adapter.ProductListAdapterForManager.OnGoodsCheckedChangeListener;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.saler.SearchActivity;
import com.hldj.hmyg.saler.StorageSaveActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

@SuppressLint("ClickableViewAccessibility")
public class ManagerListActivity extends NeedSwipeBackActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener {
	private XListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	boolean getdata; // 避免刷新多出数据
	private ProductListAdapterForManager listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_06;
	private String status = "";
	private String searchKey = "";
	FinalHttp finalHttp = new FinalHttp();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager_list);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		RelativeLayout rl_01 = (RelativeLayout) findViewById(R.id.rl_01);
		RelativeLayout rl_02 = (RelativeLayout) findViewById(R.id.rl_02);
		RelativeLayout rl_03 = (RelativeLayout) findViewById(R.id.rl_03);
		RelativeLayout rl_04 = (RelativeLayout) findViewById(R.id.rl_04);
		RelativeLayout rl_05 = (RelativeLayout) findViewById(R.id.rl_05);
		RelativeLayout rl_06 = (RelativeLayout) findViewById(R.id.rl_06);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		id_tv_edit_all.setVisibility(View.GONE);
		tv_01.setTextColor(getResources().getColor(R.color.main_color));
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		if (getIntent().getStringExtra("status") != null) {
			status = getIntent().getStringExtra("status");

		}
		if ("".equals(status)) {
			all();
		} else if ("unaudit".equals(status)) {
			unaudit();
		} else if ("published".equals(status)) {
			published();
		} else if ("outline".equals(status)) {
			outline();
		} else if ("backed".equals(status)) {
			backed();
		} else if ("unsubmit".equals(status)) {
			unsubmit();
		}
		initData();
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		rl_01.setOnClickListener(multipleClickProcess);
		rl_02.setOnClickListener(multipleClickProcess);
		rl_03.setOnClickListener(multipleClickProcess);
		rl_04.setOnClickListener(multipleClickProcess);
		rl_05.setOnClickListener(multipleClickProcess);
		rl_06.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);
		iv_search.setOnClickListener(multipleClickProcess);
		tagView = (TagView) this.findViewById(R.id.tagview);
		tagView.setOnTagDeleteListener(new OnTagDeleteListener() {

			@Override
			public void onTagDeleted(int position, me.kaede.tagview.Tag tag) {
				// TODO Auto-generated method stub
				if (tag.id == 1) {
					searchKey = "";
					onRefresh();
				}
			}
		});
		TViewUtil.EmptyViewBuilder.getInstance(ManagerListActivity.this)
				.setEmptyText(getResources().getString(R.string.nodata))
				.setEmptyTextSize(12).setEmptyTextColor(Color.GRAY)
				.setShowButton(false)
				.setActionText(getResources().getString(R.string.reload))
				.setAction(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
					}
				}).setShowIcon(true).setShowText(true).bindView(xListView);

	}

	ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void FullSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(ManagerListActivity.this, "Action_2",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void HalfSwipeListView(int position) {
			// TODO Auto-generated method stub
			seedlingDoDel(datas.get(position - 1).get("id").toString(),
					position - 1);
		}

		@Override
		public void LoadDataForScroll(int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			// Toast.makeText(ManagerListActivity.this,"Delete",
			// Toast.LENGTH_SHORT).show();
			// for(int i:reverseSortedPositions){
			// data.remove(i);
			// new MyAdapter().notifyDataSetChanged();
			// }

		}

		@Override
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub

		}

	};
	private TextView id_tv_edit_all;
	private ImageView iv_search;
	private TagView tagView;

	private void seedlingDoDel(String id, final int pos) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ids", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/doDel", params,
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
								Toast.makeText(ManagerListActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								datas.remove(pos);
								listAdapter.notifyDataSetChanged();
								// onRefresh();
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
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("status", status);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("searchKey", searchKey);
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/manage/list",
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("show_type", "manage_list");
										hMap.put("name", JsonGetInfo
												.getJsonString(jsonObject3,
														"standardName"));
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										hMap.put("specText", JsonGetInfo
												.getJsonString(jsonObject3,
														"specText"));
										hMap.put("plantType", JsonGetInfo
												.getJsonString(jsonObject3,
														"plantType"));
										hMap.put("isSelfSupport", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSelfSupportJson")); // 自营
										hMap.put("freeValidatePrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeValidatePrice")); // 返验苗费
										hMap.put("cashOnDelivery", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"cashOnDelivery")); // 担
																			// 资金担保
										hMap.put("freeDeliveryPrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeDeliveryPrice"));// 免发货费
										hMap.put(
												"tagList",
												JsonGetInfo.getJsonArray(
														jsonObject3, "tagList")
														.toString());//
										hMap.put("freeValidate", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"freeValidate")); // 免验苗
										hMap.put("isRecommend", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isRecommend")); // 推荐
										hMap.put("isSpecial", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSpecial")); // 清场
										hMap.put("imageUrl", JsonGetInfo
												.getJsonString(jsonObject3,
														"mediumImageUrl"));
										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("fullName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"fullName"));
										hMap.put("price", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.put("floorPrice", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"floorPrice"));
										hMap.put("diameter", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"diameter"));
										hMap.put("dbh", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"dbh"));
										hMap.put("height", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"height"));
										hMap.put("crown", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"crown"));
										hMap.put("offbarHeight", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"offbarHeight"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject3,
														"count"));
										hMap.put("status", JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.put("statusName", JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										hMap.put("closeDate", JsonGetInfo
												.getJsonString(jsonObject3,
														"closeDate"));
										hMap.put("unitTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitTypeName"));
										hMap.put("detailAddress", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"nurseryJson"),
														"detailAddress"));
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new ProductListAdapterForManager(
												ManagerListActivity.this, datas);
										final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(
												xListView, swipeListener,
												ManagerListActivity.this);
										touchListener.SwipeType = ListViewSwipeGesture.Double; // 设置两个选项列表项的背景
										xListView
												.setOnTouchListener(touchListener);
										listAdapter
												.setOnGoodsCheckedChangeListener(new OnGoodsCheckedChangeListener() {

													@Override
													public void onGoodsCheckedChange(
															String id,
															boolean isRefresh) {
														// TODO Auto-generated
														// method stub
														if (isRefresh) {
															onRefresh();
														}

													}
												});
										xListView.setAdapter(listAdapter);
										xListView
												.setOnItemClickListener(new OnItemClickListener() {

													@Override
													public void onItemClick(
															AdapterView<?> arg0,
															View arg1,
															int position,
															long arg3) {
														// TODO Auto-generated
														// method stub
														if(position!=0){
															Intent toFlowerDetailActivity = new Intent(
																	ManagerListActivity.this,
																	FlowerDetailActivity.class);
															toFlowerDetailActivity
																	.putExtra(
																			"id",
																			datas.get(
																					position - 1)
																					.get("id")
																					.toString());
															toFlowerDetailActivity
																	.putExtra(
																			"show_type",
																			datas.get(
																					position - 1)
																					.get("show_type")
																					.toString());
															startActivityForResult(
																	toFlowerDetailActivity,
																	1);
															overridePendingTransition(
																	R.anim.slide_in_left,
																	R.anim.slide_out_right);
														}
														

													}
												});
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
						Toast.makeText(ManagerListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
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
					finish();
					break;
				case R.id.rl_01:
					all();
					onRefresh();
					break;
				case R.id.rl_02:
					unaudit();
					onRefresh();
					break;
				case R.id.rl_03:
					published();
					onRefresh();
					break;
				case R.id.rl_04:
					outline();
					onRefresh();
					break;
				case R.id.rl_05:
					backed();
					onRefresh();
					break;
				case R.id.rl_06:
					unsubmit();
					onRefresh();
					break;
				case R.id.id_tv_edit_all:
					Intent toStorageSaveActivity = new Intent(
							ManagerListActivity.this, StorageSaveActivity.class);
					startActivity(toStorageSaveActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.iv_search:
					Intent toSearchActivity = new Intent(
							ManagerListActivity.this, SearchActivity.class);
					toSearchActivity.putExtra("searchKey", searchKey);
					startActivityForResult(toSearchActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
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
					Thread.sleep(200);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg2 != null && arg1 == 6) {
			searchKey = arg2.getStringExtra("searchKey");
			List<Tag> tags = tagView.getTags();
			for (int i = 0; i < tags.size(); i++) {
				if (tags.get(i).id == 1) {
					tagView.remove(i);
				}
			}
			if (!"".equals(searchKey)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(searchKey);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 1; // 关键字
				tagView.addTag(tag);
			}
			all();
		}
		onRefresh();
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (listAdapter == null) {
			listAdapter = new ProductListAdapterForManager(
					ManagerListActivity.this, datas);
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

	private void outline() {
		// TODO Auto-generated method stub
		status = "outline";
		tv_04.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void unsubmit() {
		// TODO Auto-generated method stub
		status = "unsubmit";
		tv_06.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void backed() {
		// TODO Auto-generated method stub
		status = "backed";
		tv_05.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void published() {
		// TODO Auto-generated method stub
		status = "published";
		tv_03.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void unaudit() {
		// TODO Auto-generated method stub
		status = "unaudit";
		tv_02.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void all() {
		// TODO Auto-generated method stub
		status = "";
		tv_01.setTextColor(getResources().getColor(R.color.main_color));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

}
