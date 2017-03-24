package com.zzy.flowers.widget.popwin;

import android.content.Context;

import com.hldj.hmyg.StoreActivity;

public class EditP5 extends ChooseTypeBottomPopwin {

	private StoreActivity activity;
	

	public EditP5(Context context, String str, StoreActivity activity) {
		super(context, str);
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void handleClickListener(String str) {
		// TODO Auto-generated method stub
		activity.plantTypes =str;
		activity.Refresh();

	}

}
