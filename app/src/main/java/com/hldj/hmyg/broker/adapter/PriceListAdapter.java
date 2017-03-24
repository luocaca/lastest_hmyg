package com.hldj.hmyg.broker.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.GalleryImageActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.MarketDetailActivity;
import com.hldj.hmyg.broker.SaveMarketPriceActivity;
import com.hldj.hmyg.broker.bean.MarketPrice;
import com.hldj.hmyg.broker.bean.Price;
import com.hldj.hmyg.broker.bean.SellectPrice;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;

/**
 * 
 * 
 * 
 */
@SuppressLint("ResourceAsColor")
public class PriceListAdapter extends BaseAdapter {
	private static final String TAG = "ProductListAdapter";

	private ArrayList<Price> data = null;

	private Context context = null;
	private SellectPrice sellectPrice = new SellectPrice();
	private MarketPrice marketPrice = new MarketPrice();
	private FinalBitmap fb;

	private ImageView iv_like;

	public PriceListAdapter(Context context, ArrayList<Price> data,
			SellectPrice sellectPrice, MarketPrice marketPrice) {
		this.data = data;
		this.context = context;
		this.sellectPrice = sellectPrice;
		this.marketPrice = marketPrice;
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
				R.layout.list_item_price, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		iv_like = (ImageView) inflate.findViewById(R.id.iv_like);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		ll_reason = (LinearLayout) inflate.findViewById(R.id.ll_reason);
		reason = (TextView) inflate.findViewById(R.id.reason);

		TextView tv_pu_time = (TextView) inflate.findViewById(R.id.tv_pu_time);
		TextView tv_floorPrice = (TextView) inflate
				.findViewById(R.id.tv_floorPrice);
		RelativeLayout rl_floorPrice = (RelativeLayout) inflate
				.findViewById(R.id.rl_floorPrice);
		TextView tv_status_01 = (TextView) inflate
				.findViewById(R.id.tv_status_01);
		TextView tv_status_02 = (TextView) inflate
				.findViewById(R.id.tv_status_02);
		TextView tv_status_03 = (TextView) inflate
				.findViewById(R.id.tv_status_03);
		TextView tv_status_04 = (TextView) inflate
				.findViewById(R.id.tv_status_04);
		TextView tv_status_05 = (TextView) inflate
				.findViewById(R.id.tv_status_05);

		ImageView sc_ziying = (ImageView) inflate.findViewById(R.id.sc_ziying);
		ImageView sc_fuwufugai = (ImageView) inflate
				.findViewById(R.id.sc_fuwufugai);
		ImageView sc_hezuoshangjia = (ImageView) inflate
				.findViewById(R.id.sc_hezuoshangjia);
		ImageView sc_huodaofukuan = (ImageView) inflate
				.findViewById(R.id.sc_huodaofukuan);
		tv_01.setText("[" + data.get(position).getStatusName() + "]");
		if ("published".equals(data.get(position).getStatus())) {// 最新价格
			tv_01.setTextColor(context.getResources().getColor(R.color.orange));
			ll_reason.setVisibility(View.GONE);
		} else if ("unaudit".equals(data.get(position).getStatus())) {// 待审核
			tv_01.setTextColor(context.getResources().getColor(
					R.color.main_color));
			ll_reason.setVisibility(View.GONE);
		} else if ("backed".equals(data.get(position).getStatus())) {// 被退回
			tv_01.setTextColor(context.getResources().getColor(R.color.red));
			tv_03.setText("编辑");
			if (!"".equals(data.get(position).getRemarks())) {
				ll_reason.setVisibility(View.VISIBLE);
				reason.setText("退回理由：" + data.get(position).getRemarks());
			}
		} else if ("history".equals(data.get(position).getStatus())) { // 历史价格
			tv_01.setTextColor(context.getResources().getColor(R.color.blue2));
			ll_reason.setVisibility(View.GONE);
		} else if ("tovoid".equals(data.get(position).getStatus())
				&& MyApplication.Userinfo.getString("id", "").equals(
						data.get(position).getCreateBy())) { // 作废
			tv_01.setTextColor(context.getResources().getColor(R.color.gray));
			tv_03.setText("删除");
			ll_reason.setVisibility(View.GONE);
		}
		tv_02.setText(ValueGetInfo.doubleTrans1(data.get(position).getPrice()));
		if ("backed".equals(data.get(position).getStatus())
				|| "tovoid".equals(data.get(position).getStatus())) {
			tv_03.setVisibility(View.VISIBLE);
		} else {
			tv_03.setVisibility(View.GONE);
		}
		tv_04.setText("发布时间：" + data.get(position).getCreateDate());
		fb.display(iv_img, data.get(position).getImageUrl().toString());
		tv_03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("published".equals(data.get(position).getStatus())) {// 最新价格
				} else if ("unaudit".equals(data.get(position).getStatus())) {// 待审核
				} else if ("backed".equals(data.get(position).getStatus())) {// 被退回
					// tv_03.setText("编辑");
					Intent intent = new Intent(context,
							SaveMarketPriceActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("SellectPrice", sellectPrice);
					bundle.putSerializable("MarketPrice", marketPrice);
					bundle.putSerializable("Price", data.get(position));
					intent.putExtras(bundle);
					((Activity)context).startActivityForResult(intent, 3);
				} else if ("history".equals(data.get(position).getStatus())) { // 历史价格
				} else if ("tovoid".equals(data.get(position).getStatus())
						&& MyApplication.Userinfo.getString("id", "").equals(
								data.get(position).getCreateBy())) { // 作废
					// tv_03.setText("删除");
					doDel(data.get(position).getId(), position);
				}
			}
		});
		iv_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (data.get(position).getSeedlingTypeJson().size() > 0) {
					GalleryImageActivity.startGalleryImageActivity(context, 0,
							data.get(position).getSeedlingTypeJson());
				}
			}
		});

		return inflate;
	}

	public void notify(ArrayList<Price> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public interface OnGoodsCheckedChangeListener {
		void onGoodsCheckedChange(String id, boolean isRefresh);
	}

	OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;

	private LinearLayout ll_reason;

	private TextView reason;

	public void setOnGoodsCheckedChangeListener(
			OnGoodsCheckedChangeListener onGoodsCheckedChangeListener) {
		this.onGoodsCheckedChangeListener = onGoodsCheckedChangeListener;
	}

	private void doDel(final String id, final int pos) {

		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/seedlingMarketPrice/doDel", params,
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
							}
							if ("1".equals(code)) {
								onGoodsCheckedChangeListener
										.onGoodsCheckedChange(id, true);
							} else {
								Toast.makeText(context, "删除失败",
										Toast.LENGTH_SHORT).show();
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
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

}
