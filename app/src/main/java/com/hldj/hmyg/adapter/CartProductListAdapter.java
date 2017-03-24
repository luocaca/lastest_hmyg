package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hy.utils.ValueGetInfo;

/**
 * 
 * 
 * @version 1.0.0
 * 
 */
@SuppressLint("ResourceAsColor")
public class CartProductListAdapter extends BaseAdapter {
	private static final String TAG = "ProductListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public CartProductListAdapter(Context context,
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
				R.layout.list_view_cart_product, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		CheckBox remmber = (CheckBox) inflate.findViewById(R.id.remmber);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		remmber.setChecked((Boolean) data.get(position).get("isCheck"));
		if ("manage_list"
				.equals(data.get(position).get("show_type").toString())) {
			if ("unaudit".equals(data.get(position).get("status").toString())) {
				tv_03.setTextColor(Color.parseColor("#6cd8b0"));
			} else if ("published".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Color.parseColor("#fa7600"));
			} else if ("outline".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Color.parseColor("#93c5fc"));
			} else if ("backed".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Color.parseColor("#b8d661"));
			} else if ("unsubmit".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Color.parseColor("#eb8ead"));
			}
			tv_03.setText(data.get(position).get("statusName").toString());
			tv_05.setText("苗源地址："
					+ data.get(position).get("detailAddress").toString());
			if( data.get(position).get("closeDate").toString().length()>10){
				tv_06.setText("下架日期："
						+ data.get(position).get("closeDate").toString().substring(0, 10));
			}else {
				tv_06.setText("下架日期："
						+ data.get(position).get("closeDate").toString());
			}
		} else if ("seedling_list".equals(data.get(position).get("show_type")
				.toString())) {
			if( data.get(position).get("closeDate").toString().length()>10){
				tv_05.setText("下架日期："
						+ data.get(position).get("closeDate").toString().substring(0, 10));
			}else {
				tv_05.setText("下架日期："
						+ data.get(position).get("closeDate").toString());
			}
			if (!"".equals(data.get(position).get("companyName").toString())) {
				tv_06.setText("发布人："
						+ data.get(position).get("companyName").toString());
			} else if ("".equals(data.get(position).get("companyName")
					.toString())
					&& !"".equals(data.get(position).get("publicName")
							.toString())) {
				tv_06.setText("发布人："
						+ data.get(position).get("publicName").toString());
			} else if ("".equals(data.get(position).get("companyName")
					.toString())
					&& "".equals(data.get(position).get("publicName")
							.toString())) {
				tv_06.setText("发布人："
						+ data.get(position).get("realName").toString());
			}
		}

		tv_02.setText(data.get(position).get("name").toString());
		tv_04.setText(ValueGetInfo.getValueString(data.get(position).get("dbh").toString(), data.get(position).get("height").toString(), data.get(position).get("crown").toString(), data.get(position).get("diameter").toString(), ""));
		
		tv_07.setText(ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("price").toString())));
		tv_08.setText("元/" + data.get(position).get("unitTypeName").toString());
		tv_09.setText(data.get(position).get("count").toString()
				+ data.get(position).get("unitTypeName").toString());
		fb.display(iv_img, data.get(position).get("imageUrl").toString());
//		inflate.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent toFlowerDetailActivity = new Intent(context,
//						FlowerDetailActivity.class);
//				toFlowerDetailActivity.putExtra("id",
//						data.get(position).get("id").toString());
//				toFlowerDetailActivity.putExtra("show_type",
//						data.get(position).get("show_type").toString());
//				context.startActivity(toFlowerDetailActivity);
//				((Activity) context).overridePendingTransition(
//						R.anim.slide_in_left, R.anim.slide_out_right);
//			}
//		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}
}
