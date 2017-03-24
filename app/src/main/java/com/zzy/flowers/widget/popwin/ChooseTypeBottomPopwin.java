package com.zzy.flowers.widget.popwin;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hy.utils.JsonGetInfo;
import com.white.utils.AndroidUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

@SuppressLint("CutPasteId")
public abstract class ChooseTypeBottomPopwin extends PopupWindow {

	// private BaseAdapter adapter;
	protected static int DISTANCE_3_DIP = 0;
	/** 显示的列表文本 */

	protected LayoutInflater inflater;

	protected Context context;
	protected String str;

	protected View mainView;
	private TextView tv_type01;
	private TextView tv_type02;
	private TextView tv_type03;
	private TextView tv_type04;
	private TextView iv_reset;
	private TextView sure;
	private static String type01 = ""; // planted,
	private String type02 = ""; // transplant,
	private String type03 = ""; // heelin,
	private String type04 = ""; // container,
	private ArrayList<String> planttype_names = new ArrayList<String>();
	private ArrayList<String> planttype_ids = new ArrayList<String>();
	private TagFlowLayout mFlowLayout3;
	private TagAdapter adapter3;

	protected abstract void handleClickListener(String str);

	public ChooseTypeBottomPopwin(Context context, String str) {
		super(context);
		setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		this.context = context;
		this.str = str;
		init();
	}

