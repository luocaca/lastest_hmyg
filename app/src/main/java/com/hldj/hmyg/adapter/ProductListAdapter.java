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
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;

@SuppressLint("ResourceAsColor")
public class ProductListAdapter extends BaseAdapter {
	private static final String TAG = "ProductListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private ImageView iv_like;

	public ProductListAdapter(Context context,
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
		ChildHolder childHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_view_seedling, null);
			childHolder = new ChildHolder();
			childHolder.iv_img = (ImageView) convertView
					.findViewById(R.id.iv_img);
			childHolder.iv_like = (ImageView) convertView
					.findViewById(R.id.iv_like);
			childHolder.tv_01 = (TextView) convertView.findViewById(R.id.tv_01);
			childHolder.tv_02 = (TextView) convertView.findViewById(R.id.tv_02);
			childHolder.tv_03 = (TextView) convertView.findViewById(R.id.tv_03);
			childHolder.tv_04 = (TextView) convertView.findViewById(R.id.tv_04);
			childHolder.tv_05 = (TextView) convertView.findViewById(R.id.tv_05);
			childHolder.tv_06 = (TextView) convertView.findViewById(R.id.tv_06);
			childHolder.tv_07 = (TextView) convertView.findViewById(R.id.tv_07);
			childHolder.tv_08 = (TextView) convertView.findViewById(R.id.tv_08);
			childHolder.tv_09 = (TextView) convertView.findViewById(R.id.tv_09);
			childHolder.tv_floorPrice = (TextView) convertView
					.findViewById(R.id.tv_floorPrice);
			childHolder.rl_floorPrice = (RelativeLayout) convertView
					.findViewById(R.id.rl_floorPrice);
			childHolder.tv_status_01 = (TextView) convertView
					.findViewById(R.id.tv_status_01);
			childHolder.tv_status_02 = (TextView) convertView
					.findViewById(R.id.tv_status_02);
			childHolder.tv_status_03 = (TextView) convertView
					.findViewById(R.id.tv_status_03);
			childHolder.tv_status_04 = (TextView) convertView
					.findViewById(R.id.tv_status_04);
			childHolder.tv_status_05 = (TextView) convertView
					.findViewById(R.id.tv_status_05);

			childHolder.sc_ziying = (ImageView) convertView
					.findViewById(R.id.sc_ziying);
			childHolder.sc_fuwufugai = (ImageView) convertView
					.findViewById(R.id.sc_fuwufugai);
			childHolder.sc_hezuoshangjia = (ImageView) convertView
					.findViewById(R.id.sc_hezuoshangjia);
			childHolder.sc_huodaofukuan = (ImageView) convertView
					.findViewById(R.id.sc_huodaofukuan);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		if (data.get(position).get("tagList").toString().contains(Data.ZIYING)) {
			childHolder.sc_ziying.setVisibility(View.VISIBLE);
		} else {
			childHolder.sc_ziying.setVisibility(View.GONE);
		}
		if (data.get(position).get("tagList").toString().contains(Data.FUWU)) {
			childHolder.sc_fuwufugai.setVisibility(View.VISIBLE);
		} else {
			childHolder.sc_fuwufugai.setVisibility(View.GONE);
		}
		if (data.get(position).get("tagList").toString()
				.contains(Data.HEZUOSHANGJIA)) {
			childHolder.sc_hezuoshangjia.setVisibility(View.VISIBLE);
		} else {
			childHolder.sc_hezuoshangjia.setVisibility(View.GONE);
		}
		if (data.get(position).get("tagList").toString()
				.contains(Data.ZIJINDANBAO)) {
			childHolder.sc_huodaofukuan.setVisibility(View.VISIBLE);
		} else {
			childHolder.sc_huodaofukuan.setVisibility(View.GONE);
		}

		if (data.get(position).get("isSelfSupport").toString().contains("true")) {
			childHolder.tv_status_01.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeValidatePrice").toString()
				.contains("true")) {
			childHolder.tv_status_02.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("cashOnDelivery").toString()
				.contains("true")) {
			childHolder.tv_status_03.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeDeliveryPrice").toString()
				.contains("true")) {
			childHolder.tv_status_04.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("freeValidate").toString().contains("true")) {
			childHolder.tv_status_05.setVisibility(View.VISIBLE);
		}
		if (data.get(position).get("isRecommend").toString().contains("true")) {
			childHolder.iv_like.setImageResource(R.drawable.tuijian_lv);
		} else {
			childHolder.iv_like.setImageResource(R.drawable.tuijian_hui);
		}

