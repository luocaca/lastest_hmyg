package com.hldj.hmyg.broker.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.broker.MarketDetailActivity;
import com.hldj.hmyg.broker.MyMarketListActivity;
import com.hldj.hmyg.broker.bean.MarketPrice;
import com.hy.utils.ValueGetInfo;

/**
 * 
 * 
 * 
 */
@SuppressLint("ResourceAsColor")
public class MyMarketListAdapter extends BaseAdapter {
	private static final String TAG = "ProductListAdapter";

	private ArrayList<MarketPrice> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private ImageView iv_like;

	public MyMarketListAdapter(Context context, ArrayList<MarketPrice> data) {
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
				R.layout.list_item_market_price_my, null);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_10 = (TextView) inflate.findViewById(R.id.tv_10);
		TextView tv_11 = (TextView) inflate.findViewById(R.id.tv_11);

		if (data.get(position).getPlantType().toString().contains("planted")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (data.get(position).getPlantType().toString()
				.contains("transplant")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (data.get(position).getPlantType().toString()
				.contains("heelin")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (data.get(position).getPlantType().toString()
				.contains("container")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_rong);
		} else {
			tv_01.setVisibility(View.GONE);
		}
		if ("".equals(data.get(position).getFirstTypeName().toString())) {
			tv_02.setText(data.get(position).getName().toString());
		} else {
			tv_02.setText("["
					+ data.get(position).getFirstTypeName().toString() + "]"
					+ data.get(position).getName().toString());
		}
		tv_03.setText("详情");
		tv_04.setText("规格：" + data.get(position).getSpecText().toString());
		if("".equals(data.get(position).getQualityGradeName().toString()) && "".equals(data.get(position).getQualityTypeName().toString())){
			tv_06.setText("品质：-");
		}else {
			if ("".equals(data.get(position).getQualityGradeName().toString())) {
				tv_06.setText("品质："
						+ data.get(position).getQualityTypeName().toString());
			} else {
				tv_06.setText("品质："
						+ data.get(position).getQualityTypeName().toString() + "("
						+ data.get(position).getQualityGradeName().toString() + ")");
			}
		}
		
		tv_10.setText("地区：" + data.get(position).getCityName().toString());
		tv_07.setText(ValueGetInfo.doubleTrans1(data.get(position).getPrice()));
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MarketDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("MarketPrice", data.get(position));
				intent.putExtras(bundle);
				context.startActivity(intent);

			}
		});

		return inflate;
	}

	public void notify(ArrayList<MarketPrice> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public interface OnGoodsCheckedChangeListener {
		void onGoodsCheckedChange(String id, boolean isRefresh);
	}

	OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;

	public void setOnGoodsCheckedChangeListener(
			OnGoodsCheckedChangeListener onGoodsCheckedChangeListener) {
		this.onGoodsCheckedChangeListener = onGoodsCheckedChangeListener;
	}

}
