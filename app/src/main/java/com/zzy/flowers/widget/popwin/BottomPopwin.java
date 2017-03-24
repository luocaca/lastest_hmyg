package com.zzy.flowers.widget.popwin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hldj.hmyg.R;
import com.white.utils.AndroidUtil;

public abstract class BottomPopwin extends PopupWindow {

	// private BaseAdapter adapter;
	protected static int DISTANCE_3_DIP = 0;

	/** 显示的列表文本 */
	protected String[] texts;

	protected LayoutInflater inflater;

	protected Context context;

	protected View mainView;

	protected abstract void handleClickListener(int pos);

	public BottomPopwin(int[] textIds, Context context) {
		super(context);
		setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		texts = new String[textIds.length];
		for (int i = 0; i < texts.length; i++) {
			int textId = textIds[i];
			texts[i] = context.getResources().getString(textId);
		}
		this.context = context;
		init();
	}

	public BottomPopwin(String[] texts, Context context) {
		super(context);
		setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		this.texts = texts;
		this.context = context;
		init();
	}

	protected void init() {
		if (DISTANCE_3_DIP == 0) {
			DISTANCE_3_DIP = AndroidUtil.dip2px(context, 3);
		}
		inflater = LayoutInflater.from(context);
		mainView = inflater.inflate(R.layout.bottom_popwin, null);
		setContentView(mainView);
		ListView listView = (ListView) mainView.findViewById(R.id.bpContentLv);
		listView.setAdapter(new ItemAdapter());
		initPopwin();
	}

	protected void initPopwin() {
		mainView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BottomPopwin.this.dismiss();
				return true;
			}
		});
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
	}

	/**
	 * 弹出窗列表适配器
	 * 
	 * @author tian
	 * 
	 */
	private class ItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return texts.length;
		}

		@Override
		public Object getItem(int position) {
			return texts[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Holder();
				convertView = inflater.inflate(R.layout.bottom_popwin_item,
						null);
				holder.itemBtn = (Button) convertView
						.findViewById(R.id.bpItemBtn);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.itemBtn.setText(texts[position]);
			if (position == (texts.length - 1)) {
				// 取消按钮样式
				holder.itemBtn
						.setBackgroundResource(R.drawable.cancel_btn_selector);
				convertView.setPadding(0, DISTANCE_3_DIP, 0, 0);
			} else {
				holder.itemBtn
						.setBackgroundResource(R.drawable.bottom_popwin_btn_selector);
				convertView.setPadding(0, 0, 0, 0);
			}
			holder.itemBtn.setOnClickListener(new ClickListener(position));
			return convertView;
		}

		private class ClickListener implements OnClickListener {

			private int position;

			public ClickListener(int position) {
				this.position = position;
			}

			@Override
			public void onClick(View v) {
				handleClickListener(position);
				dismiss();
			}
		}

		private Holder holder;

		private class Holder {
			private Button itemBtn;
		}

	}

}
