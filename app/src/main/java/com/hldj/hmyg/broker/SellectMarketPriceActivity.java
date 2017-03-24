package com.hldj.hmyg.broker;

import info.hoang8f.android.segmented.SegmentedGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedittext.MyAdapter;
import com.example.listedittext.MyAdapter.OnListRemovedListener;
import com.example.listedittext.ParamsListActivity;
import com.example.listedittext.paramsData;
import com.google.gson.Gson;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.hldj.hmyg.ManagerListActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hldj.hmyg.broker.bean.SellectPrice;
import com.hldj.hmyg.buy.bean.StorageSave;
import com.hldj.hmyg.saler.AdressListActivity;
import com.hldj.hmyg.saler.ChooseParamsActivity;
import com.hldj.hmyg.saler.UpdataImageActivity;
import com.hldj.hmyg.saler.bean.ParamsList;
import com.hldj.hmyg.saler.bean.Subscribe;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.TagAdapter;
import com.hy.utils.ToastUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.white.utils.StringUtil;
import com.white.utils.SystemSetting;
import com.yangfuhai.asimplecachedemo.lib.ACache;
import com.yunpay.app.KeyBordStateListener;
import com.yunpay.app.KeyboardLayout3;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin.DayChangeListener;

public class SellectMarketPriceActivity extends BaseSecondActivity implements
		OnTagClickListener, KeyBordStateListener, OnCheckedChangeListener,
		OnItemSelectedListener, OnListRemovedListener, OnWheelChangedListener {
	private String[] days = { "30", "90", "180" };

	private String[] customDays = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41",
			"42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52",
			"53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63",
			"64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74",
			"75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85",
			"86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96",
			"97", "98", "99", "100", "101", "102", "103", "104", "105", "106",
			"107", "108", "109", "110", "111", "112", "113", "114", "115",
			"116", "117", "118", "119", "120", "121", "122", "123", "124",
			"125", "126", "127", "128", "129", "130", "131", "132", "133",
			"134", "135", "136", "137", "138", "139", "140", "141", "142",
			"143", "144", "145", "146", "147", "148", "149", "150", "151",
			"152", "153", "154", "155", "156", "157", "158", "159", "160",
			"161", "162", "163", "164", "165", "166", "167", "168", "169",
			"170", "171", "172", "173", "174", "175", "176", "177", "178",
			"179", "180" };

	private View mainView;
	private TextView tv_day;
	private TextView tv_pic;
	private TextView tv_address;
	private String firstSeedlingTypeId = "";
	private String validity = "";
	private String addressId = "";
	private String fullAddress = "";
	private String detailAddress = "";
	private String contactName = "";
	private String contactPhone = "";
	private String companyName = "";
	private boolean isDefault;
	private String firstSeedlingTypeName = "";

	private TextView tv_firstSeedlingTypeName;

	private EditText et_name;

	private EditText et_price;
	private EditText et_FloorPrice;

	private EditText et_remark;

	private String count = "";

	private String diameter = "";

	private String diameterType = "";

	private String dbh = "";

	private String dbhType = "";

	private String height = "";

	private String crown = "";

	private String offbarHeight = "";

	private String length = "";

	private String plantType = "";
	private String qualityType = "";
	private String qualityGrade = "";
	private String plantTypeName = "";
	private String qualityTypeName = "";
	private String qualityGradeName = "";

	private String unitType = "";
	private String paramsData = "";

	private TextView tv_canshu;

	private TextView tv_qitacanshu;

	private LinearLayout ll_FloorPrice;

	private LinearLayout ll_save;
	private Button save;
	private Gson gson;
	private KProgressHUD hud;
	private String id = "";
	private TextView tv_pics;
	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	ArrayList<String> str_plantTypeLists = new ArrayList<String>();
	ArrayList<String> str_plantTypeList_ids_s = new ArrayList<String>();
	ArrayList<String> str_qualityTypeLists = new ArrayList<String>();
	ArrayList<String> str_qualityTypeList_ids = new ArrayList<String>();
	ArrayList<String> str_qualityGradeLists = new ArrayList<String>();
	ArrayList<String> str_qualityGradeList_ids = new ArrayList<String>();

	private FlowTagLayout mSizeFlowTagLayout;
	private FlowTagLayout mMobileFlowTagLayout;

	private TagAdapter<String> mSizeTagAdapter;
	private TagAdapter<String> mtypeListAdapter;
	private com.zhy.view.flowlayout.TagAdapter<String> tagadapter;
	private com.zhy.view.flowlayout.TagAdapter<String> tagadapter0;
	private com.zhy.view.flowlayout.TagAdapter<String> tagadapter1;
	int tag_a = 0;

	public static SellectMarketPriceActivity instance;
	private KProgressHUD hud_numHud;
	FinalHttp finalHttp = new FinalHttp();
	public int a = 0;

	/** 存储 */
	private static String DB_NAME = "flower.db";
	private static final String DB_TABLE = "savetable";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase db;
	private String storage_save_id = System.currentTimeMillis() + "";

	private TextView pa_tv_unitType;
	private EditText pa_et_count;
	private EditText pa_et_dbh;
	private EditText pa_et_height;
	private EditText pa_et_crown;
	private EditText pa_et_diameter;
	private EditText pa_et_offbarHeight;
	private EditText pa_et_length;

	ListView pa_lv;
	MyAdapter mAdapter;
	private String seedlingTypeId = "";

	private TextView pa_tv_guige;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private String cityCode = "";
	private String cityName = "";
	private SellectPrice sellectPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellect_market_price);
		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			sellectPrice = (SellectPrice) extras.get("sellectPrice");
			subscribe = sellectPrice.getSubscribe();
		}
		mCache = ACache.get(this);
		hud_numHud = KProgressHUD.create(SellectMarketPriceActivity.this)
				.setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true);
		instance = this;
		DBOpenHelper dbOpenHelper = new DBOpenHelper(
				SellectMarketPriceActivity.this, DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
		// 执行SQL语句
		SystemSetting.getInstance(SellectMarketPriceActivity.this).choosePhotoDirId = "";

		hud = KProgressHUD.create(SellectMarketPriceActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);

		Data.pics1.clear();
		Data.photoList.clear();
		Data.microBmList.clear();
		Data.paramsDatas.clear();
		gson = new Gson();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		mainView = (View) findViewById(R.id.ll_mainView);
		com.yunpay.app.KeyboardLayout3 resizeLayout = (com.yunpay.app.KeyboardLayout3) findViewById(R.id.ll_mainView);
		// 获得要控制隐藏和显示的区域
		resizeLayout.setKeyBordStateListener(this);// 设置回调方法
		mSizeFlowTagLayout = (FlowTagLayout) findViewById(R.id.size_flow_layout);
		mMobileFlowTagLayout = (FlowTagLayout) findViewById(R.id.mobile_flow_layout);
		mSizeTagAdapter = new TagAdapter<String>(this);
		mSizeFlowTagLayout
				.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
		mSizeFlowTagLayout.setAdapter(mSizeTagAdapter);
		mSizeFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemSelect(FlowTagLayout parent,
					List<Integer> selectedList) {
				if (selectedList != null && selectedList.size() > 0) {
					StringBuilder sb = new StringBuilder();
					for (int i : selectedList) {
						sb.append(parent.getAdapter().getItem(i));
						sb.append(":");
					}
					ToastUtil.showShortToast(SellectMarketPriceActivity.this,
							"移动研发:" + sb.toString());
				} else {
					ToastUtil.showShortToast(SellectMarketPriceActivity.this,
							"没有选择标签");
				}
			}
		});

		mtypeListAdapter = new TagAdapter<String>(this);
		mMobileFlowTagLayout
				.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
		mMobileFlowTagLayout.setAdapter(mtypeListAdapter);
		mMobileFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
			@Override
			public void onItemSelect(FlowTagLayout parent,
					List<Integer> selectedList) {
				if (selectedList != null && selectedList.size() > 0) {
					StringBuilder sb = new StringBuilder();
					for (int i : selectedList) {
						sb.append(parent.getAdapter().getItem(i));
						sb.append(":");
					}
					ToastUtil.showShortToast(SellectMarketPriceActivity.this,
							"移动研发:" + sb.toString());
				} else {
					ToastUtil.showShortToast(SellectMarketPriceActivity.this,
							"没有选择标签");
				}
			}
		});

		mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
		mFlowLayout.setMaxSelectCount(1);
		mFlowLayout0 = (TagFlowLayout) findViewById(R.id.id_flowlayout0);
		mFlowLayout0.setMaxSelectCount(1);
		mFlowLayout1 = (TagFlowLayout) findViewById(R.id.id_flowlayout1);
		mFlowLayout1.setMaxSelectCount(1);

		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_04_un = (LinearLayout) findViewById(R.id.ll_04_un);
		ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		LinearLayout ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		LinearLayout ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		LinearLayout ll_08 = (LinearLayout) findViewById(R.id.ll_08);
		list_item_adress = (LinearLayout) findViewById(R.id.list_item_adress);
		iv_edit = (ImageView) findViewById(R.id.iv_edit);
		tv_contanct_name = (TextView) findViewById(R.id.tv_name);
		tv_address_name = (TextView) findViewById(R.id.tv_address_name);
		tv_unitType = (TextView) findViewById(R.id.tv_unitType);
		tv_is_defoloat = (TextView) findViewById(R.id.tv_is_defoloat);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		iv_ready_save = (TextView) findViewById(R.id.iv_ready_save);
		save = (Button) findViewById(R.id.save);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		ll_FloorPrice = (LinearLayout) findViewById(R.id.ll_FloorPrice);
		tv_day = (TextView) findViewById(R.id.tv_day);
		tv_pics = (TextView) findViewById(R.id.tv_pics);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_canshu = (TextView) findViewById(R.id.tv_canshu);
		tv_qitacanshu = (TextView) findViewById(R.id.tv_qitacanshu);
		tv_firstSeedlingTypeName = (TextView) findViewById(R.id.tv_firstSeedlingTypeName);
		et_name = (EditText) findViewById(R.id.et_name);
		et_price = (EditText) findViewById(R.id.et_price);
		et_FloorPrice = (EditText) findViewById(R.id.et_FloorPrice);
		et_remark = (EditText) findViewById(R.id.et_remark);
		et_count = (EditText) findViewById(R.id.et_count);

		pa_lv = (ListView) findViewById(R.id.pa_lv);
		if (!"".equals(seedlingTypeId)) {

			if (Data.paramsDatas.size() == 0) {
				initData(seedlingTypeId);
			} else {
				mAdapter = new MyAdapter(Data.paramsDatas,
						SellectMarketPriceActivity.this);
				pa_lv.setAdapter(mAdapter);
				mAdapter.setOnListRemovedListener(SellectMarketPriceActivity.this);
			}
		}
		pa_ll_03 = (LinearLayout) findViewById(R.id.pa_ll_03);
		pa_ll_04 = (LinearLayout) findViewById(R.id.pa_ll_04);
		pa_ll_05 = (LinearLayout) findViewById(R.id.pa_ll_05);
		pa_ll_06 = (LinearLayout) findViewById(R.id.pa_ll_06);
		pa_ll_07 = (LinearLayout) findViewById(R.id.pa_ll_07);
		pa_ll_08 = (LinearLayout) findViewById(R.id.pa_ll_08);
		pa_ll_09 = (LinearLayout) findViewById(R.id.pa_ll_09);
		pa_ll_10 = (LinearLayout) findViewById(R.id.pa_ll_10);
		pa_et_count = (EditText) findViewById(R.id.pa_et_count);
		pa_et_dbh = (EditText) findViewById(R.id.pa_et_dbh);
		pa_et_height = (EditText) findViewById(R.id.pa_et_height);
		pa_et_crown = (EditText) findViewById(R.id.pa_et_crown);
		pa_et_diameter = (EditText) findViewById(R.id.pa_et_diameter);
		pa_et_offbarHeight = (EditText) findViewById(R.id.pa_et_offbarHeight);
		pa_et_length = (EditText) findViewById(R.id.pa_et_length);
		pa_tv_guige = (TextView) findViewById(R.id.pa_tv_guige);
		pa_tv_unitType = (TextView) findViewById(R.id.pa_tv_unitType);

		pa_et_count.addTextChangedListener(mTextWatcher);
		pa_et_dbh.addTextChangedListener(mTextWatcher);
		pa_et_height.addTextChangedListener(mTextWatcher);
		pa_et_crown.addTextChangedListener(mTextWatcher);
		pa_et_diameter.addTextChangedListener(mTextWatcher);
		pa_et_offbarHeight.addTextChangedListener(mTextWatcher);
		pa_et_length.addTextChangedListener(mTextWatcher);

		SegmentedGroup pa_segmented3 = (SegmentedGroup) findViewById(R.id.pa_segmented3);
		SegmentedGroup pa_segmented2 = (SegmentedGroup) findViewById(R.id.pa_segmented2);
		pa_button31 = (RadioButton) findViewById(R.id.pa_button31);
		pa_button32 = (RadioButton) findViewById(R.id.pa_button32);
		pa_button33 = (RadioButton) findViewById(R.id.pa_button33);
		pa_button21 = (RadioButton) findViewById(R.id.pa_button21);
		pa_button2x = (RadioButton) findViewById(R.id.pa_button2x);
		pa_button22 = (RadioButton) findViewById(R.id.pa_button22);

		if ("planted".equals(plantType)) {
		} else if ("transplant".equals(plantType)) {
		} else if ("heelin".equals(plantType)) {
		} else if ("container".equals(plantType)) {
		}

		btn_back.setOnClickListener(multipleClickProcess);
		pa_ll_03.setOnClickListener(multipleClickProcess);
		pa_ll_04.setOnClickListener(multipleClickProcess);
		pa_ll_05.setOnClickListener(multipleClickProcess);
		pa_ll_06.setOnClickListener(multipleClickProcess);
		pa_ll_07.setOnClickListener(multipleClickProcess);
		pa_ll_08.setOnClickListener(multipleClickProcess);
		pa_segmented2.setOnCheckedChangeListener(this);
		pa_segmented3.setOnCheckedChangeListener(this);

		initDataGetFirstType();

		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_04_un.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		ll_06.setOnClickListener(multipleClickProcess);
		ll_07.setOnClickListener(multipleClickProcess);
		ll_08.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		list_item_adress.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);
		save.setOnClickListener(multipleClickProcess);
		iv_ready_save.setOnClickListener(multipleClickProcess);
		if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
			ll_FloorPrice.setVisibility(View.GONE);
		}
	}

	@Override
	public void stateChange(int state) {
		// TODO Auto-generated method stub
		switch (state) {
		case KeyboardLayout3.KEYBORAD_HIDE:
			ll_save.setVisibility(View.VISIBLE);
			break;
		case KeyboardLayout3.KEYBORAD_SHOW:
			ll_save.setVisibility(View.GONE);
			break;
		}
	}

	/** 静态Helper类，用于建立、更新和打开数据库 */
	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		private static final String DB_CREATE = "create table savetable(_id integer primary key autoincrement,img text,title text,time text,money text,storage_save_id text)";

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			_db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
			onCreate(_db);
		}
	}

	private void initDataGetFirstType() {
		ChangeUi(subscribe);
		pa_et_dbh.setText(sellectPrice.getDbh());
		pa_et_height.setText(sellectPrice.getHeight());
		pa_et_crown.setText(sellectPrice.getCrown());
		pa_et_diameter.setText(sellectPrice.getDiameter());
		pa_et_offbarHeight.setText(sellectPrice.getOffbarHeight());
		pa_et_length.setText(sellectPrice.getLength());
		changeUi2(subscribe);
		for (int j = 0; j < str_plantTypeList_ids_s.size(); j++) {
			if (str_plantTypeList_ids_s.get(j).equals(
					sellectPrice.getPlantType())) {
				tagadapter0.setSelectedList(j);
				plantType = sellectPrice.getPlantType();
				plantTypeName = sellectPrice.getPlantTypeName();
			}
		}

		for (int k = 0; k < str_qualityTypeList_ids.size(); k++) {
			if (str_qualityTypeList_ids.get(k).equals(
					sellectPrice.getQualityType())) {
				tagadapter.setSelectedList(k);
				qualityType = sellectPrice.getQualityType();
				qualityTypeName = sellectPrice.getQualityTypeName();
			}
		}
		for (int m = 0; m < str_qualityGradeList_ids.size(); m++) {
			if (str_qualityGradeList_ids.get(m).equals(
					sellectPrice.getQualityGrade())) {
				tagadapter1.setSelectedList(m);
				qualityGrade = sellectPrice.getQualityGrade();
				qualityGradeName = sellectPrice.getQualityGradeName();

			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}

	@Override
	public void finish() {
		super.finish();
		instance = null;
	}

	// 在一开始声明TextWatcher，在afterTextChange内操作
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String text = s.toString();
			int len = s.toString().length();
			if (len == 1 && text.equals("0")) {
				s.clear();
			}
		}

	};

	private TagFlowLayout mFlowLayout;
	private TagFlowLayout mFlowLayout0;
	private TagFlowLayout mFlowLayout1;

	private LinearLayout list_item_adress;

	private ImageView iv_edit;

	private TextView tv_contanct_name;

	private TextView tv_address_name;

	private TextView tv_is_defoloat;

	private EditText et_count;

	private TextView iv_ready_save;

	private ACache mCache;

	private TextView id_tv_edit_all;

	private TextView tv_unitType;

	private Subscribe subscribe;

	private ArrayList<ParamsList> paramsLists;

	private LinearLayout pa_ll_03;

	private LinearLayout pa_ll_04;

	private LinearLayout pa_ll_05;

	private LinearLayout pa_ll_06;

	private LinearLayout pa_ll_07;

	private LinearLayout pa_ll_08;

	private LinearLayout pa_ll_09;

	private LinearLayout pa_ll_10;

	private RadioButton pa_button31;

	private RadioButton pa_button32;

	private RadioButton pa_button33;

	private RadioButton pa_button21;

	private RadioButton pa_button2x;

	private RadioButton pa_button22;

	private LinearLayout ll_05;

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				if (view.getId() == R.id.btn_back) {
					onBackPressed();
				} else if (view.getId() == R.id.ll_01) {
					Intent toSubscribeSearchList4MarketPriceActivity = new Intent(
							SellectMarketPriceActivity.this,
							SubscribeSearchList4MarketPriceActivity.class);
					startActivityForResult(
							toSubscribeSearchList4MarketPriceActivity, 7);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.pa_ll_04) {
					new ActionSheetDialog(SellectMarketPriceActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("单位")
							.addSheetItem("株", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "plant";
											pa_tv_unitType.setText("株");
										}
									})
							.addSheetItem("丛", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "crowd";
											pa_tv_unitType.setText("丛");
										}
									})
							.addSheetItem("斤", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "jin";
											pa_tv_unitType.setText("斤");
										}
									})

							.addSheetItem("平方米", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "squaremeter";
											pa_tv_unitType.setText("平方米");
										}
									}).addSheetItem("袋", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "dai";
											pa_tv_unitType.setText("袋");
										}
									}).addSheetItem("盆", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "pen";
											pa_tv_unitType.setText("盆");
										}
									}).show();
				} else if (view.getId() == R.id.ll_04) {
					if ("".equals(firstSeedlingTypeId)) {
						Toast.makeText(SellectMarketPriceActivity.this,
								"请先选择分类", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent toChooseParamsActivity = new Intent(
							SellectMarketPriceActivity.this,
							ChooseParamsActivity.class);
					toChooseParamsActivity.putStringArrayListExtra(
							"str_plantTypeLists", str_plantTypeLists);
					toChooseParamsActivity.putStringArrayListExtra(
							"str_plantTypeList_ids_s", str_plantTypeList_ids_s);
					toChooseParamsActivity.putExtra("count", count);
					toChooseParamsActivity.putExtra("diameter", diameter);
					toChooseParamsActivity.putExtra("diameterType",
							diameterType);
					toChooseParamsActivity.putExtra("dbh", dbh);
					toChooseParamsActivity.putExtra("dbhType", dbhType);
					toChooseParamsActivity.putExtra("height", height);
					toChooseParamsActivity.putExtra("crown", crown);
					toChooseParamsActivity.putExtra("offbarHeight",
							offbarHeight);
					toChooseParamsActivity.putExtra("length", length);
					toChooseParamsActivity.putExtra("plantType", plantType);
					toChooseParamsActivity.putExtra("unitType", unitType);
					toChooseParamsActivity.putExtra("seedlingTypeId",
							firstSeedlingTypeId);
					startActivityForResult(toChooseParamsActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.list_item_adress) {
					Intent toAdressListActivity1 = new Intent(
							SellectMarketPriceActivity.this,
							AdressListActivity.class);
					toAdressListActivity1.putExtra("addressId", addressId);
					toAdressListActivity1.putExtra("from",
							"SaveSeedlingActivity");
					startActivityForResult(toAdressListActivity1, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.ll_05) {
					showCitys();
				} else if (view.getId() == R.id.ll_06) {
					CustomDaysPickPopwin daysPopwin = new CustomDaysPickPopwin(
							SellectMarketPriceActivity.this,
							new DayChangeListener() {

								@Override
								public void onDayChange(int dayType, int pos) {
									if (dayType == 0) {
										// model.setValidity(Short.valueOf(days[pos]));
										tv_day.setText(Short.valueOf(days[pos])
												+ "天");
										validity = days[pos];
									} else {
										// model.setValidity(Short
										// .valueOf(customDays[pos]));
										tv_day.setText(""
												+ Short.valueOf(customDays[pos])
												+ "天");
										validity = customDays[pos];
									}
									// model.setValidityTextView(daysTv);
								}
							}, days, customDays, 0);
					daysPopwin.showAtLocation(mainView, Gravity.BOTTOM
							| Gravity.CENTER, 0, 0);
				} else if (view.getId() == R.id.ll_07) {
					Intent toUpdataImageActivity = new Intent(
							SellectMarketPriceActivity.this,
							UpdataImageActivity.class);
					Bundle bundleObject = new Bundle();
					final PicSerializableMaplist myMap = new PicSerializableMaplist();
					myMap.setMaplist(urlPaths);
					bundleObject.putSerializable("urlPaths", myMap);
					toUpdataImageActivity.putExtras(bundleObject);
					startActivityForResult(toUpdataImageActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

				} else if (view.getId() == R.id.ll_08) {
					Intent toParamsListActivity = new Intent(
							SellectMarketPriceActivity.this,
							ParamsListActivity.class);
					toParamsListActivity.putExtra("seedlingTypeId",
							firstSeedlingTypeId);
					startActivityForResult(toParamsListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.save) {
					Intent intent = new Intent();
					sellectPrice.setSubscribe(subscribe);
					sellectPrice.setCityCode(cityCode);
					sellectPrice.setCityName(cityName);
					sellectPrice.setCrown(pa_et_crown.getText().toString());
					sellectPrice.setDbh(pa_et_dbh.getText().toString());
					sellectPrice.setDbhType(dbhType);
					sellectPrice.setDiameter(pa_et_diameter.getText()
							.toString());
					sellectPrice.setDiameterType(diameterType);
					sellectPrice
							.setFirstSeedlingTypeId(subscribe.getParentId());
					sellectPrice.setHeight(pa_et_height.getText().toString());
					sellectPrice.setLength(pa_et_length.getText().toString());
					sellectPrice.setName("");
					sellectPrice.setOffbarHeight(pa_et_offbarHeight.getText()
							.toString());
					sellectPrice.setPlantType(plantType);
					sellectPrice.setQualityType(qualityType);
					sellectPrice.setQualityGrade(qualityGrade);
					sellectPrice.setPlantTypeName(plantTypeName);
					sellectPrice.setQualityGradeName(qualityGradeName);
					sellectPrice.setQualityTypeName(qualityTypeName);
					sellectPrice.setSecondSeedlingTypeId(subscribe.getId());
					sellectPrice.setStr_plantTypeLists(str_plantTypeLists);
					sellectPrice
							.setStr_plantTypeList_ids_s(str_plantTypeList_ids_s);
					sellectPrice
							.setStr_qualityGradeLists(str_qualityGradeLists);
					sellectPrice
							.setStr_qualityGradeList_ids(str_qualityGradeList_ids);
					sellectPrice.setStr_qualityTypeLists(str_qualityTypeLists);
					sellectPrice
							.setStr_qualityTypeList_ids(str_qualityTypeList_ids);
					intent.putExtra("sellectPrice", sellectPrice);
					setResult(16, intent);
					finish();
				} else if (view.getId() == R.id.tabs) {
					if ("".equals(et_price.getText().toString())) {
						Toast.makeText(SellectMarketPriceActivity.this,
								"请先输入苗木价格", Toast.LENGTH_SHORT).show();
						return;
					}

					if (Double.parseDouble(et_price.getText().toString()) <= 0) {
						Toast.makeText(SellectMarketPriceActivity.this,
								"请输入超过0的价格", Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(plantType)) {
						Toast.makeText(SellectMarketPriceActivity.this,
								"请选择种植类型", Toast.LENGTH_SHORT).show();
						return;
					}

					if (urlPaths.size() == 0) {
						Toast.makeText(SellectMarketPriceActivity.this,
								"请选择图片上传", Toast.LENGTH_SHORT).show();
						return;
					}

					save.setClickable(false);
					hud_numHud.show();
					a = 0;
					for (int i = 0; i < urlPaths.size(); i++) {

						if (!StringUtil.isHttpUrlPicPath(urlPaths.get(i)
								.getUrl())) {
							GetServerUrl.addHeaders(finalHttp,true);
							finalHttp.addHeader("Content-Type",
									"application/octet-stream");
							AjaxParams params1 = new AjaxParams();
							params1.put("sourceId", "");
							File file1 = new File(urlPaths.get(i).getUrl());
							try {
								params1.put("file", file1);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							params1.put("imagType", "seedling");
							finalHttp.post(GetServerUrl.getUrl()
									+ "admin/file/image", params1,
									new AjaxCallBack<Object>() {

										@Override
										public void onStart() {
											super.onStart();
										}

										@Override
										public void onSuccess(Object t) {
											try {
												JSONObject jsonObject = new JSONObject(
														t.toString());
												int code = jsonObject
														.getInt("code");
												if (code == 1) {
													JSONObject image = JsonGetInfo.getJSONObject(
															JsonGetInfo
																	.getJSONObject(
																			jsonObject,
																			"data"),
															"image");
													urlPaths.set(
															a,
															new Pic(
																	JsonGetInfo
																			.getJsonString(
																					image,
																					"id"),
																	false,
																	JsonGetInfo
																			.getJsonString(
																					image,
																					"url"),
																	a));
													a++;
													hudProgress();
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

											super.onSuccess(t);
										}

										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											// TODO Auto-generated method
											// stub
											super.onFailure(t, errorNo, strMsg);
											save.setClickable(true);
											a++;
											hudProgress();

										}
									});
						} else {
							a++;
							hudProgress();
						}
					}

				} else if (view.getId() == R.id.iv_ready_save) {
					setResult(18);
					finish();
				} else if (view.getId() == R.id.id_tv_edit_all) {
					mCache.remove("saveseedling");
					finish();
					startActivity(new Intent(SellectMarketPriceActivity.this,
							SellectMarketPriceActivity.class));
					//
					// 清空
				} else if (view.getId() == R.id.ll_04_un) {
					new ActionSheetDialog(SellectMarketPriceActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("单位")
							.addSheetItem("株", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "plant";
											tv_unitType.setText("株");
										}
									})
							.addSheetItem("丛", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "crowd";
											tv_unitType.setText("丛");
										}
									})
							.addSheetItem("斤", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "jin";
											tv_unitType.setText("斤");
										}
									})

							.addSheetItem("平方米", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "squaremeter";
											tv_unitType.setText("平方米");
										}
									}).addSheetItem("袋", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "dai";
											tv_unitType.setText("袋");
										}
									}).addSheetItem("盆", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "pen";
											tv_unitType.setText("盆");
										}
									}).show();
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

	public void hudProgress() {
		if (hud_numHud != null
				&& !SellectMarketPriceActivity.this.isFinishing()) {
			hud_numHud.setProgress(a * 100 / urlPaths.size());
			hud_numHud.setProgressText("上传中(" + a + "/" + urlPaths.size()
					+ "张)");
		}
		if (a == urlPaths.size()) {
			if (urlPaths.size() > 0) {
				if (hud_numHud != null
						&& !SellectMarketPriceActivity.this.isFinishing()) {
					hud_numHud.dismiss();
				}

				if (urlPaths.size() > 0) {
					if (!PicValiteIsUtils.needPicValite(urlPaths)) {
						Toast.makeText(SellectMarketPriceActivity.this,
								"请上传完未上传的图片", Toast.LENGTH_SHORT).show();
						return;
					}
					// if(PicValiteIsUtils.needPicValite(urlPaths)){}
					Data.pics1.clear();
					hud.show();
					for (int i = 0; i < urlPaths.size(); i++) {
						Data.pics1.add(urlPaths.get(i));
					}
					seedlingSave();
				} else {
					Toast.makeText(SellectMarketPriceActivity.this, "请选择图片上传",
							Toast.LENGTH_SHORT).show();

				}

			}
		}

	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	private void seedlingSave() {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		if (!"".equals(id)) {
			params.put("id", id);
		}
		params.put("secondSeedlingTypeId", subscribe.getId());
		params.put("firstSeedlingTypeId", subscribe.getParentId());
		params.put("diameter", pa_et_diameter.getText().toString());
		params.put("diameterType", diameterType);
		params.put("dbh", pa_et_dbh.getText().toString());
		params.put("dbhType", dbhType);
		params.put("crown", pa_et_crown.getText().toString());
		params.put("offbarHeight", pa_et_offbarHeight.getText().toString());
		params.put("height", pa_et_height.getText().toString());
		params.put("length", pa_et_length.getText().toString());
		params.put("cityCode", cityCode);//
		params.put("plantType", plantType);
		params.put("qualityType", qualityType);
		params.put("price", et_price.getText().toString());
		params.put("imagesData", gson.toJson(Data.pics1));
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedlingMarket/save",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						save.setClickable(true);
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(SellectMarketPriceActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								Cursor cursor = db.query(DB_TABLE, null,
										"storage_save_id=" + storage_save_id,
										null, null, null, null);
								if (cursor != null && cursor.moveToFirst()) {
									// 已存在，就先删除再存储
									db.delete(DB_TABLE, "storage_save_id="
											+ storage_save_id, null);
								}
								Data.pics1.clear();
								Data.photoList.clear();
								Data.microBmList.clear();
								Data.paramsDatas.clear();
								mCache.remove("saveseedling");
								setResult(1);
								finish();
								Intent toManagerListActivity = new Intent(
										SellectMarketPriceActivity.this,
										ManagerListActivity.class);
								startActivity(toManagerListActivity);
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);
							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
						if (hud != null) {
							hud.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(SellectMarketPriceActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
						if (hud != null) {
							hud.dismiss();
						}
					}

				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			firstSeedlingTypeId = data.getStringExtra("firstSeedlingTypeId");
			firstSeedlingTypeName = data
					.getStringExtra("firstSeedlingTypeName");
			tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
			Data.paramsDatas.clear();
			tv_canshu.setText("");
			tv_qitacanshu.setText("");
		} else if (resultCode == 3) {
			count = data.getStringExtra("count");
			diameter = data.getStringExtra("diameter");
			diameterType = data.getStringExtra("diameterType");
			dbh = data.getStringExtra("dbh");
			dbhType = data.getStringExtra("dbhType");
			height = data.getStringExtra("height");
			crown = data.getStringExtra("crown");
			offbarHeight = data.getStringExtra("offbarHeight");
			length = data.getStringExtra("length");
			plantType = data.getStringExtra("plantType");
			unitType = data.getStringExtra("unitType");
			paramsData = data.getStringExtra("paramsData");
			if (!"".equals(plantType)) {
				tv_canshu.setText("已填写");
			}
		} else if (resultCode == 4) {
			paramsData = data.getStringExtra("paramsData");
			tv_qitacanshu.setText("已填写");
		} else if (resultCode == 5) {
			Bundle bundle = data.getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			tv_pics.setText(PicValiteIsUtils.notiPicValite(urlPaths));
		} else if (resultCode == 14) {
			//
			Bundle bundle = data.getExtras();
			subscribe = (Subscribe) bundle.get("Subscribe");
			ChangeUi(subscribe);
			changeUi2(subscribe);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void ChangeUi(Subscribe subscribe2) {
		// TODO Auto-generated method stub
		paramsLists = subscribe.getParamsLists();
		sellectPrice.setName(subscribe.getName());
		tv_firstSeedlingTypeName.setText(subscribe.getName());
		if (paramsLists != null) {
			if (paramsLists.toString().contains("dbh")) {
				pa_ll_05.setVisibility(View.VISIBLE);
			} else {
				pa_ll_05.setVisibility(View.GONE);
			}
			if (paramsLists.toString().contains("height")) {
				pa_ll_07.setVisibility(View.VISIBLE);
			} else {
				pa_ll_07.setVisibility(View.GONE);
			}
			if (paramsLists.toString().contains("crown")) {
				pa_ll_08.setVisibility(View.VISIBLE);
			} else {
				pa_ll_08.setVisibility(View.GONE);
			}
			if (paramsLists.toString().contains("diameter")) {
				pa_ll_06.setVisibility(View.VISIBLE);
			} else {
				pa_ll_06.setVisibility(View.GONE);
			}
			if (paramsLists.toString().contains("offbarHeight")) {
				pa_ll_09.setVisibility(View.GONE);
			} else {
				pa_ll_09.setVisibility(View.GONE);
			}
			if (paramsLists.toString().contains("length")) {
				pa_ll_10.setVisibility(View.GONE);
			} else {
				pa_ll_10.setVisibility(View.GONE);
			}

			if ("size30".equals(dbhType)) {
				pa_button31.setChecked(true);
				pa_tv_guige.setText("地径");
			} else if ("size100".equals(dbhType)) {
				pa_button32.setChecked(true);
				pa_tv_guige.setText("米径");
			} else if ("size130".equals(dbhType)) {
				pa_button33.setChecked(true);
				pa_tv_guige.setText("胸径");
			} else {
				pa_button32.setChecked(true);
				pa_tv_guige.setText("米径");
			}

			if ("size0".equals(diameterType)) {
				pa_button21.setChecked(true);
			} else if ("size10".equals(diameterType)) {
				pa_button2x.setChecked(true);
			} else if ("size30".equals(diameterType)) {
				pa_button22.setChecked(true);
			} else {
				pa_button21.setChecked(true);
			}
		}

	}

	private void changeUi2(Subscribe subscribe2) {
		str_plantTypeLists = subscribe2.getStr_plantTypeLists();
		str_plantTypeList_ids_s = subscribe2.getStr_plantTypeList_ids_s();
		str_qualityTypeLists = subscribe2.getStr_qualityTypeLists();
		str_qualityTypeList_ids = subscribe2.getStr_qualityTypeList_ids();
		str_qualityGradeLists = subscribe2.getStr_qualityGradeLists();
		str_qualityGradeList_ids = subscribe2.getStr_qualityGradeList_ids();

		tagadapter = new com.zhy.view.flowlayout.TagAdapter<String>(
				str_qualityTypeLists) {

			@Override
			public View getView(FlowLayout parent, int position, String s) {
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.tv, mFlowLayout, false);
				tv.setText(s);
				return tv;
			}
		};
		mFlowLayout.setAdapter(tagadapter);
		mFlowLayout
				.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
					@Override
					public boolean onTagClick(View view, int position,
							FlowLayout parent) {
						if (qualityType == str_qualityTypeList_ids
								.get(position)) {
							qualityType = "";
							qualityTypeName = "";
						} else {
							qualityType = str_qualityTypeList_ids.get(position);
							qualityTypeName = str_qualityTypeLists
									.get(position);
						}

						Data.paramsDatas.clear();
						return true;
					}
				});

		tagadapter0 = new com.zhy.view.flowlayout.TagAdapter<String>(
				str_plantTypeLists) {

			@Override
			public View getView(FlowLayout parent, int position, String s) {
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.tv, mFlowLayout0, false);
				tv.setText(s);
				return tv;
			}
		};
		mFlowLayout0.setAdapter(tagadapter0);

		mFlowLayout0
				.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
					@Override
					public boolean onTagClick(View view, int position,
							FlowLayout parent) {

						if (plantType == str_plantTypeList_ids_s.get(position)) {
							plantType = "";
							plantTypeName = "";
						} else {
							plantType = str_plantTypeList_ids_s.get(position);
							plantTypeName = str_plantTypeLists.get(position);
						}
						Data.paramsDatas.clear();
						return true;
					}
				});

		tagadapter1 = new com.zhy.view.flowlayout.TagAdapter<String>(
				str_qualityGradeLists) {

			@Override
			public View getView(FlowLayout parent, int position, String s) {
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.tv, mFlowLayout1, false);
				tv.setText(s);
				return tv;
			}
		};
		mFlowLayout1.setAdapter(tagadapter1);

		mFlowLayout1
				.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
					@Override
					public boolean onTagClick(View view, int position,
							FlowLayout parent) {

						if (qualityGrade == str_qualityGradeList_ids
								.get(position)) {
							qualityGrade = "";
							qualityGradeName = "";
						} else {
							qualityGrade = str_qualityGradeList_ids
									.get(position);
							qualityGradeName = str_qualityGradeLists
									.get(position);
						}
						Data.paramsDatas.clear();
						return true;
					}
				});

	}

	@Override
	public void onTagClick(int position) {
		// TODO Auto-generated method stub
		if (position == -1) {
			Toast.makeText(getApplicationContext(), "点击末尾文字",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(),
					"点击 position : " + position, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.pa_button31:
			dbhType = "size30";
			pa_tv_guige.setText("地径");
			break;
		case R.id.pa_button32:
			dbhType = "size100";
			pa_tv_guige.setText("米径");
			break;
		case R.id.pa_button33:
			dbhType = "size130";
			pa_tv_guige.setText("胸径");
			break;
		case R.id.pa_button21:
			diameterType = "size0";
			break;
		case R.id.pa_button2x:
			diameterType = "size10";
			break;
		case R.id.pa_button22:
			diameterType = "size30";
			break;
		default:
			// Nothing to do
		}
	}

	private void initData(String seedlingTypeId) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("seedlingTypeId", seedlingTypeId);
		finalHttp.post(GetServerUrl.getUrl() + "seedlingType/getParams",
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
										"data").getJSONArray("paramList");
								if (typeList.length() > 0) {
									for (int i = 0; i < typeList.length(); i++) {
										JSONObject jsonObject2 = typeList
												.getJSONObject(i);
										paramsData n = new paramsData();
										n.setValue("");
										n.setCode(JsonGetInfo.getJsonString(
												jsonObject2, "code"));
										n.setName(JsonGetInfo.getJsonString(
												jsonObject2, "name"));
										Data.paramsDatas.add(n);
									}
								}

								if (Data.paramsDatas.size() > 0) {
									mAdapter = new MyAdapter(Data.paramsDatas,
											SellectMarketPriceActivity.this);
									pa_lv.setAdapter(mAdapter);
									mAdapter.setOnListRemovedListener(SellectMarketPriceActivity.this);
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
						Toast.makeText(SellectMarketPriceActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	public void onRemoved() {
		// TODO Auto-generated method stub
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

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
						SellectMarketPriceActivity.this, mCurrentProviceName,
						mCurrentCityName);
				cityName = mCurrentProviceName;
				if (!SellectMarketPriceActivity.this.isFinishing()
						&& dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!SellectMarketPriceActivity.this.isFinishing()
				&& dialog.isShowing()) {
			dialog.cancel();
		} else if (!SellectMarketPriceActivity.this.isFinishing()
				&& dialog != null && !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				SellectMarketPriceActivity.this, mProvinceDatas));
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
				SellectMarketPriceActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

}
