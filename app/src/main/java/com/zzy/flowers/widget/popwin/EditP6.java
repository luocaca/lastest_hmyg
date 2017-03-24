package com.zzy.flowers.widget.popwin;

import org.json.JSONArray;

import android.content.Context;

import com.hldj.hmyg.StoreActivity;

public class EditP6 extends ChooseTypeListPopwin {

	private StoreActivity activity;

	public EditP6(Context context, JSONArray typeList, String str,
			StoreActivity activity) {
		super(context, typeList, str);
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void handleClickListener(String str, String str_name) {
		// TODO Auto-generated method stub
		activity.firstSeedlingTypeId = str;
		activity.Refresh();
		activity.tv_type.setText(str_name);

	}

}
