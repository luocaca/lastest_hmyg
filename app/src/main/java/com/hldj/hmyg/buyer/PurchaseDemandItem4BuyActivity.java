package com.hldj.hmyg.buyer;

import info.hoang8f.android.segmented.SegmentedGroup;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.barryzhang.temptyview.TViewUtil;
import com.hldj.hmyg.BActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.buy.adapter.PurchaseDemandItemBaseExpandableListAdapter;
import com.hldj.hmyg.buy.adapter.PurchaseDemandItemBaseExpandableListAdapter.OnGoodsCheckedChangeListener;
import com.hldj.hmyg.buy.bean.PurchaseDemandChildItem;
import com.hldj.hmyg.buy.bean.PurchaseDemandGroupItem;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.StringFormatUtil;
import com.hy.utils.ValueGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zf.iosdialog.widget.AlertDialog;

public class PurchaseDemandItem4BuyActivity extends Activity implements
		OnCheckedChangeListener, OnGoodsCheckedChangeListener, OnClickListener {
	private ExpandableListView expandList;
	private List<PurchaseDemandGroupItem> groupData;// group的数据源
	private List<List<PurchaseDemandChildItem>> childData;// child的数据源
	private PurchaseDemandItemBaseExpandableListAdapter myAdapter;
	private int pageSize = 10;
	private int pageIndex = 0;
	private String usedQuote = "";
	private String name = "";
	final int CONTEXT_MENU_GROUP_DELETE = 0;// 添加上下文菜单时每一个菜单项的item ID
	final int CONTEXT_MENU_GROUP_RENAME = 1;
	final int CONTEXT_MENU_CHILD_EDIT = 2;
	final int CONTEXT_MENU_CHILD_DELETE = 3;
	private ImageView btn_back;
	private TextView id_tv_edit_all;
	FinalHttp finalHttp = new FinalHttp();
	boolean getdata; // 避免刷新多出数据
	private TextView tv_title;
	private TextView tv_count;
	private TextView tv_price;
	private LinearLayout ll_total;
	private RelativeLayout rl_segmented;
	private RadioButton button31;
	private RadioButton button32;
	private RadioButton button33;
	private RelativeLayout rl_call;
	private String clerkPhone = "";
	private EditText et_search;
	private String tag = "";
	private String url = "";
	String status = "";
	private KProgressHUD hud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_demand_item_for_buy);
		hud = KProgressHUD.create(PurchaseDemandItem4BuyActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("数据加载中...").setMaxProgress(100).setCancellable(true)
				.setDimAmount(0f);

		btn_back = (ImageView) findViewById(R.id.btn_back);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_price = (TextView) findViewById(R.id.tv_price);
		ll_total = (LinearLayout) findViewById(R.id.ll_total);
		rl_call = (RelativeLayout) findViewById(R.id.rl_call);
		status = getIntent().getStringExtra("status");
		if (getIntent().getStringExtra("tag") != null) {
			tag = getIntent().getStringExtra("tag");
			if ("clerk".equals(tag)) {
				url = "admin/clerk/purchaseDemandItem/clerkHelpQuoteList";
				rl_call.setVisibility(View.INVISIBLE);
			} else if ("buyer".equals(tag)) {
				url = "admin/buyer/purchaseDemandItem/list";
			}
		}
		et_search = (EditText) findViewById(R.id.et_search);
		final String hintText = "<img src=\"" + R.drawable.search_icon
				+ "\" /> 输入品名";
		et_search.setHint(Html.fromHtml(hintText, imageGetter, null));
		// et_search.setGravity(Gravity.CENTER);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

		rl_segmented = (RelativeLayout) findViewById(R.id.rl_segmented);
		tv_title.setText(getIntent().getStringExtra("projectName"));

		if ("confirm".equals(getIntent().getStringExtra("status"))) {
			rl_segmented.setVisibility(View.VISIBLE);
		}
		expandList = (ExpandableListView) findViewById(R.id.expandlist);
		expandList.setDivider(null);
		SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
		button31 = (RadioButton) findViewById(R.id.button31);
		button32 = (RadioButton) findViewById(R.id.button32);
		button33 = (RadioButton) findViewById(R.id.button33);
		button31.setChecked(true);
		segmented3.setOnCheckedChangeListener(this);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		id_tv_edit_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		TViewUtil.EmptyViewBuilder
				.getInstance(PurchaseDemandItem4BuyActivity.this)
				.setEmptyText(getResources().getString(R.string.nodata))
				.setEmptyTextSize(12).setEmptyTextColor(Color.GRAY)
				.setShowButton(false)
				.setActionText(getResources().getString(R.string.reload))
				.setAction(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
					}
				}).setShowIcon(true).setShowText(true).bindView(expandList);
		initDatas();
		purchaseDemandIteminitData();
		rl_call.setOnClickListener(PurchaseDemandItem4BuyActivity.this);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					button31.setChecked(true);
					usedQuote = "";
					name = et_search.getText().toString();
					onRefresh();
				}
				return false;
			}
		});

		tv_btn.setOnClickListener(this);

		// initView();
		// initEvents();
	}

	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			int rId = Integer.parseInt(source);
			drawable = getResources().getDrawable(rId);
			drawable.setBounds(0, 0, 30, 30);
			return drawable;
		}
	};
	private TextView tv_btn;

	private void purchaseDemandIteminitData() {
		// TODO Auto-generated method stub
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("demandId", getIntent().getStringExtra("demandId"));
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/purchaseDemandItem/initData", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						Log.e("admin/buyer/purchaseDemandItem/initData",
								t.toString());
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {

							}
							if ("1".equals(code)) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								clerkPhone = JsonGetInfo.getJsonString(data,
										"clerkPhone");
								if (!"".equals(clerkPhone)) {

								}
								int itemCount = JsonGetInfo.getJsonInt(data,
										"itemCount");
								int usedCount = JsonGetInfo.getJsonInt(data,
										"usedCount");
								int unUsedCount = JsonGetInfo.getJsonInt(data,
										"unUsedCount");
								double totalPrice = JsonGetInfo.getJsonDouble(
										data, "totalPrice");
								if (usedCount > 0 && totalPrice > 0) {
									StringFormatUtil fillColor = new StringFormatUtil(
											PurchaseDemandItem4BuyActivity.this,
											"已确认" + usedCount + "种，", usedCount
													+ "", R.color.red)
											.fillColor();
									tv_count.setText(fillColor.getResult());
									StringFormatUtil fillColor2 = new StringFormatUtil(
											PurchaseDemandItem4BuyActivity.this,
											"共"
													+ ValueGetInfo
															.formatFloatNumber(totalPrice)
													+ "元。",
											ValueGetInfo
													.formatFloatNumber(totalPrice)
													+ "", R.color.red)
											.fillColor();
									tv_price.setText(fillColor2.getResult());
								} else {
									tv_count.setText("暂无已确认的报价");
									tv_price.setText("");
								}
								if (itemCount > 0) {
									button31.setText("全部（" + itemCount + "）");
								} else {
									button31.setText("全部");
								}
								if (usedCount > 0) {
									button32.setText("已确定（" + usedCount + "）");
								} else {
									button32.setText("已确定");
								}
								if (unUsedCount > 0) {
									button33.setText("未确定（" + unUsedCount + "）");
								} else {
									button33.setText("未确定");
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
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	/**
	 * group和child子项的数据源
	 */
	private void initDatas() {

		getdata = false;
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		// params.put("pageSize", pageSize + "");
		// params.put("pageIndex", pageIndex + "");
		params.put("usedQuote", usedQuote);
		params.put("name", name);
		params.put("demandId", getIntent().getStringExtra("demandId"));
		Log.e(url, params.toString());
		finalHttp.post(GetServerUrl.getUrl() + url, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						if (!hud.isShowing() && hud != null) {
							hud.show();
						}
						super.onStart();
					}

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						if (hud != null) {
							hud.dismiss();
						}
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {

							}
							if ("1".equals(code)) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(data, "page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								JSONArray jsonArray = JsonGetInfo.getJsonArray(
										jsonObject2, "data");
								groupData = new ArrayList<PurchaseDemandGroupItem>();
								childData = new ArrayList<List<PurchaseDemandChildItem>>();
								Log.e("xxxxxxxxxxxxxxxx",
										"ArrayList<List<PurchaseDemandChildItem>>");
								for (int j = 0; j < jsonArray.length(); j++) {
									JSONObject groupJson = jsonArray
											.getJSONObject(j);

									Log.e("jjjjjjjjjjjj",
											"ArrayList<List<PurchaseDemandChildItem>>"
													+ j);
									List<PurchaseDemandChildItem> PurchaseDemandChildItems = new ArrayList<PurchaseDemandChildItem>();
									JSONArray childsJson = JsonGetInfo
											.getJsonArray(groupJson,
													"quoteList");

									for (int i = 0; i < childsJson.length(); i++) {

										Log.e("iiiiiiiiii",
												"ArrayList<List<PurchaseDemandChildItem>>"
														+ i);
										JSONObject childs = childsJson
												.getJSONObject(i);
										PurchaseDemandChildItem purchaseDemandChildItem = new PurchaseDemandChildItem();
										purchaseDemandChildItem
												.setId(JsonGetInfo
														.getJsonString(childs,
																"id"));
										purchaseDemandChildItem
												.setRemarks(JsonGetInfo
														.getJsonString(childs,
																"remarks"));
										Log.e("iiiiiiiiii", "remarks" + i);
										purchaseDemandChildItem
												.setCreateBy(JsonGetInfo
														.getJsonString(childs,
																"createBy"));
										purchaseDemandChildItem
												.setCreateDate(JsonGetInfo
														.getJsonString(childs,
																"createDate"));
										purchaseDemandChildItem
												.setCityCode(JsonGetInfo
														.getJsonString(childs,
																"cityCode"));
										purchaseDemandChildItem
												.setCityName(JsonGetInfo
														.getJsonString(childs,
																"cityName"));
										purchaseDemandChildItem
												.setPrCode(JsonGetInfo
														.getJsonString(childs,
																"prCode"));
										Log.e("iiiiiiiiii", "prCode" + i);
										purchaseDemandChildItem
												.setCiCode(JsonGetInfo
														.getJsonString(childs,
																"ciCode"));
										purchaseDemandChildItem
												.setCoCode(JsonGetInfo
														.getJsonString(childs,
																"coCode"));
										purchaseDemandChildItem
												.setTwCode(JsonGetInfo
														.getJsonString(childs,
																"twCode"));
										purchaseDemandChildItem
												.setDemandId(JsonGetInfo
														.getJsonString(childs,
																"demandId"));
										Log.e("iiiiiiiiii", "demandId" + i);
										purchaseDemandChildItem.setDemandItemId(JsonGetInfo
												.getJsonString(childs,
														"demandItemId"));
										purchaseDemandChildItem
												.setPrice(JsonGetInfo
														.getJsonDouble(childs,
																"price"));
										purchaseDemandChildItem
												.setFloorPrice(JsonGetInfo
														.getJsonDouble(childs,
																"floorPrice"));
										purchaseDemandChildItem
												.setPlantType(JsonGetInfo
														.getJsonString(childs,
																"plantType"));
										Log.e("iiiiiiiiii", "plantType" + i);
										purchaseDemandChildItem.setClerkConfirm(JsonGetInfo
												.getJsonBoolean(childs,
														"clerkConfirm"));
										purchaseDemandChildItem
												.setNew(JsonGetInfo
														.getJsonBoolean(childs,
																"isNew"));
										purchaseDemandChildItem.setPlantTypeName(JsonGetInfo
												.getJsonString(childs,
														"plantTypeName"));
										purchaseDemandChildItem
												.setSpecText(JsonGetInfo
														.getJsonString(childs,
																"specText"));
										purchaseDemandChildItem
												.setUnitType(JsonGetInfo
														.getJsonString(childs,
																"unitType"));
										Log.e("iiiiiiiiii", "unitType" + i);
										purchaseDemandChildItem.setUnitTypeName(JsonGetInfo
												.getJsonString(childs,
														"unitTypeName"));
										purchaseDemandChildItem
												.setUse(JsonGetInfo
														.getJsonBoolean(childs,
																"isUse"));
										ArrayList<Pic> imagesJson = new ArrayList<Pic>();
										Log.e("iiiiiiiiii", "Pic" + i);
										JSONArray imagesJsons = JsonGetInfo
												.getJsonArray(childs,
														"imagesJson");
										Log.e("iiiiiiiiii", "imagesJsonPic" + i);
										for (int k = 0; k < imagesJsons
												.length(); k++) {
											JSONObject image = imagesJsons
													.getJSONObject(k);
											imagesJson.add(new Pic(
													JsonGetInfo.getJsonString(
															image, "id"),
													false, JsonGetInfo
															.getJsonString(
																	image,
																	"url"), k));

										}
										Log.e("iiiiiiiiii", "imagesJson" + i);
										purchaseDemandChildItem
												.setImagesJson(imagesJson);
										PurchaseDemandChildItems
												.add(purchaseDemandChildItem);
										Log.e("iiiiiiiiii",
												"purchaseDemandChildItem" + i);
									}
									PurchaseDemandGroupItem purchaseDemandGroupItem = new PurchaseDemandGroupItem();
									purchaseDemandGroupItem.setId(JsonGetInfo
											.getJsonString(groupJson, "id"));
									purchaseDemandGroupItem
											.setCreateBy(JsonGetInfo
													.getJsonString(groupJson,
															"createBy"));
									purchaseDemandGroupItem
											.setCreateDate(JsonGetInfo
													.getJsonString(groupJson,
															"createDate"));
									purchaseDemandGroupItem
											.setDemandId(JsonGetInfo
													.getJsonString(groupJson,
															"demandId"));
									purchaseDemandGroupItem.setName(JsonGetInfo
											.getJsonString(groupJson, "name"));
									purchaseDemandGroupItem
											.setMinDiameter(JsonGetInfo
													.getJsonInt(groupJson,
															"minDiameter"));
									purchaseDemandGroupItem
											.setMaxDiameter(JsonGetInfo
													.getJsonInt(groupJson,
															"maxDiameter"));
									purchaseDemandGroupItem
											.setMinMijing(JsonGetInfo
													.getJsonInt(groupJson,
															"minMijing"));
									purchaseDemandGroupItem
											.setMaxMijing(JsonGetInfo
													.getJsonInt(groupJson,
															"maxMijing"));
									purchaseDemandGroupItem
											.setMinDbh(JsonGetInfo.getJsonInt(
													groupJson, "minDbh"));
									purchaseDemandGroupItem
											.setMaxDbh(JsonGetInfo.getJsonInt(
													groupJson, "maxDbh"));
									purchaseDemandGroupItem
											.setMinHeight(JsonGetInfo
													.getJsonInt(groupJson,
															"minHeight"));
									purchaseDemandGroupItem
											.setMaxHeight(JsonGetInfo
													.getJsonInt(groupJson,
															"maxHeight"));
									purchaseDemandGroupItem
											.setMinCrown(JsonGetInfo
													.getJsonInt(groupJson,
															"minCrown"));
									purchaseDemandGroupItem
											.setMaxCrown(JsonGetInfo
													.getJsonInt(groupJson,
															"maxCrown"));
									purchaseDemandGroupItem
											.setMinLength(JsonGetInfo
													.getJsonInt(groupJson,
															"minLength"));
									purchaseDemandGroupItem
											.setMaxLength(JsonGetInfo
													.getJsonInt(groupJson,
															"maxLength"));
									purchaseDemandGroupItem
											.setMinOffbarHeight(JsonGetInfo
													.getJsonInt(groupJson,
															"minOffbarHeight"));
									purchaseDemandGroupItem
											.setMaxOffbarHeight(JsonGetInfo
													.getJsonInt(groupJson,
															"maxOffbarHeight"));
									purchaseDemandGroupItem
											.setCount(JsonGetInfo.getJsonInt(
													groupJson, "count"));
									purchaseDemandGroupItem
											.setQuoteCount(JsonGetInfo
													.getJsonInt(groupJson,
															"quoteCount"));
									purchaseDemandGroupItem.setUnit(JsonGetInfo
											.getJsonString(groupJson, "unit"));
									purchaseDemandGroupItem
											.setQuoteUnit(JsonGetInfo
													.getJsonString(groupJson,
															"quoteUnit"));
									purchaseDemandGroupItem
											.setDiameter(JsonGetInfo
													.getJsonString(groupJson,
															"diameter"));
									purchaseDemandGroupItem
											.setMijing(JsonGetInfo
													.getJsonString(groupJson,
															"mijing"));
									purchaseDemandGroupItem.setDbh(JsonGetInfo
											.getJsonString(groupJson, "dbh"));
									purchaseDemandGroupItem
											.setHeight(JsonGetInfo
													.getJsonString(groupJson,
															"height"));
									purchaseDemandGroupItem
											.setCrown(JsonGetInfo
													.getJsonString(groupJson,
															"crown"));
									purchaseDemandGroupItem
											.setLength(JsonGetInfo
													.getJsonString(groupJson,
															"length"));
									purchaseDemandGroupItem
											.setOffbarHeight(JsonGetInfo
													.getJsonString(groupJson,
															"offbarHeight"));
									purchaseDemandGroupItem
											.setSpecText(JsonGetInfo
													.getJsonString(groupJson,
															"specText"));
									purchaseDemandGroupItem
											.setRemarks(JsonGetInfo
													.getJsonString(groupJson,
															"remarks"));
									purchaseDemandGroupItem
											.setUsedQuote(JsonGetInfo
													.getJsonBoolean(groupJson,
															"usedQuote"));
									purchaseDemandGroupItem
											.setQuotePrice(JsonGetInfo
													.getJsonDouble(groupJson,
															"quotePrice"));
									purchaseDemandGroupItem
											.setQuoteTotalPrice(JsonGetInfo
													.getJsonDouble(groupJson,
															"quoteTotalPrice"));
									groupData.add(purchaseDemandGroupItem);
									childData.add(PurchaseDemandChildItems);

								}
								Log.e("myAdapter", groupData.toString());
								myAdapter = new PurchaseDemandItemBaseExpandableListAdapter(
										PurchaseDemandItem4BuyActivity.this,
										groupData, childData, true, status);
								Log.e("childData", childData.toString());
								myAdapter
										.setOnGoodsCheckedChangeListener(PurchaseDemandItem4BuyActivity.this);
								Log.e("groupData", groupData.toString());
								initView();
								initEvents();
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
						if (hud != null) {
							hud.dismiss();
						}
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;

	}

	private void initView() {
		// 在drawable文件夹下新建了indicator.xml，下面这个语句也可以实现group伸展收缩时的indicator变化
		// expandList.setGroupIndicator(this.getResources().getDrawable(R.drawable.indicator));
		expandList.setGroupIndicator(null);// 这里不显示系统默认的group indicator
		expandList.setAdapter(myAdapter);
		registerForContextMenu(expandList);// 给ExpandListView添加上下文菜单
		// for (int i = 0; i < myAdapter.getGroupCount(); i++) {
		// expandList.expandGroup(i);
		// }
		// 展开全部
	}

	private void initEvents() {
		// child子项的单击事件
		expandList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// Toast.makeText(
				// StoreTypeActivity.this,
				// "你单击了："
				// + myAdapter.getChild(groupPosition,
				// childPosition), Toast.LENGTH_SHORT)
				// .show();
				// typeEx = new TypeEx(groupPosition, childPosition, childData
				// .get(groupPosition).get(childPosition).getName(),
				// childData.get(groupPosition).get(childPosition).getId());
				return true;
			}
		});

	}

	/*
	 * 添加上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		// ExpandableListView.ExpandableListContextMenuInfo info =
		// (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		// int type = ExpandableListView
		// .getPackedPositionType(info.packedPosition);
		// if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
		// menu.setHeaderTitle("Options");
		// menu.add(0, CONTEXT_MENU_GROUP_DELETE, 0, "删除");
		// menu.add(0, CONTEXT_MENU_GROUP_RENAME, 0, "重命名");
		// }
		// if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
		// menu.setHeaderTitle("Options");
		// menu.add(1, CONTEXT_MENU_CHILD_EDIT, 0, "编辑");
		// menu.add(1, CONTEXT_MENU_CHILD_DELETE, 0, "删除");
		// }

	}

	/*
	 * 每个菜单项的具体点击事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_MENU_GROUP_DELETE:
			Toast.makeText(this, "这是group的删除", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_GROUP_RENAME:
			Toast.makeText(this, "这是group的重命名", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_CHILD_EDIT:
			Toast.makeText(this, "这是child的编辑", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_CHILD_DELETE:
			Toast.makeText(this, "这是child的删除", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.button31:
			usedQuote = "";
			onRefresh();
			break;
		case R.id.button32:
			usedQuote = "true";
			onRefresh();
			break;
		case R.id.button33:
			usedQuote = "false";
			onRefresh();
			break;
		default:
			// Nothing to do
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(1);
		finish();
		super.onBackPressed();
	}

	@Override
	public void onGoodsCheckedChange(int position, String t, boolean isRefresh) {
		// TODO Auto-generated method stub
		if (isRefresh) {
			try {
				JSONObject jsonObject = new JSONObject(t.toString());
				JSONObject data = JsonGetInfo.getJSONObject(jsonObject, "data");
				int usedCount = JsonGetInfo.getJsonInt(data, "usedCount");
				int itemCount = JsonGetInfo.getJsonInt(data, "itemCount");
				int unUsedCount = JsonGetInfo.getJsonInt(data, "unUsedCount");
				double totalPrice = JsonGetInfo.getJsonDouble(data,
						"totalPrice");
				if (usedCount > 0 && totalPrice > 0) {
					StringFormatUtil fillColor = new StringFormatUtil(
							PurchaseDemandItem4BuyActivity.this, "已确认"
									+ usedCount + "种，", usedCount + "",
							R.color.red).fillColor();
					StringFormatUtil fillColor2 = new StringFormatUtil(
							PurchaseDemandItem4BuyActivity.this,
							"共" + ValueGetInfo.formatFloatNumber(totalPrice)
									+ "元。",
							ValueGetInfo.formatFloatNumber(totalPrice),
							R.color.red).fillColor();
					tv_count.setText(fillColor.getResult());
					tv_price.setText(fillColor2.getResult());
				} else {
					tv_count.setText("暂无已确认的报价");
					tv_price.setText("");
				}
				if (itemCount > 0) {
					button31.setText("全部（" + itemCount + "）");
				} else {
					button31.setText("全部");
				}
				if (usedCount > 0) {
					button32.setText("已确定（" + usedCount + "）");
				} else {
					button32.setText("已确定");
				}
				if (unUsedCount > 0) {
					button33.setText("未确定（" + unUsedCount + "）");
				} else {
					button33.setText("未确定");
				}
				JSONObject groupJson = JsonGetInfo.getJSONObject(data, "item");
				List<PurchaseDemandChildItem> PurchaseDemandChildItems = new ArrayList<PurchaseDemandChildItem>();
				JSONArray childsJson = JsonGetInfo.getJsonArray(groupJson,
						"quoteList");

				for (int i = 0; i < childsJson.length(); i++) {
					JSONObject childs = childsJson.getJSONObject(i);
					PurchaseDemandChildItem purchaseDemandChildItem = new PurchaseDemandChildItem();
					purchaseDemandChildItem.setId(JsonGetInfo.getJsonString(
							childs, "id"));
					purchaseDemandChildItem.setRemarks(JsonGetInfo
							.getJsonString(childs, "remarks"));
					purchaseDemandChildItem.setCreateBy(JsonGetInfo
							.getJsonString(childs, "createBy"));
					purchaseDemandChildItem.setCreateDate(JsonGetInfo
							.getJsonString(childs, "createDate"));
					purchaseDemandChildItem.setCityCode(JsonGetInfo
							.getJsonString(childs, "cityCode"));
					purchaseDemandChildItem.setCityName(JsonGetInfo
							.getJsonString(childs, "cityName"));
					purchaseDemandChildItem.setPrCode(JsonGetInfo
							.getJsonString(childs, "prCode"));
					purchaseDemandChildItem.setCiCode(JsonGetInfo
							.getJsonString(childs, "ciCode"));
					purchaseDemandChildItem.setCoCode(JsonGetInfo
							.getJsonString(childs, "coCode"));
					purchaseDemandChildItem.setTwCode(JsonGetInfo
							.getJsonString(childs, "twCode"));
					purchaseDemandChildItem.setDemandId(JsonGetInfo
							.getJsonString(childs, "demandId"));
					purchaseDemandChildItem.setDemandItemId(JsonGetInfo
							.getJsonString(childs, "demandItemId"));
					purchaseDemandChildItem.setPrice(JsonGetInfo.getJsonDouble(
							childs, "price"));
					purchaseDemandChildItem.setFloorPrice(JsonGetInfo
							.getJsonDouble(childs, "floorPrice"));
					purchaseDemandChildItem.setPlantType(JsonGetInfo
							.getJsonString(childs, "plantType"));
					purchaseDemandChildItem.setClerkConfirm(JsonGetInfo
							.getJsonBoolean(childs, "clerkConfirm"));
					purchaseDemandChildItem.setNew(JsonGetInfo.getJsonBoolean(
							childs, "isNew"));
					purchaseDemandChildItem.setPlantTypeName(JsonGetInfo
							.getJsonString(childs, "plantTypeName"));
					purchaseDemandChildItem.setSpecText(JsonGetInfo
							.getJsonString(childs, "specText"));
					purchaseDemandChildItem.setUnitType(JsonGetInfo
							.getJsonString(childs, "unitType"));
					purchaseDemandChildItem.setUnitTypeName(JsonGetInfo
							.getJsonString(childs, "unitTypeName"));
					purchaseDemandChildItem.setUse(JsonGetInfo.getJsonBoolean(
							childs, "isUse"));
					ArrayList<Pic> imagesJson = new ArrayList<Pic>();
					JSONArray imagesJsons = JsonGetInfo.getJsonArray(childs,
							"imagesJson");
					for (int k = 0; k < imagesJsons.length(); k++) {
						JSONObject image = imagesJsons.getJSONObject(k);
						imagesJson.add(new Pic(JsonGetInfo.getJsonString(image,
								"id"), false, JsonGetInfo.getJsonString(image,
								"url"), k));

					}
					purchaseDemandChildItem.setImagesJson(imagesJson);
					PurchaseDemandChildItems.add(purchaseDemandChildItem);
				}
				PurchaseDemandGroupItem purchaseDemandGroupItem = new PurchaseDemandGroupItem();
				purchaseDemandGroupItem.setId(JsonGetInfo.getJsonString(
						groupJson, "id"));
				purchaseDemandGroupItem.setCreateBy(JsonGetInfo.getJsonString(
						groupJson, "createBy"));
				purchaseDemandGroupItem.setCreateDate(JsonGetInfo
						.getJsonString(groupJson, "createDate"));
				purchaseDemandGroupItem.setDemandId(JsonGetInfo.getJsonString(
						groupJson, "demandId"));
				purchaseDemandGroupItem.setName(JsonGetInfo.getJsonString(
						groupJson, "name"));
				purchaseDemandGroupItem.setMinDiameter(JsonGetInfo.getJsonInt(
						groupJson, "minDiameter"));
				purchaseDemandGroupItem.setMaxDiameter(JsonGetInfo.getJsonInt(
						groupJson, "maxDiameter"));
				purchaseDemandGroupItem.setMinMijing(JsonGetInfo.getJsonInt(
						groupJson, "minMijing"));
				purchaseDemandGroupItem.setMaxMijing(JsonGetInfo.getJsonInt(
						groupJson, "maxMijing"));
				purchaseDemandGroupItem.setMinDbh(JsonGetInfo.getJsonInt(
						groupJson, "minDbh"));
				purchaseDemandGroupItem.setMaxDbh(JsonGetInfo.getJsonInt(
						groupJson, "maxDbh"));
				purchaseDemandGroupItem.setMinHeight(JsonGetInfo.getJsonInt(
						groupJson, "minHeight"));
				purchaseDemandGroupItem.setMaxHeight(JsonGetInfo.getJsonInt(
						groupJson, "maxHeight"));
				purchaseDemandGroupItem.setMinCrown(JsonGetInfo.getJsonInt(
						groupJson, "minCrown"));
				purchaseDemandGroupItem.setMaxCrown(JsonGetInfo.getJsonInt(
						groupJson, "maxCrown"));
				purchaseDemandGroupItem.setMinLength(JsonGetInfo.getJsonInt(
						groupJson, "minLength"));
				purchaseDemandGroupItem.setMaxLength(JsonGetInfo.getJsonInt(
						groupJson, "maxLength"));
				purchaseDemandGroupItem.setMinOffbarHeight(JsonGetInfo
						.getJsonInt(groupJson, "minOffbarHeight"));
				purchaseDemandGroupItem.setMaxOffbarHeight(JsonGetInfo
						.getJsonInt(groupJson, "maxOffbarHeight"));
				purchaseDemandGroupItem.setCount(JsonGetInfo.getJsonInt(
						groupJson, "count"));
				purchaseDemandGroupItem.setQuoteCount(JsonGetInfo.getJsonInt(
						groupJson, "quoteCount"));
				purchaseDemandGroupItem.setUnit(JsonGetInfo.getJsonString(
						groupJson, "unit"));
				purchaseDemandGroupItem.setQuoteUnit(JsonGetInfo.getJsonString(
						groupJson, "quoteUnit"));
				purchaseDemandGroupItem.setDiameter(JsonGetInfo.getJsonString(
						groupJson, "diameter"));
				purchaseDemandGroupItem.setMijing(JsonGetInfo.getJsonString(
						groupJson, "mijing"));
				purchaseDemandGroupItem.setDbh(JsonGetInfo.getJsonString(
						groupJson, "dbh"));
				purchaseDemandGroupItem.setHeight(JsonGetInfo.getJsonString(
						groupJson, "height"));
				purchaseDemandGroupItem.setCrown(JsonGetInfo.getJsonString(
						groupJson, "crown"));
				purchaseDemandGroupItem.setLength(JsonGetInfo.getJsonString(
						groupJson, "length"));
				purchaseDemandGroupItem.setOffbarHeight(JsonGetInfo
						.getJsonString(groupJson, "offbarHeight"));
				purchaseDemandGroupItem.setSpecText(JsonGetInfo.getJsonString(
						groupJson, "specText"));
				purchaseDemandGroupItem.setRemarks(JsonGetInfo.getJsonString(
						groupJson, "remarks"));
				purchaseDemandGroupItem.setUsedQuote(JsonGetInfo
						.getJsonBoolean(groupJson, "usedQuote"));
				purchaseDemandGroupItem.setQuotePrice(JsonGetInfo
						.getJsonDouble(groupJson, "quotePrice"));
				purchaseDemandGroupItem.setQuoteTotalPrice(JsonGetInfo
						.getJsonDouble(groupJson, "quoteTotalPrice"));
				// groupData.add(purchaseDemandGroupItem);
				// childData.add(
				// PurchaseDemandChildItems);
				groupData.set(position, purchaseDemandGroupItem);
				childData.set(position, PurchaseDemandChildItems);
				myAdapter.notifyDataSetChanged();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// groupData.remove(position);
			// childData.remove(position);
			// myAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_call:
			boolean requesCallPhonePermissions = new PermissionUtils(
					PurchaseDemandItem4BuyActivity.this)
					.requesCallPhonePermissions(200);
			if (requesCallPhonePermissions) {
				CallPhone(clerkPhone);
			}
			break;
		case R.id.tv_btn:
			button31.setChecked(true);
			usedQuote = "";
			name = et_search.getText().toString();
			onRefresh();
			break;

		default:
			break;
		}
	}

	private void CallPhone(final String displayPhone) {
		// TODO Auto-generated method stub
		if (!"".equals(displayPhone)) {
			new AlertDialog(PurchaseDemandItem4BuyActivity.this).builder()
					.setTitle(displayPhone)
					.setPositiveButton("呼叫", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + displayPhone));
							// Intent intent = new Intent(Intent.ACTION_DIAL,
							// Uri
							// .parse("tel:" + displayPhone));
							startActivity(intent);
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();
		} else {
			Toast.makeText(PurchaseDemandItem4BuyActivity.this, "暂无联系电话！",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onRefresh() {
		groupData.clear();
		childData.clear();
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}
		initDatas();

	}

}
