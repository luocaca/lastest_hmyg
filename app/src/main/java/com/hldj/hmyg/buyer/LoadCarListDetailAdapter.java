package com.hldj.hmyg.buyer;

import java.util.ArrayList;

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
public class LoadCarListDetailAdapter extends BaseAdapter {
	private static final String TAG = "LoadCarListDetailAdapter";

	private ArrayList<itemListJson> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public LoadCarListDetailAdapter(Context context, ArrayList<itemListJson> data) {
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
				R.layout.list_item_loadcar_detail, null);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);

		tv_01.setText("收货时间：" + data.get(position).getReceiptDate());
		tv_02.setText("订单编号：" + data.get(position).getNum());
		tv_03.setText("品种名称：" + data.get(position).getName());
		tv_04.setText("装车数量：" + data.get(position).getCount());
		inflate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		return inflate;
	}

	public void notify(ArrayList<itemListJson> data) {
		this.data = data;
		notifyDataSetChanged();
	}


}
