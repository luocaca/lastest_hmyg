package com.hldj.hmyg.saler;

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

import com.hldj.hmyg.R;
import com.hldj.hmyg.buyer.PurchaseDetailActivity;
import com.hldj.hmyg.buyer.QuoteListActivity;
import com.hy.utils.ValueGetInfo;

@SuppressLint("ResourceAsColor")
public class ManagerQuoteListAdapter extends BaseAdapter {
	private static final String TAG = "ManagerQuoteListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private ImageView iv_like;
	private View mainView;

	public ManagerQuoteListAdapter(Context context,
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
				R.layout.list_item_manager_quote, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		iv_like = (ImageView) inflate.findViewById(R.id.iv_like);
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
		TextView tv_caozuo01 = (TextView) inflate
				.findViewById(R.id.tv_caozuo01);
		TextView tv_caozuo02 = (TextView) inflate
				.findViewById(R.id.tv_caozuo02);
		TextView tv_caozuo03 = (TextView) inflate
				.findViewById(R.id.tv_caozuo03);
		tv_01.setText("品种名称：" + data.get(position).get("name").toString());
		tv_ac.setText(data.get(position).get("statusName").toString());
		if (!"".equals(data.get(position).get("companyName").toString())) {
			tv_02.setText("采购商家："
					+ data.get(position).get("companyName").toString());
		} else if ("".equals(data.get(position).get("companyName").toString())
				&& !"".equals(data.get(position).get("publicName").toString())) {
			tv_02.setText("采购商家："
					+ data.get(position).get("publicName").toString());
		} else if ("".equals(data.get(position).get("companyName").toString())
				&& "".equals(data.get(position).get("publicName").toString())) {
			tv_02.setText("采购商家："
					+ data.get(position).get("realName").toString());
		}
		tv_03.setText("用苗地：" + data.get(position).get("address").toString());
		tv_05.setText("分类："
				+ data.get(position).get("firstTypeName").toString());
		tv_04.setText("种植类型："
				+ data.get(position).get("plantTypeName").toString());
		tv_06.setText(ValueGetInfo.getValueString(data.get(position).get("dbh")
				.toString(), data.get(position).get("height").toString(), data
				.get(position).get("crown").toString(),
				data.get(position).get("diameter").toString(), ""));

		tv_08.setText("价格：" + ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("price").toString())));
		tv_09.setText("数量：" + data.get(position).get("count").toString());
		if ("unsubmit".equals(data.get(position).get("status").toString())) {
			tv_ac.setTextColor(Color.parseColor("#179bed"));
		} else if ("unaudit"
				.equals(data.get(position).get("status").toString())) {
			tv_ac.setTextColor(Color.parseColor("#ff6601"));
		} else if ("quoted".equals(data.get(position).get("status").toString())) {
			tv_ac.setTextColor(Color.parseColor("#00843c"));
		} else if ("backed".equals(data.get(position).get("status").toString())) {
			tv_ac.setTextColor(Color.parseColor("#ff0000"));
		} else if ("invalid"
				.equals(data.get(position).get("status").toString())) {
			tv_ac.setTextColor(Color.parseColor("#999999"));
		}
		tv_09.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 跳转查看报价
				Intent toQuoteListActivity = new Intent(context,
						QuoteListActivity.class);
				toQuoteListActivity.putExtra("purchaseItemId",
						data.get(position).get("id").toString());
				toQuoteListActivity.putExtra("purchaseId", data.get(position)
						.get("purchaseId").toString());
				((Activity) context).startActivityForResult(
						toQuoteListActivity, 1);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

			}
		});
		inflate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 编辑

				Intent toPurchaseDetailActivity = new Intent(context,
						PurchaseDetailActivity.class);
				toPurchaseDetailActivity.putExtra("id",
						data.get(position).get("id").toString());
				toPurchaseDetailActivity.putExtra("tag", "Quote");
				context.startActivity(toPurchaseDetailActivity);
				((Activity) context).overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);

				// Intent toPurchaseDetailActivity = new Intent(context,
				// QuoteDetailActivity.class);
				// toPurchaseDetailActivity.putExtra("id",
				// data.get(position).get("id").toString());
				// toPurchaseDetailActivity.putExtra("name",
				// data.get(position).get("name").toString());
				// toPurchaseDetailActivity.putExtra("count",
				// data.get(position).get("count").toString());
				// toPurchaseDetailActivity.putExtra("unitTypeName",
				// data.get(position).get("unitTypeName").toString());
				// toPurchaseDetailActivity.putExtra("num",
				// data.get(position).get("num").toString());
				// toPurchaseDetailActivity.putExtra("fullName",
				// data.get(position).get("fullName").toString());
				// toPurchaseDetailActivity.putExtra("receiptDate",
				// data.get(position).get("receiptDate").toString());
				// toPurchaseDetailActivity.putExtra("closeDate",
				// data.get(position).get("closeDate").toString());
				// toPurchaseDetailActivity.putExtra("firstTypeName",
				// data.get(position).get("firstTypeName").toString());
				// toPurchaseDetailActivity.putExtra("plantTypeName",
				// data.get(position).get("plantTypeName").toString());
				// toPurchaseDetailActivity.putExtra("remarks",
				// data.get(position).get("remarks").toString());
				// toPurchaseDetailActivity.putExtra("diameter",
				// data.get(position).get("diameter").toString());
				// toPurchaseDetailActivity.putExtra("height",
				// data.get(position).get("height").toString());
				// toPurchaseDetailActivity.putExtra("crown",
				// data.get(position).get("crown").toString());
				// toPurchaseDetailActivity.putExtra("quoteCountJson",
				// data.get(position).get("quoteCountJson").toString());
				// context.startActivity(toPurchaseDetailActivity);
				// ((Activity) context).overridePendingTransition(
				// R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}
}
