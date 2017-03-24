package com.jerome.weibo;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.hldj.hmyg.ManagerListActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buy.bean.CollectCar;
import com.hldj.hmyg.buyer.ManagerPurchaseActivity;
import com.hldj.hmyg.buyer.SavePurchaseActivity;
import com.hldj.hmyg.saler.SaveSeedlingActivity;
import com.hldj.hmyg.saler.StorageSaveActivity;

public class MoreWindow extends PopupWindow implements OnClickListener {

	private String TAG = MoreWindow.class.getSimpleName();
	Activity mContext;
	private int mWidth;
	private int mHeight;
	private int statusBarHeight;
	private Bitmap mBitmap = null;
	private Bitmap overlay = null;

	private Handler mHandler = new Handler();
	private List<CollectCar> userList = new ArrayList<CollectCar>();
	private static final String DB_NAME = "flower.db";
	private static final String DB_TABLE = "savetable";
	private static final int DB_VERSION = 1;
	private SQLiteDatabase db;
	private Animation operatingAnim;
	private ImageView close;

	public MoreWindow(Activity context) {
		mContext = context;
	}

	public void init() {

		DBOpenHelper dbOpenHelper = new DBOpenHelper(mContext, DB_NAME, null,
				DB_VERSION);
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

		Rect frame = new Rect();
		mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		DisplayMetrics metrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mWidth = metrics.widthPixels;
		mHeight = metrics.heightPixels;

		setWidth(mWidth);
		setHeight(mHeight);
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

	private Bitmap blur() {
		if (null != overlay) {
			return overlay;
		}
		long startMs = System.currentTimeMillis();

		View view = mContext.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache(true);
		mBitmap = view.getDrawingCache();

		float scaleFactor = 8;
		float radius = 10;
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();

		overlay = Bitmap.createBitmap((int) (width / scaleFactor),
				(int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(mBitmap, 0, 0, paint);

		overlay = FastBlur.doBlur(overlay, (int) radius, true);
		Log.i(TAG, "blur time is:" + (System.currentTimeMillis() - startMs));
		return overlay;
	}

	private Animation showAnimation1(final View view, int fromY, int toY) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
		go.setDuration(300);
		TranslateAnimation go1 = new TranslateAnimation(0, 0, -10, 2);
		go1.setDuration(100);
		go1.setStartOffset(250);
		set.addAnimation(go1);
		set.addAnimation(go);

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});
		return set;
	}

	@SuppressWarnings("deprecation")
	public void showMoreWindow(View anchor, int bottomMargin) {
		final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(
				mContext).inflate(R.layout.center_music_more_window, null);
		setContentView(layout);

		close = (ImageView) layout.findViewById(R.id.center_music_window_close);
		operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			close.startAnimation(operatingAnim);
		}
		android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// params.bottomMargin = bottomMargin;
		// params.addRule(RelativeLayout.BELOW, R.id.more_window_auto);
		// params.addRule(RelativeLayout.RIGHT_OF, R.id.more_window_collect);
		// params.topMargin = 200;
		// params.leftMargin = 18;
		// close.setLayoutParams(params);
		TextView more_window_local = (TextView) layout
				.findViewById(R.id.more_window_local);
		TextView more_window_online = (TextView) layout
				.findViewById(R.id.more_window_online);
		TextView more_window_delete = (TextView) layout
				.findViewById(R.id.more_window_delete);
		TextView more_window_collect = (TextView) layout
				.findViewById(R.id.more_window_collect);
		TextView more_window_auto = (TextView) layout
				.findViewById(R.id.more_window_auto);
		TextView more_window_external = (TextView) layout
				.findViewById(R.id.more_window_external);
		if (userList.size() > 0) {
			Drawable drawable = mContext.getResources().getDrawable(
					R.drawable.caogaoxiang2);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			more_window_delete.setCompoundDrawables(null, drawable, null, null);
		}
		more_window_local.setOnClickListener(this);
		more_window_online.setOnClickListener(this);
		more_window_delete.setOnClickListener(this);
		more_window_external.setOnClickListener(this);
		more_window_collect.setOnClickListener(this);
		more_window_auto.setOnClickListener(this);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowing()) {
					if (operatingAnim != null) {
						close.clearAnimation();
						close.startAnimation(operatingAnim);
					}
					closeAnimation(layout);
					dismiss();
				}
			}

		});

		showAnimation(layout);
		// 高斯模糊
		setBackgroundDrawable(new BitmapDrawable(mContext.getResources(),
				blur()));
		setOutsideTouchable(true);
		setFocusable(true);
		showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
	}

	private void showAnimation(ViewGroup layout) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			final View child = layout.getChildAt(i);
			if (child.getId() == R.id.center_music_window_close) {
				continue;
			}
			child.setOnClickListener(this);
			child.setVisibility(View.INVISIBLE);
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					child.setVisibility(View.VISIBLE);
					ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
							"translationY", 600, 0);
					fadeAnim.setDuration(300);
					KickBackAnimator kickAnimator = new KickBackAnimator();
					kickAnimator.setDuration(150);
					fadeAnim.setEvaluator(kickAnimator);
					fadeAnim.start();
				}
			}, i * 50);
		}

	}

	private void closeAnimation(ViewGroup layout) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			final View child = layout.getChildAt(i);
			if (child.getId() == R.id.center_music_window_close) {
				continue;
			}
			child.setOnClickListener(this);
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					child.setVisibility(View.VISIBLE);
					ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child,
							"translationY", 0, 600);
					fadeAnim.setDuration(200);
					KickBackAnimator kickAnimator = new KickBackAnimator();
					kickAnimator.setDuration(100);
					fadeAnim.setEvaluator(kickAnimator);
					fadeAnim.start();
					fadeAnim.addListener(new AnimatorListener() {

						@Override
						public void onAnimationStart(Animator animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animator animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animator animation) {
							child.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onAnimationCancel(Animator animation) {
							// TODO Auto-generated method stub

						}
					});
				}
			}, (layout.getChildCount() - i - 1) * 30);

			if (child.getId() == R.id.more_window_local) {
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						dismiss();
					}
				}, (layout.getChildCount() - i) * 30 + 80);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_window_local:
			Intent toSalerActivity = new Intent(mContext,
					SaveSeedlingActivity.class);
			mContext.startActivity(toSalerActivity);
			mContext.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.more_window_online:
			Intent toManagerListActivity = new Intent(mContext,
					ManagerListActivity.class);
			mContext.startActivity(toManagerListActivity);
			mContext.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.more_window_delete:
			Intent toStorageSaveActivity = new Intent(mContext,
					StorageSaveActivity.class);
			mContext.startActivity(toStorageSaveActivity);
			mContext.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);

			break;
		case R.id.more_window_collect:
			Intent toSavePurchaseActivity = new Intent(mContext,
					SavePurchaseActivity.class);
			mContext.startActivity(toSavePurchaseActivity);
			mContext.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.more_window_auto:
			Intent toManagerPurchaseActivity = new Intent(mContext,
					ManagerPurchaseActivity.class);
			mContext.startActivity(toManagerPurchaseActivity);
			mContext.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.more_window_external:
			break;

		default:
			break;
		}
	}

	public void destroy() {
		if (null != overlay) {
			overlay.recycle();
			overlay = null;
			System.gc();
		}
		if (null != mBitmap) {
			mBitmap.recycle();
			mBitmap = null;
			System.gc();
		}
	}

}
