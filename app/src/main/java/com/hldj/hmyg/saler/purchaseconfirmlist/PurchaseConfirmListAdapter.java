package com.hldj.hmyg.saler.purchaseconfirmlist;

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
import com.hldj.hmyg.WebActivity;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;

/**
 * 
 * 
 * 
 */
@SuppressLint("ResourceAsColor")
public class PurchaseConfirmListAdapter extends BaseAdapter {
	private static final String TAG = "PurchaseConfirmListAdapter";

	private ArrayList<PurchaseConfirm> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public PurchaseConfirmListAdapter(Context context,
			ArrayList<PurchaseConfirm> data) {
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
				R.layout.list_item_purchase_confirm, null);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);

		if (data.get(position).getPurchaseDate().length() > 10) {
			tv_01.setText("发货日期："
					+ data.get(position).getPurchaseDate().substring(0, 10));
		}
		if ("confirm".equals(data.get(position).getStatus())) {
			tv_03.setTextColor(context.getResources().getColor(
					R.color.main_color));
		} else {
			tv_03.setTextColor(context.getResources().getColor(R.color.red));
		}
		tv_03.setText(data.get(position).getStatusName());
		tv_04.setText("采购品种：" + data.get(position).getSpceText());
		tv_08.setText("总金额：");
		tv_09.setText(data.get(position).getTotalJson() + "元");
		inflate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!"".equals(data.get(position).getId())){
					Intent toConfirmWebActivity = new Intent(context,
							ConfirmWebActivity.class);
					toConfirmWebActivity.putExtra("title", "对账单");
					toConfirmWebActivity.putExtra("url", GetServerUrl.getPurchaseconfirm()+data.get(position).getId()+"&u="+MyApplication.Userinfo.getString("id", ""));
					((Activity) context).startActivityForResult(toConfirmWebActivity,
							1);
				}
			}
		});
		return inflate;
	}

	public void notify(ArrayList<PurchaseConfirm> data) {
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
