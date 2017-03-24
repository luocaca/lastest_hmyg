package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hldj.hmyg.R;

public class BrokerSendCarDetailAdapter extends BaseAdapter {
	private static final String TAG = "BrokerSendCarDetailAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public BrokerSendCarDetailAdapter(Context context,
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
				R.layout.list_item_send_car_detail, null);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_set = (TextView) inflate.findViewById(R.id.tv_set);

		tv_01.setText("车牌号：" + data.get(position).get("carNum").toString());
		tv_02.setText("司机姓名：" + data.get(position).get("driverName").toString());
		tv_03.setText("司机电话："
				+ data.get(position).get("driverPhone").toString());
		tv_04.setText("备注：" + data.get(position).get("remarks").toString());
		if ("loaded".equals(data.get(position).get("status").toString())) {
			tv_set.setVisibility(View.VISIBLE);
		}
		tv_set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
