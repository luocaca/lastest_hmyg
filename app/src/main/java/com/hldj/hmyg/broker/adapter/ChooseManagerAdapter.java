package com.hldj.hmyg.broker.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
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
import android.widget.TextView;

import com.hldj.hmyg.LoginActivity;
import com.hldj.hmyg.ManagerListActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.SaveMarketPriceActivity;
import com.hldj.hmyg.broker.SeedlingMarketPyMapActivity;
import com.hldj.hmyg.buyer.ManagerPurchaseActivity;
import com.hldj.hmyg.buyer.SavePurchaseActivity;
import com.hldj.hmyg.jimiao.MiaoNoteListActivity;
import com.hldj.hmyg.jimiao.SaveMiaoActivity;
import com.hldj.hmyg.saler.SaveSeedlingActivity;
import com.hldj.hmyg.saler.StorageSaveActivity;
import com.hldj.hmyg.saler.bean.ChooseManager;
import com.white.utils.AndroidUtil;

public class ChooseManagerAdapter extends BaseAdapter {

	private FinalBitmap fb;
	private ArrayList<ChooseManager> data = null;
	private Context context = null;
	private int dip30px;
	private int width;

	public ChooseManagerAdapter(Context context, ArrayList<ChooseManager> data) {
		this.data = data;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
		dip30px = AndroidUtil.dip2px(context, 30);
		WindowManager wm = ((Activity) context).getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
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
				R.layout.grid_item_choose_manager, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		LayoutParams para = iv_img.getLayoutParams();
		para.width = (width - dip30px) / 5;
		para.height = para.width;
		iv_img.setLayoutParams(para);
		// fb.display(iv_img, data.get(position).getUrlString());
		iv_img.setImageResource(data.get(position).getDraw());
		tv_title.setText(data.get(position).getName());
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
					Intent toLoginActivity = new Intent(context,
							LoginActivity.class);
					((Activity) context).startActivityForResult(
							toLoginActivity, 4);
					return;
				}

				if ("1".equals(data.get(position).getId())) {
					Intent toSalerActivity = new Intent(context,
							SaveSeedlingActivity.class);
					context.startActivity(toSalerActivity);
				} else if ("2".equals(data.get(position).getId())) {
					Intent toManagerListActivity = new Intent(context,
							ManagerListActivity.class);
					context.startActivity(toManagerListActivity);
				} else if ("3".equals(data.get(position).getId())) {
					Intent toStorageSaveActivity = new Intent(context,
							StorageSaveActivity.class);
					context.startActivity(toStorageSaveActivity);
				} else if ("4".equals(data.get(position).getId())) {
					Intent toSavePurchaseActivity = new Intent(context,
							SavePurchaseActivity.class);
					context.startActivity(toSavePurchaseActivity);
				} else if ("5".equals(data.get(position).getId())) {
					Intent toManagerPurchaseActivity = new Intent(context,
							ManagerPurchaseActivity.class);
					context.startActivity(toManagerPurchaseActivity);
				} else if ("6".equals(data.get(position).getId())) {
					Intent toSaveMiaoActivity = new Intent(context,
							SaveMiaoActivity.class);
					context.startActivity(toSaveMiaoActivity);
				} else if ("7".equals(data.get(position).getId())) {
					Intent toMiaoNoteListActivity = new Intent(context,
							MiaoNoteListActivity.class);
					context.startActivity(toMiaoNoteListActivity);
				} else if ("8".equals(data.get(position).getId())) {
					Intent toSaveMarketPriceActivity = new Intent(context,
							SaveMarketPriceActivity.class);
					context.startActivity(toSaveMarketPriceActivity);

				} else if ("9".equals(data.get(position).getId())) {
					// Intent toMarketListActivity = new Intent(context,
					// MarketListActivity.class);
					// context.startActivity(toMarketListActivity);

					Intent toMarketListActivity = new Intent(context,
							SeedlingMarketPyMapActivity.class);
					context.startActivity(toMarketListActivity);
				}

			}
		});

		return inflate;
	}

	public void notify(ArrayList<ChooseManager> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}