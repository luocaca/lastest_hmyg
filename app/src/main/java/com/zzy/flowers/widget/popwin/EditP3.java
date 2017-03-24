package com.zzy.flowers.widget.popwin;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hldj.hmyg.FlowerDetailActivity;

public class EditP3 extends ExtensionDataBottomPopwin {

	private FlowerDetailActivity activity;

	public EditP3(Context context, String str, FlowerDetailActivity activity,
			int maxDay) {
		super(context, str, maxDay);
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void handleClickListener(EditText et) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && activity.getCurrentFocus() != null
				&& activity.getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
		}
		String string = et.getText().toString();
		activity.days = string;
		activity.saveExtension();

	}

}
