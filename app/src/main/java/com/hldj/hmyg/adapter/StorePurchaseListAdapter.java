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
import android.widget.ImageView;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.buyer.PurchaseDetailActivity;
import com.hy.utils.StringFormatUtil;
import com.hy.utils.ValueGetInfo;

@SuppressLint("ResourceAsColor")
public class StorePurchaseListAdapter extends BaseAdapter {
	private static final String TAG = "StorePurchaseListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private View mainView;

	public StorePurchaseListAdapter(Context context,
			ArrayList<HashMap<String, Object>> data, View mainView) {
		this.data = data;
		this.context = context;
		this.mainView = mainView;
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
				R.layout.list_item_store_purchase, null);
		ImageView iv_img2 = (ImageView) inflate.findViewById(R.id.iv_img2);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_ac = (TextView) inflate.findViewById(R.id.tv_ac);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		TextView tv_10 = (TextView) inflate.findViewById(R.id.tv_10);
		TextView tv_11 = (TextView) inflate.findViewById(R.id.tv_11);
		TextView tv_caozuo01 = (TextView) inflate
				.findViewById(R.id.tv_caozuo01);
		TextView tv_caozuo02 = (TextView) inflate
				.findViewById(R.id.tv_caozuo02);
		TextView tv_caozuo03 = (TextView) inflate
				.findViewById(R.id.tv_caozuo03);
		tv_01.setText("[" + data.get(position).get("firstTypeName").toString()
				+ "]" + data.get(position).get("name").toString());
		tv_ac.setText(data.get(position).get("count").toString()
				+ data.get(position).get("unitTypeName").toString());
		tv_02.setText("采购单：" + data.get(position).get("num").toString());
		if (data.get(position).get("cityName") != null) {
			tv_03.setText(data.get(position).get("cityName").toString());
		}
		tv_04.setText("采购单位："
				+ data.get(position).get("displayName").toString());
		tv_02.setVisibility(View.GONE);
		tv_04.setVisibility(View.GONE);
		// tv_05.setText("截至日期：" +
		// data.get(position).get("closeDate").toString());
		// tv_06.setText("分类："
		// + data.get(position).get("firstTypeName").toString());
		// if (Boolean.parseBoolean(data.get(position).get("needInvoice")
		// .toString())) {
		// tv_10.setText("发票要求：需要");
		// } else {
		// tv_10.setText("发票要求：不需要");
		// }
		tv_05.setText(data.get(position).get("specText").toString()
				+ data.get(position).get("remarks").toString());
		tv_07.setText("种植类型："
				+ data.get(position).get("plantTypeName").toString());
		// tv_06.setText("其他要求：" +
		// data.get(position).get("remarks").toString());
		StringFormatUtil fillColor = new StringFormatUtil(context, "已有"
				+ data.get(position).get("quoteCountJson").toString() + "条报价", data.get(position).get("quoteCountJson").toString(),
				R.color.red).fillColor();
		tv_10.setText(fillColor.getResult());
		if (Boolean.parseBoolean(data.get(position).get("isQuoted").toString())) {
			iv_img2.setVisibility(View.VISIBLE);
		} else {
			iv_img2.setVisibility(View.INVISIBLE);
		}
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent toPurchaseDetailActivity = new Intent(context,
						PurchaseDetailActivity.class);
				toPurchaseDetailActivity.putExtra("id",
						data.get(position).get("id").toString());
				toPurchaseDetailActivity.putExtra("hasShowDialog",
						(Boolean) data.get(position).get("hasShowDialog"));
				context.startActivity(toPurchaseDetailActivity);
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
