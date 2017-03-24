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

import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.bingoogolapple.badgeview.BGABadgeViewHelper.BadgeGravity;

import com.hldj.hmyg.MessageListDetailActivity;
import com.hldj.hmyg.R;

@SuppressLint("ResourceAsColor")
public class MessageListAdapter extends BaseAdapter {
	private static final String TAG = "MessageListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	private BGABadgeLinearLayout ll_yanmiao_nei_01;

	public MessageListAdapter(Context context,
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
				R.layout.list_item_message, null);
		ll_yanmiao_nei_01 = (BGABadgeLinearLayout) inflate
				.findViewById(R.id.ll_yanmiao_nei_01);
		TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
		TextView tv_message = (TextView) inflate.findViewById(R.id.tv_message);
		TextView tv_createDateStr = (TextView) inflate
				.findViewById(R.id.tv_createDateStr);
		TextView tv_num = (TextView) inflate.findViewById(R.id.tv_num);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		fb.display(iv_img, data.get(position).get("icon").toString());
		tv_name.setText(data.get(position).get("text").toString());
		if ("".equals(data.get(position).get("message").toString())) {
			tv_message.setVisibility(View.GONE);
		} else {
			tv_message.setText(data.get(position).get("message").toString());
			tv_message.setVisibility(View.VISIBLE);
		}

		tv_createDateStr.setText(data.get(position).get("createDateStr")
				.toString());

		if (Integer.parseInt(data.get(position).get("count").toString()) > 0) {
			ll_yanmiao_nei_01.showTextBadge(data.get(position).get("count")
					.toString());
			ll_yanmiao_nei_01.getBadgeViewHelper().setBadgeGravity(
					BadgeGravity.RightTop);
			tv_num.setText(data.get(position).get("count").toString());
		}
		inflate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toMessageListDetailActivity = new Intent(context,
						MessageListDetailActivity.class);
				toMessageListDetailActivity.putExtra("text", data.get(position)
						.get("text").toString());
				toMessageListDetailActivity.putExtra("count", data
						.get(position).get("count").toString());
				toMessageListDetailActivity.putExtra("value", data
						.get(position).get("value").toString());
				context.startActivity(toMessageListDetailActivity);
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
