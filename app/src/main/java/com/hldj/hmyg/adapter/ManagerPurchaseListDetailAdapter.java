package com.hldj.hmyg.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.buyer.AddPurchaseActivity;
import com.hldj.hmyg.buyer.QuoteListActivity;
import com.hy.utils.ValueGetInfo;

@SuppressLint("ResourceAsColor")
public class ManagerPurchaseListDetailAdapter extends BaseAdapter {
	private static final String TAG = "ManagerPurchaseListDetailAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private ImageView iv_like;
	private View mainView;

	private LinearLayout ll_caozuo;

	public ManagerPurchaseListDetailAdapter(Context context,
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
				R.layout.list_item_manager_purchase_detail, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		ll_caozuo = (LinearLayout) inflate.findViewById(R.id.ll_caozuo);
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
		tv_ac.setText(data.get(position).get("count").toString()
				+ data.get(position).get("unitTypeName").toString());
		tv_02.setText("分类："
				+ data.get(position).get("firstTypeName").toString());
		tv_04.setText("种植类型："
				+ data.get(position).get("plantTypeName").toString());
		tv_06.setText(ValueGetInfo.getValueString(data.get(position).get("dbh")
				.toString(), data.get(position).get("height").toString(), data
				.get(position).get("crown").toString(),
				data.get(position).get("diameter").toString(), ""));

		tv_08.setText("报价数目："
				+ data.get(position).get("quoteCountJson").toString());
		if ((Integer) data.get(position).get("quoteCountJson") <= 0) {
			tv_09.setVisibility(View.INVISIBLE);
		}
		if ("closed".equals(data.get(position).get("status").toString()) ||"published".equals(data.get(position).get("status").toString())  ||"unaudit".equals(data.get(position).get("status").toString()) ) {
			ll_caozuo.setVisibility(View.GONE);
		} else{
			ll_caozuo.setVisibility(View.VISIBLE);
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
		tv_caozuo02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 编辑

				Intent toAddPurchaseActivity = new Intent(context,
						AddPurchaseActivity.class);
				toAddPurchaseActivity.putExtra("id",
						data.get(position).get("id").toString());
				toAddPurchaseActivity.putExtra("purchaseId", data.get(position)
						.get("purchaseId").toString());
				toAddPurchaseActivity.putExtra("name",
						data.get(position).get("name").toString());
				toAddPurchaseActivity.putExtra("seedlingParams",
						data.get(position).get("seedlingParams").toString());
				toAddPurchaseActivity.putExtra("firstSeedlingTypeId",
						data.get(position).get("firstSeedlingTypeId")
								.toString());
				toAddPurchaseActivity.putExtra("firstTypeName",
						data.get(position).get("firstTypeName").toString());
				toAddPurchaseActivity.putExtra("count",
						data.get(position).get("count").toString());
				toAddPurchaseActivity.putExtra("unitType", data.get(position)
						.get("unitType").toString());
				toAddPurchaseActivity.putExtra("plantType", data.get(position)
						.get("plantType").toString());
				toAddPurchaseActivity.putExtra("diameter", data.get(position)
						.get("diameter").toString());
				toAddPurchaseActivity.putExtra("diameterType",
						data.get(position).get("diameterType").toString());
				toAddPurchaseActivity.putExtra("dbh",
						data.get(position).get("dbh").toString());
				toAddPurchaseActivity.putExtra("dbhType", data.get(position)
						.get("dbhType").toString());
				toAddPurchaseActivity.putExtra("height", data.get(position)
						.get("height").toString());
				toAddPurchaseActivity.putExtra("crown",
						data.get(position).get("crown").toString());
				toAddPurchaseActivity.putExtra("offbarHeight",
						data.get(position).get("offbarHeight").toString());
				toAddPurchaseActivity.putExtra("length", data.get(position)
						.get("length").toString());
				toAddPurchaseActivity.putExtra("remarks", data.get(position)
						.get("remarks").toString());
				((Activity) context).startActivityForResult(
						toAddPurchaseActivity, 1);
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
