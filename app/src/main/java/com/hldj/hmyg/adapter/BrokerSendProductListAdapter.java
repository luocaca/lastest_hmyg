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

@SuppressLint("ResourceAsColor")
public class BrokerSendProductListAdapter extends BaseAdapter {
	private static final String TAG = "BrokerSendProductListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public BrokerSendProductListAdapter(Context context,
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
				R.layout.list_item_send_products, null);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);

		tv_01.setText("品种名称：" + data.get(position).get("orderName").toString());
		if( data.get(position).get("receiptDate").toString().length()>10){
			tv_02.setText("到货日期："
					+ data.get(position).get("receiptDate").toString().substring(0, 10));
		}else {
			tv_02.setText("到货日期："
					+ data.get(position).get("receiptDate").toString());
		}
		tv_02.setVisibility(View.GONE);
		tv_04.setText("姓名：" + data.get(position).get("contactName").toString());
		tv_04.setVisibility(View.GONE);
//		tv_03.setText("收货电话："
//				+ data.get(position).get("contactPhone").toString());
		tv_03.setText("品种规格："
				+ data.get(position).get("specText").toString());
		tv_05.setText("装车数量："
				+ data.get(position).get("loadedCountJson").toString() + "/"
				+ data.get(position).get("count").toString());
		tv_06.setText("收货地址："
				+ data.get(position).get("fullAddress").toString());
		inflate.setOnClickListener(new OnClickListener() {
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
