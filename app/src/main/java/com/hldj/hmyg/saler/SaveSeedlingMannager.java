package com.hldj.hmyg.saler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hldj.hmyg.buy.bean.CollectCar;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.white.utils.StringUtil;

public class SaveSeedlingMannager {

	private Context context;
	public CollectCar collectcar;
	private String reason = "";
	private KProgressHUD hud_numHud;
	private KProgressHUD hud;

	public int a = 0;
	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private String id = "";
	private String storage_save_id = "";
	private String name = "";
	private String price = "";
	private String floorPrice = "";
	private String count = "";
	private String lastDay = "";
	private String firstSeedlingTypeId = "";
	private String validity = "";
	private String firstSeedlingTypeName = "";
	private String seedlingParams = "";
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
	private String remarks = "";
	/** 存储 */
	private final String DB_NAME = "flower.db";
	private final String DB_TABLE = "savetable";
	private final int DB_VERSION = 1;
	private SQLiteDatabase db;
	private String addressId = "";
	public ArrayList<Pic> pics = new ArrayList<Pic>();
	private Gson gson;
	private int postion;
	OnSaveResultChangeListener onSaveResultChangeListener;
	private ArrayList<com.example.listedittext.paramsData> paramsDatas;

	public void setOnSaveResultChangeListener(
			OnSaveResultChangeListener onSaveResultChangeListener) {
		this.onSaveResultChangeListener = onSaveResultChangeListener;
	}

	public SaveSeedlingMannager(Context context, CollectCar collectcar,
			int postion) {
		this.context = context;
		this.collectcar = collectcar;
		this.postion = postion;

	}

