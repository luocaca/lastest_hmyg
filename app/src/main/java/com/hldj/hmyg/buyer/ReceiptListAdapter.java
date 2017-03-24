package com.hldj.hmyg.buyer;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hldj.hmyg.R;

@SuppressLint("ResourceAsColor")
public class ReceiptListAdapter extends BaseAdapter {
	private static final String TAG = "ReceiptListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public ReceiptListAdapter(Context context,
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
				R.layout.list_item_receipt, null);
		TextView tv_ac = (TextView) inflate.findViewById(R.id.tv_ac);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);

		tv_01.setText("到货日期："
				+ data.get(position).get("receiptDate").toString());
		tv_02.setText("数量：" + data.get(position).get("count").toString());
		tv_03.setText("姓名：" + data.get(position).get("contactName").toString());
		tv_04.setText("收货电话："
				+ data.get(position).get("contactPhone").toString());
		tv_05.setText("收货地址："
				+ data.get(position).get("fullAddress").toString());
		tv_06.setText("备注：" + data.get(position).get("remarks").toString());

		if ("BuyerOrderDetailActivity".equals(data.get(position).get("from")
				.toString())) {
			if (!"finished".equals(data.get(position).get("status").toString())
					&& !"unreceipt".equals(data.get(position).get("status")
							.toString())) {
				tv_ac.setVisibility(View.VISIBLE);
			}
		}
		tv_ac.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// 添加收货要求
				Intent toSaveReceipptActivity = new Intent(context,
						SaveReceipptActivity.class);
				toSaveReceipptActivity.putExtra("orderId", data.get(position)
						.get("orderId").toString());
				toSaveReceipptActivity.putExtra("receiptId", data.get(position)
						.get("id").toString());
				toSaveReceipptActivity.putExtra("receiptAddressId",
						data.get(position).get("receiptAddressId").toString());
				toSaveReceipptActivity.putExtra("receiptDate",
						data.get(position).get("receiptDate").toString());
				toSaveReceipptActivity.putExtra("count", data.get(position)
						.get("count").toString());
				toSaveReceipptActivity.putExtra("remarks", data.get(position)
						.get("remarks").toString());
				toSaveReceipptActivity.putExtra("contactName",
						data.get(position).get("contactName").toString());
				toSaveReceipptActivity.putExtra("contactPhone",
						data.get(position).get("contactPhone").toString());
				toSaveReceipptActivity.putExtra("fullAddress",
						data.get(position).get("fullAddress").toString());
				toSaveReceipptActivity.putExtra(
						"allowReceiptInfoCount",
						Integer.parseInt(data.get(position)
								.get("allowReceiptInfoCount").toString()));
				((Activity) context).startActivityForResult(
						toSaveReceipptActivity, 1);
			}
		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
