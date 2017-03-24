package com.zzy.flowers.widget.popwin;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.buyer.AddPurchaseActivity;

public class EditP2 extends EditBottomPopwin {

	private FlowerDetailActivity activity;
	private String str;

	public EditP2(Context context, String str, FlowerDetailActivity activity) {
		super(context, str);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.str = str;
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
		if (!"".equals(string)
				&& Integer.parseInt(string) > Integer.parseInt(str)) {
			Toast.makeText(activity, "超出库存", Toast.LENGTH_SHORT).show();
			return;
			// 收起键盘
		}
		if (!"".equals(string)) {
			activity.et_num = string;
			activity.addCart();
		}

	}

}