		if (data.get(position).get("plantType").toString().contains("planted")) {
			childHolder.tv_01.setBackgroundResource(R.drawable.icon_seller_di);
		} else if (data.get(position).get("plantType").toString()
				.contains("transplant")) {
			childHolder.tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
		} else if (data.get(position).get("plantType").toString()
				.contains("heelin")) {
			childHolder.tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
		} else if (data.get(position).get("plantType").toString()
				.contains("container")) {
			childHolder.tv_01
					.setBackgroundResource(R.drawable.icon_seller_rong);
		} else {
			childHolder.tv_01.setVisibility(View.GONE);
		}
		if ("manage_list"
				.equals(data.get(position).get("show_type").toString())) {

			childHolder.rl_floorPrice.setVisibility(View.VISIBLE);
			if (data.get(position).get("floorPrice") != null) {
				childHolder.tv_floorPrice.setText("底价："
						+ ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("floorPrice").toString())));
			}
			if ("unaudit".equals(data.get(position).get("status").toString())) {
				childHolder.tv_03.setTextColor(Color.parseColor("#6cd8b0"));
			} else if ("published".equals(data.get(position).get("status")
					.toString())) {
				childHolder.tv_03.setTextColor(Color.parseColor("#fa7600"));
				iv_like.setVisibility(View.VISIBLE);
			} else if ("outline".equals(data.get(position).get("status")
					.toString())) {
				childHolder.tv_03.setTextColor(Color.parseColor("#93c5fc"));
			} else if ("backed".equals(data.get(position).get("status")
					.toString())) {
				childHolder.tv_03.setTextColor(Color.parseColor("#b8d661"));
			} else if ("unsubmit".equals(data.get(position).get("status")
					.toString())) {
				childHolder.tv_03.setTextColor(Color.parseColor("#eb8ead"));
			}
			childHolder.tv_03.setText(data.get(position).get("statusName")
					.toString());
			childHolder.tv_05.setText("苗源地址："
					+ data.get(position).get("detailAddress").toString());
			if (data.get(position).get("closeDate").toString().length() > 10) {
				childHolder.tv_06.setText("下架日期："
						+ data.get(position).get("closeDate").toString()
								.substring(0, 10));
			} else {
				childHolder.tv_06.setText("下架日期："
						+ data.get(position).get("closeDate").toString());
			}
		} else if ("seedling_list".equals(data.get(position).get("show_type")
				.toString())) {
			childHolder.tv_05.setText("地区："
					+ data.get(position).get("fullName").toString());
			if (!"".equals(data.get(position).get("companyName").toString())) {
				childHolder.tv_06.setText("发布人："
						+ data.get(position).get("companyName").toString());
			} else if ("".equals(data.get(position).get("companyName")
					.toString())
					&& !"".equals(data.get(position).get("publicName")
							.toString())) {
				childHolder.tv_06.setText("发布人："
						+ data.get(position).get("publicName").toString());
			} else if ("".equals(data.get(position).get("companyName")
					.toString())
					&& "".equals(data.get(position).get("publicName")
							.toString())) {
				childHolder.tv_06.setText("发布人："
						+ data.get(position).get("realName").toString());
			}
		}
		childHolder.tv_02.setText(data.get(position).get("name").toString());
		// tv_04.setText(ValueGetInfo.getValueString(data.get(position).get("dbh")
		// .toString(), data.get(position).get("height").toString(), data
		// .get(position).get("crown").toString(),
		// data.get(position).get("diameter").toString(), ""));
		// tv_04.setText(ValueGetInfo.getValueStringByTag(data.get(position).get("paramsList").toString(),data.get(position).get("dbh")
		// .toString(), data.get(position).get("height").toString(), data
		// .get(position).get("crown").toString(),
		// data.get(position).get("diameter").toString(),
		// data.get(position).get("offbarHeight").toString()));
		childHolder.tv_04
				.setText(data.get(position).get("specText").toString());
		childHolder.tv_07.setText(ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("price").toString())));
		childHolder.tv_08.setText("元/"
				+ data.get(position).get("unitTypeName").toString());
		childHolder.tv_09.setText("库存："
				+ data.get(position).get("count").toString());
		fb.display(childHolder.iv_img, data.get(position).get("imageUrl")
				.toString());
		childHolder.iv_like.setOnClickListener(new OnClickListener() {

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
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("manage_list".equals(data.get(position).get("show_type")
						.toString())) {
					// "published".equals(data.get(position).get("status")
					// .toString())
					Intent toFlowerDetailActivity = new Intent(context,
							FlowerDetailActivity.class);
					toFlowerDetailActivity.putExtra("id", data.get(position)
							.get("id").toString());
					toFlowerDetailActivity.putExtra("show_type",
							data.get(position).get("show_type").toString());
					context.startActivity(toFlowerDetailActivity);
					((Activity) context).overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_right);
				} else {
					Intent toFlowerDetailActivity = new Intent(context,
							FlowerDetailActivity.class);
					toFlowerDetailActivity.putExtra("id", data.get(position)
							.get("id").toString());
					toFlowerDetailActivity.putExtra("show_type",
							data.get(position).get("show_type").toString());
					context.startActivity(toFlowerDetailActivity);
					((Activity) context).overridePendingTransition(
							R.anim.slide_in_left, R.anim.slide_out_right);
				}

			}
		});

		return convertView;
	}

	private class ChildHolder {
		ImageView iv_img;
		ImageView iv_like;
		TextView tv_01;
		TextView tv_02;
		TextView tv_03;
		TextView tv_04;
		TextView tv_05;
		TextView tv_06;
		TextView tv_07;
		TextView tv_08;
		TextView tv_09;
		TextView tv_floorPrice;
		RelativeLayout rl_floorPrice;
		TextView tv_status_01;
		TextView tv_status_02;
		TextView tv_status_03;
		TextView tv_status_04;
		TextView tv_status_05;
		ImageView sc_ziying;
		ImageView sc_fuwufugai;
		ImageView sc_hezuoshangjia;
		ImageView sc_huodaofukuan;
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
