package com.hldj.hmyg.buy.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.bingoogolapple.badgeview.BGABadgeViewHelper.BadgeGravity;

import com.hldj.hmyg.R;
import com.hldj.hmyg.buy.bean.ManagerPurchase;
import com.hldj.hmyg.buyer.ManagerPurchaseListActivity;

/**
 * 
 * 
 * @version 1.0.0
 * 
 */
@SuppressLint("ResourceAsColor")
public class ManagerPurchaseAdapter extends BaseAdapter {
	private static final String TAG = "ManagerPurchase";

	private ArrayList<ManagerPurchase> data = null;
	private Context context = null;
	private FinalBitmap fb;
	private LayoutParams l_params;

	public ManagerPurchaseAdapter(Context context,
			ArrayList<ManagerPurchase> data) {
		this.data = data;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		l_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		WindowManager wm = ((Activity) context).getWindowManager();
		l_params.height = (int) (wm.getDefaultDisplay().getWidth() * 1 / 3);
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View inflate = LayoutInflater.from(context).inflate(
				R.layout.grid_item_manager_purchase, null);
		BGABadgeLinearLayout ll_daifahuo_00 = (BGABadgeLinearLayout) inflate
				.findViewById(R.id.ll_daifahuo_00);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		// iv_img.setLayoutParams(l_params);
		iv_img.setImageResource(data.get(position).getDrawable());
		tv_title.setText(data.get(position).getName());
		if (!"".equals(data.get(position).getId())
				&& !"0".equals(data.get(position).getId())) {
			ll_daifahuo_00.showTextBadge(data.get(position).getId());
			ll_daifahuo_00.getBadgeViewHelper().setBadgeGravity(
					BadgeGravity.RightTop);
		}

		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toBActivity = new Intent(context,
						ManagerPurchaseListActivity.class);
				toBActivity.putExtra("name", data.get(position).getName());
				toBActivity.putExtra("status", data.get(position).getStatus());
				context.startActivity(toBActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});

		return inflate;
	}

	public void notify(ArrayList<ManagerPurchase> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
