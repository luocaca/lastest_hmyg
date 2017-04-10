package com.hldj.hmyg.saler.purchase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparatorSubscribe;
import com.example.sortlistview.SideBar;
import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.hhl.library.FlowTagLayout;
import com.hldj.hmyg.BFragment;
import com.hldj.hmyg.LoginActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.SeedlingMarketSearchActivity;
import com.hldj.hmyg.broker.SellectMarketPriceActivity;
import com.hldj.hmyg.broker.bean.SellectPrice;
import com.hldj.hmyg.buyer.StorePurchaseListActivity;
import com.hldj.hmyg.saler.SubscribeManagerListActivity;
import com.hldj.hmyg.saler.bean.ParamsList;
import com.hldj.hmyg.saler.bean.Subscribe;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.StringFormatUtil;
import com.hy.utils.TagAdapter;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.drakeet.materialdialog.MaterialDialog;
import me.kaede.tagview.OnTagDeleteListener;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;


@SuppressLint({ "NewApi", "ResourceAsColor" })
public class PurchasePyMapActivity extends BaseSecondActivity implements

OnCheckedChangeListener, OnWheelChangedListener, IXListViewListener {

	private RelativeLayout rl_choose_type;
	private ImageView iv_seller_arrow2;
	private ImageView iv_seller_arrow3;
	private XListView listview;
	private String orderBy = "";
	private String priceSort = "";
	private String publishDateSort = "";
	private ArrayList<Subscribe> datas = new ArrayList<Subscribe>();
	private ArrayList<PurchaseList> puchaseDatas = new ArrayList<PurchaseList>();
	private int pageSize = 10;
	private int pageIndex = 0;
	private MapSearchAdapter Adapter;
	boolean getdata; // 避免刷新多出数据
	private String noteType = "1";
	FinalHttp finalHttp = new FinalHttp();
	private String minSpec = "";
	private String maxSpec = "";
	private String minHeight = "";
	private String maxHeight = "";
	private String minCrown = "";
	private String maxCrown = "";
	private String name = "";
	private String cityCode = "";
	private String cityName = "";
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TagView tagView;
	MaterialDialog mMaterialDialog;
	private SellectPrice sellectPrice;
	private String[] keySort = new String[] { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparatorSubscribe pinyinComparator;
	private SideBar sideBar;
	private EditText et_search;
	private RadioButton button31;
	private RadioButton button32;
	private XListView lv;
	private FrameLayout fl_type;
	private PurchaseListAdapter listAdapter;
	private ArrayList<String> lanmus = new ArrayList<String>();
	private ViewPager pager;
	private PagerSlidingTabStrip tabs;
	private DisplayMetrics dm;
	String type = "quoting";
	private LinearLayout ll_01;
	private TextView tv_xiaoxitishi;
	private ImageView iv_close;
	private BaseAnimatorSet mBasIn;
	private BaseAnimatorSet mBasOut;

	public void setBasIn(BaseAnimatorSet bas_in) {
		this.mBasIn = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.mBasOut = bas_out;
	}

	private Editor e;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_py_map);
		SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
		button31 = (RadioButton) findViewById(R.id.button31);
		button32 = (RadioButton) findViewById(R.id.button32);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		tv_xiaoxitishi = (TextView) findViewById(R.id.tv_xiaoxitishi);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		e = MyApplication.Userinfo.edit();
		mBasIn = new BounceTopEnter();
		mBasOut = new SlideBottomExit();
		setOverflowShowingAlways();
		dm = getResources().getDisplayMetrics();
		lanmus.add("按采购单");
		lanmus.add("按品种");
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		pager.setOffscreenPageLimit(lanmus.size());
		tabs.setViewPager(pager);
		setTabsValue();
		showNotice(0);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparatorSubscribe();
		mMaterialDialog = new MaterialDialog(this);
		sellectPrice = new SellectPrice();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		rl_choose_type = (RelativeLayout) findViewById(R.id.rl_choose_type);
		RelativeLayout rl_choose_price = (RelativeLayout) findViewById(R.id.rl_choose_price);
		RelativeLayout rl_choose_time = (RelativeLayout) findViewById(R.id.rl_choose_time);
		RelativeLayout rl_choose_screen = (RelativeLayout) findViewById(R.id.rl_choose_screen);
		RelativeLayout RelativeLayout2 = (RelativeLayout) findViewById(R.id.RelativeLayout2);
		fl_type = (FrameLayout) findViewById(R.id.fl_type);

		iv_seller_arrow2 = (ImageView) findViewById(R.id.iv_seller_arrow2);
		iv_seller_arrow3 = (ImageView) findViewById(R.id.iv_seller_arrow3);
		listview = (XListView) findViewById(R.id.listview);
		lv = (XListView) findViewById(R.id.lv);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setDivider(null);
		lv.setPullLoadEnable(false);
		lv.setPullRefreshEnable(false);
		listview.setXListViewListener(this);
		segmented3.setOnCheckedChangeListener(this);
		button31.setChecked(true);
		tagView = (TagView) this.findViewById(R.id.tagview);
		tagView.setOnTagDeleteListener(new OnTagDeleteListener() {

			@Override
			public void onTagDeleted(int position, me.kaede.tagview.Tag tag) {
				// TODO Auto-generated method stub
				if (tag.id == 1) {
					cityCode = "";
					onRefresh();
				} else if (tag.id == 2) {
					name = "";
					onRefresh();
				} else if (tag.id == 3) {
					minSpec = "";
					maxSpec = "";
					onRefresh();
				} else if (tag.id == 4) {
					minHeight = "";
					maxHeight = "";
					onRefresh();
				} else if (tag.id == 5) {
					minCrown = "";
					maxCrown = "";
					onRefresh();
				}

			}
		});
		// init();
		initData();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		TextView dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = Adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listview.setSelection(position);
				}

			}
		});
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (et_search.getText().toString().length() != 0) {

						Intent intent = new Intent(PurchasePyMapActivity.this,
								SeedlingMarketSearchActivity.class);
						intent.putExtra("name", et_search.getText().toString());
						startActivity(intent);
					}

				}
				return false;
			}
		});
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		rl_choose_type.setOnClickListener(multipleClickProcess);
		rl_choose_price.setOnClickListener(multipleClickProcess);
		rl_choose_time.setOnClickListener(multipleClickProcess);
		rl_choose_screen.setOnClickListener(multipleClickProcess);
		btn_back.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);
		RelativeLayout2.setOnClickListener(multipleClickProcess);
		iv_close.setOnClickListener(multipleClickProcess);

	}

	private void showNotice(int position) {
		// TODO Auto-generated method stub
		if (0 == position) {
			if (MyApplication.Userinfo.getBoolean("NeedShowquoting", true)) {
				tv_xiaoxitishi.setText("采购中：已经确认采购且即将调苗的采购项目。");
				ll_01.setVisibility(View.VISIBLE);
			} else {
				ll_01.setVisibility(View.GONE);
			}
		} else if (1 == position) {
			if (MyApplication.Userinfo.getBoolean("NeedShowunquote", true)) {
				tv_xiaoxitishi.setText("待采购：已经确认采购但还未确定调苗时间的采购项目。");
				ll_01.setVisibility(View.VISIBLE);
			} else {
				ll_01.setVisibility(View.GONE);
			}
		}
	}

	private void DialogNoti(String string) {
		// TODO Auto-generated method stub

		final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
				PurchasePyMapActivity.this);
		dialog.title("温馨提示").content(string)
		//
				.btnText("不再提示", "取消")//
				.showAnim(mBasIn)//
				.dismissAnim(mBasOut)//
				.show();

		dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
					@Override
					public void onBtnClick() {
						if ("quoting".equals(type)) {
							e.putBoolean("NeedShowquoting", false);
						} else if ("unquote".equals(type)) {
							e.putBoolean("NeedShowbangwo", false);
						}
						e.commit();
						dialog.dismiss();
						ll_01.setVisibility(View.GONE);
					}
				}, new OnBtnClickL() {// right btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
						ll_01.setVisibility(View.GONE);

					}
				});

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return lanmus.get(position);
		}

		@Override
		public int getCount() {
			return lanmus.size();
		}

		@Override
		public Fragment getItem(int position) {
			BFragment fragment = new BFragment();
			return fragment;

		}
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setTabsValue() {

		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		tabs.setDividerPadding(5);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 13, dm));
		tabs.setTextColor(getResources().getColor(R.color.gray));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(getResources().getColor(R.color.main_color));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(getResources().getColor(R.color.main_color));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				if (arg0 == 0) {
					fl_type.setVisibility(View.GONE);
					listview.setVisibility(View.VISIBLE);
				} else {
					fl_type.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
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
				case R.id.rl_choose_type:
					showCitys();
					break;

				case R.id.iv_close:
					// TODO Auto-generated method stub
					DialogNoti("");
					break;
				case R.id.rl_choose_price:
					if ("".equals(priceSort)) {
						priceSort = "price_asc";
						iv_seller_arrow2
								.setImageResource(R.drawable.icon_seller_arrow2);
					} else if ("price_asc".equals(priceSort)) {
						priceSort = "price_desc";
						iv_seller_arrow2
								.setImageResource(R.drawable.icon_seller_arrow3);
					} else if ("price_desc".equals(priceSort)) {
						priceSort = "";
						iv_seller_arrow2
								.setImageResource(R.drawable.icon_seller_arrow1);
					}
					onRefresh();
					break;
				case R.id.rl_choose_time:
					if ("".equals(publishDateSort)) {
						publishDateSort = "createDate_asc";
						iv_seller_arrow3
								.setImageResource(R.drawable.icon_seller_arrow2);
					} else if ("createDate_asc".equals(publishDateSort)) {
						publishDateSort = "createDate_desc";
						iv_seller_arrow3
								.setImageResource(R.drawable.icon_seller_arrow3);
					} else if ("createDate_desc".equals(publishDateSort)) {
						publishDateSort = "";
						iv_seller_arrow3
								.setImageResource(R.drawable.icon_seller_arrow1);
					}
					onRefresh();
					break;
				case R.id.RelativeLayout2:
					Intent toSellectActivity = new Intent(
							PurchasePyMapActivity.this,
							SellectMarketPriceActivity.class);
					toSellectActivity.putExtra("sellectPrice", sellectPrice);
					startActivityForResult(toSellectActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				// case R.id.id_tv_edit_all:
				// // Intent toMyMarketListActivity = new Intent(
				// // SeedlingMarketPyMapActivity.this,
				// // MyMarketListActivity.class);
				// // startActivityForResult(toMyMarketListActivity, 1);
				// // overridePendingTransition(R.anim.slide_in_left,
				// // R.anim.slide_out_right);
				// if (et_search.getText().toString().length() != 0) {
				//
				// Intent intent = new Intent(PurchasePyMapActivity.this,
				// SeedlingMarketSearchActivity.class);
				// intent.putExtra("name", et_search.getText().toString());
				// startActivity(intent);
				// }
				// break;
				case R.id.id_tv_edit_all:
					if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
						Intent toLoginActivity = new Intent(
								PurchasePyMapActivity.this, LoginActivity.class);
						startActivityForResult(toLoginActivity, 4);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
					} else {
						Intent toSubscribeManagerListActivity = new Intent(
								PurchasePyMapActivity.this,
								SubscribeManagerListActivity.class);
						startActivityForResult(toSubscribeManagerListActivity,
								1);
					}

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 9) {
			onRefresh();
		} else if (resultCode == 8) {
			onRefresh();
		} else if (resultCode == 16) {
			if (data.getExtras() != null) {
				Bundle extras = data.getExtras();
				sellectPrice = (SellectPrice) extras.get("sellectPrice");
				List<Tag> tags = tagView.getTags();
				for (int i = 0; i < tags.size(); i++) {
					Log.e("List<Tag> tags", "tagView" + i);
					tagView.remove(i);
					// if (tags.get(i).id == 1 || tags.get(i).id == 2
					// || tags.get(i).id == 3 || tags.get(i).id == 4
					// || tags.get(i).id == 5 || tags.get(i).id == 6
					// || tags.get(i).id == 7) {
					// tagView.remove(i);
					// }
				}
				if (!"".equals(sellectPrice.getName())) {
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag("品名："
							+ sellectPrice.getName());
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 1;
					tagView.addTag(tag);
				}
				if (!"".equals(sellectPrice.getDiameter())) {
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag("地径："
							+ sellectPrice.getDiameter());
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 2;
					tagView.addTag(tag);
				}
				if (!"".equals(sellectPrice.getDbh())) {
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag("规格："
							+ sellectPrice.getDbh());
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 3;
					tagView.addTag(tag);
				}
				if (!"".equals(sellectPrice.getCityName())) {
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag("区域："
							+ sellectPrice.getCityName());
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 4;
					tagView.addTag(tag);
				}
				if (!"".equals(sellectPrice.getPlantType())) {
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
							sellectPrice.getPlantTypeName());
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 5;
					tagView.addTag(tag);
				}
				if (!"".equals(sellectPrice.getQualityType())) {
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
							sellectPrice.getQualityTypeName());
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 6;
					tagView.addTag(tag);
				}
				if (!"".equals(sellectPrice.getQualityGrade())) {
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
							sellectPrice.getQualityGradeName());
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 7; // 1 品名 2.规格 3.地径 4.区域 5.种植类型 6.品质 7.等级
					tagView.addTag(tag);
				}

			}
			onRefresh();
		} else if (resultCode == 18) {
			Intent toSellectActivity = new Intent(PurchasePyMapActivity.this,
					SellectMarketPriceActivity.class);
			sellectPrice = new SellectPrice();
			toSellectActivity.putExtra("sellectPrice", sellectPrice);
			startActivityForResult(toSellectActivity, 1);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		listview.setPullLoadEnable(false);
		pageIndex = 0;
		puchaseDatas.clear();
		if (listAdapter == null) {
			listAdapter = new PurchaseListAdapter(PurchasePyMapActivity.this,
					puchaseDatas);
			listview.setAdapter(listAdapter);
		} else {
			listAdapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			initData();
		}
		onLoad();
	}

	private void init() {
		// TODO Auto-generated method stub
		datas.clear();
		if (Adapter != null) {
			Adapter.notifyDataSetChanged();
		}
		getdata = false;
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("type", type);
		finalHttp.post(GetServerUrl.getUrl() + "purchase/pyMap", params,
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
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject pyMaps = JsonGetInfo.getJSONObject(
										data, "pyMaps");
								for (int r = 0; r < keySort.length; r++) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(pyMaps, keySort[r]);
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject2 = jsonArray
												.getJSONObject(i);
										Subscribe hMap = new Subscribe();
										hMap.setSortLetters(keySort[r]);
										hMap.setId(JsonGetInfo.getJsonString(
												jsonObject2, "id"));
										hMap.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject2,
														"createBy"));
										hMap.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject2,
														"createDate"));
										hMap.setName(JsonGetInfo.getJsonString(
												jsonObject2, "name"));
										hMap.setAliasName(JsonGetInfo
												.getJsonString(jsonObject2,
														"aliasName"));
										hMap.setParentId(JsonGetInfo
												.getJsonString(jsonObject2,
														"parentId"));
										hMap.setParentName(JsonGetInfo
												.getJsonString(jsonObject2,
														"parentName"));
										hMap.setFullPinyin(JsonGetInfo
												.getJsonString(jsonObject2,
														"firstPinyin"));
										hMap.setFirstPinyin(JsonGetInfo
												.getJsonString(jsonObject2,
														"firstPinyin"));
										hMap.setSeedlingParams(JsonGetInfo
												.getJsonString(jsonObject2,
														"seedlingParams"));
										hMap.setIsTop(JsonGetInfo
												.getJsonString(jsonObject2,
														"isTop"));
										hMap.setParentSeedlingParams(JsonGetInfo
												.getJsonString(jsonObject2,
														"parentSeedlingParams"));
										hMap.setSubscribeId(JsonGetInfo
												.getJsonString(jsonObject2,
														"subscribeId"));
										hMap.setLevel(JsonGetInfo.getJsonInt(
												jsonObject2, "level"));
										hMap.setSort(JsonGetInfo.getJsonInt(
												jsonObject2, "sort"));
										hMap.setCountPurchaseBysubscribeJson(JsonGetInfo
												.getJsonInt(jsonObject2,
														"countPurchaseBysubscribeJson"));
										ArrayList<String> str_plantTypeLists = new ArrayList<String>();
										ArrayList<String> str_plantTypeList_ids_s = new ArrayList<String>();
										ArrayList<String> str_qualityTypeLists = new ArrayList<String>();
										ArrayList<String> str_qualityTypeList_ids = new ArrayList<String>();
										ArrayList<String> str_qualityGradeLists = new ArrayList<String>();
										ArrayList<String> str_qualityGradeList_ids = new ArrayList<String>();
										ArrayList<ParamsList> paramsLists = new ArrayList<ParamsList>();
										JSONArray paramsList = JsonGetInfo
												.getJsonArray(jsonObject2,
														"paramsList");
										JSONArray qualityTypeList = JsonGetInfo
												.getJsonArray(jsonObject2,
														"qualityTypeList");
										JSONArray plantTypeList = JsonGetInfo
												.getJsonArray(jsonObject2,
														"plantTypeList");
										JSONArray qualityGradeList = JsonGetInfo
												.getJsonArray(jsonObject2,
														"qualityGradeList");
										for (int m = 0; m < paramsList.length(); m++) {

											JSONObject jsonObject3 = paramsList
													.getJSONObject(m);
											paramsLists.add(new ParamsList(
													JsonGetInfo.getJsonString(
															jsonObject3,
															"value"),
													JsonGetInfo.getJsonBoolean(
															jsonObject3,
															"required")));
										}
										for (int o = 0; o < qualityTypeList
												.length(); o++) {
											JSONObject qualityType = qualityTypeList
													.getJSONObject(o);
											str_qualityTypeLists.add(JsonGetInfo
													.getJsonString(qualityType,
															"text"));
											str_qualityTypeList_ids.add(JsonGetInfo
													.getJsonString(qualityType,
															"value"));
										}
										for (int p = 0; p < plantTypeList
												.length(); p++) {
											JSONObject plantType1 = plantTypeList
													.getJSONObject(p);
											str_plantTypeLists.add(JsonGetInfo
													.getJsonString(plantType1,
															"text"));
											str_plantTypeList_ids_s
													.add(JsonGetInfo
															.getJsonString(
																	plantType1,
																	"value"));
										}
										for (int q = 0; q < qualityGradeList
												.length(); q++) {
											JSONObject qualityGrade = qualityGradeList
													.getJSONObject(q);
											str_qualityGradeLists.add(JsonGetInfo
													.getJsonString(
															qualityGrade,
															"text"));
											str_qualityGradeList_ids.add(JsonGetInfo
													.getJsonString(
															qualityGrade,
															"value"));
										}
										hMap.setStr_plantTypeLists(str_plantTypeLists);
										hMap.setStr_plantTypeList_ids_s(str_plantTypeList_ids_s);
										hMap.setStr_qualityGradeLists(str_qualityGradeLists);
										hMap.setStr_qualityGradeList_ids(str_qualityGradeList_ids);
										hMap.setStr_qualityTypeLists(str_qualityTypeLists);
										hMap.setStr_qualityTypeList_ids(str_qualityTypeList_ids);
										hMap.setParamsLists(paramsLists);
										hMap.setEdit(false);
										datas.add(hMap);

									}
									// pageIndex++;
								}
								if (Adapter == null) {
									Adapter = new MapSearchAdapter(
											PurchasePyMapActivity.this, datas);
									lv.setAdapter(Adapter);
									lv.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int position, long arg3) {
										}
									});

								} else {
									Adapter.notifyDataSetChanged();
								}
								if (datas.size() > 0) {
									sideBar.setVisibility(View.VISIBLE);
									// 根据a-z进行排序源数据
									Collections.sort(datas, pinyinComparator);
								} else {
									sideBar.setVisibility(View.GONE);
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
		getdata = true;
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("type", type);
		finalHttp.post(GetServerUrl.getUrl() + "purchase/purchaseList", params,
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
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(data, "page");
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
										PurchaseList purchaseList = new PurchaseList();
										purchaseList.setId(JsonGetInfo
												.getJsonString(jsonObject4,
														"id"));
										purchaseList.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject4,
														"createBy"));
										purchaseList.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"createDate"));
										purchaseList.setCityCode(JsonGetInfo
												.getJsonString(jsonObject4,
														"cityCode"));
										purchaseList.setCityName(JsonGetInfo
												.getJsonString(jsonObject4,
														"cityName"));
										purchaseList.setPrCode(JsonGetInfo
												.getJsonString(jsonObject4,
														"prCode"));
										purchaseList.setCiCode(JsonGetInfo
												.getJsonString(jsonObject4,
														"ciCode"));
										purchaseList.setCoCode(JsonGetInfo
												.getJsonString(jsonObject4,
														"coCode"));
										purchaseList.setTwCode(JsonGetInfo
												.getJsonString(jsonObject4,
														"twCode"));
										purchaseList.setNum(JsonGetInfo
												.getJsonString(jsonObject4,
														"num"));
										purchaseList.setProjectName(JsonGetInfo
												.getJsonString(jsonObject4,
														"projectName"));
										purchaseList.setReceiptDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"receiptDate"));
										purchaseList.setValidity(JsonGetInfo
												.getJsonString(jsonObject4,
														"validity"));
										purchaseList.setPublishDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"publishDate"));
										purchaseList.setCloseDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"closeDate"));
										purchaseList.setNeedInvoice(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"needInvoice"));
										purchaseList.setBuyerId(JsonGetInfo
												.getJsonString(jsonObject4,
														"buyerId"));
										purchaseList.setBuyer(JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject4,
																"buyer"),
														"displayName"));
										purchaseList
												.setPurchaseFormId(JsonGetInfo
														.getJsonString(
																jsonObject4,
																"id"));
										purchaseList.setCustomerId(JsonGetInfo
												.getJsonString(jsonObject4,
														"customerId"));
										purchaseList.setStatus(JsonGetInfo
												.getJsonString(jsonObject4,
														"status"));
										purchaseList.setSource(JsonGetInfo
												.getJsonString(jsonObject4,
														"source"));
										purchaseList.setStatusName(JsonGetInfo
												.getJsonString(jsonObject4,
														"statusName"));
										purchaseList.setQuoteCountJson(JsonGetInfo
												.getJsonInt(jsonObject4,
														"quoteCountJson"));
										purchaseList.setLastDays(JsonGetInfo
												.getJsonInt(jsonObject4,
														"lastDays"));
										JSONArray jsonArray = JsonGetInfo
												.getJsonArray(jsonObject4,
														"itemNameList");
										ArrayList<String> itemNameList = new ArrayList<String>();
										for (int i = 0; i < jsonArray.length(); i++) {
											itemNameList.add(jsonArray
													.getString(i));
										}
										purchaseList
												.setItemNameList(itemNameList);
										puchaseDatas.add(purchaseList);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}
									if (listAdapter == null) {
										listAdapter = new PurchaseListAdapter(
												PurchasePyMapActivity.this,
												puchaseDatas);
										listview.setAdapter(listAdapter);
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
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	@Override
	public void onLoadMore() {
		listview.setPullRefreshEnable(false);
		initData();
		onLoad();
	}

	private void onLoad() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listview.stopRefresh();
				listview.stopLoadMore();
				listview.setRefreshTime(new Date().toLocaleString());
				listview.setPullLoadEnable(true);
				listview.setPullRefreshEnable(true);
			}
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.button31:
			type = "quoting";
			onRefresh();
			init();
			showNotice(0);
			break;
		case R.id.button32:
			type = "unquote";
			onRefresh();
			init();
			showNotice(1);
			break;
		default:
			// Nothing to do
		}
	}

	private void showCitys() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_city, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		mViewProvince = (WheelView) dia_choose_share
				.findViewById(R.id.id_province);
		mViewCity = (WheelView) dia_choose_share.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) dia_choose_share
				.findViewById(R.id.id_district);
		mViewCity.setVisibility(View.GONE);
		mViewDistrict.setVisibility(View.GONE);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		setUpData();

		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(dia_choose_share, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cityName = mCurrentProviceName + "\u0020" + mCurrentCityName
						+ "\u0020" + mCurrentDistrictName + "\u0020";
				cityCode = GetCodeByName.initProvinceDatas(
						PurchasePyMapActivity.this, mCurrentProviceName,
						mCurrentCityName);
				onRefresh();
				if (!PurchasePyMapActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!PurchasePyMapActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!PurchasePyMapActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				PurchasePyMapActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private void showSelectedResult() {
		Toast.makeText(
				PurchasePyMapActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

	public class PurchaseListAdapter extends BaseAdapter {
		private static final String TAG = "PurchaseListAdapter";

		private ArrayList<PurchaseList> data = null;

		private Context context = null;
		private FinalBitmap fb;

		public PurchaseListAdapter(Context context, ArrayList<PurchaseList> data) {
			this.data = data;
			this.context = context;
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.no_image_show);
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
					R.layout.list_item_purchase_list, null);
			TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
			TextView tv_ac = (TextView) inflate.findViewById(R.id.tv_ac);
			TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
			TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
			TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
			TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
			TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
			TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
			TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
			TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
			TextView tv_10 = (TextView) inflate.findViewById(R.id.tv_10);
			TextView tv_11 = (TextView) inflate.findViewById(R.id.tv_11);
			TextView tv_caozuo01 = (TextView) inflate
					.findViewById(R.id.tv_caozuo01);
			TextView tv_caozuo02 = (TextView) inflate
					.findViewById(R.id.tv_caozuo02);
			TextView tv_caozuo03 = (TextView) inflate
					.findViewById(R.id.tv_caozuo03);
			FlowTagLayout mMobileFlowTagLayout = (FlowTagLayout) inflate
					.findViewById(R.id.mobile_flow_layout);
			// 移动研发标签
			TagAdapter<String> mMobileTagAdapter = new TagAdapter<String>(
					context);
			// mMobileFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
			mMobileFlowTagLayout.setAdapter(mMobileTagAdapter);
			// mMobileFlowTagLayout.setOnTagSelectListener(new
			// OnTagSelectListener() {
			// @Override
			// public void onItemSelect(FlowTagLayout parent, List<Integer>
			// selectedList) {
			// if (selectedList != null && selectedList.size() > 0) {
			// StringBuilder sb = new StringBuilder();
			//
			// for (int i : selectedList) {
			// sb.append(parent.getAdapter().getItem(i));
			// sb.append(":");
			// }
			// ToastUtil.showShortToast(PurchasePyMapActivity.this, "移动研发:" +
			// sb.toString());
			// }else{
			// ToastUtil.showShortToast(PurchasePyMapActivity.this, "没有选择标签");
			// }
			// }
			// });
			// mMobileFlowTagLayout.setOnTagClickListener(new
			// OnTagClickListener() {
			//
			// @Override
			// public void onItemClick(FlowTagLayout parent, View view, int
			// position) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			mMobileTagAdapter.onlyAddAll(data.get(position).getItemNameList());
			tv_01.setText(data.get(position).getNum());
			tv_03.setText(data.get(position).getCityName());
			tv_04.setText("采购商家：" + data.get(position).getBuyer());
			if (data.get(position).isNeedInvoice()) {
				tv_08.setText("发票要求：需要");
			} else {
				tv_08.setText("发票要求：不需要");
			}
			if (data.get(position).getQuoteCountJson() > 0) {
				StringFormatUtil fillColor = new StringFormatUtil(context, "已有"
						+ data.get(position).getQuoteCountJson() + "条报价", data
						.get(position).getQuoteCountJson() + "", R.color.red)
						.fillColor();
				tv_10.setText(fillColor.getResult());
			} else {
				tv_10.setText("暂无报价");
			}

			tv_caozuo01.setText("截止时间：" + data.get(position).getReceiptDate());
			inflate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,
							StorePurchaseListActivity.class);
					intent.putExtra("purchaseFormId", data.get(position)
							.getPurchaseFormId());
					intent.putExtra("title", data.get(position).getNum());
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_right);
				}
			});

			return inflate;
		}

		public void notify(ArrayList<PurchaseList> data) {
			this.data = data;
			notifyDataSetChanged();
		}

	}

	public class MapSearchAdapter extends BaseAdapter implements SectionIndexer {
		private static final String TAG = "MapSearchAdapter";

		private ArrayList<Subscribe> data = null;

		private Context context = null;
		private FinalBitmap fb;

		public MapSearchAdapter(Context context, ArrayList<Subscribe> data) {
			this.data = data;
			this.context = context;
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.no_image_show);
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
					R.layout.list_item, null);
			RelativeLayout xiao_rl_popo_list_item = (RelativeLayout) inflate
					.findViewById(R.id.rl_popo_list_item);
			TextView xiao_quyu_tv_item = (TextView) inflate
					.findViewById(R.id.tv_item);
			TextView tvLetter = (TextView) inflate.findViewById(R.id.catalog);
			xiao_quyu_tv_item.setText("[" + data.get(position).getParentName()
					+ "]" + data.get(position).getName());
			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);

			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				tvLetter.setVisibility(View.VISIBLE);
				tvLetter.setText(data.get(position).getSortLetters());
				tvLetter.setBackgroundColor(getResources().getColor(
						R.color.gray_line));
			} else {
				tvLetter.setVisibility(View.GONE);
			}

			inflate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PurchasePyMapActivity.this,
							StorePurchaseListActivity.class);
					intent.putExtra("secondSeedlingTypeId", data.get(position)
							.getId());
					intent.putExtra("title", "["
							+ data.get(position).getParentName() + "]"
							+ data.get(position).getName());
					startActivity(intent);
					// subscribeSave(data.get(position).getId(), position);
				}

			});

			return inflate;
		}

		public void notify(ArrayList<Subscribe> data) {
			this.data = data;
			notifyDataSetChanged();
		}

		@Override
		public Object[] getSections() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getPositionForSection(int sectionIndex) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = data.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == sectionIndex) {
					return i;
				}
			}

			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			return data.get(position).getSortLetters().charAt(0);
		}

	}

}
