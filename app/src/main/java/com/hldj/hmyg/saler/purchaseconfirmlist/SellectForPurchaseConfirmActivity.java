package com.hldj.hmyg.saler.purchaseconfirmlist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.ListWheelAdapter;
import me.drakeet.materialdialog.MaterialDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.BActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.store.TypeEx;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.white.utils.DateTimePickDialogUtil;

public class SellectForPurchaseConfirmActivity extends BaseSecondActivity
		implements OnWheelChangedListener {
	MaterialDialog mMaterialDialog;
	private EditText tv_startDate;
	private EditText tv_endDate;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private int type = 0;
	SellectForPurchase sPurchase = new SellectForPurchase("", "", 0, 0);
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellect_for_purchase_confirm);
		mMaterialDialog = new MaterialDialog(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		bundle = getIntent().getExtras();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		LinearLayout ll_startDate = (LinearLayout) findViewById(R.id.ll_startDate);
		LinearLayout ll_endDate = (LinearLayout) findViewById(R.id.ll_endDate);
		tv_startDate = (EditText) findViewById(R.id.tv_startDate);
		tv_endDate = (EditText) findViewById(R.id.tv_endDate);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView iv_reset = (TextView) findViewById(R.id.iv_reset);
		TextView sure = (TextView) findViewById(R.id.sure);
		if (bundle.get("SellectForPurchase") != null) {
			sPurchase = (SellectForPurchase) bundle.get("SellectForPurchase");
			tv_startDate.setText(sPurchase.getStr_startDate());
			tv_endDate.setText(sPurchase.getStr_endDate());
		}

		btn_back.setOnClickListener(multipleClickProcess);
		iv_reset.setOnClickListener(multipleClickProcess);
		sure.setOnClickListener(multipleClickProcess);
		ll_startDate.setOnClickListener(multipleClickProcess);
		ll_endDate.setOnClickListener(multipleClickProcess);
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
				case R.id.ll_startDate:
					// DateTimePickDialogUtil dateTimePicKDialog = new
					// DateTimePickDialogUtil(
					// SellectForPurchaseConfirmActivity.this, "");
					// dateTimePicKDialog.dateTimePicKDialog(tv_startDate);
					type = 1;
					showTimes();
					break;
				case R.id.ll_endDate:
					type = 2;
					showTimes();
					break;
				case R.id.iv_reset:
					sPurchase = new SellectForPurchase("", "", 0, 0);
					tv_startDate.setText(sPurchase.getStr_startDate());
					tv_endDate.setText(sPurchase.getStr_endDate());
					setResultTofinish();
					break;
				case R.id.sure:
					setResultTofinish();
					break;

				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		private void setResultTofinish() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("SellectForPurchase", sPurchase);
			intent.putExtras(mBundle);
			setResult(13, intent);
			finish();
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

	/**
	 * 年
	 */
	protected List<String> year = new ArrayList<String>();
	/**
	 * 月
	 */
	protected String[] moth = new String[] { "1月", "2月", "3月", "4月", "5月",
			"6月", "7月", "8月", "9月", "10月", "11月", "12月" };
	/**
	 * 日
	 */
	protected List<String> day;
	String year_a;// 每次更新时存储到这个里面
	String moth_a;
	String day_a;
	String year_b;// 这个只记录第一次获取的时间
	String moth_b;
	String day_b;

	private void showTimes() {
		Calendar calendar = Calendar.getInstance();
		year_a = year_b = calendar.get(Calendar.YEAR) + "";// 获取当前年
		moth_a = moth_b = calendar.get(Calendar.MONTH) + 1 + "";// 获取当前月
		day_a = day_b = calendar.get(Calendar.DAY_OF_MONTH) + "";// 获取当前日期

		getyear();

		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_city, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		mViewProvince = (WheelView) dia_choose_share
				.findViewById(R.id.id_province);
		mViewCity = (WheelView) dia_choose_share.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) dia_choose_share
				.findViewById(R.id.id_district);
		mViewProvince.setCyclic(true);// 设置循环
		mViewCity.setCyclic(true);
		mViewDistrict.setCyclic(true);
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
				showSelectedResult();
				if (!SellectForPurchaseConfirmActivity.this.isFinishing()
						&& dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});
		setTime();

		if (!SellectForPurchaseConfirmActivity.this.isFinishing()
				&& dialog.isShowing()) {
			dialog.cancel();
		} else if (!SellectForPurchaseConfirmActivity.this.isFinishing()
				&& dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * 将当前的时间添加进时间选择框
	 */
	private void setTime() {
		int yy = 0;
		int mm = 0;
		int dd = 0;
		for (int i = 0; i < year.size(); i++) {
			if (year.get(i).equals(year_b + "年")) {
				yy = i;
				System.out.println("yy  " + yy);
			}
		}
		for (int i = 0; i < moth.length; i++) {
			if (moth[i].equals(moth_b + "月")) {
				mm = i;
				System.out.println("mm  " + mm);
			}
		}
		for (int i = 0; i < day.size(); i++) {
			if (day.get(i).equals(day_b + "日")) {
				dd = i;
				System.out.println("dd  " + dd);
			}
		}
		mViewProvince.setCurrentItem(yy);
		mViewCity.setCurrentItem(mm);
		mViewDistrict.setCurrentItem(dd);
	}

	/**
	 * 获取上下两百年的年份信息
	 */
	private void getyear() {
		int aa = Integer.parseInt(year_a) - 100;
		for (int i = 0; i < 100; i++) {
			year.add((aa + i) + "" + "年");
		}
		for (int i = 0; i < 100; i++) {
			year.add((Integer.parseInt(year_a) + i) + "" + "年");
		}
	}

	private void setUpData() {
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		upyear();
		upmoth();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {// 滑动年滚轮的事件
			upyear();
		} else if (wheel == mViewCity) {// 滑动月滚轮的事件
			upmoth();
		} else if (wheel == mViewDistrict) {// 滑动日滚轮的事件
			upday();
		}
	}

	private void upyear() {// 将年份的数据添加进滚轮中
		mViewProvince.setViewAdapter(new ListWheelAdapter<String>(
				SellectForPurchaseConfirmActivity.this, year));
		try {
			int pCurrent = mViewProvince.getCurrentItem();
			// 将当前的年赋值给全局
			year_a = year.get(pCurrent);
			// 因为在第一次进入的时候是还没有月份的数据的，所以需要排除掉
			if (moth_a != null) {
				getday(Integer.parseInt(year_a.replace("年", "")),
						Integer.parseInt(moth_a.replace("月", "")));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void upmoth() {
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(
				SellectForPurchaseConfirmActivity.this, moth));
		try {
			int pCurrent = mViewCity.getCurrentItem();
			// 将当前的月赋值给全局
			moth_a = moth[pCurrent];
			getday(Integer.parseInt(year_a.replace("年", "")),
					Integer.parseInt(moth_a.replace("月", "")));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void upday() {

		try {
			int pCurrent = mViewDistrict.getCurrentItem();
			day_a = day.get(pCurrent);

		} catch (Exception e) {
			mViewDistrict.setCurrentItem(day.size() - 1);
			day_a = day.get(day.size() - 1);
		}
	}

	private void showSelectedResult() {

		if (type == 1) {
			if (sPurchase.getL_endDate() > 0
					&& Long.parseLong((year_a.replace("年", "")
							+ moth_a.replace("月", "") + day_a.replace("日", ""))) >= sPurchase
								.getL_endDate()) {
				Toast.makeText(SellectForPurchaseConfirmActivity.this,
						"结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
			} else {
				sPurchase.setStr_startDate((year_a.replace("年", "-")
						+ moth_a.replace("月", "-") + day_a.replace("日", "")));
				sPurchase.setL_startDate(Long.parseLong((year_a
						.replace("年", "") + moth_a.replace("月", "") + day_a
						.replace("日", ""))));
				tv_startDate.setText((year_a.replace("年", "-")
						+ moth_a.replace("月", "-") + day_a.replace("日", "")));
			}

		} else if (type == 2) {

			if (sPurchase.getL_startDate() > 0
					&& sPurchase.getL_startDate() >= Long.parseLong((year_a
							.replace("年", "") + moth_a.replace("月", "") + day_a
							.replace("日", "")))) {
				Toast.makeText(SellectForPurchaseConfirmActivity.this,
						"结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
			} else {
				sPurchase.setStr_endDate((year_a.replace("年", "-")
						+ moth_a.replace("月", "-") + day_a.replace("日", "")));
				sPurchase.setL_endDate(Long.parseLong((year_a.replace("年", "")
						+ moth_a.replace("月", "") + day_a.replace("日", ""))));
				tv_endDate.setText((year_a.replace("年", "-")
						+ moth_a.replace("月", "-") + day_a.replace("日", "")));
			}

		}

		// Toast.makeText(SellectForPurchaseConfirmActivity.this, "当前选中:" +
		// year_a + moth_a + day_a,
		// Toast.LENGTH_SHORT).show();
	}

	/**
	 * 根据当前的年月来获取日的信息
	 * 
	 * @param year
	 * @param month
	 */
	private void getday(int year, int month) {
		day = new ArrayList<String>();
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month);
		a.set(Calendar.DATE, 1);
		a.set(Calendar.DATE, -1);
		int maxdate = a.get(Calendar.DATE);
		for (int i = 1; i <= maxdate + 1; i++) {
			day.add(i + "日");
		}
		mViewDistrict.setViewAdapter(new ListWheelAdapter<String>(
				SellectForPurchaseConfirmActivity.this, day));
		upday();
	}
}
