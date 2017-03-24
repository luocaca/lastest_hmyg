package com.hldj.hmyg.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weixin_friendcircle.ActionItem;
import com.example.weixin_friendcircle.TitlePopup;
import com.example.weixin_friendcircle.TitlePopup.OnItemOnClickListener;
import com.example.weixin_friendcircle.Util;
import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.BuyerValidateExpandableListAdapter.OnCheckHasGoodsListener;
import com.hldj.hmyg.buyer.BuyerValidateExpandableListAdapter.OnEditingTvChangeListener;
import com.hldj.hmyg.buyer.BuyerValidateExpandableListAdapter.OnGoodsCheckedChangeListener;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;

@SuppressLint("ResourceAsColor")
public class ProductListAdapterForManager extends BaseAdapter {
	private static final String TAG = "ProductListAdapterForManager";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private ImageView iv_like;

	public ProductListAdapterForManager(Context context,
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
				R.layout.manager_group_list_item_parent_o, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		iv_like = (ImageView) inflate.findViewById(R.id.iv_like);
		iv_like.setVisibility(View.GONE);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
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

		if (data.get(position).get("isSelfSupport").toString().contains("true")) {
			tv_status_01.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeValidatePrice").toString()
				.contains("true")) {
			tv_status_02.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("cashOnDelivery").toString()
				.contains("true")) {
			tv_status_03.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeDeliveryPrice").toString()
				.contains("true")) {
			tv_status_04.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeValidate").toString().contains("true")) {
			tv_status_05.setVisibility(View.VISIBLE);
		}

		if (data.get(position).get("isRecommend").toString().contains("true")) {
			iv_like.setImageResource(R.drawable.tuijian_lv);
		} else {
			iv_like.setImageResource(R.drawable.tuijian_hui);
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

		if ("manage_list"
				.equals(data.get(position).get("show_type").toString())) {

			rl_floorPrice.setVisibility(View.VISIBLE);
			if (data.get(position).get("floorPrice") != null) {
				tv_floorPrice.setText("底价："
						+ ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("floorPrice").toString())));
			}
			if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
				tv_floorPrice.setVisibility(View.VISIBLE);
			} else {
				tv_floorPrice.setVisibility(View.INVISIBLE);
			}
			if ("unaudit".equals(data.get(position).get("status").toString())) {
				tv_03.setTextColor(Data.STATUS_GRAY);
			} else if ("published".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Data.STATUS_STROGE_GREEN);
				// iv_like.setVisibility(View.VISIBLE);
				// 暂时将设置推荐功能苗木隐藏
			} else if ("outline".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Data.STATUS_GREEN);
			} else if ("backed".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Data.STATUS_RED);
			} else if ("unsubmit".equals(data.get(position).get("status")
					.toString())) {
				tv_03.setTextColor(Data.STATUS_BLUE);
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
			tv_05.setText("地区：" + data.get(position).get("fullName").toString());
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
		tv_04.setText(data.get(position).get("specText").toString());
		tv_07.setText(ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("price").toString())));
		tv_08.setText("元/" + data.get(position).get("unitTypeName").toString());
		tv_09.setText(data.get(position).get("count").toString()
				+ data.get(position).get("unitTypeName").toString());
		fb.display(iv_img, data.get(position).get("imageUrl").toString());
		iv_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				TitlePopup titlePopup = new TitlePopup(context, Util.dip2px(
						context, 165), Util.dip2px(context, 30));
				if (data.get(position).get("isRecommend").toString()
						.contains("true")) {
					titlePopup.addAction(new ActionItem(context, "取消推荐苗木",
							R.drawable.circle_praise));
				} else {
					titlePopup.addAction(new ActionItem(context, "设为推荐苗木",
							R.drawable.circle_praise));
				}
				titlePopup.addAction(new ActionItem(context, "赞",
						R.drawable.circle_praise));
				titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
				titlePopup.show(v);
				titlePopup.setItemOnClickListener(new OnItemOnClickListener() {

					@Override
					public void onItemClick(ActionItem item, int popo_position) {
						// TODO Auto-generated method stub
						if (data.get(position).get("isRecommend").toString()
								.contains("true")) {
							HashMap<String, Object> hashMap = data
									.get(position);
							iv_like.setImageResource(R.drawable.tuijian_lv);
						} else {
							iv_like.setImageResource(R.drawable.tuijian_hui);
						}
						setRecommend(data.get(position).get("id").toString());

					}
				});

			}

		});

		return inflate;
	}

	public void notify(ArrayList<HashMap<String, Object>> data) {
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

	private void setRecommend(final String id) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/setRecommend",
				params, new AjaxCallBack<Object>() {

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

								onGoodsCheckedChangeListener
										.onGoodsCheckedChange(id, true);
							} else {

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
