package com.hldj.hmyg.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.util.D;

import java.util.ArrayList;

public class EditSpinner {
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	// 自定义Adapter
	private OptionsAdapter optionsAdapter = null;
	// 下拉框选项数据源
	private ArrayList<String> datas = new ArrayList<String>();
	// 下拉框依附组件
	private View parent;
	// 下拉框依附组件宽度，也将作为下拉框的宽度
	private int pwidth;
	// 文本框
	private EditText et;
	// 下拉箭头图片组件
	private ImageView image;
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	// 用来处理选中或者删除下拉项消息
	private Handler handler;
	// 是否初始化完成标志
	private boolean flag = false;
	Activity activity;

	TextView mTextView;

	public EditSpinner(Handler handler, Activity activity, View parent, EditText et, View image) {
		this.handler = handler;
		this.activity = activity;
		this.parent = parent;
		this.et = (EditText) et;
		this.image = (ImageView) image;
		initWedget();
		popupWindwShowing();
	}

	public EditSpinner(Handler handler, Activity activity, View parent, TextView mTextView) {
		this.handler = handler;
		this.activity = activity;
		this.parent = parent;
		this.mTextView = mTextView;
		initWedget1();
		popupWindwShowing();
	}

	/**
	 * 初始化界面控件
	 */
	Context context;

	private void initWedget1() {

		datas.clear();
		datas.add("问题决解才上门收费");
		datas.add("问题未决解也要收费");

		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;

		// 初始化PopupWindow
		initPopuWindow();

		// // 设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		// mTextView.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// DebugLog.e("==========1===========" + selectPopupWindow.isShowing());
		//
		// // if (!selectPopupWindow.isShowing()) {
		// // 显示PopupWindow窗口
		// // image.setImageResource(R.drawable.report_location_red_down);
		// DebugLog.e("==========2===========");
		// mTextView.setSelected(true);
		// //
		// image.setBackground(context.getDrawable(R.drawable.report_location_red_upward));
		// popupWindwShowing();
		// // } else {
		// // mTextView.setSelected(false);
		// // //
		// // image.setImageResource(R.drawable.report_location_red_down);
		// // //
		// //
		// image.setBackground(context.getDrawable(R.drawable.report_location_red_down));
		// // //
		// // image.setImageResource(R.drawable.report_location_red_upward);
		// // DebugLog.e("==========3===========");
		// // dismiss();
		// // }
		// }
		// });

	}

	private void initWedget() {

		datas.clear();
		datas.add("问题决解才上门收费");
		datas.add("问题未决解也要收费");

		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;

		// 设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				D.e("==========1===========" + selectPopupWindow.isShowing());

				if (!selectPopupWindow.isShowing()) {
					// 显示PopupWindow窗口
					// image.setImageResource(R.drawable.report_location_red_down);
					D.e("==========2===========");
					image.setSelected(true);
					// image.setBackground(context.getDrawable(R.drawable.report_location_red_upward));
					popupWindwShowing();
				} else {
					// image.setImageResource(R.drawable.report_location_red_down);
					// image.setBackground(context.getDrawable(R.drawable.report_location_red_down));
					// image.setImageResource(R.drawable.report_location_red_upward);
					D.e("==========3===========");
					dismiss();
				}
			}
		});

		// 初始化PopupWindow
		initPopuWindow();

	}

	/**
	 * 初始化填充Adapter所用List数据
	 */
	private void initDatas() {

		datas.clear();

	}

	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {

		// initDatas();

		// PopupWindow浮动下拉框布局
		View loginwindow = (View) activity.getLayoutInflater().inflate(R.layout.popo_shop_type_list, null);
		listView = (ListView) loginwindow.findViewById(R.id.listview);

		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(activity, handler, datas);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth, LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);

		selectPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

				D.e("=====selectPopupWindow=====onDismiss===========");
				if (mTextView != null) {
					mTextView.setSelected(false);
				}

			}
		});

		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		// 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param
	 */ 
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
		selectPopupWindow.showAsDropDown(parent, 0, -3);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		D.e("========dismiss=========");

		if (mTextView != null) {
			mTextView.setSelected(false);
		}

		selectPopupWindow.dismiss();
	}

	class OptionsAdapter extends BaseAdapter {

		private ArrayList<String> list = new ArrayList<String>();
		private Activity activity = null;
		private Handler handler;

		/**
		 * 自定义构造方法
		 * 
		 * @param activity
		 * @param handler
		 * @param list
		 */
		public OptionsAdapter(Activity activity, Handler handler, ArrayList<String> list) {
			this.activity = activity;
			this.handler = handler;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// 下拉项布局

				convertView = LayoutInflater.from(activity).inflate(R.layout.list_item_sort, null);

				holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
				holder.is_check = (ImageView) convertView.findViewById(R.id.is_check);
				// holder.imageView = (ImageView)
				// convertView.findViewById(R.id.delImage);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_item.setText(list.get(position));




			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_item;
		ImageView is_check;
	}

}
