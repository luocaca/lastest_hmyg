package com.hldj.hmyg.saler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedittext.ParamsListActivity;
import com.example.listedittext.paramsData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.hldj.hmyg.ManagerListActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.adapter.PublishFlowerInfoPhotoAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hldj.hmyg.bean.Type;
import com.hldj.hmyg.buy.bean.StorageSave;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.TagAdapter;
import com.hy.utils.ToastUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.white.utils.FileUtil;
import com.white.utils.GifImgHelperUtil;
import com.white.utils.ImageTools;
import com.white.utils.StringUtil;
import com.white.utils.SystemSetting;
import com.white.utils.ZzyUtil;
import com.yangfuhai.asimplecachedemo.lib.ACache;
import com.yunpay.app.KeyBordStateListener;
import com.yunpay.app.KeyboardLayout3;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zzy.common.widget.MeasureGridView;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin.DayChangeListener;
import com.zzy.flowers.activity.photoalbum.EditGalleryImageActivity;
import com.zzy.flowers.activity.photoalbum.PhotoActivity;
import com.zzy.flowers.activity.photoalbum.PhotoAlbumActivity;

public class SaveSeedlingActivity extends NeedSwipeBackActivity implements
		OnTagClickListener, KeyBordStateListener {
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
	private String seedlingParams = "";

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
	ArrayList<HashMap<String, Object>> typeLists = new ArrayList<HashMap<String, Object>>();
	List<String> str_typeLists = new ArrayList<String>();
	List<String> str_typeList_ids_s = new ArrayList<String>();
	ArrayList<Type> plantTypeLists = new ArrayList<Type>();
	ArrayList<String> str_plantTypeLists = new ArrayList<String>();
	ArrayList<String> str_plantTypeList_ids_s = new ArrayList<String>();
	private FlowTagLayout mSizeFlowTagLayout;
	private FlowTagLayout mMobileFlowTagLayout;

	private TagAdapter<String> mSizeTagAdapter;
	private TagAdapter<String> mtypeListAdapter;
	private com.zhy.view.flowlayout.TagAdapter<String> tagadapter;
	int tag_a = 0;

	/**
	 */
	public static final String INTENT_START_TYPE_KEY = "intent_start_type";
	public static final String INTENT_DIR_ID_KEY = "intent_dir_id";
	public static final String INTENT_PHOTO_TYPE_KEY = "intent_photo_type";
	public static final String INTENT_HAD_CHOOSE_PHOTO_KEY = "intent_had_choose_photo";
	public static final int INTENT_NOT_NEED_FOR_RESULT = -1;
	/** 图片压缩至960像素以内 */
	public static final int COMPRESS_IMAGE_HEIGHT_PX = 960;
	public static final int COMPRESS_IMAGE_WIDTH_PX = 960;
	/** 显示图片压缩至160像素以内 */
	public static final int GRID_COMPRESS_IMAGE_HEIGHT_PX = 160;
	public static final int GRID_COMPRESS_IMAGE_WIDTH_PX = 160;

	public static final int TO_TAKE_PIC = 1;
	public static final int TO_CHOOSE_PIC = 2;
	/** 图片太大 */
	public static final int PIC_IS_TOO_BIG = 3;
	/** 加载图片失败 */
	public static final int LOAD_PIC_FAILURE = 4;
	/** 添加图片 */
	public static final int ADD_NEW_PIC = 5;
	/** 照片列表 */
	private MeasureGridView photoGv;
	/** 照片适配器 */
	private PublishFlowerInfoPhotoAdapter adapter;
	/** 新增图片的宽 */
	public int newWidth;
	/** 新增图片的高 */
	public int newHeight;

	private String flowerInfoPhotoPath = "";

	private RefreshHandler handler;
	private FlowerInfoPhotoChoosePopwin2 popwin;
	public static SaveSeedlingActivity instance;
	private KProgressHUD hud_numHud;
	FinalHttp finalHttp = new FinalHttp();
	public int a = 0;

	/** 存储 */
	private static String DB_NAME = "flower.db";
	private static final String DB_TABLE = "savetable";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase db;
	private String storage_save_id = System.currentTimeMillis() + "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_seedling);
		mCache = ACache.get(this);
		hud_numHud = KProgressHUD.create(SaveSeedlingActivity.this)
				.setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true);
		instance = this;
		DBOpenHelper dbOpenHelper = new DBOpenHelper(SaveSeedlingActivity.this,
				DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
		// 执行SQL语句
		SystemSetting.getInstance(SaveSeedlingActivity.this).choosePhotoDirId = "";

		hud = KProgressHUD.create(SaveSeedlingActivity.this)
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
		photoGv = (MeasureGridView) findViewById(R.id.publish_flower_info_gv);
		adapter = new PublishFlowerInfoPhotoAdapter(SaveSeedlingActivity.this,
				urlPaths);
		photoGv.setAdapter(adapter);
		PhotoGvOnItemClickListener itemClickListener = new PhotoGvOnItemClickListener();
		photoGv.setOnItemClickListener(itemClickListener);
		handler = new RefreshHandler(this.getMainLooper());
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
					ToastUtil.showShortToast(SaveSeedlingActivity.this, "移动研发:"
							+ sb.toString());
				} else {
					ToastUtil.showShortToast(SaveSeedlingActivity.this,
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
					ToastUtil.showShortToast(SaveSeedlingActivity.this, "移动研发:"
							+ sb.toString());
				} else {
					ToastUtil.showShortToast(SaveSeedlingActivity.this,
							"没有选择标签");
				}
			}
		});

		mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
		mFlowLayout.setMaxSelectCount(1);

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
		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			if (getIntent().getStringExtra("storage_save_id") != null) {
				storage_save_id = getIntent().getStringExtra("storage_save_id");
			}
			Bundle bundle = getIntent().getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			adapter.notify(urlPaths);
			firstSeedlingTypeId = getIntent().getStringExtra(
					"firstSeedlingTypeId");
			validity = getIntent().getStringExtra("validity");
			addressId = getIntent().getStringExtra("addressId");
			firstSeedlingTypeName = getIntent().getStringExtra(
					"firstSeedlingTypeName");
			seedlingParams = getIntent().getStringExtra("seedlingParams");
			count = getIntent().getStringExtra("count");
			diameter = getIntent().getStringExtra("diameter");
			diameterType = getIntent().getStringExtra("diameterType");
			dbh = getIntent().getStringExtra("dbh");
			dbhType = getIntent().getStringExtra("dbhType");
			height = getIntent().getStringExtra("height");
			crown = getIntent().getStringExtra("crown");
			offbarHeight = getIntent().getStringExtra("offbarHeight");
			length = getIntent().getStringExtra("length");
			plantType = getIntent().getStringExtra("plantType");
			unitType = getIntent().getStringExtra("unitType");
			paramsData = getIntent().getStringExtra("paramsData");
			ArrayList<paramsData> ps = gson.fromJson(paramsData,
					new TypeToken<ArrayList<paramsData>>() {
					}.getType());
			if (ps != null && ps.size() > 0) {
				Data.paramsDatas = ps;
			}
			contactName = getIntent().getStringExtra("contactName");
			contactPhone = getIntent().getStringExtra("contactPhone");
			fullAddress = getIntent().getStringExtra("address");
			isDefault = getIntent().getBooleanExtra("isDefault", false);
			tv_day.setText(getIntent().getStringExtra("lastDay"));
			et_name.setText(getIntent().getStringExtra("name"));
			et_price.setText(getIntent().getStringExtra("price"));
			et_FloorPrice.setText(getIntent().getStringExtra("floorPrice"));
			et_remark.setText(getIntent().getStringExtra("remarks"));
			et_count.setText(count);
			tv_pics = (TextView) findViewById(R.id.tv_pics);
			tv_qitacanshu = (TextView) findViewById(R.id.tv_qitacanshu);
			tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
			if (!"".equals(plantType)) {
				tv_canshu.setText("已填写");
			}
			if (!"".equals(addressId)) {
				list_item_adress.setVisibility(View.VISIBLE);
				ll_05.setVisibility(View.GONE);
				tv_contanct_name.setText(contactName + "\u0020" + contactPhone);
				tv_address_name.setText(fullAddress);
				if (isDefault) {
					tv_is_defoloat.setVisibility(View.VISIBLE);
				} else {
					tv_is_defoloat.setVisibility(View.GONE);
				}
			}

		} else {
			// 从缓存中取
			if (mCache.getAsString("saveseedling") != null
					&& !"".equals(mCache.getAsString("saveseedling"))) {
				StorageSave fromJson = gson.fromJson(
						mCache.getAsString("saveseedling"),
						com.hldj.hmyg.buy.bean.StorageSave.class);

				id = fromJson.getId();
				storage_save_id = fromJson.getStorage_save_id();
				urlPaths = fromJson.getUrlPaths();
				adapter.notify(urlPaths);
				firstSeedlingTypeId = fromJson.getFirstSeedlingTypeId();
				validity = fromJson.getValidity();
				addressId = fromJson.getNurseryId();
				contactName = fromJson.getContactName();
				contactPhone = fromJson.getContactPhone();
				fullAddress = fromJson.getAddress();
				isDefault = fromJson.isDefault();
				seedlingParams = fromJson.getSeedlingParams();
				count = fromJson.getCount();
				diameter = fromJson.getDiameter();
				diameterType = fromJson.getDiameterType();
				dbh = fromJson.getDbh();
				dbhType = fromJson.getDbhType();
				height = fromJson.getHeight();
				crown = fromJson.getCrown();
				offbarHeight = fromJson.getOffbarHeight();
				length = fromJson.getLength();
				plantType = fromJson.getPlantType();
				unitType = fromJson.getUnitType();
				paramsData = fromJson.getParamsData();
				ArrayList<paramsData> ps = gson.fromJson(paramsData,
						new TypeToken<ArrayList<paramsData>>() {
						}.getType());
				if (ps != null && ps.size() > 0) {
					Data.paramsDatas = ps;
				}
				tv_day.setText(fromJson.getValidity());
				et_name.setText(fromJson.getName());
				et_price.setText(fromJson.getPrice());
				et_FloorPrice.setText(fromJson.getFloorPrice());
				et_remark.setText(fromJson.getRemarks());
				et_count.setText(count);
				tv_pics = (TextView) findViewById(R.id.tv_pics);
				tv_qitacanshu = (TextView) findViewById(R.id.tv_qitacanshu);
				tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
				if (!"".equals(plantType)) {
					tv_canshu.setText("已填写");
				}
				if (!"".equals(addressId)) {
					list_item_adress.setVisibility(View.VISIBLE);
					ll_05.setVisibility(View.GONE);
					tv_contanct_name.setText(contactName + "\u0020"
							+ contactPhone);
					tv_address_name.setText(fullAddress);
					if (isDefault) {
						tv_is_defoloat.setVisibility(View.VISIBLE);
					} else {
						tv_is_defoloat.setVisibility(View.GONE);
					}

				}

			}

		}

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
			ll_FloorPrice.setVisibility(View.VISIBLE);
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
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/initPublish",
				params, new AjaxCallBack<Object>() {

					@SuppressWarnings({ "rawtypes", "unchecked" })
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
								JSONArray typeList = JsonGetInfo.getJsonArray(
										data, "typeList");
								JSONArray plantTypeList = JsonGetInfo
										.getJsonArray(data, "plantTypeList");
								JSONObject nursery = JsonGetInfo.getJSONObject(
										data, "nursery");
								if ("".equals(addressId)) {
									addressId = JsonGetInfo.getJsonString(
											nursery, "id");
									fullAddress = JsonGetInfo.getJsonString(
											nursery, "fullAddress");
									detailAddress = JsonGetInfo.getJsonString(
											nursery, "detailAddress");
									contactName = JsonGetInfo.getJsonString(
											nursery, "contactName");
									contactPhone = JsonGetInfo.getJsonString(
											nursery, "contactPhone");
									companyName = JsonGetInfo.getJsonString(
											nursery, "companyName");
									isDefault = JsonGetInfo.getJsonBoolean(
											nursery, "isDefault");
								}
								if (!"".equals(addressId)) {
									list_item_adress
											.setVisibility(View.VISIBLE);
									ll_05.setVisibility(View.GONE);
									tv_contanct_name.setText(contactName
											+ "\u0020" + contactPhone);
									tv_address_name.setText(fullAddress);
									if (isDefault) {
										tv_is_defoloat
												.setVisibility(View.VISIBLE);
									} else {
										tv_is_defoloat.setVisibility(View.GONE);
									}
								}

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
											.getJsonString(jsonObject2,
													"seedlingParams"));
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
									hMap.put("defaultUnit", JsonGetInfo
											.getJsonString(jsonObject2,
													"defaultUnit"));
									hMap.put("defaultValidity", JsonGetInfo
											.getJsonInt(jsonObject2,
													"defaultValidity"));
									typeLists.add(hMap);
									str_typeLists.add(JsonGetInfo
											.getJsonString(jsonObject2, "name"));
									str_typeList_ids_s.add(JsonGetInfo
											.getJsonString(jsonObject2, "id"));
									if (i == 0
											&& firstSeedlingTypeId.equals("")) {
										// 编辑进行初始化
										firstSeedlingTypeId = JsonGetInfo
												.getJsonString(jsonObject2,
														"id");
										firstSeedlingTypeName = JsonGetInfo
												.getJsonString(jsonObject2,
														"name");
										seedlingParams = JsonGetInfo
												.getJsonString(jsonObject2,
														"seedlingParams");
										validity = JsonGetInfo.getJsonInt(
												jsonObject2, "defaultValidity")
												+ "";
										unitType = JsonGetInfo.getJsonString(
												jsonObject2, "defaultUnit");
										tv_day.setText(validity + "天");

									}
								}
								if (typeLists.size() > 0) {
									mtypeListAdapter.onlyAddAll(str_typeLists);
									tagadapter = new com.zhy.view.flowlayout.TagAdapter<String>(
											str_typeLists) {

										@Override
										public View getView(FlowLayout parent,
												int position, String s) {
											TextView tv = (TextView) getLayoutInflater()
													.inflate(R.layout.tv,
															mFlowLayout, false);
											tv.setText(s);
											return tv;
										}
									};
									mFlowLayout.setAdapter(tagadapter);
									mFlowLayout
											.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
												@Override
												public boolean onTagClick(
														View view,
														int position,
														FlowLayout parent) {
													firstSeedlingTypeId = typeLists
															.get(position)
															.get("id")
															.toString();
													firstSeedlingTypeName = str_typeLists
															.get(position);
													seedlingParams = typeLists
															.get(position)
															.get("seedlingParams")
															.toString();
													tv_firstSeedlingTypeName
															.setText(firstSeedlingTypeName);
													unitType = typeLists
															.get(position)
															.get("defaultUnit")
															.toString();
													validity = typeLists
															.get(position)
															.get("defaultValidity")
															.toString();
													if ("plant"
															.equals(unitType)) {
														tv_unitType
																.setText("株");
													} else if ("crowd"
															.equals(unitType)) {
														tv_unitType
																.setText("丛");
													} else if ("jin"
															.equals(unitType)) {
														tv_unitType
																.setText("斤");
													} else if ("squaremeter"
															.equals(unitType)) {
														tv_unitType
																.setText("平方米");
													} else if ("dai"
															.equals(unitType)) {
														tv_unitType
																.setText("袋");
													} else if ("pen"
															.equals(unitType)) {
														tv_unitType
																.setText("盆");
													} else {
														tv_unitType.setText("");
													}
													tv_day.setText(validity
															+ "天");
													Data.paramsDatas.clear();
													return true;
												}
											});

									for (int i = 0; i < str_typeList_ids_s
											.size(); i++) {
										if (firstSeedlingTypeId
												.equals(str_typeList_ids_s
														.get(i))) {
											tag_a = i;
											tagadapter.setSelectedList(tag_a);
										} else {
										}

									}

								}

								// 传值给下个页面
								for (int i = 0; i < plantTypeList.length(); i++) {
									JSONObject plantType1 = plantTypeList
											.getJSONObject(i);
									Type mType = new Type(
											JsonGetInfo.getJsonString(
													plantType1, "value"),
											JsonGetInfo.getJsonString(
													plantType1, "text"), "", 0);
									plantTypeLists.add(mType);
									str_plantTypeLists.add(JsonGetInfo
											.getJsonString(plantType1, "text"));
									str_plantTypeList_ids_s.add(JsonGetInfo
											.getJsonString(plantType1, "value"));
									// if (i == 0 && "".equals(plantType)) {
									// plantType = JsonGetInfo.getJsonString(
									// plantType1, "value");
									// //不需要默认
									// }
								}
								if ("plant".equals(unitType)) {
									tv_unitType.setText("株");
								} else if ("crowd".equals(unitType)) {
									tv_unitType.setText("丛");
								} else if ("jin".equals(unitType)) {
									tv_unitType.setText("斤");
								} else if ("squaremeter".equals(unitType)) {
									tv_unitType.setText("平方米");
								} else if ("dai".equals(unitType)) {
									tv_unitType.setText("袋");
								} else if ("pen".equals(unitType)) {
									tv_unitType.setText("盆");
								} else {
									tv_unitType.setText("");
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
						Toast.makeText(SaveSeedlingActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mCache.remove("saveseedling");
		StorageSave mStorageSave = new StorageSave();
		mStorageSave.setStorage_save_id(storage_save_id);
		mStorageSave.setId(id);
		mStorageSave.setFirstSeedlingTypeId(firstSeedlingTypeId);
		mStorageSave.setSeedlingParams(seedlingParams);
		mStorageSave.setName(et_name.getText().toString());
		mStorageSave.setPrice(et_price.getText().toString());
		mStorageSave.setFloorPrice(et_FloorPrice.getText().toString());
		mStorageSave.setValidity(validity);
		mStorageSave.setNurseryId(addressId);
		mStorageSave.setContactName(contactName);
		mStorageSave.setContactPhone(contactPhone);
		mStorageSave.setDefault(isDefault);
		mStorageSave.setCount(et_count.getText().toString());
		mStorageSave.setDiameterType(diameterType);
		mStorageSave.setDbhType(dbhType);
		mStorageSave.setDbh(dbh);
		mStorageSave.setHeight(height);
		mStorageSave.setCrown(crown);
		mStorageSave.setDiameter(diameter);
		mStorageSave.setOffbarHeight(offbarHeight);
		mStorageSave.setLength(length);
		mStorageSave.setPlantType(plantType);
		mStorageSave.setUnitType(unitType);
		mStorageSave.setUrlPaths(urlPaths);
		mStorageSave.setParamsData(paramsData);
		// 额外数据
		mStorageSave.setRemarks(et_remark.getText().toString());
		mStorageSave.setAddress(fullAddress);
		// 保存缓存
		mCache.put("saveseedling", gson.toJson(mStorageSave));
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
	private TagFlowLayout mFlowLayout2;

	private LinearLayout list_item_adress;

	private ImageView iv_edit;

	private TextView tv_contanct_name;

	private TextView tv_address_name;

	private TextView tv_is_defoloat;

	private LinearLayout ll_05;

	private EditText et_count;

	private TextView iv_ready_save;

	private ACache mCache;

	private TextView id_tv_edit_all;

	private TextView tv_unitType;

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
					Intent toChooseFirstTypeActivity = new Intent(
							SaveSeedlingActivity.this,
							ChooseFirstTypeActivity.class);
					startActivityForResult(toChooseFirstTypeActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.ll_04) {
					if ("".equals(firstSeedlingTypeId)) {
						Toast.makeText(SaveSeedlingActivity.this, "请先选择分类",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Intent toChooseParamsActivity = new Intent(
							SaveSeedlingActivity.this,
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
					toChooseParamsActivity.putExtra("seedlingParams",
							seedlingParams);
					toChooseParamsActivity.putExtra("seedlingTypeId",
							firstSeedlingTypeId);
					startActivityForResult(toChooseParamsActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.list_item_adress) {
					Intent toAdressListActivity1 = new Intent(
							SaveSeedlingActivity.this, AdressListActivity.class);
					toAdressListActivity1.putExtra("addressId", addressId);
					toAdressListActivity1.putExtra("from",
							"SaveSeedlingActivity");
					startActivityForResult(toAdressListActivity1, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.ll_05) {
					Intent toAdressListActivity = new Intent(
							SaveSeedlingActivity.this, AdressListActivity.class);
					toAdressListActivity.putExtra("addressId", addressId);
					toAdressListActivity.putExtra("from",
							"SaveSeedlingActivity");
					startActivityForResult(toAdressListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.ll_06) {
					CustomDaysPickPopwin daysPopwin = new CustomDaysPickPopwin(
							SaveSeedlingActivity.this, new DayChangeListener() {

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
							SaveSeedlingActivity.this,
							UpdataImageActivity.class);
					Bundle bundleObject = new Bundle();
					final PicSerializableMaplist myMap = new PicSerializableMaplist();
					myMap.setMaplist(urlPaths);
					bundleObject.putSerializable("urlPaths", myMap);
					toUpdataImageActivity.putExtras(bundleObject);
					startActivityForResult(toUpdataImageActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

					// Intent intent = new Intent(SaveSeedlingActivity.this,
					// ChoosePicsActivity.class);
					// startActivityForResult(intent, 1);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

					// Intent intent = new Intent(SaveSeedlingActivity.this,
					// ChooseMorePicsActivity.class);
					// startActivityForResult(intent, 1);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

				} else if (view.getId() == R.id.ll_08) {
					Intent toParamsListActivity = new Intent(
							SaveSeedlingActivity.this, ParamsListActivity.class);
					toParamsListActivity.putExtra("seedlingTypeId",
							firstSeedlingTypeId);
					startActivityForResult(toParamsListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.save) {
					if ("".equals(firstSeedlingTypeId)) {
						Toast.makeText(SaveSeedlingActivity.this, "请先选择分类",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(et_name.getText().toString())) {
						Toast.makeText(SaveSeedlingActivity.this, "请先输入品名",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(et_price.getText().toString())) {
						Toast.makeText(SaveSeedlingActivity.this, "请先输入单价",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (Double.parseDouble(et_price.getText().toString()) <= 0) {
						Toast.makeText(SaveSeedlingActivity.this, "请输入超过0的价格",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (MyApplication.Userinfo.getBoolean("isDirectAgent",
							false)) {
						if ("".equals(et_FloorPrice.getText().toString())) {
							Toast.makeText(SaveSeedlingActivity.this, "请先输入底价",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.parseDouble(et_FloorPrice.getText()
								.toString()) <= 0) {
							Toast.makeText(SaveSeedlingActivity.this,
									"请输入超过0的底价", Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.parseDouble(et_FloorPrice.getText()
								.toString()) > Double.parseDouble(et_price
								.getText().toString())) {
							Toast.makeText(SaveSeedlingActivity.this,
									"输入底价不能超过苗木价格", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					if ("".equals(et_count.getText().toString())) {
						Toast.makeText(SaveSeedlingActivity.this, "请先输入数量",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(validity)) {
						Toast.makeText(SaveSeedlingActivity.this, "请先选择发布有效期",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(addressId)) {
						Toast.makeText(SaveSeedlingActivity.this, "请先选择苗源地址",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(plantType)) {
						Toast.makeText(SaveSeedlingActivity.this, "请选择种植类型",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(unitType)) {
						Toast.makeText(SaveSeedlingActivity.this, "请选择单位",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (urlPaths.size() == 0) {
						Toast.makeText(SaveSeedlingActivity.this, "请选择图片上传",
								Toast.LENGTH_SHORT).show();
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

					if (urlPaths.size() == 0) {
						Toast.makeText(SaveSeedlingActivity.this, "请选择图片上传",
								Toast.LENGTH_SHORT).show();
						return;
					}

					Cursor cursor = db.query(DB_TABLE, null, "storage_save_id="
							+ storage_save_id, null, null, null, null);
					if (cursor != null && cursor.moveToFirst()) {
						// 已存在，就先删除再存储
						db.delete(DB_TABLE, "storage_save_id="
								+ storage_save_id, null);
					} else {
						// 数据库还没这条数据
					}
					StorageSave mStorageSave = new StorageSave();
					mStorageSave.setStorage_save_id(storage_save_id);
					mStorageSave.setId(id);
					mStorageSave.setFirstSeedlingTypeId(firstSeedlingTypeId);
					mStorageSave.setSeedlingParams(seedlingParams);
					mStorageSave.setName(et_name.getText().toString());
					mStorageSave.setPrice(et_price.getText().toString());
					mStorageSave.setFloorPrice(et_FloorPrice.getText()
							.toString());
					mStorageSave.setValidity(validity);
					mStorageSave.setNurseryId(addressId);
					mStorageSave.setContactName(contactName);
					mStorageSave.setContactPhone(contactPhone);
					mStorageSave.setDefault(isDefault);
					mStorageSave.setCount(et_count.getText().toString());
					mStorageSave.setDiameterType(diameterType);
					mStorageSave.setDbhType(dbhType);
					mStorageSave.setDbh(dbh);
					mStorageSave.setHeight(height);
					mStorageSave.setCrown(crown);
					mStorageSave.setDiameter(diameter);
					mStorageSave.setOffbarHeight(offbarHeight);
					mStorageSave.setLength(length);
					mStorageSave.setPlantType(plantType);
					mStorageSave.setUnitType(unitType);
					mStorageSave.setUrlPaths(urlPaths);
					mStorageSave.setParamsData(paramsData);
					mStorageSave.setRemarks(et_remark.getText().toString());
					mStorageSave.setAddress(fullAddress);

					ContentValues cValue = new ContentValues();
					cValue.put("img", urlPaths.get(0).getUrl());
					cValue.put("title", gson.toJson(mStorageSave));
					cValue.put("time", new Date().toLocaleString());
					cValue.put("money", et_price.getText().toString());
					cValue.put("storage_save_id", storage_save_id);
					// 调用insert()方法插入数据
					db.insert(DB_TABLE, null, cValue);
					ToastUtil.showShortToast(SaveSeedlingActivity.this,
							"已成功存入草稿箱，请尽快上传。");
					mCache.remove("saveseedling");
					setResult(1);
					finish();

				} else if (view.getId() == R.id.id_tv_edit_all) {
					mCache.remove("saveseedling");
					finish();
					startActivity(new Intent(SaveSeedlingActivity.this,
							SaveSeedlingActivity.class));
					//
					// 清空
				} else if (view.getId() == R.id.ll_04_un) {
					new ActionSheetDialog(SaveSeedlingActivity.this)
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
									})
							.addSheetItem("袋", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "dai";
											tv_unitType.setText("袋");
										}
									})
							.addSheetItem("盆", SheetItemColor.Blue,
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
		if (hud_numHud != null && !SaveSeedlingActivity.this.isFinishing()) {
			hud_numHud.setProgress(a * 100 / urlPaths.size());
			hud_numHud.setProgressText("上传中(" + a + "/" + urlPaths.size()
					+ "张)");
		}
		if (a == urlPaths.size()) {
			if (urlPaths.size() > 0) {
				if (hud_numHud != null
						&& !SaveSeedlingActivity.this.isFinishing()) {
					hud_numHud.dismiss();
				}

				if (urlPaths.size() > 0) {
					if (!PicValiteIsUtils.needPicValite(urlPaths)) {
						Toast.makeText(SaveSeedlingActivity.this, "请上传完为上传的图片",
								Toast.LENGTH_SHORT).show();
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
					Toast.makeText(SaveSeedlingActivity.this, "请选择图片上传",
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
		params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("name", et_name.getText().toString());
		params.put("price", et_price.getText().toString());
		params.put("floorPrice", et_FloorPrice.getText().toString());
		params.put("validity", validity);
		params.put("nurseryId", addressId);
		params.put("count", et_count.getText().toString());
		params.put("diameterType", diameterType);
		params.put("dbhType", dbhType);
		params.put("dbh", dbh);
		params.put("height", height);
		params.put("crown", crown);
		params.put("diameter", diameter);
		params.put("offbarHeight", offbarHeight);
		params.put("length", length);
		params.put("plantType", plantType);
		params.put("unitType", unitType);
		params.put("imagesData", gson.toJson(Data.pics1));
		params.put("paramsData", paramsData);
		params.put("remarks", et_remark.getText().toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/save", params,
				new AjaxCallBack<Object>() {

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
								Toast.makeText(SaveSeedlingActivity.this, msg,
										Toast.LENGTH_SHORT).show();
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
										SaveSeedlingActivity.this,
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
						Toast.makeText(SaveSeedlingActivity.this,
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
		// TODO Auto-generated method stub
		if (requestCode == TO_TAKE_PIC && resultCode == RESULT_OK) {
			try {
				addImageItem(flowerInfoPhotoPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 其次把文件插入到系统图库
			try {
				MediaStore.Images.Media.insertImage(getContentResolver(),
						flowerInfoPhotoPath, flowerInfoPhotoPath, null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 最后通知图库更新
			// sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
			// Uri.parse("file://" + flowerInfoPhotoPath)));
		} else if (resultCode == 1) {
			firstSeedlingTypeId = data.getStringExtra("firstSeedlingTypeId");
			firstSeedlingTypeName = data
					.getStringExtra("firstSeedlingTypeName");
			seedlingParams = data.getStringExtra("seedlingParams");
			tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
			Data.paramsDatas.clear();
			tv_canshu.setText("");
			tv_qitacanshu.setText("");
		} else if (resultCode == 2) {
			addressId = data.getStringExtra("addressId");
			contactPhone = data.getStringExtra("contactPhone");
			contactName = data.getStringExtra("contactName");
			fullAddress = data.getStringExtra("cityName");
			boolean isDefault = data.getBooleanExtra("isDefault", false);
			tv_contanct_name.setText(contactName + "\u0020" + contactPhone);
			tv_address_name.setText(fullAddress);
			if (isDefault) {
				tv_is_defoloat.setVisibility(View.VISIBLE);
			} else {
				tv_is_defoloat.setVisibility(View.GONE);
			}
			ll_05.setVisibility(View.GONE);
			list_item_adress.setVisibility(View.VISIBLE);
			if (!"".equals(addressId)) {
				tv_address.setText("已选择");
			}
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
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	public static void startPhotoActivity(Activity context, int startType,
			String dirId, int photoType, int hadChoosePicCount, int requestCode) {
		Intent intent = new Intent(context, UpdataImageActivity.class);
		intent.putExtra(INTENT_START_TYPE_KEY, startType);
		intent.putExtra(INTENT_DIR_ID_KEY, dirId);
		intent.putExtra(INTENT_PHOTO_TYPE_KEY, photoType);
		intent.putExtra(INTENT_HAD_CHOOSE_PHOTO_KEY, hadChoosePicCount);
		if (requestCode != INTENT_NOT_NEED_FOR_RESULT) {
			context.startActivityForResult(intent, requestCode);
		} else {
			context.startActivity(intent);
		}
	}

	public static void startPhotoAlbumActivity(Context context, int photoType,
			int hadChoosePicCount) {
		Intent intent = new Intent(context, PhotoAlbumActivity.class);
		intent.putExtra(UpdataImageActivity.INTENT_PHOTO_TYPE_KEY, photoType);
		intent.putExtra(UpdataImageActivity.INTENT_HAD_CHOOSE_PHOTO_KEY,
				hadChoosePicCount);
		context.startActivity(intent);
	}

	private class PhotoGvOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == adapter.getUrlPathsCount()) {
				boolean requestCamerPermissions = new PermissionUtils(
						SaveSeedlingActivity.this).requestCamerPermissions(200);
				if (!requestCamerPermissions) {
					Toast.makeText(SaveSeedlingActivity.this, "您未同意拍照权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				boolean requestReadSDCardPermissions = new PermissionUtils(
						SaveSeedlingActivity.this)
						.requestReadSDCardPermissions(200);
				if (!requestReadSDCardPermissions) {
					Toast.makeText(SaveSeedlingActivity.this, "您未同意应用读取SD卡权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				popwin = new FlowerInfoPhotoChoosePopwin2(
						SaveSeedlingActivity.this, SaveSeedlingActivity.this);
				popwin.showAtLocation(mainView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			} else {
				EditGalleryImageActivity.startEditGalleryImageActivity(
						SaveSeedlingActivity.this,
						EditGalleryImageActivity.TO_EDIT_PUBLISH_IMAGE,
						position, adapter.getDataList());
			}

		}
	}

	/**
	 * 拍照
	 */
	public void toTakePic() {
		Log.e("toTakePic", "photostatus1");
		String photostatus = Environment.getExternalStorageState();
		Log.e("toTakePic", photostatus);
		if (photostatus.equals(Environment.MEDIA_MOUNTED)) {
			if (!ZzyUtil.ToastForSdcardSpaceEnough(SaveSeedlingActivity.this,
					true)) {
				// SD卡空间不足
				Toast.makeText(SaveSeedlingActivity.this,
						R.string.sdcard_is_full, Toast.LENGTH_SHORT).show();
				return;
			}
			doTakePhoto();
			Log.e("toTakePic", "photostatus2");
		} else {
			Toast.makeText(SaveSeedlingActivity.this,
					R.string.sdcard_is_unmount, Toast.LENGTH_SHORT).show();
			Log.e("toTakePic", "photostatus3");
		}
	}

	/**
	 * 进入拍照
	 */
	private void doTakePhoto() {
		long str = System.currentTimeMillis();
		String filename = "flower_info_" + str + ".png";
		File photoFile = new File(FileUtil.getFlowerPicPath(""), filename);
		flowerInfoPhotoPath = FileUtil.getFlowerPicPath(filename);
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			startActivityForResult(intent, TO_TAKE_PIC);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(SaveSeedlingActivity.this,
					R.string.cannot_select_pic, Toast.LENGTH_SHORT).show();

		}
		Log.e("toTakePic", "doTakePhoto");
	}

	/**
	 * 选择相片
	 */
	public void toChoosePic() {
		String picstatus = Environment.getExternalStorageState();
		if (picstatus.equals(Environment.MEDIA_MOUNTED)) {
			if (SystemSetting.getInstance(SaveSeedlingActivity.this).choosePhotoDirId
					.length() > 0
					&& SystemSetting.isAlbumHasPhoto(
							SaveSeedlingActivity.this.getContentResolver(),
							SaveSeedlingActivity.this)) {
				// UpdataImageActivity
				// .startPhotoActivity(
				// UpdataImageActivity.this,
				// PhotoActivity.START_TYPE_JUMP_IN_NOT_FROM_ALBUM,
				// SystemSetting
				// .getInstance(UpdataImageActivity.this).choosePhotoDirId,
				// PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
				// adapter.getUrlPathsCount(),
				// PhotoActivity.INTENT_NOT_NEED_FOR_RESULT);
				PhotoAlbumActivity.startPhotoAlbumActivity(
						SaveSeedlingActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			} else {
				PhotoAlbumActivity.startPhotoAlbumActivity(
						SaveSeedlingActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			}
		} else {
			Toast.makeText(SaveSeedlingActivity.this,
					R.string.sdcard_is_unmount, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 添加图片
	 * 
	 * @param sourchImagePath
	 */
	private void addImageItem(String sourchImagePath) throws IOException {
		// TODO 还需要做动态图片的预览处理和大小限制
		String image_path = "";
		long imageSize = 0;
		File file = new File(sourchImagePath);
		// 获取原图的宽高
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bm = ImageTools.decodeFile(sourchImagePath, opts,
				COMPRESS_IMAGE_WIDTH_PX, COMPRESS_IMAGE_HEIGHT_PX);
		if (bm != null) {
			newWidth = bm.getWidth();
			newHeight = bm.getHeight();
		}
		ExifInterface exifInterface = new ExifInterface(sourchImagePath);
		int orc = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				-1);
		// 然后旋转
		int degree = 0;
		if (orc == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		long sourceImgSize = file.length();
		imageSize = sourceImgSize;
		boolean isGif = GifImgHelperUtil.isGif(sourchImagePath);

		/** 如果不是GIF图片 */
		if (!isGif) {
			// SD卡空间足够才压缩
			if (ZzyUtil.ToastForSdcardSpaceEnough(SaveSeedlingActivity.this,
					false)) {
				image_path = CompressAndSaveImg(file, degree, sourchImagePath);
				file = new File(image_path);
				imageSize = file.length();
				if (imageSize > sourceImgSize) {
					image_path = sourchImagePath;
					file = new File(sourchImagePath);
					imageSize = sourceImgSize;
				}
			}
		} else {
			image_path = sourchImagePath;
		}
		// 图片不可超过5M，如果压缩成功，则用压缩后图片
		if (imageSize > ImageTools.MAX_IMAGE_SIZE) {
			handler.sendEmptyMessage(PIC_IS_TOO_BIG);
			return;
		}
		Bitmap showbm = null;
		if (degree == 0) {
			showbm = ImageTools
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
		} else {
			showbm = ImageTools
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
			showbm = ImageTools
					.rotate(file.getAbsolutePath(), showbm, degree,
							GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
		}
		if (showbm != null) {
			flowerInfoPhotoPath = image_path;
			Pic pic = new Pic("", false, flowerInfoPhotoPath, 0);
			adapter.addItem(pic);
			handler.sendEmptyMessage(ADD_NEW_PIC);
		} else {
			handler.sendEmptyMessage(LOAD_PIC_FAILURE);
			return;
		}
	}

	private class RefreshHandler extends Handler {

		public RefreshHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_PIC_FAILURE:
				Toast.makeText(SaveSeedlingActivity.this,
						R.string.image_load_failed, Toast.LENGTH_SHORT).show();
				break;
			case ADD_NEW_PIC:
				// adapter.notifyDataSetChanged();
				adapter.notify(urlPaths);

				break;
			default:
				break;
			}
		}
	}

	/**
	 * 添加图片
	 */
	public void addPicUrls(ArrayList<Pic> items) {
		// GlobalData.reqSeedlingData.addPictureUrlItems(items);
		adapter.addItems(items);
	}

	/**
	 * 删除图片
	 */
	public void removePicUrls(int pos) {
		// GlobalData.reqSeedlingData.removePic(pos);
		// ZzyUtil.printMessage(adapter.getDataList().get(pos));
		adapter.removeItem(pos);
	}

	/**
	 * 如果是静态图片，则进行压缩处理 压缩并存储临时文件至Image目录
	 * 
	 * @param rotate
	 */
	private String CompressAndSaveImg(File file, int degree,
			String sourceImgPath) throws IOException {
		/** 用于压缩的原图Image */
		Bitmap bitmapSourceImg;
		if (degree == 0) {
			bitmapSourceImg = ImageTools.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX, COMPRESS_IMAGE_WIDTH_PX);
		} else {
			bitmapSourceImg = ImageTools.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX / 2, COMPRESS_IMAGE_WIDTH_PX / 2);
			bitmapSourceImg = ImageTools.rotate(file.getAbsolutePath(),
					bitmapSourceImg, degree, COMPRESS_IMAGE_HEIGHT_PX / 2,
					COMPRESS_IMAGE_WIDTH_PX / 2);
		}
		String img_path = "";
		img_path = FileUtil.getFlowerPicPath("") + "/" + "flower_image_"
				+ System.currentTimeMillis() + ".png";
		File filetemp = new File(img_path);
		// 存储临时文件
		if (bitmapSourceImg != null) {
			FileOutputStream out = new FileOutputStream(filetemp);
			bitmapSourceImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} else {
			return sourceImgPath;
		}
		newWidth = bitmapSourceImg.getWidth();
		newHeight = bitmapSourceImg.getHeight();
		bitmapSourceImg = null;
		return img_path;
	}

}
