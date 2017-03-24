package com.hldj.hmyg;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

import me.hwang.library.widgit.SmartRefreshLayout;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import aom.xingguo.huang.banner.MyFragment;
import cn.hugo.android.scanner.CaptureActivity;

import com.autoscrollview.adapter.ImagePagerAdapter;
import com.autoscrollview.widget.AutoScrollViewPager;
import com.autoscrollview.widget.indicator.CirclePageIndicator;
import com.hldj.hmyg.adapter.HomeFunctionAdapter;
import com.hldj.hmyg.adapter.HomePayAdapter;
import com.hldj.hmyg.adapter.ThematicAdapter;
import com.hldj.hmyg.adapter.TypeAdapter;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.ABanner;
import com.hldj.hmyg.bean.HomeFunction;
import com.hldj.hmyg.bean.HomeStore;
import com.hldj.hmyg.bean.Type;
import com.hldj.hmyg.buyer.PurchaseSearchListActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.javis.ab.view.AbSlidingPlayView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.white.utils.ScreenUtil;
import com.white.utils.StringUtil;
import com.yangfuhai.asimplecachedemo.lib.ACache;

@SuppressLint("NewApi")
public class AActivity extends FragmentActivity implements OnClickListener {

	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	private ArrayList<ABanner> aBanners = new ArrayList<ABanner>();
	private ArrayList<Type> gd_datas = new ArrayList<Type>();
	private ArrayList<Type> gd_home_pay_datas = new ArrayList<Type>();
	private ArrayList<HomeFunction> home_functions = new ArrayList<HomeFunction>();
	ArrayList<HomeStore> url0s = new ArrayList<HomeStore>();
	private ArrayList<HashMap<String, Object>> lv_datas = new ArrayList<HashMap<String, Object>>();
	private ImagePagerAdapter imagePagerAdapter;
	private CirclePageIndicator indicator;
	private AutoScrollViewPager viewPager;
	private GridView gd_00;
	private GridView gd_01;
	private GridView gd;
	private ListView lv_00;
	private ImageView iv_Capture;
	private ImageView iv_msg;
	private DrawerLayout dl_content;
	private ImageView iv_home_merchants;
	private ImageView iv_home_preferential;
	private RelativeLayout relativeLayout2;
	private PtrClassicFrameLayout mPtrFrame;
	private ScrollView scrollView;
	private Button toTopBtn;// 返回顶部的按钮
	private int scrollY = 0;// 标记上次滑动位置
	private View contentView;
	private final String TAG = "test";
	private ImageView iv_fuwu;
	private ImageView iv_fenlei;
	private LinearLayout ll_fenlei;
	private ACache mCache;
	private TypeAdapter myadapter;
	private AbSlidingPlayView absviewPager;
	private ArrayList allListView;
	private SmartRefreshLayout mLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_a);
		mCache = ACache.get(this);
		viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		absviewPager = (AbSlidingPlayView) findViewById(R.id.viewPager_menu);
		// 设置播放方式为顺序播放
		absviewPager.setPlayType(1);
		// 设置播放间隔时间
		absviewPager.setSleepTime(7500);
		iv_msg = (ImageView) findViewById(R.id.iv_msg);
		relativeLayout2 = (RelativeLayout) findViewById(R.id.RelativeLayout2);
		iv_Capture = (ImageView) findViewById(R.id.iv_Capture);
		iv_home_merchants = (ImageView) findViewById(R.id.iv_home_merchants);
		iv_home_preferential = (ImageView) findViewById(R.id.iv_home_preferential);
		iv_fuwu = (ImageView) findViewById(R.id.iv_fuwu);
		iv_fenlei = (ImageView) findViewById(R.id.iv_fenlei);
		gd = (GridView) findViewById(R.id.gd);
		gd_01 = (GridView) findViewById(R.id.gd_01);
		gd_00 = (GridView) findViewById(R.id.gd_00);
		lv_00 = (ListView) findViewById(R.id.lv_00);
		ll_fenlei = (LinearLayout) findViewById(R.id.ll_fenlei);
		lv_00.setDivider(null);
		scrollView = (ScrollView) findViewById(R.id.rotate_header_scroll_view);
		if (contentView == null) {
			contentView = scrollView.getChildAt(0);
		}
		toTopBtn = (Button) findViewById(R.id.top_btn);
		toTopBtn.setOnClickListener(this);
		initView();
		LayoutParams l_params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		WindowManager wm = this.getWindowManager();
		l_params.height = (int) (wm.getDefaultDisplay().getWidth() * 1 / 2);
		viewPager.setLayoutParams(l_params);
		initData();
		iv_Capture.setOnClickListener(this);
		iv_msg.setOnClickListener(this);
		relativeLayout2.setOnClickListener(this);
		initmPtrFrame();

	}

	private void initmPtrFrame() {


		mLayout = (SmartRefreshLayout) findViewById(R.id.rotate_header_web_view_frame);
		mLayout.setOnRefreshListener(new SmartRefreshLayout.onRefreshListener() {
			@Override
			public void onRefresh() {
				mLayout.stopRefresh();
			}

			@Override
			public void onLoadMore() {
				mLayout.stopLoadMore();
			}
		});

	}

	private void initView() {

		// http://blog.csdn.net/jiangwei0910410003/article/details/17024287
		/******************** 监听ScrollView滑动停止 *****************************/
		scrollView.setOnTouchListener(new OnTouchListener() {
			private int lastY = 0;
			private int touchEventId = -9983761;
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					View scroller = (View) msg.obj;
					if (msg.what == touchEventId) {
						if (lastY == scroller.getScrollY()) {
							handleStop(scroller);
						} else {
							handler.sendMessageDelayed(handler.obtainMessage(
									touchEventId, scroller), 5);
							lastY = scroller.getScrollY();
						}
					}
				}
			};

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					handler.sendMessageDelayed(
							handler.obtainMessage(touchEventId, v), 5);
				}
				return false;
			}

			/**
			 * ScrollView 停止
			 * 
			 * @param view
			 */
			private void handleStop(Object view) {

				Log.i(TAG, "handleStop");
				ScrollView scroller = (ScrollView) view;
				scrollY = scroller.getScrollY();

				doOnBorderListener();
			}
		});
		/***********************************************************/

	}

	/**
	 * ScrollView 的顶部，底部判断：
	 * http://www.trinea.cn/android/on-bottom-load-more-scrollview-impl/
	 * 
	 * 其中getChildAt表示得到ScrollView的child View， 因为ScrollView只允许一个child
	 * view，所以contentView.getMeasuredHeight()表示得到子View的高度,
	 * getScrollY()表示得到y轴的滚动距离，getHeight()为scrollView的高度。
	 * 当getScrollY()达到最大时加上scrollView的高度就的就等于它内容的高度了啊~
	 * 
	 * @param pos
	 */
	private void doOnBorderListener() {
		Log.i(TAG,
				ScreenUtil.getScreenViewBottomHeight(scrollView) + "  "
						+ scrollView.getScrollY() + " "
						+ ScreenUtil.getScreenHeight(AActivity.this));

		// 底部判断
		if (contentView != null
				&& contentView.getMeasuredHeight() <= scrollView.getScrollY()
						+ scrollView.getHeight()) {
			toTopBtn.setVisibility(View.VISIBLE);
			Log.i(TAG, "bottom");
		}
		// 顶部判断
		else if (scrollView.getScrollY() == 0) {

			Log.i(TAG, "top");
		}

		else if (scrollView.getScrollY() > 30) {
			toTopBtn.setVisibility(View.VISIBLE);
			Log.i(TAG, "test");
		}

	}

	private void initData() {

		gd_home_pay_datas.add(new Type("fangxin", "放心购", "",
				R.drawable.shouye_fangxingou));
		gd_home_pay_datas.add(new Type("bangwo", "帮我购", "",
				R.drawable.shouye_bangwogou));
		gd_home_pay_datas.add(new Type("danbao", "担保购", "",
				R.drawable.shouye_danbaogou));
		gd_home_pay_datas.add(new Type("weituo", "委托购", "",
				R.drawable.shouye_weitougou));
		if (gd_home_pay_datas.size() > 0) {
			HomePayAdapter myadapter = new HomePayAdapter(AActivity.this,
					gd_home_pay_datas);
			gd_01.setAdapter(myadapter);
		}

		// TODO Auto-generated method stub
		// home_functions.add(new HomeFunction(1, "1", "苗木商城", "",
		// R.drawable.shouye_miaomushangcheng));
		home_functions.add(new HomeFunction(2, "2", "快速报价", "",
				R.drawable.shouye_caigoubaojia));
		home_functions.add(new HomeFunction(3, "3", "地图找苗", "",
				R.drawable.shouye_dituzhaomiao));
		if (home_functions.size() > 0) {
			HomeFunctionAdapter homeFunctionAdapter = new HomeFunctionAdapter(
					AActivity.this, home_functions);
			gd.setAdapter(homeFunctionAdapter);
		}
		if (gd_datas.size() > 0) {
			TypeAdapter myadapter = new TypeAdapter(AActivity.this, gd_datas);
			gd_00.setAdapter(myadapter);
		}
		gd_datas.clear();
		if (myadapter != null) {
			myadapter.notifyDataSetChanged();
		}

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		AjaxParams params = new AjaxParams();
		params.put("latitude", MyApplication.Userinfo.getString("latitude", ""));
		params.put("longitude",
				MyApplication.Userinfo.getString("longitude", ""));
		finalHttp.post(GetServerUrl.getUrl() + "index", params,
				new AjaxCallBack<Object>() {

					private MyFragment myFragment0;
					private Type type;

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						mCache.remove("index");
						mCache.put("index", t.toString());
						LoadCache(t.toString());
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						if (mCache.getAsString("index") != null
								&& !"".equals(mCache.getAsString("index"))) {
							LoadCache(mCache.getAsString("index"));
						}
						Toast.makeText(AActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

					private void LoadCache(String t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t);
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
							}
							if ("1".equals(code)) {

								JSONArray bannerList = JsonGetInfo
										.getJsonArray(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"), "bannerList");
								// 广告
								// JSONArray bannerList = jsonObject
								// .getJSONObject("data").getJSONArray(
								// "bannerList");
								for (int i = 0; i < bannerList.length(); i++) {
									JSONObject jsonObject2 = bannerList
											.getJSONObject(i);
									HashMap<String, Object> hMap = new HashMap<String, Object>();
									hMap.put("isNewRecord", JsonGetInfo
											.getJsonBoolean(jsonObject2,
													"isNewRecord"));
									hMap.put("delFlag", JsonGetInfo
											.getJsonString(jsonObject2,
													"delFlag"));
									hMap.put("url", JsonGetInfo.getJsonString(
											JsonGetInfo.getJSONObject(
													jsonObject2, "imageJson"),
											"url"));
									hMap.put("ossThumbnailImagePath",
											JsonGetInfo.getJsonString(
													JsonGetInfo.getJSONObject(
															jsonObject2,
															"imageJson"),
													"ossThumbnailImagePath"));
									hMap.put("ossMediumImagePath", JsonGetInfo
											.getJsonString(JsonGetInfo
													.getJSONObject(jsonObject2,
															"imageJson"),
													"ossMediumImagePath"));
									hMap.put("ossLargeImagePath", JsonGetInfo
											.getJsonString(JsonGetInfo
													.getJSONObject(jsonObject2,
															"imageJson"),
													"ossLargeImagePath"));
									hMap.put("href", JsonGetInfo.getJsonString(
											jsonObject2, "href"));
									hMap.put("name", JsonGetInfo.getJsonString(
											jsonObject2, "name"));

									ABanner aBanner = new ABanner();
									aBanner.setNewRecord(JsonGetInfo
											.getJsonBoolean(jsonObject2,
													"isNewRecord"));
									aBanner.setUrl(JsonGetInfo.getJsonString(
											JsonGetInfo.getJSONObject(
													jsonObject2, "imageJson"),
											"url"));
									aBanner.setOssThumbnailImagePath(JsonGetInfo
											.getJsonString(JsonGetInfo
													.getJSONObject(jsonObject2,
															"imageJson"),
													"ossThumbnailImagePath"));
									aBanner.setOssLargeImagePath(JsonGetInfo
											.getJsonString(JsonGetInfo
													.getJSONObject(jsonObject2,
															"imageJson"),
													"ossLargeImagePath"));
									aBanner.setOssMediumImagePath(JsonGetInfo
											.getJsonString(JsonGetInfo
													.getJSONObject(jsonObject2,
															"imageJson"),
													"ossMediumImagePath"));
									datas.add(hMap);
									aBanners.add(aBanner);
								}
								if (datas.size() > 0) {
									initViewPager();
									// initAbsViewPager();
								}
								if (aBanners.size() > 0) {
								}

								// 分类
								// JSONArray seedlingTypeList = jsonObject
								// .getJSONObject("data").getJSONArray(
								// "seedlingTypeList");
								JSONArray seedlingTypeList = JsonGetInfo
										.getJsonArray(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"),
												"seedlingTypeList");
								if (seedlingTypeList.length() > 0) {
									gd_datas.clear();
									if (myadapter != null) {
										myadapter.notifyDataSetChanged();
									} else {
										myadapter = new TypeAdapter(
												AActivity.this, gd_datas);
										gd_00.setAdapter(myadapter);
									}
								}
								for (int i = 0; i < seedlingTypeList.length(); i++) {
									JSONObject jsonObject2 = seedlingTypeList
											.getJSONObject(i);
									HashMap<String, Object> hMap = new HashMap<String, Object>();
									String id = JsonGetInfo.getJsonString(
											jsonObject2, "id");
									String icon = JsonGetInfo.getJsonString(
											jsonObject2, "icon");
									String name = JsonGetInfo.getJsonString(
											jsonObject2, "name");
									if ("乔木".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type01);
									} else if ("灌木".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type02);
									} else if ("桩景".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type03);
									} else if ("地被".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type04);
									} else if ("草皮".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type05);
									} else if ("棕榈".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type06);
									} else if ("苏铁".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type07);
									} else if ("更多".equals(name)) {
										type = new Type(id, name, icon,
												R.drawable.home_icon_type08);
									}
									gd_datas.add(type);
								}

								if (gd_datas.size() > 0) {
									if (myadapter != null) {
										myadapter.notifyDataSetChanged();
									} else {
										myadapter = new TypeAdapter(
												AActivity.this, gd_datas);
										gd_00.setAdapter(myadapter);
									}
								}

								// LayoutParams l_params = new
								// LinearLayout.LayoutParams(
								// LayoutParams.MATCH_PARENT,
								// LayoutParams.WRAP_CONTENT);
								// LayoutParams para =
								// ll_fenlei.getLayoutParams();
								// l_params.height = para.height;
								// l_params.width =para.height*2/5;
								// iv_fenlei.setLayoutParams(l_params);
								// iv_fuwu.setLayoutParams(l_params);

								// 专题
								JSONArray thematicList = JsonGetInfo.getJsonArray(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "thematicList");
								// JSONArray thematicList = jsonObject
								// .getJSONObject("data").getJSONArray(
								// "thematicList");
								for (int i = 0; i < thematicList.length(); i++) {
									JSONObject jsonObject2 = thematicList
											.getJSONObject(i);
									HashMap<String, Object> hMap = new HashMap<String, Object>();
									hMap.put("title",
											JsonGetInfo.getJsonString(
													jsonObject2, "title"));
									hMap.put("type", JsonGetInfo.getJsonString(
											jsonObject2, "type"));
									hMap.put("id", JsonGetInfo.getJsonString(
											jsonObject2, "id"));
									hMap.put(
											"ossLargeImagePath",
											JsonGetInfo.getJsonString(
													JsonGetInfo
															.getJSONObject(
																	jsonObject2,
																	"appCoverImageJson"),
													"ossLargeImagePath"));
									hMap.put(
											"url",
											JsonGetInfo.getJsonString(
													JsonGetInfo
															.getJSONObject(
																	jsonObject2,
																	"appCoverImageJson"),
													"url"));
									lv_datas.add(hMap);
								}
								if (lv_datas.size() > 0) {
									ThematicAdapter myadapter = new ThematicAdapter(
											AActivity.this, lv_datas);
									lv_00.setAdapter(myadapter);
									iv_home_preferential
											.setVisibility(View.VISIBLE);
								}

								// 商铺
								// JSONArray storeList =
								// jsonObject.getJSONObject(
								// "data").getJSONArray("storeList");
								JSONArray storeList = JsonGetInfo.getJsonArray(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "storeList");
								for (int i = 0; i < storeList.length(); i++) {
									JSONObject jsonObject2 = storeList
											.getJSONObject(i);
									HomeStore a_first_product = new HomeStore(
											JsonGetInfo.getJsonString(
													jsonObject2, "id"),
											JsonGetInfo.getJsonString(
													jsonObject2, "code"),
											JsonGetInfo.getJsonString(
													jsonObject2, "logoUrl"),
											JsonGetInfo.getJsonString(
													jsonObject2, "id"),
											JsonGetInfo.getJsonString(
													jsonObject2, "name"));
									url0s.add(a_first_product);
								}
								FragmentTransaction ft = getSupportFragmentManager()
										.beginTransaction();
								if (url0s.size() > 0) {
									if (url0s.size() % MyFragment.Num != 0) {
										int array = MyFragment.Num
												- url0s.size() % MyFragment.Num;
										for (int i = 0; i < array; i++) {
											HomeStore a_first_product = new HomeStore(
													"", "", "", "", "");
											url0s.add(a_first_product);
										}
									}
									myFragment0 = new MyFragment();
									myFragment0.setUrls(url0s);
									ft.add(R.id.con0, myFragment0);
									iv_home_merchants
											.setVisibility(View.VISIBLE);
								}
								ft.commitAllowingStateLoss();

							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

	}

	private void initAbsViewPager() {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < datas.size(); i++) {
			// 导入ViewPager的布局
			View view = LayoutInflater.from(this).inflate(R.layout.pic_item,
					null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			if (datas.get(i).get("url").toString().startsWith("http")) {
				ImageLoader.getInstance().displayImage(
						datas.get(i).get("url").toString(), imageView);
				// holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
			} else {
				imageView.setImageResource(R.drawable.ic_launcher);
			}
			allListView.add(view);
		}

		absviewPager.addViews(allListView);
		// 开始轮播
		absviewPager.startPlay();

	}

	private void initViewPager() {

		if (imagePagerAdapter != null) {
			imagePagerAdapter.notifyDataSetChanged();
			return;
		}
		imagePagerAdapter = new ImagePagerAdapter(AActivity.this, datas);
		viewPager.setAdapter(imagePagerAdapter);
		indicator.setViewPager(viewPager);
		// imagePagerAdapter.notifyDataSetChanged();
		// indicator.setRadius(5);
		indicator.setOrientation(LinearLayout.HORIZONTAL);
		// indicator.setStrokeWidth(5);
		// indicator.setSnap(true);
		viewPager.setInterval(3500);
		// viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
		viewPager
				.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
		// viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
		viewPager.setCycle(true);
		viewPager.setBorderAnimation(true);
		viewPager.startAutoScroll();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_btn:
			scrollView.post(new Runnable() {
				@Override
				public void run() {
					scrollView.fullScroll(ScrollView.FOCUS_UP);
				}
			});
			toTopBtn.setVisibility(View.GONE);
			break;
		case R.id.iv_Capture:
			Intent toCaptureActivity = new Intent(AActivity.this,
					CaptureActivity.class);
			startActivityForResult(toCaptureActivity, 1);
			getParent().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.iv_msg:
			Intent toMessageListActivity = new Intent(AActivity.this,
					MessageListActivity.class);
			startActivity(toMessageListActivity);
			getParent().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.RelativeLayout2:
			Intent intent = new Intent(AActivity.this,
					PurchaseSearchListActivity.class);
			intent.putExtra("from", "AActivity");
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 2) {
			String decodeResult = data.getStringExtra("decodeResult");
			// Toast.makeText(AActivity.this,
			// "此功能预留暂不开放，解析成功，结果为：" + decodeResult, Toast.LENGTH_SHORT)
			// .show();
			if (StringUtil.isHttpUrlPicPath(decodeResult)) {
				Intent toWebActivity3 = new Intent(AActivity.this,
						WebActivity.class);
				toWebActivity3.putExtra("title", "标题");
				toWebActivity3.putExtra("url", decodeResult);
				startActivityForResult(toWebActivity3, 1);
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
