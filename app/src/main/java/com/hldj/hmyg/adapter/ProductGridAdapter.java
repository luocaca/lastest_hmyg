package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hy.utils.ValueGetInfo;

@SuppressLint("ResourceAsColor")
public class ProductGridAdapter extends BaseAdapter {
	private static final String TAG = "ProductGridAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public ProductGridAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		this.context = context;
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.no_image_show);
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
				R.layout.grid_view_seedling, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		TextView tv_status_01 = (TextView) inflate
				.findViewById(R.id.tv_status_01);
		TextView tv_status_02 = (TextView) inflate
				.findViewById(R.id.tv_status_02);
		TextView tv_status_03 = (TextView) inflate
				.findViewById(R.id.tv_status_03);
		TextView tv_status_04 = (TextView) inflate
				.findViewById(R.id.tv_status_04);
		TextView tv_status_05 = (TextView) inflate
				.findViewById(R.id.tv_status_05);

		ImageView sc_ziying = (ImageView) inflate.findViewById(R.id.sc_ziying);
		ImageView sc_fuwufugai = (ImageView) inflate
				.findViewById(R.id.sc_fuwufugai);
		ImageView sc_hezuoshangjia = (ImageView) inflate
				.findViewById(R.id.sc_hezuoshangjia);
		ImageView sc_huodaofukuan = (ImageView) inflate
				.findViewById(R.id.sc_huodaofukuan);
		if (data.get(position).get("tagList").toString().contains(Data.ZIYING)) {
			sc_ziying.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tagList").toString().contains(Data.FUWU)) {
			sc_fuwufugai.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tagList").toString()
				.contains(Data.HEZUOSHANGJIA)) {
			sc_hezuoshangjia.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tagList").toString()
				.contains(Data.ZIJINDANBAO)) {
			sc_huodaofukuan.setVisibility(View.VISIBLE);
		}

		if (data.get(position).get("isSelfSupport").toString().contains("true")) {
			tv_status_01.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeValidatePrice").toString()
				.contains("true")) {
			tv_status_02.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("cashOnDelivery").toString()
				.contains("true")) {
			tv_status_03.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeDeliveryPrice").toString()
				.contains("true")) {
			tv_status_04.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeValidate").toString().contains("true")) {
			tv_status_05.setVisibility(View.VISIBLE);
		}

		if (data.get(position).get("plantType").toString().contains("planted")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (data.get(position).get("plantType").toString()
				.contains("transplant")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (data.get(position).get("plantType").toString()
				.contains("heelin")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (data.get(position).get("plantType").toString()
				.contains("container")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_rong);
		} else {
			tv_01.setVisibility(View.GONE);
		}
		tv_02.setText(data.get(position).get("name").toString());
		if ("unaudit".equals(data.get(position).get("status").toString())) {
			tv_03.setTextColor(Color.parseColor("#6cd8b0"));
		} else if ("published".equals(data.get(position).get("status")
				.toString())) {
			tv_03.setTextColor(Color.parseColor("#fa7600"));
		} else if ("outline"
				.equals(data.get(position).get("status").toString())) {
			tv_03.setTextColor(Color.parseColor("#93c5fc"));
		} else if ("backed".equals(data.get(position).get("status").toString())) {
			tv_03.setTextColor(Color.parseColor("#b8d661"));
		} else if ("unsubmit".equals(data.get(position).get("status")
				.toString())) {
			tv_03.setTextColor(Color.parseColor("#eb8ead"));
		}
		// tv_03.setText(data.get(position).get("statusName").toString());
		tv_07.setText(ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("price").toString())));
		tv_08.setText("元/" + data.get(position).get("unitTypeName").toString());
		// tv_09.setText(data.get(position).get("count").toString()
		// + data.get(position).get("unitTypeName").toString());
		tv_09.setText(data.get(position).get("fullName").toString());
		fb.display(iv_img, data.get(position).get("imageUrl").toString());
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toFlowerDetailActivity = new Intent(context,
						FlowerDetailActivity.class);
				toFlowerDetailActivity.putExtra("id",
						data.get(position).get("id").toString());
				toFlowerDetailActivity.putExtra("show_type", data.get(position)
						.get("show_type").toString());
				context.startActivity(toFlowerDetailActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}
}