	protected void init() {
		if (DISTANCE_3_DIP == 0) {
			DISTANCE_3_DIP = AndroidUtil.dip2px(context, 3);
		}
		planttype_names.add("地栽苗");
		planttype_ids.add("planted");
		planttype_names.add("移植苗");
		planttype_ids.add("transplant");
		planttype_names.add("假植苗");
		planttype_ids.add("heelin");
		planttype_names.add("容器苗");
		planttype_ids.add("container");
		inflater = LayoutInflater.from(context);
		mainView = inflater.inflate(R.layout.bottom_choose_type, null);
		mFlowLayout3 = (TagFlowLayout) mainView
				.findViewById(R.id.id_flowlayout3);
		tv_type01 = (TextView) mainView.findViewById(R.id.tv_type01);
		tv_type02 = (TextView) mainView.findViewById(R.id.tv_type02);
		tv_type03 = (TextView) mainView.findViewById(R.id.tv_type03);
		tv_type04 = (TextView) mainView.findViewById(R.id.tv_type04);
		iv_reset = (TextView) mainView.findViewById(R.id.iv_reset);
		sure = (TextView) mainView.findViewById(R.id.sure);
		if (str.contains("planted")) {
			type01 = "planted,";
			tv_type01.setTextColor(context.getResources().getColor(
					R.color.main_color));
			tv_type01.setBackgroundResource(R.drawable.search_edit_selector);
		}
		if (str.contains("transplant")) {
			type02 = "transplant,";
			tv_type02.setTextColor(context.getResources().getColor(
					R.color.main_color));
			tv_type02.setBackgroundResource(R.drawable.search_edit_selector);
		}
		if (str.contains("heelin")) {
			type03 = "heelin,";
			tv_type03.setTextColor(context.getResources().getColor(
					R.color.main_color));
			tv_type03.setBackgroundResource(R.drawable.search_edit_selector);
		}
		if (str.contains("container")) {
			type04 = "container,";
			tv_type04.setTextColor(context.getResources().getColor(
					R.color.main_color));
			tv_type04.setBackgroundResource(R.drawable.search_edit_selector);
		}
		if (planttype_names.size() > 0) {

			adapter3 = new com.zhy.view.flowlayout.TagAdapter<String>(
					planttype_names) {

				@Override
				public View getView(FlowLayout parent, int position, String s) {
					TextView tv = (TextView) ((Activity) context)
							.getLayoutInflater().inflate(R.layout.tv,
									mFlowLayout3, false);
					tv.setText(s);
					return tv;
				}
			};
			mFlowLayout3.setAdapter(adapter3);
			mFlowLayout3
					.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
						@Override
						public boolean onTagClick(View view, int position,
								FlowLayout parent) {
							
							if(position==0){
								type01 = "planted,";
							}else if (position==1) {
								type01 = "transplant,";
							}else if (position==2) {
								type01 = "heelin,";
							}else if (position==3) {
								type01 = "container,";
							}
							return true;
						}
					});
			
			
			if (str.contains("planted")) {
				adapter3.setSelectedList(0);
			}
			if (str.contains("transplant")) {
				adapter3.setSelectedList(1);
			}
			if (str.contains("heelin")) {
				adapter3.setSelectedList(2);
			}
			if (str.contains("container")) {
				adapter3.setSelectedList(3);
			}
		}
		sure.setOnClickListener(new myOnclick());
		iv_reset.setOnClickListener(new myOnclick());
		tv_type01.setOnClickListener(new myOnclick());
		tv_type02.setOnClickListener(new myOnclick());
		tv_type03.setOnClickListener(new myOnclick());
		tv_type04.setOnClickListener(new myOnclick());
		setContentView(mainView);
		initPopwin();
	}

	class myOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_reset:
				type01 = "";
				type02 = "";
				type03 = "";
				type04 = "";
				tv_type01.setTextColor(context.getResources().getColor(
						R.color.gray));
				tv_type01
						.setBackgroundResource(R.drawable.sellect_edit_selector);
				tv_type02.setTextColor(context.getResources().getColor(
						R.color.gray));
				tv_type02
						.setBackgroundResource(R.drawable.sellect_edit_selector);
				tv_type03.setTextColor(context.getResources().getColor(
						R.color.gray));
				tv_type03
						.setBackgroundResource(R.drawable.sellect_edit_selector);
				tv_type04.setTextColor(context.getResources().getColor(
						R.color.gray));
				tv_type04
						.setBackgroundResource(R.drawable.sellect_edit_selector);
				str = type01 + type02 + type03 + type04;
				handleClickListener(str);
				dismiss();
				break;
			case R.id.sure:
				str = type01 + type02 + type03 + type04;
				handleClickListener(str);
				dismiss();
				break;
			case R.id.tv_type01:
				if ("".equals(type01)) {
					type01 = "planted,";
					tv_type01.setTextColor(context.getResources().getColor(
							R.color.main_color));
					tv_type01
							.setBackgroundResource(R.drawable.search_edit_selector);
				} else {
					type01 = "";
					tv_type01.setTextColor(context.getResources().getColor(
							R.color.gray));
					tv_type01
							.setBackgroundResource(R.drawable.sellect_edit_selector);
				}
				break;

			case R.id.tv_type02:
				if ("".equals(type02)) {
					type02 = "transplant,";
					tv_type02.setTextColor(context.getResources().getColor(
							R.color.main_color));
					tv_type02
							.setBackgroundResource(R.drawable.search_edit_selector);
				} else {
					type02 = "";
					tv_type02.setTextColor(context.getResources().getColor(
							R.color.gray));
					tv_type02
							.setBackgroundResource(R.drawable.sellect_edit_selector);

				}
				break;
			case R.id.tv_type03:
				if ("".equals(type03)) {
					type03 = "heelin,";
					tv_type03.setTextColor(context.getResources().getColor(
							R.color.main_color));
					tv_type03
							.setBackgroundResource(R.drawable.search_edit_selector);
				} else {
					type03 = "";
					tv_type03.setTextColor(context.getResources().getColor(
							R.color.gray));
					tv_type03
							.setBackgroundResource(R.drawable.sellect_edit_selector);

				}
				break;
			case R.id.tv_type04:
				if ("".equals(type04)) {
					type04 = "container,";
					tv_type04.setTextColor(context.getResources().getColor(
							R.color.main_color));
					tv_type04
							.setBackgroundResource(R.drawable.search_edit_selector);
				} else {
					type04 = "";
					tv_type04.setTextColor(context.getResources().getColor(
							R.color.gray));
					tv_type04
							.setBackgroundResource(R.drawable.sellect_edit_selector);
				}
				break;

			default:
				break;
			}
		}

	}

	protected void initPopwin() {
		mainView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ChooseTypeBottomPopwin.this.dismiss();
				return true;
			}
		});
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
	}

}
