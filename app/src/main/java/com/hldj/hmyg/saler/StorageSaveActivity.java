package com.hldj.hmyg.saler;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.barryzhang.temptyview.TViewUtil;
import com.cn2che.androids.swipe.OnlyListViewSwipeGesture;
import com.google.gson.Gson;
import com.hldj.hmyg.BActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buy.bean.CollectCar;
import com.hldj.hmyg.saler.SaveSeedlingMannager.OnSaveResultChangeListener;
import com.zhy.view.HorizontalProgressBarWithNumber;

//草稿界面
public class StorageSaveActivity extends Activity implements OnClickListener {
	private ListView GroupManList;
	private BaseAdapter baseAdapter;

	private static String DB_NAME = "flower.db";
	private static final String DB_TABLE = "savetable";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase db;
	private boolean flag = true; // 全选或全取消
	private ArrayList<CollectCar> userList = new ArrayList<CollectCar>();
	private FinalBitmap fb;
	private Gson gson;
	private ImageView btn_back;
	private TextView tv_all;
	private TextView tv_begin;
	private int progress = 0;

	/**
	 * Called when the activity is first created.
	 */
	@SuppressLint("SdCardPath")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage_save);
		gson = new Gson();
		fb = FinalBitmap.create(this);
		fb.configLoadingImage(R.drawable.no_image_show);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_all = (TextView) findViewById(R.id.tv_all);
		tv_begin = (TextView) findViewById(R.id.tv_begin);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		btn_back.setOnClickListener(this);

		initView();
		initData();

		final OnlyListViewSwipeGesture touchListener = new OnlyListViewSwipeGesture(
				GroupManList, swipeListener, this);
		touchListener.SwipeType = OnlyListViewSwipeGesture.Double; // 设置两个选项列表项的背景
		GroupManList.setOnTouchListener(touchListener);
		TViewUtil.EmptyViewBuilder.getInstance(StorageSaveActivity.this)
				.setEmptyText(getResources().getString(R.string.nodata))
				.setEmptyTextSize(12).setEmptyTextColor(Color.GRAY)
				.setShowButton(false)
				.setActionText(getResources().getString(R.string.reload))
				.setAction(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Toast.makeText(getApplicationContext(),
						// "Yes, clicked~",Toast.LENGTH_SHORT).show();
						onRefresh();
					}
				}).setShowIcon(false).setShowText(false).bindView(GroupManList);
		btn_back.setOnClickListener(this);
		tv_all.setOnClickListener(this);
		tv_begin.setOnClickListener(this);
		id_tv_edit_all.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		DBOpenHelper dbOpenHelper = new DBOpenHelper(StorageSaveActivity.this,
				DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
		// 执行SQL语句
		Cursor cursor = db.query(DB_TABLE, null, null, null, null, null,
				"_id DESC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(0);
			String img = cursor.getString(1);
			String title = cursor.getString(2);
			String time = cursor.getString(3);
			String money = cursor.getString(4);
			String storage_save_id = cursor.getString(5);
			CollectCar car = new CollectCar(img, title, time, money, id,
					storage_save_id, false, false, "");
			userList.add(car);
			cursor.moveToNext();
		}
		cursor.close();
		if (myadapter == null) {
			myadapter = new Myadapter(StorageSaveActivity.this, userList,
					handler);
			GroupManList.setAdapter(myadapter);
		} else {
			myadapter.notify(userList);
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

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

	OnlyListViewSwipeGesture.TouchCallbacks swipeListener = new OnlyListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void FullSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(StorageSaveActivity.this, "Action_2",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void HalfSwipeListView(int position) {
			// TODO Auto-generated method stub
			db.delete(DB_TABLE, "storage_save_id="
					+ userList.get(position).getStorage_save_id(), null);
			userList.remove(position);
			baseAdapter.notifyDataSetChanged();
		}

		@Override
		public void LoadDataForScroll(int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			// Toast.makeText(StorageSaveActivity.this, "Delete",
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
	private Myadapter myadapter;
	private TextView id_tv_edit_all;

	/*
	 * 初始化View
	 */
	private void initView() {
		GroupManList = (ListView) findViewById(R.id.GroupManList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;

		case R.id.id_tv_edit_all:
			if (userList.size() > 0) {
				for (int i = 0; i < userList.size(); i++) {
					if (Myadapter.getIsSelected().get(i)) {
						Cursor cursor = db.query(DB_TABLE, null,
								"storage_save_id="
										+ userList.get(i).getStorage_save_id(),
								null, null, null, null);
						if (cursor != null && cursor.moveToFirst()) {
							// 已存在，就先删除再存储
							db.delete(DB_TABLE, "storage_save_id="
									+ userList.get(i).getStorage_save_id(),
									null);
						}
					}
				}
				userList.clear();
				myadapter.notify(userList);
				initData();
				flag = false;
				selectedAll();
			}
			break;
		case R.id.tv_all:
			selectedAll();
			flag = !flag;
			break;
		case R.id.tv_begin:
			if (userList.size() == 0) {
				Toast.makeText(StorageSaveActivity.this, "请勾选草稿箱中需要发布的苗木资源",
						Toast.LENGTH_SHORT).show();
				return;
			}
			final ArrayList<CollectCar> new_userList = userList;
			progress = 0;
			// 原数据需要修改
			for (int i = 0; i < new_userList.size(); i++) {
				if (Myadapter.getIsSelected().get(i)) {
					CollectCar collectCar = new_userList.get(i);
					SaveSeedlingMannager mannager = new SaveSeedlingMannager(
							StorageSaveActivity.this, collectCar, i);
					mannager.setOnSaveResultChangeListener(new OnSaveResultChangeListener() {

						@Override
						public void OnSaveResultChange(int postion,
								String reason, String storage_save_id,
								CollectCar collectCar) {
							// TODO Auto-generated method stub
							if ("success".equals(reason)) {
								userList.remove(collectCar);
								// collectCar需要重写hashCode和equals
								myadapter.notify(userList);
							} else {
								CollectCar new_collectCar = collectCar;
								new_collectCar.setReason(reason);
								userList.remove(collectCar);
								userList.add(0, new_collectCar);
								myadapter.notify(userList);
							}
							progress++;
							if (new_userList.size() != 0) {
							}
							if (progress == new_userList.size()) {
							}
						}
					});
					mannager.getPath_Space();

				}
			}
			break;

		default:
			break;
		}
	}

	// 全选或全取消
	private void selectedAll() {
		if (userList.size() > 0) {
			for (int i = 0; i < userList.size(); i++) {
				Myadapter.getIsSelected().put(i, flag);
			}
			myadapter.notify(userList);
		}

	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			onRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onRefresh() {
		userList.clear();
		myadapter.notify(userList);
		initData();
		flag = false;
		selectedAll();
	}

}
