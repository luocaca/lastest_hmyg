package com.hldj.hmyg.adapter;

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

import com.hldj.hmyg.BrokerOrderDetailActivity;
import com.hldj.hmyg.BrokerSendCarDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.broker.AddCarActivity;

@SuppressLint("ResourceAsColor")
public class BrokerOrderListAdapter extends BaseAdapter {
	private static final String TAG = "BrokerOrderListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public BrokerOrderListAdapter(Context context,
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
				R.layout.list_view_broker_order_item, null);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView remmber = (TextView) inflate.findViewById(R.id.remmber);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_caozuo01 = (TextView) inflate
				.findViewById(R.id.tv_caozuo01);
		TextView tv_caozuo02 = (TextView) inflate
				.findViewById(R.id.tv_caozuo02);
		TextView tv_caozuo03 = (TextView) inflate
				.findViewById(R.id.tv_caozuo03);
		remmber.setText("发货品种："
				+ data.get(position).get("recriptOrderNameText").toString());
		if( data.get(position).get("receiptDate").toString().length()>10){
			tv_01.setText("收货日期："
					+ data.get(position).get("receiptDate").toString().substring(0, 10));
		}else {
			tv_01.setText("收货日期："
					+ data.get(position).get("receiptDate").toString());
		}
		
		tv_03.setText("买家：" + data.get(position).get("displayName").toString());
		tv_05.setText("电话：" + data.get(position).get("displayPhone").toString());
		tv_caozuo02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 873528519
				Intent toAddCarActivity = new Intent(context,
						AddCarActivity.class);
				toAddCarActivity.putExtra("id", data.get(position).get("id")
						.toString());
				context.startActivity(toAddCarActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});
		tv_caozuo03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toBrokerSendCarDetailActivity = new Intent(context,
						BrokerSendCarDetailActivity.class);
				toBrokerSendCarDetailActivity.putExtra("id", data.get(position)
						.get("id").toString());
				context.startActivity(toBrokerSendCarDetailActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		inflate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toBrokerOrderDetailActivity = new Intent(context,
						BrokerOrderDetailActivity.class);
				toBrokerOrderDetailActivity.putExtra("id", data.get(position)
						.get("id").toString());
				toBrokerOrderDetailActivity.putExtra("num", data.get(position)
						.get("num").toString());
				toBrokerOrderDetailActivity.putExtra("receiptDate",
						data.get(position).get("receiptDate").toString());
				toBrokerOrderDetailActivity.putExtra("realName",
						data.get(position).get("realName").toString());
				toBrokerOrderDetailActivity.putExtra("companyName",
						data.get(position).get("companyName").toString());
				toBrokerOrderDetailActivity.putExtra("publicName",
						data.get(position).get("publicName").toString());
				toBrokerOrderDetailActivity.putExtra("publicPhone",
						data.get(position).get("publicPhone").toString());
				toBrokerOrderDetailActivity.putExtra("phone", data
						.get(position).get("phone").toString());
				toBrokerOrderDetailActivity.putExtra("displayPhone",
						data.get(position).get("displayPhone").toString());
				toBrokerOrderDetailActivity.putExtra("displayName",
						data.get(position).get("displayName").toString());
				toBrokerOrderDetailActivity.putExtra("recriptOrderNameText",
						data.get(position).get("recriptOrderNameText")
								.toString());
				toBrokerOrderDetailActivity.putExtra("status",
						data.get(position).get("status").toString());
				toBrokerOrderDetailActivity.putExtra("statusName",
						data.get(position).get("statusName").toString());
				context.startActivity(toBrokerOrderDetailActivity);
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
