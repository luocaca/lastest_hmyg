package com.hldj.hmyg.saler;

import android.content.Context;

import com.hldj.hmyg.R;
import com.hldj.hmyg.jimiao.SaveMiaoActivity;
import com.zzy.flowers.widget.popwin.BottomPopwin;

public class FlowerInfoPhotoChoosePopwin3 extends BottomPopwin {

	private SaveMiaoActivity upimg;

	public FlowerInfoPhotoChoosePopwin3(Context context,
			SaveMiaoActivity upimg) {
		super(new int[] { R.string.photo_take_pic, R.string.photo_album_title,
				R.string.cancel }, upimg);
		this.upimg = upimg;

	}

	@Override
	protected void handleClickListener(int pos) {
		switch (pos) {
		case 0:
			upimg.toTakePic();
			break;
		case 1:
			upimg.toChoosePic();
			break;
		default:
			break;
		}
	}

}