	public void getPath_Space() {
		FinalHttp finalHttp = new FinalHttp();
		gson = new Gson();
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context, DB_NAME, null,
				DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
		// 执行SQL语句
		hud_numHud = KProgressHUD.create(context)
				.setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true);
		hud = KProgressHUD.create(context)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);
		urlPaths = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getUrlPaths();
		id = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getId();
		storage_save_id = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getStorage_save_id();
		name = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getName();
		price = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getPrice();
		floorPrice = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getFloorPrice();
		count = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getCount();
		lastDay = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getValidity();
		firstSeedlingTypeId = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class)
				.getFirstSeedlingTypeId();
		validity = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getValidity();
		addressId = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getNurseryId();
		firstSeedlingTypeName = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class)
				.getFirstSeedlingTypeId();
		seedlingParams = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getSeedlingParams();
		diameter = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getDiameter();
		diameterType = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getDiameterType();
		dbh = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getDbh();
		dbhType = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getDbhType();
		height = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getHeight();
		crown = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getCrown();
		offbarHeight = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getOffbarHeight();
		length = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getLength();
		plantType = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getPlantType();
		unitType = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getUnitType();
		paramsData = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getParamsData();
		remarks = gson.fromJson(collectcar.getTitle(),
				com.hldj.hmyg.buy.bean.StorageSave.class).getRemarks();
		if ("".equals(name)) {
			reason = "未输入苗木名称";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
		}
		if ("".equals(price)) {
			reason = "未输入苗木价格";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
			return;
		}

		if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {

			if ("".equals(floorPrice)) {
				reason = "未输入底价";
				onSaveResultChangeListener.OnSaveResultChange(postion, reason,
						storage_save_id, collectcar);
				return;
			}
			if (Double.parseDouble(floorPrice) <= 0) {
				reason = "请输入超过0的底价";
				onSaveResultChangeListener.OnSaveResultChange(postion, reason,
						storage_save_id, collectcar);
				return;
			}
			if (Double.parseDouble(floorPrice) > Double.parseDouble(price)) {
				reason = "输入底价不能超过苗木价格";
				onSaveResultChangeListener.OnSaveResultChange(postion, reason,
						storage_save_id, collectcar);
				return;
			}
		}
		if ("".equals(validity)) {
			reason = "请先选择发布有效期";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
			return;
		}
		if ("".equals(addressId)) {
			reason = "请先选择苗源地址";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
			return;
		}
		if ("".equals(plantType)) {
			reason = "请选择种植类型";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
			return;
		}
		if ("".equals(unitType)) {
			reason = "请选择单位";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
			return;
		}
		if ("".equals(count)) {
			reason = "请先输入数量";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
			return;
		}

		if (urlPaths.size() == 0) {
			reason = "请选择图片上传";
			onSaveResultChangeListener.OnSaveResultChange(postion, reason,
					storage_save_id, collectcar);
			return;
		}

		hud_numHud.show();
		a = 0;
		for (int i = 0; i < urlPaths.size(); i++) {

			if (!StringUtil.isHttpUrlPicPath(urlPaths.get(i).getUrl())) {
				GetServerUrl.addHeaders(finalHttp,true);
				finalHttp.addHeader("Content-Type", "application/octet-stream");
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
				finalHttp.post(GetServerUrl.getUrl() + "admin/file/image",
						params1, new AjaxCallBack<Object>() {

							@Override
							public void onStart() {
								super.onStart();
							}

							@Override
							public void onSuccess(Object t) {
								try {
									JSONObject jsonObject = new JSONObject(t
											.toString());
									int code = jsonObject.getInt("code");
									if (code == 1) {
										JSONObject image = JsonGetInfo
												.getJSONObject(JsonGetInfo
														.getJSONObject(
																jsonObject,
																"data"),
														"image");
										urlPaths.set(
												a,
												new Pic(JsonGetInfo
														.getJsonString(image,
																"id"), false,
														JsonGetInfo
																.getJsonString(
																		image,
																		"url"),
														0));
								
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
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								// TODO Auto-generated method
								// stub
								super.onFailure(t, errorNo, strMsg);
								a++;
								hudProgress();

							}
						});
			} else {
				a++;
				hudProgress();
			}

		}

	}

	/** 静态Helper类，用于建立、更新和打开数据库 */
	private class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		private final String DB_CREATE = "create table savetable(_id integer primary key autoincrement,img text,title text,time text,money text,storage_save_id text)";

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

	public void hudProgress() {
		if (hud_numHud != null) {
			hud_numHud.setProgress(a * 100 / urlPaths.size());
			hud_numHud.setProgressText("上传中(" + a + "/" + urlPaths.size()
					+ "张)");
		}
		if (a == urlPaths.size()) {
			if (urlPaths.size() > 0) {
				if (hud_numHud != null) {
					hud_numHud.dismiss();
				}

				if (urlPaths.size() > 0) {
					if (!PicValiteIsUtils.needPicValite(urlPaths)) {
						onSaveResultChangeListener.OnSaveResultChange(postion,
								"部分图片上传失败，请上传完未上传的图片", storage_save_id,
								collectcar);
						return;
					}
					// if(PicValiteIsUtils.needPicValite(urlPaths)){}
					pics.clear();
					hud.show();
					for (int i = 0; i < urlPaths.size(); i++) {
						pics.add(urlPaths.get(i));
					}
					seedlingSave();
				} else {
					Toast.makeText(context, "请选择图片上传", Toast.LENGTH_SHORT)
							.show();

				}

			}
		}

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
		params.put("name", name);
		params.put("price", price);
		params.put("floorPrice", floorPrice);
		params.put("validity", validity);
		params.put("nurseryId", addressId);
		params.put("count", count);
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
		params.put("imagesData", gson.toJson(pics));
		params.put("paramsData", gson.toJson(paramsDatas));
		params.put("remarks", remarks);
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/save", params,
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
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
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
								onSaveResultChangeListener.OnSaveResultChange(
										postion, "success", storage_save_id,
										collectcar);
								pics.clear();
								Data.photoList.clear();
								Data.microBmList.clear();
								Data.paramsDatas.clear();

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
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
						if (hud != null) {
							hud.dismiss();
						}
					}

				});

	}

	public interface OnSaveResultChangeListener {
		void OnSaveResultChange(int postion, String reason,
				String storage_save_id, CollectCar collectcar);
	}

}
