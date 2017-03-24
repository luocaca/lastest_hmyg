package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.saler.ValidateDetailActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;

@SuppressLint("ResourceAsColor")
public class BrokerProductListAdapter extends BaseAdapter {
	private static final String TAG = "BrokerProductListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public BrokerProductListAdapter(Context context,
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
				R.layout.list_view_broker_item, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);

		ImageView sc_ziying = (ImageView) inflate.findViewById(R.id.sc_ziying);
		ImageView sc_fuwufugai = (ImageView) inflate
				.findViewById(R.id.sc_fuwufugai);
		ImageView sc_hezuoshangjia = (ImageView) inflate
				.findViewById(R.id.sc_hezuoshangjia);
		ImageView sc_huodaofukuan = (ImageView) inflate
				.findViewById(R.id.sc_huodaofukuan);
		if (data.get(position).get("tagList").toString().contains(Data.ZIYING)) {
			sc_ziying.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tagList").toString().contains(Data.FUWU)) {
			sc_fuwufugai.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tagList").toString()
				.contains(Data.HEZUOSHANGJIA)) {
			sc_hezuoshangjia.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("tagList").toString()
				.contains(Data.ZIJINDANBAO)) {
			sc_huodaofukuan.setVisibility(View.VISIBLE);
		}
		
		if (data.get(position).get("plantType").toString().contains("planted")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (data.get(position).get("plantType").toString()
				.contains("transplant")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (data.get(position).get("plantType").toString()
				.contains("heelin")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (data.get(position).get("plantType").toString()
				.contains("container")) {
			tv_01.setBackgroundResource(R.drawable.icon_seller_rong);
		} else {
			tv_01.setVisibility(View.GONE);
		}

		tv_02.setText(data.get(position).get("name").toString());
		tv_04.setText(data.get(position).get("specText").toString());
		tv_07.setText(ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("price").toString())));
		tv_08.setText("元/" + data.get(position).get("unitTypeName").toString());
		tv_09.setText(data.get(position).get("applyCount").toString()
				+ data.get(position).get("unitTypeName").toString());

		tv_05.setText("库存：" + data.get(position).get("stockJson").toString()
				);
		if (!"".equals(data.get(position).get("companyName").toString())) {
			tv_06.setText("发布人："
					+ data.get(position).get("companyName").toString());
		} else if ("".equals(data.get(position).get("companyName").toString())
				&& !"".equals(data.get(position).get("publicName").toString())) {
			tv_06.setText("发布人："
					+ data.get(position).get("publicName").toString());
		} else if ("".equals(data.get(position).get("companyName").toString())
				&& "".equals(data.get(position).get("publicName").toString())) {
			tv_06.setText("发布人："
					+ data.get(position).get("realName").toString());
		}
		fb.display(iv_img, data.get(position).get("imageUrl").toString());
		if ("accepted"
				.equals(data.get(position).get("acceptStatus").toString())) {
			tv_03.setVisibility(View.VISIBLE);
		} else {
			tv_03.setVisibility(View.GONE);
		}
		tv_03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				receiveItem(data.get(position).get("id").toString(), position);
			}

		});
		inflate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toValidateDetailActivity = new Intent(context,
						ValidateDetailActivity.class);
				toValidateDetailActivity.putExtra("id",
						data.get(position).get("id").toString());
				toValidateDetailActivity.putExtra("status", data.get(position)
						.get("acceptStatus").toString());
				toValidateDetailActivity.putExtra("tag", "broker");
				((Activity) context).startActivityForResult(
						toValidateDetailActivity, 1);
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

	private void receiveItem(String id, final int position) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("itemIds", id);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/agent/validateApply/receiveItem", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
							if ("1".equals(code)) {
								data.remove(position);
								notifyDataSetChanged();
							} else if ("6007".equals(code)) {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(context, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}
}
