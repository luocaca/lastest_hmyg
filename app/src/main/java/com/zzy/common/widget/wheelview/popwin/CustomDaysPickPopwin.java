package com.zzy.common.widget.wheelview.popwin;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.hldj.hmyg.R;
import com.zzy.common.widget.wheelview.StringWheelAdapter;
import com.zzy.common.widget.wheelview.WheelView;

public class CustomDaysPickPopwin extends PopupWindow {

	private String[] dataSource;
	private String[] customDataSource;
	/** 整体view */
	private View popView;
	/** 空白区域 */
	private View araeView;
	/** 取消按钮 */
	private Button cancelBtn;
	/** 确定按钮 */
	private Button confirmBtn;
	/** 自定义按钮 */
	private Button customBtn;
	/** 字符串 */
	private final WheelView wv_string;

	private DayChangeListener dayChangeListener;
	private int pos;
	private int dayType = 0;

	/** 实现点击确认后的时间变化 */
	public interface DayChangeListener {
		void onDayChange(int dayType, int pos);
	}

	public CustomDaysPickPopwin(Context context,
			DayChangeListener dayChangeListener, String[] dataSource, String[] customDataSource, int pos) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.custom_dayspicker_layout, null);
		araeView = (View) popView.findViewById(R.id.pop_arae_layout);
		cancelBtn = (Button) popView.findViewById(R.id.cancel_btn);
		confirmBtn = (Button) popView.findViewById(R.id.confirm_btn);
		customBtn = (Button) popView.findViewById(R.id.custom_btn);
		wv_string = (WheelView) popView.findViewById(R.id.stringwheel);
		this.dayChangeListener = dayChangeListener;
		this.pos = pos;
		this.dataSource = dataSource;
		this.customDataSource = customDataSource;
		initListener(context);
		Init(context, this.dataSource);
		this.setContentView(popView);
		// 设置弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setInputMethodMode(INPUT_METHOD_NOT_NEEDED);
		this.setFocusable(true);
		// this.setAnimationStyle(R.style.PopupAnimation);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);
	}

	private void Init(Context context, String[] dataSource) {
		wv_string.setAdapter(new StringWheelAdapter(dataSource));
		wv_string.setCyclic(false);
		wv_string.setCurrentItem(pos);
		wv_string.setLabel("");// 添加文字
	}

	private void initListener(Context context) {
		BtnOnClickListener clickListener = new BtnOnClickListener(context);
		araeView.setOnClickListener(clickListener);
		cancelBtn.setOnClickListener(clickListener);
		confirmBtn.setOnClickListener(clickListener);
		customBtn.setOnClickListener(clickListener);
	}

	private class BtnOnClickListener implements OnClickListener {

		private Context context;

		public BtnOnClickListener(Context context) {
			super();
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cancel_btn:
			case R.id.pop_arae_layout:
				// TODO 取消
				dismiss();
				break;
			case R.id.confirm_btn:
				// TODO 确定
				if (dayChangeListener != null) {
					dayChangeListener.onDayChange(dayType, wv_string.getCurrentItem());
				}
				dismiss();
				break;
			case R.id.custom_btn:
				dayType = 1;
				customBtn.setEnabled(false);
				Init(context, customDataSource);
				//dismiss();
				break;
			default:
				break;
			}
		}
	}
}
