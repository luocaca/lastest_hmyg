package com.hldj.hmyg.jimiao;

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

/**
 * 
 * 
 * 
 */
@SuppressLint("ResourceAsColor")
public class MiaoNoteListAdapter extends BaseAdapter {
	private static final String TAG = "MiaoNoteListAdapter";

	private ArrayList<HashMap<String, Object>> data = null;

	private Context context = null;
	private FinalBitmap fb;

	public MiaoNoteListAdapter(Context context,
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
				R.layout.list_item_note_miao, null);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		tv_01.setText(data.get(position).get("name").toString());
		tv_03.setText(data.get(position).get("fullName").toString());
		tv_05.setText(ValueGetInfo.doubleTrans1(Double.parseDouble(data.get(position).get("price").toString())));
		tv_07.setText(data.get(position).get("count").toString());
		String minSpec = data.get(position).get("minSpec").toString();
		String maxSpec = data.get(position).get("maxSpec").toString();
		if ("0".equals(minSpec)) {
			minSpec = "";
		}
		if ("0".equals(maxSpec)) {
			maxSpec = "";
		}

		String height = data.get(position).get("height").toString();
		String maxHeight = data.get(position).get("maxHeight").toString();
		if ("0".equals(height)) {
			height = "";
		}
		if ("0".equals(maxHeight)) {
			maxHeight = "";
		}

		String crown = data.get(position).get("crown").toString();
		String maxCrown = data.get(position).get("maxCrown").toString();
		if ("0".equals(crown)) {
			crown = "";
		}
		if ("0".equals(maxCrown)) {
			maxCrown = "";
		}

		tv_08.setText("规格：" + minSpec + "-" + maxSpec + "\u0020\u0020" + "高度："
				+ height + "-" + maxHeight + "\u0020\u0020" + "冠幅：" + crown
				+ "-" + maxCrown);

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

}
