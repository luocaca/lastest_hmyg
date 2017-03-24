package com.hldj.hmyg.buy.adapter;

import java.util.ArrayList;

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

import com.hldj.hmyg.R;
import com.hldj.hmyg.buy.bean.PurchaseDemand;
import com.hldj.hmyg.buyer.ManagerPurchaseListActivity;
import com.hldj.hmyg.buyer.PurchaseDemandItem4BuyActivity;
import com.hldj.hmyg.buyer.PurchaseDemandItemActivity;
import com.hy.utils.StringFormatUtil;

/**
 * 
 * 
 * 
 */
@SuppressLint("ResourceAsColor")
public class PurchaseDemandListAdapter extends BaseAdapter {
	private static final String TAG = "PurchaseDemandListAdapter";

	private ArrayList<PurchaseDemand> data = null;

	private Context context = null;
	private String tag = "";
	private String consumerId = "";
	private FinalBitmap fb;

	private ImageView iv_like;

	public PurchaseDemandListAdapter(Context context,
			ArrayList<PurchaseDemand> data, String tag, String consumerId) {
		this.tag = tag;
		this.data = data;
		this.context = context;
		this.consumerId = consumerId;
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
				R.layout.list_item_purchase_demand, null);
		TextView tv_00 = (TextView) inflate.findViewById(R.id.tv_00);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		TextView tv_status = (TextView) inflate.findViewById(R.id.tv_status);
		tv_00.setText("项目名称：");
		tv_02.setText("项目地址：");
		tv_04.setText("品种数量：");
		tv_06.setText("买       家：");
		tv_08.setText("联系电话：");
		tv_01.setText(data.get(position).getProjectName());
		tv_03.setText(data.get(position).getAddress());
		StringFormatUtil fillColor = new StringFormatUtil(context, data.get(
				position).getItemCount()
				+ "种（已确认" + data.get(position).getUsedCountJson() + "种）", data
				.get(position).getUsedCountJson() + "", R.color.red)
				.fillColor();
		tv_05.setText(fillColor.getResult());
		// tv_05.setText(data.get(position).getItemCount()+"种（已确认）"+data.get(position).getUsedCountJson()+"）");
		tv_07.setText(data.get(position).getBuyerName());
		tv_09.setText(data.get(position).getBuyerPhone());
		if ("unSubmit".equals(data.get(position).getStatus())) {
			tv_status.setTextColor(Color.parseColor("#179BED"));
		} else if ("quote".equals(data.get(position).getStatus())) {
			tv_status.setTextColor(Color.parseColor("#4CAF50"));
		} else if ("confirm".equals(data.get(position).getStatus())) {
			tv_status.setTextColor(Color.parseColor("#00843C"));
		} else if ("finished".equals(data.get(position).getStatus())) {
			tv_status.setTextColor(Color.parseColor("#FF6600"));
		}
		tv_status.setText(data.get(position).getStatusName());
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("clerk".equals(tag) && "".equals(consumerId)) {
					Intent toPurchaseDemandItemActivity = new Intent(context,
							PurchaseDemandItemActivity.class);
					toPurchaseDemandItemActivity.putExtra("demandId",
							data.get(position).getId());
					toPurchaseDemandItemActivity.putExtra("projectName", data
							.get(position).getProjectName());
					toPurchaseDemandItemActivity.putExtra("status",
							data.get(position).getStatus());
					((Activity) context).startActivityForResult(
							toPurchaseDemandItemActivity, 1);
				} else if ("clerk".equals(tag) && !"".equals(consumerId)) {
					Intent toPurchaseDemandItemActivity = new Intent(context,
							PurchaseDemandItem4BuyActivity.class);
					toPurchaseDemandItemActivity.putExtra("demandId",
							data.get(position).getId());
					toPurchaseDemandItemActivity.putExtra("projectName", data
							.get(position).getProjectName());
					toPurchaseDemandItemActivity.putExtra("status",
							data.get(position).getStatus());
					toPurchaseDemandItemActivity.putExtra("tag", tag);
					((Activity) context).startActivityForResult(
							toPurchaseDemandItemActivity, 1);
				} else if ("buyer".equals(tag)) {
					Intent toPurchaseDemandItemActivity = new Intent(context,
							PurchaseDemandItem4BuyActivity.class);
					toPurchaseDemandItemActivity.putExtra("demandId",
							data.get(position).getId());
					toPurchaseDemandItemActivity.putExtra("projectName", data
							.get(position).getProjectName());
					toPurchaseDemandItemActivity.putExtra("status",
							data.get(position).getStatus());
					toPurchaseDemandItemActivity.putExtra("tag", tag);
					((Activity) context).startActivityForResult(
							toPurchaseDemandItemActivity, 1);
				}

			}
		});
		return inflate;
	}

	public void notify(ArrayList<PurchaseDemand> data) {
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
