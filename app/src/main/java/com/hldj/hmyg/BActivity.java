package com.hldj.hmyg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.kaede.tagview.OnTagDeleteListener;
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
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparator;
import com.example.sortlistview.SideBar;
import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.hldj.hmyg.BActivity.MultipleClickProcess.DaquyuAdapter;
import com.hldj.hmyg.BActivity.MultipleClickProcess.XiaoquyuAdapter;
import com.hldj.hmyg.adapter.ProductGridAdapter;
import com.hldj.hmyg.adapter.ProductListAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.DaQuYu;
import com.hldj.hmyg.bean.XiaoQuYu;
import com.hldj.hmyg.buyer.PurchaseSearchListActivity;
import com.hldj.hmyg.shopsort.SortList;
import com.huewu.pla.lib.me.maxwin.view.PLAXListView;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;
import com.yangfuhai.asimplecachedemo.lib.ACache;

@SuppressLint("NewApi")
public class BActivity extends BaseSecondActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener,
		OnWheelChangedListener {
	final String hintText = "<img src=\"" + R.drawable.seller_search
			+ "\" /> 搜索商店名,商品名";
	String view_type = "list";
	private XListView xListView;
	private PLAXListView glistView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	private ArrayList<String> data_ids = new ArrayList<String>();
	boolean getdata; // 避免刷新多出数据
	private ImageView iv_view_type;
	private ImageView iv_seller_arrow2;
	private ImageView iv_seller_arrow3;
	private ProductListAdapter listAdapter;
	private ProductGridAdapter gridAdapter;
	private DaquyuAdapter daquyuAdapter;
	private XiaoquyuAdapter xiaoquyuAdapter;
	private String searchKey = "";
	private String minPrice = "";
	private String maxPrice = "";
	private String minDiameter = "";
	private String maxDiameter = "";
	private String minDbh = "";
	private String maxDbh = "";
	private String minHeight = "";
	private String maxHeight = "";
	private String minCrown = "";
	private String maxCrown = "";
	private String minOffbarHeight = "";
	private String maxOffbarHeight = "";
	private String minLength = "";
	private String maxLength = "";
	private String firstSeedlingTypeId = "";
	private String firstSeedlingTypeName = "";
	private String supportTradeType = "";
	private String supportTradeTypeName = "";
	private String secondSeedlingTypeId = "";
	private String secondSeedlingTypeName = "";
	private String plantTypes = "";
	private String searchSpec = "";
	private String specMinValue = "";
	private String specMaxValue = "";
	private ArrayList<String> planttype_has_ids = new ArrayList<String>();

	private String orderBy = "";
	private String priceSort = "";
	private String publishDateSort = "";
	private String cityCode = "";
	private String cityName = "全国";
	private int pageSize = 20;
	private int pageIndex = 0;
	private int RefreshpageIndex = 0;
	private ArrayList<DaQuYu> daQuYus = new ArrayList<DaQuYu>();
	private List<XiaoQuYu> xiaoQuYus = new ArrayList<XiaoQuYu>();
	private PopupWindow popupWindow;
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
	private PinyinComparator pinyinComparator;
	private DisplayMetrics dm;
	private ArrayList<String> lanmus = new ArrayList<String>();
	private ArrayList<String> lanmu_ids = new ArrayList<String>();
	private BaseAnimatorSet mBasIn;
	private BaseAnimatorSet mBasOut;
	private Editor e;
	private boolean scrollFlag = false;// 标记是否滑动
	private int lastVisibleItemPosition = 0;// 标记上次滑动位置
	private ACache mCache;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	public void setBasIn(BaseAnimatorSet bas_in) {
		this.mBasIn = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.mBasOut = bas_out;
	}

	private ArrayList<String> planttype_names = new ArrayList<String>();
	private ArrayList<String> planttype_ids = new ArrayList<String>();
	private KProgressHUD hud;
	private ArrayList<SortList> sortLists = new ArrayList<SortList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b);
		hud = KProgressHUD.create(BActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("数据加载中...").setMaxProgress(100).setCancellable(true)
				.setDimAmount(0f);
		e = MyApplication.Userinfo.edit();
		mCache = ACache.get(this);
		mBasIn = new BounceTopEnter();
		mBasOut = new SlideBottomExit();
		setOverflowShowingAlways();
		dm = getResources().getDisplayMetrics();
		lanmus.add("全部");
		lanmus.add("放心购");
		lanmus.add("帮我购");
		lanmus.add("担保购");
		lanmus.add("委托购");
		lanmu_ids.add("");
		lanmu_ids.add("fangxin");
		lanmu_ids.add("bangwo");
		lanmu_ids.add("danbao");
		lanmu_ids.add("weituo");

		initSortList();

		tagView = (TagView) this.findViewById(R.id.tagview);
		tagView.setOnTagDeleteListener(new OnTagDeleteListener() {

			@Override
			public void onTagDeleted(int position, me.kaede.tagview.Tag tag) {
				// TODO Auto-generated method stub
				if (tag.id == 1) {
					searchKey = "";
					onRefresh();
				} else if (tag.id == 2) {
					firstSeedlingTypeId = "";
					firstSeedlingTypeName = "";
					onRefresh();
				} else if (tag.id == 3) {
					secondSeedlingTypeId = "";
					secondSeedlingTypeName = "";
					onRefresh();
				}

			}
		});

		if (getIntent().getStringExtra("firstSeedlingTypeId") != null
				&& getIntent().getStringExtra("firstSeedlingTypeName") != null) {
			firstSeedlingTypeId = getIntent().getStringExtra(
					"firstSeedlingTypeId");
			firstSeedlingTypeName = getIntent().getStringExtra(
					"firstSeedlingTypeName");
			tagView.removeAllTags();
			if (!"".equals(firstSeedlingTypeId)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
						firstSeedlingTypeName);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 2; // 1 搜索 2分类
				tagView.addTag(tag);
			}
		}
		if (getIntent().getStringExtra("searchKey") != null) {
			searchKey = getIntent().getStringExtra("searchKey");
			tagView.removeAllTags();
			if (!"".equals(searchKey)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(searchKey);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 1; // 1 搜索 2分类
				tagView.addTag(tag);
			}
		}
		if (getIntent().getStringExtra("from") != null) {
			// if (Build.VERSION.SDK_INT >= 23) {
			// setSwipeBackEnable(false);
			// }
		} else {
			// setSwipeBackEnable(false);
		}
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		pager = (ViewPager) findViewById(R.id.pager);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		ll_all = (LinearLayout) findViewById(R.id.ll_all);
		ll_choice = (LinearLayout) findViewById(R.id.ll_choice);
		relativeLayout1 = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		tv_xiaoxitishi = (TextView) findViewById(R.id.tv_xiaoxitishi);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		pager.setOffscreenPageLimit(lanmus.size());
		tabs.setViewPager(pager);
		setTabsValue();

		if (getIntent().getStringExtra("supportTradeType") != null
				&& getIntent().getStringExtra("supportTradeTypeName") != null) {
			supportTradeType = getIntent().getStringExtra("supportTradeType");
			supportTradeTypeName = getIntent().getStringExtra(
					"supportTradeTypeName");
			if ("".equals(supportTradeType)) {
				pager.setCurrentItem(0);
			} else if ("fangxin".equals(supportTradeType)) {
				pager.setCurrentItem(1);
			} else if ("bangwo".equals(supportTradeType)) {
				pager.setCurrentItem(2);
			} else if ("danbao".equals(supportTradeType)) {
				pager.setCurrentItem(3);
			} else if ("weituo".equals(supportTradeType)) {
				pager.setCurrentItem(4);
			}
		}
		rl_choose_type = (RelativeLayout) findViewById(R.id.rl_choose_type);
		relativeLayout2 = (RelativeLayout) findViewById(R.id.RelativeLayout2);
		RelativeLayout rl_choose_price = (RelativeLayout) findViewById(R.id.rl_choose_price);
		RelativeLayout rl_choose_time = (RelativeLayout) findViewById(R.id.rl_choose_time);
		RelativeLayout rl_choose_screen = (RelativeLayout) findViewById(R.id.rl_choose_screen);
		TextView tv_search = (TextView) findViewById(R.id.tv_search);
		iv_view_type = (ImageView) findViewById(R.id.iv_view_type);
		iv_seller_arrow2 = (ImageView) findViewById(R.id.iv_seller_arrow2);
		iv_seller_arrow3 = (ImageView) findViewById(R.id.iv_seller_arrow3);
		xListView = (XListView) findViewById(R.id.xlistView);
		view = (TagCloudLinkView) findViewById(R.id.tag_cloud);
		// view.add(new Tag(1,"TAG TEXT 1  ×"));
		view.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
			@Override
			public void onTagDeleted(Tag tag, int i) {
				// write something
			}
		});
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		glistView = (PLAXListView) findViewById(R.id.glistView);
		glistView.setPullLoadEnable(true);
		glistView.setPullRefreshEnable(true);
		glistView.setXListViewListener(this);

		initDataGetFirstType();
		initData();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		iv_view_type.setOnClickListener(multipleClickProcess);
		rl_choose_type.setOnClickListener(multipleClickProcess);
		rl_choose_price.setOnClickListener(multipleClickProcess);
		rl_choose_time.setOnClickListener(multipleClickProcess);
		rl_choose_screen.setOnClickListener(multipleClickProcess);
		relativeLayout2.setOnClickListener(multipleClickProcess);
		iv_close.setOnClickListener(multipleClickProcess);

	}

	private void initSortList() {
		// TODO Auto-generated method stub
		SortList sortList1 = new SortList("default_asc", "综合排序");
		SortList sortList2 = new SortList("publishDate_desc", "最新发布");
		SortList sortList3 = new SortList("distance_asc", "最近距离");
		SortList sortList4 = new SortList("price_asc", "价格从低到高");
		SortList sortList5 = new SortList("price_desc", "价格从高到底");
		sortLists.add(sortList1);
		sortLists.add(sortList2);
		sortLists.add(sortList3);
		sortLists.add(sortList4);
		sortLists.add(sortList5);
	}

	private void ChoiceSortList() {
		// TODO Auto-generated method stub
		View popo_shop_type_list = getLayoutInflater().inflate(
				R.layout.popo_shop_type_list, null);
		ListView listview = (ListView) popo_shop_type_list
				.findViewById(R.id.listview);
		if (sortListAdapter != null) {
			listview.setAdapter(sortListAdapter);
		} else {
			sortListAdapter = new SortListAdapter();
			listview.setAdapter(sortListAdapter);
		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sortListAdapter.setSeclection(position);
				sortListAdapter.notifyDataSetChanged();
				orderBy = sortLists.get(position).getID();
				if (popupWindow2 != null) {
					popupWindow2.dismiss();
				}
				onRefresh();
			}

		});

		popupWindow2 = new PopupWindow(popo_shop_type_list,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// popupWindow = new PopupWindow(view, getResources()
		// .getDimensionPixelSize(R.dimen.popmenu_width),
		// LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow2.setBackgroundDrawable(new BitmapDrawable());

		popupWindow2.showAsDropDown(ll_choice, 1,
		// 保证尺寸是根据屏幕像素密度来的
				getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));

		// 使其聚集
		popupWindow2.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow2.setOutsideTouchable(true);
		// 刷新状态
		popupWindow2.update();
	}

	class SortListAdapter extends BaseAdapter {

		private int clickTemp = 0;

		// 标识选择的Item
		public void setSeclection(int position) {
			clickTemp = position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sortLists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View sort_list_item = getLayoutInflater().inflate(
					R.layout.list_item_sort, null);
			RelativeLayout rl_sort_list_item = (RelativeLayout) sort_list_item
					.findViewById(R.id.rl_popo_list_item);
			TextView area_tv_item = (TextView) sort_list_item
					.findViewById(R.id.tv_item);
			ImageView is_check = (ImageView) sort_list_item
					.findViewById(R.id.is_check);
			area_tv_item.setText(sortLists.get(position).getName());
			if (clickTemp == position) {
				rl_sort_list_item.setBackgroundColor(Color.argb(155, 192, 192,
						192)); // #COCOCO
				is_check.setVisibility(View.VISIBLE);
			} else {
				rl_sort_list_item.setBackgroundColor(Color.argb(155, 255, 255,
						255)); // #FFFFFF
				is_check.setVisibility(View.INVISIBLE);
			}
			return sort_list_item;
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
				// TODO Auto-generated method stub
				if ("".equals(lanmu_ids.get(arg0))) {
					supportTradeType = lanmu_ids.get(arg0);
					if (listAdapter != null && gridAdapter != null) {
						minPrice = "";
						maxPrice = "";
						minDiameter = "";
						maxDiameter = "";
						minDbh = "";
						maxDbh = "";
						minHeight = "";
						maxHeight = "";
						minCrown = "";
						maxCrown = "";
						minOffbarHeight = "";
						maxOffbarHeight = "";
						minLength = "";
						maxLength = "";
						firstSeedlingTypeId = "";
						firstSeedlingTypeName = "";
						supportTradeType = "";
						supportTradeTypeName = "";
						secondSeedlingTypeId = "";
						secondSeedlingTypeName = "";
						plantTypes = "";
						orderBy = "";
						priceSort = "";
						publishDateSort = "";
						cityCode = "";
						cityName = "全国";
						onRefresh();
					}
					showNotice(lanmu_ids.get(arg0));
				} else if ("fangxin".equals(lanmu_ids.get(arg0))) {
					supportTradeType = lanmu_ids.get(arg0);
					if (listAdapter != null && gridAdapter != null) {
						onRefresh();
					}
					showNotice(lanmu_ids.get(arg0));
				} else if ("bangwo".equals(lanmu_ids.get(arg0))) {
					supportTradeType = lanmu_ids.get(arg0);
					if (listAdapter != null && gridAdapter != null) {
						onRefresh();
					}
					showNotice(lanmu_ids.get(arg0));
				} else if ("danbao".equals(lanmu_ids.get(arg0))) {
					supportTradeType = lanmu_ids.get(arg0);
					if (listAdapter != null && gridAdapter != null) {
						onRefresh();
					}
					showNotice(lanmu_ids.get(arg0));
				} else if ("weituo".equals(lanmu_ids.get(arg0))) {
					// supportTradeType = lanmu_ids.get(arg0);
					// 弹出
					// showNotice(lanmu_ids.get(arg0));

					if ("".equals(supportTradeType)) {
						pager.setCurrentItem(0);
						tabs.notifyDataSetChanged();
					} else if ("fangxin".equals(supportTradeType)) {
						pager.setCurrentItem(1);
						tabs.notifyDataSetChanged();
					} else if ("bangwo".equals(supportTradeType)) {
						pager.setCurrentItem(2);
						tabs.notifyDataSetChanged();
					} else if ("danbao".equals(supportTradeType)) {
						pager.setCurrentItem(3);
						tabs.notifyDataSetChanged();
					}

					Intent toWeituoActivity = new Intent(BActivity.this,
							WeituoActivity.class);
					toWeituoActivity.putExtra("title", "委托购");
					toWeituoActivity.putExtra("url", Data.weituogou);
					startActivity(toWeituoActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				}
			}

			private void showNotice(String id) {
				// TODO Auto-generated method stub
				if ("".equals(id)) {
					ll_01.setVisibility(View.GONE);
				} else if ("fangxin".equals(id)) {
					if (MyApplication.Userinfo.getBoolean("NeedShowfangxin",
							true)) {

						tv_xiaoxitishi
								.setText("我们承诺：放心购所有资源，若有同批更低价格，一经确认则再优惠2%。");
//						ll_01.setVisibility(View.VISIBLE);
					} else {
						ll_01.setVisibility(View.GONE);
					}
				} else if ("bangwo".equals(id)) {
					if (MyApplication.Userinfo.getBoolean("NeedShowbangwo",
							true)) {
						tv_xiaoxitishi.setText("帮我购：买家可委托花木易购帮其验苗或者发货。");
//						ll_01.setVisibility(View.VISIBLE);
					} else {
						ll_01.setVisibility(View.GONE);
					}
				} else if ("danbao".equals(id)) {
					if (MyApplication.Userinfo.getBoolean("NeedShowdanbao",
							true)) {
						tv_xiaoxitishi
								.setText("担保购：买家可将资金托管在花木易购，确认收货后再将款项支付给苗圃。");
//						ll_01.setVisibility(View.VISIBLE);
					} else {
						ll_01.setVisibility(View.GONE);
					}
				} else if ("weituo".equals(id)) {
					ll_01.setVisibility(View.GONE);
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

	private void initDataGetFirstType() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "seedlingType/getFirstType",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						mCache.remove("getFirstType");
						mCache.put("getFirstType", t.toString());
						if (daQuYus.size() > 0) {
							daQuYus.clear();
							if (daquyuAdapter != null) {
								daquyuAdapter.notifyDataSetChanged();
							}
						}

						LoadCache(t.toString());
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						if (mCache.getAsString("getFirstType") != null
								&& !"".equals(mCache
										.getAsString("getFirstType"))) {
							if (daQuYus.size() > 0) {
								daQuYus.clear();
								if (daquyuAdapter != null) {
									daquyuAdapter.notifyDataSetChanged();
								}
							}
							LoadCache(mCache.getAsString("getFirstType"));
						}
						Toast.makeText(BActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

					private void LoadCache(String t) {
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
								daQuYus.add(new DaQuYu("", "不限"));
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
											.getJsonString(jsonObject2, "id"));
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
									DaQuYu daQuYu = new DaQuYu(JsonGetInfo
											.getJsonString(jsonObject2, "id"),
											JsonGetInfo.getJsonString(
													jsonObject2, "name"));
									daQuYus.add(daQuYu);
								}

							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		final AjaxParams params = new AjaxParams();
		params.put("searchSpec", searchSpec);
		params.put("specMinValue", specMinValue);
		params.put("specMaxValue", specMaxValue);
		params.put("searchKey", searchKey);
		params.put("minPrice", minPrice);
		params.put("maxPrice", maxPrice);
		params.put("minDiameter", minDiameter);
		params.put("maxDiameter", maxDiameter);
		params.put("minDbh", minDbh);
		params.put("maxDbh", maxDbh);
		params.put("minHeight", minHeight);
		params.put("maxHeight", maxHeight);
		params.put("minCrown", minCrown);
		params.put("maxCrown", maxCrown);
		params.put("minOffbarHeight", minOffbarHeight);
		params.put("maxOffbarHeight", maxOffbarHeight);
		params.put("minLength", minLength);
		params.put("maxLength", maxLength);
		params.put("TradeType", supportTradeType);
		params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("secondSeedlingTypeId", secondSeedlingTypeId);
		params.put("cityCode", cityCode);

		plantTypes = "";
		for (int i = 0; i < planttype_has_ids.size(); i++) {
			plantTypes = plantTypes + planttype_has_ids.get(i) + ",";
		}
		if (plantTypes.length() > 0) {
			params.put("plantTypes",
					plantTypes.substring(0, plantTypes.length() - 1));
		} else {
			params.put("plantTypes", plantTypes);
		}

		// if ("".equals(priceSort) && !"".equals(publishDateSort)) {
		// orderBy = publishDateSort;
		// } else if (!"".equals(priceSort) && "".equals(publishDateSort)) {
		// orderBy = priceSort;
		// } else if ("".equals(priceSort) && "".equals(publishDateSort)) {
		// orderBy = "";
		// } else {
		// orderBy = priceSort + "," + publishDateSort;
		// }

		if (orderBy.endsWith(",")) {
			orderBy = orderBy.substring(0, orderBy.length() - 1);
		}
		params.put("orderBy", orderBy);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");

		params.put("latitude", MyApplication.Userinfo.getString("latitude", ""));
		params.put("longitude",
				MyApplication.Userinfo.getString("longitude", ""));
		finalHttp.post(GetServerUrl.getUrl() + "seedling/list", params,
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
						AcheData(t.toString());
						super.onSuccess(t);

					}

					private void AcheData(String t) {
						// TODO Auto-generated method stub
						Log.e("orderBy", orderBy);
						if (hud != null) {
							hud.dismiss();
						}
						try {
							JSONObject jsonObject = new JSONObject(t);
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
								data_ids.clear();
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("show_type", "seedling_list");
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										if (pageIndex == 0) {
											data_ids.add(JsonGetInfo
													.getJsonString(jsonObject3,
															"id"));
										}
										hMap.put("specText", JsonGetInfo
												.getJsonString(jsonObject3,
														"specText"));
										hMap.put("name", JsonGetInfo
												.getJsonString(jsonObject3,
														"standardName"));
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
										hMap.put(
												"paramsList",
												JsonGetInfo.getJsonArray(
														jsonObject3,
														"paramsList")
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
										hMap.put("price", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject3,
														"count"));
										hMap.put("unitTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitTypeName"));
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
										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("fullName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"fullName"));

										hMap.put("ciCity_name", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"name"));
										hMap.put("realName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"realName"));
										hMap.put("companyName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"companyName"));
										hMap.put("publicName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"publicName"));
										hMap.put("status", JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.put("statusName", JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
										if (gridAdapter != null) {
											gridAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new ProductListAdapter(
												BActivity.this, datas);
										xListView.setAdapter(listAdapter);
									}
									if (gridAdapter == null) {
										gridAdapter = new ProductGridAdapter(
												BActivity.this, datas);
										glistView.setAdapter(gridAdapter);
									}
									pageIndex++;
									RefreshpageIndex++;
								}

							} else {

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						// if (mCache.getAsString("seedling/list"
						// + params.toString()) != null
						// && !"".equals(mCache
						// .getAsString("seedling/list"
						// + params.toString()))) {
						// AcheData(mCache.getAsString("seedling/list"
						// + params.toString()));
						// }
						if (hud != null) {
							hud.dismiss();
						}
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (hud != null) {
			hud.dismiss();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (hud != null) {
			hud.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (hud != null) {
			hud.dismiss();
		}
		super.onBackPressed();
	}

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;
		private SideBar sideBar;
		private ListView listview_xiaoquyu;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				switch (view.getId()) {
				case R.id.iv_view_type:
					if ("grid".equals(view_type)) {
						// gridview
						view_type = "list";
						iv_view_type
								.setImageResource(R.drawable.icon_list_view);
						glistView.setVisibility(View.GONE);
						xListView.setVisibility(View.VISIBLE);
					} else if ("list".equals(view_type)) {
						// listview
						view_type = "grid";
						iv_view_type
								.setImageResource(R.drawable.icon_grid_view);
						xListView.setVisibility(View.GONE);
						glistView.setVisibility(View.VISIBLE);
					}

					break;
				case R.id.rl_choose_type:
					showCitys();
					// if (popupWindow == null) {
					// ChoiceArea();
					// } else {
					// if (!popupWindow.isShowing()) {
					// ChoiceArea();
					// }
					// }
					break;
				case R.id.rl_choose_price:
					if (!hud.isShowing() && hud != null) {
						hud.show();
					}
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

					if (sortLists.size() > 0) {
						ChoiceSortList();
					}
					// if (!hud.isShowing() && hud != null) {
					// hud.show();
					// }
					// if ("".equals(publishDateSort)) {
					// publishDateSort = "publishDate_asc";
					// iv_seller_arrow3
					// .setImageResource(R.drawable.icon_seller_arrow2);
					// } else if ("publishDate_asc".equals(publishDateSort)) {
					// publishDateSort = "publishDate_desc";
					// iv_seller_arrow3
					// .setImageResource(R.drawable.icon_seller_arrow3);
					// } else if ("publishDate_desc".equals(publishDateSort)) {
					// publishDateSort = "";
					// iv_seller_arrow3
					// .setImageResource(R.drawable.icon_seller_arrow1);
					// }
					// onRefresh();
					break;
				case R.id.rl_choose_screen:
					Intent toSellectActivity = new Intent(BActivity.this,
							SellectActivity2.class);
					toSellectActivity.putExtra("from", "BActivity");
					toSellectActivity.putExtra("minPrice", minPrice);
					toSellectActivity.putExtra("maxPrice", maxPrice);
					toSellectActivity.putExtra("minDiameter", minDiameter);
					toSellectActivity.putExtra("maxDiameter", maxDiameter);
					toSellectActivity.putExtra("minDbh", minDbh);
					toSellectActivity.putExtra("maxDbh", maxDbh);
					toSellectActivity.putExtra("minHeight", minHeight);
					toSellectActivity.putExtra("maxHeight", maxHeight);
					toSellectActivity.putExtra("minLength", minLength);
					toSellectActivity.putExtra("maxLength", maxLength);
					toSellectActivity.putExtra("minCrown", minCrown);
					toSellectActivity.putExtra("maxCrown", maxCrown);
					toSellectActivity.putExtra("cityCode", cityCode);
					toSellectActivity.putExtra("cityName", cityName);
					toSellectActivity.putExtra("minOffbarHeight",
							minOffbarHeight);
					toSellectActivity.putExtra("maxOffbarHeight",
							maxOffbarHeight);
					toSellectActivity.putExtra("plantTypes", plantTypes);
					toSellectActivity.putStringArrayListExtra(
							"planttype_has_ids", planttype_has_ids);
					toSellectActivity.putExtra("searchSpec", searchSpec);
					toSellectActivity.putExtra("specMinValue", specMinValue);
					toSellectActivity.putExtra("specMaxValue", specMaxValue);
					toSellectActivity.putExtra("searchKey", searchKey);
					startActivityForResult(toSellectActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;

				case R.id.RelativeLayout2:
					Intent intent = new Intent(BActivity.this,
							PurchaseSearchListActivity.class);
					intent.putExtra("from", "BActivity");
					startActivityForResult(intent, 1);
					break;
				case R.id.iv_close:
					// TODO Auto-generated method stub
					if ("".equals(supportTradeType)) {
					} else if ("fangxin".equals(supportTradeType)) {
						DialogNoti("我们承诺：放心购所有资源，若有同批更低价格，一经确认则再优惠2%。");
					} else if ("bangwo".equals(supportTradeType)) {
						DialogNoti("帮我购：买家可委托花木易购帮其验苗或者发货。");
					} else if ("danbao".equals(supportTradeType)) {
						DialogNoti("担保购：买家可将资金托管在花木易购，确认收货后再将款项支付给苗圃。");
					} else if ("weituo".equals(supportTradeType)) {
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

		private void DialogNoti(String string) {
			// TODO Auto-generated method stub

			final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
					BActivity.this);
			dialog.title("温馨提示").content(string)
			//
					.btnText("不再提示", "取消")//
					.showAnim(mBasIn)//
					.dismissAnim(mBasOut)//
					.show();

			dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
						@Override
						public void onBtnClick() {
							if ("fangxin".equals(supportTradeType)) {
								e.putBoolean("NeedShowfangxin", false);
							} else if ("bangwo".equals(supportTradeType)) {
								e.putBoolean("NeedShowbangwo", false);
							} else if ("danbao".equals(supportTradeType)) {
								e.putBoolean("NeedShowdanbao", false);
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

		private void ChoiceArea() {
			// TODO Auto-generated method stub
			View quyu = getLayoutInflater().inflate(R.layout.popo_quyu, null);
			ListView listview_daquyu = (ListView) quyu
					.findViewById(R.id.listview_daquyu);
			listview_xiaoquyu = (ListView) quyu
					.findViewById(R.id.listview_xiaoquyu);
			sideBar = (SideBar) quyu.findViewById(R.id.sidrbar);
			TextView dialog = (TextView) quyu.findViewById(R.id.dialog);
			sideBar.setTextView(dialog);
			// 设置右侧触摸监听
			sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

				@Override
				public void onTouchingLetterChanged(String s) {
					// 该字母首次出现的位置
					int position = xiaoquyuAdapter.getPositionForSection(s
							.charAt(0));
					if (position != -1) {
						listview_xiaoquyu.setSelection(position);
					}

				}
			});
			if (daquyuAdapter != null) {
				listview_daquyu.setAdapter(daquyuAdapter);
			} else {
				daquyuAdapter = new DaquyuAdapter();
				listview_daquyu.setAdapter(daquyuAdapter);
			}
			if (xiaoquyuAdapter != null) {
				listview_xiaoquyu.setAdapter(xiaoquyuAdapter);
			} else {
				xiaoquyuAdapter = new XiaoquyuAdapter(BActivity.this, xiaoQuYus);
				listview_xiaoquyu.setAdapter(xiaoquyuAdapter);
			}

			listview_daquyu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if ("不限".equals(daQuYus.get(position).getName())) {
						// TODO Auto-generated method stub
						firstSeedlingTypeId = "";
						secondSeedlingTypeId = "";
						firstSeedlingTypeName = "";
						secondSeedlingTypeName = "";
						daquyuAdapter.setSeclection(position);
						xiaoquyuAdapter.notifyDataSetChanged();
						if (popupWindow != null) {
							popupWindow.dismiss();
						}
						tagView.removeAllTags();
						onRefresh();

					} else {
						firstSeedlingTypeId = daQuYus.get(position).getId();
						firstSeedlingTypeName = daQuYus.get(position).getName();
						secondSeedlingTypeId = "";
						secondSeedlingTypeName = "";
						daquyuAdapter.setSeclection(position);
						daquyuAdapter.notifyDataSetChanged();

						tagView.removeAllTags();
						me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
								firstSeedlingTypeName);
						tag.layoutColor = getResources().getColor(
								R.color.main_color);
						tag.isDeletable = true;
						tag.id = 2; // 1 搜索 2分类
						tagView.addTag(tag);

						onRefresh();

						FinalHttp finalHttp = new FinalHttp();
						GetServerUrl.addHeaders(finalHttp, false);
						AjaxParams params = new AjaxParams();
						params.put("firstTypeId", firstSeedlingTypeId);
						finalHttp.post(GetServerUrl.getUrl()
								+ "seedlingType/getSecondTypeMap", params,
								new AjaxCallBack<Object>() {

									@Override
									public void onSuccess(Object t) {
										// TODO Auto-generated method stub
										mCache.remove("getSecondTypeMap"
												+ firstSeedlingTypeId);
										mCache.put("getSecondTypeMap"
												+ firstSeedlingTypeId,
												t.toString());
										if (xiaoQuYus.size() > 0) {
											xiaoQuYus.clear();
											if (xiaoquyuAdapter != null) {
												xiaoquyuAdapter
														.notifyDataSetChanged();
											}
										}
										LoadCache(t.toString());
										super.onSuccess(t);
									}

									@Override
									public void onFailure(Throwable t,
											int errorNo, String strMsg) {
										if (mCache
												.getAsString("getSecondTypeMap"
														+ firstSeedlingTypeId) != null
												&& !"".equals(mCache
														.getAsString("getSecondTypeMap"
																+ firstSeedlingTypeId))) {
											if (xiaoQuYus.size() > 0) {
												xiaoQuYus.clear();
												if (xiaoquyuAdapter != null) {
													xiaoquyuAdapter
															.notifyDataSetChanged();
												}
											}
											LoadCache(mCache
													.getAsString("getSecondTypeMap"
															+ firstSeedlingTypeId));
										}
										Toast.makeText(BActivity.this,
												R.string.error_net,
												Toast.LENGTH_SHORT).show();
										super.onFailure(t, errorNo, strMsg);
									}

									private void LoadCache(String t) {
										// TODO Auto-generated method stub
										try {
											JSONObject jsonObject = new JSONObject(
													t.toString());
											JSONObject jsonObject2 = JsonGetInfo
													.getJSONObject(JsonGetInfo
															.getJSONObject(
																	jsonObject,
																	"data"),
															"childData");
											for (int i = 0; i < keySort.length; i++) {
												JSONArray jsonArray = JsonGetInfo
														.getJsonArray(
																jsonObject2,
																keySort[i]);

												if (jsonArray.length() > 0) {

													for (int j = 0; j < jsonArray
															.length(); j++) {
														String id = jsonArray
																.getJSONObject(
																		j)
																.getString("id");
														String name = jsonArray
																.getJSONObject(
																		j)
																.getString(
																		"name");

														// String pinyin =
														// characterParser.getSelling(date[i]);
														// String sortString =
														// pinyin.substring(0,
														// 1).toUpperCase();
														//
														// if
														// (sortString.matches("[A-Z]"))
														// {
														// sortModel.setSortLetters(sortString.toUpperCase());
														// } else {
														// sortModel.setSortLetters("#");
														// }

														XiaoQuYu xiaoQuYu = new XiaoQuYu(
																id,
																name,
																keySort[i]
																		.toUpperCase());
														xiaoQuYus.add(xiaoQuYu);
														xiaoquyuAdapter
																.notifyDataSetChanged();

													}
													// 根据a-z进行排序源数据
													Collections.sort(xiaoQuYus,
															pinyinComparator);
												}

											}
											xiaoQuYus.add(0, new XiaoQuYu("",
													"不限", "#"));
											xiaoquyuAdapter
													.notifyDataSetChanged();
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});
					}

				}

			});
			listview_xiaoquyu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					secondSeedlingTypeName = xiaoQuYus.get(position).getName();
					secondSeedlingTypeId = xiaoQuYus.get(position).getId();

					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
							secondSeedlingTypeName);
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 3; // 1 搜索 2分类
					tagView.addTag(tag);

					xiaoquyuAdapter.notifyDataSetChanged();
					if (popupWindow != null) {
						popupWindow.dismiss();
					}
					onRefresh();

				}

			});

			popupWindow = new PopupWindow(quyu, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			// popupWindow = new PopupWindow(view, getResources()
			// .getDimensionPixelSize(R.dimen.popmenu_width),
			// LayoutParams.WRAP_CONTENT);
			// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
			popupWindow.setBackgroundDrawable(new BitmapDrawable());

			popupWindow.showAsDropDown(rl_choose_type, 1,
			// 保证尺寸是根据屏幕像素密度来的
					getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));

			// 使其聚集
			popupWindow.setFocusable(true);
			// 设置允许在外点击消失
			popupWindow.setOutsideTouchable(true);
			// 刷新状态
			popupWindow.update();
		}

		class DaquyuAdapter extends BaseAdapter {

			private int clickTemp = 0;

			// 标识选择的Item
			public void setSeclection(int position) {
				clickTemp = position;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return daQuYus.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View daquyu_list_item = getLayoutInflater().inflate(
						R.layout.list_item, null);
				RelativeLayout da_rl_popo_list_item = (RelativeLayout) daquyu_list_item
						.findViewById(R.id.rl_popo_list_item);
				TextView daquyu_tv_item = (TextView) daquyu_list_item
						.findViewById(R.id.tv_item);
				TextView tvLetter = (TextView) daquyu_list_item
						.findViewById(R.id.catalog);
				tvLetter.setVisibility(View.GONE);
				daquyu_tv_item.setText(daQuYus.get(position).getName());
				if (clickTemp == position) {
					da_rl_popo_list_item.setBackgroundColor(Color.argb(155,
							192, 192, 192)); // #COCOCO
				} else {
					da_rl_popo_list_item.setBackgroundColor(Color.argb(155,
							255, 255, 255)); // #FFFFFF
				}
				return daquyu_list_item;
			}

		}

		class XiaoquyuAdapter extends BaseAdapter implements SectionIndexer {

			private int clickTemp = 0;
			private List<XiaoQuYu> list = null;
			private Context mContext;

			// 标识选择的Item
			public void setSeclection(int position) {
				clickTemp = position;
			}

			public XiaoquyuAdapter(Context mContext, List<XiaoQuYu> list) {
				this.mContext = mContext;
				this.list = list;
			}

			/**
			 * 当ListView数据发生变化时,调用此方法来更新ListView
			 * 
			 * @param list
			 */
			public void updateListView(List<XiaoQuYu> list) {
				this.list = list;
				notifyDataSetChanged();
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View xiaoquyu_list_item = getLayoutInflater().inflate(
						R.layout.list_item, null);
				RelativeLayout xiao_rl_popo_list_item = (RelativeLayout) xiaoquyu_list_item
						.findViewById(R.id.rl_popo_list_item);
				TextView xiao_quyu_tv_item = (TextView) xiaoquyu_list_item
						.findViewById(R.id.tv_item);
				TextView tvLetter = (TextView) xiaoquyu_list_item
						.findViewById(R.id.catalog);
				xiao_quyu_tv_item.setText(list.get(position).getName());
				// 根据position获取分类的首字母的Char ascii值
				int section = getSectionForPosition(position);

				// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
				if (position == getPositionForSection(section)) {
					tvLetter.setVisibility(View.VISIBLE);
					tvLetter.setText(list.get(position).getSortLetters());
				} else {
					tvLetter.setVisibility(View.GONE);
				}
				if (clickTemp == position) {
					xiao_rl_popo_list_item.setBackgroundColor(Color.argb(155,
							192, 192, 192));
				} else {
					xiao_rl_popo_list_item.setBackgroundColor(Color.argb(155,
							255, 255, 255));
				}
				return xiaoquyu_list_item;
			}

			/**
			 * 根据ListView的当前位置获取分类的首字母的Char ascii值
			 */
			public int getSectionForPosition(int position) {
				return list.get(position).getSortLetters().charAt(0);
			}

			/**
			 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
			 */
			public int getPositionForSection(int section) {
				for (int i = 0; i < getCount(); i++) {
					String sortStr = list.get(i).getSortLetters();
					char firstChar = sortStr.toUpperCase().charAt(0);
					if (firstChar == section) {
						return i;
					}
				}

				return -1;
			}

			/**
			 * 提取英文的首字母，非英文字母用#代替。
			 * 
			 * @param str
			 * @return
			 */
			private String getAlpha(String str) {
				String sortStr = str.trim().substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if (sortStr.matches("[A-Z]")) {
					return sortStr;
				} else {
					return "#";
				}
			}

			@Override
			public Object[] getSections() {
				return null;
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
			if (hud != null) {
				hud.dismiss();
			}
			cityCode = data.getStringExtra("cityCode");
			cityName = data.getStringExtra("cityName");
			minPrice = data.getStringExtra("minPrice");
			maxPrice = data.getStringExtra("maxPrice");
			minDiameter = data.getStringExtra("minDiameter");
			maxDiameter = data.getStringExtra("maxDiameter");
			minDbh = data.getStringExtra("minDbh");
			maxDbh = data.getStringExtra("maxDbh");
			minHeight = data.getStringExtra("minHeight");
			maxHeight = data.getStringExtra("maxHeight");
			minLength = data.getStringExtra("minLength");
			maxLength = data.getStringExtra("maxLength");
			minCrown = data.getStringExtra("minCrown");
			maxCrown = data.getStringExtra("maxCrown");
			minOffbarHeight = data.getStringExtra("minOffbarHeight");
			maxOffbarHeight = data.getStringExtra("maxOffbarHeight");
			plantTypes = data.getStringExtra("plantTypes");
			planttype_has_ids = data
					.getStringArrayListExtra("planttype_has_ids");
			searchSpec = data.getStringExtra("searchSpec");
			specMinValue = data.getStringExtra("specMinValue");
			specMaxValue = data.getStringExtra("specMaxValue");
			searchKey = data.getStringExtra("searchKey");
			onRefresh();
		} else if (resultCode == 8) {
			if (hud != null) {
				hud.dismiss();
			}
			searchKey = data.getStringExtra("searchKey");
			tagView.removeAllTags();
			if (!"".equals(searchKey)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(searchKey);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 1; // 1 搜索 2分类
				tagView.addTag(tag);
			}
			onRefresh();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRefresh() {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		final AjaxParams params = new AjaxParams();
		params.put("searchSpec", searchSpec);
		params.put("specMinValue", specMinValue);
		params.put("specMaxValue", specMaxValue);
		params.put("searchKey", searchKey);
		params.put("minPrice", minPrice);
		params.put("maxPrice", maxPrice);
		params.put("minDiameter", minDiameter);
		params.put("maxDiameter", maxDiameter);
		params.put("minDbh", minDbh);
		params.put("maxDbh", maxDbh);
		params.put("minHeight", minHeight);
		params.put("maxHeight", maxHeight);
		params.put("minCrown", minCrown);
		params.put("maxCrown", maxCrown);
		params.put("minOffbarHeight", minOffbarHeight);
		params.put("maxOffbarHeight", maxOffbarHeight);
		params.put("minLength", minLength);
		params.put("maxLength", maxLength);
		params.put("TradeType", supportTradeType);
		params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("secondSeedlingTypeId", secondSeedlingTypeId);
		params.put("cityCode", cityCode);

		plantTypes = "";
		for (int i = 0; i < planttype_has_ids.size(); i++) {
			plantTypes = plantTypes + planttype_has_ids.get(i) + ",";
		}
		if (plantTypes.length() > 0) {
			params.put("plantTypes",
					plantTypes.substring(0, plantTypes.length() - 1));
		} else {
			params.put("plantTypes", plantTypes);
		}

		// if ("".equals(priceSort) && !"".equals(publishDateSort)) {
		// orderBy = publishDateSort;
		// } else if (!"".equals(priceSort) && "".equals(publishDateSort)) {
		// orderBy = priceSort;
		// } else if ("".equals(priceSort) && "".equals(publishDateSort)) {
		// orderBy = "";
		// } else {
		// orderBy = priceSort + "," + publishDateSort;
		// }

		if (orderBy.endsWith(",")) {
			orderBy = orderBy.substring(0, orderBy.length() - 1);
		}
		params.put("orderBy", orderBy);
		params.put("pageSize", 10 + "");
		params.put("pageIndex", 0 + "");
		params.put("latitude", MyApplication.Userinfo.getString("latitude", ""));
		params.put("longitude",
				MyApplication.Userinfo.getString("longitude", ""));
		finalHttp.post(GetServerUrl.getUrl() + "seedling/list", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onStart() {

						super.onStart();
					}

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						AcheData(t.toString());
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

					private void AcheData(String t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t);
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
							}
							ArrayList<String> current_data_ids = new ArrayList<String>();
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										current_data_ids.add(JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
									}
								}

								if (!data_ids.toString().equals(
										current_data_ids.toString())) {
									// TODO Auto-generated method stub
									xListView.setPullLoadEnable(false);
									glistView.setPullLoadEnable(false);
									pageIndex = 0;
									datas.clear();
									if (listAdapter == null) {
										listAdapter = new ProductListAdapter(
												BActivity.this, datas);
										xListView.setAdapter(listAdapter);
									} else {
										listAdapter.notifyDataSetChanged();
									}
									if (gridAdapter == null) {
										gridAdapter = new ProductGridAdapter(
												BActivity.this, datas);
										glistView.setAdapter(gridAdapter);
									} else {
										gridAdapter.notifyDataSetChanged();
									}
									if (getdata == true) {
										initData();
									}
									onLoad();
								} else {
									onLoad();
								}
							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

	}

	@Override
	public void onLoadMore() {
		xListView.setPullRefreshEnable(false);
		glistView.setPullRefreshEnable(false);
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
				glistView.stopRefresh();
				glistView.stopLoadMore();
				glistView.setRefreshTime(new Date().toLocaleString());
				glistView.setPullLoadEnable(true);
				glistView.setPullRefreshEnable(true);

			}
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			int rId = Integer.parseInt(source);
			drawable = getResources().getDrawable(rId);
			// drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
			// drawable.getIntrinsicHeight());
			drawable.setBounds(0, 0, 25, 25);
			return drawable;
		}
	};
	private RelativeLayout rl_choose_type;
	private TagCloudLinkView view;
	private RelativeLayout relativeLayout2;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private TextView tv_xiaoxitishi;
	private ImageView iv_close;
	private LinearLayout ll_01;
	private LinearLayout ll_all;
	private RelativeLayout relativeLayout1;
	private TagView tagView;
	private PopupWindow popupWindow2;
	private SortListAdapter sortListAdapter;
	private LinearLayout ll_choice;

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
				cityCode = mCurrentZipCode;
				cityCode = GetCodeByName.initProvinceDatas(BActivity.this,
						mCurrentProviceName, mCurrentCityName);
				onRefresh();
				// tv_area.setText(cityName);
				if (!BActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!BActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!BActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				BActivity.this, mProvinceDatas));
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
				BActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

}
