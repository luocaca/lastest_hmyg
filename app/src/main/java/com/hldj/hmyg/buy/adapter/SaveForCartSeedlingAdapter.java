package com.hldj.hmyg.buy.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.buy.bean.SaveFortCartSeedling;

/**
 * 
 * 
 * @version 1.0.0
 * 
 */
@SuppressLint("ResourceAsColor")
public class SaveForCartSeedlingAdapter extends BaseAdapter {
	private static final String TAG = "SaveForCartSeedlingAdapter";

	private ArrayList<SaveFortCartSeedling> data = null;
	private Context context = null;
	private FinalBitmap fb;
	private LayoutParams l_params;

	public SaveForCartSeedlingAdapter(Context context,
			ArrayList<SaveFortCartSeedling> data) {
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
				R.layout.list_item_save_for_cart_seedling, null);
		ImageView sc_ziying = (ImageView) inflate.findViewById(R.id.sc_ziying);

		ImageView sc_fuwufugai = (ImageView) inflate
				.findViewById(R.id.sc_fuwufugai);
		ImageView sc_hezuoshangjia = (ImageView) inflate
				.findViewById(R.id.sc_hezuoshangjia);
		ImageView sc_huodaofukuan = (ImageView) inflate
				.findViewById(R.id.sc_huodaofukuan);

		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		ImageView iv_status_01 = (ImageView) inflate
				.findViewById(R.id.iv_status_01);
		ImageView iv_status_02 = (ImageView) inflate
				.findViewById(R.id.iv_status_02);
		if (data.get(position).getTagList().contains(Data.ZIYING)) {
			sc_ziying.setVisibility(View.VISIBLE);
		}
		if (data.get(position).getTagList().toString().contains(Data.FUWU)) {
			sc_fuwufugai.setVisibility(View.VISIBLE);
		}
		if (data.get(position).getTagList().toString()
				.contains(Data.HEZUOSHANGJIA)) {
			sc_hezuoshangjia.setVisibility(View.VISIBLE);
		}
		if (data.get(position).getTagList().toString()
				.contains(Data.ZIJINDANBAO)) {
			sc_huodaofukuan.setVisibility(View.VISIBLE);
		}

		tv_05.setText("品种：" + data.get(position).getName().toString());
		tv_01.setText(data.get(position).getPrice() + "元/"
				+ data.get(position).getUnitTypeName());
		tv_02.setText(data.get(position).getCount() + "");
		tv_03.setText(data.get(position).getDeliveryPrice() + "");
		tv_04.setText(data.get(position).getTotalPrice() + "");

		if ("fangxin".equals(data.get(position).getTradeType())) {
			iv_status_01.setImageResource(R.drawable.fangxingou_green);
			iv_status_02.setImageResource(R.drawable.zijindanbao_no);
		} else if ("danbao".equals(data.get(position).getTradeType())) {
			iv_status_01.setImageResource(R.drawable.fangxingou_no);
			iv_status_02.setImageResource(R.drawable.zijindanbao_green);
		}else {
			iv_status_01.setImageResource(R.drawable.weituofahuo_green);
			iv_status_02.setImageResource(R.drawable.zijindanbao_no);
		}
		
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return inflate;
	}

	public void notify(ArrayList<SaveFortCartSeedling> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
