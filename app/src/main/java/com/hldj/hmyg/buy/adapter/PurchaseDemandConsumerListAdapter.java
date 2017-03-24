package com.hldj.hmyg.buy.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hldj.hmyg.BrokerActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.buy.bean.PurchaseDemandConsumer;
import com.hldj.hmyg.buyer.PurchaseDemandListActivity;

/**
 * 
 * 
 * 
 */
@SuppressLint("ResourceAsColor")
public class PurchaseDemandConsumerListAdapter extends BaseAdapter {
	private static final String TAG = "PurchaseDemandConsumerListAdapter";

	private ArrayList<PurchaseDemandConsumer> data = null;

	private Context context = null;
	private String tag = "";

	private ImageView iv_like;

	public PurchaseDemandConsumerListAdapter(Context context,
			ArrayList<PurchaseDemandConsumer> data, String tag) {
		this.tag = tag;
		this.data = data;
		this.context = context;
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
				R.layout.list_item_purchase_demand_consumer, null);
		LinearLayout ll_item = (LinearLayout) inflate
				.findViewById(R.id.ll_item);
		TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
		tv_name.setText(data.get(position).getName());
		tv_name.setTextColor(context.getResources()
				.getColor(R.color.main_color));
		ll_item.setBackgroundResource(R.drawable.green_btn_selector);
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toPurchaseDemandListActivity = new Intent(context,
						PurchaseDemandListActivity.class);
				toPurchaseDemandListActivity.putExtra("tag", "clerk");
				toPurchaseDemandListActivity.putExtra("name", data
						.get(position).getName());
				toPurchaseDemandListActivity.putExtra("consumerId",
						data.get(position).getId());
				((Activity) context).startActivityForResult(
						toPurchaseDemandListActivity, 6);
			}
		});
		return inflate;
	}

	public void notify(ArrayList<PurchaseDemandConsumer> data) {
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
