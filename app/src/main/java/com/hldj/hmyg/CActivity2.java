package com.hldj.hmyg;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.adapter.ChooseManagerAdapter;
import com.hldj.hmyg.buy.bean.CollectCar;
import com.hldj.hmyg.saler.bean.ChooseManager;

public class CActivity2 extends Activity {

	ArrayList<ChooseManager> chooseManagers = new ArrayList<ChooseManager>();
	ArrayList<ChooseManager> chooseManagers2 = new ArrayList<ChooseManager>();
	ArrayList<ChooseManager> chooseManagers3= new ArrayList<ChooseManager>();
	private static String DB_NAME = "flower.db";
	private static final String DB_TABLE = "savetable";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase db;
	private ArrayList<CollectCar> userList = new ArrayList<CollectCar>();
	private ChooseManager chooseManager3;
	private GridView gridView1;
	private GridView gridView2;
	private GridView gridView3;
	private ChooseManagerAdapter myadapter;
	private ChooseManagerAdapter myadapter3;
	private ChooseManagerAdapter myadapter2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c2);
		gridView1 = (GridView) findViewById(R.id.gridView1);
		gridView2 = (GridView) findViewById(R.id.gridView2);
		gridView3 = (GridView) findViewById(R.id.gridView3);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		userList.clear();
		DBOpenHelper dbOpenHelper = new DBOpenHelper(CActivity2.this, DB_NAME,
				null, DB_VERSION);
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
		chooseManagers.clear();
		chooseManagers2.clear();
		chooseManagers3.clear();
		ChooseManager chooseManager1 = new ChooseManager("1", "发布苗木", "",
				R.drawable.fabuye_1, false);
		ChooseManager chooseManager2 = new ChooseManager("2", "苗木管理", "",
				R.drawable.fabuye_2, false);
		if (userList.size() > 0) {
			chooseManager3 = new ChooseManager("3", "草稿箱", "",
					R.drawable.caogaoxiang2, true);
		} else {
			chooseManager3 = new ChooseManager("3", "草稿箱", "",
					R.drawable.fabuye_3, false);
		}
		ChooseManager chooseManager4 = new ChooseManager("4", "发布采购", "",
				R.drawable.fabuye_4, false);
		ChooseManager chooseManager5 = new ChooseManager("5", "采购管理", "",
				R.drawable.fabuye_5, false);
		ChooseManager chooseManager6 = new ChooseManager("6", "快速记苗", "",
				R.drawable.fabuyemian_kuaisujimiao, false);
		ChooseManager chooseManager7 = new ChooseManager("7", "记苗本", "",
				R.drawable.fabuyemian_jimiaoben, false);
		ChooseManager chooseManager8 = new ChooseManager("8", "发布行情", "",
				R.drawable.fabuye_fabuhangqing, false);
		ChooseManager chooseManager9 = new ChooseManager("9", "实时行情", "",
				R.drawable.fabuye_shishihangqing, false);
		
		
		
		
		chooseManagers.add(chooseManager1);
		chooseManagers.add(chooseManager2);
		chooseManagers.add(chooseManager3);
		// chooseManagers.add(chooseManager4);
		// chooseManagers.add(chooseManager5);
		chooseManagers.add(chooseManager6);
		chooseManagers.add(chooseManager7);

		if (MyApplication.Userinfo.getBoolean("isLogin", false)) {
			if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
				chooseManagers.add(chooseManager8);
				chooseManagers.add(chooseManager9);
			} else {
			}
		}

		if (chooseManagers.size() > 0) {
			if (myadapter == null) {
				myadapter = new ChooseManagerAdapter(CActivity2.this,
						chooseManagers);
				gridView1.setAdapter(myadapter);
			} else {
				myadapter.notify(chooseManagers);
			}
		}
		
		if (chooseManagers2.size() > 0) {
			if (myadapter2 == null) {
				myadapter2 = new ChooseManagerAdapter(CActivity2.this,
						chooseManagers2);
				gridView2.setAdapter(myadapter2);
			} else {
				myadapter2.notify(chooseManagers2);
			}
		}
		
		if (chooseManagers3.size() > 0) {
			if (myadapter3 == null) {
				myadapter3 = new ChooseManagerAdapter(CActivity2.this,
						chooseManagers3);
				gridView3.setAdapter(myadapter3);
			} else {
				myadapter3.notify(chooseManagers3);
			}
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


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initData();
		super.onResume();
	}

}
